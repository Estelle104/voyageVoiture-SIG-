package main;

import affichage.MainFrame;

import javax.swing.*;
import java.awt.*;

/**
 * Lanceur unique pour l'application VoyageVoiture
 * Démarre l'interface Swing avec la carte SIG JSP intégrée
 */
public class AppLauncher {

    public static void main(String[] args) {
        System.out.println("🚀 Démarrage de VoyageVoiture...");

        // Afficher un écran de démarrage
        showSplashScreen();

        // Lancer l'application Swing
        launchSwingApp();
    }

    /**
     * Affiche un écran de démarrage
     */
    private static void showSplashScreen() {
        JWindow splash = new JWindow();
        JPanel panel = new JPanel();
        panel.setBackground(new Color(28, 107, 173));
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.setBorder(BorderFactory.createEmptyBorder(40, 40, 40, 40));

        JLabel title = new JLabel("🚗 VoyageVoiture SIG");
        title.setFont(new Font("Arial", Font.BOLD, 28));
        title.setForeground(Color.WHITE);
        title.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel subtitle = new JLabel("Simulation de voyage avec routes nationales");
        subtitle.setFont(new Font("Arial", Font.PLAIN, 14));
        subtitle.setForeground(Color.WHITE);
        subtitle.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        JLabel loading = new JLabel("Chargement...");
        loading.setFont(new Font("Arial", Font.ITALIC, 12));
        loading.setForeground(new Color(200, 200, 200));
        loading.setAlignmentX(JLabel.CENTER_ALIGNMENT);

        panel.add(Box.createVerticalStrut(10));
        panel.add(title);
        panel.add(Box.createVerticalStrut(10));
        panel.add(subtitle);
        panel.add(Box.createVerticalStrut(20));
        panel.add(loading);

        splash.setContentPane(panel);
        splash.setSize(500, 250);
        splash.setLocationRelativeTo(null);
        splash.setVisible(true);

        // Fermer après 2 secondes
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                splash.dispose();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    /**
     * Lance l'application Swing
     */
    private static void launchSwingApp() {
        SwingUtilities.invokeLater(() -> {
            MainFrame frame = new MainFrame();
            frame.setVisible(true);
        });
    }
}
