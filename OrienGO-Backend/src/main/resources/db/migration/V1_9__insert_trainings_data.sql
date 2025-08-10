-- Insertion des formations pour OrienGO
-- V1_9__insert_trainings_data.sql

-- Formations universitaires (UNIVERSITY)
INSERT INTO trainings (id, name, description, duration, type, highlighted, soft_deleted, version) VALUES
(1, 'Licence Informatique', 'Formation complète en informatique couvrant la programmation, les bases de données, les réseaux et les systèmes d''information.', '3 ans', 'UNIVERSITY', true, false, 1),
(2, 'Master en Psychologie Clinique', 'Formation avancée en psychologie clinique avec stages pratiques et spécialisation en thérapie cognitive-comportementale.', '2 ans', 'UNIVERSITY', true, false, 1),
(3, 'École d''Ingénieur en Data Science', 'Formation d''ingénieur spécialisée en science des données, machine learning et intelligence artificielle.', '5 ans', 'UNIVERSITY', true, false, 1),
(4, 'Licence en Marketing Digital', 'Formation en marketing moderne intégrant les outils numériques, les réseaux sociaux et l''analyse de données.', '3 ans', 'UNIVERSITY', false, false, 1),
(5, 'Master en Design UX/UI', 'Formation spécialisée en design d''expérience utilisateur et d''interface utilisateur avec projets pratiques.', '2 ans', 'UNIVERSITY', true, false, 1);

-- Formations professionnelles (VOCATIONAL)
INSERT INTO trainings (id, name, description, duration, type, highlighted, soft_deleted, version) VALUES
(6, 'Formation Développeur Web Full-Stack', 'Formation intensive pour devenir développeur web complet (frontend et backend) avec technologies modernes.', '6 mois', 'VOCATIONAL', true, false, 1),
(7, 'Formation Conseiller en Orientation', 'Formation pour accompagner les jeunes dans leur choix d''orientation professionnelle et scolaire.', '4 mois', 'VOCATIONAL', false, false, 1),
(8, 'Formation Chef de Projet IT', 'Formation pour gérer des projets informatiques avec méthodologies agiles et outils de gestion.', '8 mois', 'VOCATIONAL', true, false, 1),
(9, 'Formation Analyste de Données', 'Formation pour analyser et interpréter des données métier avec outils de BI et statistiques.', '5 mois', 'VOCATIONAL', false, false, 1),
(10, 'Formation Community Manager', 'Formation pour gérer la présence en ligne des entreprises sur les réseaux sociaux.', '3 mois', 'VOCATIONAL', false, false, 1);

-- Bootcamps (BOOTCAMP)
INSERT INTO trainings (id, name, description, duration, type, highlighted, soft_deleted, version) VALUES
(11, 'Bootcamp Développement React', 'Formation intensive de 12 semaines pour maîtriser React.js et l''écosystème moderne du développement frontend.', '12 semaines', 'BOOTCAMP', true, false, 1),
(12, 'Bootcamp Python Data Science', 'Formation intensive en Python pour la science des données, incluant pandas, numpy, scikit-learn.', '10 semaines', 'BOOTCAMP', true, false, 1),
(13, 'Bootcamp DevOps', 'Formation intensive sur les pratiques DevOps, Docker, Kubernetes et CI/CD.', '8 semaines', 'BOOTCAMP', false, false, 1),
(14, 'Bootcamp UX Design', 'Formation intensive en design d''expérience utilisateur avec projets concrets et portfolio.', '14 semaines', 'BOOTCAMP', true, false, 1),
(15, 'Bootcamp Marketing Digital', 'Formation intensive en marketing digital avec Google Ads, Facebook Ads et analytics.', '6 semaines', 'BOOTCAMP', false, false, 1);

