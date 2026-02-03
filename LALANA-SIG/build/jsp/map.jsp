<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
    <title>Carte SIG - Lalana</title>

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
    </style>
</head>

<body>

    <!-- Conteneur de la carte -->
    <div id="map"></div>

    <!-- Leaflet JS -->
    <script 
        src="https://unpkg.com/leaflet@1.9.4/dist/leaflet.js">
    </script>

    <script>
        // Création de la carte
        const map = L.map('map').setView([        const map = L.map('map').setView([-18.8792, 46.8696], 7);
-18.8792, 46.8696], 7);        const map = L.map('map').setView([-18.8792, 46.8696], 7);


        // Fond de carte OpenStreetMap
        L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors'
        }).addTo(map);
    </script>
    <script>
fetch("roads.geojson")
  .then(r => r.json())
  .then(data => {
    L.geoJSON(data).addTo(map);
  });
</script>

</body>
</html>
