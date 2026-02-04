package servlet;

import dao.SimbaDAO;
import dao.LalanaDAO;
import model.Simba;
import model.Lalana;
import utildb.ConnexionOracle;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import java.io.*;
import java.util.Vector;

public class SimbaServlet extends HttpServlet {

    protected void doGet(HttpServletRequest req, HttpServletResponse res)
            throws IOException {

        res.setContentType("application/json; charset=UTF-8");
        PrintWriter out = res.getWriter();
        String action = req.getParameter("action");
        
        try {
            ConnexionOracle conn = new ConnexionOracle();
            
            if ("getByLalana".equals(action)) {
                String idLalanaStr = req.getParameter("idLalana");
                if (idLalanaStr != null) {
                    int idLalana = Integer.parseInt(idLalanaStr);
                    Vector<Simba> simbas = SimbaDAO.getByIdLalana(conn, idLalana);
                    outputSimbasJson(out, simbas);
                } else {
                    out.print("{\"error\":\"idLalana manquant\"}");
                }
            } else if ("getAll".equals(action)) {
                Vector<Simba> allSimbas = SimbaDAO.getAll(conn);
                outputSimbasJson(out, allSimbas);
            } else {
                // Par défaut : retourner tous les Simba
                Vector<Simba> allSimbas = SimbaDAO.getAll(conn);
                outputSimbasJson(out, allSimbas);
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }

    private void outputSimbasJson(PrintWriter out, Vector<Simba> simbas) {
        out.print("[");
        for (int i = 0; i < simbas.size(); i++) {
            Simba s = simbas.get(i);
            out.print("{");
            out.print("\"id\":" + s.getId() + ",");
            out.print("\"descriptions\":\"" + s.getDescriptions().replaceAll("\"", "\\\\\"") + "\",");
            out.print("\"pkDebut\":" + s.getPkDebut() + ",");
            out.print("\"pkFin\":" + s.getPkFin() + ",");
            out.print("\"surface\":" + s.getSurface() + ",");
            out.print("\"profondeur\":" + s.getProfondeur() + ",");
            out.print("\"idLalana\":" + s.getIdLalana());
            out.print("}");
            if (i < simbas.size() - 1) out.print(",");
        }
        out.print("]");
    }
    
    protected void doPost(HttpServletRequest req, HttpServletResponse res)
            throws IOException {
        
        res.setContentType("application/json; charset=UTF-8");
        PrintWriter out = res.getWriter();
        
        try {
            // Récupérer les paramètres du formulaire
            String descriptions = req.getParameter("descriptions");
            double pkDebut = Double.parseDouble(req.getParameter("pkDebut"));
            double pkFin = Double.parseDouble(req.getParameter("pkFin"));
            double surface = Double.parseDouble(req.getParameter("surface"));
            double profondeur = Double.parseDouble(req.getParameter("profondeur"));
            int idLalana = Integer.parseInt(req.getParameter("idLalana"));
            int idTypeMatiere = Integer.parseInt(req.getParameter("idTypeMatiere"));
            
            // Créer et sauvegarder le Simba
            ConnexionOracle conn = new ConnexionOracle();
            Simba simba = new Simba(descriptions, pkDebut, pkFin, 0.0, surface, profondeur, idLalana, null, idTypeMatiere);
            SimbaDAO.save(conn, simba);
            
            out.print("{\"success\":true,\"message\":\"Simba ajouté avec succès\",\"id\":" + simba.getId() + "}");
            
        } catch (Exception e) {
            e.printStackTrace();
            out.print("{\"success\":false,\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
