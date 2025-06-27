package src;

import java.time.LocalDate;

/**
 * Classe représentant une consultation
 * Facturée à la journée avec un TJM (Taux Journalier Moyen)
 */
public class Consultation extends Prestation {

 private String description; // Description de la consultation
 private double tjm; // Taux Journalier Moyen en euros

 /**
  * Constructeur pour une consultation
  */
 public Consultation(LocalDate date, String description, double tjm, String entreprise) {
  super(date, entreprise);
  this.description = description;
  this.tjm = tjm;
 }

 /**
  * Calculer le montant de la consultation
  * Pour une consultation d'une journée, le montant = TJM
  */
 @Override
 public double calculerMontant() {
  return tjm;
 }

 /**
  * Afficher les détails de la consultation
  */
 @Override
 public void afficher() {
  System.out.println("Type : Consultation");
  System.out.println("Date : " + getDateFormatee());
  System.out.println("Description : " + description);
  System.out.println("Entreprise : " + entreprise);
  System.out.println("TJM : " + tjm + " €");
  System.out.println("Montant total : " + calculerMontant() + " €");
 }

 @Override
 public String getType() {
  return "Consultation";
 }

 // Getters spécifiques à la consultation
 public String getDescription() {
  return description;
 }

 public double getTjm() {
  return tjm;
 }
}