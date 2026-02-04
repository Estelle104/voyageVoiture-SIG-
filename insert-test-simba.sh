#!/bin/bash

################################################################################
# 🔴 SCRIPT D'INSERTION DES DONNÉES TEST SIMBA
# Ajoute des Lalanas et Simba de test dans Oracle
################################################################################

PROJECT_DIR="/home/itu/S3/Progammation/voiture/voyageVoiture-SIG-"
LALANA_DIR="$PROJECT_DIR/LALANA-SIG"

echo "🔴 INSERTION DONNÉES TEST SIMBA"
echo "==============================="

# Créer un programme Java temporaire pour exécuter les requêtes
cat > "$LALANA_DIR/temp_insert_simba.java" << 'EOF'
import dao.*;
import model.*;
import utildb.*;
import java.sql.*;

public class temp_insert_simba {
    public static void main(String[] args) {
        try {
            ConnexionOracle conn = new ConnexionOracle();
            Connection c = conn.getConnection();
            
            System.out.println("🔗 Connexion Oracle établie");
            
            // 1. Supprimer anciennes données test si elles existent
            System.out.println("🗑️ Nettoyage données existantes...");
            try {
                PreparedStatement pstmt1 = c.prepareStatement("DELETE FROM Simba WHERE id > 0");
                int deletedSimbas = pstmt1.executeUpdate();
                System.out.println("  • " + deletedSimbas + " Simba supprimés");
                pstmt1.close();
                
                PreparedStatement pstmt2 = c.prepareStatement("DELETE FROM Lalana WHERE id > 0");
                int deletedLalanas = pstmt2.executeUpdate();
                System.out.println("  • " + deletedLalanas + " Lalana supprimés");
                pstmt2.close();
                
                // Reset sequences
                PreparedStatement pstmt3 = c.prepareStatement("DROP SEQUENCE seq_lalana");
                try { pstmt3.executeUpdate(); } catch(Exception e) {}
                pstmt3.close();
                
                PreparedStatement pstmt4 = c.prepareStatement("DROP SEQUENCE seq_simba");
                try { pstmt4.executeUpdate(); } catch(Exception e) {}
                pstmt4.close();
                
                PreparedStatement pstmt5 = c.prepareStatement("CREATE SEQUENCE seq_lalana START WITH 1 INCREMENT BY 1");
                pstmt5.executeUpdate();
                pstmt5.close();
                
                PreparedStatement pstmt6 = c.prepareStatement("CREATE SEQUENCE seq_simba START WITH 1 INCREMENT BY 1");
                pstmt6.executeUpdate();
                pstmt6.close();
                
                c.commit();
                
            } catch(Exception e) {
                System.out.println("  • Nettoyage: " + e.getMessage());
            }
            
            // 2. Insérer TypeMatieres si nécessaire
            System.out.println("🏗️ Insertion TypeMatiere...");
            String[] typeMatieres = {"Terre", "Sable", "Gravier", "Rocher"};
            for(String type : typeMatieres) {
                try {
                    PreparedStatement pstmt = c.prepareStatement("INSERT INTO typeMatiere (nom) VALUES (?)");
                    pstmt.setString(1, type);
                    pstmt.executeUpdate();
                    pstmt.close();
                    System.out.println("  • TypeMatiere '" + type + "' ajouté");
                } catch(Exception e) {
                    // Ignore si existe déjà
                }
            }
            c.commit();
            
            // 3. Insérer Lalanas de test
            System.out.println("🛣️ Insertion Lalanas...");
            String[][] lalanas = {
                {"RN 4", "370", "Antananarivo", "Toamasina", "7.0"},
                {"RN 7", "165", "Antananarivo", "Antsirabe", "6.5"},
                {"RN 1", "947", "Antananarivo", "Toliara", "7.5"},
                {"RN 2", "570", "Antananarivo", "Sambava", "6.0"},
                {"RN 3A", "280", "Antsirabe", "Morondava", "5.5"}
            };
            
            for(String[] lalanaData : lalanas) {
                PreparedStatement pstmt = c.prepareStatement("INSERT INTO Lalana (nom, distance, debut, fin, largeur) VALUES (?, ?, ?, ?, ?)");
                pstmt.setString(1, lalanaData[0]);
                pstmt.setDouble(2, Double.parseDouble(lalanaData[1]));
                pstmt.setString(3, lalanaData[2]);
                pstmt.setString(4, lalanaData[3]);
                pstmt.setDouble(5, Double.parseDouble(lalanaData[4]));
                pstmt.executeUpdate();
                pstmt.close();
                System.out.println("  • Lalana '" + lalanaData[0] + "' ajoutée");
            }
            c.commit();
            
            // 4. Insérer Simba de test
            System.out.println("🔴 Insertion Simba...");
            String[][] simbas = {
                // descriptions, pkDebut, pkFin, tauxRal, surface, prof, idLalana, idTypeMatiere
                {"Trou profond", "15.5", "16.2", "25.0", "45.5", "18.0", "1", "1"},
                {"Nids de poule", "89.3", "90.1", "15.0", "32.0", "12.5", "1", "2"},
                {"Affaissement", "156.8", "158.0", "35.0", "78.2", "22.3", "1", "3"},
                {"Fissures multiples", "25.0", "25.8", "20.0", "28.5", "8.0", "2", "1"},
                {"Cratère important", "78.5", "80.2", "40.0", "65.0", "25.0", "2", "4"},
                {"Déformation route", "245.3", "246.8", "30.0", "52.0", "15.5", "3", "2"},
                {"Érosion latérale", "456.0", "458.5", "28.0", "85.3", "20.8", "3", "1"},
                {"Trou géant", "823.7", "825.0", "45.0", "95.0", "35.0", "3", "3"},
                {"Ondulations", "125.2", "126.0", "18.0", "38.5", "10.0", "4", "2"},
                {"Affaissement humide", "387.5", "389.8", "35.0", "72.8", "28.5", "4", "1"},
                {"Fissures en V", "67.3", "68.1", "22.0", "41.2", "14.0", "5", "4"},
                {"Cratère de passage", "198.8", "201.2", "38.0", "88.5", "32.0", "5", "3"},
                {"Nids multiples", "234.5", "235.8", "25.0", "48.0", "16.5", "1", "2"},
                {"Effondrement", "112.0", "114.5", "50.0", "120.0", "40.0", "2", "1"},
                {"Série de trous", "678.2", "680.0", "32.0", "68.5", "24.8", "3", "4"}
            };
            
            for(String[] simbaData : simbas) {
                PreparedStatement pstmt = c.prepareStatement(
                    "INSERT INTO Simba (descriptions, pkDebut, pkFin, tauxRalentissement, surface, profondeur, idLalana, idLavaka, idTypeMatiere) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, NULL, ?)"
                );
                pstmt.setString(1, simbaData[0]);
                pstmt.setDouble(2, Double.parseDouble(simbaData[1]));
                pstmt.setDouble(3, Double.parseDouble(simbaData[2]));
                pstmt.setDouble(4, Double.parseDouble(simbaData[3]));
                pstmt.setDouble(5, Double.parseDouble(simbaData[4]));
                pstmt.setDouble(6, Double.parseDouble(simbaData[5]));
                pstmt.setInt(7, Integer.parseInt(simbaData[6]));
                pstmt.setInt(8, Integer.parseInt(simbaData[7]));
                pstmt.executeUpdate();
                pstmt.close();
                System.out.println("  • Simba '" + simbaData[0] + "' ajouté (PK " + simbaData[1] + "-" + simbaData[2] + ")");
            }
            c.commit();
            
            // 5. Affichage récapitulatif
            System.out.println("");
            System.out.println("📊 RÉCAPITULATIF:");
            
            PreparedStatement pstmt = c.prepareStatement("SELECT COUNT(*) FROM Lalana");
            ResultSet rs = pstmt.executeQuery();
            if(rs.next()) {
                System.out.println("  • Lalanas insérées: " + rs.getInt(1));
            }
            rs.close();
            pstmt.close();
            
            pstmt = c.prepareStatement("SELECT COUNT(*) FROM Simba");
            rs = pstmt.executeQuery();
            if(rs.next()) {
                System.out.println("  • Simbas insérés: " + rs.getInt(1));
            }
            rs.close();
            pstmt.close();
            
            System.out.println("");
            System.out.println("✅ DONNÉES TEST INSÉRÉES AVEC SUCCÈS !");
            System.out.println("");
            System.out.println("🎯 Accédez maintenant à:");
            System.out.println("   http://localhost:8080/VoyageVoiture/jsp/sig-enhanced.jsp");
            System.out.println("   et cliquez sur 'Afficher tous les Simba' pour voir les points rouges !");
            
            c.close();
            
        } catch(Exception e) {
            System.err.println("❌ Erreur: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
EOF

echo "📦 [1/3] Compilation du programme d'insertion..."
cd "$LALANA_DIR"
javac -cp "lib/*:build/classes" temp_insert_simba.java

echo "🔄 [2/3] Exécution de l'insertion..."
java -cp "lib/*:build/classes:." temp_insert_simba

echo "🧹 [3/3] Nettoyage..."
rm -f temp_insert_simba.java temp_insert_simba.class

echo ""
echo "🎉 SCRIPT TERMINÉ !"
echo ""