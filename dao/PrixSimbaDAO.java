package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import model.PrixSimba;
import utildb.ConnexionOracle;

public class PrixSimbaDAO {

    public static void save(ConnexionOracle conn, PrixSimba ps) throws Exception {
        String sql = "INSERT INTO prixSimba (idSimba, idLalana, prixSimba) VALUES (?, ?, ?)";

        try (Connection c = conn.getConnection();
             PreparedStatement pstmt = c.prepareStatement(sql, new String[]{"id"})) {

            pstmt.setInt(1, ps.getIdSimba());
            pstmt.setInt(2, ps.getIdLalana());
            pstmt.setDouble(3, ps.getPrixSimba());

            pstmt.executeUpdate();

            c.commit();

            try (ResultSet rs = pstmt.getGeneratedKeys()) {
                if (rs.next()) {
                    ps.setId(rs.getInt(1));
                }
            }
        }
    }

    public static PrixSimba findBySimbaAndLalana(ConnexionOracle co, int idSimba, int idLalana) throws Exception {
        String query = "SELECT * FROM prixSimba WHERE idSimba = ? AND idLalana = ? ORDER BY id DESC";
        PrixSimba ps = null;

        try (Connection c = co.getConnection();
             PreparedStatement pstmt = c.prepareStatement(query)) {

            pstmt.setInt(1, idSimba);
            pstmt.setInt(2, idLalana);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ps = new PrixSimba();
                    ps.setId(rs.getInt("id"));
                    ps.setIdSimba(rs.getInt("idSimba"));
                    ps.setIdLalana(rs.getInt("idLalana"));
                    ps.setPrixSimba(rs.getDouble("prixSimba"));
                }
            }
        }
        return ps;
    }

    public static boolean existsBySimbaAndLalana(ConnexionOracle co, int idSimba, int idLalana) throws Exception {
        return findBySimbaAndLalana(co, idSimba, idLalana) != null;
    }

    public static Vector<PrixSimba> getAll(ConnexionOracle conn) throws Exception {
        Vector<PrixSimba> list = new Vector<>();
        String sql = "SELECT id, idSimba, idLalana, prixSimba FROM prixSimba";

        try (Connection c = conn.getConnection();
             PreparedStatement pstmt = c.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                PrixSimba ps = new PrixSimba();
                ps.setId(rs.getInt("id"));
                ps.setIdSimba(rs.getInt("idSimba"));
                ps.setIdLalana(rs.getInt("idLalana"));
                ps.setPrixSimba(rs.getDouble("prixSimba"));
                list.add(ps);
            }
        }
        return list;
    }

    public static void delete(ConnexionOracle conn, int id) throws Exception {
        String sql = "DELETE FROM prixSimba WHERE id = ?";

        try (Connection c = conn.getConnection();
             PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        }
    }

    public static PrixSimba findById(ConnexionOracle conn, int id) throws Exception {
        String sql = "SELECT * FROM prixSimba WHERE id = ?";
        PrixSimba ps = null;

        try (Connection c = conn.getConnection();
             PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    ps = new PrixSimba();
                    ps.setId(rs.getInt("id"));
                    ps.setIdSimba(rs.getInt("idSimba"));
                    ps.setIdLalana(rs.getInt("idLalana"));
                    ps.setPrixSimba(rs.getDouble("prixSimba"));
                }
            }
        }

        return ps;
    }

    public static void update(ConnexionOracle conn, PrixSimba ps) throws Exception {
        String sql = "UPDATE prixSimba SET idSimba=?, idLalana=?, prixSimba=? WHERE id=?";

        try (Connection c = conn.getConnection();
             PreparedStatement pstmt = c.prepareStatement(sql)) {

            pstmt.setInt(1, ps.getIdSimba());
            pstmt.setInt(2, ps.getIdLalana());
            pstmt.setDouble(3, ps.getPrixSimba());
            pstmt.setInt(4, ps.getId());
            
            int rowsAffected = pstmt.executeUpdate();
            System.out.println("Ligne mis a jour : " + rowsAffected + " pour PrixSimba ID: " + ps.getId());
            
            c.commit();
            // System.out.println("Commit effectue pour PrixSimba ID: " + ps.getId());
        } catch (Exception e) {
            System.out.println("Erreur lors de l'UPDATE : " + e.getMessage());
            throw e;
        }
    }
}
