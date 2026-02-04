# VoyageVoiture SIG - Guide d'utilisation avec vraies données

## 🚀 Lancement rapide avec vraies données PostgreSQL

### Prérequis
- Tomcat installé et TOMCAT_HOME défini
- PostgreSQL avec la table `rn` contenant les vraies données
- Navigateur web

### Lancement en une commande
```bash
bash start-with-carte.sh
```

Cela fera:
1. ✅ Compiler le projet
2. ✅ Déployer sur Tomcat
3. ✅ Démarrer Tomcat (si non lancé)
4. ✅ Ouvrir la carte automatiquement dans le navigateur

### Résultat attendu
L'application s'ouvre à: **http://localhost:8080/VoyageVoiture/jsp/sig.jsp**

Et affiche:
- ✅ Liste des Routes Nationales chargées depuis PostgreSQL
- ✅ Carte interactive Leaflet
- ✅ Recherche/filtre des routes
- ✅ Routes sélectionnées en bleu roi

## 📊 Fonctionnement des données

### Chargement des données
L'application utilise le **RNServlet** pour charger les données:

```
sig.jsp → fetch('../rn') → RNServlet → ConnexionPSQL → PostgreSQL (table rn)
```

**Flux:**
1. Le JSP fait une requête AJAX vers `/rn`
2. Le RNServlet récupère les données de PostgreSQL
3. Conversion en JSON avec ST_AsGeoJSON()
4. Affichage sur la carte Leaflet

### Fallback automatique
Si le serveur n'est pas disponible:
- Les données de **test** s'affichent automatiquement
- ⚠️ Message "Mode TEST" affiché dans le panneau gauche

## 🔧 Étapes détaillées

### 1. Compilation
```bash
bash compile.sh
```

### 2. Déploiement sur Tomcat
```bash
export TOMCAT_HOME=/chemin/vers/tomcat  # Si pas déjà défini
bash quick-deploy.sh
```

### 3. Démarrage de Tomcat (si pas déjà lancé)
```bash
$TOMCAT_HOME/bin/startup.sh
```

### 4. Accès à l'application
- **URL** : http://localhost:8080/VoyageVoiture/jsp/sig.jsp
- **RN API** : http://localhost:8080/VoyageVoiture/rn (JSON)
- **Swagger/Javadoc** : http://localhost:8080/VoyageVoiture/

## 🗺️ Utilisation de la carte

### Panneau gauche - Routes Nationales
- **Recherche** : Tapez le nom d'une route pour filtrer
- **Liste** : Affiche toutes les routes disponibles
- **Clic** : Sélectionne une route et la met en surbrillance

### Carte interactive
- **Zoom** : Scroll de la souris ou boutons +/-
- **Pan** : Drag (déplacement)
- **Popup** : Clic sur une route affiche son nom
- **Couleurs** :
  - 🔵 Bleu clair (#3498db) : Route normale
  - 🔷 Bleu roi (#1c6bad) : Route sélectionnée

## 📁 Structure des données

### Table PostgreSQL `rn`
```sql
SELECT * FROM rn LIMIT 2;
```

Colonnes:
- `id` : Identifiant unique
- `nom` : Nom de la route (ex: "Route Nationale RN1")
- `ref` : Référence (ex: "RN1", "RN2")
- `geom` : Géométrie LineString (SRID 4326)
- `created_at` : Timestamp

### Exemple de réponse API `/rn`
```json
[
  {
    "id": 1,
    "nom": "Route Nationale RN1",
    "geometry": {
      "type": "LineString",
      "coordinates": [[47.5236, -19.8592], [47.3, -19.9], ...]
    }
  },
  {
    "id": 2,
    "nom": "Route Nationale RN2",
    "geometry": { ... }
  }
]
```

## 📝 Logs et débogage

### Logs du serveur
```bash
tail -f $TOMCAT_HOME/logs/catalina.out
```

### Console navigateur (F12)
- Affiche le chargement des données
- Messages d'erreur en détail
- Affichage du nombre de routes chargées

Cherchez les messages:
- ✅ "Données reçues du serveur: X routes"
- ⚠️ "Mode TEST - Données brutes" (si fallback)

## 🐛 Dépannage

### "Aucune route affichée"
1. Vérifiez que PostgreSQL est lancé
2. Vérifiez que la table `rn` contient des données
3. Consultez la console du navigateur (F12)

### "Tomcat ne démarre pas"
```bash
# Vérifier les logs
cat $TOMCAT_HOME/logs/catalina.out

# Vérifier le port 8080
lsof -i :8080
```

### "Les données ne se chargent pas"
1. Vérifiez l'URL: http://localhost:8080/VoyageVoiture/rn
2. Assurez-vous que le RNServlet est compilé et déployé
3. Vérifiez la connexion PostgreSQL dans les logs Tomcat

### "Tomcat_HOME non trouvé"
```bash
# Définir la variable
export TOMCAT_HOME=/opt/tomcat  # ou votre chemin

# Ou ajouter à ~/.bashrc
echo 'export TOMCAT_HOME=/opt/tomcat' >> ~/.bashrc
source ~/.bashrc
```

## 📖 Plus d'informations

Voir [README_SIG.md](README_SIG.md) pour plus de détails sur:
- Architecture générale
- Configuration du déploiement
- Intégration avec l'interface Swing
