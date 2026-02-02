package utildb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnexionOracle {

    private Connection connection;
    public Connection getConnection() throws Exception {
        try {
            if (this.connection == null || this.connection.isClosed() || !this.connection.isValid(2)) {
                Class.forName("oracle.jdbc.OracleDriver");
                String url = "jdbc:oracle:thin:@//localhost:1521/EE.oracle.docker";
                String user = "voyageSIG";
                String password = "voyageSIG";
    
                this.connection = DriverManager.getConnection(url, user, password);
                this.connection.setAutoCommit(false);
            }
        } catch (ClassNotFoundException | SQLException e) {
            throw new Exception("Connection tsy mety " + e.getMessage());
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