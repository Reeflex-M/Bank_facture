package src.gui;

import src.FactureManager;
import src.Prestation;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.List;

/**
 * Dialogue pour la génération de factures mensuelles
 */
public class FactureFormDialog extends JDialog {

  private JComboBox<Integer> comboMois;
  private JComboBox<Integer> comboAnnee;
  private JComboBox<String> comboEntreprise;
  private JButton btnGenerer;
  private JButton btnAnnuler;
  private JButton btnApercu;
  private JTextArea textAreaResultat;
  private FactureManager factureManager;
  private int derniereAnnee = -1;
  private int dernierMois = -1;
  private String derniereEntreprise = null;

  // Couleurs du thème
  private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
  private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
  private static final Color ERROR_COLOR = new Color(231, 76, 60);
  private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);

  public FactureFormDialog(JFrame parent) {
    super(parent, "Generation de Factures Mensuelles", true);
    this.factureManager = new FactureManager();
    initializeComponents();
    setupDialog();
  }

  private void initializeComponents() {
    setLayout(new BorderLayout());

    // Panel principal
    JPanel mainPanel = new JPanel(new GridBagLayout());
    mainPanel.setBackground(BACKGROUND_COLOR);
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);

    // Titre
    JLabel titleLabel = new JLabel("Selectionner la periode pour generer les factures");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 16));
    titleLabel.setForeground(PRIMARY_COLOR);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    mainPanel.add(titleLabel, gbc);

    // Sélection du mois
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.EAST;
    JLabel labelMois = new JLabel("Mois :");
    labelMois.setFont(new Font("Arial", Font.BOLD, 14));
    gbc.gridx = 0;
    gbc.gridy = 1;
    mainPanel.add(labelMois, gbc);

    String[] nomsMois = { "Janvier", "Fevrier", "Mars", "Avril", "Mai", "Juin",
        "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Decembre" };
    comboMois = new JComboBox<>();
    for (int i = 0; i < nomsMois.length; i++) {
      comboMois.addItem(i + 1);
    }
    comboMois.setSelectedItem(LocalDate.now().getMonthValue());
    gbc.gridx = 1;
    gbc.anchor = GridBagConstraints.WEST;
    mainPanel.add(comboMois, gbc);

    // Sélection de l'année
    JLabel labelAnnee = new JLabel("Annee :");
    labelAnnee.setFont(new Font("Arial", Font.BOLD, 14));
    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.anchor = GridBagConstraints.EAST;
    mainPanel.add(labelAnnee, gbc);

    comboAnnee = new JComboBox<>();
    int anneeActuelle = LocalDate.now().getYear();
    for (int i = anneeActuelle - 5; i <= anneeActuelle + 1; i++) {
      comboAnnee.addItem(i);
    }
    comboAnnee.setSelectedItem(anneeActuelle);
    gbc.gridx = 1;
    gbc.anchor = GridBagConstraints.WEST;
    mainPanel.add(comboAnnee, gbc);

    // Sélection de l'entreprise
    JLabel labelEntreprise = new JLabel("Entreprise :");
    labelEntreprise.setFont(new Font("Arial", Font.BOLD, 14));
    gbc.gridx = 0;
    gbc.gridy = 3;
    gbc.anchor = GridBagConstraints.EAST;
    mainPanel.add(labelEntreprise, gbc);

    comboEntreprise = new JComboBox<>();
    chargerEntreprises();
    gbc.gridx = 1;
    gbc.anchor = GridBagConstraints.WEST;
    mainPanel.add(comboEntreprise, gbc);

    // Boutons
    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.setBackground(BACKGROUND_COLOR);

    btnGenerer = createStyledButton("Generer les Factures", SUCCESS_COLOR);
    btnApercu = createStyledButton("Aperçu Graphique", PRIMARY_COLOR);
    btnAnnuler = createStyledButton("Annuler", ERROR_COLOR);

    btnApercu.setEnabled(false); // Désactivé jusqu'à génération

    buttonPanel.add(btnGenerer);
    buttonPanel.add(btnApercu);
    buttonPanel.add(btnAnnuler);

    gbc.gridx = 0;
    gbc.gridy = 4;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    mainPanel.add(buttonPanel, gbc);

    // Zone de résultat
    textAreaResultat = new JTextArea(10, 50);
    textAreaResultat.setEditable(false);
    textAreaResultat.setFont(new Font("Monospaced", Font.PLAIN, 12));
    textAreaResultat.setBackground(Color.WHITE);
    textAreaResultat.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Résultats"),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

    JScrollPane scrollPane = new JScrollPane(textAreaResultat);
    gbc.gridy = 5;
    gbc.fill = GridBagConstraints.BOTH;
    gbc.weightx = 1.0;
    gbc.weighty = 1.0;
    mainPanel.add(scrollPane, gbc);

    add(mainPanel, BorderLayout.CENTER);

    // Actions des boutons
    btnGenerer.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        genererFactures();
      }
    });

    btnApercu.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        afficherApercuGraphique();
      }
    });

    btnAnnuler.addActionListener(new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        dispose();
      }
    });
  }

  private void setupDialog() {
    setSize(600, 500);
    setLocationRelativeTo(getParent());
    setResizable(true);
  }

  private JButton createStyledButton(String text, Color bgColor) {
    JButton button = new JButton(text);
    button.setFont(new Font("Arial", Font.BOLD, 12));
    button.setBackground(bgColor);
    button.setForeground(Color.WHITE);
    button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    button.setFocusPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Effet hover
    Color originalColor = bgColor;
    button.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        button.setBackground(originalColor.brighter());
      }

      public void mouseExited(java.awt.event.MouseEvent evt) {
        button.setBackground(originalColor);
      }
    });

    return button;
  }

  private void chargerEntreprises() {
    comboEntreprise.addItem("-- Toutes les entreprises --");
    List<String> entreprises = factureManager.getEntreprises();
    for (String entreprise : entreprises) {
      comboEntreprise.addItem(entreprise);
    }
  }

  private void genererFactures() {
    try {
      int mois = (Integer) comboMois.getSelectedItem();
      int annee = (Integer) comboAnnee.getSelectedItem();
      String entrepriseSelectionnee = (String) comboEntreprise.getSelectedItem();

      // Enregistrer les paramètres pour l'aperçu graphique
      dernierMois = mois;
      derniereAnnee = annee;
      derniereEntreprise = entrepriseSelectionnee;

      btnGenerer.setEnabled(false);
      btnGenerer.setText("Generation en cours...");
      textAreaResultat.setText("Generation des factures en cours...\n");

      // Créer un SwingWorker pour éviter de bloquer l'interface
      SwingWorker<Map<String, String>, String> worker = new SwingWorker<Map<String, String>, String>() {
        @Override
        protected Map<String, String> doInBackground() throws Exception {
          if ("-- Toutes les entreprises --".equals(entrepriseSelectionnee)) {
            return factureManager.genererFacturesMensuelles(annee, mois);
          } else {
            return factureManager.genererFactureEntreprise(annee, mois, entrepriseSelectionnee);
          }
        }

        @Override
        protected void process(java.util.List<String> chunks) {
          for (String message : chunks) {
            textAreaResultat.append(message + "\n");
          }
        }

        @Override
        protected void done() {
          try {
            Map<String, String> factures = get();

            if (factures.isEmpty()) {
              textAreaResultat.append("\nAucune prestation trouvée pour cette période.\n");
            } else {
              textAreaResultat.append(String.format("\n=== FACTURES GEERÉES (%d) ===\n", factures.size()));

              for (Map.Entry<String, String> entry : factures.entrySet()) {
                textAreaResultat.append(String.format("+ %s -> %s\n", entry.getKey(), entry.getValue()));
              }

              textAreaResultat.append("\nTous les fichiers ont ete generes dans le dossier 'factures/'\n");
              textAreaResultat
                  .append("\n>> Cliquez sur 'Aperçu Graphique' pour voir les factures dans l'application\n");

              // Activer le bouton d'aperçu graphique
              if (btnApercu != null) {
                btnApercu.setEnabled(true);
              }
            }

          } catch (Exception e) {
            textAreaResultat.append("\nErreur lors de la génération : " + e.getMessage() + "\n");
          } finally {
            btnGenerer.setEnabled(true);
            btnGenerer.setText("Generer les Factures");
          }
        }
      };

      worker.execute();

    } catch (Exception e) {
      textAreaResultat.append("Erreur : " + e.getMessage() + "\n");
      btnGenerer.setEnabled(true);
      btnGenerer.setText("Generer les Factures");
    }
  }

  /**
   * Affiche l'aperçu graphique des factures générées
   */
  private void afficherApercuGraphique() {
    if (derniereAnnee == -1 || dernierMois == -1) {
      JOptionPane.showMessageDialog(this,
          "Veuillez d'abord générer les factures.",
          "Aucune facture",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    try {
      // Récupérer les prestations par client pour la période
      Map<String, List<Prestation>> prestationsParClient = factureManager.getPrestationsParClient(derniereAnnee,
          dernierMois);

      if (prestationsParClient.isEmpty()) {
        JOptionPane.showMessageDialog(this,
            "Aucune prestation trouvée pour cette periode.",
            "Aucune donnée",
            JOptionPane.INFORMATION_MESSAGE);
        return;
      }

      // Si une entreprise spécifique a été sélectionnée lors de la génération
      if (derniereEntreprise != null && !"-- Toutes les entreprises --".equals(derniereEntreprise)) {
        if (prestationsParClient.containsKey(derniereEntreprise)) {
          afficherFactureEntreprise(derniereEntreprise, prestationsParClient.get(derniereEntreprise));
        } else {
          JOptionPane.showMessageDialog(this,
              "Aucune prestation trouvée pour l'entreprise " + derniereEntreprise + " sur cette période.",
              "Aucune donnée",
              JOptionPane.INFORMATION_MESSAGE);
        }
      } else {
        // Affichage pour toutes les entreprises - permettre de choisir
        if (prestationsParClient.size() > 1) {
          String[] entreprises = prestationsParClient.keySet().toArray(new String[0]);
          String entrepriseChoisie = (String) JOptionPane.showInputDialog(
              this,
              "Sélectionnez l'entreprise pour voir sa facture :",
              "Choisir une facture",
              JOptionPane.QUESTION_MESSAGE,
              null,
              entreprises,
              entreprises[0]);

          if (entrepriseChoisie != null) {
            afficherFactureEntreprise(entrepriseChoisie, prestationsParClient.get(entrepriseChoisie));
          }
        } else {
          // Une seule entreprise, l'afficher directement
          String entreprise = prestationsParClient.keySet().iterator().next();
          afficherFactureEntreprise(entreprise, prestationsParClient.get(entreprise));
        }
      }

    } catch (Exception e) {
      JOptionPane.showMessageDialog(this,
          "Erreur lors de l'affichage de l'aperçu : " + e.getMessage(),
          "Erreur",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Affiche la facture pour une entreprise spécifique
   */
  private void afficherFactureEntreprise(String entreprise, List<Prestation> prestations) {
    YearMonth periode = YearMonth.of(derniereAnnee, dernierMois);
    FactureViewerDialog viewer = new FactureViewerDialog(
        (javax.swing.JFrame) getParent(),
        entreprise,
        prestations,
        periode);
    viewer.setVisible(true);
  }
}