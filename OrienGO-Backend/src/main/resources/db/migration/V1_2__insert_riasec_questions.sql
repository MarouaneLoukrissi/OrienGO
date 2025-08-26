-- Migration V2: Insert RIASEC questions and answer options
-- Questions for REALISTIC category
INSERT INTO questions (id, category, text, soft_deleted) VALUES
(1, 'REALISTIC', 'J''aime travailler avec mes mains et utiliser des outils', false),
(2, 'REALISTIC', 'Je préfère les activités qui impliquent des objets concrets plutôt que des idées abstraites', false),
(3, 'REALISTIC', 'J''aime résoudre des problèmes techniques et mécaniques', false),
(4, 'REALISTIC', 'Je me sens à l''aise dans des environnements de travail en extérieur', false),
(5, 'REALISTIC', 'J''aime construire, réparer ou assembler des choses', false),
(6, 'REALISTIC', 'Je préfère les instructions claires et précises plutôt que les discussions ouvertes', false),
(7, 'REALISTIC', 'J''aime travailler avec des machines et des équipements', false),
(8, 'REALISTIC', 'Je me sens bien quand je peux voir les résultats concrets de mon travail', false),
(9, 'REALISTIC', 'J''aime les activités qui nécessitent de la précision et de l''attention aux détails', false),
(10, 'REALISTIC', 'Je préfère travailler de manière indépendante plutôt qu''en équipe', false);

-- Questions for INVESTIGATIVE category
INSERT INTO questions (id, category, text, soft_deleted) VALUES
(11, 'INVESTIGATIVE', 'J''aime analyser des données et résoudre des problèmes complexes', false),
(12, 'INVESTIGATIVE', 'Je suis curieux et j''aime découvrir de nouvelles choses', false),
(13, 'INVESTIGATIVE', 'J''aime faire des recherches et explorer des sujets en profondeur', false),
(14, 'INVESTIGATIVE', 'Je préfère les activités qui nécessitent de la réflexion et de la logique', false),
(15, 'INVESTIGATIVE', 'J''aime comprendre comment les choses fonctionnent', false),
(16, 'INVESTIGATIVE', 'Je me sens bien quand je peux utiliser mes compétences analytiques', false),
(17, 'INVESTIGATIVE', 'J''aime les défis intellectuels et les énigmes', false),
(18, 'INVESTIGATIVE', 'Je préfère les environnements de travail calmes et studieux', false),
(19, 'INVESTIGATIVE', 'J''aime les activités qui impliquent des expériences et des observations', false),
(20, 'INVESTIGATIVE', 'Je me sens motivé par la découverte et l''innovation', false);

-- Questions for ARTISTIC category
INSERT INTO questions (id, category, text, soft_deleted) VALUES
(21, 'ARTISTIC', 'J''aime exprimer ma créativité à travers l''art ou la musique', false),
(22, 'ARTISTIC', 'Je préfère les activités qui me permettent d''être original et unique', false),
(23, 'ARTISTIC', 'J''aime créer des choses nouvelles et innovantes', false),
(24, 'ARTISTIC', 'Je me sens bien dans des environnements qui valorisent l''expression personnelle', false),
(25, 'ARTISTIC', 'J''aime les activités qui impliquent de l''imagination et de la fantaisie', false),
(26, 'ARTISTIC', 'Je préfère les environnements de travail flexibles et non structurés', false),
(27, 'ARTISTIC', 'J''aime communiquer des idées à travers des formes d''expression créatives', false),
(28, 'ARTISTIC', 'Je me sens motivé par la beauté et l''esthétique', false),
(29, 'ARTISTIC', 'J''aime les activités qui me permettent d''expérimenter et d''innover', false),
(30, 'ARTISTIC', 'Je préfère les situations qui me permettent d''être spontané et flexible', false);

