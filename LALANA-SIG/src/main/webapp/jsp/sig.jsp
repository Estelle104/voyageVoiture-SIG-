<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Carte SIG - Routes Nationales</title>

    <!-- Leaflet CSS -->
    <link
        rel="stylesheet"
        href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"
    />

    <style>
        body {
            margin: 0;
            padding: 0;
        }

        #map {
            width: 100%;
            height: 100vh;
        }

        #panel {
            position: absolute;
            top: 10px;
            left: 10px;
            z-index: 1000;
            background: white;
            padding: 10px;
            border-radius: 5px;
            box-shadow: 0 0 5px rgba(0,0,0,0.3);
        }
    </style>
</head>

<body>

<!-- Panneau RN -->
<div id="panel">
    <label for="listeRN"><b>Routes Nationales</b></label><br>
    <select id="listeRN">
        <option value="">-- Sélectionner --</option>
    </select>
</div>

<!-- Carte -->
<div id="map"></div>

<!-- Leaflet JS -->
<script src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js"></script>

<script>
/* ==========================
   1️⃣ Initialisation carte
========================== */
const map = L.map('map').setView([-18.8792, 46.8696], 7);

// Fond OpenStreetMap
L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
    attribution: '© OpenStreetMap contributors',
    maxZoom: 19
}).addTo(map);


/* ==========================
   2️⃣ Variables globales
========================== */
let rnLayer = null;
let rnIndex = {}; // id → layer


/* ==========================
   3️⃣ Charger les RN
========================== */
fetch('rn')
    .then(response => response.json())
    .then(data => {
        afficherRN(data);
        remplirListe(data);
    })
    .catch(err => console.error(err));


/* ==========================
   4️⃣ Afficher RN sur la carte
========================== */
function afficherRN(data) {
    rnLayer = L.geoJSON(data, {
        style: {
            color: 'gray',
            weight: 3
        },
        onEachFeature: function (feature, layer) {
            rnIndex[feature.id] = layer;

            layer.on('click', function () {
                highlightRN(layer);
            });

            layer.bindPopup(feature.nom);
        }
    }).addTo(map);
}


/* ==========================
   5️⃣ Liste déroulante RN
========================== */
function remplirListe(data) {
    const select = document.getElementById("listeRN");

    data.forEach(rn => {
        const option = document.createElement("option");
        option.value = rn.id;
        option.textContent = rn.nom;
        select.appendChild(option);
    });

    select.addEventListener("change", function () {
        const id = this.value;
        if (id && rnIndex[id]) {
            highlightRN(rnIndex[id]);
            map.fitBounds(rnIndex[id].getBounds());
        }
    });
}


/* ==========================
   6️⃣ Mettre une RN en bleu
========================== */
function highlightRN(layer) {
    rnLayer.eachLayer(l => {
        l.setStyle({
            color: 'gray',
            weight: 3
        });
    });

    layer.setStyle({
        color: 'blue',
        weight: 5
    });
}
</script>

</body>
</html>
