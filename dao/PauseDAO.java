package dao;

import model.Pause;
import utildb.ConnexionOracle;

import java.sql.*;
import java.util.Vector;

public class PauseDAO {

    public static void save(ConnexionOracle conn, Pause pause) throws Exception {

        String sql = """
            INSERT INTO PAUSE
            (ID, NOM, IDLALANA, POSITION, HEUREDEBUT, HEUREFIN)
            VALUES (SEQ_PAUSE.NEXTVAL, ?, ?, ?, ?, ?)
        """;

        Connection c = conn.getConnection();

        try (PreparedStatement ps = c.prepareStatement(sql)) {

            c.setAutoCommit(false);

            ps.setString(1, pause.getNom());
            ps.setInt(2, pause.getIdLalana());
            ps.setDouble(3, pause.getPosition());
            ps.setTimestamp(4, Timestamp.valueOf(pause.getHeureDebut()));
            ps.setTimestamp(5, Timestamp.valueOf(pause.getHeureFin()));

            ps.executeUpdate();
            c.commit();
        }
        catch (Exception e) {
            c.rollback();
            throw e;
        }
    }

    public static Vector<Pause> findByLalana(
            ConnexionOracle conn, int idLalana) throws Exception {

        String sql = """
            SELECT * FROM PAUSE
            WHERE IDLALANA = ?
            ORDER BY POSITION
        """;

        Vector<Pause> pauses = new Vector<>();

        try (PreparedStatement ps =
                     conn.getConnection().prepareStatement(sql)) {

            ps.setInt(1, idLalana);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Pause p = new Pause(
                        rs.getInt("ID"),
                        rs.getString("NOM"),
                        rs.getInt("IDLALANA"),
                        rs.getTimestamp("HEUREDEBUT").toLocalDateTime(),
                        rs.getTimestamp("HEUREFIN").toLocalDateTime(),
                        rs.getDouble("POSITION")
                );
                pauses.add(p);
            }
        }

        return pauses;
    }
}
