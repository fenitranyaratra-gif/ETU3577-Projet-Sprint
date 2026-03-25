-- Création de la base de données (optionnel)
CREATE DATABASE forage;
\c forage;

-- Table Client
CREATE TABLE Client (
    idClient SERIAL PRIMARY KEY,
    nom VARCHAR(255) NOT NULL,
    contact VARCHAR(100) NOT NULL
);

-- Table TypeDevis
CREATE TABLE TypeDevis (
    idTypeDevis SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL
);

-- Table Demande
CREATE TABLE Demande (
    idDemande SERIAL PRIMARY KEY,
    idClient INTEGER NOT NULL,
    lieu VARCHAR(255),
    adresse TEXT,
    district VARCHAR(100),
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT fk_demande_client FOREIGN KEY (idClient) REFERENCES Client(idClient) ON DELETE RESTRICT
);

-- Table Devis
CREATE TABLE Devis (
    idDevis SERIAL PRIMARY KEY,
    idTypeDevis INTEGER NOT NULL,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    idDemande INTEGER NOT NULL,
    CONSTRAINT fk_devis_type FOREIGN KEY (idTypeDevis) REFERENCES TypeDevis(idTypeDevis) ON DELETE RESTRICT,
    CONSTRAINT fk_devis_demande FOREIGN KEY (idDemande) REFERENCES Demande(idDemande) ON DELETE CASCADE
);

-- Table DetailsDevis
CREATE TABLE DetailsDevis (
    idDetailsDevis SERIAL PRIMARY KEY,
    idDevis INTEGER NOT NULL,
    libelle VARCHAR(255) NOT NULL,
    montant DECIMAL(15, 2) NOT NULL CHECK (montant >= 0),
    CONSTRAINT fk_details_devis FOREIGN KEY (idDevis) REFERENCES Devis(idDevis) ON DELETE CASCADE
);

-- Table Status
CREATE TABLE Status (
    idStatus SERIAL PRIMARY KEY,
    libelle VARCHAR(100) NOT NULL UNIQUE
);

-- Table DemandeStatus (corrigé : la virgule en trop a été supprimée)
CREATE TABLE DemandeStatus (
    idDemandeStatus SERIAL PRIMARY KEY,
    idDemande INTEGER NOT NULL,
    idStatus INTEGER NOT NULL,
    date DATE NOT NULL DEFAULT CURRENT_DATE,
    CONSTRAINT fk_demandestatus_demande FOREIGN KEY (idDemande) REFERENCES Demande(idDemande) ON DELETE CASCADE,
    CONSTRAINT fk_demandestatus_status FOREIGN KEY (idStatus) REFERENCES Status(idStatus) ON DELETE RESTRICT
);

-- Insertion dans la table Status
-- Rappel : le champ 'libelle' est UNIQUE, donc évite les doublons lors de tes tests futurs.
INSERT INTO Status (libelle) VALUES 
('En attente'),
('Approuvé'),
('Rejeté'),
('En cours de traitement'),
('Terminé');

-- Insertion dans la table TypeDevis
INSERT INTO TypeDevis (libelle) VALUES 
('Etude'),
('Forage');
-- 1. Insertion d'un Devis (lié au TypeDevis 1 et à la Demande 1)
INSERT INTO Devis (idTypeDevis, date, idDemande) 
VALUES (1, CURRENT_DATE, 2);

-- 2. Insertion des détails pour ce Devis
-- On utilise CURRVAL pour récupérer l'ID du dernier devis inséré (idDevis)
INSERT INTO DetailsDevis (idDevis, libelle, montant) VALUES 
(currval('devis_iddevis_seq'), 'Installation de la pompe', 1500.00),
(currval('devis_iddevis_seq'), 'Tuyauterie PVC 100m', 850.50),
(currval('devis_iddevis_seq'), 'Main d''œuvre forage', 2200.00),
(currval('devis_iddevis_seq'), 'Filtres et accessoires', 320.75);