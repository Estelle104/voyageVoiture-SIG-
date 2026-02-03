-- Insérer des routes nationales d'exemple pour Madagascar
-- À exécuter après création de la table rn

INSERT INTO rn (nom, geom) VALUES
('RN1', ST_GeomFromText('LINESTRING(46.8696 -18.8792, 46.9696 -18.9792, 47.0696 -19.0792)', 4326)),
('RN2', ST_GeomFromText('LINESTRING(47.5 -19.5, 47.6 -19.6, 47.7 -19.7)', 4326)),
('RN4', ST_GeomFromText('LINESTRING(44.0 -20.0, 44.1 -20.1, 44.2 -20.2)', 4326)),
('RN5', ST_GeomFromText('LINESTRING(48.0 -18.0, 48.1 -18.1, 48.2 -18.2)', 4326));

-- Vérifier les données
SELECT id, nom, ST_AsText(geom) FROM rn;
