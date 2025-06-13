package fr.gestnotification.classes;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EmployeDAO {
    
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

    public void supprimerEmploye(String id) throws SQLException {
        String sql = "DELETE FROM employes WHERE id = ?";
        try (PreparedStatement pstmt = DatabaseConnection.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.executeUpdate();
        }
    }

    public Employe trouverEmploye(String id) throws SQLException {
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

    public List<Employe> trouverTousLesEmployes() throws SQLException {
        List<Employe> employes = new ArrayList<>();
        String sql = "SELECT * FROM employes";
        try (Statement stmt = DatabaseConnection.getConnection().createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                employes.add(new Employe(
                    rs.getString("id"),
                    rs.getString("nom"),
                    rs.getString("mot_de_passe"),
                    Role.valueOf(rs.getString("role"))
                ));
            }
        }
        return employes;
    }
} 