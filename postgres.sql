CREATE DATABASE voyageVoiture;

\c voyageVoiture;

-- Table Voiture
CREATE TABLE voiture (
    id SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    type VARCHAR(50) NOT NULL,
    vitesseMax INTEGER NOT NULL,
    largeur NUMERIC(6,2) NOT NULL,
    longueur NUMERIC(6,2) NOT NULL,
    reservoire NUMERIC(8,2),
    consommation NUMERIC(8,2)
);

INSERT INTO voiture (nom, type, vitesseMax, largeur, longueur, reservoire, consommation) VALUES
('Toyota Corolla', 'Berline', 180, 1.75, 4.62, 20, 5),
('Ford F-150', 'Camionnette', 160, 2.03, 5.89, 30, 10),
('Honda Civic', 'Berline', 200, 1.80, 4.50, 25, 15),
('Chevrolet Silverado', 'Camionnette', 155, 2.05, 5.89, 50,10),
('BMW X5', 'SUV', 210, 2.00, 4.92, 20,5);

INSERT INTO voiture (nom, type, vitesseMax, largeur, longueur, reservoire, consommation) VALUES
('VAOVAO', 'Berline', 180, 1.2, 4.62, 50, 9),
