package model;

public class Simba {

    private int id;
    private String descriptions;

    private double pkDebut;
    private double pkFin;
    private double tauxRalentissement;

    private double surface;
    private double profondeur;

    private int idLalana;
    private Integer idLavaka; // nullable
    private int idTypeMatiere;

    // --- Getters / Setters ---

    public Simba() {
    }

    public Simba(String descriptions, double pkDebut, double pkFin, double tauxRalentissement, double surface,
            double profondeur, int idLalana, Integer idLavaka, int idTypeMatiere) {
        this.descriptions = descriptions;
        this.pkDebut = pkDebut;
        this.pkFin = pkFin;
        this.tauxRalentissement = tauxRalentissement;
        this.surface = surface;
        this.profondeur = profondeur;
        this.idLalana = idLalana;
        this.idLavaka = idLavaka;
        this.idTypeMatiere = idTypeMatiere;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
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

    public double getTauxRalentissement() {
        return tauxRalentissement;
    }

    public void setTauxRalentissement(double tauxRalentissement) {
        this.tauxRalentissement = tauxRalentissement;
    }

    public double getSurface() {
        return surface;
    }

    public void setSurface(double surface) {
        this.surface = surface;
    }

    public double getProfondeur() {
        return profondeur;
    }

    public void setProfondeur(double profondeur) {
        this.profondeur = profondeur;
    }

    public int getIdLalana() {
        return idLalana;
    }

    public void setIdLalana(int idLalana) {
        this.idLalana = idLalana;
    }

    public Integer getIdLavaka() {
        return idLavaka;
    }

    public void setIdLavaka(Integer idLavaka) {
        this.idLavaka = idLavaka;
    }

    public int getIdTypeMatiere() {
        return idTypeMatiere;
    }

    public void setIdTypeMatiere(int idTypeMatiere) {
        this.idTypeMatiere = idTypeMatiere;
    }

    @Override
    public String toString() {
        return descriptions + " (PK: " + pkDebut + " - " + pkFin + ")";
    }
}