-- Questions for SOCIAL category
INSERT INTO questions (id, category, text, soft_deleted) VALUES
(31, 'SOCIAL', 'J''aime aider les autres et travailler pour le bien-être de la société', false),
(32, 'SOCIAL', 'Je me sens bien quand je peux enseigner ou former d''autres personnes', false),
(33, 'SOCIAL', 'J''aime travailler en équipe et collaborer avec d''autres', false),
(34, 'SOCIAL', 'Je préfère les activités qui impliquent des interactions avec les gens', false),
(35, 'SOCIAL', 'J''aime résoudre des problèmes qui concernent les relations humaines', false),
(36, 'SOCIAL', 'Je me sens motivé par la possibilité de faire une différence dans la vie des autres', false),
(37, 'SOCIAL', 'J''aime les environnements de travail qui valorisent la coopération', false),
(38, 'SOCIAL', 'Je préfère les activités qui me permettent de comprendre et d''aider les gens', false),
(39, 'SOCIAL', 'J''aime les situations qui nécessitent de l''empathie et de la compréhension', false),
(40, 'SOCIAL', 'Je me sens bien quand je peux contribuer à l''amélioration de la société', false);

-- Questions for ENTERPRISING category
INSERT INTO questions (id, category, text, soft_deleted) VALUES
(41, 'ENTERPRISING', 'J''aime prendre des décisions et assumer des responsabilités', false),
(42, 'ENTERPRISING', 'Je préfère les activités qui impliquent de la persuasion et de la négociation', false),
(43, 'ENTERPRISING', 'J''aime les défis et les situations compétitives', false),
(44, 'ENTERPRISING', 'Je me sens bien quand je peux diriger et motiver d''autres personnes', false),
(45, 'ENTERPRISING', 'J''aime les activités qui impliquent de la planification et de l''organisation', false),
(46, 'ENTERPRISING', 'Je préfère les environnements de travail dynamiques et stimulants', false),
(47, 'ENTERPRISING', 'J''aime les situations qui me permettent de prendre des risques calculés', false),
(48, 'ENTERPRISING', 'Je me sens motivé par la possibilité d''atteindre des objectifs ambitieux', false),
(49, 'ENTERPRISING', 'J''aime les activités qui impliquent de la vente ou du marketing', false),
(50, 'ENTERPRISING', 'Je préfère les situations qui me permettent d''être reconnu pour mes réalisations', false);

-- Questions for CONVENTIONAL category
INSERT INTO questions (id, category, text, soft_deleted) VALUES
(51, 'CONVENTIONAL', 'J''aime travailler avec des données et des informations précises', false),
(52, 'CONVENTIONAL', 'Je préfère les activités qui suivent des procédures établies', false),
(53, 'CONVENTIONAL', 'J''aime organiser et maintenir des systèmes bien structurés', false),
(54, 'CONVENTIONAL', 'Je me sens bien dans des environnements de travail ordonnés et prévisibles', false),
(55, 'CONVENTIONAL', 'J''aime les activités qui nécessitent de l''attention aux détails', false),
(56, 'CONVENTIONAL', 'Je préfère les situations qui impliquent de la planification et de la préparation', false),
(57, 'CONVENTIONAL', 'J''aime travailler avec des chiffres et des calculs', false),
(58, 'CONVENTIONAL', 'Je me sens motivé par la précision et l''exactitude', false),
(59, 'CONVENTIONAL', 'J''aime les activités qui impliquent de la documentation et de la tenue de registres', false),
(60, 'CONVENTIONAL', 'Je préfère les environnements de travail stables et sécurisés', false);

-- Answer options for all questions (5 options per question)
-- Options are: 1=Très en désaccord, 2=En désaccord, 3=Neutre, 4=D'accord, 5=Très d'accord

-- Options for REALISTIC questions (1-10)
INSERT INTO answer_options (id, question_id, option_index, text) VALUES
-- Question 1
(1, 1, 1, 'Très en désaccord'),
(2, 1, 2, 'En désaccord'),
(3, 1, 3, 'Neutre'),
(4, 1, 4, 'D''accord'),
(5, 1, 5, 'Très d''accord'),

