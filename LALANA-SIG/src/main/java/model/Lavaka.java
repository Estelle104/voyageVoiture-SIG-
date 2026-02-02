package model;

public class Lavaka {
    int id;
    int idLalana;
    double debutLavaka;
    double finLavaka;
    double tauxRalentissement;
    
    public Lavaka(int idLalana, double debutLavaka, double finLavaka, double tauxRalentissement) {
        this.idLalana = idLalana;
        this.debutLavaka = debutLavaka;
        this.finLavaka = finLavaka;
        this.tauxRalentissement = tauxRalentissement;
    }
    public Lavaka(int id, int idLalana, double debutLavaka, double finLavaka, double tauxRalentissement) {
        this.id = id;
        this.idLalana = idLalana;
        this.debutLavaka = debutLavaka;
        this.finLavaka = finLavaka;
        this.tauxRalentissement = tauxRalentissement;
    }
    public Lavaka() {
    }
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getIdLalana() {
        return idLalana;
    }
    public void setIdLalana(int idLalana) {
        this.idLalana = idLalana;
    }
    public double getDebutLavaka() {
        return debutLavaka;
    }
    public void setDebutLavaka(double debutLavaka) {
        this.debutLavaka = debutLavaka;
    }
    public double getFinLavaka() {
        return finLavaka;
    }
    public void setFinLavaka(double finLavaka) {
        this.finLavaka = finLavaka;
    }
    public double getTauxRalentissement() {
        return tauxRalentissement;
    }
    public void setTauxRalentissement(double tauxRalentissement) {
        this.tauxRalentissement = tauxRalentissement;
    }
}
