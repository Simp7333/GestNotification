package fr.gestnotification.db;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;

public class TestDatabase {
    public static void main(String[] args) {
        try {
            System.out.println("Test de connexion à la base de données...");
            
            // Initialiser la base de données
            DatabaseConnection.initializeDatabase();
            
            // Tester la connexion
            Connection conn = DatabaseConnection.getConnection();
            System.out.println("✅ Connexion établie avec succès");
            
            // Vérifier les tables
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SHOW TABLES");
            
            System.out.println("\nTables présentes dans la base de données :");
            while (rs.next()) {
                String tableName = rs.getString(1);
                System.out.println("- " + tableName);
                
                // Afficher la structure de chaque table
                ResultSet columns = stmt.executeQuery("DESCRIBE " + tableName);
                while (columns.next()) {
                    System.out.println("  * " + columns.getString("Field") + " (" + columns.getString("Type") + ")");
                }
                System.out.println();
            }
            
            rs.close();
            stmt.close();
            
        } catch (Exception e) {
            System.err.println("❌ Erreur : " + e.getMessage());
            e.printStackTrace();
        }
    }
} 