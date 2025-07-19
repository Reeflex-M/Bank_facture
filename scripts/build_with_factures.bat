@echo off
echo ============================================
echo     COMPILATION avec fonctionnalite FACTURES
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
echo [INFO] Compilation des fichiers Java...
javac -cp "lib/*" -d bin src/*.java src/gui/*.java

if %errorlevel% neq 0 (
    echo [ERREUR] Erreur de compilation
    pause
    exit /b 1
)

echo [OK] Compilation reussie !

echo.
echo ============================================
echo Les nouvelles fonctionnalites incluses :
echo - Generation de factures mensuelles HTML
echo - Interface graphique pour selectionner la periode
echo - Ouverture automatique des factures generees
echo ============================================
echo.
echo Pour lancer l'application : scripts\launch_gui.bat
echo Les factures seront generees dans le dossier 'factures/'
echo.
pause 