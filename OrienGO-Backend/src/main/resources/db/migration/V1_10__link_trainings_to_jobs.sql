-- Liaison des formations aux emplois existants
-- V1_10__link_trainings_to_jobs.sql

-- Liens pour le Développeur Full-Stack (ID: 100)
INSERT INTO job_training_links (job_id, training_id) VALUES
(100, 1),  -- Licence Informatique
(100, 6),  -- Formation Développeur Web Full-Stack
(100, 11), -- Bootcamp Développement React
(100, 21), -- Cours Machine Learning - Coursera
(100, 26), -- Stage Développeur Frontend
(100, 31), -- Apprentissage Développeur Full-Stack
(100, 36), -- Atelier Introduction à React
(100, 46); -- Auto-formation Développement Web

-- Liens pour le Psychologue Clinicien (ID: 101)
INSERT INTO job_training_links (job_id, training_id) VALUES
(101, 2),  -- Master en Psychologie Clinique
(101, 7),  -- Formation Conseiller en Orientation
(101, 23), -- Cours Psychologie Positive - edX
(101, 42), -- Séminaire Psychologie Positive
(101, 47); -- Auto-formation Data Science (pour la recherche)

-- Liens pour le Chef de Projet Marketing (ID: 102)
INSERT INTO job_training_links (job_id, training_id) VALUES
(102, 4),  -- Licence en Marketing Digital
(102, 8),  -- Formation Chef de Projet IT
(102, 15), -- Bootcamp Marketing Digital
(102, 18), -- Certification Scrum Master
(102, 24), -- Formation SEO Avancé - LinkedIn Learning
(102, 29), -- Stage Marketing Digital
(102, 35), -- Apprentissage Chef de Projet
(102, 39), -- Atelier Gestion de Projet Agile
(102, 44), -- Séminaire Marketing Automation
(102, 49); -- Auto-formation Marketing Digital

-- Liens pour le Designer UX/UI (ID: 103)
INSERT INTO job_training_links (job_id, training_id) VALUES
(103, 5),  -- Master en Design UX/UI
(103, 14), -- Bootcamp UX Design
(103, 25), -- Cours Design Thinking - MIT OpenCourseWare
(103, 28), -- Stage UX Researcher
(103, 33), -- Apprentissage UX Designer
(103, 37), -- Atelier Design Sprint
(103, 43), -- Séminaire Transformation Digitale
(103, 48); -- Auto-formation Design UX

-- Liens pour l'Ingénieur Data Scientist (ID: 104)
INSERT INTO job_training_links (job_id, training_id) VALUES
(104, 3),  -- École d'Ingénieur en Data Science
(104, 9),  -- Formation Analyste de Données
(104, 12), -- Bootcamp Python Data Science
(104, 16), -- Certification AWS Solutions Architect
(104, 21), -- Cours Machine Learning - Coursera
(104, 27), -- Stage Data Analyst
(104, 32), -- Apprentissage Data Scientist
(104, 40), -- Atelier Analyse de Données
(104, 41), -- Séminaire IA et Futur du Travail
(104, 47); -- Auto-formation Data Science

-- Note: Les emplois personnalisés ne sont pas encore créés dans la base de données
-- Une migration séparée sera nécessaire pour créer les emplois personnalisés
-- et les lier aux formations via une table de liaison dédiée

-- Ajout de quelques formations supplémentaires pour enrichir les recommandations
-- Formation en Cybersécurité pour les profils TECH
INSERT INTO job_training_links (job_id, training_id) VALUES
(100, 45), -- Séminaire Cybersécurité (pour Développeur Full-Stack)
(104, 45); -- Séminaire Cybersécurité (pour Data Scientist)

-- Formation en Communication pour les profils SOCIAUX
INSERT INTO job_training_links (job_id, training_id) VALUES
(101, 10), -- Formation Community Manager (pour Psychologue)
(102, 10); -- Formation Community Manager (pour Marketing)

-- Note: Les formations en Innovation et Leadership sont déjà incluses dans les sections principales ci-dessus
