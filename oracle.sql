-- CREATE DATABASE voyageVoiture;

CREATE TABLE infoVoyage (
    id NUMBER PRIMARY KEY,
    depart VARCHAR2(100) NOT NULL,
    destination VARCHAR2(100) NOT NULL
);

-- Sequence
CREATE SEQUENCE seq_infoVoyage START WITH 1 INCREMENT BY 1;

-- Trigger
CREATE OR REPLACE TRIGGER trg_infoVoyage_pk
BEFORE INSERT ON infoVoyage
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT seq_infoVoyage.NEXTVAL INTO :NEW.id FROM dual;
    END IF;
END;
/

CREATE TABLE Lalana (
    id NUMBER PRIMARY KEY,
    nom VARCHAR2(100) NOT NULL,
    distance NUMBER(10,2) NOT NULL,
    debut VARCHAR2(100) NOT NULL,
    fin VARCHAR2(100) NOT NULL,
    largeur NUMBER(6,2)
);

-- Sequence
CREATE SEQUENCE seq_lalana START WITH 1 INCREMENT BY 1;

-- Trigger
CREATE OR REPLACE TRIGGER trg_lalana_pk
BEFORE INSERT ON Lalana
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT seq_lalana.NEXTVAL INTO :NEW.id FROM dual;
    END IF;
END;
/

CREATE TABLE Lavaka (
    id NUMBER PRIMARY KEY,
    idLalana NUMBER NOT NULL,
    debutLavaka NUMBER(10,2) NOT NULL,
    finLavaka NUMBER(10,2) NOT NULL,
    tauxRalentissement NUMBER(4,2) NOT NULL,
    CONSTRAINT fk_lavaka_lalana FOREIGN KEY (idLalana) REFERENCES Lalana(id)
);

-- Sequence
CREATE SEQUENCE seq_lavaka START WITH 1 INCREMENT BY 1;

-- Trigger
CREATE OR REPLACE TRIGGER trg_lavaka_pk
BEFORE INSERT ON Lavaka
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT seq_lavaka.NEXTVAL INTO :NEW.id FROM dual;
    END IF;
END;
/


CREATE TABLE pause(
    id NUMBER PRIMARY KEY,
    nom VARCHAR2(100) NOT NULL,
    idLalana NUMBER NOT NULL,
    position NUMBER(10,2) NOT NULL,
    heureDebut DATE NOT NULL,
    heureFin DATE NOT NULL
);

CREATE SEQUENCE seq_pause START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_pause_pk
BEFORE INSERT ON pause
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT seq_pause.NEXTVAL INTO :NEW.id FROM dual;
    END IF;
END;
/

-- -- Donnee-- Segments depuis City X
-- INSERT INTO Lalana (nom, distance, debut, fin, largeur)
-- VALUES ('An-S', 200, 'Antananarivo', 'Sambaina', 5);

-- INSERT INTO Lalana (nom, distance, debut, fin, largeur)
-- VALUES ('S-Ant', 120, 'Sambaina', 'Antsirabe', 5.5);

-- -- Segments intermediaires
-- INSERT INTO Lalana (nom, distance, debut, fin, largeur)
-- VALUES ('An-Am', 163, 'Antananarivo', 'Ampefy', 3);

-- INSERT INTO Lalana (nom, distance, debut, fin, largeur)
-- VALUES ('Am-S', 88, 'Ampefy', 'Sambaina', 4.5);

-- -- Lavaka sur X -> A
-- INSERT INTO Lavaka (idLalana, debutLavaka, finLavaka, tauxRalentissement)
-- VALUES (1, 10, 18, 30);

-- -- Lavaka sur A -> B
-- INSERT INTO Lavaka (idLalana, debutLavaka, finLavaka, tauxRalentissement)
-- VALUES (4, 25, 35, 40);

-- -- Lavaka sur B -> Z
-- INSERT INTO Lavaka (idLalana, debutLavaka, finLavaka, tauxRalentissement)
-- VALUES (5, 15, 25, 20);

-- INSERT INTO Lavaka (idLalana, debutLavaka, finLavaka, tauxRalentissement)
-- VALUES (9, 56, 70, 11);


-- INSERT INTO Lavaka (idLalana, debutLavaka, finLavaka, tauxRalentissement)
-- VALUES (9, 56, 70, 11);


-- COMMIT;

INSERT INTO InfoVoyage (depart, destination)
VALUES ('Antananarivo', 'Antsirabe');

COMMIT;

-- CREATE TABLE Simba (
--     id NUMBER PRIMARY KEY,
--     descriptions VARCHAR2(25) NOT NULL,
--     pointKilometrique NUMBER(10,2) NOT NULL,
--     surface NUMBER(6,2) NOT NULL,
--     profondeur NUMBER(6,2) NOT NULL,
--     idLalana NUMBER NOT NULL,
--     idLavaka NUMBER,
--     idTypeMatiere NUMBER NOT NULL
-- );

CREATE TABLE Simba (
    id NUMBER PRIMARY KEY,
    descriptions VARCHAR2(25),
    pkDebut NUMBER(10,2) ,
    pkFin   NUMBER(10,2) ,
    tauxRalentissement NUMBER(5,2) ,
    surface NUMBER(6,2),
    profondeur NUMBER(6,2),
    idLalana NUMBER ,
    idLavaka NUMBER,
    idTypeMatiere NUMBER
);

