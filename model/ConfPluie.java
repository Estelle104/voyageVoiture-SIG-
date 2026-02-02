package model;

public class ConfPluie {
    int id;
    double minP;
    double maxP;
    int idTypeMatiere;
    
    public ConfPluie() {
    }
    
    public ConfPluie(double minP, double maxP, int idTypeMatiere) {
        this.minP = minP;
        this.maxP = maxP;
        this.idTypeMatiere = idTypeMatiere;
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public double getMinP() {
        return minP;
    }
    public void setMinP(double minP) {
        this.minP = minP;
    }
    public double getMaxP() {
        return maxP;
    }
    public void setMaxP(double maxP) {
        this.maxP = maxP;
    }
    public int getIdTypeMatiere() {
        return idTypeMatiere;
    }
    public void setIdTypeMatiere(int idTypeMatiere) {
        this.idTypeMatiere = idTypeMatiere;
    }

    
}
