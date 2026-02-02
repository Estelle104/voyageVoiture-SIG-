package affichage;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.List;

import model.Pluviometrie;
import dao.PluviometrieDAO;
import utildb.ConnexionOracle;

public class PluviometrieInputFrame extends JFrame {

    private JTextField txtTauxPluie;
    private JTextField txtPkDebut;
    private JTextField txtPkFin;

    private JTable table;
    private DefaultTableModel tableModel;

    private ConnexionOracle conn;

    public PluviometrieInputFrame(ConnexionOracle conn) {
        this.conn = conn;

        setTitle("Gestion des Pluviométries");
        setSize(600, 450);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

        setLayout(new BorderLayout(10, 10));

        /* ===================== FORMULAIRE ===================== */
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));

        formPanel.add(new JLabel("Taux de pluie :"));
        txtTauxPluie = new JTextField();
        formPanel.add(txtTauxPluie);

        formPanel.add(new JLabel("PK Début :"));
        txtPkDebut = new JTextField();
        formPanel.add(txtPkDebut);

        formPanel.add(new JLabel("PK Fin :"));
        txtPkFin = new JTextField();
        formPanel.add(txtPkFin);

        JButton btnSave = new JButton("Enregistrer");
        JButton btnRetour = new JButton("Retour");

        formPanel.add(btnSave);
        formPanel.add(btnRetour);

        add(formPanel, BorderLayout.NORTH);

        /* ===================== TABLEAU ===================== */
        tableModel = new DefaultTableModel(
                new Object[]{"ID", "Taux Pluie", "PK Début", "PK Fin"}, 0
        );
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        add(scrollPane, BorderLayout.CENTER);

        /* ===================== ACTIONS ===================== */

        // Enregistrer
        btnSave.addActionListener(e -> {
            try {
                Pluviometrie p = new Pluviometrie();
                p.setTauxPluie(Double.parseDouble(txtTauxPluie.getText()));
                p.setPkDebut(Double.parseDouble(txtPkDebut.getText()));
                p.setPkFin(Double.parseDouble(txtPkFin.getText()));

                PluviometrieDAO.save(conn, p);

                JOptionPane.showMessageDialog(this, "Pluviométrie enregistrée");

                viderChamps();
                chargerTable(); // rafraîchir le tableau

            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Erreur", JOptionPane.ERROR_MESSAGE);
            }
        });

        // Retour
        btnRetour.addActionListener(e -> dispose());

        /* ===================== CHARGEMENT INITIAL ===================== */
        chargerTable();

        setVisible(true);
    }

    /* ===================== METHODES ===================== */

    private void chargerTable() {
        try {
            tableModel.setRowCount(0);
            List<Pluviometrie> list = PluviometrieDAO.findAll(conn);

            for (Pluviometrie p : list) {
                tableModel.addRow(new Object[]{
                        p.getId(),
                        p.getTauxPluie(),
                        p.getPkDebut(),
                        p.getPkFin()
                });
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    private void viderChamps() {
        txtTauxPluie.setText("");
        txtPkDebut.setText("");
        txtPkFin.setText("");
    }
}
