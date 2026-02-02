package model;

public class LalanaVoyage {
    Lalana lalana;
    double pkDebutGlobal;
    double pkFinGlobal;

    public LalanaVoyage(Lalana lalana, double pkDebutGlobal, double pkFinGlobal) {
        this.lalana = lalana;
        this.pkDebutGlobal = pkDebutGlobal;
        this.pkFinGlobal = pkFinGlobal;
    }
    
    public LalanaVoyage() {
    }
    public Lalana getLalana() {
        return lalana;
    }
    public void setLalana(Lalana lalana) {
        this.lalana = lalana;
    }
    public double getPkDebutGlobal() {
        return pkDebutGlobal;
    }
    public void setPkDebutGlobal(double pkDebutGlobal) {
        this.pkDebutGlobal = pkDebutGlobal;
    }
    public double getPkFinGlobal() {
        return pkFinGlobal;
    }
    public void setPkFinGlobal(double pkFinGlobal) {
        this.pkFinGlobal = pkFinGlobal;
    }

}
