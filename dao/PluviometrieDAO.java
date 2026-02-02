package dao;

import java.sql.*;
import java.util.Vector;

import model.Pluviometrie;
import utildb.ConnexionOracle;

public class PluviometrieDAO {

    /* ===================== INSERT ===================== */
    public static void save(ConnexionOracle conn, Pluviometrie p) throws Exception {
        String sql = "INSERT INTO pluviometrie (tauxPluie, pkDebut, pkFin) VALUES (?, ?, ?)";

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);

            pstmt.setDouble(1, p.getTauxPluie());
            pstmt.setDouble(2, p.getPkDebut());
            pstmt.setDouble(3, p.getPkFin());

            pstmt.executeUpdate();
            c.commit();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    /* ===================== UPDATE ===================== */
    public static void update(ConnexionOracle conn, Pluviometrie p) throws Exception {
        String sql = "UPDATE pluviometrie SET tauxPluie = ?, pkDebut = ?, pkFin = ? WHERE id = ?";

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);

            pstmt.setDouble(1, p.getTauxPluie());
            pstmt.setDouble(2, p.getPkDebut());
            pstmt.setDouble(3, p.getPkFin());
            pstmt.setInt(4, p.getId());

            pstmt.executeUpdate();
            c.commit();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    /* ===================== DELETE ===================== */
    public static void delete(ConnexionOracle conn, int id) throws Exception {
        String sql = "DELETE FROM pluviometrie WHERE id = ?";

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
    public static Pluviometrie findById(ConnexionOracle conn, int id) throws Exception {
        String sql = "SELECT * FROM pluviometrie WHERE id = ?";
        Pluviometrie p = null;

        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, id);

            rs = pstmt.executeQuery();
            if (rs.next()) {
                p = new Pluviometrie();
                p.setId(rs.getInt("id"));
                p.setTauxPluie(rs.getDouble("tauxPluie"));
                p.setPkDebut(rs.getDouble("pkDebut"));
                p.setPkFin(rs.getDouble("pkFin"));
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return p;
    }

    /* ===================== FIND ALL ===================== */
    public static Vector<Pluviometrie> findAll(ConnexionOracle conn) throws Exception {
        String sql = "SELECT * FROM pluviometrie";
        Vector<Pluviometrie> list = new Vector<>();

        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                Pluviometrie p = new Pluviometrie();
                p.setId(rs.getInt("id"));
                p.setTauxPluie(rs.getDouble("tauxPluie"));
                p.setPkDebut(rs.getDouble("pkDebut"));
                p.setPkFin(rs.getDouble("pkFin"));
                list.add(p);
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return list;
    }
}
