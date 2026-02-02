package sig;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;

import java.io.*;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.*;

import dao.*;
import model.*;
import utildb.ConnexionOracle;

/**
 * Serveur HTTP pour l'API SIG
 * Fournit les endpoints pour la carte interactive
 */
public class SIGServer {
    
    private HttpServer server;
    private ConnexionOracle conn;
    private int port;
    
    public SIGServer(ConnexionOracle conn, int port) throws IOException {
        this.conn = conn;
        this.port = port;
        
        server = HttpServer.create(new InetSocketAddress("localhost", port), 0);
        
        // Routes
        server.createContext("/", new FileHandler());
        server.createContext("/api/rn/all", new RNListHandler());
        server.createContext("/api/simba/byRN/", new SimbaByRNHandler());
        
        server.setExecutor(null);
    }
    
    public void start() {
        server.start();
        System.out.println("🗺️  Serveur SIG démarré sur http://localhost:" + port);
    }
    
    public void stop() {
        server.stop(0);
        System.out.println("🗺️  Serveur SIG arrêté");
    }
    
    // === Handlers ===
    
    /**
     * Servir les fichiers statiques (HTML, CSS, JS)
     */
    class FileHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            
            // Mapper les URLs vers les fichiers
            if (path.equals("/")) {
                path = "/map.html";
            }
            
            // Charger le fichier
            String filePath = "sig" + path;
            File file = new File(filePath);
            
            if (!file.exists()) {
                send404(exchange);
                return;
            }
            
            byte[] bytes = Files.readAllBytes(file.toPath());
            
            String mimeType = getMimeType(path);
            exchange.getResponseHeaders().set("Content-Type", mimeType);
            exchange.sendResponseHeaders(200, bytes.length);
            
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(bytes);
            }
        }
        
        private String getMimeType(String path) {
            if (path.endsWith(".html")) return "text/html";
            if (path.endsWith(".css")) return "text/css";
            if (path.endsWith(".js")) return "application/javascript";
            if (path.endsWith(".json")) return "application/json";
            if (path.endsWith(".geojson")) return "application/geo+json";
            return "text/plain";
        }
        
        private void send404(HttpExchange exchange) throws IOException {
            String response = "404 - Fichier non trouvé";
            exchange.sendResponseHeaders(404, response.getBytes().length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
    
    /**
     * API: GET /api/rn/all
     * Retourne la liste de toutes les RN
     */
    class RNListHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCORSHeaders(exchange);
            
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
                return;
            }
            
            try {
                Vector<Lalana> lalanas = LalanaDAO.getAll(conn);
                String json = buildRNJSON(lalanas);
                sendJSON(exchange, json);
            } catch (Exception e) {
                e.printStackTrace();
                sendError(exchange, "Erreur: " + e.getMessage());
            }
        }
        
        private String buildRNJSON(Vector<Lalana> lalanas) {
            StringBuilder json = new StringBuilder("[");
            
            for (int i = 0; i < lalanas.size(); i++) {
                Lalana lalana = lalanas.get(i);
                
                if (i > 0) json.append(",");
                
                json.append("{");
                json.append("\"id\":").append(lalana.getId()).append(",");
                json.append("\"name\":\"").append(escapeJSON(lalana.getNomLalana())).append("\",");
                json.append("\"debut\":\"").append(escapeJSON(lalana.getDebut())).append("\",");
                json.append("\"fin\":\"").append(escapeJSON(lalana.getFin())).append("\",");
                json.append("\"distance\":").append(lalana.getDistance()).append(",");
                json.append("\"geometry\":{");
                json.append("\"type\":\"LineString\",");
                json.append("\"coordinates\":[[46.0,-18.0],[47.0,-19.0]]");
                json.append("}");
                json.append("}");
            }
            
            json.append("]");
            return json.toString();
        }
    }
    
    /**
     * API: GET /api/simba/byRN/{rnId}
     * Retourne les Simba d'une RN
     */
    class SimbaByRNHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            setCORSHeaders(exchange);
            
            if (exchange.getRequestMethod().equals("OPTIONS")) {
                exchange.sendResponseHeaders(200, 0);
                exchange.close();
                return;
            }
            
            try {
                String path = exchange.getRequestURI().getPath();
                String rnIdStr = path.replace("/api/simba/byRN/", "");
                
                if (rnIdStr.isEmpty()) {
                    sendError(exchange, "ID RN manquant");
                    return;
                }
                
                int rnId = Integer.parseInt(rnIdStr);
                
                Vector<Simba> simbas = SimbaDAO.getByIdLalana(conn, rnId);
                String json = buildSimbaJSON(simbas);
                sendJSON(exchange, json);
            } catch (Exception e) {
                e.printStackTrace();
                sendError(exchange, "Erreur: " + e.getMessage());
            }
        }
        
        private String buildSimbaJSON(Vector<Simba> simbas) {
            StringBuilder json = new StringBuilder("[");
            
            for (int i = 0; i < simbas.size(); i++) {
                Simba simba = simbas.get(i);
                
                if (i > 0) json.append(",");
                
                // Coordonnées basées sur pkDebut
                double latitude = -18.0 + ((simba.getPkDebut() % 10) * 0.1);
                double longitude = 46.0 + ((simba.getPkFin() % 10) * 0.1);
                
                json.append("{");
                json.append("\"id\":").append(simba.getId()).append(",");
                json.append("\"descriptions\":\"").append(escapeJSON(simba.getDescriptions())).append("\",");
                json.append("\"pkDebut\":").append(simba.getPkDebut()).append(",");
                json.append("\"pkFin\":").append(simba.getPkFin()).append(",");
                json.append("\"tauxRalentissement\":").append(simba.getTauxRalentissement()).append(",");
                json.append("\"surface\":").append(simba.getSurface()).append(",");
                json.append("\"profondeur\":").append(simba.getProfondeur()).append(",");
                json.append("\"latitude\":").append(latitude).append(",");
                json.append("\"longitude\":").append(longitude);
                json.append("}");
            }
            
            json.append("]");
            return json.toString();
        }
    }
    
    // === Utilitaires ===
    
    private void setCORSHeaders(HttpExchange exchange) {
        exchange.getResponseHeaders().set("Access-Control-Allow-Origin", "*");
        exchange.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, OPTIONS");
        exchange.getResponseHeaders().set("Access-Control-Allow-Headers", "Content-Type");
    }
    
    private void sendJSON(HttpExchange exchange, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json; charset=utf-8");
        exchange.sendResponseHeaders(200, bytes.length);
        try (OutputStream os = exchange.getResponseBody()) {
            os.write(bytes);
        }
    }
    
    private void sendError(HttpExchange exchange, String message) throws IOException {
        String json = "{\"error\":\"" + escapeJSON(message) + "\"}";
        sendJSON(exchange, json);
    }
    
    private static String escapeJSON(String str) {
        if (str == null) return "";
        return str.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}
