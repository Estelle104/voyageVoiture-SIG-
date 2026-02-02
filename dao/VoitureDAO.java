package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import model.Voiture;
import utildb.ConnexionPSQL;

public class VoitureDAO {
    public static void save(ConnexionPSQL conn,Voiture voiture) throws Exception {
        String sql = "INSERT INTO voiture (nom, type,vitesseMax, largeur, longueur, reservoire, consommation) VALUES (?, ?, ?, ?, ?,?,?)";
        
        Connection c = null;
        PreparedStatement pstmt = null;
        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setString(1, voiture.getNom());
            pstmt.setString(2, voiture.getType());
            pstmt.setDouble(3, voiture.getVitesseMax());
            pstmt.setDouble(4, voiture.getLargeur());
            pstmt.setDouble(5, voiture.getLongueur());
            pstmt.setDouble(5, voiture.getLongueur());
            pstmt.setDouble(6, voiture.getReservoire());
            pstmt.setDouble(7, voiture.getConsommation());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // try {
            //     if (pstmt != null) {
            //         pstmt.close();
            //     }
                
            // } catch (SQLException ex) {
            //     ex.printStackTrace();
            // }
        }
    }

    public static Vector<Voiture> getAllVoiture(ConnexionPSQL conn) throws Exception{
        Vector<Voiture> voitures = new Vector<>();
        String sql = "SELECT id, nom, type, vitesseMax, longueur, largeur, reservoire, consommation FROM voiture";
        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            rs = pstmt.executeQuery();
            while (rs.next()) {
                Voiture voiture = new Voiture();
                voiture.setId(rs.getInt("id"));
                voiture.setNom(rs.getString("nom"));
                voiture.setType(rs.getString("type"));
                voiture.setVitesseMax(rs.getDouble("vitesseMax"));
                voiture.setLongueur(rs.getDouble("longueur"));
                voiture.setLargeur(rs.getDouble("largeur"));
                voiture.setReservoire(rs.getDouble("reservoire"));
                voiture.setConsommation(rs.getDouble("consommation"));
             
                voitures.add(voiture);
                
            }

        } catch (Exception e) {
            throw e;
        } finally {
            // if (rs != null) rs.close();
            // if (pstmt != null) pstmt.close();
        }

        return voitures;
    }
    
    public static Voiture getVoitureById(ConnexionPSQL conn, int id) throws Exception{
        Voiture voiture = new Voiture();
        String sql = "SELECT id, nom, type, vitesseMax, longueur, largeur, reservoire, consommation FROM voiture WHERE id = ?";
        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setInt(1, id);
            rs = pstmt.executeQuery();
            if (rs.next()) {
                voiture.setId(rs.getInt("id"));
                voiture.setNom(rs.getString("nom"));
                voiture.setType(rs.getString("type"));
                voiture.setVitesseMax(rs.getDouble("vitesseMax"));
                voiture.setLongueur(rs.getDouble("longueur"));
                voiture.setLargeur(rs.getDouble("largeur"));
                voiture.setReservoire(rs.getDouble("reservoire"));
                voiture.setConsommation(rs.getDouble("consommation"));
             
            }
            return voiture;
        } catch (Exception e) {
            throw e;
        }
    }
}


