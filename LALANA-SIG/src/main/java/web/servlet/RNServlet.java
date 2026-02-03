package web.servlet;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.*;
import java.util.*;

import dao.RNDAO;
import model.RN;
import utildb.ConnexionPSQL;

@WebServlet("/rn")
public class RNServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Access-Control-Allow-Origin", "*");
        
        PrintWriter out = response.getWriter();
        
        try {
            // Récupérer les routes nationales depuis la base de données
            ConnexionPSQL conn = new ConnexionPSQL();
            Vector<RN> routes = RNDAO.getAllRN(conn);
            conn.closeConnection();
            
            // Créer le JSON manuellement (FeatureCollection)
            StringBuilder json = new StringBuilder();
            json.append("{\"type\":\"FeatureCollection\",\"features\":[");
            
            for (int i = 0; i < routes.size(); i++) {
                RN rn = routes.get(i);
                if (i > 0) json.append(",");
                
                json.append("{");
                json.append("\"type\":\"Feature\",");
                json.append("\"id\":").append(rn.getId()).append(",");
                json.append("\"properties\":{");
                json.append("\"nom\":\"").append(escapeJson(rn.getNom())).append("\"");
                json.append("},");
                json.append("\"geometry\":").append(rn.getGeoJson() != null ? rn.getGeoJson() : "{\"type\":\"LineString\",\"coordinates\":[]}");
                json.append("}");
            }
            
            json.append("]}");
            
            out.print(json.toString());
            out.flush();
            
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            out.print("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}");
            out.flush();
            e.printStackTrace();
        }
    }
    
    /**
     * Échappe les caractères spéciaux pour JSON
     */
    private String escapeJson(String input) {
        if (input == null) return "";
        return input.replace("\\", "\\\\")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r")
                   .replace("\t", "\\t");
    }
}
