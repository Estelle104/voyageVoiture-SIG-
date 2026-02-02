package affichage;

import model.Lalana;
import model.Lavaka;
import model.Pause;
import model.SensVoyage;
import service.InfoVoyageService;
import service.VoitureService;

import javax.swing.*;
import java.awt.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Vector;

public class SimulationPanel extends JPanel {

    private Vector<Lalana> chemin;
    private int segmentIndex = 0;
    private double position = 0; // proportion segment courant (0..1)
    private double vitesse; 
    private double voitureLargeur = 1.8; 
    private double voitureLongueur = 4.5; 
    private double xProportionLalanaVoiture = 2.0;
    private String nomVoiture;

    private Timer timer;
    private LocalDateTime heureDepart;
    private SensVoyage sens = SensVoyage.ALLER;

    private double tempsEcoule = 0; // en heures

    private static final int MARGIN = 40;
    private static final int ROAD_WIDTH = 20;

    public SimulationPanel() {
        setBackground(Color.WHITE);
    }

    public void setVoitureDimensions(double largeur, double longueur, String nomVoiture) {
        this.voitureLargeur = largeur;
        this.voitureLongueur = longueur;
        this.nomVoiture = nomVoiture;
    }

    public void setHeureDepart(LocalDateTime heureDepart) {
        this.heureDepart = heureDepart;
    }

    public void setSens(SensVoyage sens) {
        this.sens = sens;
    }

    public void lancerSimulation(Vector<Lalana> chemin, double vitesse) {
        this.chemin = chemin;
        this.vitesse = vitesse;
        this.segmentIndex = 0;
        this.position = 0;
        this.tempsEcoule = 0;

        if (timer != null)
            timer.stop();

        timer = new Timer(40, e -> avancer());
        timer.start();
    }

    private void avancer() {
        if (chemin == null || segmentIndex >= chemin.size()) {
            if (timer != null) timer.stop();
            return;
        }

        Lalana l = chemin.get(segmentIndex);

        double positionKm = position * l.getDistance();

        // vitesse selon lavakas
        double vitesseEffective = InfoVoyageService.calculVitesseSurLalana(
                l, vitesse, positionKm);

        // distance parcourue par tick (40ms -> 10s approx)
        double distanceParTick = vitesseEffective * 10 / 3600.0; // km

        // verifier si pause en cours
        if (pauseEnCours(l, positionKm, getHeureCourante())) {
            tempsEcoule += 10.0 / 3600.0; // temps passe mais voiture fixe
            repaint();
            return;
        }

        // progression
        position += distanceParTick / l.getDistance();
        tempsEcoule += distanceParTick / vitesseEffective;

        if (position >= 1) {
            position = 0;
            segmentIndex++;
        }

        repaint();
    }

    private LocalDateTime getHeureCourante() {
        if (heureDepart == null) return LocalDateTime.now();
        return heureDepart.plusSeconds((long) (tempsEcoule * 3600));
    }

    private boolean pauseEnCours(Lalana l, double positionKm, LocalDateTime heureCourante) {
        if (l.getPauses() == null) return false;

        for (Pause p : l.getPauses()) {
            if (Math.abs(positionKm - p.getPosition()) < 0.05) {
                // verifie l'heure
                if (!heureCourante.isBefore(p.getHeureDebut()) &&
                    !heureCourante.isAfter(p.getHeureFin())) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (chemin == null) return;

        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING,
                            RenderingHints.VALUE_ANTIALIAS_ON);

        int x = MARGIN;
        int y = getHeight() / 2;

        for (int i = 0; i < chemin.size(); i++) {
            Lalana l = chemin.get(i);
            int length = (int) (l.getDistance() * 3);
            int y2 = y;
            float lWidth = ROAD_WIDTH * (float) l.getLargeur();

            // route
            g2.setColor(new Color(130, 130, 130));
            g2.setStroke(new BasicStroke(lWidth,
                    BasicStroke.CAP_BUTT, BasicStroke.JOIN_ROUND));
            g2.drawLine(x, y, x + length, y2);

            // bords
            g2.setColor(Color.BLACK);
            g2.setStroke(new BasicStroke(2));
            g2.drawLine(x, (int)(y - lWidth/2), x + length, (int)(y2 - lWidth/2));
            g2.drawLine(x, (int)(y + lWidth/2), x + length, (int)(y2 + lWidth/2));

            // ligne centrale
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2, BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_BEVEL, 0, new float[]{10,10},0));
            g2.drawLine(x, y, x + length, y2);

            // lavakas
            if (l.getLavakas() != null) {
                for (Lavaka lavaka : l.getLavakas()) {
                    double startRatio = lavaka.getDebutLavaka() / l.getDistance();
                    double endRatio   = lavaka.getFinLavaka() / l.getDistance();
                    int lavakaX = (int)(x + startRatio * length);
                    int lavakaWidth = (int)((endRatio - startRatio) * length);
                    int lavakaHeight = (int)(lWidth * 0.6);

                    g2.setColor(new Color(80,30,30));
                    g2.fillRect(lavakaX, (int)(y - lavakaHeight/2),
                                lavakaWidth, lavakaHeight);
                    g2.setColor(Color.RED);
                    g2.drawRect(lavakaX, (int)(y - lavakaHeight/2),
                                lavakaWidth, lavakaHeight);
                }
            }

            // voiture
            if (i == segmentIndex) {
                int cx = (int)(x + position * length);
                int cy = y + 10;
                double scale = ROAD_WIDTH / xProportionLalanaVoiture;
                int carWidthPx = (int)(voitureLargeur * scale);
                int carLengthPx = (int)(voitureLongueur * scale);
                int voieOffset = ROAD_WIDTH / 6;

                g2.setColor(Color.BLUE);
                g2.fillRect(cx - carLengthPx/2,
                            cy + voieOffset - carWidthPx/2,
                            carLengthPx, carWidthPx);
                g2.setColor(Color.BLACK);
                g2.drawRect(cx - carLengthPx/2,
                            cy + voieOffset - carWidthPx/2,
                            carLengthPx, carWidthPx);
            }

            g2.setColor(Color.BLACK);
            g2.drawString(l.getNomLalana(), x, y - 25);

            x += length;
            y = y2;
        }

