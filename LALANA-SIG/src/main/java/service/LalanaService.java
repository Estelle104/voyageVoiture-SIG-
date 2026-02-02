package service;

import java.util.List;
import java.util.Vector;

import model.Lalana;
import model.Voiture;

public class LalanaService {
    public static boolean voitureCompatible(Lalana lalana, Voiture voiture) {
        return voiture.getLargeur() <= lalana.getLargeur() / 2;
    }

    public static void verifierCompatibiliteVoiture(List<Lalana> chemin, Voiture voiture) {

        for (Lalana l : chemin) {

            double largeurVoie = l.getLargeur() / 2;

            if (voiture.getLargeur() > largeurVoie) {
                throw new IllegalArgumentException(
                        "Voiture trop large pour le lalana : " + l.getNomLalana() +
                                " (largeur voie = " + largeurVoie + " m)");
            }
        }
    }

    public static double calculerDistanceTotale(Vector<Lalana> chemin) {
        double total = 0;
        for (Lalana l : chemin) {
            total += l.getDistance();
        }
        return total;
    }

}
