package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Pause {
    int id;
    String nom;
    int idLalana;
    LocalDateTime heureDebut;
    LocalDateTime heureFin;
    double position;

    public Pause(int id, String nom, int idLalana, LocalDateTime heureDebut, LocalDateTime heureFin, double position) {
        this.id = id;
        this.nom = nom;
        this.idLalana = idLalana;
        this.heureDebut = heureDebut;
        this.heureFin = heureFin;
        this.position = position;
    }

    public Pause() {
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

    public int getIdLalana() {
        return idLalana;
    }

    public void setIdLalana(int idLalana) {
        this.idLalana = idLalana;
    }

    public LocalDateTime getHeureDebut() {
        return heureDebut;
    }

    public void setHeureDebut(LocalDateTime heureDebut) {
        this.heureDebut = heureDebut;
    }

    public LocalDateTime getHeureFin() {
        return heureFin;
    }

    public void setHeureFin(LocalDateTime heureFin) {
        this.heureFin = heureFin;
    }

    public double getPosition() {
        return position;
    }

    public void setPosition(double position) {
        this.position = position;
    }

    public Duration getDuree() {
        return Duration.between(heureDebut, heureFin);
    }

    // --- Version formatée en heures et minutes ---
    public String getDureeFormatee() {
        Duration duree = getDuree();
        long heures = duree.toHours();
        long minutes = duree.toMinutes() % 60;
        return heures + "h "
                + minutes + "m";
    }

    public Duration calculerPauseEffective(LocalDateTime heureArrivee) {
        if (heureArrivee.isBefore(heureDebut)) {
            return Duration.ZERO;
        }
        if (!heureArrivee.isBefore(heureFin)) {
            return Duration.ZERO;
        }
        return Duration.between(heureArrivee, heureFin);
    }
    
    // Pause p = new Pause();
    // p.heureDebut = LocalDateTime.of(2026, 1, 13, 9, 0);
    // p.heureFin = LocalDateTime.of(2026, 1, 13, 11, 30);
    // System.out.println("Durée brute en minutes : " + p.getDuree().toMinutes());
    // System.out.println("Durée formatée : " + p.getDureeFormatee());

}
