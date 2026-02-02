package model;

import java.util.Vector;

public class Lalana {
    int id;
    String nomLalana;
    double distance;
    String debut;
    String fin;
    double largeur;

    private Vector<Lavaka> lavakas;
    private Vector<Pause> pauses;

    public Lalana(int id, String nomLalana, double distance, String debut, String fin) {
        this.id = id;
        this.nomLalana = nomLalana;
        this.distance = distance;
        this.debut = debut;
        this.fin = fin;
    }

    public Lalana() {
    }

    public double getLargeur() {
        return largeur;
    }

    public void setLargeur(double largeur) {
        this.largeur = largeur;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNomLalana() {
        return nomLalana;
    }

    public void setNomLalana(String nomLalana) {
        this.nomLalana = nomLalana;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getDebut() {
        return debut;
    }

    public void setDebut(String debut) {
        this.debut = debut;
    }

    public String getFin() {
        return fin;
    }

    public void setFin(String fin) {
        this.fin = fin;
    }

    public Vector<Lavaka> getLavakas() {
        if (lavakas == null) {
            lavakas = new Vector<>();
        }
        return lavakas;
    }

    public void setLavakas(Vector<Lavaka> allOwnLavakas) {
        this.lavakas = allOwnLavakas;
    }

    public Vector<Pause> getPauses() {
        if (pauses == null) {
            pauses = new Vector<>();
        }
        return pauses;
    }

    public void setPauses(Vector<Pause> pauses) {
        this.pauses = pauses;
    }

    @Override
    public String toString() {
        return nomLalana;
    }

 

}
