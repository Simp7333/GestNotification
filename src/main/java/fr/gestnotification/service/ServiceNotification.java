package fr.gestnotification.service;

import fr.gestnotification.core.Observateur;
import fr.gestnotification.core.Sujet;
import fr.gestnotification.model.Employe;
import fr.gestnotification.model.Role;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Service simple de gestion des notifications
 */
public class ServiceNotification implements Sujet {
    private final List<Observateur> observateurs;
    private final Map<String, Employe> employes;
    private boolean adminCree = false;

    public ServiceNotification() {
        this.observateurs = new ArrayList<>();
        this.employes = new HashMap<>();
    }

    public void ajouterEmploye(Employe employe) {
        if (employe.estAdmin() && adminCree) {
            System.out.println("❌ Un administrateur existe déjà");
            return;
        }
        if (employe.estAdmin()) {
            adminCree = true;
        }
        employes.put(employe.getIdentifiant(), employe);
    }

    public Employe authentifier(String id, String motDePasse) {
        Employe employe = employes.get(id);
        if (employe != null && employe.verifierMotDePasse(motDePasse)) {
            return employe;
        }
        return null;
    }

    public void supprimerEmploye(String id) {
        Employe employe = employes.remove(id);
        if (employe != null) {
            if (employe.estAdmin()) {
                adminCree = false;
            }
            retirerObservateur(employe);
            System.out.println("🗑️ Employé supprimé : " + employe);
        }
    }

    public void afficherTousLesEmployes() {
        System.out.println("\n👥 Liste de tous les employés :");
        if (employes.isEmpty()) {
            System.out.println("Aucun employé enregistré");
            return;
        }
        employes.values().forEach(employe -> 
            System.out.println("- " + employe + (estAbonne(employe) ? " [Abonné]" : " [Non abonné]")));
    }

    @Override
    public void ajouterObservateur(Observateur observateur) {
        if (!observateurs.contains(observateur)) {
            observateurs.add(observateur);
            System.out.println("➕ Nouvel abonné : " + observateur);
        }
    }

    @Override
    public void retirerObservateur(Observateur observateur) {
        if (observateurs.remove(observateur)) {
            System.out.println("➖ Désabonnement : " + observateur);
        }
    }

    @Override
    public boolean estAbonne(Observateur observateur) {
        return observateurs.contains(observateur);
    }

    @Override
    public void notifierObservateurs(String message, String expediteur) {
        System.out.println("\n📢 Nouvelle notification de " + expediteur + " :");
        System.out.println("Message : " + message);
        System.out.println("Envoi aux " + observateurs.size() + " abonnés...\n");
        
        for (Observateur observateur : observateurs) {
            observateur.recevoirNotification(message, expediteur);
        }
    }

    /**
     * Obtient la liste des observateurs actuellement abonnés
     * @return Liste des observateurs
     */
    public List<Observateur> getObservateurs() {
        return new ArrayList<>(observateurs);
    }

    /**
     * Affiche la liste des abonnés actuels
     */
    public void afficherAbonnes() {
        if (observateurs.isEmpty()) {
            System.out.println("📭 Aucun abonné aux notifications");
            return;
        }

        System.out.println("\n📋 Liste des abonnés aux notifications :");
        System.out.println("Nombre total d'abonnés : " + observateurs.size());
        for (Observateur obs : observateurs) {
            System.out.println("- " + obs);
        }
    }
} 