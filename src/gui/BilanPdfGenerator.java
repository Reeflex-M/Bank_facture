package src.gui;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import java.io.FileOutputStream;
import java.util.List;
import src.Prestation;

public class BilanPdfGenerator {
    public static void genererBilan(List<Prestation> prestations, String periode, double total, String cheminFichier) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(cheminFichier));
        document.open();
        Font titleFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        Font normalFont = new Font(Font.HELVETICA, 12);
        document.add(new Paragraph("Bilan du chiffre d'affaires", titleFont));
        document.add(new Paragraph("Période : " + periode, normalFont));
        document.add(new Paragraph(" "));

        PdfPTable table = new PdfPTable(4);
        table.setWidthPercentage(100);
        table.addCell("Date");
        table.addCell("Type");
        table.addCell("Entreprise");
        table.addCell("Montant (€)");

        for (Prestation p : prestations) {
            table.addCell(p.getDateFormatee());
            table.addCell(p.getType());
            table.addCell(p.getEntreprise());
            table.addCell(String.format("%.2f", p.calculerMontant()));
        }
        document.add(table);

        document.add(new Paragraph(" "));
        document.add(new Paragraph("Total : " + String.format("%.2f", total) + " €", titleFont));
        document.close();
    }

    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> new MainWindow().setVisible(true));
    }
} 