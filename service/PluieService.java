package service;

import java.util.Vector;
import dao.ConfPrixDAO;
import dao.PrixSimbaDAO;
import model.ConfPluie;
import model.ConfPrix;
import model.LalanaVoyage;
import model.Pluviometrie;
import model.PrixSimba;
import model.Simba;
import utildb.ConnexionOracle;

public class PluieService {
    public Integer determinerTypeMatierePourSimba(
            Simba simba,
            Vector<LalanaVoyage> cheminVoyage,
            Vector<Pluviometrie> pluviometries,
            Vector<ConfPluie> confPluies) {

        if (simba == null) {
            return null; // aucun Simba a traiter
        }

        double pkSimba = simba.getPkDebut();

        // Verifier si le Simba se trouve sur un tronçon du voyage
        for (LalanaVoyage lv : cheminVoyage) {
            if (pkSimba < lv.getPkDebutGlobal() || pkSimba > lv.getPkFinGlobal()) {
                continue; // Simba hors de ce tronçon
            }

            // Chercher une pluviometrie couvrant ce PK
            for (Pluviometrie p : pluviometries) {
                if (pkSimba < p.getPkDebut() || pkSimba > p.getPkFin()) {
                    continue; // pas de pluie active a ce PK
                }

                double tauxPluie = p.getTauxPluie();

                // Chercher la matiere adaptee selon ConfPluie                
                for (ConfPluie cp : confPluies) {
                    if (tauxPluie >= cp.getMinP() && tauxPluie <= cp.getMaxP()) {
                        return cp.getIdTypeMatiere(); // matiere trouvee
                    }
                }
            }
        }

        return null; // aucune matiere trouvee
    }

    private double calculerPrixSimba(double surface, double prixMC) {
        return surface * prixMC;
    }

    public PrixSimba calculerPrixParMatiere(
            ConnexionOracle co,
            Simba simba,
            Vector<LalanaVoyage> cheminVoyage,
            Vector<Pluviometrie> pluviometries,
            Vector<ConfPluie> confPluies) throws Exception {

        
        //   Determiner le type de matiere via pluie + regles
        Integer idTypeMatiere = determinerTypeMatierePourSimba(
                simba,
                cheminVoyage,
                pluviometries,
                confPluies);

        //   Trouver la configuration de prix
        ConfPrix confPrix = ConfPrixDAO.findByTypeAndProfondeur(
                co,
                idTypeMatiere,
                simba.getProfondeur());

        if (confPrix == null) {
            throw new Exception(
                    "Aucun prix defini pour le Simba ID " + simba.getId() +
                            " (matiere / profondeur incompatible)");
        }
        
         // Calcul du prix total
        
        double prixTotal = calculerPrixSimba(
                simba.getSurface(),
                confPrix.getPrixMC());

        // Sauvegarde ou mise a jour du prix
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

}
