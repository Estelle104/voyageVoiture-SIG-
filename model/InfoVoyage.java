package model;

import java.time.LocalDateTime;
import java.util.Vector;

public class InfoVoyage {
    int id;
    String depart;
    String destination;
    Vector<Lalana> lalanaDispo;
    String voiture;
    double tempsVoyage;
    double vitesseMoyenne;
    LocalDateTime dateDebut;
    LocalDateTime dateFin;
    double coutTotal;

    public InfoVoyage(String depart, String destination, Vector<Lalana> lalanaDispo, String voiture, double tempsVoyage,
            double vitesseMoyenne, LocalDateTime dateDebut, LocalDateTime dateFin, double coutTotal) {
        this.depart = depart;
        this.destination = destination;
        this.lalanaDispo = lalanaDispo;
        this.voiture = voiture;
        this.tempsVoyage = tempsVoyage;
        this.vitesseMoyenne = vitesseMoyenne;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.coutTotal = coutTotal;
    }

    public InfoVoyage(String depart, String destination, String voiture, double tempsVoyage, double vitesseMoyenne) {
        this.depart = depart;
        this.destination = destination;
        this.voiture = voiture;
        this.tempsVoyage = tempsVoyage;
        this.vitesseMoyenne = vitesseMoyenne;
        this.lalanaDispo = new Vector<>();
    }

    public InfoVoyage() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDepart() {
        return depart;
    }

    public void setDepart(String depart) {
        this.depart = depart;
    }

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getvoiture() {
        return voiture;
    }

    public void setvoiture(String voiture) {
        this.voiture = voiture;
    }

    public double getTempsVoyage() {
        return tempsVoyage;
    }

    public void setTempsVoyage(double tempsVoyage) {
        this.tempsVoyage = tempsVoyage;
    }

    public double getVitesseMoyenne() {
        return vitesseMoyenne;
    }

    public void setVitesseMoyenne(double vitesseMoyenne) {
        this.vitesseMoyenne = vitesseMoyenne;
    }

    public void setLalanaDispo(Vector<Lalana> lalanaDispo) {
        this.lalanaDispo = lalanaDispo;
    }

    public Vector<Lalana> getLalanaDispo() {
        return this.lalanaDispo;
    }

    public String getVoiture() {
        return voiture;
    }

    public void setVoiture(String voiture) {
        this.voiture = voiture;
    }

    public LocalDateTime getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDateTime dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDateTime getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDateTime dateFin) {
        this.dateFin = dateFin;
    }

    public double getCoutTotal() {
        return coutTotal;
    }

    public void setCoutTotal(double coutTotal) {
        this.coutTotal = coutTotal;
    }

    @Override
    public String toString() {
        return depart + " -> " + destination;
    }

}
