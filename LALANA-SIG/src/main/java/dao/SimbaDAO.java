package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import model.Simba;
import utildb.ConnexionOracle;

public class SimbaDAO {

    // CREATE
    public static void save(ConnexionOracle conn, Simba s) throws Exception {
        String sql = "INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection c = conn.getConnection();
                PreparedStatement pstmt = c.prepareStatement(sql, new String[] { "id" })) {

            pstmt.setString(1, s.getDescriptions());
            pstmt.setDouble(2, s.getPkDebut());
            pstmt.setDouble(3, s.getPkFin());
            pstmt.setDouble(4, s.getTauxRalentissement());
            pstmt.setDouble(5, s.getSurface());
            pstmt.setDouble(6, s.getProfondeur());
            pstmt.setInt(7, s.getIdLalana());
            pstmt.setInt(8, s.getIdLavaka());
            pstmt.setInt(9, s.getIdTypeMatiere());

            pstmt.executeUpdate();

            c.commit();

            // Récupérer l'id généré par la séquence via le trigger
            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    s.setId(rs.getInt(1));
                }
            }
        }
    }

    // READ by ID
    public static Simba findById(ConnexionOracle conn, int id) throws Exception {
        String sql = "SELECT * FROM Simba WHERE id = ?";
        Simba s = null;

        try (Connection c = conn.getConnection();
                PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    s = new Simba();
                    s.setId(rs.getInt("id"));
                    s.setDescriptions(rs.getString("descriptions"));
                    s.setPkDebut(rs.getDouble("pkDebut"));
                    s.setPkFin(rs.getDouble("pkFin"));
                    s.setTauxRalentissement(rs.getDouble("tauxRalentissement"));
                    s.setSurface(rs.getDouble("surface"));
                    s.setProfondeur(rs.getDouble("profondeur"));
                    s.setIdLalana(rs.getInt("idLalana"));
                    s.setIdLavaka(rs.getInt("idLavaka"));
                    s.setIdTypeMatiere(rs.getInt("idTypeMatiere"));
                }
            }
        }

        return s;
    }

    // GET all by Lalana
    public static Vector<Simba> getByIdLalana(ConnexionOracle conn, int idLalana) throws Exception {
        Vector<Simba> list = new Vector<>();
        String sql = "SELECT * FROM Simba WHERE idLalana = ?";

        try (Connection c = conn.getConnection();
                PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setInt(1, idLalana);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Simba s = new Simba();
                    s.setId(rs.getInt("id"));
                    s.setDescriptions(rs.getString("descriptions"));
                    s.setPkDebut(rs.getDouble("pkDebut"));
                    s.setPkFin(rs.getDouble("pkFin"));
                    s.setTauxRalentissement(rs.getDouble("tauxRalentissement"));
                    s.setSurface(rs.getDouble("surface"));
                    s.setProfondeur(rs.getDouble("profondeur"));
                    s.setIdLalana(rs.getInt("idLalana"));
                    s.setIdLavaka(rs.getInt("idLavaka"));
                    s.setIdTypeMatiere(rs.getInt("idTypeMatiere"));
                    list.add(s);
                }
            }
        }

        return list;
    }

    // DELETE
    public static void delete(ConnexionOracle conn, int id) throws Exception {
        String sql = "DELETE FROM Simba WHERE id = ?";

        try (Connection c = conn.getConnection();
                PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    // UPDATE
    public static void update(ConnexionOracle conn, Simba s) throws Exception {
        String sql = "UPDATE Simba SET descriptions=?, pkDebut=?, pkFin=?, tauxRalentissement=?, surface=?, profondeur=?, idLalana=?, idLavaka=?, idTypeMatiere=? "
                + "WHERE id=?";

        try (Connection c = conn.getConnection();
                PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setString(1, s.getDescriptions());
            pstmt.setDouble(2, s.getPkDebut());
            pstmt.setDouble(3, s.getPkFin());
            pstmt.setDouble(4, s.getTauxRalentissement());
            pstmt.setDouble(5, s.getSurface());
            pstmt.setDouble(6, s.getProfondeur());
            pstmt.setInt(7, s.getIdLalana());
            pstmt.setInt(8, s.getIdLavaka());
            pstmt.setInt(9, s.getIdTypeMatiere());
            pstmt.setInt(10, s.getId());

            pstmt.executeUpdate();

            c.commit();
        }
    }

    public static Vector<Simba> getSimbasBetweenPk(ConnexionOracle conn, int idLalana,
            double pkDebut, double pkFin) throws Exception {

        Vector<Simba> result = new Vector<>();

        for (Simba s : getByIdLalana(conn, idLalana)) {
            double pkSimbaDebut = s.getPkDebut();
            if (pkSimbaDebut >= pkDebut && pkSimbaDebut <= pkFin) {
                result.add(s);
            }
        }

        return result;
    }

    // GET ALL
    public static Vector<Simba> getAll(ConnexionOracle conn) throws Exception {
        Vector<Simba> list = new Vector<>();
        String sql = "SELECT * FROM Simba ORDER BY idLalana, pkDebut";

        try (Connection c = conn.getConnection();
                PreparedStatement pstmt = c.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Simba s = new Simba();
                s.setId(rs.getInt("id"));
                s.setDescriptions(rs.getString("descriptions"));
                s.setPkDebut(rs.getDouble("pkDebut"));
                s.setPkFin(rs.getDouble("pkFin"));
                s.setTauxRalentissement(rs.getDouble("tauxRalentissement"));
                s.setSurface(rs.getDouble("surface"));
                s.setProfondeur(rs.getDouble("profondeur"));
                s.setIdLalana(rs.getInt("idLalana"));
                s.setIdLavaka((Integer) rs.getObject("idLavaka"));
                s.setIdTypeMatiere(rs.getInt("idTypeMatiere"));
                list.add(s);
            }
        }
        return list;
    }

}
