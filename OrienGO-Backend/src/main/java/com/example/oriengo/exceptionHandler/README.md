# Système de Gestion d'Exceptions

Ce dossier contient le système centralisé de gestion d'exceptions pour l'application OrienGO.

## Structure

```
exceptionHandler/
├── GlobalExceptionHandler.java     # Handler principal pour toutes les exceptions
├── dto/
│   └── ErrorResponse.java          # DTO pour les réponses d'erreur standardisées
├── exceptions/
│   ├── BusinessException.java      # Exception pour les erreurs métier
│   └── ValidationException.java    # Exception pour les erreurs de validation
├── util/
│   └── ExceptionUtils.java         # Utilitaires pour faciliter la gestion d'exceptions
└── README.md                       # Cette documentation
```

## Utilisation

### 1. Exceptions Gérées

Le `GlobalExceptionHandler` gère automatiquement les types d'exceptions suivants :

- **ResourceNotFoundException** → HTTP 404
- **BusinessException** → HTTP 400
- **ValidationException** → HTTP 400
- **MethodArgumentNotValidException** → HTTP 400 (Bean Validation)
- **IllegalArgumentException** → HTTP 400
- **DataIntegrityViolationException** → HTTP 409
- **DuplicateKeyException** → HTTP 409
- **HttpMediaTypeNotSupportedException** → HTTP 415
- **HttpRequestMethodNotSupportedException** → HTTP 405
- **Exception** → HTTP 500 (exceptions génériques)

### 2. Format de Réponse d'Erreur

Toutes les erreurs retournent un format JSON standardisé :

```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Job title is required",
  "path": "/api/jobs",
  "validationErrors": {
    "title": "Job title is required",
    "description": "Job description is required"
  }
}
```

### 3. Utilisation dans les Services

#### Avec ExceptionUtils (Recommandé)

```java
@Service
public class JobService {
    
    public Job create(JobRequestDto requestDto) {
        // Validations métier
        ExceptionUtils.throwIfNull(requestDto, "Job request cannot be null");
        ExceptionUtils.throwIfFalse(StringUtils.hasText(requestDto.getTitle()), 
            "Job title is required");
        
        // Logique métier...
    }
}
```

#### Avec les Exceptions Personnalisées

```java
@Service
public class JobService {
    
    public Job create(JobRequestDto requestDto) {
        if (requestDto == null) {
            throw new BusinessException("Job request cannot be null");
        }
        
        if (!StringUtils.hasText(requestDto.getTitle())) {
            throw new ValidationException("Job title is required");
        }
        
        // Logique métier...
    }
}
```

### 4. Méthodes Utilitaires Disponibles

#### ExceptionUtils

- `throwIfTrue(boolean condition, String message)` - Lance BusinessException si condition vraie
- `throwIfFalse(boolean condition, String message)` - Lance BusinessException si condition fausse
- `throwIfNull(Object object, String message)` - Lance BusinessException si objet null
- `throwIfNotNull(Object object, String message)` - Lance BusinessException si objet non null
- `throwValidationIfTrue(boolean condition, String message)` - Lance ValidationException si condition vraie
- `throwValidationIfFalse(boolean condition, String message)` - Lance ValidationException si condition fausse
- `throwValidationIfNull(Object object, String message)` - Lance ValidationException si objet null
- `throwValidationIfNotNull(Object object, String message)` - Lance ValidationException si objet non null

### 5. Exemples d'Utilisation

#### Validation de Données

```java
public Job create(JobRequestDto requestDto) {
    // Validations de base
    ExceptionUtils.throwIfNull(requestDto, "Job request cannot be null");
    ExceptionUtils.throwIfFalse(StringUtils.hasText(requestDto.getTitle()), 
        "Job title is required");
    
    // Validation métier
    ExceptionUtils.throwIfTrue(requestDto.getSalary() < 0, 
        "Salary cannot be negative");
    
    // Logique métier...
}
```

#### Validation RIASEC

```java
private void validateRiasScore(Double score, String type) {
    if (score != null) {
        ExceptionUtils.throwIfTrue(score < 0 || score > 100, 
            type + " RIASEC score must be between 0 and 100");
    }
}
```

#### Gestion des Erreurs Métier

```java
public Job update(Long id, JobRequestDto requestDto) {
    Job existingJob = repository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("Job", "id", id));
    
    // Validation métier
    ExceptionUtils.throwIfTrue(existingJob.getActive(), 
        "Cannot update an active job");
    
    // Logique de mise à jour...
}
```

### 6. Tests

Pour tester les exceptions, vous pouvez utiliser Postman :

#### Test d'Erreur de Validation

```bash
POST http://localhost:8888/api/jobs
Content-Type: application/json

{
  "title": "",  # Titre vide
  "description": "Test job"
}
```

**Réponse attendue :**
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Job title is required",
  "path": "/api/jobs"
}
```

#### Test d'Erreur Métier

```bash
POST http://localhost:8888/api/jobs
Content-Type: application/json

{
  "title": "Test Job",
  "description": "Test description",
  "riasecRealistic": 150  # Score invalide
}
```

**Réponse attendue :**
```json
{
  "timestamp": "2024-01-15T10:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Realistic RIASEC score must be between 0 and 100",
  "path": "/api/jobs"
}
```

## Avantages

1. **Centralisation** : Toutes les exceptions sont gérées au même endroit
2. **Standardisation** : Format de réponse d'erreur uniforme
3. **Maintenabilité** : Code plus propre et facile à maintenir
4. **Réutilisabilité** : Utilitaires réutilisables dans tout le projet
5. **Sécurité** : Pas d'exposition d'informations sensibles dans les erreurs
6. **Logging** : Possibilité d'ajouter facilement du logging centralisé 