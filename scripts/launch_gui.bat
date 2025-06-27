@echo off
echo ================================================
echo    Compilation et lancement de l'interface GUI
echo ================================================

cd /d "%~dp0.."

echo [1/3] Compilation des classes Java...
if not exist "build" mkdir build
javac -cp "lib\mysql-connector-java.jar;src" -d build src\MainGUI.java src\gui\*.java src\AuthManager.java src\Consultation.java src\DatabaseManager.java src\Formation.java src\Prestation.java src\PrestationManager.java

if %ERRORLEVEL% neq 0 (
    echo ERREUR: Échec de la compilation !
    pause
    exit /b 1
)

echo [2/3] Compilation réussie !

echo [3/3] Lancement de l'interface graphique...
java -cp "lib\mysql-connector-java.jar;build" src.MainGUI

echo.
echo Interface fermée.
pause 