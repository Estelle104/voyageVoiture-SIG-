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

                // URL de connexion PostgreSQL
                // Format : jdbc:postgresql://host:port/database
                String url = "jdbc:postgresql://localhost:5432/voyage_sig";
                String user = "postgres";
                String password = "4185";

                this.connection = DriverManager.getConnection(url, user, password);

                // Désactiver l'auto-commit pour gérer manuellement commit/rollback
                this.connection.setAutoCommit(false);

            } catch (ClassNotFoundException | SQLException e) {
                throw new Exception("Connexion tsy mety " + e.getMessage());
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
