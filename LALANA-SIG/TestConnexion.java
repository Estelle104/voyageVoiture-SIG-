import utildb.ConnexionOracle;

public class TestConnexion {
    public static void main(String[] args) {
        ConnexionOracle conn = new ConnexionOracle();
        try {
            conn.getConnection();
            System.out.println("Connexion Oracle OK !");
        } catch (Exception e) {
            System.err.println("Erreur de connexion : " + e.getMessage());
            e.printStackTrace();
        } finally {
            conn.closeConnection();
        }
    }
}
