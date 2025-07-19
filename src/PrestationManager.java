package src;

import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * Gestionnaire des prestations (formations et consultations)
 * Partie 3 : Saisir chaque prestation avec ses spécificités
 * Version base de données MySQL/WAMP
 */
public class PrestationManager {

  private DatabaseManager dbManager;

  // Formateurs pour les dates et heures
  private DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private DateTimeFormatter formatHeure = DateTimeFormatter.ofPattern("HH:mm");

  public PrestationManager() {
    this.dbManager = DatabaseManager.getInstance();
  }

  /**
   * Menu pour saisir une nouvelle prestation
   */
  public void saisirPrestation(Scanner scanner) {
    System.out.println("\n=== SAISIE D'UNE NOUVELLE PRESTATION ===");
    System.out.println("Quel type de prestation voulez-vous saisir ?");
    System.out.println("1. Formation (facturée à l'heure)");
    System.out.println("2. Consultation (facturée à la journée avec TJM)");
    System.out.print("Votre choix : ");

    int choix = scanner.nextInt();
    scanner.nextLine(); // Consommer la ligne

    switch (choix) {
      case 1:
        saisirFormation(scanner);
        break;
      case 2:
        saisirConsultation(scanner);
        break;
      default:
        System.out.println("[ERREUR] Choix invalide !");
    }
  }

  /**
   * Saisir une formation avec ses spécificités
   * Format : date, heure début, heure fin, classe, titre/module, entreprise
   */
  private void saisirFormation(Scanner scanner) {
    System.out.println("\n--- SAISIE D'UNE FORMATION ---");

    try {
      // Saisir la date
      System.out.print("Date de la formation (dd/MM/yyyy) : ");
      String dateStr = scanner.nextLine().trim();
      LocalDate date = LocalDate.parse(dateStr, formatDate);

      // Saisir l'heure de début
      System.out.print("Heure de début (HH:mm) : ");
      String heureDebutStr = scanner.nextLine().trim();
      LocalTime heureDebut = LocalTime.parse(heureDebutStr, formatHeure);

      // Saisir l'heure de fin
      System.out.print("Heure de fin (HH:mm) : ");
      String heureFinStr = scanner.nextLine().trim();
      LocalTime heureFin = LocalTime.parse(heureFinStr, formatHeure);

      // Vérifier que l'heure de fin est après l'heure de début
      if (heureFin.isBefore(heureDebut)) {
        System.out.println("[ERREUR] L'heure de fin doit être après l'heure de début !");
        return;
      }

      // Saisir la classe/groupe
      System.out.print("Classe ou groupe de formation : ");
      String classe = scanner.nextLine().trim();

      // Saisir le titre ou module
      System.out.print("Titre de la formation ou module : ");
      String titre = scanner.nextLine().trim();

      // Saisir l'entreprise
      System.out.print("Nom de l'entreprise : ");
      String entreprise = scanner.nextLine().trim();

      // Saisir le tarif horaire
      System.out.print("Tarif horaire (€) : ");
      double tarifHoraire = scanner.nextDouble();
      scanner.nextLine(); // Consommer la ligne

      // Créer la formation
      Formation formation = new Formation(date, heureDebut, heureFin, classe, titre, entreprise, tarifHoraire);

      // Sauvegarder en base de données
      if (sauvegarderFormationEnBase(formation)) {
        System.out.println("[OK] Formation ajoutée avec succès en base de données !");
        System.out.println("Durée : " + formation.calculerDureeHeures() + " heures");
        System.out.println("Montant : " + formation.calculerMontant() + " €");
      } else {
        System.out.println("[ERREUR] Erreur lors de la sauvegarde en base de données !");
      }

    } catch (DateTimeParseException e) {
      System.out.println("[ERREUR] Format de date ou heure invalide !");
    } catch (Exception e) {
      System.out.println("[ERREUR] Erreur lors de la saisie : " + e.getMessage());
    }
  }

  /**
   * Saisir une consultation avec ses spécificités
   * Format : date, description, TJM (Taux Journalier Moyen), entreprise
   */
  private void saisirConsultation(Scanner scanner) {
    System.out.println("\n--- SAISIE D'UNE CONSULTATION ---");

    try {
      // Saisir la date
      System.out.print("Date de la consultation (dd/MM/yyyy) : ");
      String dateStr = scanner.nextLine().trim();
      LocalDate date = LocalDate.parse(dateStr, formatDate);

      // Saisir la description
      System.out.print("Description de la consultation : ");
      String description = scanner.nextLine().trim();

      // Saisir le TJM
      System.out.print("TJM - Taux Journalier Moyen (€) : ");
      double tjm = scanner.nextDouble();
      scanner.nextLine(); // Consommer la ligne

      // Saisir l'entreprise
      System.out.print("Nom de l'entreprise : ");
      String entreprise = scanner.nextLine().trim();

      // Créer la consultation
      Consultation consultation = new Consultation(date, description, tjm, entreprise);

      // Sauvegarder en base de données
      if (sauvegarderConsultationEnBase(consultation)) {
        System.out.println("[OK] Consultation ajoutée avec succès en base de données !");
        System.out.println("Montant : " + consultation.calculerMontant() + " €");
      } else {
        System.out.println("[ERREUR] Erreur lors de la sauvegarde en base de données !");
      }

    } catch (DateTimeParseException e) {
      System.out.println("[ERREUR] Format de date invalide !");
    } catch (Exception e) {
      System.out.println("[ERREUR] Erreur lors de la saisie : " + e.getMessage());
    }
  }