-- Question 2
(6, 2, 1, 'Très en désaccord'),
(7, 2, 2, 'En désaccord'),
(8, 2, 3, 'Neutre'),
(9, 2, 4, 'D''accord'),
(10, 2, 5, 'Très d''accord'),

-- Question 3
(11, 3, 1, 'Très en désaccord'),
(12, 3, 2, 'En désaccord'),
(13, 3, 3, 'Neutre'),
(14, 3, 4, 'D''accord'),
(15, 3, 5, 'Très d''accord'),

-- Question 4
(16, 4, 1, 'Très en désaccord'),
(17, 4, 2, 'En désaccord'),
(18, 4, 3, 'Neutre'),
(19, 4, 4, 'D''accord'),
(20, 4, 5, 'Très d''accord'),

-- Question 5
(21, 5, 1, 'Très en désaccord'),
(22, 5, 2, 'En désaccord'),
(23, 5, 3, 'Neutre'),
(24, 5, 4, 'D''accord'),
(25, 5, 5, 'Très d''accord'),

-- Question 6
(26, 6, 1, 'Très en désaccord'),
(27, 6, 2, 'En désaccord'),
(28, 6, 3, 'Neutre'),
(29, 6, 4, 'D''accord'),
(30, 6, 5, 'Très d''accord'),

-- Question 7
(31, 7, 1, 'Très en désaccord'),
(32, 7, 2, 'En désaccord'),
(33, 7, 3, 'Neutre'),
(34, 7, 4, 'D''accord'),
(35, 7, 5, 'Très d''accord'),

-- Question 8
(36, 8, 1, 'Très en désaccord'),
(37, 8, 2, 'En désaccord'),
(38, 8, 3, 'Neutre'),
(39, 8, 4, 'D''accord'),
(40, 8, 5, 'Très d''accord'),

-- Question 9
(41, 9, 1, 'Très en désaccord'),
(42, 9, 2, 'En désaccord'),
(43, 9, 3, 'Neutre'),
(44, 9, 4, 'D''accord'),
(45, 9, 5, 'Très d''accord'),

-- Question 10
(46, 10, 1, 'Très en désaccord'),
(47, 10, 2, 'En désaccord'),
(48, 10, 3, 'Neutre'),
(49, 10, 4, 'D''accord'),
(50, 10, 5, 'Très d''accord');

-- Options for INVESTIGATIVE questions (11-20)
INSERT INTO answer_options (id, question_id, option_index, text) VALUES
-- Question 11
(51, 11, 1, 'Très en désaccord'),
(52, 11, 2, 'En désaccord'),
(53, 11, 3, 'Neutre'),
(54, 11, 4, 'D''accord'),
(55, 11, 5, 'Très d''accord'),

-- Question 12
(56, 12, 1, 'Très en désaccord'),
(57, 12, 2, 'En désaccord'),
(58, 12, 3, 'Neutre'),
(59, 12, 4, 'D''accord'),
(60, 12, 5, 'Très d''accord'),

-- Question 13
(61, 13, 1, 'Très en désaccord'),
(62, 13, 2, 'En désaccord'),
(63, 13, 3, 'Neutre'),
(64, 13, 4, 'D''accord'),
(65, 13, 5, 'Très d''accord'),

-- Question 14
(66, 14, 1, 'Très en désaccord'),
(67, 14, 2, 'En désaccord'),
(68, 14, 3, 'Neutre'),
(69, 14, 4, 'D''accord'),
(70, 14, 5, 'Très d''accord'),

-- Question 15
(71, 15, 1, 'Très en désaccord'),
(72, 15, 2, 'En désaccord'),
(73, 15, 3, 'Neutre'),
(74, 15, 4, 'D''accord'),
(75, 15, 5, 'Très d''accord'),

-- Question 16
(76, 16, 1, 'Très en désaccord'),
(77, 16, 2, 'En désaccord'),
(78, 16, 3, 'Neutre'),
(79, 16, 4, 'D''accord'),
(80, 16, 5, 'Très d''accord'),

