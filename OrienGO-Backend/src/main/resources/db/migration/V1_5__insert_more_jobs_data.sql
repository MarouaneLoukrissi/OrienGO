-- Migration V1_5: Ajout de données supplémentaires pour la table jobs avec scores RIASEC en pourcentage
-- Ajoute 45 emplois au total avec des profils RIASEC variés en pourcentage (0-100)

-- Suppression des emplois existants pour éviter les conflits
DELETE FROM jobs WHERE id >= 100;

-- Insertion de tous les emplois avec scores RIASEC en pourcentage
INSERT INTO jobs (id, title, description, category, education, salary_range, job_market, riasec_realistic, riasec_investigative, riasec_artistic, riasec_social, riasec_enterprising, riasec_conventional, active, soft_deleted) VALUES

-- Première série (IDs 100-109) - 10 emplois
(100, 'Développeur Full-Stack', 'Développement d''applications web modernes avec React et Spring Boot', 'TECH', 'Master en Informatique', '45,000 - 65,000 EUR/an', 'Forte demande', 70.0, 84.0, 56.0, 62.0, 78.0, 74.0, true, false),
(101, 'Psychologue Clinicien', 'Accompagnement thérapeutique et évaluation psychologique', 'HEALTH', 'Master en Psychologie', '35,000 - 50,000 EUR/an', 'Stable', 42.0, 90.0, 64.0, 96.0, 58.0, 46.0, true, false),
(102, 'Chef de Projet Marketing', 'Gestion de projets marketing et stratégies de communication', 'BUSINESS', 'Master en Marketing', '40,000 - 60,000 EUR/an', 'Croissance', 56.0, 62.0, 78.0, 84.0, 92.0, 68.0, true, false),
(103, 'Designer UX/UI', 'Conception d''interfaces utilisateur et expérience utilisateur', 'ARTS', 'Bachelor en Design', '35,000 - 55,000 EUR/an', 'Innovation', 50.0, 66.0, 94.0, 76.0, 64.0, 42.0, true, false),
(104, 'Ingénieur Data Scientist', 'Analyse de données et développement de modèles prédictifs', 'TECH', 'Master en Data Science', '50,000 - 70,000 EUR/an', 'Très forte demande', 64.0, 96.0, 58.0, 68.0, 74.0, 82.0, true, false),
(105, 'Enseignant en Mathématiques', 'Enseignement des mathématiques au niveau secondaire', 'EDUCATION', 'Master en Mathématiques', '30,000 - 45,000 EUR/an', 'Stable', 56.0, 82.0, 50.0, 86.0, 60.0, 64.0, true, false),
(106, 'Architecte Logiciel', 'Conception et développement d''architectures logicielles', 'TECH', 'Master en Informatique', '55,000 - 75,000 EUR/an', 'Forte demande', 62.0, 92.0, 60.0, 66.0, 80.0, 76.0, true, false),
(107, 'Infirmier(e) Spécialisé(e)', 'Soins infirmiers spécialisés en unité de soins intensifs', 'HEALTH', 'Diplôme d''État Infirmier', '35,000 - 50,000 EUR/an', 'Très forte demande', 76.0, 64.0, 48.0, 94.0, 56.0, 62.0, true, false),
(108, 'Directeur Commercial', 'Gestion d''équipe commerciale et développement des ventes', 'BUSINESS', 'Master en Commerce', '45,000 - 70,000 EUR/an', 'Croissance', 58.0, 68.0, 62.0, 82.0, 96.0, 70.0, true, false),
(109, 'Graphiste Créatif', 'Création d''identités visuelles et supports de communication', 'ARTS', 'Bachelor en Arts Graphiques', '30,000 - 50,000 EUR/an', 'Stable', 46.0, 60.0, 98.0, 70.0, 62.0, 48.0, true, false),

-- Deuxième série (IDs 200-214) - 15 emplois
-- TECH (5 nouveaux emplois)
(200, 'Développeur Mobile iOS', 'Développement d''applications mobiles pour iPhone et iPad avec Swift', 'TECH', 'Bachelor en Informatique', '40,000 - 60,000 EUR/an', 'Forte demande', 64.0, 82.0, 76.0, 50.0, 68.0, 72.0, true, false),
(201, 'DevOps Engineer', 'Automatisation des déploiements et gestion de l''infrastructure cloud', 'TECH', 'Master en Informatique', '50,000 - 75,000 EUR/an', 'Très forte demande', 82.0, 86.0, 44.0, 56.0, 74.0, 84.0, true, false),
(202, 'Ingénieur en Cybersécurité', 'Protection des systèmes informatiques contre les menaces', 'TECH', 'Master en Cybersécurité', '55,000 - 80,000 EUR/an', 'Très forte demande', 76.0, 94.0, 42.0, 58.0, 64.0, 90.0, true, false),
(203, 'Développeur Blockchain', 'Développement d''applications décentralisées et smart contracts', 'TECH', 'Master en Informatique', '60,000 - 90,000 EUR/an', 'Innovation', 62.0, 96.0, 70.0, 48.0, 82.0, 78.0, true, false),
(204, 'Ingénieur Machine Learning', 'Développement d''algorithmes d''intelligence artificielle', 'TECH', 'Master en IA/ML', '65,000 - 95,000 EUR/an', 'Très forte demande', 58.0, 98.0, 64.0, 54.0, 76.0, 80.0, true, false),

