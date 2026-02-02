package sig;

/**
 * Utilitaires pour la gestion des coordonnées géographiques
 */
public class CoordinatesUtil {
    
    // Coordonnées Madagascar
    public static final double MADAGASCAR_LAT = -18.8792;
    public static final double MADAGASCAR_LON = 46.8696;
    
    // Limites Madagascar
    public static final double MIN_LAT = -25.6;
    public static final double MAX_LAT = -11.95;
    public static final double MIN_LON = 43.2;
    public static final double MAX_LON = 50.5;
    
    /**
     * Générer une paire de coordonnées basée sur un point kilométrique
     * Utile pour afficher les Simba
     */
    public static double[] getCoordinatesFromPK(double pkDebut, double pkFin, double rn_id) {
        // Simuler une progression linéaire le long d'une RN
        double lat = MADAGASCAR_LAT + (Math.sin(rn_id) * (pkDebut / 1000.0));
        double lon = MADAGASCAR_LON + (Math.cos(rn_id) * (pkFin / 1000.0));
        
        // Limiter aux limites Madagascar
        lat = Math.max(MIN_LAT, Math.min(MAX_LAT, lat));
        lon = Math.max(MIN_LON, Math.min(MAX_LON, lon));
        
        return new double[]{lat, lon};
    }
    
    /**
     * Calculer la distance entre deux points (formule Haversine approximée)
     */
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Rayon terrestre en km
        
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        
        return R * c;
    }
    
    /**
     * Vérifier si des coordonnées sont dans Madagascar
     */
    public static boolean isInMadagascar(double lat, double lon) {
        return lat >= MIN_LAT && lat <= MAX_LAT && lon >= MIN_LON && lon <= MAX_LON;
    }
}
