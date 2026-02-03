package affichage;

import dao.*;
import model.*;
import service.*;
import utildb.ConnexionOracle;
import utildb.ConnexionPSQL;

import javax.swing.*;
import java.awt.*;
import java.awt.Desktop;
import java.net.URI;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Vector;

public class MainPanel extends JPanel {

    private ConnexionOracle connOracle;
    private ConnexionPSQL connPSQL;

    private Vector<Vector<Lalana>> cheminsTrouve = new Vector<>();
    private Vector<Voiture> voituresDisponibles = new Vector<>();
    private Voiture voitureChoisie;

    private JButton btnAjouterLavaka;
    private JButton btnAjouterPause;
    private JButton btnAjouterSimba;
    private JButton btnAjouterConfPrix;
    private JButton btnReparerLalana;

    private JButton btnAjouterPluviometrie;
    private JButton btnAjouterConfPluie;
    private JButton btnAfficherMap;


    public MainPanel() {

        try {
            connOracle = new ConnexionOracle();
            connPSQL = new ConnexionPSQL();

            setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

            // Boutons existants
            btnAjouterLavaka = new JButton("Ajouter un Lavaka");
            btnAjouterLavaka.addActionListener(e -> new LavakaInputFrame(connOracle));
            add(btnAjouterLavaka);

            btnAjouterPause = new JButton("Ajouter une Pause");
            btnAjouterPause.addActionListener(e -> new PauseInputFrame(connOracle));
            add(btnAjouterPause);

            // Nouveaux boutons pour Simba et ConfPrix
            btnAjouterSimba = new JButton("Ajouter un Simba");
            btnAjouterSimba.addActionListener(e -> new SimbaInputFrame(connOracle));
            add(btnAjouterSimba);

            btnAjouterConfPrix = new JButton("Ajouter un ConfPrix");
            btnAjouterConfPrix.addActionListener(e -> new ConfPrixInputFrame(connOracle));
            add(btnAjouterConfPrix);

            btnReparerLalana = new JButton("Réparer un Lalana");
            btnReparerLalana.addActionListener(e -> {
                try {
                    ReparerLalanaFrame frame = new ReparerLalanaFrame(
                            connOracle,
                            InfoVoyageDAO.getAll(connOracle));
                    frame.setVisible(true); 
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            this,
                            "Erreur lors de l'ouverture de la fenêtre Réparer Lalana");
                }
            });
            add(btnReparerLalana);

            btnAjouterPluviometrie = new JButton("Ajouter une Pluviométrie");
            btnAjouterPluviometrie.addActionListener(
                    e -> new PluviometrieInputFrame(connOracle)
            );
            add(btnAjouterPluviometrie);

            btnAjouterConfPluie = new JButton("Ajouter une ConfPluie");
            btnAjouterConfPluie.addActionListener(
                    e -> new ConfPluieInputFrame(connOracle)
            );

            // Bouton Afficher carte SIG

            btnAfficherMap = new JButton("Afficher la carte SIG");
            btnAfficherMap.addActionListener(e -> {
                try {
                    // Essayer d'abord localhost:8080 (Tomcat)
                    String urlTomcat = "http://localhost:8080/VoyageVoiture/jsp/sig.jsp";
                    
                    if (isTomcatAvailable()) {
                        // Tomcat est disponible, utiliser localhost:8080
                        Desktop.getDesktop().browse(new URI(urlTomcat));
                    } else {
                        // Tomcat n'est pas disponible, utiliser le fichier local
                        java.io.File sigFile = new java.io.File("LALANA-SIG/src/main/webapp/jsp/sig.jsp");
                        if (!sigFile.exists()) {
                            sigFile = new java.io.File("src/main/webapp/jsp/sig.jsp");
                        }
                        
                        if (!sigFile.exists()) {
                            JOptionPane.showMessageDialog(this,
                                "❌ Le fichier sig.jsp n'a pas pu être trouvé\n" +
                                "Chemin attendu: src/main/webapp/jsp/sig.jsp",
                                "Erreur",
                                JOptionPane.ERROR_MESSAGE);
                            return;
                        }
                        
                        String fileUrl = sigFile.getAbsoluteFile().toURI().toString();
                        Desktop.getDesktop().browse(new URI(fileUrl));
                    }
                    
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                        "❌ Erreur lors de l'ouverture de la carte:\n" + ex.getMessage(),
                        "Erreur",
                        JOptionPane.ERROR_MESSAGE);
                }
            });

