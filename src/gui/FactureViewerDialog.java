package src.gui;

import src.FactureManager;
import src.Prestation;
import src.Formation;
import src.Consultation;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.awt.print.*;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

/**
 * Visualiseur graphique de factures intégré dans l'application
 */
public class FactureViewerDialog extends JDialog {

  private String entreprise;
  private List<Prestation> prestations;
  private YearMonth periode;
  private JPanel facturePanel;

  // Couleurs du thème
  private static final Color PRIMARY_COLOR = new Color(41, 128, 185);
  private static final Color SECONDARY_COLOR = new Color(52, 73, 94);
  private static final Color SUCCESS_COLOR = new Color(39, 174, 96);
  private static final Color ERROR_COLOR = new Color(231, 76, 60);
  private static final Color BACKGROUND_COLOR = new Color(236, 240, 241);
  private static final Color FACTURE_BG = Color.WHITE;

  public FactureViewerDialog(JFrame parent, String entreprise, List<Prestation> prestations, YearMonth periode) {
    super(parent, "Aperçu Facture - " + entreprise, true);
    this.entreprise = entreprise;
    this.prestations = prestations;
    this.periode = periode;
    initializeComponents();
    setupDialog();
    creerFactureGraphique();
  }

  private void initializeComponents() {
    setLayout(new BorderLayout());

    // Barre d'outils
    JPanel toolBar = new JPanel(new FlowLayout(FlowLayout.LEFT));
    toolBar.setBackground(PRIMARY_COLOR);

    JButton btnImprimer = createToolButton("Imprimer", "Imprimer la facture");
    JButton btnExporterHTML = createToolButton("Export HTML", "Exporter en HTML");
    JButton btnFermer = createToolButton("Fermer", "Fermer l'aperçu");

    toolBar.add(btnImprimer);
    toolBar.add(btnExporterHTML);
    toolBar.add(Box.createHorizontalGlue());
    toolBar.add(btnFermer);

    add(toolBar, BorderLayout.NORTH);

    // Panel principal avec la facture
    facturePanel = new JPanel();
    facturePanel.setLayout(new BoxLayout(facturePanel, BoxLayout.Y_AXIS));
    facturePanel.setBackground(Color.LIGHT_GRAY);
    facturePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

    JScrollPane scrollPane = new JScrollPane(facturePanel);
    scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
    scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    add(scrollPane, BorderLayout.CENTER);

    // Actions des boutons
    btnImprimer.addActionListener(e -> imprimerFacture());
    btnExporterHTML.addActionListener(e -> exporterHTML());
    btnFermer.addActionListener(e -> dispose());
  }

  private void setupDialog() {
    setSize(800, 900);
    setLocationRelativeTo(getParent());
    setResizable(true);
  }

