CREATE DATABASE forage;
\c forage;

CREATE TABLE Client (
    idClient SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    contact VARCHAR(100) NOT NULL
);

CREATE TABLE TypeDevis (
    idTypeDevis SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

CREATE TABLE Demande (
    idDemande SERIAL PRIMARY KEY,
    idClient INTEGER NOT NULL,
    lieu VARCHAR(255),
    adresse TEXT,
    district VARCHAR(100),
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT fk_demande_client FOREIGN KEY (idClient) REFERENCES Client(idClient) ON DELETE RESTRICT
);

CREATE TABLE Devis (
    idDevis SERIAL PRIMARY KEY,
    idTypeDevis INTEGER NOT NULL,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    idDemande INTEGER NOT NUL
);

CREATE TABLE DetailsDevis (
    idDetailsDevis SERIAL PRIMARY KEY,
    idDevis INTEGER NOT NULL,
    libelle VARCHAR(255) NOT NULL,
    montant DECIMAL(15, 2) 
);

CREATE TABLE Status (
    idStatus SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE DemandeStatus (
    idDemandeStatus SERIAL PRIMARY KEY,
    idDemande INTEGER NOT NULL,
    idStatus INTEGER NOT NULL,
    date DATE NOT NULL DEFAULT CURRENT_DATE
);

-- INSERT INTO Status (libelle) VALUES 
-- ('En attente'),
-- ('Approuvé'),
-- ('Rejeté'),
-- ('En cours de traitement'),
-- ('Terminé');
-- INSERT INTO TypeDevis (libelle) VALUES 
-- ('Etude'),
-- ('Forage');
-- -- 1. Insertion d'un Devis (lié au TypeDevis 1 et à la Demande 1)
-- INSERT INTO Devis (idTypeDevis, date, idDemande) 
-- VALUES (1, CURRENT_DATE, 2);

-- INSERT INTO DetailsDevis (idDevis, libelle, montant) VALUES 
-- (currval('devis_iddevis_seq'), 'Installation de la pompe', 1500.00),
-- (currval('devis_iddevis_seq'), 'Tuyauterie PVC 100m', 850.50),
-- (currval('devis_iddevis_seq'), 'Main d''œuvre forage', 2200.00),
-- (currval('devis_iddevis_seq'), 'Filtres et accessoires', 320.75);