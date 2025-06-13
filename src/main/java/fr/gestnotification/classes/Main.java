package fr.gestnotification.classes;

import fr.gestnotification.classes.Employe;
import fr.gestnotification.classes.Role;
import fr.gestnotification.classes.ServiceNotification;
import java.util.Scanner;

public class Main {
    private static ServiceNotification service;
    private static Scanner scanner;
    private static Employe employeConnecte;

    public static void main(String[] args) {
        System.out.println("\n=== 🚀 Bienvenue dans le Gestionnaire de Notifications ===");
        
        // Initialiser la base de données
        DatabaseConnection.initializeDatabase();
        
        service = new ServiceNotification();
        scanner = new Scanner(System.in);

        // Création automatique du compte admin
        service.ajouterEmploye(new Employe("simp", "Administrateur", "simp7333", Role.ADMIN));
        System.out.println("\nℹ️ Un compte administrateur a été créé automatiquement.");
        System.out.println("   ID: simp");
        System.out.println("   Mot de passe: simp7333");

        while (true) {
            if (employeConnecte == null) {
                afficherMenuConnexion();
            } else if (employeConnecte.estAdmin()) {
                afficherMenuAdmin();
            } else {
                afficherMenuEmploye();
            }
        }
    }

    private static void afficherMenuConnexion() {
        System.out.println("\n=== 🔐 Connexion ===");
        System.out.print("ID : ");
        String id = scanner.nextLine().trim();
        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine().trim();

        if (id.isEmpty() || motDePasse.isEmpty()) {
            System.out.println("❌ L'ID et le mot de passe ne peuvent pas être vides");
            return;
        }

        employeConnecte = service.authentifier(id, motDePasse);
        if (employeConnecte == null) {
            System.out.println("❌ Identifiants incorrects");
        } else {
            System.out.println("✅ Connexion réussie !");
        }
    }

    private static void afficherMenuAdmin() {
        System.out.println("\n=== 👑 Console d'Administration ===");
        System.out.println("Connecté en tant que : " + employeConnecte);
        System.out.println("\nQue souhaitez-vous faire ?");
        System.out.println("1. 👥 Voir tous les employés");
        System.out.println("2. 🗑️ Supprimer un employé");
        System.out.println("3. 📨 Envoyer une notification");
        System.out.println("4. 📢 Voir la liste des abonnés");
        System.out.println("5. 📋 Consulter mes notifications");
        System.out.println("6. ➕ Ajouter un employé");
        System.out.println("7. 🔒 Se déconnecter");
        System.out.println("8. ❌ Quitter");
        System.out.print("\nVotre choix : ");

        int choix = lireChoix();
        switch (choix) {
            case 1:
                service.afficherTousLesEmployes();
                break;
            case 2:
                supprimerEmploye();
                break;
            case 3:
                envoyerNotification();
                break;
            case 4:
                service.afficherAbonnes();
                break;
            case 5:
                employeConnecte.afficherNotifications();
                break;
            case 6:
                ajouterEmployeParAdmin();
                break;
            case 7:
                System.out.println("👋 Au revoir " + employeConnecte.getNom() + " !");
                employeConnecte = null;
                break;
            case 8:
                System.out.println("\n👋 Merci d'avoir utilisé le Gestionnaire de Notifications !");
                System.exit(0);
                break;
            default:
                System.out.println("❌ Choix invalide. Veuillez entrer un nombre entre 1 et 8.");
        }
    }

    private static void afficherMenuEmploye() {
        System.out.println("\n=== 📱 Gestionnaire de Notifications ===");
        System.out.println("Connecté en tant que : " + employeConnecte);
        System.out.println("\nQue souhaitez-vous faire ?");
        System.out.println("1. 📢 S'abonner aux notifications");
        System.out.println("2. 🔕 Se désabonner des notifications");
        System.out.println("3. 📨 Envoyer une notification");
        System.out.println("4. 👥 Voir la liste des abonnés");
        System.out.println("5. 📋 Consulter mes notifications");
        System.out.println("6. 🔒 Se déconnecter");
        System.out.println("7. ❌ Quitter");
        System.out.print("\nVotre choix : ");

        int choix = lireChoix();
        switch (choix) {
            case 1:
                service.abonner(employeConnecte);
                break;
            case 2:
                service.desabonner(employeConnecte);
                break;
            case 3:
                envoyerNotification();
                break;
            case 4:
                service.afficherAbonnes();
                break;
            case 5:
                employeConnecte.afficherNotifications();
                break;
            case 6:
                System.out.println("👋 Au revoir " + employeConnecte.getNom() + " !");
                employeConnecte = null;
                break;
            case 7:
                System.out.println("\n👋 Merci d'avoir utilisé le Gestionnaire de Notifications !");
                System.exit(0);
                break;
            default:
                System.out.println("❌ Choix invalide. Veuillez entrer un nombre entre 1 et 7.");
        }
    }

    private static int lireChoix() {
        try {
            return Integer.parseInt(scanner.nextLine().trim());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void supprimerEmploye() {
        System.out.println("\n=== 🗑️ Suppression d'un employé ===");
        System.out.print("ID de l'employé à supprimer : ");
        String id = scanner.nextLine().trim();
        
        if (id.isEmpty()) {
            System.out.println("❌ L'ID ne peut pas être vide");
            return;
        }

        if (id.equals(employeConnecte.getIdentifiant())) {
            System.out.println("❌ Vous ne pouvez pas supprimer votre propre compte");
            return;
        }

        service.supprimerEmploye(id);
    }

    private static void envoyerNotification() {
        System.out.println("\n=== 📨 Envoi d'une notification ===");
        System.out.print("Message : ");
        String message = scanner.nextLine().trim();
        
        if (message.isEmpty()) {
            System.out.println("❌ Le message ne peut pas être vide");
            return;
        }

        service.envoyerNotification(message, employeConnecte);
        System.out.println("✅ Notification envoyée avec succès !");
    }

    private static void ajouterEmployeParAdmin() {
        System.out.println("\n=== ➕ Ajout d'un employé ===");
        
        System.out.print("ID de l'employé : ");
        String id = scanner.nextLine().trim();
        if (id.isEmpty()) {
            System.out.println("❌ L'ID ne peut pas être vide");
            return;
        }

        System.out.print("Nom de l'employé : ");
        String nom = scanner.nextLine().trim();
        if (nom.isEmpty()) {
            System.out.println("❌ Le nom ne peut pas être vide");
            return;
        }

        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine().trim();
        if (motDePasse.isEmpty()) {
            System.out.println("❌ Le mot de passe ne peut pas être vide");
            return;
        }

        System.out.print("Rôle (1: ADMIN, 2: EMPLOYE) : ");
        String roleChoix = scanner.nextLine().trim();
        
        Role role;
        if (roleChoix.equals("1")) {
            role = Role.ADMIN;
        } else if (roleChoix.equals("2")) {
            role = Role.EMPLOYE;
        } else {
            System.out.println("❌ Choix de rôle invalide");
            return;
        }

        Employe employe = new Employe(id, nom, motDePasse, role);
        service.ajouterEmploye(employe);
        System.out.println("✅ Employé créé avec succès !");
    }
} 