-- Migration V1_7: Modification de la structure pour empêcher les champs NULL
-- Rend les champs importants NOT NULL après avoir rempli toutes les valeurs

-- Modification des colonnes pour les rendre NOT NULL avec des valeurs par défaut
ALTER TABLE jobs ALTER COLUMN education SET NOT NULL;
ALTER TABLE jobs ALTER COLUMN salary_range SET NOT NULL;
ALTER TABLE jobs ALTER COLUMN job_market SET NOT NULL;

-- Modification des colonnes RIASEC pour les rendre NOT NULL
ALTER TABLE jobs ALTER COLUMN riasec_realistic SET NOT NULL;
ALTER TABLE jobs ALTER COLUMN riasec_investigative SET NOT NULL;
ALTER TABLE jobs ALTER COLUMN riasec_artistic SET NOT NULL;
ALTER TABLE jobs ALTER COLUMN riasec_social SET NOT NULL;
ALTER TABLE jobs ALTER COLUMN riasec_enterprising SET NOT NULL;
ALTER TABLE jobs ALTER COLUMN riasec_conventional SET NOT NULL;

-- Ajout de contraintes de validation pour s'assurer que les scores RIASEC sont entre 0 et 100
ALTER TABLE jobs ADD CONSTRAINT chk_riasec_realistic CHECK (riasec_realistic >= 0 AND riasec_realistic <= 100);
ALTER TABLE jobs ADD CONSTRAINT chk_riasec_investigative CHECK (riasec_investigative >= 0 AND riasec_investigative <= 100);
ALTER TABLE jobs ADD CONSTRAINT chk_riasec_artistic CHECK (riasec_artistic >= 0 AND riasec_artistic <= 100);
ALTER TABLE jobs ADD CONSTRAINT chk_riasec_social CHECK (riasec_social >= 0 AND riasec_social <= 100);
ALTER TABLE jobs ADD CONSTRAINT chk_riasec_enterprising CHECK (riasec_enterprising >= 0 AND riasec_enterprising <= 100);
ALTER TABLE jobs ADD CONSTRAINT chk_riasec_conventional CHECK (riasec_conventional >= 0 AND riasec_conventional <= 100);

-- Ajout d'un index pour améliorer les performances des requêtes par catégorie
CREATE INDEX IF NOT EXISTS idx_jobs_category_active ON jobs(category, active);

-- Ajout d'un index pour les scores RIASEC (utile pour les recommandations)
CREATE INDEX IF NOT EXISTS idx_jobs_riasec_scores ON jobs(riasec_realistic, riasec_investigative, riasec_artistic, riasec_social, riasec_enterprising, riasec_conventional);

-- Mise à jour des commentaires pour documenter les contraintes
COMMENT ON TABLE jobs IS 'Table des emplois avec scores RIASEC en pourcentage (0-100). Tous les champs sont obligatoires.';
COMMENT ON COLUMN jobs.education IS 'Niveau d''éducation requis (obligatoire)';
COMMENT ON COLUMN jobs.salary_range IS 'Fourchette de salaire (obligatoire)';
COMMENT ON COLUMN jobs.job_market IS 'État du marché du travail (obligatoire)';
COMMENT ON COLUMN jobs.riasec_realistic IS 'Score RIASEC Realistic en pourcentage (0-100, obligatoire)';
COMMENT ON COLUMN jobs.riasec_investigative IS 'Score RIASEC Investigative en pourcentage (0-100, obligatoire)';
COMMENT ON COLUMN jobs.riasec_artistic IS 'Score RIASEC Artistic en pourcentage (0-100, obligatoire)';
COMMENT ON COLUMN jobs.riasec_social IS 'Score RIASEC Social en pourcentage (0-100, obligatoire)';
COMMENT ON COLUMN jobs.riasec_enterprising IS 'Score RIASEC Enterprising en pourcentage (0-100, obligatoire)';
COMMENT ON COLUMN jobs.riasec_conventional IS 'Score RIASEC Conventional en pourcentage (0-100, obligatoire)'; 