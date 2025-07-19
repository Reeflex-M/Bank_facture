#!/bin/bash

echo "============================================"
echo "     TEST des FACTURES GRAPHIQUES"
echo "============================================"

# Vérifier si Java est installé
if ! java -version &> /dev/null; then
    echo "[ERREUR] Java n'est pas installé ou pas dans le PATH"
    echo "Veuillez installer Java et relancer le script"
    read -p "Appuyez sur Entrée pour continuer..."
    exit 1
fi

echo "[OK] Java détecté"

# Créer le dossier de compilation
mkdir -p bin

# Compiler tous les fichiers Java avec les nouvelles classes
echo "[INFO] Compilation des fichiers Java avec fonctionnalités graphiques..."

javac -cp "lib/*" -d bin \
    src/AuthManager.java \
    src/Consultation.java \
    src/DatabaseManager.java \
    src/Formation.java \
    src/Main.java \
    src/MainGUI.java \
    src/Prestation.java \
    src/PrestationManager.java \
    src/FactureManager.java \
    src/gui/ConsultationFormDialog.java \
    src/gui/FormationFormDialog.java \
    src/gui/MainWindow.java \
    src/gui/FactureFormDialog.java \
    src/gui/FactureViewerDialog.java

if [ $? -ne 0 ]; then
    echo "[ERREUR] Erreur de compilation"
    echo "Vérifiez que toutes les classes sont présentes :"
    echo "- FactureManager.java"
    echo "- FactureViewerDialog.java"
    echo "- FactureFormDialog.java (modifiée)"
    echo "- MainWindow.java (modifiée)"
    read -p "Appuyez sur Entrée pour continuer..."
    exit 1
fi

echo "[OK] Compilation réussie !"

echo
echo "============================================"
echo "NOUVELLES FONCTIONNALITES DISPONIBLES :"
echo "============================================"
echo "1. Génération de factures mensuelles HTML"
echo "2. APERCU GRAPHIQUE intégré dans l'application"
echo "3. Interface de prévisualisation des factures"
echo "4. Impression directe depuis l'application"
echo "5. Export HTML depuis l'aperçu graphique"
echo
echo "Instructions d'utilisation :"
echo "1. Connectez-vous avec : entrepreneur / facture2024"
echo "2. Cliquez sur \"Générer Factures\""
echo "3. Sélectionnez la période"
echo "4. Cliquez sur \"Générer les Factures\""
echo "5. Cliquez sur \"Aperçu Graphique\" pour voir les factures"
echo
echo "============================================"

echo "[INFO] Lancement de l'application..."
echo

# Lancer l'application
java -cp "bin:lib/*" src.MainGUI

echo
echo "============================================"
echo "Application fermée"
echo "============================================"
read -p "Appuyez sur Entrée pour continuer..." 