package model;

public class PrixSimba {
    int id;
    int idSimba;
    int idLalana;
    double prixSimba;

    public PrixSimba(int idSimba, int idLalana, double prixSimba) {
        this.idSimba = idSimba;
        this.idLalana = idLalana;
        this.prixSimba = prixSimba;
    }
    public PrixSimba() {
    }
    
    public int getId() {
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getIdSimba() {
        return idSimba;
    }
    public void setIdSimba(int idSimba) {
        this.idSimba = idSimba;
    }
    public int getIdLalana() {
        return idLalana;
    }
    public void setIdLalana(int idLalana) {
        this.idLalana = idLalana;
    }
    public double getPrixSimba() {
        return prixSimba;
    }
    public void setPrixSimba(double prixSimba) {
        this.prixSimba = prixSimba;
    }
    
    @Override
    public String toString() {
        return "PrixSimba{" +
                "id=" + id +
                ", idSimba=" + idSimba +
                ", idLalana=" + idLalana +
                ", prixSimba=" + prixSimba +
                '}';
    }
}
