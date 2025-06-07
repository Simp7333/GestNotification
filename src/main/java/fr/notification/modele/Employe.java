package fr.notification.modele;

import fr.notification.observer.Observateur;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe représentant un employé
 * Implémente l'interface Observateur pour recevoir des notifications
 */
public class Employe implements Observateur {
    private final String identifiant;
    private final String nom;
    private final String email;
    private final List<String> notifications;

    public Employe(String identifiant, String nom, String email) {
        this.identifiant = identifiant;
        this.nom = nom;
        this.email = email;
        this.notifications = new ArrayList<>();
    }

    @Override
    public void recevoirNotification(String message, String expediteur) {
        if (!this.nom.equals(expediteur)) {
            String notification = String.format("Message de %s : %s", expediteur, message);
            notifications.add(notification);
            System.out.println(nom + " a reçu : " + notification);
        }
    }

    @Override
    public String getIdentifiant() {
        return identifiant;
    }

    @Override
    public String getEmail() {
        return email;
    }

    public String getNom() {
        return nom;
    }

    public List<String> getNotifications() {
        return new ArrayList<>(notifications);
    }

    @Override
    public String toString() {
        return String.format("Employe{id='%s', nom='%s', email='%s'}", 
                           identifiant, nom, email);
    }
} 