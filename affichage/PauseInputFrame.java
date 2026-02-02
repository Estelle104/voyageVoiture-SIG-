package affichage;

import model.Lalana;
import model.Pause;
import dao.LalanaDAO;
import dao.PauseDAO;
import utildb.ConnexionOracle;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDateTime;
import java.util.Vector;

public class PauseInputFrame extends JFrame {

    private ConnexionOracle connexionOracle;

    public PauseInputFrame(ConnexionOracle connOracle) {
        this.connexionOracle = connOracle;

        setTitle("Ajouter une pause");
        setSize(350, 300);
        setLocationRelativeTo(null);
        setLayout(new GridLayout(0, 2, 5, 5));

        JTextField nomField = new JTextField();
        JTextField positionField = new JTextField();
        JTextField debutField = new JTextField("2026-01-13T10:00");
        JTextField finField = new JTextField("2026-01-13T10:15");

        JComboBox<String> lalanaComboBox = new JComboBox<>();
        Vector<Lalana> lalanaList = new Vector<>();

        // Charger les lalanas avec la connexion injectée
        try {
            Vector<Lalana> temp = LalanaDAO.getAllLight(connexionOracle);
            lalanaList.addAll(temp);

            for (Lalana l : lalanaList) {
                lalanaComboBox.addItem(l.getNomLalana());
            }

        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Erreur chargement lalana : " + ex.getMessage());
            ex.printStackTrace();
        }

        add(new JLabel("Nom :"));
        add(nomField);

        add(new JLabel("Lalana :"));
        add(lalanaComboBox);

        add(new JLabel("Position (km) :"));
        add(positionField);

        add(new JLabel("Heure début :"));
        add(debutField);

        add(new JLabel("Heure fin :"));
        add(finField);

        JButton btnSave = new JButton("Enregistrer");
        add(new JLabel());
        add(btnSave);

        // Action enregistrer
        btnSave.addActionListener(e -> {
            try {
                int index = lalanaComboBox.getSelectedIndex();
                if (index < 0) {
                    JOptionPane.showMessageDialog(this, "Veuillez choisir un lalana");
                    return;
                }

                Lalana lalanaChoisi = lalanaList.get(index);

                Pause p = new Pause(
                        0,
                        nomField.getText(),
                        lalanaChoisi.getId(),
                        LocalDateTime.parse(debutField.getText()),
                        LocalDateTime.parse(finField.getText()),
                        Double.parseDouble(positionField.getText())
                );

                PauseDAO.save(connexionOracle, p);

                JOptionPane.showMessageDialog(this, "Pause ajoutée");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
                ex.printStackTrace();
            }
        });

        setVisible(true);
    }
}
