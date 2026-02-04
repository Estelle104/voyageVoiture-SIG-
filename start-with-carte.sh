#!/bin/bash

# Script de déploiement et lancement complet
# Compile, déploie sur Tomcat, démarre Tomcat et ouvre la carte dans le navigateur

TOMCAT_HOME="${TOMCAT_HOME:-/opt/tomcat}"

echo "🚀 Déploiement complet VoyageVoiture"
echo "========================================="
echo ""

# Compiler et déployer
bash quick-deploy.sh
if [ $? -ne 0 ]; then
    echo "❌ Erreur lors du déploiement"
    exit 1
fi

echo ""
echo "🔄 Vérification de Tomcat..."

# Vérifier si Tomcat est déjà lancé
if curl -s http://localhost:8080 > /dev/null 2>&1; then
    echo "✅ Tomcat est déjà lancé"
else
    echo "📌 Démarrage de Tomcat..."
    
    # Attendre un peu pour que l'app se déploie
    sleep 2
    
    # Attendre que Tomcat soit prêt
    echo "⏳ Attente du démarrage de Tomcat..."
    for i in {1..30}; do
        if curl -s http://localhost:8080 > /dev/null 2>&1; then
            echo "✅ Tomcat démarré!"
            break
        fi
        echo -n "."
        sleep 1
    done
fi

echo ""
echo "🌐 Ouverture de la carte SIG..."

# Ouvrir la carte dans le navigateur
xdg-open "http://localhost:8080/VoyageVoiture/jsp/sig.jsp" 2>/dev/null || \
firefox "http://localhost:8080/VoyageVoiture/jsp/sig.jsp" 2>/dev/null || \
echo "📍 Ouvrez manuellement: http://localhost:8080/VoyageVoiture/jsp/sig.jsp"

echo ""
echo "✅ Prêt!"
echo "💡 Les données RN seront chargées depuis PostgreSQL"