-- Question 17
(81, 17, 1, 'Très en désaccord'),
(82, 17, 2, 'En désaccord'),
(83, 17, 3, 'Neutre'),
(84, 17, 4, 'D''accord'),
(85, 17, 5, 'Très d''accord'),

-- Question 18
(86, 18, 1, 'Très en désaccord'),
(87, 18, 2, 'En désaccord'),
(88, 18, 3, 'Neutre'),
(89, 18, 4, 'D''accord'),
(90, 18, 5, 'Très d''accord'),

-- Question 19
(91, 19, 1, 'Très en désaccord'),
(92, 19, 2, 'En désaccord'),
(93, 19, 3, 'Neutre'),
(94, 19, 4, 'D''accord'),
(95, 19, 5, 'Très d''accord'),

-- Question 20
(96, 20, 1, 'Très en désaccord'),
(97, 20, 2, 'En désaccord'),
(98, 20, 3, 'Neutre'),
(99, 20, 4, 'D''accord'),
(100, 20, 5, 'Très d''accord');

-- Options for ARTISTIC questions (21-30)
INSERT INTO answer_options (id, question_id, option_index, text) VALUES
-- Question 21
(101, 21, 1, 'Très en désaccord'),
(102, 21, 2, 'En désaccord'),
(103, 21, 3, 'Neutre'),
(104, 21, 4, 'D''accord'),
(105, 21, 5, 'Très d''accord'),

-- Question 22
(106, 22, 1, 'Très en désaccord'),
(107, 22, 2, 'En désaccord'),
(108, 22, 3, 'Neutre'),
(109, 22, 4, 'D''accord'),
(110, 22, 5, 'Très d''accord'),

-- Question 23
(111, 23, 1, 'Très en désaccord'),
(112, 23, 2, 'En désaccord'),
(113, 23, 3, 'Neutre'),
(114, 23, 4, 'D''accord'),
(115, 23, 5, 'Très d''accord'),

-- Question 24
(116, 24, 1, 'Très en désaccord'),
(117, 24, 2, 'En désaccord'),
(118, 24, 3, 'Neutre'),
(119, 24, 4, 'D''accord'),
(120, 24, 5, 'Très d''accord'),

-- Question 25
(121, 25, 1, 'Très en désaccord'),
(122, 25, 2, 'En désaccord'),
(123, 25, 3, 'Neutre'),
(124, 25, 4, 'D''accord'),
(125, 25, 5, 'Très d''accord'),

-- Question 26
(126, 26, 1, 'Très en désaccord'),
(127, 26, 2, 'En désaccord'),
(128, 26, 3, 'Neutre'),
(129, 26, 4, 'D''accord'),
(130, 26, 5, 'Très d''accord'),

-- Question 27
(131, 27, 1, 'Très en désaccord'),
(132, 27, 2, 'En désaccord'),
(133, 27, 3, 'Neutre'),
(134, 27, 4, 'D''accord'),
(135, 27, 5, 'Très d''accord'),

-- Question 28
(136, 28, 1, 'Très en désaccord'),
(137, 28, 2, 'En désaccord'),
(138, 28, 3, 'Neutre'),
(139, 28, 4, 'D''accord'),
(140, 28, 5, 'Très d''accord'),

-- Question 29
(141, 29, 1, 'Très en désaccord'),
(142, 29, 2, 'En désaccord'),
(143, 29, 3, 'Neutre'),
(144, 29, 4, 'D''accord'),
(145, 29, 5, 'Très d''accord'),

-- Question 30
(146, 30, 1, 'Très en désaccord'),
(147, 30, 2, 'En désaccord'),
(148, 30, 3, 'Neutre'),
(149, 30, 4, 'D''accord'),
(150, 30, 5, 'Très d''accord');

-- Options for SOCIAL questions (31-40)
INSERT INTO answer_options (id, question_id, option_index, text) VALUES
-- Question 31
(151, 31, 1, 'Très en désaccord'),
(152, 31, 2, 'En désaccord'),
(153, 31, 3, 'Neutre'),
(154, 31, 4, 'D''accord'),
(155, 31, 5, 'Très d''accord'),

