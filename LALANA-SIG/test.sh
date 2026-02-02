#!/bin/bash
cd "$(dirname "$0")"

echo "🔨 Compilation vers bin/..."

# Créer bin/ s'il n'existe pas
mkdir -p bin

# Compiler les fichiers SIG vers bin/
javac -d bin -cp ".:lib/*" \
    sig/CoordinatesUtil.java \
    sig/RoadParser.java \
    sig/MapManager.java \
    sig/SIGServer.java \
    sig/TestSIG.java

if [ $? -ne 0 ]; then
    echo "❌ Erreur compilation sig/"
    exit 1
fi

echo "✅ Compilation SIG réussie"

# Générer GeoJSON avec vraies coordonnées Madagascar
echo "📍 Génération GeoJSON avec vraies routes..."
java -cp "bin:lib/*" sig.RoadParser

if [ $? -ne 0 ]; then
    echo "⚠️  Attention: Génération GeoJSON échouée"
fi

echo "✅ Compilation complète"
echo ""
echo "🚀 LANCER LE SERVEUR SIG:"
echo "   java -cp \"bin:lib/*\" sig.TestSIG"
echo ""
echo "📍 Puis ouvrir: http://localhost:8888"
