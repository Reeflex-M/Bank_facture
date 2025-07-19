# 📄 Fonctionnalité de Génération de Factures

## 🎯 Description

Cette nouvelle fonctionnalité permet de générer automatiquement des factures mensuelles pour chaque client en format HTML, facilement imprimables en PDF.

## ✨ Caractéristiques

- **Génération automatique** : Regroupe toutes les prestations par client et par mois
- **Format HTML professionnel** : Factures avec design moderne et bouton d'impression intégré
- **Interface graphique intuitive** : Sélection simple du mois et de l'année
- **Multiples clients** : Génère une facture séparée pour chaque entreprise ayant des prestations
- **Ouverture automatique** : Les factures peuvent être ouvertes directement dans le navigateur

## 🚀 Utilisation

### 1. Accès à la fonctionnalité

1. Connectez-vous à l'application avec les identifiants :
   - **Login** : `entrepreneur`
   - **Mot de passe** : `facture2024`
2. Dans le menu principal, cliquez sur **"Générer Factures"**

### 2. Génération des factures

1. Sélectionnez le **mois** et l'**année** souhaités
2. Cliquez sur **"Générer les Factures"**
3. L'application va :
   - Récupérer toutes les prestations de la période
   - Grouper par entreprise
   - Créer une facture HTML pour chaque client
   - Afficher les résultats dans la zone de texte

### 3. Consultation des factures

- Les factures sont sauvegardées dans le dossier `factures/`
- Format de nom : `Facture_[Entreprise]_[AAAA_MM].html`
- Chaque facture contient un bouton **"🖨️ Imprimer / Enregistrer en PDF"**

## 📋 Contenu des factures

Chaque facture inclut :

- **En-tête** avec titre "FACTURE"
- **Informations du prestataire** (Micro Entrepreneur)
- **Informations du client** (entreprise et période)
- **Tableau détaillé** des prestations avec :
  - Date de chaque prestation
  - Description (titre/module pour formations, description pour consultations)
  - Type (Formation ou Consultation)
  - Détails tarifaires (heures et tarif horaire pour formations, TJM pour consultations)
  - Montant de chaque prestation
- **Total général** en bas à droite
- **Pied de page** avec remerciements et date de génération

## 🔧 Fonctionnalités techniques

### Classes ajoutées

- **`FactureManager.java`** : Gestion de la génération des factures
- **`FactureFormDialog.java`** : Interface graphique pour la sélection de période

### Méthodes principales

- `genererFacturesMensuelles(int annee, int mois)` : Génère toutes les factures pour une période
- `getPrestationsParClient(int annee, int mois)` : Récupère les prestations groupées par client
- `genererFactureHTML(...)` : Crée le fichier HTML de facture
- `ouvrirFacture(String chemin)` : Ouvre la facture dans le navigateur

## 📁 Structure des fichiers

```
Bank_facture/
├── factures/               # Dossier créé automatiquement
│   ├── Facture_TechCorp_2024_12.html
│   ├── Facture_SecureBank_2024_12.html
│   └── ...
├── src/
│   ├── FactureManager.java # Nouvelle classe
│   └── gui/
│       ├── FactureFormDialog.java # Nouvelle interface
│       └── MainWindow.java        # Modifiée (nouveau bouton)
└── scripts/
    └── build_with_factures.bat   # Script de compilation
```

## 💡 Comment convertir en PDF

### Méthode 1 : Navigateur (Recommandée)

1. Ouvrir la facture HTML dans le navigateur
2. Cliquer sur le bouton **"🖨️ Imprimer / Enregistrer en PDF"**
3. Dans la boîte de dialogue d'impression :
   - Choisir "Enregistrer au format PDF" comme destination
   - Ajuster les paramètres si nécessaire
   - Cliquer sur "Enregistrer"

### Méthode 2 : Raccourci clavier

1. Ouvrir la facture HTML
2. Appuyer sur `Ctrl + P`
3. Sélectionner "Enregistrer au format PDF"

## 🎨 Apparence des factures

- **Design moderne** : Couleurs professionnelles (bleu et gris)
- **Responsive** : Adaptée à l'impression
- **Lisible** : Police Arial, espacement optimisé
- **Structurée** : Tableau avec lignes alternées pour faciliter la lecture
- **Print-friendly** : Marges optimisées pour l'impression

## 🔍 Exemple de sortie

```
=== FACTURES GÉNÉRÉES (2) ===
✓ TechCorp -> factures/Facture_TechCorp_2024_12.html
✓ SecureBank -> factures/Facture_SecureBank_2024_12.html

Tous les fichiers PDF ont été générés dans le dossier 'factures/'
```

## 🛠️ Compilation et lancement

```batch
# Compiler avec la nouvelle fonctionnalité
scripts\build_with_factures.bat

# Lancer l'application
scripts\launch_gui.bat
```

## 📝 Notes importantes

- Les factures HTML sont compatibles avec tous les navigateurs modernes
- Le bouton d'impression disparaît automatiquement lors de l'impression
- Les factures sont générées uniquement s'il y a des prestations pour la période sélectionnée
- Le format HTML permet une personnalisation facile du design si nécessaire
