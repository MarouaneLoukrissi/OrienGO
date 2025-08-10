-- Migration V1_4: Insertion des données de test pour la table jobs
-- Remplit uniquement la table jobs avec des emplois de test

-- Insertion des emplois avec scores RIASEC (IDs à partir de 100 pour éviter les conflits)
INSERT INTO jobs (id, title, description, category, education, salary_range, job_market, riasec_realistic, riasec_investigative, riasec_artistic, riasec_social, riasec_enterprising, riasec_conventional, active, soft_deleted) VALUES
(100, 'Développeur Full-Stack', 'Développement d''applications web modernes avec React et Spring Boot', 'TECH', 'Master en Informatique', '45,000 - 65,000 EUR/an', 'Forte demande', 3.5, 4.2, 2.8, 3.1, 3.9, 3.7, true, false),
(101, 'Psychologue Clinicien', 'Accompagnement thérapeutique et évaluation psychologique', 'HEALTH', 'Master en Psychologie', '35,000 - 50,000 EUR/an', 'Stable', 2.1, 4.5, 3.2, 4.8, 2.9, 2.3, true, false),
(102, 'Chef de Projet Marketing', 'Gestion de projets marketing et stratégies de communication', 'BUSINESS', 'Master en Marketing', '40,000 - 60,000 EUR/an', 'Croissance', 2.8, 3.1, 3.9, 4.2, 4.6, 3.4, true, false),
(103, 'Designer UX/UI', 'Conception d''interfaces utilisateur et expérience utilisateur', 'ARTS', 'Bachelor en Design', '35,000 - 55,000 EUR/an', 'Innovation', 2.5, 3.3, 4.7, 3.8, 3.2, 2.1, true, false),
(104, 'Ingénieur Data Scientist', 'Analyse de données et développement de modèles prédictifs', 'TECH', 'Master en Data Science', '50,000 - 70,000 EUR/an', 'Très forte demande', 3.2, 4.8, 2.9, 3.4, 3.7, 4.1, true, false),
(105, 'Enseignant en Mathématiques', 'Enseignement des mathématiques au niveau secondaire', 'EDUCATION', 'Master en Mathématiques', '30,000 - 45,000 EUR/an', 'Stable', 2.8, 4.1, 2.5, 4.3, 3.0, 3.2, true, false),
(106, 'Architecte Logiciel', 'Conception et développement d''architectures logicielles', 'TECH', 'Master en Informatique', '55,000 - 75,000 EUR/an', 'Forte demande', 3.1, 4.6, 3.0, 3.3, 4.0, 3.8, true, false),
(107, 'Infirmier(e) Spécialisé(e)', 'Soins infirmiers spécialisés en unité de soins intensifs', 'HEALTH', 'Diplôme d''État Infirmier', '35,000 - 50,000 EUR/an', 'Très forte demande', 3.8, 3.2, 2.4, 4.7, 2.8, 3.1, true, false),
(108, 'Directeur Commercial', 'Gestion d''équipe commerciale et développement des ventes', 'BUSINESS', 'Master en Commerce', '45,000 - 70,000 EUR/an', 'Croissance', 2.9, 3.4, 3.1, 4.1, 4.8, 3.5, true, false),
(109, 'Graphiste Créatif', 'Création d''identités visuelles et supports de communication', 'ARTS', 'Bachelor en Arts Graphiques', '30,000 - 50,000 EUR/an', 'Stable', 2.3, 3.0, 4.9, 3.5, 3.1, 2.4, true, false); 