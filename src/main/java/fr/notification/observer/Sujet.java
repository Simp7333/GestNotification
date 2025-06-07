package fr.notification.observer;

/**
 * Interface Sujet pour le pattern Observer
 * Définit le comportement du service de notification
 */
public interface Sujet {
    /**
     * Ajoute un observateur à la liste des abonnés
     * @param observateur L'observateur à ajouter
     */
    void ajouterObservateur(Observateur observateur);
    
    /**
     * Retire un observateur de la liste des abonnés
     * @param observateur L'observateur à retirer
     */
    void retirerObservateur(Observateur observateur);
    
    /**
     * Vérifie si un observateur est abonné
     * @param observateur L'observateur à vérifier
     * @return true si l'observateur est abonné, false sinon
     */
    boolean estAbonne(Observateur observateur);
    
    /**
     * Notifie tous les observateurs abonnés
     * @param message Le message à envoyer
     * @param expediteur L'expéditeur du message
     */
    void notifierObservateurs(String message, String expediteur);
} 