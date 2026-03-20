
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



INSERT INTO Matiere (nom, coeff) VALUES ('JAVA', 1), ('PHP', 1);

INSERT INTO candidat (nom, prenom) VALUES 
('CANDIDAT 1', '1'),
('CANDIDAT 2', '2'),
('CANDIDAT 3', '3');
INSERT INTO Correcteur (nom, prenom) VALUES 
('1', 'CORRECTEUR 1'), 
('2', 'CORRECTEUR 2'), 
('3', 'CORRECTEUR 3');

INSERT INTO Operateur (symbole) VALUES ('<'),('<='), ('>'),('>=');
INSERT INTO Resolution (libelleNote) VALUES 
('plus petit'),
('plus grand'),
('moyenne');

INSERT INTO Note (id_matiere, id_candidat, id_correcteur, valeur_note) VALUES 
(1,1,1,15),
(1,1,2,10),
(1,1,3,12),
(2,1,1,10),
(2,1,2,16);
INSERT INTO Note (id_matiere, id_candidat, id_correcteur, valeur_note) VALUES 
(1,2,1,10),
(1,2,2,17.5),
(2,2,1,14),
(2,2,2,11);
INSERT INTO Note (id_matiere, id_candidat, id_correcteur, valeur_note) VALUES 
(1,3,1,10),
(1,3,2,10.5),
(2,3,1,14),
(2,3,2,11);
INSERT INTO parametre (id_matiere, ecart_max, id_operateur, id_resolution) VALUES 
(1, 3, 3, 1), 
(1, 3,2, 3), 
(1, 2, 1, 2),
(1, 5, 3, 1),
(1, 10, 4, 2);
INSERT INTO parametre (id_matiere, ecart_max, id_operateur, id_resolution) VALUES 
(2, 3, 3, 1), 
(2, 3,2, 3), 
(2, 2, 1, 2),
(2, 5, 3, 1),
(2, 10, 4, 2);