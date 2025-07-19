# Application de Gestion de Factures - Micro Entrepreneur

## Description

Application Java avec base de données MySQL (WAMP) pour gérer les factures d'un micro-entrepreneur spécialisé dans les formations et consultations.

## 🗄️ Base de données MySQL/WAMP

### Prérequis

1. **WAMP Server** installé et démarré
2. **MySQL Connector/J** (driver JDBC MySQL) - déjà inclus dans `lib/`

### Installation de la base de données

#### 1. Créer la base de données

1. Démarrez WAMP (icône verte dans la barre des tâches)
2. Ouvrez phpMyAdmin : `http://localhost/phpmyadmin`
3. Exécutez le script `sql/database_setup.sql` dans phpMyAdmin

#### 2. Structure de la base

```sql
Base: bank_facture
Tables:
- utilisateurs (login, mot_de_passe, nom, email)
- prestations (type, date, entreprise, montant, + champs spécifiques)
```

## 🏗️ Structure du projet

```
Bank_facture/
├── src/                    # Code source Java
│   ├── Main.java          # Point d'entrée de l'application
│   ├── AuthManager.java   # Authentification avec base de données
│   ├── DatabaseManager.java # Gestionnaire de connexion MySQL
│   ├── PrestationManager.java # CRUD des prestations en base
│   ├── Prestation.java    # Classe abstraite de base
│   ├── Formation.java     # Classe pour les formations
│   └── Consultation.java  # Classe pour les consultations
│
├── lib/                   # Librairies externes
│   └── mysql-connector-java.jar # Driver JDBC MySQL
│
├── sql/                   # Scripts de base de données
│   └── database_setup.sql # Script de création de la base
│
├── scripts/               # Script de build
│   └── build.bat         # Compilation + lancement
│
├── docs/                  # Documentation
│   └── INSTALLATION_WAMP.md # Guide d'installation WAMP
│
├── build/                 # Fichiers compilés (.class)
└── README.md             # Documentation principale
```

## Fonctionnalités implémentées

### Partie 1.2 : Authentification 🔐

- Système de connexion avec base de données MySQL
- Vérification des identifiants en temps réel
- 3 tentatives maximum
- Test de connexion automatique

### Partie 3 : Saisie des prestations 📝

- **Formations** : sauvegardées en base avec tous les détails

  - Date, heure début/fin, classe/groupe, titre/module
  - Entreprise, tarif horaire
  - Calcul automatique et stockage du montant

- **Consultations** : sauvegardées en base avec TJM

  - Date, description, TJM, entreprise
  - Calcul automatique et stockage du montant

- **Affichage** : Récupération depuis la base de données

## 🚀 Utilisation

### 1. Préparation

1. Créer la base avec `sql/database_setup.sql` dans phpMyAdmin
2. S'assurer que WAMP est démarré (icône verte)

### 2. Compilation et lancement

```cmd
scripts\build.bat
```

### 3. Lancement seul (si déjà compilé)

```cmd
scripts\run.bat
```

### 4. Nettoyage du projet

```cmd
scripts\clean.bat
```

### 5. Connexion

Utiliser les identifiants de la base :

- **Login** : entrepreneur
- **Mot de passe** : facture2024

## Exemples de saisie

### Formation

- Date : 15/12/2024
- Heure début : 09:00
- Heure fin : 17:00
- Classe : Développeurs Java
- Titre : Formation Spring Boot
- Entreprise : TechCorp
- Tarif horaire : 75 €

### Consultation

- Date : 20/12/2024
- Description : Audit sécurité application web
- TJM : 600 €
- Entreprise : SecureBank

## 🚨 Dépannage

### Erreurs courantes

- **[ERREUR] Driver MySQL non trouvé** : Le fichier est dans `lib/mysql-connector-java.jar`
- **[ERREUR] Connexion échouée** : Vérifiez que WAMP est démarré
- **[ERREUR] Base non trouvée** : Exécutez `sql/database_setup.sql` dans phpMyAdmin
- **[ERREUR] Application non compilée** : Exécutez `scripts\build.bat`

### Vérifications

1. WAMP démarré (icône verte)
2. Base `bank_facture` existe
3. Fichiers compilés dans `build/`
4. Port MySQL 3306 libre

## 📁 Avantages de cette structure

- ✅ **Organisation claire** : Chaque type de fichier dans son dossier
- ✅ **Sources séparées** : Code source dans `src/`
- ✅ **Build propre** : Fichiers compilés dans `build/`
- ✅ **Scripts centralisés** : Tous les scripts dans `scripts/`
- ✅ **Documentation organisée** : Guides dans `docs/`
- ✅ **Maintenance facile** : Structure standard et professionnelle

## Code "humain"

Le code reste simple et lisible avec :

- Commentaires explicatifs en français
- Noms de variables et méthodes parlants
- Gestion d'erreurs avec messages clairs
- Architecture propre avec pattern Singleton
- Séparation des responsabilités (connexion, auth, prestations)

file to add
openpdf
consultation
mainwinwos
bilan
