package servlet;

import dao.LalanaDAO;
import model.Lalana;
import utildb.ConnexionOracle;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.Vector;

public class LalanaServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json; charset=UTF-8");
        PrintWriter out = res.getWriter();

        try {
            ConnexionOracle conn = new ConnexionOracle();
            Vector<Lalana> lalanas = LalanaDAO.getAll(conn);

            out.print("[");
            for (int i = 0; i < lalanas.size(); i++) {
                Lalana l = lalanas.get(i);
                out.print("{");
                out.print("\"id\":" + l.getId() + ",");
                out.print("\"nom\":\"" + l.getNomLalana().replaceAll("\"", "\\\\\"") + "\",");
                out.print("\"distance\":" + l.getDistance() + ",");
                out.print("\"debut\":\"" + l.getDebut().replaceAll("\"", "\\\\\"") + "\",");
                out.print("\"fin\":\"" + l.getFin().replaceAll("\"", "\\\\\"") + "\"");
                out.print("}");
                if (i < lalanas.size() - 1) out.print(",");
            }
            out.print("]");

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
