@echo off
echo =====================================================
echo    APPLICATION GESTION FACTURES - COMPILATION
echo =====================================================

REM Aller au répertoire racine du projet
cd /d "%~dp0.."

REM Vérifier si le driver MySQL est présent
if not exist "lib\mysql-connector-java.jar" (
    echo [ERREUR] Driver MySQL non trouvé dans lib\ !
    echo [INFO] Téléchargez mysql-connector-java.jar depuis:
    echo        https://dev.mysql.com/downloads/connector/j/
    echo        et placez-le dans le dossier lib\
    pause
    exit /b 1
)

echo [OK] Driver MySQL trouvé dans lib\

REM Créer le dossier build s'il n'existe pas
echo [INFO] Création du dossier build...
if not exist "build" mkdir build

REM Compilation des sources Java
echo [INFO] Compilation des sources src\ vers build\...
javac -cp "lib\mysql-connector-java.jar;src" -d build src\*.java

if %errorlevel% equ 0 (
    echo [OK] Compilation réussie !
    echo [INFO] Les fichiers .class sont dans build\
    echo.
    echo =====================================================
    echo    LANCEMENT DE L'APPLICATION
    echo =====================================================
    echo [INFO] Assurez-vous que WAMP est démarré et que la base 'bank_facture' existe
    echo [INFO] Script SQL disponible dans sql\database_setup.sql
    echo.
    java -cp "lib\mysql-connector-java.jar;build" Main
    echo.
    echo [INFO] Application terminée
    pause
) else (
    echo [ERREUR] Erreur de compilation !
    echo [INFO] Vérifiez vos fichiers Java dans src\
    pause
) 