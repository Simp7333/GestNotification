package fr.gestnotification.model;

import fr.gestnotification.core.Observateur;
import java.util.ArrayList;
import java.util.List;

/**
 * Représente un employé qui peut recevoir des notifications
 */
public class Employe implements Observateur {
    private final String nom;
    private final String id;
    private final String motDePasse;
    private final List<String> notifications;
    private final Role role;

    public Employe(String id, String nom, String motDePasse, Role role) {
        this.id = id;
        this.nom = nom;
        this.motDePasse = motDePasse;
        this.notifications = new ArrayList<>();
        this.role = role;
    }

    public Employe(String id, String nom, String motDePasse) {
        this(id, nom, motDePasse, Role.EMPLOYE);
    }

    @Override
    public void recevoirNotification(String message, String expediteur) {
        String notification = String.format("[%s] %s : %s", 
            java.time.LocalDateTime.now().format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")),
            expediteur, 
            message);
        notifications.add(notification);
        System.out.println("👤 " + nom + " a reçu un message de " + expediteur + " :");
        System.out.println("   " + message);
    }

    public boolean verifierMotDePasse(String motDePasse) {
        return this.motDePasse.equals(motDePasse);
    }

    public void afficherNotifications() {
        System.out.println("\n📋 Notifications de " + nom + " :");
        if (notifications.isEmpty()) {
            System.out.println("Aucune notification");
            return;
        }
        for (String notification : notifications) {
            System.out.println("- " + notification);
        }
    }

    public boolean estAdmin() {
        return role == Role.ADMIN;
    }

    public String getNom() {
        return nom;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public Role getRole() {
        return role;
    }

    @Override
    public String getIdentifiant() {
        return id;
    }

    @Override
    public String getEmail() {
        return nom.toLowerCase().replace(" ", ".") + "@entreprise.com";
    }

    @Override
    public String toString() {
        return nom + " (ID: " + id + ")" + (estAdmin() ? " [ADMIN]" : "");
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Employe employe = (Employe) o;
        return id.equals(employe.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
} 