  /**
   * Afficher toutes les prestations depuis la base de données
   */
  public void afficherPrestations() {
    System.out.println("\n=== LISTE DES PRESTATIONS ===");

    List<Prestation> prestations = chargerPrestationsDepuisBase();

    if (prestations.isEmpty()) {
      System.out.println("Aucune prestation trouvée en base de données.");
      return;
    }

    double totalMontant = 0;
    int numeroPrestation = 1;

    for (Prestation prestation : prestations) {
      System.out.println("\n--- Prestation " + numeroPrestation + " ---");
      prestation.afficher();
      totalMontant += prestation.calculerMontant();
      numeroPrestation++;
    }

    System.out.println("\n[TOTAL] TOTAL GÉNÉRAL : " + totalMontant + " €");
  }

  /**
   * Sauvegarder une formation en base de données
   */
  private boolean sauvegarderFormationEnBase(Formation formation) {
    Connection conn = dbManager.getConnection();
    if (conn == null)
      return false;

    try {
      String sql = "INSERT INTO prestations (type_prestation, date_prestation, entreprise, montant, " +
          "heure_debut, heure_fin, classe, titre, tarif_horaire) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";

      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, "Formation");
      stmt.setDate(2, Date.valueOf(formation.getDate()));
      stmt.setString(3, formation.getEntreprise());
      stmt.setDouble(4, formation.calculerMontant());
      stmt.setTime(5, Time.valueOf(formation.getHeureDebut()));
      stmt.setTime(6, Time.valueOf(formation.getHeureFin()));
      stmt.setString(7, formation.getClasse());
      stmt.setString(8, formation.getTitre());
      stmt.setDouble(9, formation.getTarifHoraire());

      int result = stmt.executeUpdate();
      stmt.close();
      return result > 0;

    } catch (SQLException e) {
      System.err.println("[ERREUR] Erreur lors de la sauvegarde de la formation : " + e.getMessage());
      return false;
    }
  }

  /**
   * Sauvegarder une consultation en base de données
   */
  private boolean sauvegarderConsultationEnBase(Consultation consultation) {
    Connection conn = dbManager.getConnection();
    if (conn == null)
      return false;

    try {
      String sql = "INSERT INTO prestations (type_prestation, date_prestation, entreprise, montant, " +
          "description, tjm) VALUES (?, ?, ?, ?, ?, ?)";

      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setString(1, "Consultation");
      stmt.setDate(2, Date.valueOf(consultation.getDate()));
      stmt.setString(3, consultation.getEntreprise());
      stmt.setDouble(4, consultation.calculerMontant());
      stmt.setString(5, consultation.getDescription());
      stmt.setDouble(6, consultation.getTjm());

      int result = stmt.executeUpdate();
      stmt.close();
      return result > 0;

    } catch (SQLException e) {
      System.err.println("[ERREUR] Erreur lors de la sauvegarde de la consultation : " + e.getMessage());
      return false;
    }
  }

  /**
   * Charger toutes les prestations depuis la base de données
   */
  private List<Prestation> chargerPrestationsDepuisBase() {
    List<Prestation> prestations = new ArrayList<>();
    Connection conn = dbManager.getConnection();
    if (conn == null)
      return prestations;

    try {
      String sql = "SELECT * FROM prestations ORDER BY date_prestation DESC";
      PreparedStatement stmt = conn.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String type = rs.getString("type_prestation");

        if ("Formation".equals(type)) {
          // Reconstruire une formation
          Formation formation = new Formation(
              rs.getDate("date_prestation").toLocalDate(),
              rs.getTime("heure_debut").toLocalTime(),
              rs.getTime("heure_fin").toLocalTime(),
              rs.getString("classe"),
              rs.getString("titre"),
              rs.getString("entreprise"),
              rs.getDouble("tarif_horaire"));
          prestations.add(formation);

        } else if ("Consultation".equals(type)) {
          // Reconstruire une consultation
          Consultation consultation = new Consultation(
              rs.getDate("date_prestation").toLocalDate(),
              rs.getString("description"),
              rs.getDouble("tjm"),
              rs.getString("entreprise"));
          prestations.add(consultation);
        }
      }

      rs.close();
      stmt.close();

    } catch (SQLException e) {
      System.err.println("[ERREUR] Erreur lors du chargement des prestations : " + e.getMessage());
    }

    return prestations;
  }

  /**
   * Obtenir toutes les prestations depuis la base de données
   */
  public List<Prestation> getPrestations() {
    return chargerPrestationsDepuisBase();
  }

  /**
   * Méthode publique pour sauvegarder une formation (utilisée par l'interface
   * graphique)
   */
  public boolean sauvegarderFormation(Formation formation) {
    return sauvegarderFormationEnBase(formation);
  }

  /**
   * Méthode publique pour sauvegarder une consultation (utilisée par l'interface
   * graphique)
   */
  public boolean sauvegarderConsultation(Consultation consultation) {
    return sauvegarderConsultationEnBase(consultation);
  }
}