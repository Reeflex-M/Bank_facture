package src.gui;

import src.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Dialogue pour la saisie d'une nouvelle formation
 */
public class FormationFormDialog extends JDialog {

  private PrestationManager prestationManager;
  private boolean formationCreated = false;

  // Composants du formulaire
  private JTextField dateField;
  private JTextField heureDebutField;
  private JTextField heureFinField;
  private JTextField classeField;
  private JTextField titreField;
  private JTextField entrepriseField;
  private JTextField tarifField;

  // Couleurs du theme
  private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
  private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
  private static final Color ERROR_COLOR = new Color(231, 76, 60);

  public FormationFormDialog(JFrame parent, PrestationManager prestationManager) {
    super(parent, "Nouvelle Formation", true);
    this.prestationManager = prestationManager;
    initializeComponents();
    setupDialog();
  }

  private void initializeComponents() {
    setLayout(new BorderLayout());

    // Panel principal
    JPanel mainPanel = new JPanel(new GridBagLayout());
    mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
    mainPanel.setBackground(Color.WHITE);

    GridBagConstraints gbc = new GridBagConstraints();
    gbc.insets = new Insets(5, 5, 5, 5);
    gbc.anchor = GridBagConstraints.WEST;

    // Titre
    JLabel titleLabel = new JLabel("Saisie d'une Formation");
    titleLabel.setFont(new Font("Arial", Font.BOLD, 18));
    titleLabel.setForeground(PRIMARY_COLOR);
    gbc.gridx = 0;
    gbc.gridy = 0;
    gbc.gridwidth = 2;
    gbc.anchor = GridBagConstraints.CENTER;
    gbc.insets = new Insets(0, 0, 20, 0);
    mainPanel.add(titleLabel, gbc);

    // Reinitialiser les contraintes
    gbc.gridwidth = 1;
    gbc.anchor = GridBagConstraints.WEST;
    gbc.insets = new Insets(5, 5, 5, 5);

    // Champs du formulaire
    int row = 1;

    // Date
    addFormField(mainPanel, gbc, "Date (jj/MM/aaaa) :", row++);
    dateField = createStyledTextField();
    dateField.setName("dateField");
    dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
    gbc.gridx = 1;
    mainPanel.add(dateField, gbc);

    // Heure début
    addFormField(mainPanel, gbc, "Heure debut (HH:mm) :", row++);
    heureDebutField = createStyledTextField();
    heureDebutField.setName("heureDebutField");
    heureDebutField.setText("09:00");
    gbc.gridx = 1;
    mainPanel.add(heureDebutField, gbc);

    // Heure fin
    addFormField(mainPanel, gbc, "Heure fin (HH:mm) :", row++);
    heureFinField = createStyledTextField();
    heureFinField.setName("heureFinField");
    heureFinField.setText("17:00");
    gbc.gridx = 1;
    mainPanel.add(heureFinField, gbc);

    // Classe
    addFormField(mainPanel, gbc, "Classe/Groupe :", row++);
    classeField = createStyledTextField();
    classeField.setName("classeField");
    gbc.gridx = 1;
    mainPanel.add(classeField, gbc);

    // Titre
    addFormField(mainPanel, gbc, "Titre/Module :", row++);
    titreField = createStyledTextField();
    titreField.setName("titreField");
    gbc.gridx = 1;
    mainPanel.add(titreField, gbc);

    // Entreprise
    addFormField(mainPanel, gbc, "Entreprise :", row++);
    entrepriseField = createStyledTextField();
    entrepriseField.setName("entrepriseField");
    gbc.gridx = 1;
    mainPanel.add(entrepriseField, gbc);

    // Tarif horaire
    addFormField(mainPanel, gbc, "Tarif horaire (€) :", row++);
    tarifField = createStyledTextField();
    tarifField.setName("tarifField");
    tarifField.setText("50.00");
    gbc.gridx = 1;
    mainPanel.add(tarifField, gbc);

    add(mainPanel, BorderLayout.CENTER);

    // Panel des boutons
    JPanel buttonPanel = new JPanel(new FlowLayout());
    buttonPanel.setBackground(Color.WHITE);

    JButton saveButton = createStyledButton("Enregistrer", SUCCESS_COLOR);
    JButton cancelButton = createStyledButton("Annuler", ERROR_COLOR);

    saveButton.addActionListener(e -> saveFormation());
    cancelButton.addActionListener(e -> dispose());

    buttonPanel.add(saveButton);
    buttonPanel.add(cancelButton);

    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void addFormField(JPanel panel, GridBagConstraints gbc, String labelText, int row) {
    JLabel label = new JLabel(labelText);
    label.setFont(new Font("Arial", Font.BOLD, 12));
    gbc.gridx = 0;
    gbc.gridy = row;
    panel.add(label, gbc);
  }

  private JTextField createStyledTextField() {
    // Créer un JTextField avec taille fixe explicite (solution au problème
    // d'édition)
    JTextField field = new JTextField();

    // Tailles explicites pour éviter les problèmes de disposition
    field.setPreferredSize(new java.awt.Dimension(200, 25));
    field.setMinimumSize(new java.awt.Dimension(200, 25));
    field.setMaximumSize(new java.awt.Dimension(200, 25));

    // Configuration basique
    field.setEditable(true);
    field.setEnabled(true);
    field.setFocusable(true);

    // Style simple et moderne
    field.setFont(new Font("Arial", Font.PLAIN, 12));
    field.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.GRAY),
        BorderFactory.createEmptyBorder(3, 5, 3, 5)));

    return field;
  }

  private JButton createStyledButton(String text, Color bgColor) {
    JButton button = new JButton(text);
    button.setFont(new Font("Arial", Font.BOLD, 12));
    button.setBackground(bgColor);
    button.setForeground(Color.WHITE);
    button.setBorder(BorderFactory.createEmptyBorder(8, 15, 8, 15));
    button.setFocusPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));

    // Forcer l'opacité pour éviter les problèmes d'affichage
    button.setOpaque(true);
    button.setContentAreaFilled(true);
    button.setBorderPainted(true);

    return button;
  }

  private void setupDialog() {
    setSize(450, 500);
    setLocationRelativeTo(getParent());
    setResizable(false);
    setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

    // S'assurer que le dialogue peut recevoir le focus
    setFocusable(true);
    setAutoRequestFocus(true);

    // Permettre l'interaction avec les composants
    setModal(true);

    // Focus sur le premier champ au démarrage
    SwingUtilities.invokeLater(() -> {
      if (dateField != null) {
        dateField.requestFocusInWindow();
        dateField.selectAll();
      }
    });
  }

  private void saveFormation() {
    try {
      // Valider et recuperer les donnees
      String dateText = dateField.getText().trim();
      String heureDebutText = heureDebutField.getText().trim();
      String heureFinText = heureFinField.getText().trim();
      String classe = classeField.getText().trim();
      String titre = titreField.getText().trim();
      String entreprise = entrepriseField.getText().trim();
      String tarifText = tarifField.getText().trim();

      // Verifications
      if (dateText.isEmpty() || heureDebutText.isEmpty() || heureFinText.isEmpty() ||
          classe.isEmpty() || titre.isEmpty() || entreprise.isEmpty() || tarifText.isEmpty()) {

        JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
      }

      // Parser les données
      DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
      DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");

      LocalDate date = LocalDate.parse(dateText, dateFormatter);
      LocalTime heureDebut = LocalTime.parse(heureDebutText, timeFormatter);
      LocalTime heureFin = LocalTime.parse(heureFinText, timeFormatter);
      double tarifHoraire = Double.parseDouble(tarifText);

      // Verifier que l'heure de fin est apres l'heure de debut
      if (heureFin.isBefore(heureDebut)) {
        JOptionPane.showMessageDialog(this, "L'heure de fin doit etre apres l'heure de debut.", "Erreur",
            JOptionPane.ERROR_MESSAGE);
        return;
      }

      if (tarifHoraire <= 0) {
        JOptionPane.showMessageDialog(this, "Le tarif horaire doit etre positif.", "Erreur", JOptionPane.ERROR_MESSAGE);
        return;
      }

      // Creer la formation
      Formation formation = new Formation(date, heureDebut, heureFin, classe, titre, entreprise, tarifHoraire);

      // Afficher un apercu
      String message = String.format(
          "Formation creee :\n" +
              "Date : %s\n" +
              "Horaires : %s a %s\n" +
              "Duree : %.2f heures\n" +
              "Classe : %s\n" +
              "Titre : %s\n" +
              "Entreprise : %s\n" +
              "Tarif : %.2f €/h\n" +
              "Montant total : %.2f €",
          formation.getDateFormatee(),
          heureDebut.format(timeFormatter),
          heureFin.format(timeFormatter),
          formation.calculerDureeHeures(),
          classe,
          titre,
          entreprise,
          tarifHoraire,
          formation.calculerMontant());

      int confirmation = JOptionPane.showConfirmDialog(this, message, "Confirmer la formation",
          JOptionPane.YES_NO_OPTION);

      if (confirmation == JOptionPane.YES_OPTION) {
        // Sauvegarder la formation en base de données
        if (prestationManager.sauvegarderFormation(formation)) {
          formationCreated = true;
          dispose();
          JOptionPane.showMessageDialog(this,
              "Formation sauvegardée avec succès en base de données !",
              "Succès",
              JOptionPane.INFORMATION_MESSAGE);
        } else {
          JOptionPane.showMessageDialog(this,
              "Erreur lors de la sauvegarde en base de données !",
              "Erreur",
              JOptionPane.ERROR_MESSAGE);
        }
      }

    } catch (DateTimeParseException e) {
      JOptionPane.showMessageDialog(this,
          "Format de date ou heure invalide.\nUtilisez le format jj/MM/aaaa pour la date et HH:mm pour les heures.",
          "Erreur", JOptionPane.ERROR_MESSAGE);
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this, "Le tarif horaire doit être un nombre valide.", "Erreur",
          JOptionPane.ERROR_MESSAGE);
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Erreur lors de la création de la formation : " + e.getMessage(), "Erreur",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  public boolean isFormationCreated() {
    return formationCreated;
  }
}