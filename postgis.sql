CREATE TABLE rn (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(50),
    geom GEOMETRY(LineString, 4326)
);
