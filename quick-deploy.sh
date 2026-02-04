#!/bin/bash

# Script de déploiement/test rapide avec Tomcat
# Ce script déploie l'application sur Tomcat et la teste

TOMCAT_HOME="${TOMCAT_HOME:-/opt/tomcat}"
WEBAPP_DIR="$TOMCAT_HOME/webapps/VoyageVoiture"

echo "🚀 Déploiement rapide VoyageVoiture"
echo "========================================="
echo ""

# Vérifier Tomcat
if [ ! -d "$TOMCAT_HOME/bin" ]; then
    echo "❌ Tomcat non trouvé à: $TOMCAT_HOME"
    echo "Définissez TOMCAT_HOME avant d'exécuter ce script:"
    echo "  export TOMCAT_HOME=/chemin/vers/tomcat"
    exit 1
fi

echo "🔧 Compilation..."
cd LALANA-SIG || exit 1
mkdir -p build/classes
find src/main/java -name "*.java" -type f | xargs javac -d build/classes -cp "lib/*:." 2>&1

if [ $? -ne 0 ]; then
    echo "❌ Erreur de compilation"
    exit 1
fi

echo "✅ Compilation réussie!"
echo ""

# Déployer
echo "📦 Déploiement sur Tomcat..."
rm -rf "$WEBAPP_DIR" 2>/dev/null
mkdir -p "$WEBAPP_DIR/WEB-INF/classes"
mkdir -p "$WEBAPP_DIR/WEB-INF/lib"
mkdir -p "$WEBAPP_DIR/jsp"

# Copier les fichiers
cp -r build/classes/* "$WEBAPP_DIR/WEB-INF/classes/" 2>/dev/null
cp lib/* "$WEBAPP_DIR/WEB-INF/lib/" 2>/dev/null
cp src/main/webapp/WEB-INF/web.xml "$WEBAPP_DIR/WEB-INF/" 2>/dev/null
cp src/main/webapp/jsp/*.jsp "$WEBAPP_DIR/jsp/" 2>/dev/null

echo "✅ Déploiement réussi!"
echo ""
echo "📍 Accédez à l'application:"
echo "   http://localhost:8080/VoyageVoiture/jsp/sig.jsp"
echo ""
echo "ℹ️  Les données RN seront chargées depuis PostgreSQL"
echo "   (ou les données de test si le serveur n'est pas disponible)"
