-- Migration V1_6: Remplissage de tous les champs NULL dans la table jobs
-- Assure que tous les champs ont des valeurs par défaut appropriées

-- Remplissage des champs education NULL
UPDATE jobs SET 
    education = CASE 
        WHEN category = 'TECH' THEN 'Bachelor/Master en Informatique'
        WHEN category = 'HEALTH' THEN 'Master/Doctorat en Médecine'
        WHEN category = 'BUSINESS' THEN 'Master en Management/Commerce'
        WHEN category = 'ARTS' THEN 'Bachelor/Master en Arts'
        WHEN category = 'EDUCATION' THEN 'Master en Sciences de l''Éducation'
        ELSE 'Formation spécialisée'
    END
WHERE education IS NULL;

-- Remplissage des champs salary_range NULL
UPDATE jobs SET 
    salary_range = CASE 
        WHEN category = 'TECH' THEN '40,000 - 80,000 EUR/an'
        WHEN category = 'HEALTH' THEN '35,000 - 100,000 EUR/an'
        WHEN category = 'BUSINESS' THEN '40,000 - 80,000 EUR/an'
        WHEN category = 'ARTS' THEN '25,000 - 70,000 EUR/an'
        WHEN category = 'EDUCATION' THEN '30,000 - 55,000 EUR/an'
        ELSE '35,000 - 60,000 EUR/an'
    END
WHERE salary_range IS NULL;

-- Remplissage des champs job_market NULL
UPDATE jobs SET 
    job_market = CASE 
        WHEN category = 'TECH' THEN 'Forte demande'
        WHEN category = 'HEALTH' THEN 'Stable'
        WHEN category = 'BUSINESS' THEN 'Croissance'
        WHEN category = 'ARTS' THEN 'Variable'
        WHEN category = 'EDUCATION' THEN 'Stable'
        ELSE 'Stable'
    END
WHERE job_market IS NULL;

-- Remplissage des scores RIASEC NULL avec des valeurs par défaut par catégorie
-- TECH
UPDATE jobs SET 
    riasec_realistic = COALESCE(riasec_realistic, 60.0),
    riasec_investigative = COALESCE(riasec_investigative, 85.0),
    riasec_artistic = COALESCE(riasec_artistic, 40.0),
    riasec_social = COALESCE(riasec_social, 50.0),
    riasec_enterprising = COALESCE(riasec_enterprising, 70.0),
    riasec_conventional = COALESCE(riasec_conventional, 65.0)
WHERE category = 'TECH' AND (
    riasec_realistic IS NULL OR 
    riasec_investigative IS NULL OR 
    riasec_artistic IS NULL OR 
    riasec_social IS NULL OR 
    riasec_enterprising IS NULL OR 
    riasec_conventional IS NULL
);

-- HEALTH
UPDATE jobs SET 
    riasec_realistic = COALESCE(riasec_realistic, 70.0),
    riasec_investigative = COALESCE(riasec_investigative, 75.0),
    riasec_artistic = COALESCE(riasec_artistic, 30.0),
    riasec_social = COALESCE(riasec_social, 90.0),
    riasec_enterprising = COALESCE(riasec_enterprising, 50.0),
    riasec_conventional = COALESCE(riasec_conventional, 60.0)
WHERE category = 'HEALTH' AND (
    riasec_realistic IS NULL OR 
    riasec_investigative IS NULL OR 
    riasec_artistic IS NULL OR 
    riasec_social IS NULL OR 
    riasec_enterprising IS NULL OR 
    riasec_conventional IS NULL
);

-- BUSINESS
UPDATE jobs SET 
    riasec_realistic = COALESCE(riasec_realistic, 50.0),
    riasec_investigative = COALESCE(riasec_investigative, 70.0),
    riasec_artistic = COALESCE(riasec_artistic, 40.0),
    riasec_social = COALESCE(riasec_social, 80.0),
    riasec_enterprising = COALESCE(riasec_enterprising, 90.0),
    riasec_conventional = COALESCE(riasec_conventional, 80.0)
