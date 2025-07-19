package src.gui;

import src.*;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

/**
 * Dialogue pour la saisie d'une nouvelle consultation
 */
public class ConsultationFormDialog extends JDialog {

 private PrestationManager prestationManager;
 private boolean consultationCreated = false;

 // Composants du formulaire
 private JTextField dateField;
 private JTextArea descriptionArea;
 private JTextField tjmField;
 private JTextField entrepriseField;

 // Couleurs du theme
 private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
 private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
 private static final Color ERROR_COLOR = new Color(231, 76, 60);

 public ConsultationFormDialog(JFrame parent, PrestationManager prestationManager) {
  super(parent, "Nouvelle Consultation", true);
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
  JLabel titleLabel = new JLabel("Saisie d'une Consultation");
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
  dateField.setText(LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")));
  gbc.gridx = 1;
  mainPanel.add(dateField, gbc);

  // Description
  addFormField(mainPanel, gbc, "Description :", row++);
  descriptionArea = createStyledTextArea();
  JScrollPane descScrollPane = new JScrollPane(descriptionArea);
  descScrollPane.setPreferredSize(new Dimension(250, 80));
  gbc.gridx = 1;
  gbc.fill = GridBagConstraints.BOTH;
  mainPanel.add(descScrollPane, gbc);

  // TJM
  gbc.fill = GridBagConstraints.NONE;
  addFormField(mainPanel, gbc, "TJM (€/jour) :", row++);
  tjmField = createStyledTextField();
  tjmField.setText("400.00");
  gbc.gridx = 1;
  mainPanel.add(tjmField, gbc);

  // Entreprise
  addFormField(mainPanel, gbc, "Entreprise :", row++);
  entrepriseField = createStyledTextField();
  gbc.gridx = 1;
  mainPanel.add(entrepriseField, gbc);

  add(mainPanel, BorderLayout.CENTER);

  // Panel des boutons
  JPanel buttonPanel = new JPanel(new FlowLayout());
  buttonPanel.setBackground(Color.WHITE);

  JButton saveButton = createStyledButton("Enregistrer", SUCCESS_COLOR);
  JButton cancelButton = createStyledButton("Annuler", ERROR_COLOR);

  saveButton.addActionListener(e -> saveConsultation());
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

 private JTextArea createStyledTextArea() {
  JTextArea area = new JTextArea(4, 20);
  area.setFont(new Font("Arial", Font.PLAIN, 12));
  area.setBorder(BorderFactory.createCompoundBorder(
    BorderFactory.createLineBorder(Color.GRAY),
    BorderFactory.createEmptyBorder(5, 5, 5, 5)));
  area.setLineWrap(true);
  area.setWrapStyleWord(true);

  // Configuration essentielle pour l'édition
  area.setEditable(true);
  area.setEnabled(true);
  area.setFocusable(true);
  area.setRequestFocusEnabled(true);

  // Forcer l'apparence pour éviter les problèmes de rendu
  area.setOpaque(true);
  area.setBackground(Color.WHITE);
  area.setForeground(Color.BLACK);

  // S'assurer que la zone peut recevoir la saisie
  area.setCaretPosition(0);

  return area;
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
  setSize(400, 350);
  setLocationRelativeTo(getParent());
  setResizable(false);
  setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

  // S'assurer que le dialogue peut recevoir le focus
  setFocusable(true);
  setAutoRequestFocus(true);

  // Permettre l'interaction avec les composants
  setModal(true);

  // S'assurer que le premier champ reçoit le focus au démarrage
  SwingUtilities.invokeLater(() -> {
   if (dateField != null) {
    dateField.requestFocusInWindow();
    dateField.selectAll();
   }
  });
 }

 private void saveConsultation() {
  try {
   // Valider et recuperer les donnees
   String dateText = dateField.getText().trim();
   String description = descriptionArea.getText().trim();
   String tjmText = tjmField.getText().trim();
   String entreprise = entrepriseField.getText().trim();

   // Verifications
   if (dateText.isEmpty() || description.isEmpty() || tjmText.isEmpty() || entreprise.isEmpty()) {
    JOptionPane.showMessageDialog(this, "Veuillez remplir tous les champs.", "Erreur", JOptionPane.ERROR_MESSAGE);
    return;
   }

   // Parser les donnees
   DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
   LocalDate date = LocalDate.parse(dateText, dateFormatter);
   double tjm = Double.parseDouble(tjmText);

   if (tjm <= 0) {
    JOptionPane.showMessageDialog(this, "Le TJM doit etre positif.", "Erreur", JOptionPane.ERROR_MESSAGE);
    return;
   }

   // Creer la consultation
   Consultation consultation = new Consultation(date, description, tjm, entreprise);

   // Afficher un apercu
   String message = String.format(
     "Consultation creee :\n" +
       "Date : %s\n" +
       "Description : %s\n" +
       "Entreprise : %s\n" +
       "TJM : %.2f €\n" +
       "Montant total : %.2f €",
     consultation.getDateFormatee(),
     description,
     entreprise,
     tjm,
     consultation.calculerMontant());

   int confirmation = JOptionPane.showConfirmDialog(this, message, "Confirmer la consultation",
     JOptionPane.YES_NO_OPTION);

   if (confirmation == JOptionPane.YES_OPTION) {
    // Sauvegarder la consultation en base de données
    if (prestationManager.sauvegarderConsultation(consultation)) {
     consultationCreated = true;
     dispose();
     JOptionPane.showMessageDialog(this,
       "Consultation sauvegardée avec succès en base de données !",
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
   JOptionPane.showMessageDialog(this, "Format de date invalide.\nUtilisez le format jj/MM/aaaa.", "Erreur",
     JOptionPane.ERROR_MESSAGE);
  } catch (NumberFormatException e) {
   JOptionPane.showMessageDialog(this, "Le TJM doit être un nombre valide.", "Erreur", JOptionPane.ERROR_MESSAGE);
  } catch (Exception e) {
   JOptionPane.showMessageDialog(this, "Erreur lors de la création de la consultation : " + e.getMessage(), "Erreur",
     JOptionPane.ERROR_MESSAGE);
  }
 }

 public boolean isConsultationCreated() {
  return consultationCreated;
 }
}