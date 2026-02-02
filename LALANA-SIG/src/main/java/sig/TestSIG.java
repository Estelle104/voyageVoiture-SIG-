package sig;

import utildb.ConnexionOracle;

/**
 * Test du système SIG
 * Vérifie que la carte démarre correctement
 */
public class TestSIG {
    
    public static void main(String[] args) {
        System.out.println("🧪 Test SIG - Démarre le serveur...\n");
        
        try {
            // Initialiser la connexion
            ConnexionOracle conn = new ConnexionOracle();
            
            // Tester le serveur
            System.out.println("✓ Connexion Oracle établie");
            
            // Démarrer le serveur SIG
            MapManager manager = MapManager.getInstance();
            manager.openMap(conn);
            
            System.out.println("\n✅ Serveur SIG démarré avec succès!");
            System.out.println("📍 URL: http://localhost:8888");
            System.out.println("\n💡 Conseil: Accédez à la carte via le bouton MainPanel");
            System.out.println("              ou directement via http://localhost:8888");
            
            // Garder le serveur actif
            System.out.println("\n⏸️  Appuyez sur Ctrl+C pour arrêter le serveur...");
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur test SIG:");
            e.printStackTrace();
        }
    }
}
