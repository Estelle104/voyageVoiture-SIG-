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

public class ReparerLalanaFrame extends JFrame {

    private ConnexionOracle conn;

    private JComboBox<InfoVoyage> voyageCombo;
    private JComboBox<String> cheminCombo;
    private JComboBox<Lalana> lalanaCombo;
    private JComboBox<Simba> simbaCombo;

    private JTextField txtPkDebut;
    private JTextField txtPkFin;

    private JButton btnValider;
    private JButton btnFiltrer;
    private JButton btnAfficherResultat;

    private JTable tableauReparations;
    private DefaultTableModel tableModel;
    private JLabel lblCoutTotal;

    private Vector<Object[]> donneesTableau = new Vector<>();
    private int triEtat = 0;

    private Vector<Vector<Lalana>> cheminsTrouves = new Vector<>();
    private Vector<Lalana> cheminSelectionne = null;

    public ReparerLalanaFrame(ConnexionOracle conn, Vector<InfoVoyage> voyages) {
        this.conn = conn;

        setTitle("Reparer un Lalana");
        setSize(950, 600);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout(5, 5));

        /*  PANEL NORD  */
        JPanel panelControls = new JPanel(new GridLayout(0, 2, 5, 5));

        panelControls.add(new JLabel("Voyage :"));
        voyageCombo = new JComboBox<>(voyages);
        panelControls.add(voyageCombo);

        panelControls.add(new JLabel("Chemin :"));
        cheminCombo = new JComboBox<>();
        panelControls.add(cheminCombo);

        panelControls.add(new JLabel("Lalana :"));
        lalanaCombo = new JComboBox<>();
        panelControls.add(lalanaCombo);

        panelControls.add(new JLabel("Simba :"));
        simbaCombo = new JComboBox<>();
        panelControls.add(simbaCombo);

        panelControls.add(new JLabel("PK Debut :"));
        txtPkDebut = new JTextField();
        panelControls.add(txtPkDebut);

        panelControls.add(new JLabel("PK Fin :"));
        txtPkFin = new JTextField();
        panelControls.add(txtPkFin);

        btnValider = new JButton("Valider");
        btnFiltrer = new JButton("Filtrer");
        btnAfficherResultat = new JButton("Afficher resultat");

        panelControls.add(btnValider);
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
        voyageCombo.addActionListener(e -> chargerChemins());
        cheminCombo.addActionListener(e -> chargerLalanasDuChemin());
        lalanaCombo.addActionListener(e -> chargerSimbas());

        btnValider.addActionListener(e -> validerChoix());
        btnFiltrer.addActionListener(e -> appliquerFiltre());
        btnAfficherResultat.addActionListener(e -> appliquerFiltre());
    }

    /*  CHARGEMENTS  */

    private void chargerChemins() {
        InfoVoyage voyage = (InfoVoyage) voyageCombo.getSelectedItem();
        if (voyage == null)
            return;

        try {
            cheminsTrouves = InfoVoyageService.trouverChemins(
                    voyage, LalanaDAO.getAll(conn));

            cheminCombo.removeAllItems();

            for (Vector<Lalana> chemin : cheminsTrouves) {
                StringBuilder sb = new StringBuilder();
                for (Lalana l : chemin)
                    sb.append(l.getNomLalana()).append(" -> ");
                sb.append(voyage.getDestination());
                cheminCombo.addItem(sb.toString());
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement chemins");
        }
    }

    private void chargerLalanasDuChemin() {
        int idx = cheminCombo.getSelectedIndex();
        if (idx < 0)
            return;

        Vector<Lalana> chemin = cheminsTrouves.get(idx);
        lalanaCombo.removeAllItems();
        simbaCombo.removeAllItems();

        for (Lalana l : chemin)
            lalanaCombo.addItem(l);
    }

    private void chargerSimbas() {
        Lalana lalana = (Lalana) lalanaCombo.getSelectedItem();
        if (lalana == null)
            return;

        try {
            simbaCombo.removeAllItems();
            for (Simba s : SimbaDAO.getByIdLalana(conn, lalana.getId())) {
                simbaCombo.addItem(s);
            }
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Erreur chargement Simba");
        }
    }

    /*  VALIDATION  */

    private void validerChoix() {
        int idx = cheminCombo.getSelectedIndex();
        if (idx < 0) {
            JOptionPane.showMessageDialog(this, "Veuillez choisir un chemin");
            return;
        }

        cheminSelectionne = cheminsTrouves.get(idx);
        JOptionPane.showMessageDialog(this,
                "Chemin metyy (" + cheminSelectionne.size() + " lalanas)");
    }

    //  FILTRE pk 
    private void appliquerFiltre() {
        if (cheminSelectionne == null) {
            JOptionPane.showMessageDialog(this, "Veuillez valider le chemin d'abord");
            return;
        }

        try {
            double pkDebut = Double.parseDouble(txtPkDebut.getText());
            double pkFin = Double.parseDouble(txtPkFin.getText());

            if (pkDebut > pkFin) {
                JOptionPane.showMessageDialog(this, "PK Debut > PK Fin");
                return;
            }

            tableModel.setRowCount(0);
            donneesTableau.clear();
            triEtat = 0;

            PluieService pluieService = new PluieService();
            Vector<LalanaVoyage> pkGlobaux =
                    InfoVoyageService.calculerPkGlobaux(cheminSelectionne);

            Vector<Pluviometrie> pluies = PluviometrieDAO.findAll(conn);
            Vector<ConfPluie> confs = ConfPluieDAO.findAll(conn);

            double total = 0;

            for (Lalana l : cheminSelectionne) {
                Vector<Simba> simbas =
                        SimbaDAO.getSimbasBetweenPk(conn, l.getId(), pkDebut, pkFin);

                for (Simba s : simbas) {
                    PrixSimba prix = pluieService.calculerPrixParMatiere(
                            conn, s, pkGlobaux, pluies, confs);

                    if (prix == null)
                        continue;

                    Integer idType =
                            pluieService.determinerTypeMatierePourSimba(
                                    s, pkGlobaux, pluies, confs);

                    String typeMatiere = "N/A";
                    if (idType != null) {
                        TypeMatiere mat = TypeMatiereDAO.findById(conn, idType);
                        if (mat != null)
                            typeMatiere = mat.getTypeMatiere();
                    }

                    Object[] row = {
                            l.getNomLalana(),
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
            }

            lblCoutTotal.setText(String.format(
                    "Cout Total de reparation simba entre PK %.2f et %.2f : %.2f Ar",
                    pkDebut, pkFin, total));

            mettreAJourEnTete();

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "PK invalide");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, e.getMessage());
        }
    }

    /*  TRI  */
    private void trierTableauAscendant() {
        triEtat = 1;
        donneesTableau.sort((a, b) -> Double.compare((double) a[6], (double) b[6]));
        rafraichirTableau();
    }

    private void trierTableauDescendant() {
        triEtat = -1;
        donneesTableau.sort((a, b) -> Double.compare((double) b[6], (double) a[6]));
        rafraichirTableau();
    }

    private void rafraichirTableau() {
        tableModel.setRowCount(0);
        for (Object[] r : donneesTableau)
            tableModel.addRow(r);
        mettreAJourEnTete();
    }

    private void mettreAJourEnTete() {
        TableColumnModel cm = tableauReparations.getColumnModel();
        cm.getColumn(6).setHeaderValue(
                triEtat == 1 ? "Cout (Ar) ↑" :
                triEtat == -1 ? "Cout (Ar) ↓" : "Cout (Ar)");
        tableauReparations.getTableHeader().repaint();
    }
}