-- Certifications (CERTIFICATION)
INSERT INTO trainings (id, name, description, duration, type, highlighted, soft_deleted, version) VALUES
(16, 'Certification AWS Solutions Architect', 'Certification officielle Amazon Web Services pour architecte de solutions cloud.', '3-6 mois', 'CERTIFICATION', true, false, 1),
(17, 'Certification Google Analytics', 'Certification Google Analytics pour maîtriser l''analyse de données web et l''optimisation.', '1-2 mois', 'CERTIFICATION', false, false, 1),
(18, 'Certification Scrum Master', 'Certification officielle Scrum Alliance pour devenir Scrum Master certifié.', '2-3 mois', 'CERTIFICATION', true, false, 1),
(19, 'Certification Microsoft Azure', 'Certification Microsoft Azure pour les solutions cloud et l''infrastructure.', '4-6 mois', 'CERTIFICATION', false, false, 1),
(20, 'Certification HubSpot Marketing', 'Certification HubSpot pour le marketing inbound et la gestion de la relation client.', '1-3 mois', 'CERTIFICATION', false, false, 1);

-- Cours en ligne (ONLINE_COURSE)
INSERT INTO trainings (id, name, description, duration, type, highlighted, soft_deleted, version) VALUES
(21, 'Cours Machine Learning - Coursera', 'Cours en ligne de Stanford sur le machine learning avec Andrew Ng, couvrant les algorithmes et applications.', '11 semaines', 'ONLINE_COURSE', true, false, 1),
(22, 'Formation JavaScript ES6+ - Udemy', 'Cours complet sur JavaScript moderne, ES6+ et les nouvelles fonctionnalités du langage.', '20 heures', 'ONLINE_COURSE', false, false, 1),
(23, 'Cours Psychologie Positive - edX', 'Cours en ligne sur la psychologie positive et le bien-être au travail.', '8 semaines', 'ONLINE_COURSE', false, false, 1),
(24, 'Formation SEO Avancé - LinkedIn Learning', 'Cours sur l''optimisation pour les moteurs de recherche et le référencement naturel.', '6 heures', 'ONLINE_COURSE', false, false, 1),
(25, 'Cours Design Thinking - MIT OpenCourseWare', 'Cours du MIT sur la méthodologie Design Thinking pour l''innovation.', '12 semaines', 'ONLINE_COURSE', true, false, 1);

-- Stages (INTERNSHIP)
INSERT INTO trainings (id, name, description, duration, type, highlighted, soft_deleted, version) VALUES
(26, 'Stage Développeur Frontend', 'Stage en entreprise pour développer des interfaces utilisateur avec React et TypeScript.', '6 mois', 'INTERNSHIP', true, false, 1),
(27, 'Stage Data Analyst', 'Stage pour analyser des données métier et créer des tableaux de bord avec Power BI.', '4 mois', 'INTERNSHIP', false, false, 1),
(28, 'Stage UX Researcher', 'Stage pour mener des recherches utilisateur et améliorer l''expérience produit.', '3 mois', 'INTERNSHIP', false, false, 1),
(29, 'Stage Marketing Digital', 'Stage pour gérer les campagnes publicitaires et les réseaux sociaux d''une entreprise.', '5 mois', 'INTERNSHIP', false, false, 1),
(30, 'Stage Chef de Projet IT', 'Stage pour participer à la gestion de projets informatiques avec méthodologies agiles.', '6 mois', 'INTERNSHIP', true, false, 1);

-- Apprentissages (APPRENTICESHIP)
INSERT INTO trainings (id, name, description, duration, type, highlighted, soft_deleted, version) VALUES
(31, 'Apprentissage Développeur Full-Stack', 'Formation en alternance pour devenir développeur full-stack avec expérience en entreprise.', '2 ans', 'APPRENTICESHIP', true, false, 1),
(32, 'Apprentissage Data Scientist', 'Formation en alternance en science des données avec projets concrets en entreprise.', '2 ans', 'APPRENTICESHIP', true, false, 1),
(33, 'Apprentissage UX Designer', 'Formation en alternance en design d''expérience utilisateur avec portfolio professionnel.', '1 an', 'APPRENTICESHIP', false, false, 1),
(34, 'Apprentissage Community Manager', 'Formation en alternance pour gérer la communication digitale d''une entreprise.', '1 an', 'APPRENTICESHIP', false, false, 1),
(35, 'Apprentissage Chef de Projet', 'Formation en alternance en gestion de projet avec certification PMP.', '2 ans', 'APPRENTICESHIP', true, false, 1);