            add(btnAfficherMap);


            add(btnAjouterConfPluie);

            // Choix VOYAGE
            Vector<InfoVoyage> voyages = InfoVoyageDAO.getAll(connOracle);
            JComboBox<String> voyageComboBox = new JComboBox<>();
            for (InfoVoyage v : voyages) {
                voyageComboBox.addItem(v.getDepart() + " -> " + v.getDestination());
            }
            add(new JLabel("Choisissez un voyage :"));
            add(voyageComboBox);

            // Choix CHEMIN
            JComboBox<String> lalanaComboBox = new JComboBox<>();
            add(new JLabel("Choisissez un chemin :"));
            add(lalanaComboBox);

            // Bouton Voir les coûts de réparation par Lalana
            JButton btnVoirCoutsLalana = new JButton(" Voir coûts réparation par Lalana");
            btnVoirCoutsLalana.setBackground(new Color(28, 107, 173));
            btnVoirCoutsLalana.setForeground(Color.WHITE);
            btnVoirCoutsLalana.setFont(btnVoirCoutsLalana.getFont().deriveFont(Font.BOLD));
            btnVoirCoutsLalana.addActionListener(e -> {
                try {
                    int indexChemin = lalanaComboBox.getSelectedIndex();
                    if (indexChemin < 0) {
                        JOptionPane.showMessageDialog(this, "Veuillez choisir un chemin d'abord");
                        return;
                    }
                    Vector<Lalana> cheminChoisi = cheminsTrouve.get(indexChemin);
                    LalanaSelectionFrame frame = new LalanaSelectionFrame(connOracle, cheminChoisi);
                    frame.setVisible(true);
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(
                            this,
                            "Erreur lors de l'ouverture: " + ex.getMessage());
                }
            });
            add(btnVoirCoutsLalana);

            // Choix VOITURE
            JComboBox<String> voitureComboBox = new JComboBox<>();
            add(new JLabel("Choisissez une voiture :"));
            add(voitureComboBox);

            // Champ vitesse
            JTextField vitesseField = new JTextField(5);
            add(new JLabel("Vitesse moyenne (km/h) :"));
            add(vitesseField);

            // Champ date/heure de depart
            JTextField dateField = new JTextField(16);
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
            dateField.setText(LocalDateTime.now().format(formatter));
            add(new JLabel("Date et heure de depart (yyyy-MM-dd HH:mm) :"));
            add(dateField);

            // Choix du sens
            JComboBox<String> sensComboBox = new JComboBox<>(new String[] { "ALLER", "RETOUR" });
            add(new JLabel("Sens du voyage :"));
            add(sensComboBox);

            // Bouton START
            JButton startButton = new JButton("Start");
            add(startButton);

            // LABEL INFO
            JLabel infoCheminLabel = new JLabel("Distance : ");
            add(infoCheminLabel);

            // PANEL SIMULATION
            SimulationPanel simulationPanel = new SimulationPanel();
            simulationPanel.setPreferredSize(new Dimension(700, 300));
            add(simulationPanel);

            // Chargement voitures
            voituresDisponibles = VoitureDAO.getAllVoiture(connPSQL);
            for (Voiture v : voituresDisponibles) {
                voitureComboBox.addItem(
                        v.getNom()
                                + " | Vmax=" + v.getVitesseMax()
                                + " km/h | Reservoir=" + v.getReservoire()
                                + " L | Conso=" + v.getConsommation() + " L/100km");
            }

            // LISTENERS

