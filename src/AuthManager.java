import java.sql.*;
import java.util.Scanner;

/**
 * Gestionnaire d'authentification pour l'application
 * Partie 1.2 : S'authentifier pour utiliser l'application
 * Version base de données MySQL/WAMP
 */
public class AuthManager {

 private DatabaseManager dbManager;

 public AuthManager() {
  this.dbManager = DatabaseManager.getInstance();
 }

 /**
  * Méthode pour se connecter à l'application
  * 
  * @param scanner Scanner pour saisir les données
  * @return true si la connexion est réussie, false sinon
  */
 public boolean seConnecter(Scanner scanner) {
  System.out.println("\n=== AUTHENTIFICATION ===");
  System.out.println("Connexion à la base de données...");

  // Vérifier la connexion à la base de données
  if (!dbManager.testerConnexion()) {
   System.out.println("[ERREUR] Impossible de se connecter à la base de données !");
   System.out.println("[INFO] Vérifiez que WAMP est démarré et que la base 'bank_facture' existe");
   return false;
  }

  System.out.println("[OK] Connexion à la base de données OK");
  System.out.println("Veuillez vous identifier pour accéder à l'application");

  // Donner 3 tentatives à l'utilisateur
  int tentatives = 3;

  while (tentatives > 0) {
   System.out.print("Login : ");
   String login = scanner.nextLine().trim();

   System.out.print("Mot de passe : ");
   String motDePasse = scanner.nextLine().trim();

   // Vérifier les identifiants dans la base de données
   if (verifierIdentifiants(login, motDePasse)) {
    return true; // Connexion réussie
   } else {
    tentatives--;
    if (tentatives > 0) {
     System.out.println("[ERREUR] Identifiants incorrects ! Il vous reste " + tentatives + " tentative(s).");
    } else {
     System.out.println("[ERREUR] Trop de tentatives échouées !");
    }
   }
  }

  return false; // Connexion échouée
 }

 /**
  * Vérifier si les identifiants sont corrects dans la base de données
  * 
  * @param login      Login saisi
  * @param motDePasse Mot de passe saisi
  * @return true si les identifiants sont corrects
  */
 private boolean verifierIdentifiants(String login, String motDePasse) {
  Connection conn = dbManager.getConnection();
  if (conn == null) {
   return false;
  }

  try {
   // Requête pour vérifier les identifiants
   String sql = "SELECT id, nom FROM utilisateurs WHERE login = ? AND mot_de_passe = ?";
   PreparedStatement stmt = conn.prepareStatement(sql);
   stmt.setString(1, login);
   stmt.setString(2, motDePasse);

   ResultSet rs = stmt.executeQuery();

   if (rs.next()) {
    // Utilisateur trouvé
    String nom = rs.getString("nom");
    System.out.println("Bonjour " + nom + " !");
    rs.close();
    stmt.close();
    return true;
   } else {
    // Aucun utilisateur trouvé
    rs.close();
    stmt.close();
    return false;
   }

  } catch (SQLException e) {
   System.err.println("[ERREUR] Erreur lors de la vérification des identifiants : " + e.getMessage());
   return false;
  }
 }

 /**
  * Afficher les identifiants de démonstration depuis la base de données
  */
 public static void afficherIdentifiantsDemo() {
  System.out.println("\n[INFO] IDENTIFIANTS DE DÉMONSTRATION :");
  System.out.println("Login : entrepreneur");
  System.out.println("Mot de passe : facture2024");
  System.out.println("(Ces identifiants sont stockés en base de données)");
 }
}