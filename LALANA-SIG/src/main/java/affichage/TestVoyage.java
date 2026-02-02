package affichage;

import java.time.*;
import java.util.*;

import model.*;
import service.*;

public class TestVoyage {

    public static void main(String[] args) {

        // === LAVAKAS ===
        Lavaka lavaka1 = new Lavaka(1, 20, 30, 50);
        Lavaka lavaka2 = new Lavaka(2, 70, 80, 30);

        // === PAUSES ===
        Pause pause1 = new Pause(
                1, "Pause café", 1,
                LocalDateTime.of(2026,1,13,9,20),
                LocalDateTime.of(2026,1,13,9,40),
                25
            );
            

        Pause pause2 = new Pause(
                2,
                "Pause carburant",
                1,
                LocalDateTime.of(2026, 1, 13, 11, 0),
                LocalDateTime.of(2026, 1, 13, 11, 20),
                75
        );

            
        // === LALANA ===
        Lalana lalana = new Lalana();
        lalana.setId(1);
        lalana.setNomLalana("RN1");
        lalana.setDebut("A");
        lalana.setFin("B");
        lalana.setDistance(100);

        Vector<Lavaka> lavakas = new Vector<>();
        lavakas.add(lavaka1);
        lavakas.add(lavaka2);
        lalana.setLavakas(lavakas);

        Vector<Pause> pauses = new Vector<>();
        pauses.add(pause1);
        pauses.add(pause2);
        lalana.setPauses(pauses);

        // === CHEMIN ===
        List<Lalana> chemin = new ArrayList<>();
        chemin.add(lalana);

        double vitesse = 60;
        LocalDateTime depart = LocalDateTime.of(2026, 1, 13, 9, 0);

        // === TEST ALLER ===
        Duration dureeAller = VoitureService.calculerTempsTotal(
                chemin, vitesse, depart, SensVoyage.ALLER);

        LocalDateTime arriveeAller = depart.plus(dureeAller);

        System.out.println("=== ALLER ===");
        System.out.println("Départ : " + depart);
        System.out.println("Arrivée prévue : " + arriveeAller);
        afficherDuree(dureeAller);

        // === TEST RETOUR ===
        Duration dureeRetour = VoitureService.calculerTempsTotal(
                chemin, vitesse, depart, SensVoyage.RETOUR);

        LocalDateTime arriveeRetour = depart.plus(dureeRetour);

        System.out.println("\n=== RETOUR ===");
        System.out.println("Départ : " + depart);
        System.out.println("Arrivée prévue : " + arriveeRetour);
        afficherDuree(dureeRetour);
    }

    private static void afficherDuree(Duration d) {
        long h = d.toHours();
        long m = d.toMinutes() % 60;
        System.out.println("Durée : " + h + "h " + m + "m");
    }
}
