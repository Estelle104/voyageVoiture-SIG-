package affichage;

import dao.LalanaDAO;
import dao.SimbaDAO;
import model.Lalana;
import model.Simba;
import utildb.ConnexionOracle;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class SimbaInputFrame extends JFrame {

    private ConnexionOracle conn;

    private JComboBox<Lalana> lalanaComboBox;
    private JTextField descriptionsField;
    private JTextField pkDebutField;
    private JTextField pkFinField;
    private JTextField tauxRalentissementField;
    private JTextField surfaceField;
    private JTextField profondeurField;

    private JButton saveButton;

    public SimbaInputFrame(ConnexionOracle conn) {
        this.conn = conn;

        setTitle("Ajouter un Simba");
        setSize(400, 420);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(0, 2, 5, 5));

        // Champs
        add(new JLabel("Description :"));
        descriptionsField = new JTextField();
        add(descriptionsField);

        add(new JLabel("PK Debut :"));
        pkDebutField = new JTextField();
        add(pkDebutField);

        add(new JLabel("PK Fin :"));
        pkFinField = new JTextField();
        add(pkFinField);

        add(new JLabel("Taux Ralentissement :"));
        tauxRalentissementField = new JTextField();
        add(tauxRalentissementField);

        add(new JLabel("Surface :"));
        surfaceField = new JTextField();
        add(surfaceField);

        add(new JLabel("Profondeur :"));
        profondeurField = new JTextField();
        add(profondeurField);

        add(new JLabel("Lalana :"));
        lalanaComboBox = new JComboBox<>();
        add(lalanaComboBox);

        saveButton = new JButton("Enregistrer");
        add(new JLabel()); // vide pour alignement
        add(saveButton);

        // Chargement données
        chargerComboBoxes();

        // Listener du bouton
        saveButton.addActionListener(e -> enregistrerSimba());

        setVisible(true);
    }

    private void chargerComboBoxes() {
        try {
            Vector<Lalana> lalanaList = LalanaDAO.getAll(conn);
            for (Lalana l : lalanaList) {
                lalanaComboBox.addItem(l);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des combos : " + e.getMessage());
        }
    }

    private void enregistrerSimba() {
        try {
            String descriptions = descriptionsField.getText();
            double pkDebut = Double.parseDouble(pkDebutField.getText());
            double pkFin = Double.parseDouble(pkFinField.getText());
            double tauxRalentissement = Double.parseDouble(tauxRalentissementField.getText());
            double surface = Double.parseDouble(surfaceField.getText());
            double profondeur = Double.parseDouble(profondeurField.getText());

            Lalana lalana = (Lalana) lalanaComboBox.getSelectedItem();

            if (lalana == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner Lalana");
                return;
            }

            Simba s = new Simba(descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur,
                    lalana.getId(), null, 0);

            SimbaDAO.save(conn, s);

            JOptionPane.showMessageDialog(this, "Simba enregistré !");
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valeurs numériques invalides");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage());
        }
    }
}
