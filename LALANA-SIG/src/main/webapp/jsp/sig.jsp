<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>🗺️ Carte SIG - Routes Nationales Madagascar</title>

<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>

<style>
body {
  margin: 0;
  display: flex;
  height: 100vh;
  font-family: Arial, sans-serif;
}

#panel {
  width: 350px;
  padding: 15px;
  background: #ffffff;
  overflow-y: auto;
  box-shadow: 2px 0 6px rgba(0,0,0,0.2);
}

#map {
  flex: 1;
}

.rn-item {
  padding: 8px;
  margin-bottom: 6px;
  cursor: pointer;
  border-left: 4px solid #9b59b6;
  background: #ecf0f1;
}

.rn-item:hover {
  background: #f5d9ff;
}

.rn-item.active {
  background: #8e44ad;
  color: white;
  font-weight: bold;
}
</style>
</head>

<body>

<div id="panel">
  <h3>🛣️ Routes Nationales</h3>
  <input type="text" id="searchRN" placeholder="Rechercher..." style="width:100%; margin-bottom:10px;">
  <div id="stats">⏳ Chargement...</div>
  <div id="listeRN"></div>
</div>

<div id="map"></div>

<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>

<script>
/* =====================
 1️⃣ Carte
===================== */
const map = L.map('map').setView([-18.8792, 46.8696], 6);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  attribution: '© OpenStreetMap'
}).addTo(map);

/* =====================
 2️⃣ Variables globales
===================== */
let rnLayer = null;
let rnIndex = {};   // nom -> [layers] (groupé par nom de RN)
let rnData = [];
let rnUnique = []; // Liste unique des RN (pas de doublons par segments)

/* =====================
 3️⃣ Charger les RN
===================== */
fetch('../rn')
  .then(res => res.json())
  .then(data => {
    // Nettoyer les données
    rnData = data
      .filter(r => r.geometry && r.id > 7) // Exclure test data
      .map(r => ({
        ...r,
        ref: (r.ref && String(r.ref).trim()) || 'N/A'
      }));

    // Créer une liste unique des RN (groupée par nom)
    rnUnique = [];
    const rnMap = {};
    
    // Regrouper par nom et fusionner les données
    rnData.forEach(rn => {
      const nom = rn.nom;
      if (!rnMap[nom]) {
        rnMap[nom] = {
          nom: rn.nom,
          id: rn.id,
          ref: rn.ref,
          refs: new Set() // Pour collecter toutes les références uniques
        };
      }
      if (rn.ref && rn.ref !== 'N/A') {
        rnMap[nom].refs.add(rn.ref);
      }
    });
    
    // Convertir en array et fusionner les références
    rnUnique = Object.values(rnMap).map(rn => ({
      nom: rn.nom,
      id: rn.id,
      ref: rn.refs.size > 0 ? Array.from(rn.refs).join(', ') : 'N/A'
    }));
    
    // Trier par nom
    rnUnique.sort((a, b) => a.nom.localeCompare(b.nom));

    console.log('✅ ' + rnUnique.length + ' route(s) unique(s) chargee(s) (' + rnData.length + ' segments)');
    afficherRN();
    remplirListe();

    document.getElementById('stats').innerHTML =
      '✅ ' + rnUnique.length + ' route(s) chargee(s)';
  })
  .catch(err => {
    document.getElementById('stats').innerHTML =
      '❌ Erreur de chargement';
    console.error(err);
  });

/* =====================
 4️⃣ Affichage carte
===================== */
function afficherRN() {

  if (rnLayer) map.removeLayer(rnLayer);
  rnIndex = {};

  const features = rnData.map(rn => ({
    type: "Feature",
    id: rn.id,
    nom: rn.nom,
    properties: {
      nom: rn.nom,
      ref: rn.ref
    },
    geometry: typeof rn.geometry === "string"
      ? JSON.parse(rn.geometry)
      : rn.geometry
  }));

  rnLayer = L.geoJSON(features, {
    style: {
      color: '#9b59b6',
      weight: 3
    },
    onEachFeature: (feature, layer) => {

      // Grouper par NOM de RN (pas par ID, pour regrouper les segments)
      const rnName = feature.nom;
      if (!rnIndex[rnName]) {
        rnIndex[rnName] = [];
      }
      rnIndex[rnName].push(layer);

      layer.on('click', () => selectRN(rnName));
    }
  }).addTo(map);

  // 🔥 ZOOM GLOBAL SUR TOUTES LES RN
  map.fitBounds(rnLayer.getBounds(), { padding: [40,40] });
}

/* =====================
 5️⃣ Liste à gauche
===================== */
function remplirListe() {

  const div = document.getElementById('listeRN');
  div.innerHTML = '';

  rnUnique.forEach(rn => {
    const item = document.createElement('div');
    item.className = 'rn-item';
    item.id = 'rn-' + rn.nom.replace(/[^a-zA-Z0-9]/g, '_');
    
    // Afficher proprement la ref (jamais "false")
    const refDisplay = (rn.ref && rn.ref !== 'false') ? rn.ref : 'N/A';
    
    item.innerHTML = '<div style="font-weight: bold; color: #8e44ad;">' + rn.nom + '</div>' +
                     '<div style="font-size: 12px; color: #666; margin-top: 3px;">Ref: ' + refDisplay + '</div>';
    item.onclick = function() { selectRN(rn.nom); };
    div.appendChild(item);
  });
}

/* =====================
 6️⃣ Sélection RN (CORRIGÉ)
===================== */
function selectRN(rnName) {

  // reset liste
  document.querySelectorAll('.rn-item')
    .forEach(i => i.classList.remove('active'));

  const itemId = 'rn-' + rnName.replace(/[^a-zA-Z0-9]/g, '_');
  const item = document.getElementById(itemId);
  if (item) item.classList.add('active');

  // reset styles carte - tous les segments en violet clair
  Object.values(rnIndex).flat().forEach(l =>
    l.setStyle({ color:'#9b59b6', weight:3 })
  );

  if (!rnIndex[rnName]) return;

  // 🟣 Highlight SEULEMENT les segments de cette RN en violet foncé
  rnIndex[rnName].forEach(l =>
    l.setStyle({ color:'#6c3483', weight:5 })
  );

  // 🔥 ZOOM SUR TOUTE LA RN AVEC PADDING IMPORTANT
  const group = L.featureGroup(rnIndex[rnName]);
  const bounds = group.getBounds();
  
  // Calculer un padding adapté à la taille de la RN
  const padding = Math.min(150, 100); // Min 100px de padding
  
  map.fitBounds(bounds, { 
    padding: [padding, padding],
    maxZoom: 15
  });
  
  console.log('📍 Affichage RN: ' + rnName + ' (' + rnIndex[rnName].length + ' segment(s))');
}

/* =====================
 7️⃣ Recherche
===================== */
document.getElementById('searchRN').addEventListener('input', e => {
  const v = e.target.value.toLowerCase();
  document.querySelectorAll('.rn-item').forEach(i => {
    i.style.display =
      i.textContent.toLowerCase().includes(v) ? 'block' : 'none';
  });
});
</script>

</body>
</html>
