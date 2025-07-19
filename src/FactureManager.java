package src;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.awt.Desktop;

/**
 * Gestionnaire pour la génération de factures mensuelles en HTML (imprimables
 * en PDF)
 */
public class FactureManager {

  private DatabaseManager dbManager;
  private DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
  private DateTimeFormatter formatMois = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH);

  public FactureManager() {
    this.dbManager = DatabaseManager.getInstance();
  }

  /**
   * Génère les factures mensuelles pour tous les clients
   */
  public Map<String, String> genererFacturesMensuelles(int annee, int mois) {
    Map<String, String> facturesGenerees = new HashMap<>();

    try {
      Map<String, List<Prestation>> prestationsParClient = getPrestationsParClient(annee, mois);

      for (String entreprise : prestationsParClient.keySet()) {
        List<Prestation> prestations = prestationsParClient.get(entreprise);
        if (!prestations.isEmpty()) {
          String cheminFichier = genererFactureHTML(entreprise, prestations, annee, mois);
          if (cheminFichier != null) {
            facturesGenerees.put(entreprise, cheminFichier);
          }
        }
      }

    } catch (Exception e) {
      System.err.println("[ERREUR] Erreur lors de la génération des factures : " + e.getMessage());
      e.printStackTrace();
    }

    return facturesGenerees;
  }

  /**
   * Génère la facture pour une entreprise spécifique
   */
  public Map<String, String> genererFactureEntreprise(int annee, int mois, String entreprise) {
    Map<String, String> facturesGenerees = new HashMap<>();

    try {
      Map<String, List<Prestation>> prestationsParClient = getPrestationsParClient(annee, mois);

      if (prestationsParClient.containsKey(entreprise)) {
        List<Prestation> prestations = prestationsParClient.get(entreprise);
        if (!prestations.isEmpty()) {
          String cheminFichier = genererFactureHTML(entreprise, prestations, annee, mois);
          if (cheminFichier != null) {
            facturesGenerees.put(entreprise, cheminFichier);
          }
        }
      }

    } catch (Exception e) {
      System.err
          .println("[ERREUR] Erreur lors de la génération de la facture pour " + entreprise + " : " + e.getMessage());
      e.printStackTrace();
    }

    return facturesGenerees;
  }

  /**
   * Récupère les prestations groupées par client pour un mois donné
   */
  public Map<String, List<Prestation>> getPrestationsParClient(int annee, int mois) {
    Map<String, List<Prestation>> prestationsParClient = new HashMap<>();
    Connection conn = dbManager.getConnection();

    if (conn == null)
      return prestationsParClient;

    try {
      String sql = "SELECT * FROM prestations WHERE YEAR(date_prestation) = ? AND MONTH(date_prestation) = ? ORDER BY entreprise, date_prestation";
      PreparedStatement stmt = conn.prepareStatement(sql);
      stmt.setInt(1, annee);
      stmt.setInt(2, mois);

      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        String entreprise = rs.getString("entreprise");
        Prestation prestation = creerPrestationDepuisResultSet(rs);

        prestationsParClient.computeIfAbsent(entreprise, k -> new ArrayList<>()).add(prestation);
      }

      rs.close();
      stmt.close();

    } catch (SQLException e) {
      System.err.println("[ERREUR] Erreur lors de la récupération des prestations : " + e.getMessage());
    }

    return prestationsParClient;
  }

  /**
   * Crée un objet Prestation à partir d'un ResultSet
   */
  private Prestation creerPrestationDepuisResultSet(ResultSet rs) throws SQLException {
    String type = rs.getString("type_prestation");
    LocalDate date = rs.getDate("date_prestation").toLocalDate();
    String entreprise = rs.getString("entreprise");

    if ("Formation".equals(type)) {
      return new Formation(
          date,
          rs.getTime("heure_debut").toLocalTime(),
          rs.getTime("heure_fin").toLocalTime(),
          rs.getString("classe"),
          rs.getString("titre"),
          entreprise,
          rs.getDouble("tarif_horaire"));
    } else {
      return new Consultation(
          date,
          rs.getString("description"),
          rs.getDouble("tjm"),
          entreprise);
    }
  }

  /**
   * Génère un fichier HTML de facture pour un client (facilement imprimable en
   * PDF)
   */
  public String genererFactureHTML(String entreprise, List<Prestation> prestations, int annee, int mois) {
    try {
      // Créer le dossier factures s'il n'existe pas
      File dossierFactures = new File("factures");
      if (!dossierFactures.exists()) {
        dossierFactures.mkdirs();
      }

      YearMonth yearMonth = YearMonth.of(annee, mois);
      String nomFichier = String.format("factures/Facture_%s_%s.html",
          entreprise.replaceAll("[^a-zA-Z0-9]", "_"),
          yearMonth.format(DateTimeFormatter.ofPattern("yyyy_MM")));

      // Créer le contenu HTML
      StringBuilder htmlContent = new StringBuilder();
      genererContenuHTML(htmlContent, entreprise, prestations, yearMonth);

      // Écrire le fichier
      try (FileWriter writer = new FileWriter(nomFichier, java.nio.charset.StandardCharsets.UTF_8)) {
        writer.write(htmlContent.toString());
      }

      System.out.println("[OK] Facture générée : " + nomFichier);
      return nomFichier;

    } catch (Exception e) {
      System.err.println("[ERREUR] Erreur lors de la génération de la facture : " + e.getMessage());
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Génère le contenu HTML de la facture
   */
  private void genererContenuHTML(StringBuilder html, String entreprise, List<Prestation> prestations,
      YearMonth yearMonth) {
    html.append("<!DOCTYPE html>\n");
    html.append("<html lang='fr'>\n");
    html.append("<head>\n");
    html.append("    <meta charset='UTF-8'>\n");
    html.append("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
    html.append("    <title>Facture - ").append(entreprise).append("</title>\n");
    html.append("    <style>\n");
    html.append("        body { font-family: Arial, sans-serif; margin: 40px; color: #333; }\n");
    html.append("        .header { text-align: center; margin-bottom: 30px; }\n");
    html.append("        .title { font-size: 28px; font-weight: bold; color: #2980b9; margin-bottom: 20px; }\n");
    html.append("        .info { margin-bottom: 20px; }\n");
    html.append("        .client-info { background-color: #f8f9fa; padding: 15px; border-left: 4px solid #2980b9; }\n");
    html.append("        table { width: 100%; border-collapse: collapse; margin: 20px 0; }\n");
    html.append("        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }\n");
    html.append("        th { background-color: #2980b9; color: white; font-weight: bold; }\n");
    html.append("        tr:nth-child(even) { background-color: #f2f2f2; }\n");
    html.append(
        "        .total { text-align: right; font-size: 18px; font-weight: bold; margin: 20px 0; color: #2980b9; }\n");
    html.append("        .footer { text-align: center; margin-top: 40px; font-size: 12px; color: #666; }\n");
    html.append(
        "        .print-btn { background-color: #2980b9; color: white; padding: 10px 20px; border: none; border-radius: 5px; cursor: pointer; margin: 20px 0; }\n");
    html.append("        @media print { .print-btn { display: none; } body { margin: 20px; } }\n");
    html.append("    </style>\n");
    html.append("</head>\n");
    html.append("<body>\n");

    // Bouton d'impression
    html.append("    <button class='print-btn' onclick='window.print()'>Imprimer / Enregistrer en PDF</button>\n");

    // En-tête
    html.append("    <div class='header'>\n");
    html.append("        <h1 class='title'>FACTURE</h1>\n");
    html.append("    </div>\n");

    // Informations prestataire
    html.append("    <div class='info'>\n");
    html.append("        <strong>Micro Entrepreneur</strong><br>\n");
    html.append("        Email: contact@entrepreneur.fr<br>\n");
    html.append("        Date: ").append(LocalDate.now().format(formatDate)).append("\n");
    html.append("    </div>\n");

    // Informations client
    html.append("    <div class='client-info'>\n");
    html.append("        <strong>Facturé à :</strong> ").append(entreprise).append("<br>\n");
    html.append("        <strong>Période :</strong> ").append(yearMonth.format(formatMois)).append("\n");
    html.append("    </div>\n");

    // Tableau des prestations
    html.append("    <table>\n");
    html.append("        <thead>\n");
    html.append("            <tr>\n");
    html.append("                <th>Date</th>\n");
    html.append("                <th>Description</th>\n");
    html.append("                <th>Type</th>\n");
    html.append("                <th>Détails</th>\n");
    html.append("                <th>Montant (€)</th>\n");
    html.append("            </tr>\n");
    html.append("        </thead>\n");
    html.append("        <tbody>\n");

    double total = 0;
    for (Prestation prestation : prestations) {
      html.append("            <tr>\n");
      html.append("                <td>").append(prestation.getDateFormatee()).append("</td>\n");

      String description = "";
      String details = "";

      if (prestation instanceof Formation) {
        Formation formation = (Formation) prestation;
        description = formation.getTitre() + " (" + formation.getClasse() + ")";
        details = String.format("%.1fh à %.0f€/h", formation.calculerDureeHeures(), formation.getTarifHoraire());
      } else if (prestation instanceof Consultation) {
        Consultation consultation = (Consultation) prestation;
        description = consultation.getDescription();
        details = String.format("TJM: %.0f€", consultation.getTjm());
      }

      html.append("                <td>").append(description).append("</td>\n");
      html.append("                <td>").append(prestation.getType()).append("</td>\n");
      html.append("                <td>").append(details).append("</td>\n");
      html.append("                <td>").append(String.format("%.2f", prestation.calculerMontant())).append("</td>\n");
      html.append("            </tr>\n");

      total += prestation.calculerMontant();
    }

    html.append("        </tbody>\n");
    html.append("    </table>\n");

    // Total
    html.append("    <div class='total'>\n");
    html.append("        TOTAL: ").append(String.format("%.2f", total)).append(" €\n");
    html.append("    </div>\n");

    // Pied de page
    html.append("    <div class='footer'>\n");
    html.append("        <p>Merci pour votre confiance !</p>\n");
    html.append("        <p>Facture générée automatiquement le ").append(LocalDate.now().format(formatDate))
        .append("</p>\n");
    html.append("    </div>\n");

    html.append("</body>\n");
    html.append("</html>\n");
  }

  /**
   * Obtient la liste des entreprises qui ont des prestations
   */
  public List<String> getEntreprises() {
    List<String> entreprises = new ArrayList<>();
    Connection conn = dbManager.getConnection();

    if (conn == null)
      return entreprises;

    try {
      String sql = "SELECT DISTINCT entreprise FROM prestations ORDER BY entreprise";
      PreparedStatement stmt = conn.prepareStatement(sql);
      ResultSet rs = stmt.executeQuery();

      while (rs.next()) {
        entreprises.add(rs.getString("entreprise"));
      }

      rs.close();
      stmt.close();

    } catch (SQLException e) {
      System.err.println("[ERREUR] Erreur lors de la récupération des entreprises : " + e.getMessage());
    }

    return entreprises;
  }

  /**
   * Ouvre une facture générée dans le navigateur par défaut
   */
  public void ouvrirFacture(String cheminFichier) {
    try {
      File fichier = new File(cheminFichier);
      if (fichier.exists() && Desktop.isDesktopSupported()) {
        Desktop.getDesktop().open(fichier);
      }
    } catch (Exception e) {
      System.err.println("[ERREUR] Impossible d'ouvrir la facture : " + e.getMessage());
    }
  }
}