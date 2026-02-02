package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import model.InfoVoyage;
import utildb.ConnexionOracle;

public class InfoVoyageDAO {

    // CREATE
    public static void save(ConnexionOracle conn, InfoVoyage gv) throws Exception {
        String sql = "INSERT INTO infoVoyage (depart, destination) VALUES (?, ?)";

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setString(1, gv.getDepart());
            pstmt.setString(2, gv.getDestination());
            pstmt.executeUpdate();

            c.commit();

        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    // READ ALL
    public static Vector<InfoVoyage> getAll(ConnexionOracle conn) throws Exception {
        Vector<InfoVoyage> list = new Vector<>();
        String sql = "SELECT id, depart, destination FROM infoVoyage";

        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                InfoVoyage gv = new InfoVoyage();
                gv.setId(rs.getInt("id"));
                gv.setDepart(rs.getString("depart"));
                gv.setDestination(rs.getString("destination"));
                list.add(gv);
            }
        } finally {
            // if (rs != null) rs.close();
            // if (pstmt != null) pstmt.close();
        }
        return list;
    }

    // UPDATE
    public static void update(ConnexionOracle conn, InfoVoyage gv) throws Exception {
        String sql = "UPDATE infoVoyage SET depart=?, destination=? WHERE id=?";

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setString(1, gv.getDepart());
            pstmt.setString(2, gv.getDestination());
            pstmt.setInt(3, gv.getId());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    // DELETE
    public static void delete(ConnexionOracle conn, int id) throws Exception {
        String sql = "DELETE FROM infoVoyage WHERE id=?";

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }
}
