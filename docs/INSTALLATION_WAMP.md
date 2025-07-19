# Guide d'installation WAMP et création de la base de données

## 🔧 Installation de WAMP

### 1. Téléchargement et installation

1. Téléchargez WAMP depuis : https://www.wampserver.com/
2. Exécutez l'installateur en tant qu'administrateur
3. Suivez les étapes d'installation (installation par défaut recommandée)
4. Redémarrez votre ordinateur si demandé

### 2. Démarrage de WAMP

1. Lancez WAMP depuis le menu Démarrer
2. L'icône WAMP apparaît dans la barre des tâches (zone de notification)
3. Attendez que l'icône devienne **VERTE** (services démarrés)

### Codes couleur de l'icône WAMP :

- 🔴 **Rouge** : Services arrêtés
- 🟠 **Orange** : Services en cours de démarrage
- 🟢 **Vert** : Tous les services opérationnels ✅

## 🗄️ Création de la base de données

### 1. Accès à phpMyAdmin

1. Clic gauche sur l'icône WAMP (verte)
2. Sélectionnez "phpMyAdmin"
3. Ou ouvrez votre navigateur : `http://localhost/phpmyadmin`

### 2. Connexion à phpMyAdmin

- **Serveur** : localhost
- **Utilisateur** : root
- **Mot de passe** : (laisser vide par défaut)

### 3. Création de la base avec le script SQL

#### Méthode 1 : Import du fichier SQL

1. Dans phpMyAdmin, cliquez sur l'onglet "**Importer**"
2. Cliquez sur "**Choisir un fichier**"
3. Sélectionnez le fichier `database_setup.sql`
4. Cliquez sur "**Exécuter**"
5. Vérifiez que le message de succès s'affiche

#### Méthode 2 : Copier-coller le script

1. Dans phpMyAdmin, cliquez sur l'onglet "**SQL**"
2. Copiez tout le contenu du fichier `database_setup.sql`
3. Collez-le dans la zone de texte
4. Cliquez sur "**Exécuter**"

### 4. Vérification de la création

Après exécution du script, vous devriez voir :

- Une nouvelle base de données `bank_facture` dans la liste de gauche
- Table `utilisateurs` avec 1 enregistrement
- Table `prestations` avec 2 enregistrements de test

### 5. Structure créée

```sql
Base: bank_facture
├── utilisateurs
│   ├── id (clé primaire)
│   ├── login (unique)
│   ├── mot_de_passe
│   ├── nom
│   ├── email
│   └── date_creation
└── prestations
    ├── id (clé primaire)
    ├── type_prestation (Formation/Consultation)
    ├── date_prestation
    ├── entreprise
    ├── montant
    ├── heure_debut (formations)
    ├── heure_fin (formations)
    ├── classe (formations)
    ├── titre (formations)
    ├── tarif_horaire (formations)
    ├── description (consultations)
    ├── tjm (consultations)
    └── date_creation
```

## 🔧 Installation du driver MySQL

### 1. Téléchargement

1. Allez sur : https://dev.mysql.com/downloads/connector/j/
2. Téléchargez "**Platform Independent**" (fichier ZIP)
3. Extrayez le fichier `mysql-connector-java-X.X.XX.jar`

### 2. Installation

1. Copiez le fichier `.jar` dans le dossier de votre application Java
2. Renommez-le en `mysql-connector-java.jar` (optionnel, pour simplifier)

## ✅ Test de la configuration

### 1. Vérification WAMP

- Icône WAMP verte ✅
- `http://localhost` affiche la page WAMP ✅
- `http://localhost/phpmyadmin` accessible ✅

### 2. Vérification base de données

```sql
-- Dans phpMyAdmin, onglet SQL :
USE bank_facture;
SELECT * FROM utilisateurs;
SELECT * FROM prestations;
```

### 3. Test de l'application Java

```cmd
compile_run_with_db.bat
```

## 🚨 Dépannage

### Problèmes courants

#### WAMP ne démarre pas (icône rouge)

1. Vérifiez les ports (80, 443, 3306)
2. Fermez Skype ou autres applications utilisant le port 80
3. Redémarrez WAMP en tant qu'administrateur

#### Port 80 occupé

1. Clic droit sur icône WAMP
2. "Outils" → "Utiliser un port autre que 80"
3. Choisissez le port 8080
4. Accès : `http://localhost:8080/phpmyadmin`

#### phpMyAdmin inaccessible

1. Vérifiez que Apache et MySQL sont démarrés
2. Clic gauche sur WAMP → Services → Redémarrer tous les services

#### Erreur "mysql-connector-java.jar" non trouvé

1. Téléchargez le driver depuis le site officiel MySQL
2. Placez le fichier dans le dossier de l'application
3. Vérifiez le nom du fichier (sans espaces)

#### Erreur de connexion Java

1. Vérifiez que WAMP est démarré (icône verte)
2. Vérifiez que la base `bank_facture` existe
3. Testez la connexion : `http://localhost/phpmyadmin`

## 📞 Support

En cas de problème persistant :

1. Vérifiez les logs WAMP (clic droit → Logs)
2. Redémarrez complètement WAMP
3. Vérifiez la configuration Windows (pare-feu, antivirus)
4. Consultez la documentation officielle WAMP
