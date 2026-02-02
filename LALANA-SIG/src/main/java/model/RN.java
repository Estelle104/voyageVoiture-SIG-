package model;

public class RN {

    private int id;
    private String nom;

    // on stocke la géométrie en GeoJSON (simple pour Leaflet)
    private String geoJson;

    // --- constructeurs ---
    public RN() {}

    public RN( String nom, String geoJson) {
        this.nom = nom;
        this.geoJson = geoJson;
    }

    // --- getters & setters ---
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getGeoJson() {
        return geoJson;
    }

    public void setGeoJson(String geoJson) {
        this.geoJson = geoJson;
    }
}
