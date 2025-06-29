package fr.gestnotification.classes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    
    public void sauvegarderNotification(String message, String expediteur, String destinataireId) throws SQLException {
        String sql = "INSERT INTO notifications (message, expediteur, date_envoi, destinataire_id) VALUES (?, ?, NOW(), ?)";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, message);
            pstmt.setString(2, expediteur);
            pstmt.setString(3, destinataireId);
            pstmt.executeUpdate();
        }
    }

    public void ajouterAbonnement(String employeId) throws SQLException {
        String sql = "INSERT INTO abonnements (employe_id, date_abonnement) VALUES (?, NOW())";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, employeId);
            pstmt.executeUpdate();
        }
    }

    public void supprimerAbonnement(String employeId) throws SQLException {
        String sql = "DELETE FROM abonnements WHERE employe_id = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, employeId);
            pstmt.executeUpdate();
        }
    }

    public List<String> trouverNotifications(String employeId) throws SQLException {
        List<String> notifications = new ArrayList<>();
        String sql = "SELECT * FROM notifications WHERE destinataire_id = ? ORDER BY date_envoi DESC";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, employeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String notification = String.format("[%s] %s : %s",
                    rs.getTimestamp("date_envoi").toLocalDateTime().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    rs.getString("expediteur"),
                    rs.getString("message"));
                notifications.add(notification);
            }
        }
        return notifications;
    }

    public List<String> trouverAbonnes() throws SQLException {
        List<String> abonnes = new ArrayList<>();
        String sql = "SELECT e.* FROM employes e JOIN abonnements a ON e.id = a.employe_id";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                abonnes.add(rs.getString("id"));
            }
        }
        return abonnes;
    }
} 