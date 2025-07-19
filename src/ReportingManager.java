package src;

import java.io.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.awt.Desktop;

/**
 * Gestionnaire pour la génération de rapports financiers en HTML
 * Permet de générer des bilans de chiffre d'affaires (imprimables en PDF)
 */
public class ReportingManager {

 private DatabaseManager dbManager;
 private DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");
 private DateTimeFormatter formatMois = DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH);

 public ReportingManager() {
  this.dbManager = DatabaseManager.getInstance();
 }

 /**
  * Génère un bilan mensuel pour un mois spécifique
  */
 public String genererBilanMensuel(int annee, int mois) {
  try {
   LocalDate dateDebut = LocalDate.of(annee, mois, 1);
   LocalDate dateFin = dateDebut.plusMonths(1).minusDays(1);

   Map<String, Object> donnees = calculerDonneesBilan(dateDebut, dateFin);
   String titre = String.format("Bilan Mensuel - %s", dateDebut.format(formatMois));

   return genererHTML(titre, donnees, dateDebut, dateFin);

  } catch (Exception e) {
   System.err.println("[ERREUR] Erreur lors de la génération du bilan mensuel : " + e.getMessage());
   e.printStackTrace();
   return null;
  }
 }

 /**
  * Génère un bilan annuel pour une année complète
  */
 public String genererBilanAnnuel(int annee) {
  try {
   LocalDate dateDebut = LocalDate.of(annee, 1, 1);
   LocalDate dateFin = LocalDate.of(annee, 12, 31);

   Map<String, Object> donnees = calculerDonneesBilan(dateDebut, dateFin);
   String titre = String.format("Bilan Annuel - %d", annee);

   return genererHTML(titre, donnees, dateDebut, dateFin);

  } catch (Exception e) {
   System.err.println("[ERREUR] Erreur lors de la génération du bilan annuel : " + e.getMessage());
   e.printStackTrace();
   return null;
  }
 }

 /**
  * Génère un bilan sur une période personnalisée
  */
 public String genererBilanPeriode(LocalDate dateDebut, LocalDate dateFin) {
  try {
   Map<String, Object> donnees = calculerDonneesBilan(dateDebut, dateFin);
   String titre = String.format("Bilan Période - Du %s au %s",
     dateDebut.format(formatDate), dateFin.format(formatDate));

   return genererHTML(titre, donnees, dateDebut, dateFin);

  } catch (Exception e) {
   System.err.println("[ERREUR] Erreur lors de la génération du bilan de période : " + e.getMessage());
   e.printStackTrace();
   return null;
  }
 }

 /**
  * Calcule les données du bilan pour une période donnée
  */
 private Map<String, Object> calculerDonneesBilan(LocalDate dateDebut, LocalDate dateFin) {
  Map<String, Object> donnees = new HashMap<>();
  Connection conn = dbManager.getConnection();

  if (conn == null) {
   donnees.put("chiffreAffaires", 0.0);
   donnees.put("nombreFactures", 0);
   donnees.put("prestations", new ArrayList<>());
   return donnees;
  }

  try {
   // Calculer le chiffre d'affaires total et le nombre de factures
   String sqlTotal = "SELECT COUNT(DISTINCT entreprise) as nb_factures, SUM(montant) as ca_total " +
     "FROM prestations " +
     "WHERE date_prestation BETWEEN ? AND ?";

   PreparedStatement stmtTotal = conn.prepareStatement(sqlTotal);
   stmtTotal.setDate(1, java.sql.Date.valueOf(dateDebut));
   stmtTotal.setDate(2, java.sql.Date.valueOf(dateFin));

   ResultSet rsTotal = stmtTotal.executeQuery();

   if (rsTotal.next()) {
    donnees.put("nombreFactures", rsTotal.getInt("nb_factures"));
    donnees.put("chiffreAffaires", rsTotal.getDouble("ca_total"));
   }

   rsTotal.close();
   stmtTotal.close();

   // Récupérer les détails des prestations
   String sqlDetails = "SELECT * FROM prestations " +
     "WHERE date_prestation BETWEEN ? AND ? " +
     "ORDER BY date_prestation, entreprise";

   PreparedStatement stmtDetails = conn.prepareStatement(sqlDetails);
   stmtDetails.setDate(1, java.sql.Date.valueOf(dateDebut));
   stmtDetails.setDate(2, java.sql.Date.valueOf(dateFin));

   ResultSet rsDetails = stmtDetails.executeQuery();
   List<Map<String, Object>> prestations = new ArrayList<>();

   while (rsDetails.next()) {
    Map<String, Object> prestation = new HashMap<>();
    prestation.put("date", rsDetails.getDate("date_prestation").toLocalDate());
    prestation.put("entreprise", rsDetails.getString("entreprise"));
    prestation.put("type", rsDetails.getString("type_prestation"));
    prestation.put("montant", rsDetails.getDouble("montant"));

    if ("Formation".equals(rsDetails.getString("type_prestation"))) {
     prestation.put("titre", rsDetails.getString("titre"));
     prestation.put("classe", rsDetails.getString("classe"));
    } else {
     prestation.put("description", rsDetails.getString("description"));
    }

    prestations.add(prestation);
   }

   donnees.put("prestations", prestations);

   rsDetails.close();
   stmtDetails.close();

  } catch (SQLException e) {
   System.err.println("[ERREUR] Erreur lors du calcul des données du bilan : " + e.getMessage());
   donnees.put("chiffreAffaires", 0.0);
   donnees.put("nombreFactures", 0);
   donnees.put("prestations", new ArrayList<>());
  }

  return donnees;
 }

 /**
  * Génère le fichier HTML du bilan
  */
 private String genererHTML(String titre, Map<String, Object> donnees, LocalDate dateDebut, LocalDate dateFin) {
  try {
   // Créer le dossier rapports s'il n'existe pas
   File dossierRapports = new File("rapports");
   if (!dossierRapports.exists()) {
    dossierRapports.mkdirs();
   }

   String nomFichier = String.format("rapports/Bilan_%s.html",
     titre.replaceAll("[^a-zA-Z0-9]", "_").replaceAll("_+", "_"));

   // Créer le contenu HTML
   StringBuilder htmlContent = new StringBuilder();
   genererContenuHTML(htmlContent, titre, donnees, dateDebut, dateFin);

   // Écrire le fichier
   try (FileWriter writer = new FileWriter(nomFichier, java.nio.charset.StandardCharsets.UTF_8)) {
    writer.write(htmlContent.toString());
   }

   System.out.println("[OK] Bilan généré : " + nomFichier);
   return nomFichier;

  } catch (Exception e) {
   System.err.println("[ERREUR] Erreur lors de la génération du bilan : " + e.getMessage());
   e.printStackTrace();
   return null;
  }
 }

 /**
  * Génère le contenu HTML du bilan
  */
 private void genererContenuHTML(StringBuilder html, String titre, Map<String, Object> donnees,
   LocalDate dateDebut, LocalDate dateFin) {
  html.append("<!DOCTYPE html>\n");
  html.append("<html lang='fr'>\n");
  html.append("<head>\n");
  html.append("    <meta charset='UTF-8'>\n");
  html.append("    <meta name='viewport' content='width=device-width, initial-scale=1.0'>\n");
  html.append("    <title>").append(titre).append("</title>\n");
  html.append("    <style>\n");
  html.append("        body { font-family: Arial, sans-serif; margin: 40px; color: #333; }\n");
  html.append("        .header { text-align: center; margin-bottom: 30px; }\n");
  html.append("        .title { font-size: 28px; font-weight: bold; color: #2980b9; margin-bottom: 20px; }\n");
  html.append("        .info { margin-bottom: 20px; }\n");
  html.append("        .periode-info { background-color: #f8f9fa; padding: 15px; border-left: 4px solid #2980b9; }\n");
  html.append("        .resume { background-color: #e8f4fd; padding: 20px; border-radius: 5px; margin: 20px 0; }\n");
  html.append("        .resume h3 { color: #2980b9; margin-top: 0; }\n");
  html.append("        .resume-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 20px; margin-top: 15px; }\n");
  html.append("        .resume-item { text-align: center; }\n");
  html.append("        .resume-value { font-size: 24px; font-weight: bold; color: #2980b9; }\n");
  html.append("        .resume-label { font-size: 14px; color: #666; margin-top: 5px; }\n");
  html.append("        table { width: 100%; border-collapse: collapse; margin: 20px 0; }\n");
  html.append("        th, td { border: 1px solid #ddd; padding: 12px; text-align: left; }\n");
  html.append("        th { background-color: #2980b9; color: white; font-weight: bold; }\n");
  html.append("        tr:nth-child(even) { background-color: #f2f2f2; }\n");
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
  html.append("        <h1 class='title'>").append(titre).append("</h1>\n");
  html.append("    </div>\n");

  // Informations de période
  html.append("    <div class='periode-info'>\n");
  html.append("        <strong>Période analysée :</strong> Du ").append(dateDebut.format(formatDate))
    .append(" au ").append(dateFin.format(formatDate)).append("<br>\n");
  html.append("        <strong>Date de génération :</strong> ").append(LocalDate.now().format(formatDate)).append("\n");
  html.append("    </div>\n");

  // Résumé financier
  double chiffreAffaires = (Double) donnees.get("chiffreAffaires");
  int nombreFactures = (Integer) donnees.get("nombreFactures");

  html.append("    <div class='resume'>\n");
  html.append("        <h3>RÉSUMÉ FINANCIER</h3>\n");
  html.append("        <div class='resume-grid'>\n");
  html.append("            <div class='resume-item'>\n");
  html.append("                <div class='resume-value'>").append(String.format("%.2f €", chiffreAffaires))
    .append("</div>\n");
  html.append("                <div class='resume-label'>Chiffre d'affaires total</div>\n");
  html.append("            </div>\n");
  html.append("            <div class='resume-item'>\n");
  html.append("                <div class='resume-value'>").append(nombreFactures).append("</div>\n");
  html.append("                <div class='resume-label'>Nombre de factures</div>\n");
  html.append("            </div>\n");
  html.append("        </div>\n");
  html.append("    </div>\n");

  // Détail des prestations
  html.append("    <h3>DÉTAIL DES PRESTATIONS</h3>\n");
  html.append("    <table>\n");
  html.append("        <thead>\n");
  html.append("            <tr>\n");
  html.append("                <th>Date</th>\n");
  html.append("                <th>Entreprise</th>\n");
  html.append("                <th>Type</th>\n");
  html.append("                <th>Détails</th>\n");
  html.append("                <th>Montant (€)</th>\n");
  html.append("            </tr>\n");
  html.append("        </thead>\n");
  html.append("        <tbody>\n");

  @SuppressWarnings("unchecked")
  List<Map<String, Object>> prestations = (List<Map<String, Object>>) donnees.get("prestations");

  if (!prestations.isEmpty()) {
   for (Map<String, Object> prestation : prestations) {
    html.append("            <tr>\n");
    html.append("                <td>").append(((LocalDate) prestation.get("date")).format(formatDate))
      .append("</td>\n");
    html.append("                <td>").append((String) prestation.get("entreprise")).append("</td>\n");
    html.append("                <td>").append((String) prestation.get("type")).append("</td>\n");

    String details = "";
    if ("Formation".equals(prestation.get("type"))) {
     details = prestation.get("titre") + " - " + prestation.get("classe");
    } else {
     details = (String) prestation.get("description");
    }
    html.append("                <td>").append(details).append("</td>\n");

    html.append("                <td>").append(String.format("%.2f", (Double) prestation.get("montant")))
      .append("</td>\n");
    html.append("            </tr>\n");
   }
  } else {
   html.append("            <tr>\n");
   html.append(
     "                <td colspan='5' style='text-align: center; font-style: italic;'>Aucune prestation trouvée pour cette période</td>\n");
   html.append("            </tr>\n");
  }

  html.append("        </tbody>\n");
  html.append("    </table>\n");

  // Pied de page
  html.append("    <div class='footer'>\n");
  html.append("        <p>Rapport généré automatiquement par le système de gestion des factures</p>\n");
  html.append("        <p>Micro Entrepreneur - ").append(LocalDate.now().format(formatDate)).append("</p>\n");
  html.append("    </div>\n");

  html.append("</body>\n");
  html.append("</html>\n");
 }

 /**
  * Ouvre le fichier HTML généré
  */
 public void ouvrirBilan(String cheminFichier) {
  try {
   File fichier = new File(cheminFichier);
   if (fichier.exists() && Desktop.isDesktopSupported()) {
    Desktop.getDesktop().open(fichier);
   } else {
    System.out.println("[INFO] Fichier non trouvé ou ouverture automatique non supportée : " + cheminFichier);
   }
  } catch (Exception e) {
   System.err.println("[ERREUR] Erreur lors de l'ouverture du fichier : " + e.getMessage());
  }
 }
}