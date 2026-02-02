package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Vector;

import model.Lalana;
import model.Lavaka;
import model.Pause;
import utildb.ConnexionOracle;

public class LalanaDAO {

    // CREATE
    public static void save(ConnexionOracle conn, Lalana lalana) throws Exception {
        String sql = "INSERT INTO lalana (nom, distance, debut, fin, largeur) VALUES (?, ?, ?, ?)";

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setString(1, lalana.getNomLalana());
            pstmt.setDouble(2, lalana.getDistance());
            pstmt.setString(3, lalana.getDebut());
            pstmt.setString(4, lalana.getFin());
            pstmt.setDouble(5, lalana.getLargeur());
            pstmt.executeUpdate();

            c.commit();

        } finally {
            if (pstmt != null)
                pstmt.close();
        }
    }

    // READ ALL COMPLET (simulation)
    public static Vector<Lalana> getAll(ConnexionOracle conn) throws Exception {

        Vector<Lalana> list = new Vector<>();
        String sql = "SELECT ID, NOM, DISTANCE, DEBUT, FIN, LARGEUR FROM LALANA";

        try (
                Connection c = conn.getConnection();
                PreparedStatement pstmt = c.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Lalana l = new Lalana();

                l.setId(rs.getInt("ID"));
                l.setNomLalana(rs.getString("NOM"));
                l.setDistance(rs.getDouble("DISTANCE"));
                l.setDebut(rs.getString("DEBUT"));
                l.setFin(rs.getString("FIN"));
                l.setLargeur(rs.getDouble("LARGEUR"));

                // lavakas
                Vector<Lavaka> lavakas = LavakaDAO.getByLalana(conn, l.getId());
                l.setLavakas(lavakas);

                // pauses
                Vector<Pause> pauses = PauseDAO.findByLalana(conn, l.getId());
                l.setPauses(pauses);

                // System.out.println("Lalana ID=" + l.getId() + " with "
                //         + lavakas.size() + " lavakas and "
                //         + pauses.size() + " pauses.");

                for (Pause pause : pauses) {
                    System.out.println("pause " + pause.getNom() + 
                                        " de " + pause.getHeureDebut() + 
                                        " à " + pause.getHeureFin());
                }
                        

                list.add(l);
            }
        }

        return list;
    }

    public static Vector<Lalana> getAllLight(ConnexionOracle conn) throws Exception {

        Vector<Lalana> list = new Vector<>();
        String sql = "SELECT ID, NOM FROM LALANA";

        try (
                Connection c = conn.getConnection();
                PreparedStatement pstmt = c.prepareStatement(sql);
                ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                Lalana l = new Lalana();
                l.setId(rs.getInt("ID"));
                l.setNomLalana(rs.getString("NOM"));
                list.add(l);
            }
        }

        return list;
    }

    // UPDATE
    public static void update(ConnexionOracle conn, Lalana lalana) throws Exception {
        String sql = "UPDATE lalana SET nom=?, distance=?, debut=?, fin=?, largeur = ? WHERE id=?";

        Connection c = null;
        PreparedStatement pstmt = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            pstmt.setString(1, lalana.getNomLalana());
            pstmt.setDouble(2, lalana.getDistance());
            pstmt.setString(3, lalana.getDebut());
            pstmt.setString(4, lalana.getFin());
            pstmt.setInt(5, lalana.getId());
            pstmt.setDouble(6, lalana.getLargeur());
            pstmt.executeUpdate();
        } finally {
            if (pstmt != null)
                pstmt.close();
        }
    }

    // DELETE
    public static void delete(ConnexionOracle conn, int id) throws Exception {
        String sql = "DELETE FROM lalana WHERE id=?";

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
