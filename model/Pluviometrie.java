package model;

public class Pluviometrie {
    int id;
    double tauxPluie;
    double pkDebut;
    double pkFin;
    
    public Pluviometrie() {
    }
    public Pluviometrie(double tauxPluie, double pkDebut, double pkFin) {
        this.tauxPluie = tauxPluie;
        this.pkDebut = pkDebut;
        this.pkFin = pkFin;
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public double getTauxPluie() {
        return tauxPluie;
    }
    public void setTauxPluie(double tauxPluie) {
        this.tauxPluie = tauxPluie;
    }
    public double getPkDebut() {
        return pkDebut;
    }
    public void setPkDebut(double pkDebut) {
        this.pkDebut = pkDebut;
    }
    public double getPkFin() {
        return pkFin;
    }
    public void setPkFin(double pkFin) {
        this.pkFin = pkFin;
    }
}
