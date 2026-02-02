package affichage;

import javax.swing.*;
import java.awt.*;
import java.util.Vector;

import model.ConfPluie;
import model.TypeMatiere;
import dao.ConfPluieDAO;
import dao.TypeMatiereDAO;
import utildb.ConnexionOracle;

public class ConfPluieInputFrame extends JFrame {

    private JTextField txtMinP;
    private JTextField txtMaxP;
    private JComboBox<TypeMatiere> comboTypeMatiere;

    public ConfPluieInputFrame(ConnexionOracle conn) {
        setTitle("Ajouter une Configuration Pluie");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));

        panel.add(new JLabel("Pluie min :"));
        txtMinP = new JTextField();
        panel.add(txtMinP);

        panel.add(new JLabel("Pluie max :"));
        txtMaxP = new JTextField();
        panel.add(txtMaxP);

        panel.add(new JLabel("Type de matière :"));
        comboTypeMatiere = new JComboBox<>();

        try {
            Vector<TypeMatiere> types = TypeMatiereDAO.getAll(conn);
            for (TypeMatiere t : types) {
                comboTypeMatiere.addItem(t);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement TypeMatiere");
        }

        // affichage du nom dans la combo
        comboTypeMatiere.setRenderer((list, value, index, isSelected, cellHasFocus) ->
                new JLabel(value != null ? value.getTypeMatiere() : "")
        );

        panel.add(comboTypeMatiere);

        JButton btnSave = new JButton("Enregistrer");
        btnSave.addActionListener(e -> {
            try {
                TypeMatiere tm = (TypeMatiere) comboTypeMatiere.getSelectedItem();

                ConfPluie cp = new ConfPluie();
                cp.setMinP(Double.parseDouble(txtMinP.getText()));
                cp.setMaxP(Double.parseDouble(txtMaxP.getText()));
                cp.setIdTypeMatiere(tm.getId());

                ConfPluieDAO.save(conn, cp);
                JOptionPane.showMessageDialog(this, "Configuration pluie enregistrée");
                dispose();

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage());
            }
        });

        panel.add(new JLabel());
        panel.add(btnSave);

        add(panel);
        setVisible(true);
    }
}
