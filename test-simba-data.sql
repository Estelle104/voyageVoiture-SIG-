-- ============================================================================
-- DONNÉES DE TEST SIMBA
-- Insertion de Lalanas et Simba de test pour la démonstration
-- ============================================================================

-- 🛣️ Insertion de Lalanas de test (correspondant aux RN existantes)
INSERT INTO Lalana (nom, distance, debut, fin, largeur)
VALUES ('RN 4', 370, 'Antananarivo', 'Toamasina', 7.0);

INSERT INTO Lalana (nom, distance, debut, fin, largeur)
VALUES ('RN 7', 165, 'Antananarivo', 'Antsirabe', 6.5);

INSERT INTO Lalana (nom, distance, debut, fin, largeur)
VALUES ('RN 1', 947, 'Antananarivo', 'Toliara', 7.5);

INSERT INTO Lalana (nom, distance, debut, fin, largeur)
VALUES ('RN 2', 570, 'Antananarivo', 'Sambava', 6.0);

INSERT INTO Lalana (nom, distance, debut, fin, largeur)
VALUES ('RN 3A', 280, 'Antsirabe', 'Morondava', 5.5);

-- 🏗️ Insertion de TypeMatiere (si pas encore créé)
INSERT INTO typeMatiere (nom) VALUES ('Terre');
INSERT INTO typeMatiere (nom) VALUES ('Sable');
INSERT INTO typeMatiere (nom) VALUES ('Gravier');
INSERT INTO typeMatiere (nom) VALUES ('Rocher');

COMMIT;

-- 🔴 Insertion de Simba de test sur RN 4 (Antananarivo → Toamasina)
-- Simba 1 : Début de route
INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere)
VALUES ('Trou profond', 15.5, 16.2, 25.0, 45.5, 18.0, 1, NULL, 1);

-- Simba 2 : Milieu de route
INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere)
VALUES ('Nids de poule', 89.3, 90.1, 15.0, 32.0, 12.5, 1, NULL, 2);

-- Simba 3 : Zone difficile
INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere)
VALUES ('Affaissement', 156.8, 158.0, 35.0, 78.2, 22.3, 1, NULL, 3);

-- 🔴 Insertion de Simba de test sur RN 7 (Antananarivo → Antsirabe)
-- Simba 4 : Sortie de ville
INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere)
VALUES ('Fissures multiples', 25.0, 25.8, 20.0, 28.5, 8.0, 2, NULL, 1);

-- Simba 5 : Zone montagneuse
INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere)
VALUES ('Cratère important', 78.5, 80.2, 40.0, 65.0, 25.0, 2, NULL, 4);

-- 🔴 Insertion de Simba de test sur RN 1 (Antananarivo → Toliara)
-- Simba 6 : Hauts plateaux
INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere)
VALUES ('Déformation route', 245.3, 246.8, 30.0, 52.0, 15.5, 3, NULL, 2);

-- Simba 7 : Descente vers le sud
INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere)
VALUES ('Érosion latérale', 456.0, 458.5, 28.0, 85.3, 20.8, 3, NULL, 1);

-- Simba 8 : Approche Toliara
INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere)
VALUES ('Trou géant', 823.7, 825.0, 45.0, 95.0, 35.0, 3, NULL, 3);

-- 🔴 Insertion de Simba de test sur RN 2 (Antananarivo → Sambava)
-- Simba 9 : Route du nord
INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere)
VALUES ('Ondulations', 125.2, 126.0, 18.0, 38.5, 10.0, 4, NULL, 2);

-- Simba 10 : Côte est
INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere)
VALUES ('Affaissement humide', 387.5, 389.8, 35.0, 72.8, 28.5, 4, NULL, 1);

-- 🔴 Insertion de Simba de test sur RN 3A (Antsirabe → Morondava)
-- Simba 11 : Traversée centrale
INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere)
VALUES ('Fissures en V', 67.3, 68.1, 22.0, 41.2, 14.0, 5, NULL, 4);

-- Simba 12 : Approche côte ouest
INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere)
VALUES ('Cratère de passage', 198.8, 201.2, 38.0, 88.5, 32.0, 5, NULL, 3);

-- 🔴 Simba additionnels pour tests multiples
-- Simba 13 : RN 4 - Autre zone
INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere)
VALUES ('Nids multiples', 234.5, 235.8, 25.0, 48.0, 16.5, 1, NULL, 2);

-- Simba 14 : RN 7 - Zone critique
INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere)
VALUES ('Effondrement', 112.0, 114.5, 50.0, 120.0, 40.0, 2, NULL, 1);

-- Simba 15 : RN 1 - Test final
INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere)
VALUES ('Série de trous', 678.2, 680.0, 32.0, 68.5, 24.8, 3, NULL, 4);

COMMIT;

-- 📊 Affichage récapitulatif
SELECT 'Lalanas insérées' AS Type, COUNT(*) AS Nombre FROM Lalana
UNION ALL
SELECT 'Simba insérés' AS Type, COUNT(*) AS Nombre FROM Simba;

-- 🗺️ Vue détaillée des Simba par Lalana
SELECT 
    l.nom AS "Route (Lalana)",
    COUNT(s.id) AS "Nb Simba",
    ROUND(AVG(s.surface), 2) AS "Surface moy.",
    ROUND(AVG(s.profondeur), 2) AS "Prof. moy."
FROM Lalana l 
LEFT JOIN Simba s ON l.id = s.idLalana 
GROUP BY l.nom, l.id
ORDER BY COUNT(s.id) DESC;