package affichage;

import model.Lalana;
import model.Lavaka;
import utildb.ConnexionOracle;

import javax.swing.*;

import dao.LalanaDAO;
import dao.LavakaDAO;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.Vector;

public class LavakaInputFrame extends JFrame {

    private JComboBox<String> lalanaDropdown;
    private JTextField debutField;
    private JTextField finField;
    private JTextField tauxField;
    private JButton ajouterButton;

    private Vector<Lalana> toutesLalanas;
    private ConnexionOracle connexionOracle;

    // Injection de la connexion Oracle
    public LavakaInputFrame(ConnexionOracle connOracle) {
        this.connexionOracle = connOracle;

        setTitle("Ajouter un Lavaka");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(5, 2, 10, 10));

        try {
            toutesLalanas = LalanaDAO.getAll(connexionOracle);
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur récupération Lalana : " + e.getMessage());
            e.printStackTrace();
        }

        lalanaDropdown = new JComboBox<>();
        for (Lalana l : toutesLalanas) {
            lalanaDropdown.addItem(l.getNomLalana());
        }

        debutField = new JTextField();
        finField = new JTextField();
        tauxField = new JTextField();

        ajouterButton = new JButton("Ajouter");

        add(new JLabel("Lalana :"));
        add(lalanaDropdown);
        add(new JLabel("Début Lavaka (km) :"));
        add(debutField);
        add(new JLabel("Fin Lavaka (km) :"));
        add(finField);
        add(new JLabel("Taux de ralentissement :"));
        add(tauxField);
        add(new JLabel());
        add(ajouterButton);

        ajouterButton.addActionListener(this::ajouterLavaka);

        setVisible(true);
    }

    private void ajouterLavaka(ActionEvent e) {
        try {
            String nomLalana = (String) lalanaDropdown.getSelectedItem();
            double debut = Double.parseDouble(debutField.getText());
            double fin = Double.parseDouble(finField.getText());
            double taux = Double.parseDouble(tauxField.getText());

            int idLalana = -1;
            for (Lalana l : toutesLalanas) {
                if (l.getNomLalana().equals(nomLalana)) {
                    idLalana = l.getId();
                    break;
                }
            }

            if (idLalana == -1) {
                JOptionPane.showMessageDialog(this, "Lalana introuvable !");
                return;
            }

            Lavaka nouveauLavaka = new Lavaka();
            nouveauLavaka.setIdLalana(idLalana);
            nouveauLavaka.setDebutLavaka(debut);
            nouveauLavaka.setFinLavaka(fin);
            nouveauLavaka.setTauxRalentissement(taux);

            LavakaDAO.save(connexionOracle, nouveauLavaka);

            JOptionPane.showMessageDialog(this, "Lavaka ajouté !");
            debutField.setText("");
            finField.setText("");
            tauxField.setText("");

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Veuillez entrer des nombres valides !");
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
            ex.printStackTrace();
        }
    }
}
