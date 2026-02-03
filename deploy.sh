#!/bin/bash

# Script de déploiement de VoyageVoiture sur Tomcat
# Copie l'application dans le répertoire webapps de Tomcat

TOMCAT_HOME="${TOMCAT_HOME:-.}"
WEBAPP_DIR="$TOMCAT_HOME/webapps/VoyageVoiture"

echo "📦 Déploiement de VoyageVoiture sur Tomcat"
echo "TOMCAT_HOME: $TOMCAT_HOME"
echo ""

# Vérifier que Tomcat existe
if [ ! -d "$TOMCAT_HOME/webapps" ]; then
    echo "❌ Erreur: Le dossier webapps de Tomcat n'existe pas"
    echo "Définissez TOMCAT_HOME:"
    echo "  export TOMCAT_HOME=/chemin/vers/tomcat"
    echo "  bash deploy.sh"
    exit 1
fi

echo "🔨 Compilation du projet..."
cd LALANA-SIG || exit 1
mkdir -p build/classes
find src/main/java -name "*.java" -type f | xargs javac -d build/classes -cp "lib/*:." 2>&1

if [ $? -ne 0 ]; then
    echo "❌ Erreur de compilation"
    exit 1
fi

echo "✅ Compilation réussie!"
echo ""

# Créer la structure WAR
echo "📂 Création de la structure WAR..."
rm -rf "$WEBAPP_DIR" 2>/dev/null
mkdir -p "$WEBAPP_DIR/WEB-INF/classes"
mkdir -p "$WEBAPP_DIR/WEB-INF/lib"
mkdir -p "$WEBAPP_DIR/jsp"
mkdir -p "$WEBAPP_DIR/css"
mkdir -p "$WEBAPP_DIR/js"

# Copier les classes compilées
cp -r build/classes/* "$WEBAPP_DIR/WEB-INF/classes/" 2>/dev/null

# Copier les librairies
cp lib/* "$WEBAPP_DIR/WEB-INF/lib/" 2>/dev/null

# Copier le web.xml
cp src/main/webapp/WEB-INF/web.xml "$WEBAPP_DIR/WEB-INF/" 2>/dev/null

# Copier les JSP
cp src/main/webapp/jsp/*.jsp "$WEBAPP_DIR/jsp/" 2>/dev/null

# Copier les ressources statiques
cp src/main/webapp/css/*.css "$WEBAPP_DIR/css/" 2>/dev/null
cp src/main/webapp/js/*.js "$WEBAPP_DIR/js/" 2>/dev/null

echo "✅ Déploiement réussi!"
echo ""
echo "📍 Accédez à l'application:"
echo "   http://localhost:8080/VoyageVoiture/jsp/sig.jsp"
echo ""
echo "⚠️  Assurez-vous que Tomcat est démarré!"
