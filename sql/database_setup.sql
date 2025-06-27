-- Script de création de la base de données pour l'application Bank Facture
-- À exécuter dans phpMyAdmin (WAMP)

-- Créer la base de données
CREATE DATABASE IF NOT EXISTS bank_facture CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE bank_facture;

-- Table des utilisateurs pour l'authentification
CREATE TABLE utilisateurs (
    id INT PRIMARY KEY AUTO_INCREMENT,
    login VARCHAR(50) UNIQUE NOT NULL,
    mot_de_passe VARCHAR(255) NOT NULL,
    nom VARCHAR(100),
    email VARCHAR(100),
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Table des prestations (formations et consultations)
CREATE TABLE prestations (
    id INT PRIMARY KEY AUTO_INCREMENT,
    type_prestation ENUM('Formation', 'Consultation') NOT NULL,
    date_prestation DATE NOT NULL,
    entreprise VARCHAR(200) NOT NULL,
    montant DECIMAL(10,2) NOT NULL,

-- Champs spécifiques aux formations
heure_debut TIME NULL,
heure_fin TIME NULL,
classe VARCHAR(100) NULL,
titre VARCHAR(200) NULL,
tarif_horaire DECIMAL(10, 2) NULL,

-- Champs spécifiques aux consultations
description TEXT NULL,
    tjm DECIMAL(10,2) NULL,
    
    date_creation TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Insérer un utilisateur de test
INSERT INTO
    utilisateurs (
        login,
        mot_de_passe,
        nom,
        email
    )
VALUES (
        'entrepreneur',
        'facture2024',
        'Micro Entrepreneur',
        'contact@entrepreneur.fr'
    );

-- Insérer quelques données de test (optionnel)
INSERT INTO
    prestations (
        type_prestation,
        date_prestation,
        entreprise,
        montant,
        heure_debut,
        heure_fin,
        classe,
        titre,
        tarif_horaire
    )
VALUES (
        'Formation',
        '2024-12-01',
        'TechCorp',
        600.00,
        '09:00:00',
        '17:00:00',
        'Développeurs Java',
        'Formation Spring Boot',
        75.00
    );

INSERT INTO
    prestations (
        type_prestation,
        date_prestation,
        entreprise,
        montant,
        description,
        tjm
    )
VALUES (
        'Consultation',
        '2024-12-15',
        'SecureBank',
        600.00,
        'Audit sécurité application web',
        600.00
    );

-- Afficher les données créées
SELECT 'Utilisateurs créés:' as Info;

SELECT * FROM utilisateurs;

SELECT 'Prestations créées:' as Info;

SELECT * FROM prestations;