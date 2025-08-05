# Données de Test Essentielles - OrienGO

## Vue d'ensemble
Ce fichier documente les données de test insérées dans les tables principales utilisées par votre application.

## Utilisateurs de Test

### Admin Principal
- **Email**: admin@oriengo.com
- **Rôle**: ADMIN (MANAGER)
- **Département**: TECH

### Étudiants
1. **Alex Dubois** (alex.dubois@student.com)
   - École: Lycée Victor Hugo
   - Domaine: Sciences
   - Niveau: HIGH_SCHOOL
   - Profil RIASEC: INVESTIGATIVE (dominant)

2. **Emma Leroy** (emma.leroy@student.com)
   - École: Université Paris-Sorbonne
   - Domaine: Psychologie
   - Niveau: BACHELOR
   - Profil RIASEC: SOCIAL (dominant)

3. **Lucas Moreau** (lucas.moreau@student.com)
   - École: École Centrale
   - Domaine: Informatique
   - Niveau: BACHELOR
   - Test: EN COURS

## Emplois Disponibles

1. **Développeur Full-Stack** (TECH)
   - Salaire: 45,000 - 65,000 EUR/an
   - RIASEC: INVESTIGATIVE (4.2), ENTERPRISING (3.9)

2. **Psychologue Clinicien** (HEALTH)
   - Salaire: 35,000 - 50,000 EUR/an
   - RIASEC: SOCIAL (4.8), INVESTIGATIVE (4.5)

3. **Chef de Projet Marketing** (BUSINESS)
   - Salaire: 40,000 - 60,000 EUR/an
   - RIASEC: ENTERPRISING (4.6), SOCIAL (4.2)

4. **Designer UX/UI** (ARTS)
   - Salaire: 35,000 - 55,000 EUR/an
   - RIASEC: ARTISTIC (4.7), SOCIAL (3.8)

5. **Ingénieur Data Scientist** (TECH)
   - Salaire: 50,000 - 70,000 EUR/an
   - RIASEC: INVESTIGATIVE (4.8), CONVENTIONAL (4.1)

## Tests et Résultats

### Tests Complétés
- **Alex**: Test FAST terminé (25 min) → Profil INVESTIGATIVE
- **Emma**: Test FAST terminé (20 min) → Profil SOCIAL

### Test en Cours
- **Lucas**: Test FAST en cours (15 min sur 20)

## Recommandations Générées

### Pour Alex (INVESTIGATIVE)
- Développeur Full-Stack (85.5% match)
- Ingénieur Data Scientist (92.3% match)

### Pour Emma (SOCIAL)
- Psychologue Clinicien (88.7% match)

### Pour Lucas
- Développeur Full-Stack (78.9% match)

## Emplois Personnalisés

1. **Développeur Frontend React** (TechCorp) - 87.5% match
2. **Analyste de Données Junior** (DataLab) - 91.2% match
3. **Conseiller en Orientation** (EduGuide) - 89.3% match

## Favoris et Sauvegardes

### Alex
- **Sauvegardé**: Développeur Full-Stack, Développeur Frontend React
- **Favori**: Ingénieur Data Scientist

### Emma
- **Sauvegardé**: Psychologue Clinicien
- **Favori**: Conseiller en Orientation

### Lucas
- **Favori**: Développeur Full-Stack

## Questions RIASEC

12 questions de base couvrant les 6 types RIASEC :
- REALISTIC: 2 questions
- INVESTIGATIVE: 2 questions
- ARTISTIC: 2 questions
- SOCIAL: 2 questions
- ENTERPRISING: 2 questions
- CONVENTIONAL: 2 questions

## Comment Utiliser

1. **Démarrer l'application** : Les données seront automatiquement insérées via Flyway
2. **Se connecter** avec les emails des utilisateurs de test
3. **Tester les fonctionnalités** :
   - Consultation des emplois
   - Passage de tests RIASEC
   - Visualisation des recommandations
   - Gestion des favoris

## Tables Principales Remplies

- ✅ users, students, admins
- ✅ roles, privileges, roles_privileges
- ✅ jobs (avec scores RIASEC)
- ✅ questions, answer_options
- ✅ tests, test_results, test_result_scores
- ✅ job_recommendations
- ✅ personalized_jobs
- ✅ student_job_links
- ✅ student_personalized_job_links
- ✅ notification_settings 