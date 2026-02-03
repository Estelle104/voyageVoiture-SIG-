#!/bin/bash

# Script pour lancer l'application VoyageVoiture

echo "🚀 Démarrage de VoyageVoiture..."
echo ""

cd LALANA-SIG || exit 1

# Vérifier que la compilation a été faite
if [ ! -d "build/classes" ]; then
    echo "❌ Les classes compilées n'ont pas été trouvées"
    echo "Veuillez d'abord exécuter: bash ../compile.sh"
    exit 1
fi

# Lancer l'application
java -cp "build/classes:lib/*" main.AppLauncher
