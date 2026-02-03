package servlet;

import dao.RNDAO;
import model.RN;
import utildb.ConnexionPSQL;

/*
import javax.servlet.*;
import javax.servlet.http.*;
*/
import java.io.*;
import java.util.Vector;

/*
public class RNServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json");
        PrintWriter out = res.getWriter();

        try {
            ConnexionPSQL conn = new ConnexionPSQL();
            Vector<RN> rns = RNDAO.getAllRN(conn);

            out.print("[");
            for (int i = 0; i < rns.size(); i++) {
                RN rn = rns.get(i);

                out.print("{");
                out.print("\"id\":" + rn.getId() + ",");
                out.print("\"nom\":\"" + rn.getNom() + "\",");
                out.print("\"geometry\":" + rn.getGeoJson());
                out.print("}");

                if (i < rns.size() - 1) out.print(",");
            }
            out.print("]");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
*/

// Classe commentée en attente des dépendances servlet
