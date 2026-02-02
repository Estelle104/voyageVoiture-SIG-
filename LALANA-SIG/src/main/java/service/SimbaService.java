package service;

import java.util.Vector;

import dao.*;
import model.*;
import utildb.ConnexionOracle;

public class SimbaService {

    // Calcul elementaire
    private double calculerPrixSimba(double surface, double prixMC) {
        return surface * prixMC;
    }

    // Calcul + sauvegarde prix d’un Simba

    public PrixSimba calculerPrixParMatiere(ConnexionOracle co, Simba simba) throws Exception {

        if (simba.getIdTypeMatiere() <= 0) {
            System.out.println("Aucune matiere definie pour le Simba ID " + simba.getId());
        }

        ConfPrix confPrix = ConfPrixDAO.findByTypeAndProfondeur(
                co,
                simba.getIdTypeMatiere(),
                simba.getProfondeur());

        if (confPrix == null) {
            throw new Exception(
                    "Aucun prix defini pour le Simba ID " + simba.getId() +
                            " (matiere / profondeur incompatible)");
        }

        double prixTotal = calculerPrixSimba(
                simba.getSurface(),
                confPrix.getPrixMC());

        // Verifier si un prix existe deja
        PrixSimba existant = PrixSimbaDAO.findBySimbaAndLalana(
                co,
                simba.getId(),
                simba.getIdLalana());

        if (existant != null) {
            existant.setPrixSimba(prixTotal);
            PrixSimbaDAO.update(co, existant);
            return existant;
        }

        PrixSimba nouveau = new PrixSimba(
                simba.getId(),
                simba.getIdLalana(),
                prixTotal);
        PrixSimbaDAO.save(co, nouveau);
        return nouveau;
    }

    // Cout TOTAL d’un Lalana
    public double calculerCoutReparationParLalana(ConnexionOracle co, int idLalana) throws Exception {

        Vector<Simba> simbas = SimbaDAO.getByIdLalana(co, idLalana);

        if (simbas == null || simbas.isEmpty()) {
            throw new Exception("Aucun Simba trouve pour le Lalana ID " + idLalana);
        }

        double total = 0.0;

        for (Simba simba : simbas) {
            PrixSimba prixSimba = calculerPrixParMatiere(co, simba);
            total += prixSimba.getPrixSimba();
        }

        return total;
    }

    // Cout TOTAL d’un Voyage
    public double calculerCoutReparationParVoyage(
            ConnexionOracle co,
            Vector<Lalana> lalanas) throws Exception {

        if (lalanas == null || lalanas.isEmpty()) {
            throw new Exception("Aucun Lalana trouve pour ce voyage");
        }

        double total = 0.0;

        for (Lalana lalana : lalanas) {
            total += calculerCoutReparationParLalana(co, lalana.getId());
        }

        return total;
    }
}
