# Application de Gestion de Factures - Micro Entrepreneur

## Prérequis

### Base de données

- **WAMP Server** installé et démarré
- **MySQL** (inclus avec WAMP)

### Java

- **Java JDK** (version 8 ou supérieure)
- **Java Runtime Environment (JRE)**

### IDE

- **Visual Studio Code** avec l'extension Java

## Packages et dépendances

### Librairies incluses

- `mysql-connector-java.jar` (déjà inclus dans le dossier `lib/`)

### Installation de la base de données

1. Démarrez WAMP Server
2. Ouvrez phpMyAdmin : `http://localhost/phpmyadmin`
3. Exécutez le script `sql/database_setup.sql` dans phpMyAdmin

## Lancement du projet

### Méthode recommandée (VSCode)

1. Ouvrez le projet dans Visual Studio Code
2. Naviguez vers `src/MainGUI.java`
3. Cliquez sur le bouton **"Run"** (▶️) dans VSCode
4. Ou utilisez le raccourci `F5`

### Identifiants de connexion

- **Login** : `entrepreneur`
- **Mot de passe** : `facture2024`

## Fonctionnalités

- Gestion des formations et consultations
- Génération de factures
- Reporting financier
- Interface graphique intuitive