        drawInfoPanel(g2);
    }

    private void drawInfoPanel(Graphics2D g2) {
        int panelWidth  = 220;
        int panelHeight = 160;
        int margin = 15;
    
        int panelX = getWidth() - panelWidth - margin;
        int panelY = margin;
    
        // fond semi-transparent
        g2.setColor(new Color(0,0,0,140));
        g2.fillRoundRect(panelX, panelY, panelWidth, panelHeight, 12,12);
    
        // bordure
        g2.setColor(Color.WHITE);
        g2.setStroke(new BasicStroke(1.5f));
        g2.drawRoundRect(panelX, panelY, panelWidth, panelHeight, 12,12);
    
        // titre
        g2.setFont(new Font("SansSerif", Font.BOLD,13));
        g2.drawString("INFOS DE VOYAGE", panelX + 12, panelY + 18);
    
        // ligne de séparation
        g2.setStroke(new BasicStroke(1));
        g2.drawLine(panelX + 10, panelY + 22, panelX + panelWidth - 10, panelY + 22);
    
        g2.setFont(new Font("SansSerif", Font.PLAIN,12));
        int textY = panelY + 40;
        int spacing = 16;
    
        if (chemin != null && !chemin.isEmpty()) {
            // Vitesse moyenne réelle sur tout le chemin (toujours affichée)
            double vitesseMoyenneReelle = InfoVoyageService.calculerVitesseMoyenneReelle(chemin, vitesse);
            g2.drawString(String.format("Vitesse moyenne reel : %.2f km/h", vitesseMoyenneReelle),
                          panelX + 12, textY);
            textY += spacing;
    
            // Vitesse sur le segment courant seulement si le voyage n'est pas terminé
            if (segmentIndex < chemin.size()) {
                Lalana l = chemin.get(segmentIndex);
                double positionKm = position * l.getDistance();
                double vitesseSegment = InfoVoyageService.calculVitesseSurLalana(l, vitesse, positionKm);
                g2.drawString(String.format("Vitesse segment : %.2f km/h", vitesseSegment),
                              panelX + 12, textY);
                textY += spacing;
            }
        }
    
        // Temps écoulé
        g2.drawString(String.format("Temps ecoule : %.2f h", tempsEcoule),
                      panelX + 12, textY);
        textY += spacing;
    
        // Temps estimé et heure d'arrivée
        if (heureDepart != null && chemin != null && !chemin.isEmpty()) {
            Duration dureeTotale = VoitureService.calculerTempsTotal(
                    chemin, vitesse, heureDepart, sens);
            double tempsEstimeH = dureeTotale.toMinutes() / 60.0;
            g2.drawString(String.format("Temps estime : %.2f h", tempsEstimeH),
                          panelX + 12, textY);
            textY += spacing;
    
            LocalDateTime heureArrivee = heureDepart.plus(dureeTotale);
            g2.drawString("Arrivee prevue : " + heureArrivee.toLocalTime(),
                          panelX + 12, textY);
            textY += spacing;
        }
    
        // Informations voiture
        if (nomVoiture != null) {
            g2.drawString("Voiture : " + nomVoiture, panelX + 12, textY);
            textY += spacing;
        }
    
        g2.drawString(String.format("Vitesse max : %.0f km/h", vitesse),
                      panelX + 12, textY);
    }
        
}
