-- Vérification du test 702
-- Exécute ces requêtes dans IntelliJ ou dans ton client PostgreSQL

-- 1. Vérifier si le test 702 existe
SELECT 
    id,
    type,
    status,
    soft_deleted,
    started_at,
    completed_at,
    questions_count,
    duration_minutes,
    student_id
FROM tests 
WHERE id = 702;

-- 2. Vérifier les questions associées au test 702
SELECT 
    tq.test_id,
    tq.question_id,
    q.category,
    q.text
FROM test_questions tq
JOIN questions q ON tq.question_id = q.id
WHERE tq.test_id = 702
ORDER BY q.category, q.id;

-- 3. Vérifier si le test a un résultat
SELECT 
    tr.id,
    tr.dominant_type,
    tr.dominant_type_description,
    tr.key_points,
    tr.soft_deleted
FROM test_results tr
WHERE tr.test_id = 702;

-- 4. Vérifier les scores du test (si le résultat existe)
SELECT 
    trs.result_id,
    trs.type,
    trs.percentage
FROM test_result_scores trs
JOIN test_results tr ON trs.result_id = tr.id
WHERE tr.test_id = 702;

-- 5. Vérifier l'étudiant associé au test
SELECT 
    s.id,
    s.school,
    s.field_of_study,
    s.education_level,
    u.first_name,
    u.last_name,
    u.email
FROM students s
JOIN users u ON s.id = u.id
JOIN tests t ON s.id = t.student_id
WHERE t.id = 702;

-- 6. Lister tous les tests récents pour voir s'il y a un problème
SELECT 
    id,
    type,
    status,
    soft_deleted,
    started_at,
    student_id
FROM tests 
WHERE id >= 700
ORDER BY id DESC
LIMIT 10; 