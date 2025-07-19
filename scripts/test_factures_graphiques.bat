@echo off
echo ============================================
echo     TEST des FACTURES GRAPHIQUES
echo ============================================

REM Vérifier si Java est installé
java -version >nul 2>&1
if %errorlevel% neq 0 (
    echo [ERREUR] Java n'est pas installé ou pas dans le PATH
    echo Veuillez installer Java et relancer le script
    pause
    exit /b 1
)

echo [OK] Java detecte

REM Créer le dossier de compilation
if not exist "bin" mkdir bin

REM Compiler tous les fichiers Java avec les nouvelles classes
echo [INFO] Compilation des fichiers Java avec fonctionnalites graphiques...
javac -cp "lib/*" -d bin src/AuthManager.java src/Consultation.java src/DatabaseManager.java src/Formation.java src/Main.java src/MainGUI.java src/Prestation.java src/PrestationManager.java src/FactureManager.java src/gui/ConsultationFormDialog.java src/gui/FormationFormDialog.java src/gui/MainWindow.java src/gui/FactureFormDialog.java src/gui/FactureViewerDialog.java

if %errorlevel% neq 0 (
    echo [ERREUR] Erreur de compilation
    echo Verifiez que toutes les classes sont presentes :
    echo - FactureManager.java
    echo - FactureViewerDialog.java  
    echo - FactureFormDialog.java (modifiee)
    echo - MainWindow.java (modifiee)
    pause
    exit /b 1
)

echo [OK] Compilation reussie !

echo.
echo ============================================
echo NOUVELLES FONCTIONNALITES DISPONIBLES :
echo ============================================
echo 1. Generation de factures mensuelles HTML
echo 2. APERCU GRAPHIQUE integre dans l'application
echo 3. Interface de previsualisation des factures
echo 4. Impression directe depuis l'application
echo 5. Export HTML depuis l'apercu graphique
echo.
echo Instructions d'utilisation :
echo 1. Connectez-vous avec : entrepreneur / facture2024
echo 2. Cliquez sur "Generer Factures"
echo 3. Selectionnez la periode
echo 4. Cliquez sur "Generer les Factures"
echo 5. Cliquez sur "Apercu Graphique" pour voir les factures
echo.
echo ============================================

echo [INFO] Lancement de l'application...
echo.

REM Lancer l'application
java -cp "bin;lib/*" src.MainGUI

echo.
echo ============================================
echo Application fermee
echo ============================================
pause 