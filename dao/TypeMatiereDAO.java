package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import model.TypeMatiere;
import utildb.ConnexionOracle;

public class TypeMatiereDAO {

    // CREATE
    public static void save(ConnexionOracle conn, TypeMatiere tm) throws Exception {
        String sql = "INSERT INTO typeMatiere (typeMatiere, descriptions) VALUES (?, ?)";
    
        Connection c = null;
        PreparedStatement pstmt = null;
    
        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setString(1, tm.getTypeMatiere());
            pstmt.setString(2, tm.getDescriptions());
            pstmt.executeUpdate();

            c.commit();

        } finally {
            if (pstmt != null) pstmt.close();
        }
    }
    
    // READ BY ID
    public static TypeMatiere findById(ConnexionOracle conn, int id) throws Exception {
        String sql = "SELECT * FROM typeMatiere WHERE id = ?";

        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        TypeMatiere tm = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();

            if (rs.next()) {
                tm = new TypeMatiere();
                tm.setId(rs.getInt("id"));
                tm.setTypeMatiere(rs.getString("typeMatiere"));
                tm.setDescriptions(rs.getString("descriptions"));
            }
        } finally {
            if (rs != null) rs.close();
            if (pstmt != null) pstmt.close();
        }
        return tm;
    }

    public static Vector<TypeMatiere> getAll(ConnexionOracle conn) throws Exception {
        Vector<TypeMatiere> list = new Vector<>();
        String sql = "SELECT ID, TYPEMATIERE, DESCRIPTIONS FROM TYPEMATIERE";

        try (
                Connection c = conn.getConnection();
                PreparedStatement pstmt = c.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                TypeMatiere tm = new TypeMatiere();

                tm.setId(rs.getInt("ID"));
                tm.setTypeMatiere(rs.getString("TYPEMATIERE"));
                tm.setDescriptions(rs.getString("DESCRIPTIONS"));

                list.add(tm);
            }
        }
        return list;
    }

    // UPDATE
    public static void update(ConnexionOracle conn, TypeMatiere tm) throws Exception {
        String sql = "UPDATE typeMatiere SET typeMatiere = ?, descriptions = ? WHERE id = ?";

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setString(1, tm.getTypeMatiere());
            pstmt.setString(2, tm.getDescriptions());
            pstmt.setInt(3, tm.getId());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null) pstmt.close();
        }
    }

    // DELETE
    public static void delete(ConnexionOracle conn, int id) throws Exception {
        String sql = "DELETE FROM typeMatiere WHERE id = ?";

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
