package src;

import src.gui.MainWindow;
import javax.swing.*;

/**
 * Point d'entrée pour l'interface graphique de l'application
 * Préserve la version console existante dans Main.java
 */
public class MainGUI {

 public static void main(String[] args) {
  // Configuration pour un affichage optimal
  System.setProperty("sun.java2d.d3d", "false");
  System.setProperty("sun.java2d.ddoffscreen", "false");

  // Utiliser le Look and Feel par défaut (Metal) qui est plus fiable pour
  // l'édition
  try {
   UIManager.setLookAndFeel(UIManager.getCrossPlatformLookAndFeelClassName());
   System.out.println("[OK] Look and Feel Metal (par défaut) configuré");
  } catch (Exception e) {
   System.err.println("[ATTENTION] Impossible de configurer le Look and Feel : " + e.getMessage());
  }

  // Lancer l'interface graphique dans l'EDT (Event Dispatch Thread)
  SwingUtilities.invokeLater(() -> {
   try {
    MainWindow mainWindow = new MainWindow();
    mainWindow.setVisible(true);

    System.out.println("Interface graphique lancée avec succès !");
    System.out.println("Version console toujours disponible via Main.java");

   } catch (Exception e) {
    e.printStackTrace();
    JOptionPane.showMessageDialog(null,
      "Erreur lors du lancement de l'interface graphique :\n" + e.getMessage(),
      "Erreur",
      JOptionPane.ERROR_MESSAGE);
    System.exit(1);
   }
  });
 }
}