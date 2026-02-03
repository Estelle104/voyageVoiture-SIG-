# VoyageVoiture SIG - Guide d'utilisation

## 🚀 Démarrage rapide

### Option 1: Avec Tomcat (recommandé)

#### 1. Compiler le projet
```bash
bash compile.sh
```

#### 2. Déployer sur Tomcat
```bash
export TOMCAT_HOME=/chemin/vers/tomcat
bash deploy.sh
```

#### 3. Lancer Tomcat et l'application
```bash
# Démarrer Tomcat
$TOMCAT_HOME/bin/startup.sh

# Dans un autre terminal
bash run.sh
```

Accédez à l'application via: **`http://localhost:8080/VoyageVoiture/jsp/sig.jsp`**

### Option 2: Sans Tomcat (fichier local)

#### 1. Compiler le projet
```bash
bash compile.sh
```

#### 2. Lancer l'application
```bash
bash run.sh
```

Cliquez sur "Afficher la carte SIG" pour ouvrir le JSP en local dans votre navigateur.

## 📋 Fonctionnalités

### Interface Swing Principal
Quand vous lancez l'application, une interface Swing s'ouvre avec:
- Boutons pour ajouter des Lavaka, Pauses, Simba, ConfPrix, Pluviométrie
- Sélection des voyages, chemins et voitures
- Simulation de voyage

### 🗺️ Carte SIG

Cliquez sur le bouton **"Afficher la carte SIG"** pour ouvrir la carte interactive.

#### Fonctionnalités de la carte:
- **Liste des Routes Nationales** à gauche
- **Recherche** : Filtrez les routes par nom
- **Sélection** : Cliquez sur une route dans la liste pour la mettre en surbrillance en **bleu roi**
- **Carte interactive** à droite avec Leaflet

#### Données
- **Mode TEST** : La carte utilise des données de test (5 routes nationales prédéfinies)
- Les routes sont affichées en bleu clair par défaut
- La route sélectionnée est affichée en bleu roi (#1c6bad)

## 🌐 Déploiement sur Tomcat

### Prérequis
- Tomcat 8.0+ installé
- Variable `TOMCAT_HOME` pointant vers le répertoire Tomcat

### Structure du déploiement
```
$TOMCAT_HOME/webapps/VoyageVoiture/
├── WEB-INF/
│   ├── classes/          # Classes Java compilées
│   ├── lib/              # Dépendances JAR
│   └── web.xml           # Configuration web
├── jsp/                  # Fichiers JSP
├── css/                  # Feuilles de style
└── js/                   # Fichiers JavaScript
```

### Accès à l'application déployée
- **Carte SIG** : `http://localhost:8080/VoyageVoiture/jsp/sig.jsp`
- **RN JSON** : `http://localhost:8080/VoyageVoiture/jsp/rn` (avec servlet activée)

## 📂 Structure du projet

```
voyageVoiture-SIG-/
├── compile.sh                          # Script de compilation
├── run.sh                              # Script de lancement
├── deploy.sh                           # Script de déploiement Tomcat
├── README_SIG.md                       # Ce fichier
├── LALANA-SIG/
│   ├── build/
│   │   └── classes/                    # Classes compilées
│   ├── lib/                            # Dépendances JAR
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   ├── affichage/          # Interface Swing
│   │       │   ├── dao/                # Accès aux données
│   │       │   ├── model/              # Modèles de données
│   │       │   ├── service/            # Services métier
│   │       │   ├── servlet/            # Servlets (optionnel)
│   │       │   ├── sig/                # Utilitaires SIG
│   │       │   ├── utildb/             # Connexions BD
│   │       │   └── main/
│   │       │       └── AppLauncher.java # Point d'entrée
│   │       └── webapp/
│   │           ├── WEB-INF/
│   │           │   └── web.xml         # Configuration Tomcat
│   │           └── jsp/
│   │               └── sig.jsp         # Carte SIG (HTML/JavaScript/Leaflet)
│   └── pom.xml
```

## 🔄 Flux d'utilisation

### Avec Tomcat:
1. **Lance l'application** → Swing UI
2. **Clique "Afficher la carte SIG"** → Ouvre `http://localhost:8080/VoyageVoiture/jsp/sig.jsp`
3. **Cherche une route** → Utilise le champ recherche
4. **Clique sur une route** → Elle se met en surbrillance en bleu roi
5. **Utilise la carte** → Zoom, pan, popup sur les routes

### Sans Tomcat:
1. **Lance l'application** → Swing UI
2. **Clique "Afficher la carte SIG"** → Ouvre le JSP en local dans le navigateur
3. Même fonctionnalités que avec Tomcat

## 🎨 Couleurs

- **Routes normales** : #3498db (bleu clair)
- **Route sélectionnée** : #1c6bad (bleu roi)
- **Panneau gauche** : Blanc avec accents bleus

## 📝 Notes

- L'application utilise des **données de test** pour la carte
- L'application bascule **automatiquement** entre Tomcat et fichier local
- Compatible avec Firefox, Chrome, Edge, Safari
- Fonctionne **avec ou sans Tomcat**

## ⚙️ Configuration

### Données de test
Les données de test des routes sont définies dans `sig.jsp`:
```javascript
const donneesBrutes = [
    {
        id: 1,
        nom: "RN1 - Antananarivo to Toliara",
        geometry: { ... }
    },
    // ... autres routes
];
```

### Variables d'environnement
```bash
# Pour le déploiement sur Tomcat
export TOMCAT_HOME=/opt/tomcat
bash deploy.sh
```

## 🐛 Dépannage

**"Le fichier sig.jsp n'a pas pu être trouvé"**
- Vérifie que le fichier existe à : `LALANA-SIG/src/main/webapp/jsp/sig.jsp`

**"La carte ne s'affiche pas"**
- Vérifiez votre connexion internet (Leaflet se charge depuis CDN)
- Les données de test devraient s'afficher même hors ligne

**"Tomcat n'est pas disponible"**
- Assurez-vous que Tomcat est démarré: `$TOMCAT_HOME/bin/startup.sh`
- L'application basculera automatiquement sur le fichier local

**"TOMCAT_HOME non défini"**
```bash
# Définir temporairement
export TOMCAT_HOME=/chemin/vers/tomcat
bash deploy.sh

# Ou ajouter à ~/.bashrc pour la rendre permanente
echo 'export TOMCAT_HOME=/chemin/vers/tomcat' >> ~/.bashrc
```
