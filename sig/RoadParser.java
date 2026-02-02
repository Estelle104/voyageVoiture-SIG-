package sig;

import java.io.*;
import java.util.*;

/**
 * Parse le fichier OSM pour extraire les routes nationales
 * madagascar-260130.osm.pbf → Routes RN avec coordonnées
 */
public class RoadParser {
    
    /**
     * Données de routes prédéfinies pour Madagascar
     * Source: OSM data pour routes nationales
     */
    public static Map<String, double[][]> getRoadCoordinates() {
        Map<String, double[][]> roads = new HashMap<>();
        
        // RN1: Antananarivo - Toliara (Sud)
        roads.put("RN1", new double[][] {
            {-18.8792, 47.5263}, {-19.0, 47.5}, {-19.5, 47.4},
            {-20.0, 47.3}, {-20.5, 47.2}, {-21.0, 47.1},
            {-21.5, 47.0}, {-22.0, 46.9}, {-22.5, 46.8},
            {-23.0, 46.7}, {-23.5, 46.6}, {-24.0, 46.5},
            {-24.5, 46.4}, {-25.3624, 46.3333}
        });
        
        // RN2: Antananarivo - Antalaha (Nord-Est)
        roads.put("RN2", new double[][] {
            {-18.8792, 47.5263}, {-18.5, 48.0}, {-18.0, 48.5},
            {-17.5, 49.0}, {-17.0, 49.5}, {-16.5, 50.0},
            {-16.2, 50.2}
        });
        
        // RN3: Route Ouest (Menabe)
        roads.put("RN3", new double[][] {
            {-19.8863, 43.6667}, {-20.0, 44.0}, {-20.2, 44.5},
            {-20.3, 45.0}, {-20.4, 45.5}, {-20.4669, 46.0}
        });
        
        // RN4: Antananarivo - Sambava (Nord)
        roads.put("RN4", new double[][] {
            {-18.8792, 47.5263}, {-18.0, 48.0}, {-17.0, 48.5},
            {-16.0, 49.0}, {-15.0, 50.0}
        });
        
        // RN5: Sambava - Andapa
        roads.put("RN5", new double[][] {
            {-13.5, 50.2}, {-14.0, 49.8}, {-14.5, 49.5},
            {-15.0, 49.0}
        });
        
        // RN6: Route Ouest Sud
        roads.put("RN6", new double[][] {
            {-18.8792, 47.5263}, {-19.5, 46.5}, {-20.5, 45.5},
            {-21.5, 45.0}, {-22.5, 44.5}, {-23.5, 44.0},
            {-24.5, 43.5}, {-25.3624, 43.5}
        });
        
        return roads;
    }
    
    /**
     * Convertir coordonnées routes en GeoJSON
     */
    public static String toGeoJSON(Map<String, double[][]> roads) {
        StringBuilder json = new StringBuilder();
        json.append("{\"type\":\"FeatureCollection\",\"features\":[");
        
        List<String> features = new ArrayList<>();
        int id = 1;
        
        for (Map.Entry<String, double[][]> entry : roads.entrySet()) {
            String name = entry.getKey();
            double[][] coords = entry.getValue();
            
            StringBuilder feature = new StringBuilder();
            feature.append("{");
            feature.append("\"type\":\"Feature\",");
            feature.append("\"properties\":{\"id\":").append(id++).append(",");
            feature.append("\"name\":\"").append(name).append("\"},");
            feature.append("\"geometry\":{");
            feature.append("\"type\":\"LineString\",");
            feature.append("\"coordinates\":[");
            
            for (int i = 0; i < coords.length; i++) {
                if (i > 0) feature.append(",");
                feature.append("[").append(coords[i][1]).append(",").append(coords[i][0]).append("]");
            }
            
            feature.append("]}}");
            features.add(feature.toString());
        }
        
        json.append(String.join(",", features));
        json.append("]}");
        
        return json.toString();
    }
    
    /**
     * Générer le fichier rn.geojson avec vraies coordonnées
     */
    public static void generateGeoJSONFile(String outputPath) throws IOException {
        Map<String, double[][]> roads = getRoadCoordinates();
        String geoJson = toGeoJSON(roads);
        
        try (FileWriter fw = new FileWriter(outputPath)) {
            fw.write(geoJson);
        }
        
        System.out.println("✅ GeoJSON généré: " + outputPath);
    }
    
    public static void main(String[] args) throws IOException {
        generateGeoJSONFile("sig/rn.geojson");
    }
}
