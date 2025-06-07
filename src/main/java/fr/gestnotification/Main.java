package fr.gestnotification;

import fr.gestnotification.model.Employe;
import fr.gestnotification.model.Role;
import fr.gestnotification.service.ServiceNotification;
import java.util.Scanner;

public class Main {
    private static ServiceNotification service;
    private static Scanner scanner;
    private static Employe employeConnecte;

    public static void main(String[] args) {
        service = new ServiceNotification();
        scanner = new Scanner(System.in);

        // Création automatique du compte admin
        service.ajouterEmploye(new Employe("admin", "Administrateur", "admin123", Role.ADMIN));
        System.out.println("ℹ️ Compte admin créé (ID: admin, mot de passe: admin123)");

        while (true) {
            if (employeConnecte == null) {
                afficherMenuConnexion();
                int choix = lireChoix();
                
                switch (choix) {
                    case 1:
                        creerCompte();
                        break;
                    case 2:
                        seConnecter();
                        break;
                    case 3:
                        System.out.println("Au revoir !");
                        scanner.close();
                        return;
                    default:
                        System.out.println("❌ Choix invalide. Veuillez réessayer.");
                }
            } else if (employeConnecte.estAdmin()) {
                afficherMenuAdmin();
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
                        afficherAbonnes();
                        break;
                    case 5:
                        consulterMesNotifications();
                        break;
                    case 6:
                        seDeconnecter();
                        break;
                    case 7:
                        System.out.println("Au revoir !");
                        scanner.close();
                        return;
                    default:
                        System.out.println("❌ Choix invalide. Veuillez réessayer.");
                }
            } else {
                afficherMenuEmploye();
                int choix = lireChoix();
                
                switch (choix) {
                    case 1:
                        abonnerEmployeConnecte();
                        break;
                    case 2:
                        desabonnerEmployeConnecte();
                        break;
                    case 3:
                        envoyerNotification();
                        break;
                    case 4:
                        afficherAbonnes();
                        break;
                    case 5:
                        consulterMesNotifications();
                        break;
                    case 6:
                        seDeconnecter();
                        break;
                    case 7:
                        System.out.println("Au revoir !");
                        scanner.close();
                        return;
                    default:
                        System.out.println("❌ Choix invalide. Veuillez réessayer.");
                }
            }
        }
    }

    private static void afficherMenuConnexion() {
        System.out.println("\n=== 🔐 Système de Notifications - Login ===");
        System.out.println("1. Créer un compte");
        System.out.println("2. Se connecter");
        System.out.println("3. Quitter");
        System.out.print("\nVotre choix : ");
    }

    private static void afficherMenuAdmin() {
        System.out.println("\n=== 👑 Console d'Administration ===");
        System.out.println("Connecté en tant que : " + employeConnecte);
        System.out.println("1. Voir tous les employés");
        System.out.println("2. Supprimer un employé");
        System.out.println("3. Envoyer une notification");
        System.out.println("4. Voir la liste des abonnés");
        System.out.println("5. Consulter mes notifications");
        System.out.println("6. Se déconnecter");
        System.out.println("7. Quitter");
        System.out.print("\nVotre choix : ");
    }

    private static void afficherMenuEmploye() {
        System.out.println("\n=== 📱 Gestionnaire de Notifications ===");
        System.out.println("Connecté en tant que : " + employeConnecte);
        System.out.println("1. S'abonner aux notifications");
        System.out.println("2. Se désabonner des notifications");
        System.out.println("3. Envoyer une notification");
        System.out.println("4. Voir la liste des abonnés");
        System.out.println("5. Consulter mes notifications");
        System.out.println("6. Se déconnecter");
        System.out.println("7. Quitter");
        System.out.print("\nVotre choix : ");
    }

    private static int lireChoix() {
        try {
            return Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    private static void creerCompte() {
        System.out.println("\n=== 📝 Création de compte ===");
        System.out.print("ID de l'employé : ");
        String id = scanner.nextLine();
        System.out.print("Nom de l'employé : ");
        String nom = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();

        Employe employe = new Employe(id, nom, motDePasse);
        service.ajouterEmploye(employe);
    }

    private static void seConnecter() {
        System.out.println("\n=== 🔑 Connexion ===");
        System.out.print("ID de l'employé : ");
        String id = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();

        Employe employe = service.authentifier(id, motDePasse);
        if (employe != null) {
            employeConnecte = employe;
            System.out.println("✅ Connecté en tant que : " + employe);
        } else {
            System.out.println("❌ Identifiants incorrects");
        }
    }

    private static void seDeconnecter() {
        System.out.println("👋 Au revoir, " + employeConnecte + " !");
        employeConnecte = null;
    }

    private static void supprimerEmploye() {
        System.out.println("\n=== 🗑️ Suppression d'un employé ===");
        System.out.print("ID de l'employé à supprimer : ");
        String id = scanner.nextLine();
        
        if (id.equals("admin")) {
            System.out.println("❌ Impossible de supprimer le compte administrateur");
            return;
        }
        
        service.supprimerEmploye(id);
    }

    private static void abonnerEmployeConnecte() {
        service.ajouterObservateur(employeConnecte);
    }

    private static void desabonnerEmployeConnecte() {
        service.retirerObservateur(employeConnecte);
    }

    private static void envoyerNotification() {
        System.out.println("\n=== 📢 Envoi d'une notification ===");
        System.out.print("Message : ");
        String message = scanner.nextLine();

        service.notifierObservateurs(message, employeConnecte.toString());
    }

    private static void afficherAbonnes() {
        service.afficherAbonnes();
    }

    private static void consulterMesNotifications() {
        employeConnecte.afficherNotifications();
    }
}