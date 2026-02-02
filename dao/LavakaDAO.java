package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import model.Lavaka;
import utildb.ConnexionOracle;

public class LavakaDAO {

    // CREATE
    public static void save(ConnexionOracle conn, Lavaka lavaka) throws Exception {
        String sql = "INSERT INTO lavaka (idLalana, debutLavaka, finLavaka, tauxRalentissement) VALUES (?, ?, ?, ?)";

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, lavaka.getIdLalana());
            pstmt.setDouble(2, lavaka.getDebutLavaka());
            pstmt.setDouble(3, lavaka.getFinLavaka());
            pstmt.setDouble(4, lavaka.getTauxRalentissement());
            pstmt.executeUpdate();

            c.commit();

        } finally {
            if (pstmt != null) pstmt.close();
        }
    }
    
    // READ ALL
    public static Vector<Lavaka> getAll(ConnexionOracle conn) throws Exception {
        Vector<Lavaka> list = new Vector<>();
        String sql = "SELECT id, idLalana, debutLavaka, finLavaka, tauxRalentissement FROM lavaka";

        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Lavaka l = new Lavaka();
                l.setId(rs.getInt("id"));
                l.setIdLalana(rs.getInt("idLalana"));
                l.setDebutLavaka(rs.getDouble("debutLavaka"));
                l.setFinLavaka(rs.getDouble("finLavaka"));
                l.setTauxRalentissement(rs.getDouble("tauxRalentissement"));
                list.add(l);
            }
        } finally {
            if (rs != null)
                rs.close();
            if (pstmt != null)
                pstmt.close();
        }
        return list;
    }

    // UPDATE
    public static void update(ConnexionOracle conn, Lavaka lavaka) throws Exception {
        String sql = "UPDATE lavaka SET idLalana=?, debutLavaka=?, finLavaka=?, tauxRalentissement=? WHERE id=?";

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, lavaka.getIdLalana());
            pstmt.setDouble(2, lavaka.getDebutLavaka());
            pstmt.setDouble(3, lavaka.getFinLavaka());
            pstmt.setDouble(4, lavaka.getTauxRalentissement());
            pstmt.setInt(5, lavaka.getId());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null)
                pstmt.close();
        }
    }

    // DELETE
    public static void delete(ConnexionOracle conn, int id) throws Exception {
        String sql = "DELETE FROM lavaka WHERE id=?";

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

    // public static Vector<Lavaka> getByIdLalana(ConnexionOracle conn, int
    // idLalana) throws Exception {
    // Vector<Lavaka> list = new Vector<>();
    // String sql = "SELECT id, idLalana, debutLavaka, finLavaka, tauxRalentissement
    // "
    // + "FROM lavaka WHERE idLalana = ?";

    // Connection c = null;
    // PreparedStatement pstmt = null;
    // ResultSet rs = null;

    // try {
    // c = conn.getConnection();
    // pstmt = c.prepareStatement(sql);
    // pstmt.setInt(1, idLalana);
    // rs = pstmt.executeQuery();

    // while (rs.next()) {
    // Lavaka l = new Lavaka();
    // l.setId(rs.getInt("id"));
    // l.setIdLalana(rs.getInt("idLalana"));
    // l.setDebutLavaka(rs.getDouble("debutLavaka"));
    // l.setFinLavaka(rs.getDouble("finLavaka"));
    // l.setTauxRalentissement(rs.getDouble("tauxRalentissement"));
    // list.add(l);
    // }
    // } finally {
    // if (rs != null) rs.close();
    // if (pstmt != null) pstmt.close();
    // }
    // return list;
    // }

    public static Vector<Lavaka> getByLalana(
            ConnexionOracle conn,
            int idLalana) throws Exception {

        Vector<Lavaka> result = new Vector<>();

        String sql = """
                    SELECT id, idLalana, debutLavaka, finLavaka, tauxRalentissement
                    FROM Lavaka
                    WHERE idLalana = ?
                """;

        PreparedStatement ps = conn.getConnection().prepareStatement(sql);
        ps.setInt(1, idLalana);

        ResultSet rs = ps.executeQuery();
        while (rs.next()) {
            result.add(new Lavaka(
                    rs.getInt("id"),
                    rs.getInt("idLalana"),
                    rs.getDouble("debutLavaka"),
                    rs.getDouble("finLavaka"),
                    rs.getDouble("tauxRalentissement")));
        }
        return result;
    }

}
