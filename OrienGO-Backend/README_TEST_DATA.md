# Données de Test OrienGO

Ce document explique les données de test insérées dans la base de données OrienGO pour faciliter le développement et les tests.

## 📋 Vue d'ensemble

Les données de test sont réparties en deux migrations :
- `V1_4__insert_test_data.sql` : Données de base (utilisateurs, jobs, formations, questions)
- `V1_5__insert_advanced_test_data.sql` : Données avancées (tests, résultats, recommandations, conversations)

## 👥 Utilisateurs de Test

### Super Admin
- **Email** : `superadmin@oriengo.com`
- **Nom** : Super Admin
- **Rôle** : ROLE_SUPER_ADMIN
- **Département** : TECH (Manager)

### Admins
- **Email** : `tech.admin@oriengo.com` - Tech Manager (STANDARD_ADMIN)
- **Email** : `hr.admin@oriengo.com` - HR Manager (STANDARD_ADMIN)

### Coaches
- **Email** : `marie.dupont@coach.com` - Marie Dupont (Profil public)
- **Email** : `jean.martin@coach.com` - Jean Martin (Profil public)
- **Email** : `sophie.bernard@coach.com` - Sophie Bernard (Profil privé)

### Étudiants
- **Email** : `alex.dubois@student.com` - Alex Dubois (18 ans, Lycée, Sciences)
- **Email** : `emma.leroy@student.com` - Emma Leroy (20 ans, Université, Informatique)
- **Email** : `lucas.moreau@student.com` - Lucas Moreau (19 ans, École de Commerce, Management)
- **Email** : `chloe.simon@student.com` - Chloé Simon (21 ans, Institut Polytechnique, Ingénierie)

## 🧪 Tests RIASEC

### Profils de Test

#### Alex Dubois - Type INVESTIGATIVE (85%)
- **Scores** : R(65%) I(85%) A(45%) S(55%) E(40%) C(70%)
- **Caractéristiques** : Analytique, curieux, aime résoudre des problèmes complexes
- **Jobs recommandés** : Data Scientist, Développeur Full Stack, Analyste Financier
- **Formations recommandées** : Master Data Science, Formation Développeur Web

#### Emma Leroy - Type SOCIAL (90%)
- **Scores** : R(35%) I(60%) A(50%) S(90%) E(65%) C(45%)
- **Caractéristiques** : Empathique, communicatif, aime aider les autres
- **Jobs recommandés** : Psychologue Clinicien, Conseiller d'Orientation, Chef de Projet
- **Formations recommandées** : Formation Psychologie Clinique, Formation Conseiller d'Orientation

## 💼 Jobs de Test

### Catégorie TECH
1. **Développeur Full Stack** - 45,000-65,000 EUR/an
2. **Data Scientist** - 50,000-75,000 EUR/an
3. **DevOps Engineer** - 55,000-80,000 EUR/an

### Catégorie HEALTH
4. **Psychologue Clinicien** - 35,000-55,000 EUR/an
5. **Infirmier(e) Spécialisé(e)** - 30,000-45,000 EUR/an

### Catégorie BUSINESS
6. **Chef de Projet** - 45,000-70,000 EUR/an
7. **Analyste Financier** - 40,000-65,000 EUR/an

### Catégorie EDUCATION
8. **Professeur de Mathématiques** - 25,000-40,000 EUR/an
9. **Conseiller d'Orientation** - 30,000-45,000 EUR/an

### Catégorie ARTS
10. **Designer UX/UI** - 35,000-60,000 EUR/an
11. **Graphiste** - 25,000-45,000 EUR/an

## 🎓 Formations de Test

### Formations Mises en Avant
1. **Formation Développeur Web Full Stack** (BOOTCAMP, 6 mois)
2. **Master en Data Science** (UNIVERSITY, 2 ans)
3. **Certification AWS Solutions Architect** (CERTIFICATION, 3 mois)
4. **MBA en Management de Projet** (UNIVERSITY, 1 an)
5. **Formation Designer UX/UI** (BOOTCAMP, 4 mois)

### Autres Formations
6. **Formation en Psychologie Clinique** (UNIVERSITY, 2 ans)
7. **Formation Infirmier(e) Spécialisé(e)** (VOCATIONAL, 1 an)
8. **Formation en Analyse Financière** (VOCATIONAL, 6 mois)
9. **CAPES Mathématiques** (CERTIFICATION, 1 an)
10. **Formation Conseiller d'Orientation** (VOCATIONAL, 1 an)

## 🔗 Connexions et Interactions

