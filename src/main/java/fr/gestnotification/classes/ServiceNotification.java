package fr.gestnotification.classes;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServiceNotification {
    private final Map<String, Employe> employes;
    private final EmployeDAO employeDAO;
    private final NotificationDAO notificationDAO;
    private boolean adminCree = false;

    public ServiceNotification() {
        this.employes = new HashMap<>();
        this.employeDAO = new EmployeDAO();
        this.notificationDAO = new NotificationDAO();
        chargerEmployes();
    }

    private void chargerEmployes() {
        try {
            List<Employe> listeEmployes = employeDAO.trouverTousLesEmployes();
            for (Employe employe : listeEmployes) {
                employes.put(employe.getIdentifiant(), employe);
                if (employe.estAdmin()) {
                    adminCree = true;
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors du chargement des employés : " + e.getMessage());
        }
    }

    public void ajouterEmploye(Employe employe) {
        if (employe.estAdmin() && adminCree) {
            System.out.println("❌ Un administrateur existe déjà");
            return;
        }
        try {
            employeDAO.sauvegarderEmploye(employe);
            if (employe.estAdmin()) {
                adminCree = true;
            }
            employes.put(employe.getIdentifiant(), employe);
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'ajout de l'employé : " + e.getMessage());
        }
    }

    public Employe authentifier(String id, String motDePasse) {
        try {
            Employe employe = employeDAO.trouverEmploye(id);
            if (employe != null && employe.verifierMotDePasse(motDePasse)) {
                return employe;
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'authentification : " + e.getMessage());
        }
        return null;
    }

    public void supprimerEmploye(String id) {
        try {
            employeDAO.supprimerEmploye(id);
            Employe employe = employes.remove(id);
            if (employe != null && employe.estAdmin()) {
                adminCree = false;
            }
            notificationDAO.supprimerAbonnement(id);
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de la suppression de l'employé : " + e.getMessage());
        }
    }

    public void abonner(Employe employe) {
        try {
            notificationDAO.ajouterAbonnement(employe.getIdentifiant());
            System.out.println("✅ " + employe.getNom() + " s'est abonné aux notifications");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'abonnement : " + e.getMessage());
        }
    }

    public void desabonner(Employe employe) {
        try {
            notificationDAO.supprimerAbonnement(employe.getIdentifiant());
            System.out.println("✅ " + employe.getNom() + " s'est désabonné des notifications");
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors du désabonnement : " + e.getMessage());
        }
    }

    public void envoyerNotification(String message, Employe expediteur) {
        try {
            List<String> abonnes = notificationDAO.trouverAbonnes();
            for (String abonneId : abonnes) {
                if (!abonneId.equals(expediteur.getIdentifiant())) {
                    notificationDAO.sauvegarderNotification(message, expediteur.getNom(), abonneId);
                    Employe destinataire = employes.get(abonneId);
                    if (destinataire != null) {
                        destinataire.recevoirNotification(message, expediteur.getNom());
                    }
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'envoi de la notification : " + e.getMessage());
        }
    }

    public void afficherTousLesEmployes() {
        System.out.println("\n👥 Liste des employés :");
        for (Employe employe : employes.values()) {
            System.out.println("- " + employe);
        }
    }

    public void afficherAbonnes() {
        try {
            List<String> abonnes = notificationDAO.trouverAbonnes();
            System.out.println("\n📢 Liste des abonnés :");
            if (abonnes.isEmpty()) {
                System.out.println("Aucun abonné");
                return;
            }
            for (String abonneId : abonnes) {
                Employe employe = employes.get(abonneId);
                if (employe != null) {
                    System.out.println("- " + employe);
                }
            }
        } catch (SQLException e) {
            System.err.println("❌ Erreur lors de l'affichage des abonnés : " + e.getMessage());
        }
    }
} 