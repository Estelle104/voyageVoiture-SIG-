#!/bin/bash

# Arrêt du script en cas d'erreur
set -e

# Couleurs
GREEN="\e[32m"
BLUE="\e[34m"
RED="\e[31m"
RESET="\e[0m"

echo -e "${BLUE}===== Déploiement de l'application =====${RESET}"

# Variables
APP_NAME="VoyageVoiture"
SRC_DIR="src/main/java"
WEB_DIR="src/main/webapp"
BUILD_DIR="build"
LIB_DIR="lib"

TOMCAT_WEBAPPS="/home/itu/S2/programmation/a_lecon/tomcat/apache-tomcat-10.0.16/webapps"
TOMCAT_BIN="/home/itu/S2/programmation/a_lecon/tomcat/apache-tomcat-10.0.16/bin"
CATALINA_LOG="/home/itu/S2/programmation/a_lecon/tomcat/apache-tomcat-10.0.16/logs/catalina.out"

SERVLET_API_JAR="$LIB_DIR/servlet-api.jar"

echo -e "${GREEN}1) Nettoyage et préparation...${RESET}"
rm -rf "$BUILD_DIR"
mkdir -p "$BUILD_DIR/WEB-INF/classes"

echo -e "${GREEN}2) Compilation Java...${RESET}"
find "$SRC_DIR" -name "*.java" > sources.txt
javac -cp "$SERVLET_API_JAR" -d "$BUILD_DIR/WEB-INF/classes" @sources.txt
rm sources.txt

echo -e "${GREEN}3) Copie des fichiers web...${RESET}"
cp -r "$WEB_DIR/"* "$BUILD_DIR/"

echo -e "${GREEN}4) Génération du fichier WAR...${RESET}"
cd "$BUILD_DIR"
jar -cvf "$APP_NAME.war" *
cd ..

echo -e "${GREEN}5) Déploiement dans Tomcat...${RESET}"
cp -f "$BUILD_DIR/$APP_NAME.war" "$TOMCAT_WEBAPPS/"

echo -e "${GREEN}6) Redémarrage de Tomcat...${RESET}"
"$TOMCAT_BIN/shutdown.sh" || true
sleep 2
"$TOMCAT_BIN/startup.sh"
sleep 2

echo -e "${BLUE}===== Déploiement terminé =====${RESET}"
echo ""
echo -e "${GREEN}Vous allez maintenant voir les logs en temps réel :${RESET}"
echo ""

# Log en temps réel
tail -f "$CATALINA_LOG"
