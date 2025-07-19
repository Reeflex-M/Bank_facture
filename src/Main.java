package src;

import java.util.Scanner;

/**
 * Application principale pour la gestion des factures d'un micro-entrepreneur
 * Permet de gérer les formations et consultations avec facturation
 */
public class Main {

  public static void main(String[] args) {
    System.out.println("=== Gestion des Factures - Micro Entrepreneur ===");
    System.out.println("Application de facturation pour formations et consultations");

    // Afficher les identifiants de démonstration
    AuthManager.afficherIdentifiantsDemo();

    // Créer le gestionnaire d'authentification
    AuthManager authManager = new AuthManager();

    // Créer le gestionnaire de prestations
    PrestationManager prestationManager = new PrestationManager();

    Scanner scanner = new Scanner(System.in);

    // Étape 1.2 : Authentification
    if (authManager.seConnecter(scanner)) {
      System.out.println("\n[OK] Connexion réussie !");

      // Menu principal après authentification
      boolean continuer = true;
      while (continuer) {
        System.out.println("\n=== MENU PRINCIPAL ===");
        System.out.println("1. Saisir une nouvelle prestation");
        System.out.println("2. Voir toutes les prestations");
        System.out.println("3. Quitter");
        System.out.print("Votre choix : ");

        int choix = scanner.nextInt();
        scanner.nextLine(); // Consommer la ligne

        switch (choix) {
          case 1:
            // Partie 3 : Saisir les prestations
            prestationManager.saisirPrestation(scanner);
            break;
          case 2:
            prestationManager.afficherPrestations();
            break;
          case 3:
            continuer = false;
            System.out.println("Au revoir !");
            break;
          default:
            System.out.println("[ERREUR] Choix invalide !");
        }
      }
    } else {
      System.out.println("[ERREUR] Échec de la connexion. Au revoir !");
    }

    scanner.close();
  }
}