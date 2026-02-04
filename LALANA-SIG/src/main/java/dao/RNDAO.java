package dao;

import model.RN;
import utildb.ConnexionPSQL;

import java.sql.*;
import java.util.Vector;

public class RNDAO {
    public static Vector<RN> getAllRN(ConnexionPSQL conn) throws Exception {

        Vector<RN> liste = new Vector<>();

        String sql = """
                    SELECT
                        id,
                        nom,
                        ref,
                        ST_AsGeoJSON(geom) AS geojson
                    FROM rn
                    ORDER BY nom
                """;

        Connection c = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            c = conn.getConnection();
            pstmt = c.prepareStatement(sql);
            rs = pstmt.executeQuery();

            while (rs.next()) {
                RN rn = new RN();
                rn.setId(rs.getInt("id"));
                rn.setNom(rs.getString("nom"));
                rn.setRef(rs.getString("ref"));
                rn.setGeoJson(rs.getString("geojson"));

                liste.add(rn);
            }

        } catch (Exception e) {
            throw e;
        } finally {
            // même logique que Voiture : on laisse vide ou on ferme plus tard
            // if (rs != null) rs.close();
            // if (pstmt != null) pstmt.close();
        }

        return liste;
    }

}
