import java.sql.*;

/**
 * Gestionnaire de base de données pour l'application
 * Gère la connexion à MySQL via WAMP
 */
public class DatabaseManager {

 // Configuration de la base de données WAMP/MySQL
 private static final String URL = "jdbc:mysql://localhost:3306/bank_facture";
 private static final String USERNAME = "root"; // Utilisateur par défaut WAMP
 private static final String PASSWORD = ""; // Mot de passe vide par défaut WAMP

 // Instance unique (pattern Singleton)
 private static DatabaseManager instance;
 private Connection connection;

 /**
  * Constructeur privé pour le pattern Singleton
  */
 private DatabaseManager() {
  try {
   // Charger le driver MySQL
   Class.forName("com.mysql.cj.jdbc.Driver");
   System.out.println("[OK] Driver MySQL chargé avec succès");
  } catch (ClassNotFoundException e) {
   System.err.println("[ERREUR] Driver MySQL non trouvé : " + e.getMessage());
   System.err.println("[INFO] Ajoutez mysql-connector-java.jar dans le classpath");
  }
 }

 /**
  * Obtenir l'instance unique du gestionnaire de base de données
  */
 public static DatabaseManager getInstance() {
  if (instance == null) {
   instance = new DatabaseManager();
  }
  return instance;
 }

 /**
  * Établir la connexion à la base de données
  */
 public Connection getConnection() {
  try {
   // Vérifier si la connexion existe et est valide
   if (connection == null || connection.isClosed()) {
    connection = DriverManager.getConnection(URL, USERNAME, PASSWORD);
    System.out.println("[OK] Connexion à la base de données établie");
   }
   return connection;
  } catch (SQLException e) {
   System.err.println("[ERREUR] Erreur de connexion à la base de données : " + e.getMessage());
   System.err.println("[INFO] Vérifiez que WAMP est démarré et que la base 'bank_facture' existe");
   return null;
  }
 }

 /**
  * Fermer la connexion à la base de données
  */
 public void closeConnection() {
  try {
   if (connection != null && !connection.isClosed()) {
    connection.close();
    System.out.println("[OK] Connexion à la base de données fermée");
   }
  } catch (SQLException e) {
   System.err.println("[ERREUR] Erreur lors de la fermeture de la connexion : " + e.getMessage());
  }
 }

 /**
  * Tester la connexion à la base de données
  */
 public boolean testerConnexion() {
  Connection conn = getConnection();
  if (conn != null) {
   try {
    // Test simple avec une requête
    PreparedStatement stmt = conn.prepareStatement("SELECT 1");
    ResultSet rs = stmt.executeQuery();
    rs.close();
    stmt.close();
    return true;
   } catch (SQLException e) {
    System.err.println("[ERREUR] Test de connexion échoué : " + e.getMessage());
    return false;
   }
  }
  return false;
 }
}