<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>🗺️ Carte SIG - Routes Nationales Madagascar</title>

<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />

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
  border-left: 4px solid #3498db;
  background: #ecf0f1;
}

.rn-item:hover {
  background: #d6eaf8;
}

.rn-item.active {
  background: #1c6bad;
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
   1. Carte
===================== */
const map = L.map('map').setView([-18.8792, 46.8696], 7);

L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
  attribution: '© OpenStreetMap'
}).addTo(map);

/* =====================
   2. Variables
===================== */
let rnLayer;
let rnIndex = {};
let rnData = [];

/* =====================
   3. Charger RN
===================== */
fetch('../rn')
  .then(res => res.json())
  .then(data => {
    rnData = data.filter(r => r.id > 7);
    afficherRN();
    remplirListe();
    document.getElementById('stats').innerHTML =
      `✅ ${rnData.length} route(s) chargée(s)`;
  })
  .catch(err => {
    document.getElementById('stats').innerHTML =
      '❌ Erreur de chargement';
    console.error(err);
  });

/* =====================
   4. Affichage carte
===================== */
function afficherRN() {

  if (rnLayer) map.removeLayer(rnLayer);
  rnIndex = {};

  rnLayer = L.geoJSON(rnData.map(rn => ({
    type: "Feature",
    id: rn.id,
    properties: {
      nom: rn.nom,
      ref: rn.ref
    },
    geometry: typeof rn.geometry === "string"
      ? JSON.parse(rn.geometry)
      : rn.geometry
  })), {
    style: {
      color: '#3498db',
      weight: 3
    },
    onEachFeature: (feature, layer) => {
      if (!rnIndex[feature.id]) {
        rnIndex[feature.id] = [];
      }
      rnIndex[feature.id].push(layer);

      layer.on('click', () => selectRN(feature.id));
    }
  }).addTo(map);
}

/* =====================
   5. Liste gauche
===================== */
function remplirListe() {
  const div = document.getElementById('listeRN');
  div.innerHTML = '';

  rnData.forEach(rn => {
    const item = document.createElement('div');
    item.className = 'rn-item';
    item.id = `rn-${rn.id}`;
    item.innerHTML = `<b>${rn.nom}</b><br>Ref: ${rn.ref || 'N/A'}`;
    item.onclick = () => selectRN(rn.id);
    div.appendChild(item);
  });
}

/* =====================
   6. Sélection RN (FIX)
===================== */
function selectRN(id) {

  document.querySelectorAll('.rn-item')
    .forEach(i => i.classList.remove('active'));

  document.getElementById(`rn-${id}`)?.classList.add('active');

  // Reset styles
  Object.values(rnIndex).flat().forEach(l =>
    l.setStyle({ color:'#3498db', weight:3 })
  );

  if (!rnIndex[id]) return;

  // Highlight TOUS les segments
  rnIndex[id].forEach(l =>
    l.setStyle({ color:'#1c6bad', weight:5 })
  );

  // 🔥 Calcul du bounds GLOBAL (solution)
  const group = L.featureGroup(rnIndex[id]);
  map.fitBounds(group.getBounds(), { padding: [80,80] });
}

/* =====================
   7. Recherche
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
