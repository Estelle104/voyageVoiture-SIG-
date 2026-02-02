package model;

public class ConfPrix {
    int id;
    double min;
    double max;
    double prixMC;
    int idTypeMatiere;

    public ConfPrix( double min, double max, double prixMC, int idTypeMatiere) {
        this.min = min;
        this.max = max;
        this.prixMC = prixMC;
        this.idTypeMatiere = idTypeMatiere;
    }
    public ConfPrix() {
    }
    
    public double getMin() {
        return min;
    }
    public void setMin(double min) {
        this.min = min;
    }
    public double getMax() {
        return max;
    }
    public void setMax(double max) {
        this.max = max;
    }
    public double getPrixMC() {
        return prixMC;
    }
    public void setPrixMC(double prixMC) {
        this.prixMC = prixMC;
    }
    public int getIdTypeMatiere() {
        return idTypeMatiere;
    }
    public void setIdTypeMatiere(int idTypeMatiere) {
        this.idTypeMatiere = idTypeMatiere;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    
}
