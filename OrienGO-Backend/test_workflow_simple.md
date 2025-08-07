# Workflow de test simple - Créer, compléter et voir le score

## Étape 1 : Créer un test FAST

**Méthode :** POST  
**URL :** `http://localhost:8080/api/tests/start?type=FAST`  
**Headers :** `Content-Type: application/json`  
**Body :**
```json
{
  "firstName": "Test",
  "lastName": "User",
  "email": "test.user@email.com",
  "phoneNumber": "0123456789",
  "age": "25",
  "gender": "MALE",
  "school": "Université Test",
  "fieldOfStudy": "Informatique",
  "educationLevel": "BACHELOR",
  "location": "Paris, France",
  "profileVisibility": "PUBLIC",
  "accountPrivacy": "PUBLIC",
  "messagePermission": "ALLOWED"
}
```

**Réponse attendue :**
```json
{
  "id": 1001,
  "type": "FAST",
  "status": "PENDING",
  "startedAt": "2025-01-07T...",
  "completedAt": null,
  "durationMinutes": 0,
  "questionsCount": 18,
  "softDeleted": false,
  "studentId": 1001,
  "questions": [...]
}
```

**⚠️ IMPORTANT :** Notez le `id` du test (ex: 1001) et le `studentId` (ex: 1001)

---

## Étape 2 : Soumettre le test (le compléter)

**Méthode :** POST  
**URL :** `http://localhost:8080/api/tests/1001/submit` (remplacez 1001 par votre testId)  
**Headers :** `Content-Type: application/json`  
**Body :**
```json
{
  "1": 4, "2": 5, "3": 3, "4": 4, "5": 5, "6": 2,
  "7": 4, "8": 3, "9": 5, "10": 4, "11": 3, "12": 4,
  "13": 5, "14": 2, "15": 4, "16": 3, "17": 5, "18": 4
}
```

**Réponse attendue :**
```json
{
  "id": 2001,
  "test": {
    "id": 1001,
    "type": "FAST",
    "status": "COMPLETED"
  },
  "dominantType": "REALISTIC",
  "dominantTypeDescription": "REALISTIC",
  "scores": {
    "REALISTIC": 85,
    "INVESTIGATIVE": 60,
    "ARTISTIC": 40,
    "SOCIAL": 55,
    "ENTERPRISING": 45,
    "CONVENTIONAL": 50
  },
  "keyPoints": "Key points based on your REALISTIC profile",
  "shared": false,
  "downloaded": false,
  "softDeleted": false
}
```

---

## Étape 3 : Voir le score (vérifier le résultat)

**Méthode :** GET  
**URL :** `http://localhost:8080/api/test-results/test/1001` (remplacez 1001 par votre testId)

**Réponse attendue :**
```json
{
  "id": 2001,
  "test": {
    "id": 1001,
    "type": "FAST",
    "status": "COMPLETED"
  },
  "dominantType": "REALISTIC",
  "dominantTypeDescription": "REALISTIC",
  "scores": {
    "REALISTIC": 85,
    "INVESTIGATIVE": 60,
    "ARTISTIC": 40,
    "SOCIAL": 55,
    "ENTERPRISING": 45,
    "CONVENTIONAL": 50
  },
  "keyPoints": "Key points based on your REALISTIC profile",
  "shared": false,
  "downloaded": false,
  "softDeleted": false
}
```

---

## Résumé des scores

- **REALISTIC** : 85% (dominant)
- **INVESTIGATIVE** : 60%
- **ARTISTIC** : 40%
- **SOCIAL** : 55%
- **ENTERPRISING** : 45%
- **CONVENTIONAL** : 50%

**Type dominant :** REALISTIC (85%)

---

## Test COMPLETE (60 questions)

Si vous voulez tester un test COMPLETE, remplacez `type=FAST` par `type=COMPLETE` et utilisez 60 réponses :

**URL :** `http://localhost:8080/api/tests/start?type=COMPLETE`

**Body pour soumission (60 réponses) :**
```json
{
  "1": 4, "2": 5, "3": 4, "4": 3, "5": 4, "6": 3, "7": 4, "8": 5, "9": 4, "10": 3,
  "11": 5, "12": 4, "13": 5, "14": 4, "15": 5, "16": 4, "17": 5, "18": 4, "19": 3, "20": 4,
  "21": 2, "22": 3, "23": 2, "24": 3, "25": 2, "26": 3, "27": 2, "28": 3, "29": 2, "30": 3,
  "31": 5, "32": 4, "33": 5, "34": 4, "35": 5, "36": 4, "37": 5, "38": 4, "39": 5, "40": 4,
  "41": 3, "42": 2, "43": 3, "44": 2, "45": 3, "46": 2, "47": 3, "48": 2, "49": 1, "50": 2,
  "51": 4, "52": 3, "53": 4, "54": 3, "55": 4, "56": 3, "57": 4, "58": 3, "59": 4, "60": 3
}
``` 