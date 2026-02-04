package utildb;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionPSQL {

    private Connection connection;

    public Connection getConnection() throws Exception {
        if (this.connection == null) {
            try {
                Class.forName("org.postgresql.Driver");
                System.out.println("✅ Driver PostgreSQL chargé");

                // URL de connexion PostgreSQL
                // Format : jdbc:postgresql://host:port/database
                String url = "jdbc:postgresql://localhost:5432/voyage_sig";
                String user = "postgres";
                String password = "4185";

                System.out.println("🔄 Tentative de connexion: " + url);
                this.connection = DriverManager.getConnection(url, user, password);
                System.out.println("✅ Connexion réussie à PostgreSQL");

                // Désactiver l'auto-commit pour gérer manuellement commit/rollback
                this.connection.setAutoCommit(false);

            } catch (ClassNotFoundException e) {
                System.err.println("❌ Driver PostgreSQL non trouvé: " + e.getMessage());
                throw new Exception("Driver PostgreSQL non trouvé: " + e.getMessage());
            } catch (SQLException e) {
                System.err.println("❌ Erreur SQL: " + e.getMessage());
                System.err.println("   SQL State: " + e.getSQLState());
                System.err.println("   Error Code: " + e.getErrorCode());
                throw new Exception("Erreur de connexion PostgreSQL: " + e.getMessage());
            }
        }
        return this.connection;
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                connection = null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void commit() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.commit();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void rollback() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.rollback();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