  private JButton createToolButton(String text, String tooltip) {
    JButton button = new JButton(text);
    button.setFont(new Font("Arial", Font.BOLD, 12));
    button.setBackground(Color.WHITE);
    button.setForeground(PRIMARY_COLOR);
    button.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(PRIMARY_COLOR, 1),
        BorderFactory.createEmptyBorder(8, 15, 8, 15)));
    button.setFocusPainted(false);
    button.setCursor(new Cursor(Cursor.HAND_CURSOR));
    button.setToolTipText(tooltip);

    // Effet hover
    button.addMouseListener(new java.awt.event.MouseAdapter() {
      public void mouseEntered(java.awt.event.MouseEvent evt) {
        button.setBackground(PRIMARY_COLOR);
        button.setForeground(Color.WHITE);
      }

      public void mouseExited(java.awt.event.MouseEvent evt) {
        button.setBackground(Color.WHITE);
        button.setForeground(PRIMARY_COLOR);
      }
    });

    return button;
  }

  private void creerFactureGraphique() {
    // Panel de la facture avec fond blanc (comme une feuille)
    JPanel feuille = new JPanel();
    feuille.setLayout(new BoxLayout(feuille, BoxLayout.Y_AXIS));
    feuille.setBackground(FACTURE_BG);
    feuille.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createLineBorder(Color.GRAY, 1),
        BorderFactory.createEmptyBorder(40, 40, 40, 40)));
    feuille.setMaximumSize(new Dimension(700, Integer.MAX_VALUE));

    // En-tête de la facture
    ajouterEnTete(feuille);

    // Informations prestataire et client
    ajouterInformations(feuille);

    // Tableau des prestations
    ajouterTableauPrestations(feuille);

    // Total
    ajouterTotal(feuille);

    // Pied de page
    ajouterPiedDePage(feuille);

    facturePanel.add(feuille);
    facturePanel.add(Box.createVerticalGlue());
  }

  private void ajouterEnTete(JPanel parent) {
    JLabel titre = new JLabel("FACTURE", SwingConstants.CENTER);
    titre.setFont(new Font("Arial", Font.BOLD, 28));
    titre.setForeground(PRIMARY_COLOR);
    titre.setAlignmentX(Component.CENTER_ALIGNMENT);
    titre.setBorder(BorderFactory.createEmptyBorder(0, 0, 30, 0));
    parent.add(titre);
  }

  private void ajouterInformations(JPanel parent) {
    // Panel pour les informations en deux colonnes
    JPanel infoPanel = new JPanel(new GridLayout(1, 2, 20, 0));
    infoPanel.setBackground(FACTURE_BG);
    infoPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 120));

    // Colonne gauche - Prestataire
    JPanel prestatairePanel = new JPanel();
    prestatairePanel.setLayout(new BoxLayout(prestatairePanel, BoxLayout.Y_AXIS));
    prestatairePanel.setBackground(FACTURE_BG);
    prestatairePanel.setBorder(BorderFactory.createTitledBorder("Prestataire"));

    JLabel nomPrestataire = new JLabel("Micro Entrepreneur");
    nomPrestataire.setFont(new Font("Arial", Font.BOLD, 14));
    prestatairePanel.add(nomPrestataire);

    prestatairePanel.add(new JLabel("Email: contact@entrepreneur.fr"));
    prestatairePanel.add(new JLabel("Date: " + java.time.LocalDate.now().format(
        DateTimeFormatter.ofPattern("dd/MM/yyyy"))));

    // Colonne droite - Client
    JPanel clientPanel = new JPanel();
    clientPanel.setLayout(new BoxLayout(clientPanel, BoxLayout.Y_AXIS));
    clientPanel.setBackground(FACTURE_BG);
    clientPanel.setBorder(BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Client"),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)));

    JLabel nomClient = new JLabel("Facturé à: " + entreprise);
    nomClient.setFont(new Font("Arial", Font.BOLD, 14));
    clientPanel.add(nomClient);

    clientPanel.add(new JLabel("Période: " + periode.format(
        DateTimeFormatter.ofPattern("MMMM yyyy", Locale.FRENCH))));

    infoPanel.add(prestatairePanel);
    infoPanel.add(clientPanel);

    parent.add(infoPanel);
    parent.add(Box.createVerticalStrut(20));
  }

  private void ajouterTableauPrestations(JPanel parent) {
    // Créer le modèle de tableau
    String[] colonnes = { "Date", "Description", "Type", "Détails", "Montant (€)" };
    DefaultTableModel model = new DefaultTableModel(colonnes, 0) {
      @Override
      public boolean isCellEditable(int row, int column) {
        return false;
      }
    };

    // Remplir le tableau
    for (Prestation prestation : prestations) {
      String description = "";
      String details = "";

      if (prestation instanceof Formation) {
        Formation formation = (Formation) prestation;
        description = formation.getTitre() + " (" + formation.getClasse() + ")";
        details = String.format("%.1fh à %.0f€/h", formation.calculerDureeHeures(), formation.getTarifHoraire());
      } else if (prestation instanceof Consultation) {
        Consultation consultation = (Consultation) prestation;
        description = consultation.getDescription();
        details = String.format("TJM: %.0f€", consultation.getTjm());
      }

      model.addRow(new Object[] {
          prestation.getDateFormatee(),
          description,
          prestation.getType(),
          details,
          String.format("%.2f", prestation.calculerMontant())
      });
    }

    // Créer et configurer le tableau
    JTable table = new JTable(model);
    table.setFont(new Font("Arial", Font.PLAIN, 12));
    table.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
    table.getTableHeader().setBackground(PRIMARY_COLOR);
    table.getTableHeader().setForeground(Color.WHITE);
    table.setRowHeight(25);
    table.setGridColor(Color.LIGHT_GRAY);
    table.setShowGrid(true);

    // Alternance des couleurs de lignes
    table.setDefaultRenderer(Object.class, new DefaultTableCellRenderer() {
      @Override
      public Component getTableCellRendererComponent(JTable table, Object value,
          boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (!isSelected) {
          c.setBackground(row % 2 == 0 ? Color.WHITE : new Color(248, 249, 250));
        }
        return c;
      }
    });

    JScrollPane tableScrollPane = new JScrollPane(table);
    tableScrollPane.setPreferredSize(new Dimension(600, Math.min(200, prestations.size() * 25 + 50)));
    tableScrollPane.setMaximumSize(new Dimension(Integer.MAX_VALUE, 300));

    parent.add(tableScrollPane);
    parent.add(Box.createVerticalStrut(20));
  }

  private void ajouterTotal(JPanel parent) {
    double total = prestations.stream().mapToDouble(Prestation::calculerMontant).sum();

    JPanel totalPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
    totalPanel.setBackground(FACTURE_BG);
    totalPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 50));

    JLabel totalLabel = new JLabel(String.format("TOTAL: %.2f €", total));
    totalLabel.setFont(new Font("Arial", Font.BOLD, 18));
    totalLabel.setForeground(PRIMARY_COLOR);
    totalPanel.add(totalLabel);

    parent.add(totalPanel);
    parent.add(Box.createVerticalStrut(20));
  }

  private void ajouterPiedDePage(JPanel parent) {
    JPanel piedPanel = new JPanel();
    piedPanel.setLayout(new BoxLayout(piedPanel, BoxLayout.Y_AXIS));
    piedPanel.setBackground(FACTURE_BG);
    piedPanel.setMaximumSize(new Dimension(Integer.MAX_VALUE, 60));

    JLabel merci = new JLabel("Merci pour votre confiance !", SwingConstants.CENTER);
    merci.setFont(new Font("Arial", Font.ITALIC, 12));
    merci.setAlignmentX(Component.CENTER_ALIGNMENT);

    JLabel dateGeneration = new JLabel("Facture générée le " +
        java.time.LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy")),
        SwingConstants.CENTER);
    dateGeneration.setFont(new Font("Arial", Font.PLAIN, 10));
    dateGeneration.setForeground(Color.GRAY);
    dateGeneration.setAlignmentX(Component.CENTER_ALIGNMENT);

    piedPanel.add(merci);
    piedPanel.add(dateGeneration);

    parent.add(piedPanel);
  }

  private void imprimerFacture() {
    try {
      PrinterJob job = PrinterJob.getPrinterJob();
      job.setPrintable(new FacturePrintable());

      if (job.printDialog()) {
        job.print();
        JOptionPane.showMessageDialog(this,
            "Facture envoyée à l'imprimante avec succès !",
            "Impression",
            JOptionPane.INFORMATION_MESSAGE);
      }
    } catch (PrinterException e) {
      JOptionPane.showMessageDialog(this,
          "Erreur lors de l'impression : " + e.getMessage(),
          "Erreur d'impression",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void exporterHTML() {
    // Utiliser le FactureManager existant pour générer le HTML
    FactureManager factureManager = new FactureManager();
    String fichier = factureManager.genererFactureHTML(entreprise, prestations, periode.getYear(),
        periode.getMonthValue());

    if (fichier != null) {
      JOptionPane.showMessageDialog(this,
          "Facture exportée avec succès :\n" + fichier,
          "Export réussi",
          JOptionPane.INFORMATION_MESSAGE);

      // Proposer d'ouvrir le fichier
      int choix = JOptionPane.showConfirmDialog(this,
          "Voulez-vous ouvrir la facture dans votre navigateur ?",
          "Ouvrir le fichier",
          JOptionPane.YES_NO_OPTION);

      if (choix == JOptionPane.YES_OPTION) {
        factureManager.ouvrirFacture(fichier);
      }
    } else {
      JOptionPane.showMessageDialog(this,
          "Erreur lors de l'export de la facture.",
          "Erreur d'export",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Classe interne pour l'impression de la facture
   */
  private class FacturePrintable implements Printable {
    @Override
    public int print(Graphics g, PageFormat pf, int page) throws PrinterException {
      if (page > 0) {
        return NO_SUCH_PAGE;
      }

      Graphics2D g2d = (Graphics2D) g;
      g2d.translate(pf.getImageableX(), pf.getImageableY());

      // Ajuster l'échelle pour l'impression
      double scaleX = pf.getImageableWidth() / facturePanel.getWidth();
      double scaleY = pf.getImageableHeight() / facturePanel.getHeight();
      double scale = Math.min(scaleX, scaleY);
      g2d.scale(scale, scale);

      // Imprimer le contenu
      facturePanel.printAll(g2d);

      return PAGE_EXISTS;
    }
  }
}