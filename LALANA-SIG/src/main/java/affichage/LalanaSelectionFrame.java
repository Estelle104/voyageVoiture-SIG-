package affichage;

import dao.*;
import model.*;
import service.*;
import utildb.ConnexionOracle;

import javax.swing.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Vector;

public class LalanaSelectionFrame extends JFrame {

    private ConnexionOracle conn;
    private PluieService pluieService;
    private Vector<Lalana> lalanas;
    private Vector<LalanaVoyage> pkGlobaux;

    private JComboBox<Lalana> lalanaCombo;
    private JTextField txtPkDebut;
    private JTextField txtPkFin;

    private JButton btnFiltrer;
    private JButton btnAfficherResultat;

    private JTable tableauReparations;
    private DefaultTableModel tableModel;
    private JLabel lblCoutTotal;

    private Vector<Object[]> donneesTableau = new Vector<>();
    private int triEtat = 0;

    public LalanaSelectionFrame(ConnexionOracle conn, Vector<Lalana> cheminComplet) {
        this.conn = conn;
        this.pluieService = new PluieService();
        this.lalanas = cheminComplet;

        setTitle("Voir les coûts de réparation - Lalana");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        try {
            // Calculer les PK globaux du chemin
            pkGlobaux = InfoVoyageService.calculerPkGlobaux(lalanas);

            /*  PANEL NORD  */
            JPanel panelControls = new JPanel(new GridLayout(0, 2, 5, 5));

            panelControls.add(new JLabel("Lalana :"));
            lalanaCombo = new JComboBox<>(lalanas);
            lalanaCombo.setRenderer(new DefaultListCellRenderer() {
                @Override
                public Component getListCellRendererComponent(JList<?> list, Object value,
                        int index, boolean isSelected, boolean cellHasFocus) {
                    if (value instanceof Lalana) {
                        Lalana l = (Lalana) value;
                        value = l.getNomLalana() + " (" + l.getDebut() + " -> " + l.getFin() + ")";
                    }
                    return super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
                }
            });
            lalanaCombo.addActionListener(e -> appliquerFiltre());
            panelControls.add(lalanaCombo);

            panelControls.add(new JLabel("PK Debut :"));
            txtPkDebut = new JTextField();
            panelControls.add(txtPkDebut);

            panelControls.add(new JLabel("PK Fin :"));
            txtPkFin = new JTextField();
            panelControls.add(txtPkFin);

            btnFiltrer = new JButton("Filtrer");
            btnAfficherResultat = new JButton("Afficher resultat");

            panelControls.add(btnFiltrer);
            panelControls.add(btnAfficherResultat);

            add(panelControls, BorderLayout.NORTH);

            /*  TABLEAU  */
            String[] colonnes = {
                    "Lalana", "Description", "PK Debut",
                    "Surface", "Profondeur", "Type Matière", "Cout (Ar)"
            };

            tableModel = new DefaultTableModel(colonnes, 0) {
                @Override
                public boolean isCellEditable(int r, int c) {
                    return false;
                }
            };

            tableauReparations = new JTable(tableModel);
            JTableHeader header = tableauReparations.getTableHeader();

            header.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (tableauReparations.columnAtPoint(e.getPoint()) == 6) {
                        triEtat = (triEtat == 1) ? -1 : 1;
                        if (triEtat == 1)
                            trierTableauAscendant();
                        else
                            trierTableauDescendant();
                    }
                }
            });

            add(new JScrollPane(tableauReparations), BorderLayout.CENTER);

            /*  PANEL SUD  */
            lblCoutTotal = new JLabel("Cout Total : 0.00 Ar");
            lblCoutTotal.setFont(new Font("Arial", Font.BOLD, 14));
            lblCoutTotal.setForeground(new Color(0, 100, 0));
            add(lblCoutTotal, BorderLayout.SOUTH);

            /*  LISTENERS  */
            btnFiltrer.addActionListener(e -> appliquerFiltre());
            btnAfficherResultat.addActionListener(e -> appliquerFiltre());

            // Afficher le premier Lalana
            appliquerFiltre();

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur : " + e.getMessage());
            dispose();
        }
    }

    private void appliquerFiltre() {
        Lalana lalanaChoisi = (Lalana) lalanaCombo.getSelectedItem();
        if (lalanaChoisi == null)
            return;

        try {
            double pkDebut = txtPkDebut.getText().isEmpty() ? 0 : Double.parseDouble(txtPkDebut.getText());
            double pkFin = txtPkFin.getText().isEmpty() ? Double.MAX_VALUE : Double.parseDouble(txtPkFin.getText());

            if (pkDebut > pkFin) {
                JOptionPane.showMessageDialog(this, "PK Debut > PK Fin");
                return;
            }

            tableModel.setRowCount(0);
            donneesTableau.clear();
            triEtat = 0;

            // Charger les données de pluie
            Vector<Pluviometrie> pluies = PluviometrieDAO.findAll(conn);
            Vector<ConfPluie> confs = ConfPluieDAO.findAll(conn);

            double total = 0;

            // Récupérer les Simbas du Lalana sélectionné dans la plage PK
            Vector<Simba> simbas = SimbaDAO.getSimbasBetweenPk(conn, lalanaChoisi.getId(), pkDebut, pkFin);

            for (Simba s : simbas) {
                PrixSimba prix = pluieService.calculerPrixParMatiere(
                        conn, s, pkGlobaux, pluies, confs);

                if (prix == null)
                    continue;

                Integer idType = pluieService.determinerTypeMatierePourSimba(
                        s, pkGlobaux, pluies, confs);

                String typeMatiere = "N/A";
                if (idType != null) {
                    TypeMatiere mat = TypeMatiereDAO.findById(conn, idType);
                    if (mat != null)
                        typeMatiere = mat.getTypeMatiere();
                }

                Object[] row = {
                        lalanaChoisi.getNomLalana(),
                        s.getDescriptions(),
                        s.getPkDebut(),
                        s.getSurface(),
                        s.getProfondeur(),
                        typeMatiere,
                        prix.getPrixSimba()
                };

                donneesTableau.add(row);
                tableModel.addRow(row);
                total += prix.getPrixSimba();
            }

            lblCoutTotal.setText(String.format("Cout Total : %.2f Ar", total));

        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "PK invalide");
        } catch (Exception ex) {
            ex.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur : " + ex.getMessage());
        }
    }

    private void trierTableauAscendant() {
        donneesTableau.sort((a, b) -> {
            Double cout1 = (Double) a[6];
            Double cout2 = (Double) b[6];
            return cout1.compareTo(cout2);
        });

        tableModel.setRowCount(0);
        for (Object[] row : donneesTableau)
            tableModel.addRow(row);
    }

    private void trierTableauDescendant() {
        donneesTableau.sort((a, b) -> {
            Double cout1 = (Double) a[6];
            Double cout2 = (Double) b[6];
            return cout2.compareTo(cout1);
        });

        tableModel.setRowCount(0);
        for (Object[] row : donneesTableau)
            tableModel.addRow(row);
    }
}
