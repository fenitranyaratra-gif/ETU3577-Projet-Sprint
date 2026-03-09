
CREATE DATABASE noteCorrection;
\c noteCorrection;
CREATE TABLE Matiere (
    id_matiere SERIAL PRIMARY KEY,
    nom VARCHAR(100) NOT NULL,
    coeff INT DEFAULT 1
);

CREATE TABLE Operateur (
    id_operateur SERIAL PRIMARY KEY,
    symbole VARCHAR(10) 
);

CREATE TABLE Candidat (
    id_candidat SERIAL PRIMARY KEY,
    nom VARCHAR(100),
    prenom VARCHAR(100)
);

CREATE TABLE Resolution (
    id_resolution SERIAL PRIMARY KEY,
    libelleNote VARCHAR(100) 
);

CREATE TABLE Correcteur (
    id_correcteur SERIAL PRIMARY KEY,
    nom VARCHAR(100),
    prenom VARCHAR(100)
);

CREATE TABLE Note (
    id_note SERIAL PRIMARY KEY,
    id_matiere INT REFERENCES Matiere(id_matiere),
    id_candidat INT REFERENCES Candidat(id_candidat),
    id_correcteur INT REFERENCES Correcteur(id_correcteur),
    valeur_note DECIMAL(5,2) CHECK (valeur_note BETWEEN 0 AND 20),
    UNIQUE(id_matiere, id_candidat, id_correcteur)
);

CREATE TABLE Parametre (
    id_param SERIAL PRIMARY KEY,
    id_matiere INT REFERENCES Matiere(id_matiere),
    ecart_max DECIMAL(5,2),
    id_operateur INT REFERENCES Operateur(id_operateur),
    id_resolution INT REFERENCES Resolution(id_resolution)
);

CREATE TABLE note_finale (
    id_note_finale SERIAL PRIMARY KEY,
    id_matiere INT REFERENCES Matiere(id_matiere) NOT NULL,
    id_candidat INT REFERENCES Candidat(id_candidat) NOT NULL,
    valeur_note_finale DECIMAL(5,2) CHECK (valeur_note_finale BETWEEN 0 AND 20),
    id_resolution_utilisee INT REFERENCES Resolution(id_resolution),
    date_calcul TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    UNIQUE(id_matiere, id_candidat)
);


-- INSERT INTO Matiere (nom, coeff) VALUES ('Mathématiques', 5), ('Physique-Chimie', 4);
-- INSERT INTO Candidat (nom, prenom) VALUES ('Rakoto', 'Antsa');
-- INSERT INTO Correcteur (nom) VALUES ('Jean Dupont'), ('Marie Curie'), ('Expert Arbitre');
-- INSERT INTO Operateur (symbole) VALUES ('>'), ('<');

-- INSERT INTO Resolution (libelleNote) VALUES ('plus petit'), ('moyenne'), ('plus grand');

INSERT INTO Resolution (libelleNote) VALUES 
('plus petit'),
('moyenne'),
('plus grand');

INSERT INTO Matiere (nom, coeff) VALUES ('Mathématiques', 5), ('Physique', 4),('Chimie', 3);

INSERT INTO candidat (nom, prenom) VALUES 
('CANDIDAT 1', 'Anna'),
('CANDIDAT 2', 'Anna'),
('CANDIDAT 3', 'Anna');
-- Insertion des correcteurs
INSERT INTO Correcteur (nom, prenom) VALUES 
('Prof.', 'CORRECTEUR 1'), 
('Prof.', 'CORRECTEUR 2'), 
('Prof.', 'CORRECTEUR 3'), 
('Prof.', 'CORRECTEUR 4'), 
('Prof.', 'CORRECTEUR 5'), 
('Prof.', 'CORRECTEUR 6');

INSERT INTO Operateur (symbole) VALUES ('<'), ('>'),('=');

-- INSERT INTO Parametre (id_matiere, ecart_max, id_operateur, id_resolution) VALUES 
-- (1, 2.00, 1, 1);  -- Math: si écart > 2, prendre note la plus petite (id_resolution=1)

INSERT INTO Note (id_matiere, id_candidat, id_correcteur, valeur_note) VALUES 
(1,1,1,10.5),
(1,1,2,9),
(1,1,3,13),
(2,1,4,12),
(2,1,5,12),
(2,1,6,12),
(3,1,1,17),
(3,1,2,9),
(3,1,3,14),

(1,2,4,8),
(1,2,5,11),
(1,2,6,9),
(2,2,1,10),
(2,2,2,12),
(2,2,3,11),
(3,2,4,13),
(3,2,5,12),
(3,2,6,14),

(1,3,1,15),
(1,3,2,14),
(1,3,3,16),
(2,3,4,9),
(2,3,5,10),
(2,3,6,11),
(3,3,1,18),
(3,3,2,17),
(3,3,3,16);


INSERT INTO parametre (id_matiere, ecart_max, id_operateur, id_resolution) VALUES 
(1, 8, 1, 1),
(1, 7, 2, 2),
(2, 6, 1, 1),
(2, 3, 2, 2),
(3, 12, 1, 1),
(3, 10, 2, 2);
