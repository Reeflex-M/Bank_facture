package src;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

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

    // Créer le gestionnaire de factures
    FactureManager factureManager = new FactureManager();

    // Créer le gestionnaire de reporting
    ReportingManager reportingManager = new ReportingManager();

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
        System.out.println("3. Générer des factures");
        System.out.println("4. Reporting financier");
        System.out.println("5. Quitter");
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
            menuFactures(scanner, factureManager);
            break;
          case 4:
            menuReporting(scanner, reportingManager);
            break;
          case 5:
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

  /**
   * Menu de gestion des factures
   */
  private static void menuFactures(Scanner scanner, FactureManager factureManager) {
    System.out.println("\n=== GÉNÉRATION DE FACTURES ===");
    System.out.println("1. Générer factures mensuelles pour tous les clients");
    System.out.println("2. Générer facture pour une entreprise spécifique");
    System.out.println("3. Retour au menu principal");
    System.out.print("Votre choix : ");

    int choix = scanner.nextInt();
    scanner.nextLine(); // Consommer la ligne

    switch (choix) {
      case 1:
        genererFacturesMensuelles(scanner, factureManager);
        break;
      case 2:
        genererFactureEntreprise(scanner, factureManager);
        break;
      case 3:
        return;
      default:
        System.out.println("[ERREUR] Choix invalide !");
    }
  }

  /**
   * Menu de reporting financier
   */
  private static void menuReporting(Scanner scanner, ReportingManager reportingManager) {
    System.out.println("\n=== REPORTING FINANCIER ===");
    System.out.println("1. Bilan mensuel");
    System.out.println("2. Bilan annuel");
    System.out.println("3. Bilan sur période personnalisée");
    System.out.println("4. Retour au menu principal");
    System.out.print("Votre choix : ");

    int choix = scanner.nextInt();
    scanner.nextLine(); // Consommer la ligne

    switch (choix) {
      case 1:
        genererBilanMensuel(scanner, reportingManager);
        break;
      case 2:
        genererBilanAnnuel(scanner, reportingManager);
        break;
      case 3:
        genererBilanPeriode(scanner, reportingManager);
        break;
      case 4:
        return;
      default:
        System.out.println("[ERREUR] Choix invalide !");
    }
  }

  /**
   * Génère les factures mensuelles pour tous les clients
   */
  private static void genererFacturesMensuelles(Scanner scanner, FactureManager factureManager) {
    System.out.print("Année (ex: 2024) : ");
    int annee = scanner.nextInt();
    System.out.print("Mois (1-12) : ");
    int mois = scanner.nextInt();

    if (mois < 1 || mois > 12) {
      System.out.println("[ERREUR] Mois invalide !");
      return;
    }

    System.out.println("\n[INFO] Génération des factures en cours...");
    var facturesGenerees = factureManager.genererFacturesMensuelles(annee, mois);

    if (facturesGenerees.isEmpty()) {
      System.out.println("[INFO] Aucune facture générée pour cette période.");
    } else {
      System.out.println("[OK] " + facturesGenerees.size() + " facture(s) générée(s) :");
      for (String entreprise : facturesGenerees.keySet()) {
        System.out.println("  - " + entreprise + " : " + facturesGenerees.get(entreprise));
      }
    }
  }

  /**
   * Génère la facture pour une entreprise spécifique
   */
  private static void genererFactureEntreprise(Scanner scanner, FactureManager factureManager) {
    System.out.print("Année (ex: 2024) : ");
    int annee = scanner.nextInt();
    System.out.print("Mois (1-12) : ");
    int mois = scanner.nextInt();
    scanner.nextLine(); // Consommer la ligne

    System.out.print("Nom de l'entreprise : ");
    String entreprise = scanner.nextLine();

    if (mois < 1 || mois > 12) {
      System.out.println("[ERREUR] Mois invalide !");
      return;
    }

    System.out.println("\n[INFO] Génération de la facture en cours...");
    var facturesGenerees = factureManager.genererFactureEntreprise(annee, mois, entreprise);

    if (facturesGenerees.isEmpty()) {
      System.out.println("[INFO] Aucune facture générée pour cette entreprise et cette période.");
    } else {
      System.out.println("[OK] Facture générée : " + facturesGenerees.get(entreprise));
    }
  }

  /**
   * Génère un bilan mensuel
   */
  private static void genererBilanMensuel(Scanner scanner, ReportingManager reportingManager) {
    System.out.print("Année (ex: 2024) : ");
    int annee = scanner.nextInt();
    System.out.print("Mois (1-12) : ");
    int mois = scanner.nextInt();

    if (mois < 1 || mois > 12) {
      System.out.println("[ERREUR] Mois invalide !");
      return;
    }

    System.out.println("\n[INFO] Génération du bilan mensuel en cours...");
    String cheminFichier = reportingManager.genererBilanMensuel(annee, mois);

    if (cheminFichier != null) {
      System.out.println("[OK] Bilan mensuel généré : " + cheminFichier);
      System.out.print("Voulez-vous ouvrir le bilan ? (o/n) : ");
      String reponse = scanner.next();
      if ("o".equalsIgnoreCase(reponse) || "oui".equalsIgnoreCase(reponse)) {
        reportingManager.ouvrirBilan(cheminFichier);
      }
    } else {
      System.out.println("[ERREUR] Échec de la génération du bilan mensuel.");
    }
  }

  /**
   * Génère un bilan annuel
   */
  private static void genererBilanAnnuel(Scanner scanner, ReportingManager reportingManager) {
    System.out.print("Année (ex: 2024) : ");
    int annee = scanner.nextInt();

    System.out.println("\n[INFO] Génération du bilan annuel en cours...");
    String cheminFichier = reportingManager.genererBilanAnnuel(annee);

    if (cheminFichier != null) {
      System.out.println("[OK] Bilan annuel généré : " + cheminFichier);
      System.out.print("Voulez-vous ouvrir le bilan ? (o/n) : ");
      String reponse = scanner.next();
      if ("o".equalsIgnoreCase(reponse) || "oui".equalsIgnoreCase(reponse)) {
        reportingManager.ouvrirBilan(cheminFichier);
      }
    } else {
      System.out.println("[ERREUR] Échec de la génération du bilan annuel.");
    }
  }

  /**
   * Génère un bilan sur une période personnalisée
   */
  private static void genererBilanPeriode(Scanner scanner, ReportingManager reportingManager) {
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    System.out.print("Date de début (format: dd/MM/yyyy) : ");
    String dateDebutStr = scanner.nextLine();

    System.out.print("Date de fin (format: dd/MM/yyyy) : ");
    String dateFinStr = scanner.nextLine();

    try {
      LocalDate dateDebut = LocalDate.parse(dateDebutStr, formatter);
      LocalDate dateFin = LocalDate.parse(dateFinStr, formatter);

      if (dateDebut.isAfter(dateFin)) {
        System.out.println("[ERREUR] La date de début doit être antérieure à la date de fin !");
        return;
      }

      System.out.println("\n[INFO] Génération du bilan de période en cours...");
      String cheminFichier = reportingManager.genererBilanPeriode(dateDebut, dateFin);

      if (cheminFichier != null) {
        System.out.println("[OK] Bilan de période généré : " + cheminFichier);
        System.out.print("Voulez-vous ouvrir le bilan ? (o/n) : ");
        String reponse = scanner.next();
        if ("o".equalsIgnoreCase(reponse) || "oui".equalsIgnoreCase(reponse)) {
          reportingManager.ouvrirBilan(cheminFichier);
        }
      } else {
        System.out.println("[ERREUR] Échec de la génération du bilan de période.");
      }

    } catch (DateTimeParseException e) {
      System.out.println("[ERREUR] Format de date invalide ! Utilisez le format dd/MM/yyyy");
    }
  }
}