-- Question 32
(156, 32, 1, 'Très en désaccord'),
(157, 32, 2, 'En désaccord'),
(158, 32, 3, 'Neutre'),
(159, 32, 4, 'D''accord'),
(160, 32, 5, 'Très d''accord'),

-- Question 33
(161, 33, 1, 'Très en désaccord'),
(162, 33, 2, 'En désaccord'),
(163, 33, 3, 'Neutre'),
(164, 33, 4, 'D''accord'),
(165, 33, 5, 'Très d''accord'),

-- Question 34
(166, 34, 1, 'Très en désaccord'),
(167, 34, 2, 'En désaccord'),
(168, 34, 3, 'Neutre'),
(169, 34, 4, 'D''accord'),
(170, 34, 5, 'Très d''accord'),

-- Question 35
(171, 35, 1, 'Très en désaccord'),
(172, 35, 2, 'En désaccord'),
(173, 35, 3, 'Neutre'),
(174, 35, 4, 'D''accord'),
(175, 35, 5, 'Très d''accord'),

-- Question 36
(176, 36, 1, 'Très en désaccord'),
(177, 36, 2, 'En désaccord'),
(178, 36, 3, 'Neutre'),
(179, 36, 4, 'D''accord'),
(180, 36, 5, 'Très d''accord'),

-- Question 37
(181, 37, 1, 'Très en désaccord'),
(182, 37, 2, 'En désaccord'),
(183, 37, 3, 'Neutre'),
(184, 37, 4, 'D''accord'),
(185, 37, 5, 'Très d''accord'),

-- Question 38
(186, 38, 1, 'Très en désaccord'),
(187, 38, 2, 'En désaccord'),
(188, 38, 3, 'Neutre'),
(189, 38, 4, 'D''accord'),
(190, 38, 5, 'Très d''accord'),

-- Question 39
(191, 39, 1, 'Très en désaccord'),
(192, 39, 2, 'En désaccord'),
(193, 39, 3, 'Neutre'),
(194, 39, 4, 'D''accord'),
(195, 39, 5, 'Très d''accord'),

-- Question 40
(196, 40, 1, 'Très en désaccord'),
(197, 40, 2, 'En désaccord'),
(198, 40, 3, 'Neutre'),
(199, 40, 4, 'D''accord'),
(200, 40, 5, 'Très d''accord');

-- Options for ENTERPRISING questions (41-50)
INSERT INTO answer_options (id, question_id, option_index, text) VALUES
-- Question 41
(201, 41, 1, 'Très en désaccord'),
(202, 41, 2, 'En désaccord'),
(203, 41, 3, 'Neutre'),
(204, 41, 4, 'D''accord'),
(205, 41, 5, 'Très d''accord'),

-- Question 42
(206, 42, 1, 'Très en désaccord'),
(207, 42, 2, 'En désaccord'),
(208, 42, 3, 'Neutre'),
(209, 42, 4, 'D''accord'),
(210, 42, 5, 'Très d''accord'),

-- Question 43
(211, 43, 1, 'Très en désaccord'),
(212, 43, 2, 'En désaccord'),
(213, 43, 3, 'Neutre'),
(214, 43, 4, 'D''accord'),
(215, 43, 5, 'Très d''accord'),

-- Question 44
(216, 44, 1, 'Très en désaccord'),
(217, 44, 2, 'En désaccord'),
(218, 44, 3, 'Neutre'),
(219, 44, 4, 'D''accord'),
(220, 44, 5, 'Très d''accord'),

-- Question 45
(221, 45, 1, 'Très en désaccord'),
(222, 45, 2, 'En désaccord'),
(223, 45, 3, 'Neutre'),
(224, 45, 4, 'D''accord'),
(225, 45, 5, 'Très d''accord'),

-- Question 46
(226, 46, 1, 'Très en désaccord'),
(227, 46, 2, 'En désaccord'),
(228, 46, 3, 'Neutre'),
(229, 46, 4, 'D''accord'),
(230, 46, 5, 'Très d''accord'),

