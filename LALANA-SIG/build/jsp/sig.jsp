<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>🗺️ Carte SIG - Routes Nationales Madagascar</title>

    <!-- Leaflet CSS -->
    <link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css" />

    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }

        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: #f0f0f0;
        }

        #map {
            width: 100%;
            height: 100vh;
        }

        #panel {
            position: absolute;
            top: 20px;
            left: 20px;
            z-index: 1000;
            background: white;
            padding: 20px;
            border-radius: 8px;
            box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
            min-width: 280px;
            max-width: 350px;
            font-size: 14px;
            margin-left : 45px;
        }

        #panel h2 {
            color: #2c3e50;
            margin-bottom: 15px;
            font-size: 18px;
            border-bottom: 3px solid #3498db;
            padding-bottom: 10px;
        }

        #panel label {
            display: block;
            margin: 10px 0 5px 0;
            color: #555;
            font-weight: 600;
        }

        #listeRN {
            width: 100%;
            padding: 10px;
            border: 2px solid #ddd;
            border-radius: 4px;
            font-size: 14px;
            background: white;
            cursor: pointer;
            transition: border-color 0.3s ease;
        }

        #listeRN:focus {
            outline: none;
            border-color: #3498db;
            box-shadow: 0 0 5px rgba(52, 152, 219, 0.3);
        }

        #listeRN:hover {
            border-color: #3498db;
        }

        #stats {
            margin-top: 15px;
            padding: 10px;
            background: #f8f9fa;
            border-left: 4px solid #27ae60;
            border-radius: 4px;
            color: #555;
            font-size: 13px;
        }

        .loading {
            color: #e74c3c;
            font-style: italic;
            margin-top: 10px;
        }

        .success {
            color: #27ae60;
            font-weight: bold;
        }
    </style>
</head>

<body>

<!-- Panneau de contrôle -->
<div id="panel">
    <h2>🗺️ Routes Nationales</h2>
    <label for="listeRN">Sélectionner une route :</label>
    <select id="listeRN">
        <option value="">⏳ Chargement...</option>
    </select>
    <div id="stats"></div>
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
let rnCount = 0;

/* ==========================
   3️⃣ Charger les RN
========================== */
console.log('🔄 Chargement des routes nationales...');
fetch('rn')
    .then(response => {
        console.log('✅ Réponse reçue, statut:', response.status);
        return response.json();
    })
    .then(data => {
        console.log('📊 Données reçues:', data);
        if (!data || data.length === 0) {
            console.warn('⚠️  Aucune RN trouvée!');
            document.getElementById('stats').innerHTML = 
                '<span class="loading">❌ Aucune route trouvée.<br>' +
                'Exécuter: <code>assets/BD/insert_rn.sql</code></span>';
            document.getElementById('listeRN').innerHTML = 
                '<option>-- Aucune donnée --</option>';
            return;
        }
        rnCount = data.length;
        afficherRN(data);
        remplirListe(data);
        document.getElementById('stats').innerHTML = 
            `<span class="success">✅ ${rnCount} route(s) chargée(s)</span>`;
    })
    .catch(err => {
        console.error('❌ Erreur:', err);
        document.getElementById('stats').innerHTML = 
            `<span class="loading">❌ Erreur de connexion.<br>` +
            `Vérifier que le serveur est en cours d'exécution</span>`;
    });

/* ==========================
   4️⃣ Afficher RN sur la carte
========================== */
function afficherRN(data) {
    console.log('🎨 Affichage des RN sur la carte...');
    rnLayer = L.geoJSON(data, {
        style: {
            color: '#3498db',
            weight: 3,
            opacity: 0.7
        },
        onEachFeature: function (feature, layer) {
            rnIndex[feature.id] = layer;

            layer.on('click', function () {
                highlightRN(layer);
                console.log('📍 RN cliquée:', feature.nom);
            });

            layer.bindPopup(`
                <div style="font-weight: bold; color: #2c3e50;">
                    ${feature.nom}
                </div>
            `);
        }
    }).addTo(map);
    
    console.log('✅ RN affichées sur la carte');
}

/* ==========================
   5️⃣ Liste déroulante RN
========================== */
function remplirListe(data) {
    console.log('📋 Remplissage de la liste...');
    const select = document.getElementById("listeRN");
    select.innerHTML = '<option value="">-- Sélectionner --</option>';

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
            console.log('🎯 Navigation vers RN ID:', id);
        }
    });
}

/* ==========================
   6️⃣ Mettre une RN en bleu
========================== */
function highlightRN(layer) {
    rnLayer.eachLayer(l => {
        l.setStyle({
            color: '#3498db',
            weight: 3,
            opacity: 0.7
        });
    });

    layer.setStyle({
        color: '#e74c3c',
        weight: 5,
        opacity: 1
    });
}
</script>

</body>
</html>
