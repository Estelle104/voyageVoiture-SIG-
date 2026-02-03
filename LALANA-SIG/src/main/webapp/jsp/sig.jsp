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
            display: flex;
            height: 100vh;
        }

        /* Panneau latéral gauche */
        #panel {
            width: 350px;
            background: white;
            padding: 20px;
            overflow-y: auto;
            box-shadow: 2px 0 8px rgba(0, 0, 0, 0.1);
            z-index: 1000;
        }

        #panel h2 {
            color: #2c3e50;
            margin-bottom: 20px;
            font-size: 18px;
            border-bottom: 3px solid #3498db;
            padding-bottom: 10px;
        }

        #panel label {
            display: block;
            margin: 15px 0 8px 0;
            color: #555;
            font-weight: 600;
            font-size: 14px;
        }

        #listeRN {
            width: 100%;
            padding: 10px;
            border: 2px solid #ddd;
            border-radius: 4px;
            font-size: 13px;
            background: white;
            cursor: pointer;
            transition: border-color 0.3s ease;
            height: 35px;
        }

        #listeRN:focus {
            outline: none;
            border-color: #3498db;
            box-shadow: 0 0 5px rgba(52, 152, 219, 0.3);
        }

        #listeRN:hover {
            border-color: #3498db;
        }

        #listeRNDetails {
            margin-top: 20px;
            padding: 15px;
            background: #f8f9fa;
            border-left: 4px solid #1c6bad;
            border-radius: 4px;
            max-height: 400px;
            overflow-y: auto;
        }

        .rn-item {
            padding: 10px 12px;
            margin: 8px 0;
            background: #ecf0f1;
            border-left: 4px solid #3498db;
            border-radius: 4px;
            cursor: pointer;
            transition: all 0.3s ease;
            font-size: 13px;
            color: #2c3e50;
        }

        .rn-item:hover {
            background: #d5e8f7;
            border-left-color: #1c6bad;
            transform: translateX(5px);
        }

        .rn-item.active {
            background: #1c6bad;
            color: white;
            border-left-color: #0d4577;
            font-weight: bold;
        }

        #stats {
            margin-top: 15px;
            padding: 10px;
            background: white;
            border-left: 4px solid #27ae60;
            border-radius: 4px;
            color: #555;
            font-size: 13px;
        }

        .loading {
            color: #e74c3c;
            font-style: italic;
        }

        .success {
            color: #27ae60;
            font-weight: bold;
        }

        /* Carte à droite */
        #map {
            flex: 1;
            height: 100vh;
        }
    </style>
</head>

<body>

<!-- Panneau latéral gauche avec liste RN -->
<div id="panel">
    <h2>🗺️ Routes Nationales</h2>
    <label for="searchRN">🔍 Rechercher :</label>
    <input type="text" id="searchRN" placeholder="Nom de la route..." style="width: 100%; padding: 8px; border: 1px solid #ddd; border-radius: 4px; margin-bottom: 15px;">
    
    <div id="listeRNDetails">
        <div id="stats"><span class="loading">⏳ Chargement des routes...</span></div>
    </div>
</div>

<!-- Carte à droite -->
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
let rnData = [];
let currentSelectedId = null;

/* ==========================
   3️⃣ Données de test (avant d'utiliser les vraies données)
========================== */
const donneesBrutes = [
    {
        id: 1,
        nom: "RN1 - Antananarivo to Toliara",
        geometry: {
            type: "LineString",
            coordinates: [
                [47.5236, -19.8592],
                [47.3, -19.9],
                [46.8, -20.5],
                [45.5, -23.2],
                [43.5, -24.5]
            ]
        }
    },
    {
        id: 2,
        nom: "RN2 - Antananarivo to Toamasina",
        geometry: {
            type: "LineString",
            coordinates: [
                [47.5236, -19.8592],
                [48.0, -19.8],
                [49.0, -19.3],
                [49.4, -18.8]
            ]
        }
    },
    {
        id: 3,
        nom: "RN3 - Antananarivo to Sambava",
        geometry: {
            type: "LineString",
            coordinates: [
                [47.5236, -19.8592],
                [48.2, -19.5],
                [49.2, -18.9],
                [50.1, -17.5]
            ]
        }
    },
    {
        id: 4,
        nom: "RN4 - Antananarivo to Antalaha",
        geometry: {
            type: "LineString",
            coordinates: [
                [47.5236, -19.8592],
                [48.3, -19.2],
                [49.5, -18.5],
                [50.5, -17.0]
            ]
        }
    },
    {
        id: 5,
        nom: "RN5 - Fianarantsoa to Toliara",
        geometry: {
            type: "LineString",
            coordinates: [
                [47.0, -21.4],
                [46.5, -22.0],
                [45.8, -23.0],
                [44.0, -24.5]
            ]
        }
    }
];

/* ==========================
   4️⃣ Charger les RN
========================== */
console.log('🔄 Chargement des routes nationales...');

