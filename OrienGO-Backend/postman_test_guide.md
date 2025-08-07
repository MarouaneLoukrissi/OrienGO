# Guide de test Postman - Système de tests RIASEC

## Configuration Postman

### Base URL
```
http://localhost:8080
```

## 1. Tests FAST

### 1.1 Créer un test FAST
**Méthode :** POST  
**URL :** `{{baseUrl}}/api/tests/start?type=FAST`  
**Headers :** `Content-Type: application/json`  
**Body (raw JSON) :**
```json
{
  "firstName": "Jean",
  "lastName": "Dupont",
  "email": "jean.dupont@email.com",
  "phoneNumber": "0123456789",
  "age": "25",
  "gender": "MALE",
  "school": "Université de Paris",
  "fieldOfStudy": "Informatique",
  "educationLevel": "BACHELOR",
  "location": "Paris, France",
  "profileVisibility": "PUBLIC",
  "accountPrivacy": "PUBLIC",
  "messagePermission": "ALLOWED"
}
```

### 1.2 Soumettre un test FAST
**Méthode :** POST  
**URL :** `{{baseUrl}}/api/tests/{{testId}}/submit`  
**Headers :** `Content-Type: application/json`  
**Body (raw JSON) :**
```json
{
  "1": 4, "2": 5, "3": 3, "4": 4, "5": 5, "6": 2,
  "7": 4, "8": 3, "9": 5, "10": 4, "11": 3, "12": 4,
  "13": 5, "14": 2, "15": 4, "16": 3, "17": 5, "18": 4
}
```

## 2. Tests COMPLETE

### 2.1 Créer un test COMPLETE
**Méthode :** POST  
**URL :** `{{baseUrl}}/api/tests/start?type=COMPLETE`  
**Headers :** `Content-Type: application/json`  
**Body (raw JSON) :**
```json
{
  "firstName": "Marie",
  "lastName": "Martin",
  "email": "marie.martin@email.com",
  "phoneNumber": "0123456789",
  "age": "22",
  "gender": "FEMALE",
  "school": "Université de Lyon",
  "fieldOfStudy": "Psychologie",
  "educationLevel": "BACHELOR",
  "location": "Lyon, France",
  "profileVisibility": "PUBLIC",
  "accountPrivacy": "PUBLIC",
  "messagePermission": "ALLOWED"
}
```

### 2.2 Soumettre un test COMPLETE
**Méthode :** POST  
**URL :** `{{baseUrl}}/api/tests/{{testId}}/submit`  
**Headers :** `Content-Type: application/json`  
**Body (raw JSON) :**
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

## 3. Consultation des tests

### 3.1 Lister tous les tests
**Méthode :** GET  
**URL :** `{{baseUrl}}/api/tests`

### 3.2 Récupérer un test spécifique
**Méthode :** GET  
**URL :** `{{baseUrl}}/api/tests/{{testId}}`

## 4. Consultation des résultats

### 4.1 Lister tous les résultats
**Méthode :** GET  
**URL :** `{{baseUrl}}/api/test-results`

### 4.2 Récupérer un résultat par ID
**Méthode :** GET  
**URL :** `{{baseUrl}}/api/test-results/{{resultId}}`

### 4.3 Récupérer le résultat d'un test spécifique
**Méthode :** GET  
**URL :** `{{baseUrl}}/api/test-results/test/{{testId}}`

### 4.4 Récupérer tous les résultats d'un étudiant
**Méthode :** GET  
**URL :** `{{baseUrl}}/api/test-results/student/{{studentId}}`

## 5. Tests d'erreurs

### 5.1 Test incomplet (réponses manquantes)
**Méthode :** POST  
**URL :** `{{baseUrl}}/api/tests/{{testId}}/submit`  
**Headers :** `Content-Type: application/json`  
**Body (raw JSON) :**
```json
{
  "1": 4, "2": 5, "3": 3
}
```

### 5.2 Test avec réponses invalides
**Méthode :** POST  
**URL :** `{{baseUrl}}/api/tests/{{testId}}/submit`  
**Headers :** `Content-Type: application/json`  
**Body (raw JSON) :**
```json
{
  "1": 6, "2": 0, "3": 3, "4": 4, "5": 5, "6": 2,
  "7": 4, "8": 3, "9": 5, "10": 4, "11": 3, "12": 4,
  "13": 5, "14": 2, "15": 4, "16": 3, "17": 5, "18": 4
}
```

## 6. Variables Postman

### 6.1 Variables d'environnement
```
baseUrl: http://localhost:8080
testId: (à remplir après création d'un test)
studentId: (à remplir après création d'un test)
resultId: (à remplir après soumission d'un test)
```

### 6.2 Scripts de test automatiques

#### Script pour extraire testId après création
```javascript
if (pm.response.code === 201) {
    const response = pm.response.json();
    pm.environment.set("testId", response.id);
    pm.environment.set("studentId", response.studentId);
    console.log("Test créé avec ID:", response.id);
}
```

#### Script pour extraire resultId après soumission
```javascript
if (pm.response.code === 200) {
    const response = pm.response.json();
    pm.environment.set("resultId", response.id);
    console.log("Résultat créé avec ID:", response.id);
}
```

## 7. Workflow de test complet

### 7.1 Workflow FAST
1. **Créer test FAST** → Extraire testId
2. **Soumettre test FAST** → Extraire resultId
3. **Vérifier résultat** → GET /api/test-results/test/{{testId}}
4. **Vérifier statut COMPLETED** → GET /api/tests/{{testId}}

### 7.2 Workflow COMPLETE
1. **Créer test COMPLETE** → Extraire testId
2. **Soumettre test COMPLETE** → Extraire resultId
3. **Vérifier résultat** → GET /api/test-results/test/{{testId}}
4. **Vérifier statut COMPLETED** → GET /api/tests/{{testId}}

## 8. Vérifications attendues

### 8.1 Test créé
- ✅ Status: 201 Created
- ✅ Type: FAST ou COMPLETE
- ✅ Status: PENDING
- ✅ QuestionsCount: 18 (FAST) ou 60 (COMPLETE)
- ✅ StudentId: présent

### 8.2 Test soumis
- ✅ Status: 200 OK
- ✅ DominantType: présent
- ✅ Scores: pourcentages (0-100)
- ✅ KeyPoints: présent

### 8.3 Test COMPLETED
- ✅ Status: COMPLETED
- ✅ CompletedAt: présent
- ✅ Result: créé automatiquement

### 8.4 Erreurs
- ✅ Test incomplet: 400 Bad Request
- ✅ Réponses invalides: 400 Bad Request
- ✅ Test inexistant: 404 Not Found 