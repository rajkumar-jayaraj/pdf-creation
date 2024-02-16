package org.example;

import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileOutputStream;

public class PdfGenerator {

    public static void main(String[] args) {
        try {
            Document document = new Document();
            PdfWriter.getInstance(document, new FileOutputStream("output.pdf"));
            document.open();

            // Add content to the PDF
            addContent(document);

            document.close();
            System.out.println("PDF created successfully.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addContent(Document document) {
        try {
            // Add paragraphs with the provided data
            document.add(new Paragraph("GROUP     A                                              BRANCH     TOTALS"));
            document.add(new Paragraph("******************************************************** LOAN    RECONCILIATION ****************************************************"));

            // Create a table for loan debits and credits
            PdfPTable table = new PdfPTable(4); // 4 columns
            table.setWidthPercentage(100); // Width 100%

            // Add column headers
            table.addCell("Description");
            table.addCell("Debits - Number");
            table.addCell("Debits - Amount");
            table.addCell("Credits - Number");

            // Add data rows
            addRow(table, "ENTERED", "0", "0.00", "0");

            addRow(table, "GENERATED", "AUTOMATIC LOAN", "0", "");
            addRow(table, "GENERATED", "LOAN OVERPAYMENT", "0", "");
            // Add more rows as needed...

            // Add the table to the document
            document.add(table);

            document.add(new Paragraph("TOTAL TRANSACTIONS"));
            // Create a new table for the "TOTAL TRANSACTIONS" section
            PdfPTable totalTransactionsTable = new PdfPTable(4);
            totalTransactionsTable.setWidthPercentage(100);

            totalTransactionsTable.addCell("Description");
            totalTransactionsTable.addCell("Debits - Number");
            totalTransactionsTable.addCell("Debits - Amount");
            totalTransactionsTable.addCell("Credits - Number");

            // Add data rows for "TOTAL TRANSACTIONS"
            addRow(totalTransactionsTable, "TOTAL TRANSACTIONS", "0", "0.00", "0");
            addRow(totalTransactionsTable, "POSTED TRANSACTIONS", "0", "0.00", "0");
            addRow(totalTransactionsTable, "UNPOSTED TRANSACTIONS", "0", "0.00", "0");
            addRow(totalTransactionsTable, "TOTAL TRANSACTIONS", "0", "0.00", "0");

            // Add the "TOTAL TRANSACTIONS" table to the document
            document.add(totalTransactionsTable);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void addRow(PdfPTable table, String description, String debitsNumber, String debitsAmount, String creditsNumber) {
        table.addCell(description);
        table.addCell(debitsNumber);
        table.addCell(debitsAmount);
        table.addCell(creditsNumber);
    }
}
