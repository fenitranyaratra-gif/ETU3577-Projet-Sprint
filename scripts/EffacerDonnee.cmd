@echo off
SET PGUSER=postgres
SET PGDATABASE=notecorrection
SET PGHOST=localhost
SET PGPORT=5432

echo Purge imminente de la base de donnees %PGDATABASE%...

:: Utilisation de TRUNCATE avec CASCADE pour vider toutes les tables en ignorant les contraintes d'ordre
psql -c "TRUNCATE TABLE note_finale, Note, Parametre, Correcteur, Resolution, Candidat, Operateur, Matiere RESTART IDENTITY CASCADE;"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Operation accomplie avec succes. Les tables sont vacantes.
) else (
    echo.
    echo Une erreur est survenue lors de l'execution du script.
)

pause