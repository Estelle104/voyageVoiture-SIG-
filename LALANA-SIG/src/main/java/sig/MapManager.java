package sig;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import utildb.ConnexionOracle;

/**
 * Gestionnaire de la carte SIG
 * Démarre le serveur et ouvre la carte dans le navigateur
 */
public class MapManager {
    
    private static MapManager instance = null;
    private static SIGServer server = null;
    private static final int PORT = 8888;
    
    private MapManager() {
    }
    
    /**
     * Obtenir l'instance unique (Singleton)
     */
    public static MapManager getInstance() {
        if (instance == null) {
            instance = new MapManager();
        }
        return instance;
    }
    
    /**
     * Ouvrir la carte (démarre le serveur une seule fois)
     */
    public void openMap(ConnexionOracle conn) {
        try {
            // Démarrer le serveur une seule fois
            if (server == null) {
                server = new SIGServer(conn, PORT);
                server.start();
                
                // Attendre que le serveur soit prêt
                Thread.sleep(500);
            }
            
            // Ouvrir dans le navigateur
            openBrowser();
            
        } catch (IOException e) {
            System.err.println("❌ Erreur démarrage serveur SIG: " + e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Ouvrir l'URL dans le navigateur par défaut
     */
    private void openBrowser() {
        try {
            String url = "http://localhost:" + PORT;
            
            if (Desktop.isDesktopSupported()) {
                Desktop desktop = Desktop.getDesktop();
                
                if (desktop.isSupported(Desktop.Action.BROWSE)) {
                    desktop.browse(new URI(url));
                    System.out.println("🌐 Carte ouverte: " + url);
                } else {
                    System.err.println("⚠️  Action BROWSE non supportée");
                }
            } else {
                System.err.println("⚠️  Desktop non supporté");
            }
        } catch (IOException | URISyntaxException e) {
            System.err.println("❌ Erreur ouverture navigateur: " + e.getMessage());
        }
    }
    
    /**
     * Arrêter le serveur
     */
    public void stopServer() {
        if (server != null) {
            server.stop();
            server = null;
        }
    }
    
    /**
     * Vérifier si le serveur est en cours d'exécution
     */
    public boolean isRunning() {
        return server != null;
    }
}
