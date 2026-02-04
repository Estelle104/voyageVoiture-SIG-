#!/bin/bash

################################################################################
# 🛠️ SCRIPT D'AJOUT DES FONCTIONNALITÉS SIMBA
# Ajoute le traçage des Simba sur la carte SIG
################################################################################

echo "🛠️ AJOUT DES FONCTIONNALITÉS SIMBA"
echo "=================================="

PROJECT_DIR="/home/itu/S3/Progammation/voiture/voyageVoiture-SIG-"
LALANA_DIR="$PROJECT_DIR/LALANA-SIG"
TOMCAT_HOME="/opt/lampp/htdocs/tomcat/apache-tomcat-10.0.16"

echo ""
echo "📦 [1/5] Création de SimbaServlet..."

# 1. Créer SimbaServlet
cat > "$LALANA_DIR/src/main/java/servlet/SimbaServlet.java" << 'EOF'
package servlet;

import dao.SimbaDAO;
import dao.LalanaDAO;
import model.Simba;
import model.Lalana;
import utildb.ConnexionOracle;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.Vector;

public class SimbaServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json; charset=UTF-8");
        PrintWriter out = res.getWriter();
        String action = req.getParameter("action");
        
        try {
            ConnexionOracle conn = new ConnexionOracle();
            
            if ("getByLalana".equals(action)) {
                String idLalanaStr = req.getParameter("idLalana");
                if (idLalanaStr != null) {
                    int idLalana = Integer.parseInt(idLalanaStr);
                    Vector<Simba> simbas = SimbaDAO.getByIdLalana(conn, idLalana);
                    outputSimbasJson(out, simbas);
                } else {
                    out.print("{\"error\":\"idLalana manquant\"}");
                }
            } else if ("getAll".equals(action)) {
                Vector<Simba> allSimbas = SimbaDAO.getAll(conn);
                outputSimbasJson(out, allSimbas);
            } else {
                // Par défaut : retourner tous les Simba
                Vector<Simba> allSimbas = SimbaDAO.getAll(conn);
                outputSimbasJson(out, allSimbas);
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private void outputSimbasJson(PrintWriter out, Vector<Simba> simbas) {
        out.print("[");
        for (int i = 0; i < simbas.size(); i++) {
            Simba s = simbas.get(i);
            out.print("{");
            out.print("\"id\":" + s.getId() + ",");
            out.print("\"descriptions\":\"" + s.getDescriptions().replaceAll("\"", "\\\\\"") + "\",");
            out.print("\"pkDebut\":" + s.getPkDebut() + ",");
            out.print("\"pkFin\":" + s.getPkFin() + ",");
            out.print("\"surface\":" + s.getSurface() + ",");
            out.print("\"profondeur\":" + s.getProfondeur() + ",");
            out.print("\"idLalana\":" + s.getIdLalana());
            out.print("}");
            if (i < simbas.size() - 1) out.print(",");
        }
        out.print("]");
    }
    
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        
        res.setContentType("application/json; charset=UTF-8");
        PrintWriter out = res.getWriter();
        
        try {
            // Récupérer les paramètres du formulaire
            String descriptions = req.getParameter("descriptions");
            double pkDebut = Double.parseDouble(req.getParameter("pkDebut"));
            double pkFin = Double.parseDouble(req.getParameter("pkFin"));
            double surface = Double.parseDouble(req.getParameter("surface"));
            double profondeur = Double.parseDouble(req.getParameter("profondeur"));
            int idLalana = Integer.parseInt(req.getParameter("idLalana"));
            int idTypeMatiere = Integer.parseInt(req.getParameter("idTypeMatiere"));
            
            // Créer et sauvegarder le Simba
            ConnexionOracle conn = new ConnexionOracle();
            Simba simba = new Simba(descriptions, pkDebut, pkFin, 0.0, surface, profondeur, idLalana, null, idTypeMatiere);
            SimbaDAO.save(conn, simba);
            
            out.print("{\"success\":true,\"message\":\"Simba ajouté avec succès\",\"id\":" + simba.getId() + "}");
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
EOF

echo "✅ SimbaServlet créé"

echo ""
echo "📦 [2/5] Création de LalanaServlet (correspondance avec RN)..."

# 2. Créer LalanaServlet
cat > "$LALANA_DIR/src/main/java/servlet/LalanaServlet.java" << 'EOF'
package servlet;

import dao.LalanaDAO;
import model.Lalana;
import utildb.ConnexionOracle;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.Vector;

public class LalanaServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json; charset=UTF-8");
        PrintWriter out = res.getWriter();

        try {
            ConnexionOracle conn = new ConnexionOracle();
            Vector<Lalana> lalanas = LalanaDAO.getAll(conn);

            out.print("[");
            for (int i = 0; i < lalanas.size(); i++) {
                Lalana l = lalanas.get(i);
                out.print("{");
                out.print("\"id\":" + l.getId() + ",");
                out.print("\"nom\":\"" + l.getNomLalana().replaceAll("\"", "\\\\\"") + "\",");
                out.print("\"distance\":" + l.getDistance() + ",");
                out.print("\"debut\":\"" + l.getDebut().replaceAll("\"", "\\\\\"") + "\",");
                out.print("\"fin\":\"" + l.getFin().replaceAll("\"", "\\\\\"") + "\"");
                out.print("}");
                if (i < lalanas.size() - 1) out.print(",");
            }
            out.print("]");

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
EOF

echo "✅ LalanaServlet créé"

echo ""
echo "📦 [3/5] Ajout des mappings web.xml..."

# 3. Mettre à jour web.xml
cat > "$LALANA_DIR/src/main/webapp/WEB-INF/web.xml" << 'EOF'
<?xml version="1.0" encoding="UTF-8"?>
<web-app xmlns="https://jakarta.ee/xml/ns/jakartaee"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="https://jakarta.ee/xml/ns/jakartaee
         https://jakarta.ee/xml/ns/jakartaee/web-app_5_0.xsd"
         version="5.0">

    <servlet>
        <servlet-name>RNServlet</servlet-name>
        <servlet-class>servlet.RNServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>RNServlet</servlet-name>
        <url-pattern>/rn</url-pattern>
    </servlet-mapping>
    
    <servlet>
        <servlet-name>SimbaServlet</servlet-name>
        <servlet-class>servlet.SimbaServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>SimbaServlet</servlet-name>
        <url-pattern>/simba</url-pattern>
    </servlet-mapping>
    
    <servlet>
        <servlet-name>LalanaServlet</servlet-name>
        <servlet-class>servlet.LalanaServlet</servlet-class>
    </servlet>
    <servlet-mapping>
        <servlet-name>LalanaServlet</servlet-name>
        <url-pattern>/lalana</url-pattern>
    </servlet-mapping>

</web-app>
EOF

echo "✅ web.xml mis à jour"

echo ""
echo "📦 [4/5] Création de getAll() pour SimbaDAO..."

# 4. Ajouter méthode getAll() à SimbaDAO si elle n'existe pas
if ! grep -q "getAll" "$LALANA_DIR/src/main/java/dao/SimbaDAO.java"; then
    cat >> "$LALANA_DIR/src/main/java/dao/SimbaDAO.java" << 'EOF'

    // GET ALL
    public static Vector<Simba> getAll(ConnexionOracle conn) throws Exception {
        Vector<Simba> list = new Vector<>();
        String sql = "SELECT * FROM Simba ORDER BY idLalana, pkDebut";

        try (Connection c = conn.getConnection();
                PreparedStatement pstmt = c.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Simba s = new Simba();
                s.setId(rs.getInt("id"));
                s.setDescriptions(rs.getString("descriptions"));
                s.setPkDebut(rs.getDouble("pkDebut"));
                s.setPkFin(rs.getDouble("pkFin"));
                s.setTauxRalentissement(rs.getDouble("tauxRalentissement"));
                s.setSurface(rs.getDouble("surface"));
                s.setProfondeur(rs.getDouble("profondeur"));
                s.setIdLalana(rs.getInt("idLalana"));
                s.setIdLavaka((Integer) rs.getObject("idLavaka"));
                s.setIdTypeMatiere(rs.getInt("idTypeMatiere"));
                list.add(s);
            }
        }
        return list;
    }
EOF
    echo "✅ Méthode getAll() ajoutée à SimbaDAO"
fi

echo ""
echo "📦 [5/5] Création de sig-enhanced.jsp avec Simba..."

# 5. Créer version améliorée du JSP avec support Simba
cat > "$LALANA_DIR/src/main/webapp/jsp/sig-enhanced.jsp" << 'EOF'
<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>🗺️ Carte SIG - Routes Nationales & Simba</title>

<link rel="stylesheet" href="https://unpkg.com/leaflet@1.9.4/dist/leaflet.css"/>

<style>
body {
  margin: 0;
  display: flex;
  height: 100vh;
  font-family: Arial, sans-serif;
}

#panel {
  width: 400px;
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
  border-radius: 4px;
}

.rn-item:hover {
  background: #f5d9ff;
}

.rn-item.active {
  background: #8e44ad;
  color: white;
  font-weight: bold;
}

.form-section {
  background: #f8f9fa;
  padding: 15px;
  margin: 10px 0;
  border-radius: 8px;
  border: 1px solid #dee2e6;
}

.form-section h4 {
  margin: 0 0 10px 0;
  color: #495057;
}

.form-group {
  margin-bottom: 10px;
}

.form-group label {
  display: block;
  margin-bottom: 3px;
  font-size: 13px;
  font-weight: 500;
}

.form-group input, .form-group select {
  width: 100%;
  padding: 6px;
  border: 1px solid #ced4da;
  border-radius: 4px;
  font-size: 13px;
}

.btn {
  padding: 8px 15px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
  margin-right: 5px;
}

.btn-primary {
  background-color: #007bff;
  color: white;
}

.btn-danger {
  background-color: #dc3545;
  color: white;
}

.btn-success {
  background-color: #28a745;
  color: white;
}

#simbaList {
  max-height: 150px;
  overflow-y: auto;
  border: 1px solid #dee2e6;
  border-radius: 4px;
  background: white;
}

.simba-item {
  padding: 6px;
  border-bottom: 1px solid #eee;
  cursor: pointer;
  font-size: 12px;
}

.simba-item:hover {
  background: #f8d7da;
}

.simba-item.active {
  background: #dc3545;
  color: white;
}
</style>
</head>

<body>

<div id="panel">
  <h3>🛣️ Routes Nationales</h3>
  <input type="text" id="searchRN" placeholder="Rechercher..." style="width:100%; margin-bottom:10px;">
  <div id="stats">⏳ Chargement...</div>
  <div id="listeRN"></div>
  
  <!-- Formulaire d'ajout de Simba -->
  <div class="form-section">
    <h4>➕ Ajouter un Simba</h4>
    <form id="simbaForm">
      <div class="form-group">
        <label>Lalana:</label>
        <select id="lalanaSelect" required>
          <option value="">Sélectionner une Lalana...</option>
        </select>
      </div>
      <div class="form-group">
        <label>Description:</label>
        <input type="text" id="descriptions" required>
      </div>
      <div class="form-group">
        <label>PK Début (km):</label>
        <input type="number" id="pkDebut" step="0.1" required>
      </div>
      <div class="form-group">
        <label>PK Fin (km):</label>
        <input type="number" id="pkFin" step="0.1" required>
      </div>
      <div class="form-group">
        <label>Surface (m²):</label>
        <input type="number" id="surface" step="0.1" required>
      </div>
      <div class="form-group">
        <label>Profondeur (cm):</label>
        <input type="number" id="profondeur" step="0.1" required>
      </div>
      <div class="form-group">
        <label>Type Matière:</label>
        <select id="idTypeMatiere" required>
          <option value="1">Type 1</option>
          <option value="2">Type 2</option>
          <option value="3">Type 3</option>
        </select>
      </div>
      <button type="submit" class="btn btn-primary">Ajouter Simba</button>
    </form>
  </div>
  
  <!-- Contrôles de traçage -->
  <div class="form-section">
    <h4>🎯 Traçage Simba</h4>
    <button id="showAllSimbas" class="btn btn-danger">Afficher tous les Simba</button>
    <button id="hideSimbas" class="btn btn-success">Masquer Simba</button>
    <div id="simbaCount" style="margin-top: 10px; font-size: 13px;"></div>
    <div id="simbaList"></div>
  </div>
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
let simbaLayer = null;
let rnIndex = {};   // nom -> [layers] (groupé par nom de RN)
let simbaIndex = {}; // id -> layer
let rnData = [];
let rnUnique = []; // Liste unique des RN (pas de doublons par segments)
let lalanaData = [];
let simbaData = [];
let activeSimbaId = null;

/* =====================
 3️⃣ Fonctions utilitaires
===================== */
function calculateDistance(lat1, lon1, lat2, lon2) {
    const R = 6371; // Rayon de la Terre en km
    const dLat = (lat2 - lat1) * Math.PI / 180;
    const dLon = (lon2 - lon1) * Math.PI / 180;
    const a = Math.sin(dLat/2) * Math.sin(dLat/2) +
              Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
              Math.sin(dLon/2) * Math.sin(dLon/2);
    const c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
    return R * c; // Distance en km
}

function getPositionOnRoute(rnName, pkKm, rnDataArray) {
    // Trouver la géométrie de la RN
    const rnSegments = rnDataArray.filter(r => r.nom === rnName);
    if (rnSegments.length === 0) return null;
    
    // Pour simplifier, on prend le premier segment et on interpole
    const firstSegment = rnSegments[0];
    const geometry = typeof firstSegment.geometry === "string" 
        ? JSON.parse(firstSegment.geometry) 
        : firstSegment.geometry;
        
    if (geometry.type === 'LineString' && geometry.coordinates.length >= 2) {
        const coords = geometry.coordinates;
        
        // Calculer la distance totale de ce segment
        let totalDistance = 0;
        for (let i = 1; i < coords.length; i++) {
            totalDistance += calculateDistance(
                coords[i-1][1], coords[i-1][0],
                coords[i][1], coords[i][0]
            );
        }
        
        // Interpoler la position à pkKm
        const targetDistance = pkKm;
        let accumulatedDistance = 0;
        
        for (let i = 1; i < coords.length; i++) {
            const segmentDistance = calculateDistance(
                coords[i-1][1], coords[i-1][0],
                coords[i][1], coords[i][0]
            );
            
            if (accumulatedDistance + segmentDistance >= targetDistance) {
                // Interpoler entre coords[i-1] et coords[i]
                const ratio = (targetDistance - accumulatedDistance) / segmentDistance;
                const lat = coords[i-1][1] + (coords[i][1] - coords[i-1][1]) * ratio;
                const lng = coords[i-1][0] + (coords[i][0] - coords[i-1][0]) * ratio;
                return [lat, lng];
            }
            
            accumulatedDistance += segmentDistance;
        }
        
        // Si on dépasse, retourner le dernier point
        const lastCoord = coords[coords.length - 1];
        return [lastCoord[1], lastCoord[0]];
    }
    
    return null;
}

/* =====================
 4️⃣ Charger données
===================== */
Promise.all([
    fetch('../rn').then(res => res.json()),
    fetch('../lalana').then(res => res.json()),
    fetch('../simba').then(res => res.json())
]).then(([rn, lalana, simba]) => {
    // Traiter les RN
    rnData = rn
      .filter(r => r.geometry && r.id > 7)
      .map(r => ({
        ...r,
        ref: (r.ref && String(r.ref).trim()) || 'N/A'
      }));

    // Créer liste unique des RN
    rnUnique = [];
    const rnMap = {};
    
    rnData.forEach(rn => {
      const nom = rn.nom;
      if (!rnMap[nom]) {
        rnMap[nom] = {
          nom: rn.nom,
          id: rn.id,
          ref: rn.ref,
          refs: new Set()
        };
      }
      if (rn.ref && rn.ref !== 'N/A') {
        rnMap[nom].refs.add(rn.ref);
      }
    });
    
    rnUnique = Object.values(rnMap).map(rn => ({
      nom: rn.nom,
      id: rn.id,
      ref: rn.refs.size > 0 ? Array.from(rn.refs).join(', ') : 'N/A'
    }));
    
    rnUnique.sort((a, b) => a.nom.localeCompare(b.nom));

    lalanaData = lalana;
    simbaData = simba;

    afficherRN();
    remplirListe();
    remplirLalanaSelect();

    document.getElementById('stats').innerHTML =
      '✅ ' + rnUnique.length + ' route(s) - ' + lalanaData.length + ' lalana(s) - ' + simbaData.length + ' simba(s)';
      
}).catch(err => {
    document.getElementById('stats').innerHTML =
      '❌ Erreur de chargement';
    console.error(err);
});

/* =====================
 5️⃣ Affichage RN
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
      const rnName = feature.nom;
      if (!rnIndex[rnName]) {
        rnIndex[rnName] = [];
      }
      rnIndex[rnName].push(layer);

      layer.on('click', () => selectRN(rnName));
    }
  }).addTo(map);

  map.fitBounds(rnLayer.getBounds(), { padding: [40,40] });
}

/* =====================
 6️⃣ Affichage Simba
===================== */
function afficherSimbas() {
    if (simbaLayer) {
        map.removeLayer(simbaLayer);
    }
    
    simbaIndex = {};
    const simbaMarkers = [];
    
    simbaData.forEach(simba => {
        // Trouver la RN correspondante via Lalana
        const lalana = lalanaData.find(l => l.id === simba.idLalana);
        if (!lalana) return;
        
        // Trouver la position sur la route
        const position = getPositionOnRoute(lalana.nom, simba.pkDebut, rnData);
        if (!position) return;
        
        // Créer un marqueur rouge pour le Simba
        const marker = L.circleMarker(position, {
            color: '#ff0000',
            fillColor: '#ff0000',
            fillOpacity: 0.8,
            radius: 6,
            weight: 2
        }).bindPopup(
            '<div style="font-weight: bold; color: #dc3545;">' + simba.descriptions + '</div>' +
            '<div style="font-size: 12px; margin-top: 5px;">' +
            'PK: ' + simba.pkDebut + ' - ' + simba.pkFin + ' km<br>' +
            'Surface: ' + simba.surface + ' m²<br>' +
            'Profondeur: ' + simba.profondeur + ' cm<br>' +
            'Lalana: ' + lalana.nom +
            '</div>'
        );
        
        marker.on('click', () => selectSimba(simba.id));
        simbaMarkers.push(marker);
        simbaIndex[simba.id] = marker;
    });
    
    if (simbaMarkers.length > 0) {
        simbaLayer = L.layerGroup(simbaMarkers).addTo(map);
        document.getElementById('simbaCount').innerHTML = 
            simbaMarkers.length + ' Simba(s) affiché(s) en rouge';
    }
    
    remplirSimbaList();
}

function masquerSimbas() {
    if (simbaLayer) {
        map.removeLayer(simbaLayer);
        simbaLayer = null;
        document.getElementById('simbaCount').innerHTML = 'Simba masqués';
    }
    document.getElementById('simbaList').innerHTML = '';
}

/* =====================
 7️⃣ Listes et sélection
===================== */
function remplirListe() {
  const div = document.getElementById('listeRN');
  div.innerHTML = '';

  rnUnique.forEach(rn => {
    const item = document.createElement('div');
    item.className = 'rn-item';
    item.id = 'rn-' + rn.nom.replace(/[^a-zA-Z0-9]/g, '_');
    
    const refDisplay = (rn.ref && rn.ref !== 'false') ? rn.ref : 'N/A';
    
    item.innerHTML = '<div style="font-weight: bold; color: #8e44ad;">' + rn.nom + '</div>' +
                     '<div style="font-size: 12px; color: #666; margin-top: 3px;">Ref: ' + refDisplay + '</div>';
    item.onclick = function() { selectRN(rn.nom); };
    div.appendChild(item);
  });
}

function remplirLalanaSelect() {
    const select = document.getElementById('lalanaSelect');
    select.innerHTML = '<option value="">Sélectionner une Lalana...</option>';
    
    lalanaData.forEach(lalana => {
        const option = document.createElement('option');
        option.value = lalana.id;
        option.textContent = lalana.nom + ' (' + lalana.debut + ' → ' + lalana.fin + ')';
        select.appendChild(option);
    });
}

function remplirSimbaList() {
    const div = document.getElementById('simbaList');
    div.innerHTML = '';
    
    simbaData.forEach(simba => {
        const lalana = lalanaData.find(l => l.id === simba.idLalana);
        const item = document.createElement('div');
        item.className = 'simba-item';
        item.id = 'simba-' + simba.id;
        
        item.innerHTML = 
            '<div style="font-weight: bold;">' + simba.descriptions + '</div>' +
            '<div>PK: ' + simba.pkDebut + '-' + simba.pkFin + ' | ' + (lalana ? lalana.nom : 'Lalana inconnue') + '</div>';
        
        item.onclick = function() { selectSimba(simba.id); };
        div.appendChild(item);
    });
}

function selectRN(rnName) {
  document.querySelectorAll('.rn-item')
    .forEach(i => i.classList.remove('active'));

  const itemId = 'rn-' + rnName.replace(/[^a-zA-Z0-9]/g, '_');
  const item = document.getElementById(itemId);
  if (item) item.classList.add('active');

  Object.values(rnIndex).flat().forEach(l =>
    l.setStyle({ color:'#9b59b6', weight:3 })
  );

  if (!rnIndex[rnName]) return;

  rnIndex[rnName].forEach(l =>
    l.setStyle({ color:'#6c3483', weight:5 })
  );

  const group = L.featureGroup(rnIndex[rnName]);
  const bounds = group.getBounds();
  const padding = Math.min(150, 100);
  
  map.fitBounds(bounds, { 
    padding: [padding, padding],
    maxZoom: 15
  });
  
  console.log('📍 Affichage RN: ' + rnName + ' (' + rnIndex[rnName].length + ' segment(s))');
}

function selectSimba(simbaId) {
    // Désélectionner les autres
    document.querySelectorAll('.simba-item').forEach(i => i.classList.remove('active'));
    
    // Sélectionner celui-ci
    const item = document.getElementById('simba-' + simbaId);
    if (item) item.classList.add('active');
    
    // Centrer sur le simba
    if (simbaIndex[simbaId]) {
        map.setView(simbaIndex[simbaId].getLatLng(), 14);
        simbaIndex[simbaId].openPopup();
    }
    
    activeSimbaId = simbaId;
}

/* =====================
 8️⃣ Formulaire et événements
===================== */
document.getElementById('simbaForm').addEventListener('submit', function(e) {
    e.preventDefault();
    
    const formData = new FormData();
    formData.append('descriptions', document.getElementById('descriptions').value);
    formData.append('pkDebut', document.getElementById('pkDebut').value);
    formData.append('pkFin', document.getElementById('pkFin').value);
    formData.append('surface', document.getElementById('surface').value);
    formData.append('profondeur', document.getElementById('profondeur').value);
    formData.append('idLalana', document.getElementById('lalanaSelect').value);
    formData.append('idTypeMatiere', document.getElementById('idTypeMatiere').value);
    
    fetch('../simba', {
        method: 'POST',
        body: formData
    })
    .then(res => res.json())
    .then(data => {
        if (data.success) {
            alert('Simba ajouté avec succès !');
            document.getElementById('simbaForm').reset();
            // Recharger les simba
            fetch('../simba').then(res => res.json()).then(simbas => {
                simbaData = simbas;
                if (simbaLayer) afficherSimbas();
            });
        } else {
            alert('Erreur: ' + data.error);
        }
    })
    .catch(err => {
        alert('Erreur réseau: ' + err.message);
    });
});

document.getElementById('showAllSimbas').addEventListener('click', afficherSimbas);
document.getElementById('hideSimbas').addEventListener('click', masquerSimbas);

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
EOF

echo "✅ sig-enhanced.jsp créé"

echo ""
echo "🎉 INSTALLATION TERMINÉE !"
echo ""
echo "📋 Résumé des ajouts:"
echo "  • SimbaServlet pour API Simba (/simba)"
echo "  • LalanaServlet pour correspondance Lalana-RN (/lalana)"
echo "  • sig-enhanced.jsp avec interface complète"
echo "  • Fonction de calcul de distance intégrée"
echo "  • Formulaires d'ajout et de traçage"
echo "  • Affichage Simba en rouge sur la carte"
echo ""
echo "🔄 Pour utiliser:"
echo "  1. bash lance.sh"
echo "  2. Accédez à: http://localhost:8080/VoyageVoiture/jsp/sig-enhanced.jsp"
echo ""