            voyageComboBox.addActionListener(e -> {
                int index = voyageComboBox.getSelectedIndex();
                if (index < 0)
                    return;

                InfoVoyage voyageChoisi = voyages.get(index);
                try {
                    cheminsTrouve = InfoVoyageService.trouverChemins(
                            voyageChoisi,
                            LalanaDAO.getAll(connOracle));

                    lalanaComboBox.removeAllItems();
                    for (Vector<Lalana> chemin : cheminsTrouve) {
                        StringBuilder sb = new StringBuilder();
                        for (Lalana l : chemin) {
                            sb.append(l.getNomLalana()).append(" -> ");
                        }
                        sb.append(voyageChoisi.getDestination());
                        lalanaComboBox.addItem(sb.toString());
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    JOptionPane.showMessageDialog(this,
                            "Erreur lors du calcul des chemins");
                }
            });

            // Choix voiture
            voitureComboBox.addActionListener(e -> {
                int index = voitureComboBox.getSelectedIndex();
                if (index >= 0)
                    voitureChoisie = voituresDisponibles.get(index);
                mettreAJourInfoChemin(infoCheminLabel, lalanaComboBox, cheminsTrouve, voitureChoisie);
            });

            // START SIMULATION
            startButton.addActionListener(e -> {
                int indexChemin = lalanaComboBox.getSelectedIndex();
                if (indexChemin < 0) {
                    JOptionPane.showMessageDialog(this, "Veuillez choisir un chemin");
                    return;
                }
                if (voitureChoisie == null) {
                    JOptionPane.showMessageDialog(this, "Veuillez choisir une voiture");
                    return;
                }
                try {
                    Vector<Lalana> chemin = cheminsTrouve.get(indexChemin);

                    double vitesse = Double.parseDouble(vitesseField.getText());
                    if (vitesse > voitureChoisie.getVitesseMax()) {
                        JOptionPane.showMessageDialog(this,
                                "La vitesse depasse la vitesse max de la voiture");
                        return;
                    }

                    // sens et heure de depart
                    SensVoyage sensChoisi = SensVoyage.valueOf(
                            ((String) sensComboBox.getSelectedItem()).toUpperCase());

                    LocalDateTime heureDepart;
                    try {
                        heureDepart = LocalDateTime.parse(dateField.getText(), formatter);
                    } catch (Exception ex) {
                        JOptionPane.showMessageDialog(this, "Date/heure invalide, utilisation de maintenant");
                        heureDepart = LocalDateTime.now();
                    }

                    simulationPanel.setVoitureDimensions(
                            voitureChoisie.getLargeur(),
                            voitureChoisie.getLongueur(),
                            voitureChoisie.getNom());

                    simulationPanel.setHeureDepart(heureDepart);
                    simulationPanel.setSens(sensChoisi);

                    simulationPanel.lancerSimulation(chemin, vitesse);

                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Vitesse invalide");
                } catch (CarburantExcpetion ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                } catch (IllegalArgumentException ex) {
                    JOptionPane.showMessageDialog(this, ex.getMessage());
                }
            });

            // Mise à jour info chemin
            lalanaComboBox.addActionListener(
                    e -> mettreAJourInfoChemin(infoCheminLabel, lalanaComboBox, cheminsTrouve, voitureChoisie));

        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur d'initialisation : " + e.getMessage());
        }
    }

    private void mettreAJourInfoChemin(
            JLabel infoCheminLabel,
            JComboBox<String> lalanaComboBox,
            Vector<Vector<Lalana>> cheminsTrouve,
            Voiture voitureChoisie) {

        int index = lalanaComboBox.getSelectedIndex();
        if (index < 0) {
            infoCheminLabel.setText("Distance : ");
            return;
        }

        Vector<Lalana> chemin = cheminsTrouve.get(index);
        double distance = LalanaService.calculerDistanceTotale(chemin);

        if (voitureChoisie != null) {
            double carburant = VoitureService.calculerCarburantNecessaire(chemin, voitureChoisie);

            infoCheminLabel.setText(
                    String.format(
                            "Distance : %.1f km   |  Carburant requis : %.2f L (Reservoir : %.2f L)",
                            distance,
                            carburant,
                            voitureChoisie.getReservoire()));
        } else {
            infoCheminLabel.setText(
                    String.format("Distance : %.1f km |", distance));
        }
    }

    /**
     * Vérifie si Tomcat est disponible à localhost:8080
     */
    private boolean isTomcatAvailable() {
        try {
            java.net.URL url = new java.net.URL("http://localhost:8080/");
            java.net.HttpURLConnection conn = (java.net.HttpURLConnection) url.openConnection();
            conn.setRequestMethod("HEAD");
            conn.setConnectTimeout(2000);
            conn.setReadTimeout(2000);
            int responseCode = conn.getResponseCode();
            conn.disconnect();
            return responseCode == 200;
        } catch (Exception e) {
            return false;
        }
    }

    public void close() {
        if (connOracle != null) {
            connOracle.closeConnection();
        }
    }
}
