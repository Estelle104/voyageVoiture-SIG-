package model;

public class Voiture{
    int id;
    String nom;
    String type;
    double vitesseMax;
    double longueur;
    double largeur;
    double reservoire;
    double consommation;

    public Voiture() {
    }
    public Voiture(int id, String nom, String type, double vitesseMax, double longueur, double largeur) {
        this.id = id;
        this.nom = nom;
        this.type = type;
        this.vitesseMax = vitesseMax;
        this.longueur = longueur;
        this.largeur = largeur;
    }
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
    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public double getVitesseMax() {
        return vitesseMax;
    }
    public void setVitesseMax(double vitesseMax) {
        this.vitesseMax = vitesseMax;
    }
    public double getLongueur() {
        return longueur;
    }
    public void setLongueur(double longueur) {
        this.longueur = longueur;
    }
    public double getLargeur() {
        return largeur;
    }
    public void setLargeur(double largeur) {
        this.largeur = largeur;
    }
    public double getReservoire() {
        return reservoire;
    }
    public void setReservoire(double reservoire) {
        this.reservoire = reservoire;
    }
    public double getConsommation() {
        return consommation;
    }
    public void setConsommation(double consommation) {
        this.consommation = consommation;
    }

    
}