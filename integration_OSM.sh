# installer osmctools pour le filtre
sudo apt install osmctools

# Convertir osm.pbf en .o5m (plus petit)
osmconvert madagascar-260130.osm.pbf -o=madagascar.o5m


# Extraire uniquement les RN de madagascar.o5m
osmfilter madagascar.o5m \
  --keep="highway=primary highway=trunk ref=RN*" \
  --drop-author \
  --drop-version \
  -o=madagascar_RN.osm
# On a donc madagascar_RN.osm, donnees des RN

# Supprimer le fichier intermediaire
rm madagascar.o5m


# EXTENSION POSTGIS
CREATE EXTENSION postgis;


# EXTENSION hstore
CREATE EXTENSION hstore;


# changer permission
chmod o+r madagascar_RN.osm
chmod o+x ~/Documents ~/Documents/GitHub ~/Documents/GitHub/voyageVoiture-SIG


# IMPORT
sudo -u postgres osm2pgsql \
  -d voyage_voiture_sig \
  -U postgres \
  -O pgsql \
  -C 128 \
  --hstore \
  madagascar_RN.osm

# create table
CREATE TABLE IF NOT EXISTS rn (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50),
    geom GEOMETRY(LineString, 4326)
);

# remplir rn depuis planet_osm_line => mets les tags dans nom, et geometrie dans geom
INSERT INTO rn (nom, geom)
SELECT ref, ST_Transform(way, 4326)
FROM planet_osm_line
WHERE ref LIKE 'N%' OR ref LIKE 'RN%';


