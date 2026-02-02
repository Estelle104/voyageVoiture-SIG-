package service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Vector;

import dao.CarburantExcpetion;
import model.Lalana;
import model.Lavaka;
import model.Pause;
import model.SensVoyage;
import model.Voiture;

public class VoitureService {
    public static Duration calculerTempsDeVoyage(
            Lalana lalana,
            double vitesse,
            LocalDateTime heureDepart,
            SensVoyage sens) {

        if (vitesse <= 0)
            throw new IllegalArgumentException("Vitesse invalide");

        LocalDateTime heureCourante = heureDepart;
        double distanceParcourue = 0.0;
        double distanceTotale = lalana.getDistance();

        //  Gestion  lavakas 
        Vector<Lavaka> lavakas = lalana.getLavakas();
        if (lavakas != null && !lavakas.isEmpty()) {
            // Trier les lavakas par position sur la route
            lavakas.sort((a, b) -> Double.compare(
                    debutLavakaEffectif(a, lalana, sens),
                    debutLavakaEffectif(b, lalana, sens)));

            for (Lavaka lavaka : lavakas) {
                double debut = debutLavakaEffectif(lavaka, lalana, sens);
                double fin = finLavakaEffectif(lavaka, lalana, sens);

                // Temps pour la distance avant le lavaka
                double distanceAvant = debut - distanceParcourue;
                if (distanceAvant > 0) {
                    long minutesAvant = (long) Math.round((distanceAvant / vitesse) * 60);
                    heureCourante = heureCourante.plusMinutes(minutesAvant);
                }

                // llavaka avec ralentissement
                double longueurLavaka = fin - debut;
                double tauxRalentissement = lavaka.getTauxRalentissement() / 100.0;
                double vitesseLavaka = vitesse * (1 - tauxRalentissement);

                long minutesLavaka = (long) Math.round((longueurLavaka / vitesseLavaka) * 60);
                heureCourante = heureCourante.plusMinutes(minutesLavaka);

                distanceParcourue = fin;
            }
        }

        //  Distance restante apres tous les lavakas 
        double distanceRestante = distanceTotale - distanceParcourue;
        if (distanceRestante > 0) {
            long minutesRestantes = (long) Math.round((distanceRestante / vitesse) * 60);
            heureCourante = heureCourante.plusMinutes(minutesRestantes);
        }

        //  Gestion des pauses 
        Vector<Pause> pauses = lalana.getPauses();
        if (pauses != null && !pauses.isEmpty()) {
            // Trier les pauses par position effective
            pauses.sort((a, b) -> Double.compare(
                    positionEffectivePause(a, lalana, sens),
                    positionEffectivePause(b, lalana, sens)));

            for (Pause pause : pauses) {
                double pos = positionEffectivePause(pause, lalana, sens);

                // Temps depuis le depart jusqu'a la pause
                long minutesJusquaPause = (long) Math.round((pos / vitesse) * 60);
                LocalDateTime heureArriveePause = heureDepart.plusMinutes(minutesJusquaPause);

                // Ajouter la duree effective de la pause
                Duration pauseEffective = pause.calculerPauseEffective(heureArriveePause);
                heureCourante = heureCourante.plus(pauseEffective);
            }
        }

        return Duration.between(heureDepart, heureCourante);
    }

    public static Duration calculerTempsTotal(
            List<Lalana> chemin,
            double vitesse,
            LocalDateTime heureDepart,
            SensVoyage sens) {

        LocalDateTime heureCourante = heureDepart;

        for (Lalana l : chemin) {
            Duration d = calculerTempsDeVoyage(
                    l, vitesse, heureCourante, sens);

            heureCourante = heureCourante.plus(d);
        }

        return Duration.between(heureDepart, heureCourante);
    }

    public static void verifierCarburantSuffisant(
            List<Lalana> chemin,
            Voiture voiture) {

        if (voiture.getReservoire() <= 0) {
            throw new IllegalArgumentException("Reservoir invalide");
        }

        if (voiture.getConsommation() <= 0) {
            throw new IllegalArgumentException("Consommation invalide");
        }

        double distanceTotale = 0;

        for (Lalana l : chemin) {
            distanceTotale += l.getDistance();
        }

        double carburantNecessaire = (distanceTotale / 100.0) * voiture.getConsommation();

        if (carburantNecessaire > voiture.getReservoire()) {
            throw new CarburantExcpetion(
                    String.format(
                            "Carburant insuffisant car %.2f L necessaires or reservoir = %.2f L",
                            carburantNecessaire,
                            voiture.getReservoire()));
        }
    }

    public static double calculerCarburantNecessaire(
            Vector<Lalana> chemin,
            Voiture voiture) {

        double distance = LalanaService.calculerDistanceTotale(chemin);
        return (distance / 100.0) * voiture.getConsommation();
    }

    public static double debutLavakaEffectif(
            Lavaka lavaka,
            Lalana lalana,
            SensVoyage sens) {

        if (sens == SensVoyage.ALLER) {
            return lavaka.getDebutLavaka();
        }
        return lalana.getDistance() - lavaka.getFinLavaka();
    }

    public static double finLavakaEffectif(
            Lavaka lavaka,
            Lalana lalana,
            SensVoyage sens) {

        if (sens == SensVoyage.ALLER) {
            return lavaka.getFinLavaka();
        }
        return lalana.getDistance() - lavaka.getDebutLavaka();
    }

    public static double positionEffectivePause(
            Pause pause,
            Lalana lalana,
            SensVoyage sens) {

        if (sens == SensVoyage.ALLER) {
            return pause.getPosition();
        }
        return lalana.getDistance() - pause.getPosition();
    }
}
