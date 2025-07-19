package src.gui;

import src.*;
import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Fenetre principale de l'application de gestion des factures
 * Interface graphique moderne pour remplacer le terminal
 */
public class MainWindow extends JFrame {

  private AuthManager authManager;
  private PrestationManager prestationManager;
  private FactureManager factureManager;
  private ReportingManager reportingManager;
  private JPanel currentPanel;
  private JLabel statusLabel;
  private boolean isAuthenticated = false;

  // Couleurs du theme moderne
  private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
  private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
  private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
  private static final Color ERROR_COLOR = new Color(231, 76, 60);
  private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);

  public MainWindow() {
    initializeComponents();
    setupWindow();
    showLoginPanel();
  }

  private void initializeComponents() {
    authManager = new AuthManager();
    prestationManager = new PrestationManager();
    factureManager = new FactureManager();
    reportingManager = new ReportingManager();

    // Configuration de la fenêtre
    setTitle("Gestion des Factures - Micro Entrepreneur");
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    setLayout(new BorderLayout());

    // Barre de statut
    statusLabel = new JLabel("Pret");
    statusLabel.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    statusLabel.setOpaque(true);
    statusLabel.setBackground(BACKGROUND_COLOR);
    add(statusLabel, BorderLayout.SOUTH);

    // Panel principal
    currentPanel = new JPanel();
    add(currentPanel, BorderLayout.CENTER);
  }

  private void setupWindow() {
    setSize(800, 600);
    setLocationRelativeTo(null);
    setResizable(true);
  }

  private void showLoginPanel() {
    currentPanel.removeAll();
    currentPanel.setLayout(new GridBagLayout());
    currentPanel.setBackground(BACKGROUND_COLOR);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(10, 10, 10, 10);

    // Panel de connexion
    JPanel loginPanel = createStyledPanel();
    loginPanel.setLayout(new GridBagLayout());
    loginPanel.setBorder(BorderFactory.createTitledBorder(
        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
        "Authentification",
        0, 0, new Font("Arial", Font.BOLD, 16), PRIMARY_COLOR));

    GridBagConstraints loginGbc = new GridBagConstraints();
    loginGbc.insets = new Insets(5, 5, 5, 5);

    // Titre
    JLabel titleLabel = new JLabel("Gestion des Factures");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
    titleLabel.setForeground(PRIMARY_COLOR);
    loginGbc.gridx = 0;
    loginGbc.gridy = 0;
    loginGbc.gridwidth = 2;
    loginGbc.anchor = GridBagConstraints.CENTER;
    loginPanel.add(titleLabel, loginGbc);

    // Informations de demonstration
    JLabel demoLabel = new JLabel(
        "<html><center>Identifiants de demonstration:<br/>Login: entrepreneur<br/>Mot de passe: facture2024</center></html>");
    demoLabel.setFont(new Font("Arial", Font.ITALIC, 12));
    demoLabel.setForeground(SECONDARY_COLOR);
    loginGbc.gridy = 1;
    loginGbc.insets = new Insets(5, 5, 15, 5);
    loginPanel.add(demoLabel, loginGbc);

    // Champs de saisie
    loginGbc.gridwidth = 1;
    loginGbc.anchor = GridBagConstraints.EAST;
    loginGbc.insets = new Insets(5, 5, 5, 5);

    JLabel loginLabel = new JLabel("Login :");
    loginLabel.setFont(new Font("Arial", Font.BOLD, 14));
    loginGbc.gridx = 0;
    loginGbc.gridy = 2;
    loginPanel.add(loginLabel, loginGbc);

    JTextField loginField = createStyledTextField();
    loginGbc.gridx = 1;
    loginGbc.anchor = GridBagConstraints.WEST;
    loginPanel.add(loginField, loginGbc);

    JLabel passwordLabel = new JLabel("Mot de passe :");
    passwordLabel.setFont(new Font("Arial", Font.BOLD, 14));
    loginGbc.gridx = 0;
    loginGbc.gridy = 3;
    loginGbc.anchor = GridBagConstraints.EAST;
    loginPanel.add(passwordLabel, loginGbc);

    JPasswordField passwordField = createStyledPasswordField();
    loginGbc.gridx = 1;
    loginGbc.anchor = GridBagConstraints.WEST;
    loginPanel.add(passwordField, loginGbc);

    // Bouton de connexion
    JButton loginButton = createStyledButton("Se connecter", PRIMARY_COLOR);
    loginGbc.gridx = 0;
    loginGbc.gridy = 4;
    loginGbc.gridwidth = 2;
    loginGbc.anchor = GridBagConstraints.CENTER;
    loginGbc.insets = new Insets(15, 5, 5, 5);
    loginPanel.add(loginButton, loginGbc);

    // Action du bouton de connexion
    loginButton.addActionListener(e -> {
      String login = loginField.getText().trim();
      String password = new String(passwordField.getPassword());

      if (login.isEmpty() || password.isEmpty()) {
        showStatus("Veuillez saisir le login et le mot de passe", ERROR_COLOR);
        return;
      }

      // Simulation de l'authentification
      if (authenticateUser(login, password)) {
        isAuthenticated = true;
        showStatus("Connexion reussie !", SUCCESS_COLOR);
        showMainMenu();
      } else {
        showStatus("Identifiants incorrects", ERROR_COLOR);
        passwordField.setText("");
      }
    });

    // Ajouter le panel de connexion au centre
    gbc.anchor = GridBagConstraints.CENTER;
    currentPanel.add(loginPanel, gbc);

    currentPanel.revalidate();
    currentPanel.repaint();
  }

  private void showMainMenu() {
    currentPanel.removeAll();
    currentPanel.setLayout(new BorderLayout());
    currentPanel.setBackground(BACKGROUND_COLOR);

    // Header
    JPanel headerPanel = new JPanel(new FlowLayout());
    headerPanel.setBackground(PRIMARY_COLOR);
    JLabel headerLabel = new JLabel("Menu Principal - Gestion des Prestations");
    headerLabel.setFont(new Font("Arial", Font.BOLD, 18));
    headerLabel.setForeground(Color.WHITE);
    headerPanel.add(headerLabel);
    currentPanel.add(headerPanel, BorderLayout.NORTH);

    // Panel central avec les boutons
    JPanel menuPanel = new JPanel(new GridBagLayout());
    menuPanel.setBackground(BACKGROUND_COLOR);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(15, 15, 15, 15);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    // Boutons du menu
    JButton newPrestationBtn = createMenuButton("Nouvelle Prestation", "Ajouter une formation ou consultation");
    JButton viewPrestationsBtn = createMenuButton("Voir les Prestations", "Afficher toutes les prestations");
    JButton generateInvoicesBtn = createMenuButton("Generer Factures", "Generer les factures mensuelles par client");
    JButton reportingBtn = createMenuButton("Reporting Financier", "Generer des bilans de chiffre d'affaires");
    JButton logoutBtn = createMenuButton("Deconnexion", "Se deconnecter de l'application");

    gbc.gridx = 0;
    gbc.gridy = 0;
    menuPanel.add(newPrestationBtn, gbc);

    gbc.gridy = 1;
    menuPanel.add(viewPrestationsBtn, gbc);

    gbc.gridy = 2;
    menuPanel.add(generateInvoicesBtn, gbc);

    gbc.gridy = 3;
    menuPanel.add(reportingBtn, gbc);

    gbc.gridy = 4;
    menuPanel.add(logoutBtn, gbc);

    // Actions des boutons
    newPrestationBtn.addActionListener(e -> showPrestationTypeSelection());
    viewPrestationsBtn.addActionListener(e -> showPrestationsList());
    generateInvoicesBtn.addActionListener(e -> showFactureGenerationDialog());
    reportingBtn.addActionListener(e -> showReportingMenu());
    logoutBtn.addActionListener(e -> {
      isAuthenticated = false;
      showLoginPanel();
      showStatus("Deconnecte", SUCCESS_COLOR);
    });

    currentPanel.add(menuPanel, BorderLayout.CENTER);
    currentPanel.revalidate();
    currentPanel.repaint();
  }

  private void showPrestationTypeSelection() {
    currentPanel.removeAll();
    currentPanel.setLayout(new BorderLayout());
    currentPanel.setBackground(BACKGROUND_COLOR);

    // Header avec bouton retour
    JPanel headerPanel = createHeaderWithBackButton("Nouvelle Prestation");
    currentPanel.add(headerPanel, BorderLayout.NORTH);

    // Panel central
    JPanel selectionPanel = new JPanel(new GridBagLayout());
    selectionPanel.setBackground(BACKGROUND_COLOR);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(20, 20, 20, 20);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel questionLabel = new JLabel("Quel type de prestation voulez-vous ajouter ?");
    questionLabel.setFont(new Font("Arial", Font.BOLD, 16));
    questionLabel.setForeground(SECONDARY_COLOR);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    selectionPanel.add(questionLabel, gbc);

    // Boutons de sélection
    JButton formationBtn = createLargeButton("Formation", "Facturee a l'heure avec horaires");
    JButton consultationBtn = createLargeButton("Consultation", "Facturee a la journee avec TJM");

    gbc.gridwidth = 1;
    gbc.gridy = 1;
    gbc.gridx = 0;
    selectionPanel.add(formationBtn, gbc);

    gbc.gridx = 1;
    selectionPanel.add(consultationBtn, gbc);

    // Actions
    formationBtn.addActionListener(e -> showFormationForm());
    consultationBtn.addActionListener(e -> showConsultationForm());

    currentPanel.add(selectionPanel, BorderLayout.CENTER);
    currentPanel.revalidate();
    currentPanel.repaint();
  }

  private void showPrestationsList() {
    currentPanel.removeAll();
    currentPanel.setLayout(new BorderLayout());
    currentPanel.setBackground(BACKGROUND_COLOR);

    // Header avec bouton retour
    JPanel headerPanel = createHeaderWithBackButton("Liste des Prestations");
    currentPanel.add(headerPanel, BorderLayout.NORTH);

    // Récupérer les prestations
    List<Prestation> prestations = prestationManager.getPrestations();

    if (prestations.isEmpty()) {
      JPanel emptyPanel = new JPanel(new GridBagLayout());
      emptyPanel.setBackground(BACKGROUND_COLOR);
      JLabel emptyLabel = new JLabel("Aucune prestation trouvee");
      emptyLabel.setFont(new Font("Arial", Font.ITALIC, 16));
      emptyLabel.setForeground(SECONDARY_COLOR);
      emptyPanel.add(emptyLabel);
      currentPanel.add(emptyPanel, BorderLayout.CENTER);
    } else {
      // Tableau des prestations
      String[] columnNames = { "Type", "Date", "Entreprise", "Description", "Montant" };
      Object[][] data = new Object[prestations.size()][5];

      double totalMontant = 0;
      for (int i = 0; i < prestations.size(); i++) {
        Prestation p = prestations.get(i);
        data[i][0] = p.getType();
        data[i][1] = p.getDateFormatee();
        data[i][2] = p.getEntreprise();

        if (p instanceof Formation) {
          Formation f = (Formation) p;
          data[i][3] = f.getTitre() + " (" + f.calculerDureeHeures() + "h)";
        } else if (p instanceof Consultation) {
          Consultation c = (Consultation) p;
          data[i][3] = c.getDescription();
        }

        data[i][4] = String.format("%.2f €", p.calculerMontant());
        totalMontant += p.calculerMontant();
      }

      JTable table = new JTable(data, columnNames);
      table.setFont(new Font("Arial", Font.PLAIN, 12));
      table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
      table.getTableHeader().setBackground(PRIMARY_COLOR);
      table.getTableHeader().setForeground(Color.WHITE);
      table.setRowHeight(25);

      JScrollPane scrollPane = new JScrollPane(table);
      currentPanel.add(scrollPane, BorderLayout.CENTER);

      // Panel du total
      JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
      totalPanel.setBackground(BACKGROUND_COLOR);
      JLabel totalLabel = new JLabel("Total general : " + String.format("%.2f €", totalMontant));
      totalLabel.setFont(new Font("Arial", Font.BOLD, 16));
      totalLabel.setForeground(SUCCESS_COLOR);
      totalPanel.add(totalLabel);
      currentPanel.add(totalPanel, BorderLayout.SOUTH);
    }

    currentPanel.revalidate();
    currentPanel.repaint();
  }

  private void showStatistics() {
    JOptionPane.showMessageDialog(this, "Fonctionnalite statistiques a implementer !", "Information",
        JOptionPane.INFORMATION_MESSAGE);
  }

  private void showFactureGenerationDialog() {
    FactureFormDialog dialog = new FactureFormDialog(this);
    dialog.setVisible(true);
  }

  private void showReportingMenu() {
    currentPanel.removeAll();
    currentPanel.setLayout(new BorderLayout());
    currentPanel.setBackground(BACKGROUND_COLOR);

    // Header avec bouton retour
    JPanel headerPanel = createHeaderWithBackButton("Reporting Financier");
    currentPanel.add(headerPanel, BorderLayout.NORTH);

    // Panel central
    JPanel reportingPanel = new JPanel(new GridBagLayout());
    reportingPanel.setBackground(BACKGROUND_COLOR);
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(20, 20, 20, 20);
    gbc.fill = GridBagConstraints.HORIZONTAL;

    JLabel titleLabel = new JLabel("Choisissez le type de bilan à générer :");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setForeground(SECONDARY_COLOR);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    reportingPanel.add(titleLabel, gbc);

    // Boutons pour les différents types de bilans
    JButton bilanMensuelBtn = createLargeButton("Bilan Mensuel", "Générer un rapport pour un mois spécifique");
    JButton bilanAnnuelBtn = createLargeButton("Bilan Annuel", "Générer un rapport pour une année complète");
    JButton bilanPeriodeBtn = createLargeButton("Bilan Période", "Générer un rapport sur une période personnalisée");

    gbc.gridwidth = 1;
    gbc.gridy = 1;
    gbc.gridx = 0;
    reportingPanel.add(bilanMensuelBtn, gbc);

    gbc.gridx = 1;
    reportingPanel.add(bilanAnnuelBtn, gbc);

    gbc.gridx = 0;
    gbc.gridy = 2;
    gbc.gridwidth = 2;
    reportingPanel.add(bilanPeriodeBtn, gbc);

    // Actions des boutons
    bilanMensuelBtn.addActionListener(e -> showBilanMensuelDialog());
    bilanAnnuelBtn.addActionListener(e -> showBilanAnnuelDialog());
    bilanPeriodeBtn.addActionListener(e -> showBilanPeriodeDialog());

    currentPanel.add(reportingPanel, BorderLayout.CENTER);
    currentPanel.revalidate();
    currentPanel.repaint();
  }

  private void showBilanMensuelDialog() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);

    JLabel anneeLabel = new JLabel("Année :");
    JTextField anneeField = new JTextField(10);
    anneeField.setText(String.valueOf(LocalDate.now().getYear()));

    JLabel moisLabel = new JLabel("Mois :");
    String[] moisOptions = { "Janvier", "Février", "Mars", "Avril", "Mai", "Juin",
        "Juillet", "Août", "Septembre", "Octobre", "Novembre", "Décembre" };
    JComboBox<String> moisCombo = new JComboBox<>(moisOptions);
    moisCombo.setSelectedIndex(LocalDate.now().getMonthValue() - 1);

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(anneeLabel, gbc);
    gbc.gridx = 1;
    panel.add(anneeField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(moisLabel, gbc);
    gbc.gridx = 1;
    panel.add(moisCombo, gbc);

    int result = JOptionPane.showConfirmDialog(this, panel, "Bilan Mensuel",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      try {
        int annee = Integer.parseInt(anneeField.getText().trim());
        int mois = moisCombo.getSelectedIndex() + 1;

        if (annee < 1900 || annee > 2100) {
          showStatus("Année invalide !", ERROR_COLOR);
          return;
        }

        showStatus("Génération du bilan mensuel en cours...", PRIMARY_COLOR);
        String cheminFichier = reportingManager.genererBilanMensuel(annee, mois);

        if (cheminFichier != null) {
          showStatus("Bilan mensuel généré avec succès !", SUCCESS_COLOR);
          int openResult = JOptionPane.showConfirmDialog(this,
              "Bilan généré : " + cheminFichier + "\nVoulez-vous l'ouvrir ?",
              "Succès", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

          if (openResult == JOptionPane.YES_OPTION) {
            reportingManager.ouvrirBilan(cheminFichier);
          }
        } else {
          showStatus("Erreur lors de la génération du bilan", ERROR_COLOR);
        }
      } catch (NumberFormatException ex) {
        showStatus("Année invalide !", ERROR_COLOR);
      }
    }
  }

  private void showBilanAnnuelDialog() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);

    JLabel anneeLabel = new JLabel("Année :");
    JTextField anneeField = new JTextField(10);
    anneeField.setText(String.valueOf(LocalDate.now().getYear()));

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(anneeLabel, gbc);
    gbc.gridx = 1;
    panel.add(anneeField, gbc);

    int result = JOptionPane.showConfirmDialog(this, panel, "Bilan Annuel",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      try {
        int annee = Integer.parseInt(anneeField.getText().trim());

        if (annee < 1900 || annee > 2100) {
          showStatus("Année invalide !", ERROR_COLOR);
          return;
        }

        showStatus("Génération du bilan annuel en cours...", PRIMARY_COLOR);
        String cheminFichier = reportingManager.genererBilanAnnuel(annee);

        if (cheminFichier != null) {
          showStatus("Bilan annuel généré avec succès !", SUCCESS_COLOR);
          int openResult = JOptionPane.showConfirmDialog(this,
              "Bilan généré : " + cheminFichier + "\nVoulez-vous l'ouvrir ?",
              "Succès", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

          if (openResult == JOptionPane.YES_OPTION) {
            reportingManager.ouvrirBilan(cheminFichier);
          }
        } else {
          showStatus("Erreur lors de la génération du bilan", ERROR_COLOR);
        }
      } catch (NumberFormatException ex) {
        showStatus("Année invalide !", ERROR_COLOR);
      }
    }
  }

  private void showBilanPeriodeDialog() {
    JPanel panel = new JPanel(new GridBagLayout());
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);

    JLabel debutLabel = new JLabel("Date de début (dd/MM/yyyy) :");
    JTextField debutField = new JTextField(10);
    debutField.setText("01/01/" + LocalDate.now().getYear());

    JLabel finLabel = new JLabel("Date de fin (dd/MM/yyyy) :");
    JTextField finField = new JTextField(10);
    finField.setText("31/12/" + LocalDate.now().getYear());

    gbc.gridx = 0;
    gbc.gridy = 0;
    panel.add(debutLabel, gbc);
    gbc.gridx = 1;
    panel.add(debutField, gbc);

    gbc.gridx = 0;
    gbc.gridy = 1;
    panel.add(finLabel, gbc);
    gbc.gridx = 1;
    panel.add(finField, gbc);

    int result = JOptionPane.showConfirmDialog(this, panel, "Bilan sur Période",
        JOptionPane.OK_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

    if (result == JOptionPane.OK_OPTION) {
      try {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        LocalDate dateDebut = LocalDate.parse(debutField.getText().trim(), formatter);
        LocalDate dateFin = LocalDate.parse(finField.getText().trim(), formatter);

        if (dateDebut.isAfter(dateFin)) {
          showStatus("La date de début doit être antérieure à la date de fin !", ERROR_COLOR);
          return;
        }

        showStatus("Génération du bilan de période en cours...", PRIMARY_COLOR);
        String cheminFichier = reportingManager.genererBilanPeriode(dateDebut, dateFin);

        if (cheminFichier != null) {
          showStatus("Bilan de période généré avec succès !", SUCCESS_COLOR);
          int openResult = JOptionPane.showConfirmDialog(this,
              "Bilan généré : " + cheminFichier + "\nVoulez-vous l'ouvrir ?",
              "Succès", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);

          if (openResult == JOptionPane.YES_OPTION) {
            reportingManager.ouvrirBilan(cheminFichier);
          }
        } else {
          showStatus("Erreur lors de la génération du bilan", ERROR_COLOR);
        }
      } catch (Exception ex) {
        showStatus("Format de date invalide ! Utilisez dd/MM/yyyy", ERROR_COLOR);
      }
    }
  }

  private void showFormationForm() {
    FormationFormDialog dialog = new FormationFormDialog(this, prestationManager);
    dialog.setVisible(true);

    if (dialog.isFormationCreated()) {
      showStatus("Formation ajoutee avec succes !", SUCCESS_COLOR);
      showMainMenu();
    }
  }

  private void showConsultationForm() {
    ConsultationFormDialog dialog = new ConsultationFormDialog(this, prestationManager);
    dialog.setVisible(true);

    if (dialog.isConsultationCreated()) {
      showStatus("Consultation ajoutee avec succes !", SUCCESS_COLOR);
      showMainMenu();
    }
  }

  // Methodes utilitaires pour creer des composants styles
  private JPanel createStyledPanel() {
    JPanel panel = new JPanel();
    panel.setBackground(Color.WHITE);
    panel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    return panel;
  }

  private JTextField createStyledTextField() {
    JTextField field = new JTextField(15);
    field.setFont(new Font("Arial", Font.PLAIN, 14));
    field.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.GRAY),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    field.setEditable(true);
    field.setEnabled(true);
    field.setFocusable(true);
    return field;
  }

  private JPasswordField createStyledPasswordField() {
    JPasswordField field = new JPasswordField(15);
    field.setFont(new Font("Arial", Font.PLAIN, 14));
    field.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.GRAY),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));
    field.setEditable(true);
    field.setEnabled(true);
    field.setFocusable(true);
    return field;
  }

  private JButton createStyledButton(String text, Color bgColor) {
    JButton button = new JButton(text);
    button.setFont(new Font("Arial", Font.BOLD, 14));
    button.setBackground(bgColor);
    button.setForeground(Color.WHITE);
    button.setBorder(BorderFactory.createEmptyBorder(10, 20, 10, 20));
    button.setFocusPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Forcer l'opacité pour éviter les problèmes d'affichage
    button.setOpaque(true);
    button.setContentAreaFilled(true);
    button.setBorderPainted(true);

    // Effet hover
    button.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        button.setBackground(bgColor.darker());
        button.repaint();
      }

      public void mouseExited(java.awt.event.MouseEvent evt) {
        button.setBackground(bgColor);
        button.repaint();
      }
    });

    return button;
  }

  private JButton createMenuButton(String title, String description) {
    JButton button = new JButton(
        "<html><center><b>" + title + "</b><br/><small>" + description + "</small></center></html>");
    button.setFont(new Font("Arial", Font.PLAIN, 14));
    button.setBackground(Color.WHITE);
    button.setForeground(SECONDARY_COLOR);
    button.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(PRIMARY_COLOR, 2),
        BorderFactory.createEmptyBorder(15, 20, 15, 20)));
    button.setPreferredSize(new Dimension(300, 80));
    button.setFocusPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Forcer l'opacité pour éviter les problèmes d'affichage
    button.setOpaque(true);
    button.setContentAreaFilled(true);
    button.setBorderPainted(true);

    // Effet hover
    button.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        button.setBackground(BACKGROUND_COLOR);
        button.repaint();
      }

      public void mouseExited(java.awt.event.MouseEvent evt) {
        button.setBackground(Color.WHITE);
        button.repaint();
      }
    });

    return button;
  }

  private JButton createLargeButton(String title, String description) {
    JButton button = new JButton(
        "<html><center><b style='font-size:16px'>" + title + "</b><br/><br/>" + description + "</center></html>");
    button.setFont(new Font("Arial", Font.PLAIN, 12));
    button.setBackground(PRIMARY_COLOR);
    button.setForeground(Color.WHITE);
    button.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    button.setPreferredSize(new Dimension(250, 120));
    button.setFocusPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Forcer l'opacité pour éviter les problèmes d'affichage
    button.setOpaque(true);
    button.setContentAreaFilled(true);
    button.setBorderPainted(true);

    // Effet hover
    button.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        button.setBackground(PRIMARY_COLOR.darker());
        button.repaint();
      }

      public void mouseExited(java.awt.event.MouseEvent evt) {
        button.setBackground(PRIMARY_COLOR);
        button.repaint();
      }
    });

    return button;
  }

  private JPanel createHeaderWithBackButton(String title) {
    JPanel headerPanel = new JPanel(new BorderLayout());
    headerPanel.setBackground(PRIMARY_COLOR);
    headerPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

    JButton backButton = new JButton("< Retour");
    backButton.setBackground(SECONDARY_COLOR);
    backButton.setForeground(Color.WHITE);
    backButton.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));
    backButton.setFocusPainted(false);
    backButton.setCursor(new Cursor(Cursor.HAND_CURSOR));
    backButton.addActionListener(e -> showMainMenu());

    JLabel titleLabel = new JLabel(title);
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setForeground(Color.WHITE);
    titleLabel.setHorizontalAlignment(SwingConstants.CENTER);

    headerPanel.add(backButton, BorderLayout.WEST);
    headerPanel.add(titleLabel, BorderLayout.CENTER);

    return headerPanel;
  }

  private void showStatus(String message, Color color) {
    statusLabel.setText(message);
    statusLabel.setForeground(color);

    // Effacer le message apres 3 secondes
    Timer timer = new Timer(3000, e -> {
      statusLabel.setText("Pret");
      statusLabel.setForeground(Color.BLACK);
    });
    timer.setRepeats(false);
    timer.start();
  }

  private boolean authenticateUser(String login, String password) {
    // Implementation simplifiee - utilise les identifiants de demonstration
    return "entrepreneur".equals(login) && "facture2024".equals(password);
  }
}