-- Ateliers (WORKSHOP)
INSERT INTO trainings (id, name, description, duration, type, highlighted, soft_deleted, version) VALUES
(36, 'Atelier Introduction à React', 'Atelier pratique d''une journée pour découvrir React.js et créer une première application.', '1 jour', 'WORKSHOP', false, false, 1),
(37, 'Atelier Design Sprint', 'Atelier intensif de 5 jours pour résoudre un problème complexe avec la méthodologie Design Sprint.', '5 jours', 'WORKSHOP', true, false, 1),
(38, 'Atelier SEO Local', 'Atelier pour optimiser le référencement local d''une entreprise sur Google My Business.', '1 jour', 'WORKSHOP', false, false, 1),
(39, 'Atelier Gestion de Projet Agile', 'Atelier pratique sur les méthodologies agiles et les outils de gestion de projet.', '2 jours', 'WORKSHOP', false, false, 1),
(40, 'Atelier Analyse de Données', 'Atelier pour analyser des données avec Excel et créer des visualisations impactantes.', '1 jour', 'WORKSHOP', false, false, 1);

-- Séminaires (SEMINAR)
INSERT INTO trainings (id, name, description, duration, type, highlighted, soft_deleted, version) VALUES
(41, 'Séminaire IA et Futur du Travail', 'Séminaire sur l''impact de l''intelligence artificielle sur les métiers de demain.', '1 jour', 'SEMINAR', true, false, 1),
(42, 'Séminaire Psychologie Positive', 'Séminaire sur l''application de la psychologie positive en entreprise.', '1 jour', 'SEMINAR', false, false, 1),
(43, 'Séminaire Transformation Digitale', 'Séminaire sur les enjeux et stratégies de la transformation digitale des entreprises.', '1 jour', 'SEMINAR', true, false, 1),
(44, 'Séminaire Marketing Automation', 'Séminaire sur l''automatisation du marketing et l''utilisation des outils CRM.', '1 jour', 'SEMINAR', false, false, 1),
(45, 'Séminaire Cybersécurité', 'Séminaire sur les bonnes pratiques de cybersécurité en entreprise.', '1 jour', 'SEMINAR', false, false, 1);

-- Auto-formation (SELF_TAUGHT)
INSERT INTO trainings (id, name, description, duration, type, highlighted, soft_deleted, version) VALUES
(46, 'Auto-formation Développement Web', 'Parcours d''auto-formation en développement web avec ressources gratuites et projets personnels.', '6-12 mois', 'SELF_TAUGHT', false, false, 1),
(47, 'Auto-formation Data Science', 'Parcours d''auto-formation en science des données avec MOOCs et projets Kaggle.', '8-15 mois', 'SELF_TAUGHT', false, false, 1),
(48, 'Auto-formation Design UX', 'Parcours d''auto-formation en design UX avec tutoriels et création de portfolio.', '4-8 mois', 'SELF_TAUGHT', false, false, 1),
(49, 'Auto-formation Marketing Digital', 'Parcours d''auto-formation en marketing digital avec certifications Google et Facebook.', '3-6 mois', 'SELF_TAUGHT', false, false, 1),
(50, 'Auto-formation Gestion de Projet', 'Parcours d''auto-formation en gestion de projet avec méthodologies et outils.', '4-8 mois', 'SELF_TAUGHT', false, false, 1);

-- Insertion des spécialisations pour les formations
INSERT INTO training_specializations (training_id, specialization) VALUES
-- Formations universitaires
(1, 'Développement Web'), (1, 'Intelligence Artificielle'), (1, 'Cybersécurité'),
(2, 'Thérapie Cognitive'), (2, 'Psychologie de l''Enfant'), (2, 'Neuropsychologie'),
(3, 'Machine Learning'), (3, 'Big Data'), (3, 'Deep Learning'),
(4, 'Marketing Digital'), (4, 'E-commerce'), (4, 'Branding'),
(5, 'Design d''Interface'), (5, 'Recherche Utilisateur'), (5, 'Prototypage'),

-- Formations professionnelles
(6, 'JavaScript'), (6, 'React'), (6, 'Node.js'), (6, 'Base de données'),
(7, 'Orientation Scolaire'), (7, 'Orientation Professionnelle'), (7, 'Bilan de Compétences'),
(8, 'Méthodologies Agiles'), (8, 'Gestion d''Équipe'), (8, 'Planification'),
(9, 'SQL'), (9, 'Python'), (9, 'Tableau'), (9, 'Power BI'),
(10, 'Réseaux Sociaux'), (10, 'Content Marketing'), (10, 'Publicité en ligne'),

