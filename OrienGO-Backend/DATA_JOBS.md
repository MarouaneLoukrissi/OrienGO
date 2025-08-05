# Données de Test - Table Jobs

## Vue d'ensemble
Ce fichier documente les données de test insérées dans la table `jobs` de votre application.

## Emplois Disponibles (10 emplois)

### 1. Développeur Full-Stack (ID: 100, TECH)
- **Salaire**: 45,000 - 65,000 EUR/an
- **Éducation**: Master en Informatique
- **Marché**: Forte demande
- **Scores RIASEC**: INVESTIGATIVE (4.2), ENTERPRISING (3.9)

### 2. Psychologue Clinicien (ID: 101, HEALTH)
- **Salaire**: 35,000 - 50,000 EUR/an
- **Éducation**: Master en Psychologie
- **Marché**: Stable
- **Scores RIASEC**: SOCIAL (4.8), INVESTIGATIVE (4.5)

### 3. Chef de Projet Marketing (ID: 102, BUSINESS)
- **Salaire**: 40,000 - 60,000 EUR/an
- **Éducation**: Master en Marketing
- **Marché**: Croissance
- **Scores RIASEC**: ENTERPRISING (4.6), SOCIAL (4.2)

### 4. Designer UX/UI (ID: 103, ARTS)
- **Salaire**: 35,000 - 55,000 EUR/an
- **Éducation**: Bachelor en Design
- **Marché**: Innovation
- **Scores RIASEC**: ARTISTIC (4.7), SOCIAL (3.8)

### 5. Ingénieur Data Scientist (ID: 104, TECH)
- **Salaire**: 50,000 - 70,000 EUR/an
- **Éducation**: Master en Data Science
- **Marché**: Très forte demande
- **Scores RIASEC**: INVESTIGATIVE (4.8), CONVENTIONAL (4.1)

### 6. Enseignant en Mathématiques (ID: 105, EDUCATION)
- **Salaire**: 30,000 - 45,000 EUR/an
- **Éducation**: Master en Mathématiques
- **Marché**: Stable
- **Scores RIASEC**: INVESTIGATIVE (4.1), SOCIAL (4.3)

### 7. Architecte Logiciel (ID: 106, TECH)
- **Salaire**: 55,000 - 75,000 EUR/an
- **Éducation**: Master en Informatique
- **Marché**: Forte demande
- **Scores RIASEC**: INVESTIGATIVE (4.6), ENTERPRISING (4.0)

### 8. Infirmier(e) Spécialisé(e) (ID: 107, HEALTH)
- **Salaire**: 35,000 - 50,000 EUR/an
- **Éducation**: Diplôme d'État Infirmier
- **Marché**: Très forte demande
- **Scores RIASEC**: SOCIAL (4.7), REALISTIC (3.8)

### 9. Directeur Commercial (ID: 108, BUSINESS)
- **Salaire**: 45,000 - 70,000 EUR/an
- **Éducation**: Master en Commerce
- **Marché**: Croissance
- **Scores RIASEC**: ENTERPRISING (4.8), SOCIAL (4.1)

### 10. Graphiste Créatif (ID: 109, ARTS)
- **Salaire**: 30,000 - 50,000 EUR/an
- **Éducation**: Bachelor en Arts Graphiques
- **Marché**: Stable
- **Scores RIASEC**: ARTISTIC (4.9), SOCIAL (3.5)

## Catégories d'Emplois
- **TECH**: 3 emplois (Développeur, Data Scientist, Architecte)
- **HEALTH**: 2 emplois (Psychologue, Infirmier)
- **BUSINESS**: 2 emplois (Marketing, Commercial)
- **ARTS**: 2 emplois (Designer, Graphiste)
- **EDUCATION**: 1 emploi (Enseignant)

## Comment Utiliser
1. **Démarrer l'application** : Les données seront automatiquement insérées via Flyway
2. **Tester les fonctionnalités** :
   - Consultation des emplois
   - Filtrage par catégorie
   - Recherche par mots-clés
   - Affichage des scores RIASEC

## Structure des Données
Chaque emploi contient :
- Informations de base (titre, description, catégorie)
- Exigences d'éducation
- Fourchette de salaire
- État du marché du travail
- Scores RIASEC (6 valeurs de 1 à 5)
- Statut actif/inactif 