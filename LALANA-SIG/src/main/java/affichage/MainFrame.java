package affichage;

import javax.swing.*;

public class MainFrame extends JFrame {

    public MainFrame() {
        setTitle("🚗 Simulation de Voyage");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 700);
        setLocationRelativeTo(null);

        try {
            MainPanel mainPanel = new MainPanel();
            setContentPane(mainPanel);
        } catch (Exception e) {
            e.printStackTrace();
            JOptionPane.showMessageDialog(this, "Erreur d'initialisation : " + e.getMessage());
            System.exit(1);
        }
    }
}
