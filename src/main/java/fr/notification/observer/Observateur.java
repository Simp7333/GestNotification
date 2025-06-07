package fr.notification.observer;

/**
 * Interface Observateur pour le pattern Observer
 * Définit le comportement d'un abonné aux notifications
 */
public interface Observateur {
    /**
     * Reçoit une notification
     * @param message Le contenu du message
     * @param expediteur L'expéditeur du message
     */
    void recevoirNotification(String message, String expediteur);
    
    /**
     * Obtient l'identifiant de l'observateur
     * @return L'identifiant unique
     */
    String getIdentifiant();
    
    /**
     * Obtient l'email de l'observateur
     * @return L'adresse email
     */
    String getEmail();
} 