### Connexions Coach-Étudiant
- **Marie Dupont** ↔ **Alex Dubois** (Acceptée)
- **Jean Martin** ↔ **Emma Leroy** (Acceptée)
- **Sophie Bernard** ↔ **Lucas Moreau** (Acceptée)
- **Marie Dupont** ↔ **Chloé Simon** (En attente)
- **Jean Martin** ↔ **Alex Dubois** (En attente)
- **Sophie Bernard** ↔ **Emma Leroy** (Refusée)

### Conversations Actives
- Conversation entre Marie Dupont et Alex Dubois (5 messages)
- Conversation entre Jean Martin et Emma Leroy (5 messages)
- Conversation entre Sophie Bernard et Lucas Moreau (5 messages)

## 📊 Tests et Résultats

### Tests Terminés
- **Alex Dubois** : Test FAST + Test COMPLETE (Type INVESTIGATIVE)
- **Emma Leroy** : Test FAST + Test COMPLETE (Type SOCIAL)

### Tests en Cours
- **Lucas Moreau** : Test FAST en cours

### Tests en Attente
- **Chloé Simon** : Test FAST non commencé

## 🔐 Authentification

### Tokens Actifs
- Tokens d'accès pour les utilisateurs connectés
- Tokens de rafraîchissement valides
- Tokens expirés/révoqués pour les tests

### Mots de passe
⚠️ **Note** : Les mots de passe dans les données de test sont des placeholders. En production, utilisez des mots de passe sécurisés.

## 🚀 Comment Utiliser

### 1. Exécution des Migrations
```bash
# Les migrations s'exécutent automatiquement au démarrage de l'application
# Assurez-vous que Flyway est configuré dans application.yml
```

### 2. Connexion avec les Comptes de Test
```bash
# Étudiant
POST /api/auth/login
{
  "email": "alex.dubois@student.com",
  "password": "password123"
}

# Coach
POST /api/auth/login
{
  "email": "marie.dupont@coach.com",
  "password": "password123"
}

# Admin
POST /api/auth/login
{
  "email": "superadmin@oriengo.com",
  "password": "password123"
}
```

### 3. Endpoints de Test Utiles
```bash
# Lister tous les jobs
GET /api/jobs

# Lister les jobs par catégorie
GET /api/jobs/category/TECH

# Obtenir les recommandations d'un étudiant
GET /api/students/7/recommendations

# Lister les tests d'un étudiant
GET /api/students/7/tests

# Obtenir les résultats d'un test
GET /api/tests/1/results
```

## 📈 Scénarios de Test

### Scénario 1 : Étudiant Consulte ses Recommandations
1. Connectez-vous avec `alex.dubois@student.com`
2. Accédez à `/student/recommendations`
3. Vérifiez les jobs et formations recommandés (Data Scientist, Développeur Full Stack)

### Scénario 2 : Coach Consulte ses Étudiants
1. Connectez-vous avec `marie.dupont@coach.com`
2. Accédez à `/coach/students`
3. Vérifiez la connexion avec Alex Dubois

### Scénario 3 : Admin Gère les Jobs
1. Connectez-vous avec `superadmin@oriengo.com`
2. Accédez à `/admin/jobs`
3. Testez la création/modification de jobs

### Scénario 4 : Test RIASEC Complet
1. Connectez-vous avec `chloe.simon@student.com`
2. Commencez un test FAST
3. Répondez aux questions
4. Consultez les résultats

## 🔧 Personnalisation

Pour ajouter vos propres données de test :

1. **Modifiez les migrations** : Éditez `V1_4__insert_test_data.sql` et `V1_5__insert_advanced_test_data.sql`
2. **Créez une nouvelle migration** : `V1_6__my_custom_data.sql`
3. **Redémarrez l'application** : Les nouvelles données seront insérées automatiquement

## ⚠️ Notes Importantes

- **Environnement de développement uniquement** : Ces données ne doivent pas être utilisées en production
- **Mots de passe** : Remplacez les mots de passe par des valeurs sécurisées
- **Données sensibles** : Les emails et informations personnelles sont fictifs
- **Séquences** : Les IDs sont définis manuellement, assurez-vous qu'ils ne créent pas de conflits

## 🐛 Dépannage

### Problème : Erreur de contrainte d'intégrité
```sql
-- Vérifiez que les séquences sont correctement initialisées
SELECT setval('users_seq', (SELECT MAX(id) FROM users));
SELECT setval('jobs_seq', (SELECT MAX(id) FROM jobs));
```

### Problème : Données manquantes
```sql
-- Vérifiez que les migrations ont été exécutées
SELECT * FROM flyway_schema_history ORDER BY installed_rank DESC;
```

### Problème : Connexion échoue
- Vérifiez que les mots de passe correspondent à votre configuration d'authentification
- Assurez-vous que les utilisateurs sont activés (`enabled = true`) 