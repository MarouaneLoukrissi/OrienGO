# Test de la fonctionnalité "Test Completed"

## Étape 1 : Créer et terminer un test

### 1.1 Créer un test FAST
```bash
POST http://localhost:8080/api/tests/start?type=FAST
Content-Type: application/json

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

### 1.2 Soumettre le test (le terminer)
```bash
POST http://localhost:8080/api/tests/{testId}/submit
Content-Type: application/json

{
  "1": 4, "2": 5, "3": 3, "4": 4, "5": 5, "6": 2,
  "7": 4, "8": 3, "9": 5, "10": 4, "11": 3, "12": 4,
  "13": 5, "14": 2, "15": 4, "16": 3, "17": 5, "18": 4
}
```

## Étape 2 : Tester la fonctionnalité "Test Completed"

### 2.1 Voir le résultat d'un test spécifique
```bash
GET http://localhost:8080/api/test-results/test/{testId}
```

### 2.2 Voir tous les résultats d'un étudiant
```bash
GET http://localhost:8080/api/test-results/student/{studentId}
```

### 2.3 Voir un résultat par ID
```bash
GET http://localhost:8080/api/test-results/{resultId}
```

### 2.4 Voir tous les résultats
```bash
GET http://localhost:8080/api/test-results
```

## Étape 3 : Vérifications

### 3.1 Vérifier que le test est COMPLETED
```bash
GET http://localhost:8080/api/tests/{testId}
```

### 3.2 Vérifier les scores calculés
- Scores en pourcentage (0-100)
- Type dominant déterminé
- Key points générés 