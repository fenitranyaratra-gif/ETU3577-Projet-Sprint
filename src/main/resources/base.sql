
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


INSERT INTO Matiere (nom, coeff) VALUES ('Mathématiques', 5), ('Physique-Chimie', 4);
INSERT INTO Candidat (nom, prenom) VALUES ('Rakoto', 'Antsa');
INSERT INTO Correcteur (nom) VALUES ('Jean Dupont'), ('Marie Curie'), ('Expert Arbitre');
INSERT INTO Operateur (symbole) VALUES ('>'), ('<');

INSERT INTO Resolution (libelleNote) VALUES ('plus petit'), ('moyenne'), ('plus grand');

INSERT INTO Resolution (libelleNote) VALUES 
('plus petit'),
('plus grand'),
('moyenne');

INSERT INTO Matiere (nom, coeff) VALUES ('Mathématiques', 5), ('Physique-Chimie', 4);

\INSERT INTO Candidat (nom, prenom) VALUES ('Rakoto', 'Anna'), ('Rabe', 'Jean');

-- Insertion des correcteurs
INSERT INTO Correcteur (nom, prenom) VALUES 
('Jean', 'Dupont'), 
('Marie', 'Curie'), 
('Expert', 'Arbitre');

INSERT INTO Operateur (symbole) VALUES ('>'), ('<');

INSERT INTO Parametre (id_matiere, ecart_max, id_operateur, id_resolution) VALUES 
(1, 2.00, 1, 1);  -- Math: si écart > 2, prendre note la plus petite (id_resolution=1)

INSERT INTO Note (id_matiere, id_candidat, id_correcteur, valeur_note) VALUES 
(1, 1, 1, 12.5),  -- Math, Rakoto, Dupont: 12.5
(1, 1, 2, 15.0),  -- Math, Rakoto, Curie: 15.0
(1, 1, 3, 10.0);  -- Math, Rakoto, Arbitre: 10.0 (écart = 5 > 2, donc on prend la plus petite: 10.0)

INSERT INTO Parametre (id_matiere, ecart_max, id_operateur, id_resolution) VALUES 
(1, 2.00, 2, 2);
INSERT INTO Note (id_matiere, id_candidat, id_correcteur, valeur_note) VALUES 
(1, 2, 1, 14),  -- Math, Rakoto, Dupont: 12.5
(1, 2, 2, 15.0);  -- Math, Rakoto, Curie: 15.0