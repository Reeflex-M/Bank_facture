package src;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.Duration;

/**
 * Classe représentant une formation
 * Facturée à l'heure avec heure de début et fin
 */
public class Formation extends Prestation {

  private LocalTime heureDebut;
  private LocalTime heureFin;
  private String classe; // Classe ou groupe de formation
  private String titre; // Titre de la formation ou module
  private double tarifHoraire; // Tarif à l'heure en euros

  private DateTimeFormatter formatHeure = DateTimeFormatter.ofPattern("HH:mm");

  /**
   * Constructeur pour une formation
   */
  public Formation(LocalDate date, LocalTime heureDebut, LocalTime heureFin,
      String classe, String titre, String entreprise, double tarifHoraire) {
    super(date, entreprise);
    this.heureDebut = heureDebut;
    this.heureFin = heureFin;
    this.classe = classe;
    this.titre = titre;
    this.tarifHoraire = tarifHoraire;
  }

  /**
   * Calculer la durée de la formation en heures
   */
  public double calculerDureeHeures() {
    Duration duree = Duration.between(heureDebut, heureFin);
    return duree.toMinutes() / 60.0; // Convertir en heures avec décimales
  }

  /**
   * Calculer le montant de la formation
   * Montant = durée en heures × tarif horaire
   */
  @Override
  public double calculerMontant() {
    return calculerDureeHeures() * tarifHoraire;
  }

  /**
   * Afficher les détails de la formation
   */
  @Override
  public void afficher() {
    System.out.println("Type : Formation");
    System.out.println("Date : " + getDateFormatee());
    System.out.println("Horaires : " + heureDebut.format(formatHeure) + " à " + heureFin.format(formatHeure));
    System.out.println("Durée : " + calculerDureeHeures() + " heures");
    System.out.println("Classe/Groupe : " + classe);
    System.out.println("Titre/Module : " + titre);
    System.out.println("Entreprise : " + entreprise);
    System.out.println("Tarif horaire : " + tarifHoraire + " €/h");
    System.out.println("Montant total : " + calculerMontant() + " €");
  }

  @Override
  public String getType() {
    return "Formation";
  }

  // Getters spécifiques à la formation
  public LocalTime getHeureDebut() {
    return heureDebut;
  }

  public LocalTime getHeureFin() {
    return heureFin;
  }

  public String getClasse() {
    return classe;
  }

  public String getTitre() {
    return titre;
  }

  public double getTarifHoraire() {
    return tarifHoraire;
  }
}