WHERE category = 'BUSINESS' AND (
    riasec_realistic IS NULL OR 
    riasec_investigative IS NULL OR 
    riasec_artistic IS NULL OR 
    riasec_social IS NULL OR 
    riasec_enterprising IS NULL OR 
    riasec_conventional IS NULL
);

-- ARTS
UPDATE jobs SET 
    riasec_realistic = COALESCE(riasec_realistic, 40.0),
    riasec_investigative = COALESCE(riasec_investigative, 50.0),
    riasec_artistic = COALESCE(riasec_artistic, 95.0),
    riasec_social = COALESCE(riasec_social, 60.0),
    riasec_enterprising = COALESCE(riasec_enterprising, 70.0),
    riasec_conventional = COALESCE(riasec_conventional, 30.0)
WHERE category = 'ARTS' AND (
    riasec_realistic IS NULL OR 
    riasec_investigative IS NULL OR 
    riasec_artistic IS NULL OR 
    riasec_social IS NULL OR 
    riasec_enterprising IS NULL OR 
    riasec_conventional IS NULL
);

-- EDUCATION
UPDATE jobs SET 
    riasec_realistic = COALESCE(riasec_realistic, 50.0),
    riasec_investigative = COALESCE(riasec_investigative, 75.0),
    riasec_artistic = COALESCE(riasec_artistic, 40.0),
    riasec_social = COALESCE(riasec_social, 90.0),
    riasec_enterprising = COALESCE(riasec_enterprising, 60.0),
    riasec_conventional = COALESCE(riasec_conventional, 60.0)
WHERE category = 'EDUCATION' AND (
    riasec_realistic IS NULL OR 
    riasec_investigative IS NULL OR 
    riasec_artistic IS NULL OR 
    riasec_social IS NULL OR 
    riasec_enterprising IS NULL OR 
    riasec_conventional IS NULL
);

-- Remplissage des scores RIASEC pour les autres catégories (si elles existent)
UPDATE jobs SET 
    riasec_realistic = COALESCE(riasec_realistic, 60.0),
    riasec_investigative = COALESCE(riasec_investigative, 70.0),
    riasec_artistic = COALESCE(riasec_artistic, 50.0),
    riasec_social = COALESCE(riasec_social, 70.0),
    riasec_enterprising = COALESCE(riasec_enterprising, 70.0),
    riasec_conventional = COALESCE(riasec_conventional, 60.0)
WHERE (
    riasec_realistic IS NULL OR 
    riasec_investigative IS NULL OR 
    riasec_artistic IS NULL OR 
    riasec_social IS NULL OR 
    riasec_enterprising IS NULL OR 
    riasec_conventional IS NULL
);

-- Vérification et affichage des statistiques
-- Cette requête affichera le nombre d'enregistrements avec des champs NULL (devrait être 0)
SELECT 
    COUNT(*) as total_jobs,
    COUNT(CASE WHEN education IS NULL THEN 1 END) as null_education,
    COUNT(CASE WHEN salary_range IS NULL THEN 1 END) as null_salary_range,
    COUNT(CASE WHEN job_market IS NULL THEN 1 END) as null_job_market,
    COUNT(CASE WHEN riasec_realistic IS NULL THEN 1 END) as null_riasec_realistic,
    COUNT(CASE WHEN riasec_investigative IS NULL THEN 1 END) as null_riasec_investigative,
    COUNT(CASE WHEN riasec_artistic IS NULL THEN 1 END) as null_riasec_artistic,
    COUNT(CASE WHEN riasec_social IS NULL THEN 1 END) as null_riasec_social,
    COUNT(CASE WHEN riasec_enterprising IS NULL THEN 1 END) as null_riasec_enterprising,
    COUNT(CASE WHEN riasec_conventional IS NULL THEN 1 END) as null_riasec_conventional
FROM jobs; 