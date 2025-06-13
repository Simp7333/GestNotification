package fr.gestnotification;

import fr.gestnotification.model.Employe;
import fr.gestnotification.model.Role;
import fr.gestnotification.model.Service;
import fr.gestnotification.service.ServiceNotification;
import fr.gestnotification.db.DatabaseConnection;
import fr.gestnotification.db.ServiceDAO;
import java.util.Scanner;
import java.sql.SQLException;
import java.util.List;

public class Main {
    private static ServiceNotification service;
    private static ServiceDAO serviceDAO;
    private static Scanner scanner;
    private static Employe employeConnecte;

    public static void main(String[] args) {
        // Initialiser la base de données
        DatabaseConnection.initializeDatabase();
        
        service = new ServiceNotification();
        serviceDAO = new ServiceDAO();
        scanner = new Scanner(System.in);

        // Création automatique du compte admin
        service.ajouterEmploye(new Employe("simp", "Administrateur", "simp7333", Role.ADMIN));

        while (true) {
            if (employeConnecte == null) {
                afficherMenuConnexion();
                int choix = lireChoix();
                
                switch (choix) {
                    case 1:
                        seConnecter();
                        break;
                    case 2:
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
                    case 8:
                        ajouterEmployeParAdmin();
                        break;
                    case 9:
                        gererServices();
                        break;
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
        System.out.println("1. Se connecter");
        System.out.println("2. Quitter");
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
        System.out.println("8. Ajouter un employé");
        System.out.println("9. Gérer les services");
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
        
        if (id.equals("simp")) {
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

    private static void ajouterEmployeParAdmin() {
        System.out.println("\n=== 📝 Ajout d'un employé ===");
        System.out.print("ID de l'employé : ");
        String id = scanner.nextLine();
        System.out.print("Nom de l'employé : ");
        String nom = scanner.nextLine();
        System.out.print("Mot de passe : ");
        String motDePasse = scanner.nextLine();
        System.out.print("Rôle (1: ADMIN, 2: EMPLOYE) : ");
        String roleChoix = scanner.nextLine();

        Role role = roleChoix.equals("1") ? Role.ADMIN : Role.EMPLOYE;
        Employe employe = new Employe(id, nom, motDePasse, role);
        service.ajouterEmploye(employe);
        System.out.println("✅ Employé créé avec succès avec le rôle : " + role);
    }

    private static void gererServices() {
        while (true) {
            System.out.println("\n=== 🔧 Gestion des Services ===");
            System.out.println("1. Voir tous les services");
            System.out.println("2. Créer un service");
            System.out.println("3. Modifier un service");
            System.out.println("4. Supprimer un service");
            System.out.println("5. Retour au menu principal");
            System.out.print("\nVotre choix : ");

            int choix = lireChoix();
            try {
                switch (choix) {
                    case 1:
                        afficherTousLesServices();
                        break;
                    case 2:
                        creerService();
                        break;
                    case 3:
                        modifierService();
                        break;
                    case 4:
                        supprimerService();
                        break;
                    case 5:
                        return;
                    default:
                        System.out.println("❌ Choix invalide. Veuillez réessayer.");
                }
            } catch (SQLException e) {
                System.err.println("❌ Erreur : " + e.getMessage());
            }
        }
    }

    private static void afficherTousLesServices() throws SQLException {
        List<Service> services = serviceDAO.chargerTousLesServices();
        if (services.isEmpty()) {
            System.out.println("Aucun service n'existe.");
            return;
        }
        System.out.println("\n=== 📋 Liste des Services ===");
        for (Service service : services) {
            System.out.println(service);
        }
    }

    private static void creerService() throws SQLException {
        System.out.println("\n=== ➕ Création d'un Service ===");
        System.out.print("ID du service : ");
        String id = scanner.nextLine();
        System.out.print("Nom du service : ");
        String nom = scanner.nextLine();
        System.out.print("Description : ");
        String description = scanner.nextLine();

        Service service = new Service(id, nom, description);
        serviceDAO.creerService(service);
        System.out.println("✅ Service créé avec succès");
    }

    private static void modifierService() throws SQLException {
        System.out.println("\n=== ✏️ Modification d'un Service ===");
        System.out.print("ID du service à modifier : ");
        String id = scanner.nextLine();

        Service service = serviceDAO.chargerService(id);
        if (service == null) {
            System.out.println("❌ Service non trouvé");
            return;
        }

        System.out.print("Nouveau nom (" + service.getNom() + ") : ");
        String nom = scanner.nextLine();
        if (!nom.isEmpty()) service.setNom(nom);

        System.out.print("Nouvelle description (" + service.getDescription() + ") : ");
        String description = scanner.nextLine();
        if (!description.isEmpty()) service.setDescription(description);

        System.out.print("Service actif (o/n) [" + (service.isActif() ? "o" : "n") + "] : ");
        String actif = scanner.nextLine();
        if (!actif.isEmpty()) {
            service.setActif(actif.toLowerCase().startsWith("o"));
        }

        serviceDAO.modifierService(service);
        System.out.println("✅ Service modifié avec succès");
    }

    private static void supprimerService() throws SQLException {
        System.out.println("\n=== 🗑️ Suppression d'un Service ===");
        System.out.print("ID du service à supprimer : ");
        String id = scanner.nextLine();

        Service service = serviceDAO.chargerService(id);
        if (service == null) {
            System.out.println("❌ Service non trouvé");
            return;
        }

        System.out.print("Êtes-vous sûr de vouloir supprimer le service " + service.getNom() + " ? (o/n) : ");
        String confirmation = scanner.nextLine();
        if (confirmation.toLowerCase().startsWith("o")) {
            serviceDAO.supprimerService(id);
            System.out.println("✅ Service supprimé avec succès");
        } else {
            System.out.println("❌ Suppression annulée");
        }
    }
} 