CREATE SEQUENCE seq_simba START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_simba_pk
BEFORE INSERT ON simba
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT seq_simba.NEXTVAL INTO :NEW.id FROM dual;
    END IF;
END;
/

CREATE TABLE typeMatiere(
    id NUMBER PRIMARY KEY,
    typeMatiere VARCHAR2(20) NOT NULL,
    descriptions VARCHAR2(25)
);

CREATE SEQUENCE seq_typeMatiere START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_typeMatiere_pk
BEFORE INSERT ON typeMatiere
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT seq_typeMatiere.NEXTVAL INTO :NEW.id FROM dual;
    END IF;
END;
/


CREATE TABLE confPrix(
    id NUMBER PRIMARY KEY,
    min NUMBER(6,2) NOT NULL,
    max NUMBER(6,2) NOT NULL,
    prixMC NUMBER(12,2) NOT NULL,
    idTypeMatiere NUMBER NOT NULL
);

CREATE SEQUENCE seq_confPrix START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_confPrix_pk
BEFORE INSERT ON confPrix
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT seq_confPrix.NEXTVAL INTO :NEW.id FROM dual;
    END IF;
END;
/

CREATE TABLE prixSimba(
    id NUMBER PRIMARY KEY,
    idSimba NUMBER NOT NULL,
    idLalana NUMBER,
    prixSimba NUMBER(12,2) NOT NULL
);


CREATE SEQUENCE seq_prixSimba START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_prixSimba_pk
BEFORE INSERT ON prixSimba
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT seq_prixSimba.NEXTVAL INTO :NEW.id FROM dual;
    END IF;
END;
/

INSERT INTO typeMatiere ( typeMatiere, descriptions) VALUES ( 'Beton', 'Revetement en beton arme');
INSERT INTO typeMatiere ( typeMatiere, descriptions) VALUES ( 'Goudron', 'Revetement bitumineux');
INSERT INTO typeMatiere ( typeMatiere, descriptions) VALUES ( 'Pave', 'Revetement en paves');
COMMIT;


-- p10 -> beton
-- p25 -> Goudron
-- p32 -> pave
-- p44 -> beton
-- p60 -> Goudron

-- UPDATE simba SET idtypematiere = 2 WHERE idlalana = 6;

------ 27-01-26 -------

CREATE TABLE confPluie(
    id NUMBER PRIMARY KEY,
    minP NUMBER(6,2),
    maxP NUMBER(6,2),
    idTypeMatiere NUMBER
);

CREATE SEQUENCE seq_confPluie START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_confPluie_pk
BEFORE INSERT ON confPluie
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT seq_confPluie.NEXTVAL INTO :NEW.id FROM dual;
    END IF;
END;
/

CREATE TABLE pluviometrie(
    id NUMBER PRIMARY KEY,
    tauxPluie NUMBER(6,2),
    pkDebut NUMBER(10,2),
    pkFin NUMBER(10,2)
);

CREATE SEQUENCE seq_pluviometrie START WITH 1 INCREMENT BY 1;

CREATE OR REPLACE TRIGGER trg_pluviometrie_pk
BEFORE INSERT ON pluviometrie
FOR EACH ROW
BEGIN
    IF :NEW.id IS NULL THEN
        SELECT seq_pluviometrie.NEXTVAL INTO :NEW.id FROM dual;
    END IF;
END;
/

----- Test
-- Pluie couvrant PK 25–35

-- insert into pluviometrie (id, tauxpluie, pkdebut, pkfin)
-- values (3, 0.6, 25, 35);

-- -- Pluie couvrant PK 40–50
-- insert into pluviometrie (id, tauxpluie, pkdebut, pkfin)
-- values (4, 0.3, 40, 50);

-- -- Pluie couvrant PK 55–65
-- insert into pluviometrie (id, tauxpluie, pkdebut, pkfin)
-- values (5, 0.7, 55, 65);

-- commit;

---- drop --------

-- drop table lalana;
-- drop SEQUENCE seq_lalana;
-- drop trigger trg_lalana_pk;

-- drop table lavaka;
-- drop SEQUENCE seq_lavaka;
-- drop trigger trg_lavaka_pk;

-- drop table InfoVoyage;
-- drop trigger trg_infoVoyage_pk;
-- drop SEQUENCE seq_infoVoyage;

-- ---- -----------

-- drop table typeMatiere;
-- drop SEQUENCE seq_typeMatiere;
-- drop trigger trg_typeMatiere_pk;

-- drop table simba;
-- drop SEQUENCE seq_simba;
-- drop trigger trg_simba_pk;

-- drop table confPrix;
-- drop SEQUENCE seq_confPrix;
-- drop trigger trg_confPrix_pk;

-- drop table prixSimba;
-- drop SEQUENCE seq_prixSimba;
-- drop trigger trg_prixSimba_pk;

-- drop table confPluie;
-- drop SEQUENCE seq_confPluie;
-- drop trigger trg_confPluie_pk;

-- drop table pluviometrie;
-- drop SEQUENCE seq_pluviometrie;
-- drop trigger trg_pluviometrie_pk;