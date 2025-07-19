# Interface Graphique - Gestion des Factures

## 🎯 Présentation

Une interface graphique moderne en Java Swing pour remplacer l'application console existante. Cette GUI préserve toutes les fonctionnalités du terminal tout en offrant une expérience utilisateur améliorée.

## 📁 Structure du Projet

```
Bank_facture/
├── src/
│   ├── Main.java                    # ← Version console (préservée)
│   ├── MainGUI.java                 # ← Nouveau point d'entrée GUI
│   ├── gui/                         # ← Nouveau package pour l'interface
│   │   ├── MainWindow.java          # Fenêtre principale
│   │   ├── FormationFormDialog.java # Formulaire formations
│   │   └── ConsultationFormDialog.java # Formulaire consultations
│   ├── Prestation.java             # Classes métier existantes
│   ├── Formation.java
│   ├── Consultation.java
│   ├── PrestationManager.java
│   ├── AuthManager.java
│   └── DatabaseManager.java
├── scripts/
│   ├── build.bat                    # Script console existant
│   └── launch_gui.bat               # ← Nouveau script GUI
└── lib/
    └── mysql-connector-java.jar
```

## 🚀 Lancement

### Option 1 : Script automatique (Recommandé)

```bash
# Double-cliquez sur le fichier ou exécutez :
scripts/launch_gui.bat
```

### Option 2 : Compilation manuelle

```bash
# Compilation
javac -cp "lib/*;src" src/*.java src/gui/*.java

# Lancement
java -cp "lib/*;src" src.MainGUI
```

### Option 3 : Version console (préservée)

```bash
scripts/build.bat
java -cp "lib/*;src" src.Main
```

## ✨ Fonctionnalités

### 🔐 Authentification

- Interface de connexion moderne
- Validation des identifiants en base de données
- Identifiants de démonstration affichés

### 📝 Gestion des Prestations

- **Formations** : Saisie avec horaires, calcul automatique de la durée
- **Consultations** : Saisie avec TJM (Taux Journalier Moyen)
- Formulaires avec validation des données
- Aperçu avant confirmation

### 📊 Visualisation

- Liste des prestations en tableau
- Calcul automatique du total
- Interface claire et organisée

### 🎨 Design Moderne

- Thème couleurs professionnel
- Boutons avec effets hover
- Messages de statut temporaires
- Navigation intuitive avec boutons retour

## 🔧 Configuration

### Prérequis

- Java 8 ou supérieur
- WAMP/XAMPP avec MySQL démarré
- Base de données `bank_facture` configurée

### Identifiants de Démonstration

- **Login** : `entrepreneur`
- **Mot de passe** : `facture2024`

## 🏗️ Architecture

### Séparation des Responsabilités

- **`src/gui/`** : Interface graphique uniquement
- **`src/`** : Logique métier préservée (inchangée)
- **Scripts séparés** : Console et GUI indépendants

### Avantages de cette Structure

- ✅ **Arborescence propre** : GUI isolée dans son package
- ✅ **Compatibilité** : Version console préservée
- ✅ **Réutilisation** : Logique métier partagée
- ✅ **Maintenabilité** : Code organisé et modulaire

## 🔄 Évolutions Possibles

### Fonctionnalités à Ajouter

- [ ] Statistiques graphiques
- [ ] Export PDF des factures
- [ ] Recherche et filtrage
- [ ] Modification/suppression des prestations
- [ ] Sauvegarde automatique

### Améliorations Techniques

- [ ] JavaFX pour interface plus moderne
- [ ] Configuration par fichier de propriétés
- [ ] Internationalisation (i18n)
- [ ] Tests unitaires pour l'interface

## 📞 Support

En cas de problème :

1. Vérifiez que WAMP est démarré
2. Vérifiez la base de données `bank_facture`
3. Utilisez la version console comme fallback
4. Consultez les logs d'erreur dans la console

---

**Note** : Cette interface graphique coexiste parfaitement avec votre version console existante. Aucune fonctionnalité n'a été supprimée ou modifiée dans le code métier original.
