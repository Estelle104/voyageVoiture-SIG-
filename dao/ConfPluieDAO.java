package dao;

import java.sql.*;
import java.util.Vector;

import model.ConfPluie;
import utildb.ConnexionOracle;

public class ConfPluieDAO {

    /* ===================== INSERT ===================== */
    public static void save(ConnexionOracle conn, ConfPluie cp) throws Exception {
        String sql = "INSERT INTO confPluie (minP, maxP, idTypeMatiere) VALUES (?, ?, ?)";

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);

            pstmt.setDouble(1, cp.getMinP());
            pstmt.setDouble(2, cp.getMaxP());
            pstmt.setInt(3, cp.getIdTypeMatiere());

            pstmt.executeUpdate();
            c.commit();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    /* ===================== UPDATE ===================== */
    public static void update(ConnexionOracle conn, ConfPluie cp) throws Exception {
        String sql = "UPDATE confPluie SET minP = ?, maxP = ?, idTypeMatiere = ? WHERE id = ?";

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);

            pstmt.setDouble(1, cp.getMinP());
            pstmt.setDouble(2, cp.getMaxP());
            pstmt.setInt(3, cp.getIdTypeMatiere());
            pstmt.setInt(4, cp.getId());

            pstmt.executeUpdate();
            c.commit();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    /* ===================== DELETE ===================== */
    public static void delete(ConnexionOracle conn, int id) throws Exception {
        String sql = "DELETE FROM confPluie WHERE id = ?";

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            c.commit();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    /* ===================== FIND BY ID ===================== */
    public static ConfPluie findById(ConnexionOracle conn, int id) throws Exception {
        String sql = "SELECT * FROM confPluie WHERE id = ?";
        ConfPluie cp = null;

        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                cp = new ConfPluie();
                cp.setId(rs.getInt("id"));
                cp.setMinP(rs.getDouble("minP"));
                cp.setMaxP(rs.getDouble("maxP"));
                cp.setIdTypeMatiere(rs.getInt("idTypeMatiere"));
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return cp;
    }

    /* ===================== FIND ALL ===================== */
    public static Vector<ConfPluie> findAll(ConnexionOracle conn) throws Exception {
        String sql = "SELECT * FROM confPluie";
        Vector<ConfPluie> list = new Vector<>();

        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                ConfPluie cp = new ConfPluie();
                cp.setId(rs.getInt("id"));
                cp.setMinP(rs.getDouble("minP"));
                cp.setMaxP(rs.getDouble("maxP"));
                cp.setIdTypeMatiere(rs.getInt("idTypeMatiere"));
                list.add(cp);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return list;
    }
}
