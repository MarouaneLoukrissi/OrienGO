-- Migration V1_8: Assurance que tous les scores RIASEC sont en pourcentage (0-100)
-- Convertit les scores qui seraient encore dans l'ancienne échelle (1-5) vers le pourcentage

-- Conversion des scores RIASEC qui seraient encore dans l'ancienne échelle (1-5) vers le pourcentage
-- Si un score est entre 1 et 5, on le convertit en pourcentage
UPDATE jobs SET 
    riasec_realistic = CASE 
        WHEN riasec_realistic BETWEEN 1 AND 5 THEN (riasec_realistic / 5.0) * 100.0
        ELSE riasec_realistic
    END,
    riasec_investigative = CASE 
        WHEN riasec_investigative BETWEEN 1 AND 5 THEN (riasec_investigative / 5.0) * 100.0
        ELSE riasec_investigative
    END,
    riasec_artistic = CASE 
        WHEN riasec_artistic BETWEEN 1 AND 5 THEN (riasec_artistic / 5.0) * 100.0
        ELSE riasec_artistic
    END,
    riasec_social = CASE 
        WHEN riasec_social BETWEEN 1 AND 5 THEN (riasec_social / 5.0) * 100.0
        ELSE riasec_social
    END,
    riasec_enterprising = CASE 
        WHEN riasec_enterprising BETWEEN 1 AND 5 THEN (riasec_enterprising / 5.0) * 100.0
        ELSE riasec_enterprising
    END,
    riasec_conventional = CASE 
        WHEN riasec_conventional BETWEEN 1 AND 5 THEN (riasec_conventional / 5.0) * 100.0
        ELSE riasec_conventional
    END
WHERE 
    (riasec_realistic BETWEEN 1 AND 5) OR
    (riasec_investigative BETWEEN 1 AND 5) OR
    (riasec_artistic BETWEEN 1 AND 5) OR
    (riasec_social BETWEEN 1 AND 5) OR
    (riasec_enterprising BETWEEN 1 AND 5) OR
    (riasec_conventional BETWEEN 1 AND 5);

-- Vérification que tous les scores sont bien dans la plage 0-100
-- Si un score est supérieur à 100, on le ramène à 100
UPDATE jobs SET 
    riasec_realistic = CASE WHEN riasec_realistic > 100 THEN 100.0 ELSE riasec_realistic END,
    riasec_investigative = CASE WHEN riasec_investigative > 100 THEN 100.0 ELSE riasec_investigative END,
    riasec_artistic = CASE WHEN riasec_artistic > 100 THEN 100.0 ELSE riasec_artistic END,
    riasec_social = CASE WHEN riasec_social > 100 THEN 100.0 ELSE riasec_social END,
    riasec_enterprising = CASE WHEN riasec_enterprising > 100 THEN 100.0 ELSE riasec_enterprising END,
    riasec_conventional = CASE WHEN riasec_conventional > 100 THEN 100.0 ELSE riasec_conventional END
WHERE 
    riasec_realistic > 100 OR
    riasec_investigative > 100 OR
    riasec_artistic > 100 OR
    riasec_social > 100 OR
    riasec_enterprising > 100 OR
    riasec_conventional > 100;

-- Si un score est inférieur à 0, on le ramène à 0
UPDATE jobs SET 
    riasec_realistic = CASE WHEN riasec_realistic < 0 THEN 0.0 ELSE riasec_realistic END,
    riasec_investigative = CASE WHEN riasec_investigative < 0 THEN 0.0 ELSE riasec_investigative END,
    riasec_artistic = CASE WHEN riasec_artistic < 0 THEN 0.0 ELSE riasec_artistic END,
    riasec_social = CASE WHEN riasec_social < 0 THEN 0.0 ELSE riasec_social END,
    riasec_enterprising = CASE WHEN riasec_enterprising < 0 THEN 0.0 ELSE riasec_enterprising END,
    riasec_conventional = CASE WHEN riasec_conventional < 0 THEN 0.0 ELSE riasec_conventional END
WHERE 
    riasec_realistic < 0 OR
    riasec_investigative < 0 OR
    riasec_artistic < 0 OR
    riasec_social < 0 OR
    riasec_enterprising < 0 OR
    riasec_conventional < 0;

-- Vérification et affichage des statistiques des scores RIASEC
-- Cette requête affichera la répartition des scores pour vérifier qu'ils sont bien en pourcentage
SELECT 
    'RIASEC Scores Statistics' as info,
    COUNT(*) as total_jobs,
    ROUND(AVG(riasec_realistic), 2) as avg_realistic,
    ROUND(AVG(riasec_investigative), 2) as avg_investigative,
    ROUND(AVG(riasec_artistic), 2) as avg_artistic,
    ROUND(AVG(riasec_social), 2) as avg_social,
    ROUND(AVG(riasec_enterprising), 2) as avg_enterprising,
    ROUND(AVG(riasec_conventional), 2) as avg_conventional,
    MIN(riasec_realistic) as min_realistic,
    MAX(riasec_realistic) as max_realistic,
    MIN(riasec_investigative) as min_investigative,
    MAX(riasec_investigative) as max_investigative,
    MIN(riasec_artistic) as min_artistic,
    MAX(riasec_artistic) as max_artistic,
    MIN(riasec_social) as min_social,
    MAX(riasec_social) as max_social,
    MIN(riasec_enterprising) as min_enterprising,
    MAX(riasec_enterprising) as max_enterprising,
    MIN(riasec_conventional) as min_conventional,
    MAX(riasec_conventional) as max_conventional
FROM jobs;

-- Affichage de quelques exemples d'emplois avec leurs scores pour vérification
SELECT 
    id,
    title,
    category,
    riasec_realistic,
    riasec_investigative,
    riasec_artistic,
    riasec_social,
    riasec_enterprising,
    riasec_conventional
FROM jobs 
ORDER BY id 
LIMIT 10; 