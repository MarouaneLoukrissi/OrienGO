# 🧪 Guide de Test - API de Recommandations

## 📋 **Endpoint Testé**
- **URL** : `POST /api/recommendations/process`
- **Description** : Traite les recommandations de jobs et trainings basées sur les scores RIASEC

## 🔧 **Configuration Requise**

### **1. Base de données**
- Assurez-vous que la base de données est démarrée
- Vérifiez que les tables existent (migrations Flyway)

### **2. API Flask**
- L'API Flask doit être démarrée sur `http://127.0.0.1:5000`
- L'endpoint `/recommendations` doit être accessible
- Format de réponse attendu : `{"jobs": [...], "trainings": [...]}`

## 📝 **Données de Test**

### **Student et TestResult**
- Utilisez des IDs valides existants dans votre base de données
- Le TestResult doit avoir des scores RIASEC valides

## 🚀 **Test avec Postman**

### **1. Configuration de la requête**
- **Méthode** : `POST`
- **URL** : `http://localhost:8080/api/recommendations/process`
- **Headers** : 
  - `Content-Type: application/json`
  - `Authorization: Bearer {votre_token}` (si nécessaire)

### **2. Paramètres de requête**
```
studentId: 1
testResultId: 1
```

### **3. Exemple de requête complète**
```
POST http://localhost:8080/api/recommendations/process?studentId=1&testResultId=1
```

## 🔍 **Format des données échangées**

### **Requête envoyée à Flask**
```json


```

### **Réponse attendue de Flask**
```json
{
  "jobs": [
    {
      "title": "Software Engineer",
      "description": "Develop software applications",
      "category": "TECH",
      "jobMarket": "High demand",
      "education": "Bachelor's degree",
      "salaryRange": "50,000 - 80,000 USD/year",
      "tags": ["Programming", "Problem Solving", "Teamwork"]
    }
  ],
  "trainings": [
    {
      "name": "Computer Science Degree",
      "type": "UNIVERSITY",
      "description": "University degree in Computer Science",
      "duration": "3-4 years",
      "specializations": ["Computer Science", "Software Engineering"]
    }
  ]
}
```

### **Réponse finale de l'API Spring Boot**
```json
{
  "code": "SUCCESS",
  "status": 200,
  "message": "Recommendations processed successfully",
  "data": {
    "jobs": [
      {
        "title": "Software Engineer",
        "description": "Develop software applications",
        "category": "TECH",
        "jobMarket": "High demand",
        "education": "Bachelor's degree",
        "salaryRange": "50,000 - 80,000 USD/year",
        "tags": ["Programming", "Problem Solving", "Teamwork"]
      }
    ],
    "trainings": [
      {
        "name": "Computer Science Degree",
        "type": "UNIVERSITY",
        "description": "University degree in Computer Science",
        "duration": "3-4 years",
        "specializations": ["Computer Science", "Software Engineering"]
      }
    ]
  },
  "errors": null
}
```

## 🔍 **Vérifications à Effectuer**

### **1. Base de données**
- Vérifiez que les nouveaux jobs ont été créés dans la table `jobs`
- Vérifiez que les nouveaux trainings ont été créés dans la table `trainings`
- Vérifiez que les `JobRecommendation` ont été créées
- Vérifiez que les `TrainingRecommendation` ont été créées
- Vérifiez que les `StudentJobLink` ont été créés avec `LinkType.NORMAL`
- Vérifiez que les `StudentTrainingLink` ont été créés avec `LinkType.NORMAL`

### **2. Logs**
- Vérifiez les logs de l'application pour confirmer le traitement
- Recherchez les messages de succès et d'erreur

## ⚠️ **Cas d'Erreur à Tester**

### **1. Paramètres manquants**
```
POST /api/recommendations/process
```
**Réponse attendue** : 400 Bad Request avec message "Both studentId and testResultId are required"

### **2. Student inexistant**
```
POST /api/recommendations/process?studentId=99999&testResultId=1
```
**Réponse attendue** : 500 Internal Server Error avec message "Student not found with id: 99999"

### **3. TestResult inexistant**
```
POST /api/recommendations/process?studentId=1&testResultId=99999
```
**Réponse attendue** : 500 Internal Server Error avec message "TestResult not found with id: 99999"

### **4. API Flask indisponible**
- Arrêtez l'API Flask et testez
- **Réponse attendue** : 500 Internal Server Error avec message "Failed to call Flask API"

## 🧹 **Nettoyage après Test**

### **1. Supprimer les données de test**
- Supprimez les `JobRecommendation` créées
- Supprimez les `TrainingRecommendation` créées
- Supprimez les `StudentJobLink` créés
- Supprimez les `StudentTrainingLink` créés
- Supprimez les jobs et trainings créés (si nécessaire)

### **2. Vérifier l'intégrité**
- Assurez-vous qu'aucune référence orpheline n'existe
- Vérifiez que les contraintes de base de données sont respectées

## 📊 **Métriques de Performance**

### **1. Temps de réponse**
- Mesurez le temps de réponse de l'API
- Identifiez les goulots d'étranglement

### **2. Utilisation de la base de données**
- Surveillez le nombre de requêtes SQL exécutées
- Vérifiez l'utilisation des connexions de base de données

## 🔧 **Configuration Avancée**

### **1. URL de l'API Flask**
Modifiez la constante dans `RecommendationService.java` :
```java
private static final String FLASK_API_URL = "http://votre-api:port/recommendations";
```

### **2. Timeout de l'API Flask**
Modifiez la configuration dans `RestTemplateConfig.java` :
```java
factory.setConnectTimeout(15000); // 15 secondes
factory.setReadTimeout(45000);    // 45 secondes
```

## 📝 **Notes Importantes**

1. **Transaction** : L'opération est annotée avec `@Transactional` pour garantir la cohérence
2. **Gestion d'erreurs** : Les erreurs sont loggées et remontées avec des messages explicites
3. **Validation** : Les données d'entrée sont validées avant traitement
4. **Logging** : Toutes les étapes importantes sont loggées pour le debugging
5. **Gestion des doublons** : Les jobs et trainings existants sont réutilisés

## 🚀 **Prochaines Étapes**

1. **Tests unitaires** : Ajoutez des tests JUnit pour le service
2. **Tests d'intégration** : Testez avec une vraie base de données
3. **Monitoring** : Ajoutez des métriques et alertes
4. **Documentation API** : Intégrez avec Swagger/OpenAPI
5. **Gestion des erreurs** : Améliorez la gestion des erreurs spécifiques

## 🔗 **Endpoints disponibles**

- `POST /api/recommendations/process` - Traiter les recommandations
- `GET /api/recommendations/health` - Vérifier l'état du service
