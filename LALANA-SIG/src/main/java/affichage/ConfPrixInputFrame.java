package affichage;

import dao.ConfPrixDAO;
import dao.TypeMatiereDAO;
import model.ConfPrix;
import model.TypeMatiere;
import utildb.ConnexionOracle;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

public class ConfPrixInputFrame extends JFrame {

    private ConnexionOracle conn;

    private JTextField minField;
    private JTextField maxField;
    private JTextField prixMCField;
    private JComboBox<TypeMatiere> typeMatiereComboBox;
    private JButton saveButton;

    public ConfPrixInputFrame(ConnexionOracle conn) {
        this.conn = conn;

        setTitle("Ajouter ConfPrix");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new GridLayout(0, 2, 5, 5));

        // Champs
        add(new JLabel("Min :"));
        minField = new JTextField();
        add(minField);

        add(new JLabel("Max :"));
        maxField = new JTextField();
        add(maxField);

        add(new JLabel("Prix MC :"));
        prixMCField = new JTextField();
        add(prixMCField);

        add(new JLabel("Type Matiere :"));
        typeMatiereComboBox = new JComboBox<>();
        add(typeMatiereComboBox);

        saveButton = new JButton("Enregistrer");
        add(new JLabel()); // vide
        add(saveButton);

        // Chargement des types
        chargerTypeMatiere();

        // Listener du bouton
        saveButton.addActionListener(e -> enregistrerConfPrix());

        setVisible(true);
    }

    private void chargerTypeMatiere() {
        try {
            Vector<TypeMatiere> typeList = TypeMatiereDAO.getAll(conn);
            for (TypeMatiere t : typeList) {
                typeMatiereComboBox.addItem(t);
            }
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors du chargement des types : " + e.getMessage());
        }
    }

    private void enregistrerConfPrix() {
        try {
            ConfPrix cp = new ConfPrix();
            cp.setMin(Double.parseDouble(minField.getText()));
            cp.setMax(Double.parseDouble(maxField.getText()));
            cp.setPrixMC(Double.parseDouble(prixMCField.getText()));

            TypeMatiere type = (TypeMatiere) typeMatiereComboBox.getSelectedItem();
            if (type == null) {
                JOptionPane.showMessageDialog(this, "Veuillez sélectionner un Type Matière");
                return;
            }

            cp.setIdTypeMatiere(type.getId());

            ConfPrixDAO.save(conn, cp);

            JOptionPane.showMessageDialog(this, "ConfPrix enregistré !");
            dispose();

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Valeurs numériques invalides");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur lors de l'enregistrement : " + ex.getMessage());
        }
    }
}