// Essayer de charger depuis le serveur, sinon utiliser les données de test
fetch('rn')
    .then(response => {
        console.log('✅ Réponse reçue, statut:', response.status);
        if (!response.ok) throw new Error('Serveur non disponible');
        return response.json();
    })
    .then(data => {
        console.log('📊 Données reçues du serveur:', data);
        if (!data || data.length === 0) {
            console.warn('⚠️  Données vides, utilisation des données de test');
            chargerDonneesBrutes();
        } else {
            rnData = data;
            afficherRN();
            remplirListe();
            updateStats();
        }
    })
    .catch(err => {
        console.warn('⚠️  Serveur non disponible, utilisation des données de test:', err);
        chargerDonneesBrutes();
    });

function chargerDonneesBrutes() {
    console.log('📝 Utilisation des données de test');
    rnData = donneesBrutes;
    afficherRN();
    remplirListe();
    updateStats();
    document.getElementById('stats').innerHTML = 
        '<span style="color: #f39c12; font-weight: bold;">ℹ️ Mode TEST - Données brutes</span>';
}

/* ==========================
   5️⃣ Afficher RN sur la carte
========================== */
function afficherRN() {
    console.log('🎨 Affichage des RN sur la carte...');
    
    // Supprimer les couches précédentes
    if (rnLayer) {
        map.removeLayer(rnLayer);
    }
    
    // Créer un array de features GeoJSON
    const features = rnData.map(rn => ({
        type: "Feature",
        id: rn.id,
        properties: { nom: rn.nom },
        geometry: typeof rn.geometry === 'string' ? JSON.parse(rn.geometry) : rn.geometry
    }));
    
    rnLayer = L.geoJSON(features, {
        style: {
            color: '#3498db',
            weight: 3,
            opacity: 0.7
        },
        onEachFeature: function (feature, layer) {
            rnIndex[feature.id] = layer;

            layer.on('click', function () {
                highlightRN(feature.id);
                console.log('📍 RN cliquée:', feature.properties.nom);
            });

            layer.bindPopup(`
                <div style="font-weight: bold; color: #1c6bad;">
                    ${feature.properties.nom}
                </div>
            `);
        }
    }).addTo(map);
    
    console.log('✅ RN affichées sur la carte');
}

/* ==========================
   6️⃣ Remplir la liste de gauche
========================== */
function remplirListe() {
    console.log('📋 Remplissage de la liste...');
    const container = document.getElementById("listeRNDetails");
    container.innerHTML = '';

    rnData.forEach(rn => {
        const item = document.createElement("div");
        item.className = "rn-item";
        item.textContent = rn.nom;
        item.onclick = () => {
            selectRN(rn.id);
        };
        item.id = `rn-${rn.id}`;
        container.appendChild(item);
    });
}

/* ==========================
   7️⃣ Sélectionner une RN
========================== */
function selectRN(id) {
    highlightRN(id);
    if (rnIndex[id]) {
        map.fitBounds(rnIndex[id].getBounds());
        console.log('🎯 Navigation vers RN ID:', id);
    }
}

/* ==========================
   8️⃣ Mettre une RN en bleu roi (highlight)
========================== */
function highlightRN(id) {
    // Réinitialiser tous les styles
    if (rnLayer) {
        rnLayer.eachLayer(l => {
            l.setStyle({
                color: '#3498db',
                weight: 3,
                opacity: 0.7
            });
        });
    }

    // Supprimer les anciens actifs
    document.querySelectorAll('.rn-item.active').forEach(el => {
        el.classList.remove('active');
    });

    // Appliquer le style bleu roi à l'élément sélectionné
    if (rnIndex[id]) {
        rnIndex[id].setStyle({
            color: '#1c6bad',  // Bleu roi
            weight: 5,
            opacity: 1
        });
    }

    // Marquer comme actif dans la liste
    const item = document.getElementById(`rn-${id}`);
    if (item) {
        item.classList.add('active');
    }

    currentSelectedId = id;
}

/* ==========================
   9️⃣ Mise à jour des stats
========================== */
function updateStats() {
    const stats = document.getElementById('stats');
    if (rnData.length > 0) {
        stats.innerHTML = `<span class="success">✅ ${rnData.length} route(s) chargée(s)</span>`;
    } else {
        stats.innerHTML = '<span class="loading">❌ Aucune route trouvée</span>';
    }
}

/* ==========================
   🔟 Recherche/Filtre
========================== */
document.getElementById('searchRN').addEventListener('input', function(e) {
    const searchTerm = e.target.value.toLowerCase();
    
    document.querySelectorAll('.rn-item').forEach(item => {
        const text = item.textContent.toLowerCase();
        if (text.includes(searchTerm)) {
            item.style.display = 'block';
        } else {
            item.style.display = 'none';
        }
    });
});
</script>

</body>
</html>
