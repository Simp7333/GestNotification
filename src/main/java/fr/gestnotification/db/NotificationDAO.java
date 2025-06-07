package fr.gestnotification.db;

import fr.gestnotification.model.Employe;
import fr.gestnotification.model.Role;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class NotificationDAO {
    
    public void sauvegarderEmploye(Employe employe) throws SQLException {
        String sql = "INSERT INTO employes (id, nom, mot_de_passe, role) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, employe.getIdentifiant());
            pstmt.setString(2, employe.getNom());
            pstmt.setString(3, employe.getMotDePasse());
            pstmt.setString(4, employe.getRole().name());
            pstmt.executeUpdate();
        }
    }

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

    public void retirerAbonnement(String employeId) throws SQLException {
        String sql = "DELETE FROM abonnements WHERE employe_id = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, employeId);
            pstmt.executeUpdate();
        }
    }

    public List<String> chargerNotifications(String employeId) throws SQLException {
        List<String> notifications = new ArrayList<>();
        String sql = "SELECT message, expediteur, date_envoi FROM notifications WHERE destinataire_id = ? ORDER BY date_envoi DESC";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, employeId);
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                String notification = String.format("[%s] %s : %s",
                    rs.getTimestamp("date_envoi").toLocalDateTime()
                        .format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
                    rs.getString("expediteur"),
                    rs.getString("message"));
                notifications.add(notification);
            }
        }
        return notifications;
    }

    public Employe chargerEmploye(String id) throws SQLException {
        String sql = "SELECT * FROM employes WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return new Employe(
                    rs.getString("id"),
                    rs.getString("nom"),
                    rs.getString("mot_de_passe"),
                    Role.valueOf(rs.getString("role"))
                );
            }
        }
        return null;
    }

    public List<Employe> chargerTousLesEmployes() throws SQLException {
        List<Employe> employes = new ArrayList<>();
        String sql = "SELECT e.*, CASE WHEN a.employe_id IS NOT NULL THEN 1 ELSE 0 END as est_abonne " +
                    "FROM employes e LEFT JOIN abonnements a ON e.id = a.employe_id";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Employe employe = new Employe(
                    rs.getString("id"),
                    rs.getString("nom"),
                    rs.getString("mot_de_passe"),
                    Role.valueOf(rs.getString("role"))
                );
                employes.add(employe);
            }
        }
        return employes;
    }

    public void supprimerEmploye(String id) throws SQLException {
        // Supprimer d'abord les références dans les autres tables
        String[] sqls = {
            "DELETE FROM notifications WHERE destinataire_id = ?",
            "DELETE FROM abonnements WHERE employe_id = ?",
            "DELETE FROM employes WHERE id = ?"
        };
        
        for (String sql : sqls) {
            try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
                pstmt.setString(1, id);
                pstmt.executeUpdate();
            }
        }
    }

    public boolean estAbonne(String employeId) throws SQLException {
        String sql = "SELECT COUNT(*) FROM abonnements WHERE employe_id = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, employeId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        }
        return false;
    }
} 