-- HEALTH (3 nouveaux emplois)
(205, 'Médecin Généraliste', 'Diagnostic et traitement des patients en cabinet médical', 'HEALTH', 'Doctorat en Médecine', '60,000 - 100,000 EUR/an', 'Forte demande', 70.0, 84.0, 42.0, 96.0, 78.0, 64.0, true, false),
(206, 'Kinésithérapeute', 'Rééducation fonctionnelle et traitement des troubles musculo-squelettiques', 'HEALTH', 'Master en Kinésithérapie', '35,000 - 55,000 EUR/an', 'Stable', 84.0, 62.0, 56.0, 90.0, 58.0, 68.0, true, false),
(207, 'Nutritionniste', 'Conseil en nutrition et élaboration de régimes alimentaires', 'HEALTH', 'Master en Nutrition', '30,000 - 50,000 EUR/an', 'Croissance', 56.0, 78.0, 62.0, 92.0, 64.0, 74.0, true, false),

-- BUSINESS (3 nouveaux emplois)
(208, 'Analyste Financier', 'Analyse des marchés financiers et conseil en investissement', 'BUSINESS', 'Master en Finance', '45,000 - 70,000 EUR/an', 'Stable', 58.0, 88.0, 46.0, 62.0, 84.0, 92.0, true, false),
(209, 'Consultant en Stratégie', 'Conseil aux entreprises sur leur stratégie de développement', 'BUSINESS', 'Master en Management', '50,000 - 80,000 EUR/an', 'Croissance', 50.0, 82.0, 68.0, 86.0, 94.0, 76.0, true, false),
(210, 'Responsable RH', 'Gestion des ressources humaines et recrutement', 'BUSINESS', 'Master en RH', '40,000 - 65,000 EUR/an', 'Stable', 56.0, 64.0, 62.0, 94.0, 82.0, 86.0, true, false),

-- ARTS (2 nouveaux emplois)
(211, 'Photographe Professionnel', 'Photographie artistique et commerciale', 'ARTS', 'Formation spécialisée', '25,000 - 60,000 EUR/an', 'Variable', 76.0, 58.0, 98.0, 64.0, 74.0, 42.0, true, false),
(212, 'Architecte d''Intérieur', 'Conception et aménagement d''espaces intérieurs', 'ARTS', 'Master en Architecture', '35,000 - 70,000 EUR/an', 'Stable', 78.0, 68.0, 92.0, 76.0, 70.0, 64.0, true, false),

-- EDUCATION (2 nouveaux emplois)
(213, 'Professeur de Langues', 'Enseignement des langues étrangères en collège/lycée', 'EDUCATION', 'Master en Langues', '28,000 - 45,000 EUR/an', 'Stable', 50.0, 62.0, 76.0, 88.0, 64.0, 72.0, true, false),
(214, 'Formateur en Entreprise', 'Formation professionnelle des salariés', 'EDUCATION', 'Master en Formation', '35,000 - 55,000 EUR/an', 'Croissance', 56.0, 66.0, 70.0, 92.0, 84.0, 68.0, true, false),

-- Troisième série (IDs 300-319) - 20 nouveaux emplois
-- TECH (6 nouveaux emplois)
(300, 'Développeur Android', 'Développement d''applications mobiles pour Android avec Kotlin/Java', 'TECH', 'Bachelor en Informatique', '40,000 - 65,000 EUR/an', 'Forte demande', 68.0, 80.0, 72.0, 56.0, 70.0, 76.0, true, false),
(301, 'Ingénieur Cloud', 'Architecture et gestion des solutions cloud (AWS, Azure, GCP)', 'TECH', 'Master en Informatique', '55,000 - 85,000 EUR/an', 'Très forte demande', 78.0, 88.0, 50.0, 58.0, 76.0, 86.0, true, false),
(302, 'Développeur Frontend', 'Développement d''interfaces utilisateur avec React, Vue.js, Angular', 'TECH', 'Bachelor en Informatique', '35,000 - 60,000 EUR/an', 'Forte demande', 56.0, 74.0, 84.0, 62.0, 66.0, 70.0, true, false),
(303, 'Ingénieur Big Data', 'Gestion et analyse de grandes quantités de données', 'TECH', 'Master en Data Science', '60,000 - 90,000 EUR/an', 'Très forte demande', 64.0, 92.0, 56.0, 60.0, 72.0, 88.0, true, false),
(304, 'Développeur Game', 'Création de jeux vidéo avec Unity ou Unreal Engine', 'TECH', 'Bachelor en Informatique', '35,000 - 70,000 EUR/an', 'Croissance', 62.0, 84.0, 90.0, 66.0, 74.0, 64.0, true, false),
(305, 'Ingénieur Réseau', 'Conception et maintenance d''infrastructures réseau', 'TECH', 'Master en Réseaux', '45,000 - 75,000 EUR/an', 'Stable', 86.0, 76.0, 42.0, 54.0, 68.0, 82.0, true, false),

