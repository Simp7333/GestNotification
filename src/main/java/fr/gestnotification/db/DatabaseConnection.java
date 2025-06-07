package fr.gestnotification.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/gestnotification";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private static Connection connection;

    public static Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
        }
        return connection;
    }

    public static void initializeDatabase() {
        try {
            // Créer la base de données si elle n'existe pas
            Connection tempConnection = DriverManager.getConnection(
                "jdbc:mysql://localhost:3306", USER, PASSWORD);
            Statement stmt = tempConnection.createStatement();
            stmt.executeUpdate("CREATE DATABASE IF NOT EXISTS gestnotification");
            stmt.close();
            tempConnection.close();

            // Créer les tables
            Connection conn = getConnection();
            Statement statement = conn.createStatement();

            // Table Employes
            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS employes (" +
                "id VARCHAR(50) PRIMARY KEY," +
                "nom VARCHAR(100) NOT NULL," +
                "mot_de_passe VARCHAR(100) NOT NULL," +
                "role VARCHAR(20) NOT NULL" +
                ")"
            );

            // Table Notifications
            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS notifications (" +
                "id INT AUTO_INCREMENT PRIMARY KEY," +
                "message TEXT NOT NULL," +
                "expediteur VARCHAR(100) NOT NULL," +
                "date_envoi DATETIME NOT NULL," +
                "destinataire_id VARCHAR(50) NOT NULL," +
                "FOREIGN KEY (destinataire_id) REFERENCES employes(id)" +
                ")"
            );

            // Table Abonnements
            statement.executeUpdate(
                "CREATE TABLE IF NOT EXISTS abonnements (" +
                "employe_id VARCHAR(50) NOT NULL," +
                "date_abonnement DATETIME NOT NULL," +
                "FOREIGN KEY (employe_id) REFERENCES employes(id)," +
                "PRIMARY KEY (employe_id)" +
                ")"
            );

            statement.close();
            System.out.println("✅ Base de données initialisée avec succès");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'initialisation de la base de données : " + e.getMessage());
        }
    }
} 