@echo off
:: Configuration des variables d'environnement
SET PGUSER=postgres
SET PGDATABASE=forage
SET PGHOST=localhost
SET PGPORT=5432

echo Tentative de réinitialisation de la base de données : %PGDATABASE%...

:: Utilisation de TRUNCATE avec CASCADE et RESTART IDENTITY
:: L'ordre importe peu avec CASCADE, mais voici toutes tes tables :
psql -c "TRUNCATE TABLE DetailsDevis, Devis, DemandeStatus, Status, Demande, TypeDevis, Client RESTART IDENTITY CASCADE;"

if %ERRORLEVEL% EQU 0 (
    echo.
    echo Operation reussie : Les tables sont vides et les compteurs sequences sont a zero.
) else (
    echo.
    echo Une erreur est survenue lors de la suppression des donnees.
)

pause