import time
import requests
import json
import re
import api.api_token as api_token

# ========== CONFIGURATION ==========
GEMINI_API_KEY = api_token.gemini_key()
GEMINI_API_URL = f"https://generativelanguage.googleapis.com/v1beta/models/gemini-2.0-flash:generateContent?key={GEMINI_API_KEY}"
HEADERS = {"Content-Type": "application/json"}
MAX_RETRIES = 5
INITIAL_RETRY_DELAY = 5
BACKOFF_FACTOR = 2

# ========== UTILS ==========
def print_separator(char="-", length=5):
    print(char * length)


def print_section(title, content):
    print(f"\n{title}\n{'-' * len(title)}\n{content}\n")


def call_gemini_with_retry(prompt, context_label="RECOMMENDATIONS", return_text=True):
    """Appelle Gemini avec gestion des erreurs et retries"""
    payload = {"contents": [{"parts": [{"text": prompt}]}]}
    retry_delay = INITIAL_RETRY_DELAY
    # attempt = _
    for _ in range(MAX_RETRIES):
        response = requests.post(GEMINI_API_URL, headers=HEADERS, json=payload, timeout=15)

        if response.status_code == 200:
            result = response.json()
            text = result["candidates"][0]["content"]["parts"][0]["text"].strip()
            if return_text:
                return text
            else:
                print(f"\n{'='*5} {context_label} {'='*5}")
                print(text)
                print(f"{'='*20}\n")
                return
        elif response.status_code == 429:
            print(f"[{context_label}] Rate limit exceeded. Retrying in {retry_delay}s...")
            time.sleep(retry_delay)
            retry_delay *= BACKOFF_FACTOR
        else:
            print(f"[{context_label}] Error {response.status_code}: {response.text}")
            return "ERROR"
    return "FAILED"

def build_recommendation_prompt(test_result):
    """
    Prompt optimisé pour donner des métiers connus et formations adaptées.
    """
    # Convert dict to readable string
    test_result_text = ", ".join(f"{k}: {v}%" for k, v in test_result.items())

    return f"""
Tu es conseiller d'orientation. On va te donner le résultat d’un test RIASEC d’un étudiant : {test_result_text}.

⚠️ Priorise les métiers **largement connus et communs** (ex: enseignant, ingénieur, médecin, architecte, avocat, infirmier, etc.) et accessibles au Maroc. 
Ne détaille pas des métiers trop techniques ou très spécialisés (ex: mécanicien, électricien, technicien industriel).

Tâche :
1. Proposer 2 à 5 métiers **largement connus et communs** correspondant au profil.
   - title (nom du métier)
   - description (1-2 phrases)
   - category (HEALTH, EDUCATION, TECH, BUSINESS, ARTS)
   - jobMarket
   - education
   - salaryRange (format strict : "min - max CURRENCY/period", ex: "10,000 - 25,000 MAD/month")
   - tags (3-5 mots clés)
2. Proposer 2 à 5 formations adaptées pour accéder à ces métiers :
   - name
   - type (UNIVERSITY, VOCATIONAL, BOOTCAMP, CERTIFICATION, ONLINE_COURSE,
           INTERNSHIP, APPRENTICESHIP, WORKSHOP, SEMINAR, SELF_TAUGHT)
   - description
   - duration
   - specializations (2-4 max)

Réponds uniquement en JSON valide avec ce format :

{{
  "jobs": [
    {{
      "title": "Médecin",
      "description": "Diagnostique et soigne les patients, pratique des examens médicaux...",
      "category": "HEALTH",
      "jobMarket": "En forte demande",
      "education": "Doctorat en médecine",
      "salaryRange": "50,000 - 150,000 MAD/month",
      "tags": ["Santé", "Patients", "Diagnostic"]
    }}
  ],
  "trainings": [
    {{
      "name": "Faculté de Médecine",
      "type": "UNIVERSITY",
      "description": "Formation pour devenir médecin généraliste ou spécialiste",
      "duration": "7 ans",
      "specializations": ["Médecine générale", "Spécialisation médicale"]
    }}
  ]
}}
"""

# ========== MAIN LOGIC ==========
def generate_recommendations(test_result):
    prompt = build_recommendation_prompt(test_result)
    result = call_gemini_with_retry(prompt, context_label="RECOMMENDATIONS", return_text=True)

    if result and result not in ["ERROR", "FAILED"]:
        match = re.search(r'(\{.*\})', result, re.DOTALL)
        if match:
            json_text = match.group(1)
            try:
                parsed_json = json.loads(json_text)
                return parsed_json
            except json.JSONDecodeError:
                return {"error": "Impossible de parser le JSON", "raw": json_text}
        else:
            return {"error": "Aucun JSON trouvé", "raw": result}
    else:
        return {"error": "Erreur lors de la génération des recommandations"}