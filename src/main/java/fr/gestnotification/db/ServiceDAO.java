package fr.gestnotification.db;

import fr.gestnotification.model.Service;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceDAO {
    
    public void creerService(Service service) throws SQLException {
        String sql = "INSERT INTO services (id, nom, description, actif) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, service.getId());
            pstmt.setString(2, service.getNom());
            pstmt.setString(3, service.getDescription());
            pstmt.setBoolean(4, service.isActif());
            pstmt.executeUpdate();
        }
    }

    public void modifierService(Service service) throws SQLException {
        String sql = "UPDATE services SET nom = ?, description = ?, actif = ? WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, service.getNom());
            pstmt.setString(2, service.getDescription());
            pstmt.setBoolean(3, service.isActif());
            pstmt.setString(4, service.getId());
            pstmt.executeUpdate();
        }
    }

    public void supprimerService(String id) throws SQLException {
        String sql = "DELETE FROM services WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }

    public Service chargerService(String id) throws SQLException {
        String sql = "SELECT * FROM services WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Service service = new Service(
                    rs.getString("id"),
                    rs.getString("nom"),
                    rs.getString("description")
                );
                service.setActif(rs.getBoolean("actif"));
                return service;
            }
        }
        return null;
    }

    public List<Service> chargerTousLesServices() throws SQLException {
        List<Service> services = new ArrayList<>();
        String sql = "SELECT * FROM services ORDER BY nom";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement()) {
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                Service service = new Service(
                    rs.getString("id"),
                    rs.getString("nom"),
                    rs.getString("description")
                );
                service.setActif(rs.getBoolean("actif"));
                services.add(service);
            }
        }
        return services;
    }
} 