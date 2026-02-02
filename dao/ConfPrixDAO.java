package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import model.ConfPrix;
import utildb.ConnexionOracle;

public class ConfPrixDAO {

    // CREATE
    public static void save(ConnexionOracle conn, ConfPrix cp) throws Exception {
        String sql = "INSERT INTO confPrix ( min, max, prixMC, idTypeMatiere) VALUES ( ?, ?, ?, ?)";

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setDouble(1, cp.getMin());
            pstmt.setDouble(2, cp.getMax());
            pstmt.setDouble(3, cp.getPrixMC());
            pstmt.setInt(4, cp.getIdTypeMatiere());
            pstmt.executeUpdate();

            c.commit();
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }

    }

    public static ConfPrix findByTypeAndProfondeur(
            ConnexionOracle co,
            int idTypeMatiere,
            double profondeur) throws Exception {

        String sql = """
                    SELECT * FROM confPrix
                    WHERE idTypeMatiere = ?
                    AND ? BETWEEN min AND max
                """;

        PreparedStatement ps = co.getConnection().prepareStatement(sql);
        ps.setInt(1, idTypeMatiere);
        ps.setDouble(2, profondeur);

        ResultSet rs = ps.executeQuery();
        if (rs.next()) {
            ConfPrix c = new ConfPrix();
            c.setId(rs.getInt("id"));
            c.setMin(rs.getDouble("min"));
            c.setMax(rs.getDouble("max"));
            c.setPrixMC(rs.getDouble("prixMC"));
            c.setIdTypeMatiere(rs.getInt("idTypeMatiere"));
            return c;
        }
        return null;
    }

    // READ BY ID
    public static ConfPrix findById(ConnexionOracle conn, int id) throws Exception {
        String sql = "SELECT * FROM confPrix WHERE id = ?";

        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ConfPrix cp = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                cp = new ConfPrix();
                cp.setId(rs.getInt("id"));
                cp.setMin(rs.getDouble("min"));
                cp.setMax(rs.getDouble("max"));
                cp.setPrixMC(rs.getDouble("prixMC"));
                cp.setIdTypeMatiere(rs.getInt("idTypeMatiere"));
            }
        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
        }
        return cp;
    }

    // UPDATE
    public static void update(ConnexionOracle conn, ConfPrix cp) throws Exception {
        String sql = "UPDATE confPrix SET min=?, max=?, prixMC=?, idTypeMatiere=? WHERE id=?";

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setDouble(1, cp.getMin());
            pstmt.setDouble(2, cp.getMax());
            pstmt.setDouble(3, cp.getPrixMC());
            pstmt.setInt(4, cp.getIdTypeMatiere());
            pstmt.setInt(5, cp.getId());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null)
                pstmt.close();
        }
    }

    public static Vector<ConfPrix> getAll(ConnexionOracle conn) throws Exception {
        Vector<ConfPrix> list = new Vector<>();
        String sql = "SELECT ID, MIN, MAX, PRIXMC, IDTYPEMATIERE FROM CONFPRIX";

        try (
                Connection c = conn.getConnection();
                PreparedStatement pstmt = c.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                ConfPrix cp = new ConfPrix();

                cp.setId(rs.getInt("ID"));
                cp.setMin(rs.getDouble("MIN"));
                cp.setMax(rs.getDouble("MAX"));
                cp.setPrixMC(rs.getDouble("PRIXMC"));
                cp.setIdTypeMatiere(rs.getInt("IDTYPEMATIERE"));

                list.add(cp);
            }
        }
        return list;
    }

    // DELETE
    public static void delete(ConnexionOracle conn, int id) throws Exception {
        String sql = "DELETE FROM confPrix WHERE id = ?";

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null)
                pstmt.close();
        }
    }
}
