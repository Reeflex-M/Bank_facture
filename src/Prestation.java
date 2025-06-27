package src;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * Classe abstraite représentant une prestation générale
 * Peut être une Formation ou une Consultation
 */
public abstract class Prestation {

 // Attributs communs à toutes les prestations
 protected LocalDate date;
 protected String entreprise;
 protected DateTimeFormatter formatDate = DateTimeFormatter.ofPattern("dd/MM/yyyy");

 /**
  * Constructeur de base pour une prestation
  */
 public Prestation(LocalDate date, String entreprise) {
  this.date = date;
  this.entreprise = entreprise;
 }

 /**
  * Méthode abstraite pour calculer le montant de la prestation
  * Chaque type de prestation a son propre calcul
  */
 public abstract double calculerMontant();

 /**
  * Méthode abstraite pour afficher les détails de la prestation
  */
 public abstract void afficher();

 /**
  * Obtenir le type de prestation (Formation ou Consultation)
  */
 public abstract String getType();

 // Getters
 public LocalDate getDate() {
  return date;
 }

 public String getEntreprise() {
  return entreprise;
 }

 /**
  * Obtenir la date formatée en string
  */
 public String getDateFormatee() {
  return date.format(formatDate);
 }
}