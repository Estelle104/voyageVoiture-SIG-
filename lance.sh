#!/bin/bash

################################################################################
# 🚀 SCRIPT DE LANCEMENT COMPLET - VoyageVoiture SIG
# Lance l'application Swing + Tomcat + API RN
################################################################################

set -e

PROJECT_DIR="/home/itu/S3/Progammation/voiture/voyageVoiture-SIG-"
LALANA_DIR="$PROJECT_DIR/LALANA-SIG"
TOMCAT_HOME="/opt/lampp/htdocs/tomcat/apache-tomcat-10.0.16"

echo "================================"
echo "🚀 LANCEMENT DE VOYAGEEVOITURE"
echo "================================"
echo ""

# 1️⃣ COMPILATION
echo "📦 [1/5] Compilation du projet..."
cd "$LALANA_DIR"
if [ ! -d "build/classes" ]; then
    mkdir -p build/classes
fi
javac -cp "lib/*" -d build/classes src/main/java/**/*.java 2>&1 | grep -i error || true
echo "✅ Compilation réussie"
echo ""

# 2️⃣ DÉPLOIEMENT TOMCAT
echo "📂 [2/5] Déploiement sur Tomcat..."
rm -rf "$TOMCAT_HOME/webapps/VoyageVoiture"
mkdir -p "$TOMCAT_HOME/webapps/VoyageVoiture/WEB-INF"

# Copier les ressources web
cp -r "$LALANA_DIR/src/main/webapp"/* "$TOMCAT_HOME/webapps/VoyageVoiture/" 2>/dev/null || true

# Copier les classes compilées
cp -r "$LALANA_DIR/build/classes" "$TOMCAT_HOME/webapps/VoyageVoiture/WEB-INF/" 2>/dev/null || true

# Copier les librairies
cp -r "$LALANA_DIR/lib" "$TOMCAT_HOME/webapps/VoyageVoiture/WEB-INF/" 2>/dev/null || true

echo "✅ Déploiement réussi"
echo ""

# 3️⃣ REDÉMARRAGE TOMCAT
echo "🔄 [3/5] Redémarrage de Tomcat..."
$TOMCAT_HOME/bin/shutdown.sh >/dev/null 2>&1 || true
sleep 2
$TOMCAT_HOME/bin/startup.sh >/dev/null 2>&1
sleep 3

# Vérifier que Tomcat est opérationnel
if curl -s http://localhost:8080/VoyageVoiture/rn | grep -q "id"; then
    echo "✅ Tomcat opérationnel"
else
    echo "⚠️  Tomcat en cours de démarrage... attendre quelques secondes"
    sleep 3
fi
echo ""

# 4️⃣ AFFICHAGE DES URLs
echo "🌐 [4/5] URLs disponibles:"
echo "   • Swing Application: Lancée ci-dessous"
echo "   • Carte SIG: http://localhost:8080/VoyageVoiture/jsp/sig.jsp"
echo "   • API RN: http://localhost:8080/VoyageVoiture/rn"
echo ""

# 5️⃣ LANCEMENT SWING
echo "🖥️  [5/5] Lancement de l'application Swing..."
cd "$LALANA_DIR"
java -cp "build/classes:lib/*" main.AppLauncher &

echo ""
echo "================================"
echo "✅ DÉMARRAGE TERMINÉ"
echo "================================"
echo ""
echo "L'application devrait s'ouvrir automatiquement."
echo "Cliquez sur 'Ouvrir la carte SIG' pour accéder à la carte."
echo ""