-- HEALTH (4 nouveaux emplois)
(306, 'Dentiste', 'Soins dentaires et chirurgie bucco-dentaire', 'HEALTH', 'Doctorat en Chirurgie Dentaire', '70,000 - 120,000 EUR/an', 'Stable', 82.0, 78.0, 56.0, 84.0, 76.0, 70.0, true, false),
(307, 'Pharmacien', 'Dispensation de médicaments et conseil pharmaceutique', 'HEALTH', 'Master en Pharmacie', '45,000 - 75,000 EUR/an', 'Stable', 74.0, 86.0, 48.0, 82.0, 64.0, 92.0, true, false),
(308, 'Sage-femme', 'Accompagnement des femmes enceintes et accouchements', 'HEALTH', 'Diplôme de Sage-femme', '35,000 - 55,000 EUR/an', 'Forte demande', 80.0, 64.0, 58.0, 96.0, 62.0, 66.0, true, false),
(309, 'Ergothérapeute', 'Rééducation et adaptation des activités quotidiennes', 'HEALTH', 'Master en Ergothérapie', '30,000 - 50,000 EUR/an', 'Croissance', 76.0, 68.0, 72.0, 90.0, 56.0, 74.0, true, false),

-- BUSINESS (4 nouveaux emplois)
(310, 'Comptable', 'Gestion comptable et fiscale des entreprises', 'BUSINESS', 'Master en Comptabilité', '35,000 - 60,000 EUR/an', 'Stable', 58.0, 76.0, 44.0, 62.0, 60.0, 96.0, true, false),
(311, 'Chef de Produit', 'Gestion du cycle de vie des produits digitaux', 'BUSINESS', 'Master en Management', '50,000 - 80,000 EUR/an', 'Croissance', 54.0, 80.0, 66.0, 84.0, 90.0, 72.0, true, false),
(312, 'Acheteur', 'Gestion des achats et négociation avec les fournisseurs', 'BUSINESS', 'Master en Achats', '40,000 - 70,000 EUR/an', 'Stable', 64.0, 70.0, 56.0, 78.0, 86.0, 82.0, true, false),
(313, 'Auditeur Interne', 'Contrôle et évaluation des processus internes', 'BUSINESS', 'Master en Audit', '45,000 - 75,000 EUR/an', 'Stable', 62.0, 84.0, 46.0, 68.0, 74.0, 94.0, true, false),

-- ARTS (3 nouveaux emplois)
(314, 'Réalisateur', 'Création et direction de films et productions audiovisuelles', 'ARTS', 'Formation en Cinéma', '30,000 - 100,000 EUR/an', 'Variable', 70.0, 76.0, 98.0, 82.0, 86.0, 56.0, true, false),
(315, 'Musicien Professionnel', 'Performance musicale et composition', 'ARTS', 'Conservatoire', '25,000 - 80,000 EUR/an', 'Variable', 76.0, 64.0, 98.0, 80.0, 72.0, 50.0, true, false),
(316, 'Scénariste', 'Écriture de scénarios pour films, séries et publicités', 'ARTS', 'Formation en Écriture', '20,000 - 70,000 EUR/an', 'Variable', 48.0, 78.0, 96.0, 74.0, 64.0, 62.0, true, false),

-- EDUCATION (3 nouveaux emplois)
(317, 'Professeur de Sciences', 'Enseignement des sciences au niveau secondaire', 'EDUCATION', 'Master en Sciences', '30,000 - 48,000 EUR/an', 'Stable', 64.0, 86.0, 58.0, 84.0, 62.0, 70.0, true, false),
(318, 'Conseiller d''Orientation', 'Accompagnement des élèves dans leur orientation scolaire', 'EDUCATION', 'Master en Orientation', '32,000 - 50,000 EUR/an', 'Stable', 56.0, 72.0, 64.0, 94.0, 76.0, 68.0, true, false),
(319, 'Formateur en Informatique', 'Formation aux outils informatiques et bureautique', 'EDUCATION', 'Master en Informatique', '35,000 - 55,000 EUR/an', 'Croissance', 68.0, 82.0, 60.0, 86.0, 70.0, 76.0, true, false);

-- Mise à jour des commentaires sur les colonnes pour documenter l'échelle en pourcentage
COMMENT ON COLUMN jobs.riasec_realistic IS 'Score RIASEC Realistic en pourcentage (0-100)';
COMMENT ON COLUMN jobs.riasec_investigative IS 'Score RIASEC Investigative en pourcentage (0-100)';
COMMENT ON COLUMN jobs.riasec_artistic IS 'Score RIASEC Artistic en pourcentage (0-100)';
COMMENT ON COLUMN jobs.riasec_social IS 'Score RIASEC Social en pourcentage (0-100)';
COMMENT ON COLUMN jobs.riasec_enterprising IS 'Score RIASEC Enterprising en pourcentage (0-100)';
COMMENT ON COLUMN jobs.riasec_conventional IS 'Score RIASEC Conventional en pourcentage (0-100)'; 