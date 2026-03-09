@echo off
SET PGUSER=postgres
SET PGDATABASE=notecorrection
SET PGHOST=localhost
SET PGPORT=5432

echo Fafana %PGDATABASE%...

:: Utilisation de TRUNCATE avec CASCADE pour vider toutes les tables en ignorant les contraintes d'ordre
psql -c "TRUNCATE TABLE note_finale, Note, Parametre, Correcteur, Resolution, Candidat, Operateur, Matiere RESTART IDENTITY CASCADE;"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Mety
) else (
    echo.
    echo Misy erreur
)

pause