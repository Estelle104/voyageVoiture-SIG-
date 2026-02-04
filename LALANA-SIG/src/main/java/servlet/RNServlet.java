package servlet;

import dao.RNDAO;
import model.RN;
import utildb.ConnexionPSQL;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.Vector;

public class RNServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json; charset=UTF-8");
        PrintWriter out = res.getWriter();

        try {
            ConnexionPSQL conn = new ConnexionPSQL();
            Vector<RN> rns = RNDAO.getAllRN(conn);

            out.print("[");
            for (int i = 0; i < rns.size(); i++) {
                RN rn = rns.get(i);

                out.print("{");
                out.print("\"id\":" + rn.getId() + ",");
                out.print("\"nom\":\"" + rn.getNom().replaceAll("\"", "\\\\\"") + "\",");
                String ref = rn.getRef() != null ? rn.getRef() : "";
                out.print("\"ref\":\"" + ref.replaceAll("\"", "\\\\\"") + "\",");
                out.print("\"geometry\":" + rn.getGeoJson());
                out.print("}");

                if (i < rns.size() - 1) out.print(",");
            }
            out.print("]");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("❌ Erreur RNServlet: " + e.getClass().getName() + " - " + e.getMessage());
            e.printStackTrace(System.err);
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