-- Question 47
(231, 47, 1, 'Très en désaccord'),
(232, 47, 2, 'En désaccord'),
(233, 47, 3, 'Neutre'),
(234, 47, 4, 'D''accord'),
(235, 47, 5, 'Très d''accord'),

-- Question 48
(236, 48, 1, 'Très en désaccord'),
(237, 48, 2, 'En désaccord'),
(238, 48, 3, 'Neutre'),
(239, 48, 4, 'D''accord'),
(240, 48, 5, 'Très d''accord'),

-- Question 49
(241, 49, 1, 'Très en désaccord'),
(242, 49, 2, 'En désaccord'),
(243, 49, 3, 'Neutre'),
(244, 49, 4, 'D''accord'),
(245, 49, 5, 'Très d''accord'),

-- Question 50
(246, 50, 1, 'Très en désaccord'),
(247, 50, 2, 'En désaccord'),
(248, 50, 3, 'Neutre'),
(249, 50, 4, 'D''accord'),
(250, 50, 5, 'Très d''accord');

-- Options for CONVENTIONAL questions (51-60)
INSERT INTO answer_options (id, question_id, option_index, text) VALUES
-- Question 51
(251, 51, 1, 'Très en désaccord'),
(252, 51, 2, 'En désaccord'),
(253, 51, 3, 'Neutre'),
(254, 51, 4, 'D''accord'),
(255, 51, 5, 'Très d''accord'),

-- Question 52
(256, 52, 1, 'Très en désaccord'),
(257, 52, 2, 'En désaccord'),
(258, 52, 3, 'Neutre'),
(259, 52, 4, 'D''accord'),
(260, 52, 5, 'Très d''accord'),

-- Question 53
(261, 53, 1, 'Très en désaccord'),
(262, 53, 2, 'En désaccord'),
(263, 53, 3, 'Neutre'),
(264, 53, 4, 'D''accord'),
(265, 53, 5, 'Très d''accord'),

-- Question 54
(266, 54, 1, 'Très en désaccord'),
(267, 54, 2, 'En désaccord'),
(268, 54, 3, 'Neutre'),
(269, 54, 4, 'D''accord'),
(270, 54, 5, 'Très d''accord'),

-- Question 55
(271, 55, 1, 'Très en désaccord'),
(272, 55, 2, 'En désaccord'),
(273, 55, 3, 'Neutre'),
(274, 55, 4, 'D''accord'),
(275, 55, 5, 'Très d''accord'),

-- Question 56
(276, 56, 1, 'Très en désaccord'),
(277, 56, 2, 'En désaccord'),
(278, 56, 3, 'Neutre'),
(279, 56, 4, 'D''accord'),
(280, 56, 5, 'Très d''accord'),

-- Question 57
(281, 57, 1, 'Très en désaccord'),
(282, 57, 2, 'En désaccord'),
(283, 57, 3, 'Neutre'),
(284, 57, 4, 'D''accord'),
(285, 57, 5, 'Très d''accord'),

-- Question 58
(286, 58, 1, 'Très en désaccord'),
(287, 58, 2, 'En désaccord'),
(288, 58, 3, 'Neutre'),
(289, 58, 4, 'D''accord'),
(290, 58, 5, 'Très d''accord'),

-- Question 59
(291, 59, 1, 'Très en désaccord'),
(292, 59, 2, 'En désaccord'),
(293, 59, 3, 'Neutre'),
(294, 59, 4, 'D''accord'),
(295, 59, 5, 'Très d''accord'),

-- Question 60
(296, 60, 1, 'Très en désaccord'),
(297, 60, 2, 'En désaccord'),
(298, 60, 3, 'Neutre'),
(299, 60, 4, 'D''accord'),
(300, 60, 5, 'Très d''accord');

SELECT setval('question_seq', (SELECT MAX(id) FROM questions));
SELECT setval('answer_option_seq', (SELECT MAX(id) FROM answer_options));