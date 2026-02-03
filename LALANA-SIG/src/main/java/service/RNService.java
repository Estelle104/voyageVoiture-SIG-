package service;

import java.util.List;
import java.util.Map;

import org.w3c.dom.Node;

import sig.Point;

import java.util.ArrayList;

public class RNService {
    public boolean isRN(Map<String, String> tags) {
        if (tags == null)
            return false;

        String ref = tags.get("ref"); // RN7, RN2, etc.
        String highway = tags.get("highway"); // primary, trunk

        if (ref == null || highway == null)
            return false;

        return ref.startsWith("RN");
    }

    public List<double[]> extractCoordinates(List<Object> nodes) {
        List<double[]> points = new ArrayList<>();
        // Implémentation simplifiée
        return points;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        final double R = 6371000; // rayon Terre en mètres

        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);

        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2)
                + Math.cos(Math.toRadians(lat1))
                        * Math.cos(Math.toRadians(lat2))
                        * Math.sin(dLon / 2) * Math.sin(dLon / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

        return R * c;
    }

    public static double calculDistanceRN(List<Point> points) {
        double total = 0;

        for (int i = 0; i < points.size() - 1; i++) {
            total += distance(
                    points.get(i).lat, points.get(i).lon,
                    points.get(i + 1).lat, points.get(i + 1).lon);
        }

        return total; // en mètres
    }

}
