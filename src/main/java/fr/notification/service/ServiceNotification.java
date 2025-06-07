package fr.notification.service;

import fr.notification.observer.Observateur;
import fr.notification.observer.Sujet;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

/**
 * Service de gestion des notifications
 * Implémente l'interface Sujet du pattern Observer
 */
public class ServiceNotification implements Sujet {
    private final List<Observateur> observateurs;
    private static final String EMAIL_EXPEDITEUR = "votre-email@gmail.com";
    private static final String MOT_DE_PASSE = "votre-mot-de-passe";

    public ServiceNotification() {
        this.observateurs = new ArrayList<>();
    }

    @Override
    public void ajouterObservateur(Observateur observateur) {
        if (!observateurs.contains(observateur)) {
            observateurs.add(observateur);
            System.out.println("Nouvel abonné ajouté : " + observateur);
        }
    }

    @Override
    public void retirerObservateur(Observateur observateur) {
        if (observateurs.remove(observateur)) {
            System.out.println("Abonné retiré : " + observateur);
        }
    }

    @Override
    public boolean estAbonne(Observateur observateur) {
        return observateurs.contains(observateur);
    }

    @Override
    public void notifierObservateurs(String message, String expediteur) {
        for (Observateur observateur : observateurs) {
            observateur.recevoirNotification(message, expediteur);
            envoyerEmail(observateur.getEmail(), 
                        "Nouvelle notification", 
                        "Message de " + expediteur + " : " + message);
        }
    }

    private void envoyerEmail(String destinataire, String sujet, String contenu) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(EMAIL_EXPEDITEUR, MOT_DE_PASSE);
            }
        });

        try {
            Message email = new MimeMessage(session);
            email.setFrom(new InternetAddress(EMAIL_EXPEDITEUR));
            email.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinataire));
            email.setSubject(sujet);
            email.setText(contenu);

            Transport.send(email);
            System.out.println("Email envoyé à " + destinataire);
        } catch (MessagingException e) {
            System.out.println("Erreur lors de l'envoi de l'email : " + e.getMessage());
        }
    }
} 