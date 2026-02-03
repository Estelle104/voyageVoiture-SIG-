#!/bin/bash

# Compilation du projet voyageVoiture-SIG (LALANA-SIG)
# Compile avec javac directement

echo "🔨 Compilation du projet LALANA-SIG..."

cd LALANA-SIG || exit 1

# Créer le répertoire build/classes s'il n'existe pas
mkdir -p build/classes

# Compiler tous les fichiers Java en une seule passe
echo "  └─ Compilation de tous les fichiers Java..."
find src/main/java -name "*.java" -type f | xargs javac -d build/classes -cp "lib/*:." 2>&1

if [ $? -eq 0 ]; then
    echo "✅ Compilation réussie!"
    echo ""
    echo "� Pour lancer l'application:"
    echo "   java -cp \"build/classes:lib/*\" main.AppLauncher"
else
    echo "❌ Erreur de compilation"
    exit 1
fi