-- Bootcamps
(11, 'React.js'), (11, 'Redux'), (11, 'TypeScript'), (11, 'Next.js'),
(12, 'Python'), (12, 'Pandas'), (12, 'NumPy'), (12, 'Scikit-learn'),
(13, 'Docker'), (13, 'Kubernetes'), (13, 'Jenkins'), (13, 'Terraform'),
(14, 'Design Thinking'), (14, 'Prototypage'), (14, 'Tests Utilisateur'),
(15, 'Google Ads'), (15, 'Facebook Ads'), (15, 'Analytics'),

-- Certifications
(16, 'AWS'), (16, 'Cloud Computing'), (16, 'Architecture'),
(17, 'Google Analytics'), (17, 'Web Analytics'), (17, 'Optimisation'),
(18, 'Scrum'), (18, 'Agile'), (18, 'Gestion de Projet'),
(19, 'Microsoft Azure'), (19, 'Cloud'), (19, 'DevOps'),
(20, 'HubSpot'), (20, 'Marketing Inbound'), (20, 'CRM'),

-- Cours en ligne
(21, 'Machine Learning'), (21, 'Algorithms'), (21, 'Neural Networks'),
(22, 'JavaScript'), (22, 'ES6'), (22, 'Modern JS'),
(23, 'Psychologie Positive'), (23, 'Bien-être'), (23, 'Développement Personnel'),
(24, 'SEO'), (24, 'Référencement'), (24, 'Google'),
(25, 'Design Thinking'), (25, 'Innovation'), (25, 'Résolution de Problèmes'),

-- Stages
(26, 'Frontend'), (26, 'React'), (26, 'TypeScript'),
(27, 'Data Analysis'), (27, 'Power BI'), (27, 'Excel'),
(28, 'UX Research'), (28, 'User Testing'), (28, 'Personas'),
(29, 'Digital Marketing'), (29, 'Social Media'), (29, 'Campaigns'),
(30, 'Project Management'), (30, 'Agile'), (30, 'Scrum'),

-- Apprentissages
(31, 'Full-Stack'), (31, 'Web Development'), (31, 'Databases'),
(32, 'Data Science'), (32, 'Machine Learning'), (32, 'Python'),
(33, 'UX Design'), (33, 'UI Design'), (33, 'Prototyping'),
(34, 'Community Management'), (34, 'Social Media'), (34, 'Content Creation'),
(35, 'Project Management'), (35, 'Leadership'), (35, 'PMP'),

-- Ateliers
(36, 'React'), (36, 'JavaScript'), (36, 'Frontend'),
(37, 'Design Sprint'), (37, 'Innovation'), (37, 'Problem Solving'),
(38, 'SEO Local'), (38, 'Google My Business'), (38, 'Local Marketing'),
(39, 'Agile'), (39, 'Scrum'), (39, 'Project Management'),
(40, 'Data Analysis'), (40, 'Excel'), (40, 'Visualization'),

-- Séminaires
(41, 'Artificial Intelligence'), (41, 'Future of Work'), (41, 'Technology'),
(42, 'Positive Psychology'), (42, 'Well-being'), (42, 'Workplace'),
(43, 'Digital Transformation'), (43, 'Innovation'), (43, 'Strategy'),
(44, 'Marketing Automation'), (44, 'CRM'), (44, 'Automation'),
(45, 'Cybersecurity'), (45, 'Security'), (45, 'Best Practices'),

-- Auto-formation
(46, 'Web Development'), (46, 'Self-Learning'), (46, 'Projects'),
(47, 'Data Science'), (47, 'MOOCs'), (47, 'Kaggle'),
(48, 'UX Design'), (48, 'Portfolio'), (48, 'Self-Learning'),
(49, 'Digital Marketing'), (49, 'Certifications'), (49, 'Self-Learning'),
(50, 'Project Management'), (50, 'Methodologies'), (50, 'Self-Learning');

-- Mise à jour de la séquence pour les prochaines insertions
SELECT setval('training_seq', 50);
