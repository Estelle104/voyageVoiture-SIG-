package service;

import java.util.HashSet;
import java.util.Vector;

import model.InfoVoyage;
import model.Lalana;
import model.LalanaVoyage;
import model.Lavaka;

public class InfoVoyageService {

    public static Vector<LalanaVoyage> calculerPkGlobaux(Vector<Lalana> chemin) {

        Vector<LalanaVoyage> result = new Vector<>();
        double pkCourant = 0.0;

        for (Lalana l : chemin) {

            LalanaVoyage lv = new LalanaVoyage();
            lv.setLalana(l);
            lv.setPkDebutGlobal(pkCourant);

            double pkFin = pkCourant + l.getDistance();
            lv.setPkFinGlobal(pkFin);

            result.add(lv);

            // Mise à jour du PK pour la prochaine lalana
            pkCourant = pkFin;
        }

        return result;
    }

    public static void remplirLalanaDispo(InfoVoyage voyage, Vector<Lalana> allLalana) {
        // Lalana manomboka am deepart du voyage
        Vector<Lalana> result = new Vector<>();
        for (Lalana l : allLalana) {
            if (l.getDebut().equals(voyage.getDepart())) {
                result.add(l);
            }
        }
        voyage.setLalanaDispo(result);
    }

    // liste lalana rehetra
    public static Vector<Vector<Lalana>> trouverChemins(InfoVoyage voyage, Vector<Lalana> allLalana) {
        Vector<Vector<Lalana>> chemins = new Vector<>();
        findLalanaPossible(voyage.getDepart(), voyage.getDestination(),
                new Vector<>(), new HashSet<>(), allLalana, chemins);
        return chemins;
    }

    // maka ny lalana possibles entre depart et destination ; cheminCourant : lalana
    // efa nalain, chemins : liste ny lalana rehetra azo
    private static void findLalanaPossible(String courant, String destination, Vector<Lalana> cheminCourant,
            HashSet<Integer> visites, Vector<Lalana> allLalana, Vector<Vector<Lalana>> chemins) {

        // rah mitovy ny depart sy destinstion
        if (courant.equals(destination)) {
            chemins.add(new Vector<>(cheminCourant));
            return;
        }

        for (Lalana l : allLalana) {
            if (!l.getDebut().equals(courant))
                continue; // segment pas utilisable
            if (visites.contains(l.getId()))
                continue; // eviter cycles

            visites.add(l.getId());
            cheminCourant.add(l);

            findLalanaPossible(l.getFin(), destination, cheminCourant, visites, allLalana, chemins);

            cheminCourant.remove(cheminCourant.size() - 1);
            visites.remove(l.getId());
        }
    }

    public static double calculVitesseSurLalana(
            Lalana lalana,
            double vitesseBase,
            double positionKm) {

        if (lalana.getLavakas() == null)
            return vitesseBase;

        for (Lavaka lavaka : lalana.getLavakas()) {

            if (positionKm >= lavaka.getDebutLavaka()
                    && positionKm <= lavaka.getFinLavaka()) {

                double taux = lavaka.getTauxRalentissement() / 100.0;
                return vitesseBase * (1 - taux);
            }
        }
        return vitesseBase;
    }

    public static double calculerTempsReelSurLalana(Lalana lalana, double vitesseMoyenne) {
        if (vitesseMoyenne <= 0)
            throw new IllegalArgumentException("Vitesse moyenne invalide");

        double tempsTotal = 0.0; // en heures
        double positionCourante = 0.0;

        Vector<Lavaka> lavakas = lalana.getLavakas();
        if (lavakas != null && !lavakas.isEmpty()) {
            // trier les lavakas par debut
            lavakas.sort((a, b) -> Double.compare(a.getDebutLavaka(), b.getDebutLavaka()));

            for (Lavaka lavaka : lavakas) {
                double debut = lavaka.getDebutLavaka();
                double fin = lavaka.getFinLavaka();

                // Segment avant le lavaka
                if (debut > positionCourante) {
                    double distanceAvant = debut - positionCourante;
                    tempsTotal += distanceAvant / vitesseMoyenne;
                }

                // Segment sur le lavaka
                double distanceLavaka = fin - debut;
                double vitesseLavaka = vitesseMoyenne * (1 - lavaka.getTauxRalentissement() / 100.0);
                if (vitesseLavaka <= 0)
                    throw new IllegalArgumentException(
                            "Taux de ralentissement invalide pour lavaka id=" + lavaka.getId());
                tempsTotal += distanceLavaka / vitesseLavaka;

                positionCourante = fin;
            }
        }

        // Segment après le dernier lavaka
        double distanceRestante = lalana.getDistance() - positionCourante;
        if (distanceRestante > 0) {
            tempsTotal += distanceRestante / vitesseMoyenne;
        }

        return tempsTotal; // en heures
    }

    public static double calculerVitesseMoyenneReelle(Vector<Lalana> chemin, double vitesseMoyenne) {
        if (chemin == null || chemin.isEmpty())
            return 0.0;

        double distanceTotale = 0.0;
        double tempsTotal = 0.0; // en heures

        for (Lalana l : chemin) {
            distanceTotale += l.getDistance();
            tempsTotal += calculerTempsReelSurLalana(l, vitesseMoyenne);
        }

        if (tempsTotal <= 0)
            return 0.0;

        return distanceTotale / tempsTotal;
    }

}
