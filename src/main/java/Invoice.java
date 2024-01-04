import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

public class Invoice {
    private long time;
    private String title, seller,buyer, what;
    private ArrayList<Car> list;

    public Invoice(long time,String title, String seller, String buyer, ArrayList<Car> list, String what) {
        this.time = time;
        this.title = title + "_" + time;
        this.seller = seller;
        this.buyer = buyer;
        this.list = list;
        this.what = what;
    }
    public void generateInvoice() {
        Document document = new Document();
        String path = "invoices/" + this.title + ".pdf";
        try {
            PdfWriter.getInstance(document, new FileOutputStream(path));
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }

        document.open();
        Font fontBold = FontFactory.getFont("Courier", "Cp1250", true, 20, Font.BOLD, BaseColor.BLACK);
        Font fontNormal = FontFactory.getFont("Courier", "Cp1250", true, 16,Font.NORMAL, BaseColor.BLACK);
        Font fontRed = FontFactory.getFont("Courier", "Cp1250", true, 16, Font.BOLD, BaseColor.RED);

        Font cellBold = FontFactory.getFont("Courier", "Cp1250", true, 12, Font.BOLD, BaseColor.BLACK);
        Font cellNormal = FontFactory.getFont("Courier", "Cp1250", true, 12,Font.NORMAL, BaseColor.BLACK);


        Date date = new Date(this.time);
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        String dateFormatted = formatter.format(date);
        Chunk chunk = new Chunk("Faktura: " + dateFormatted,fontBold);

        Paragraph nabywca = new Paragraph("Nabywca: " + this.buyer, fontNormal);
        Paragraph sprzedawca = new Paragraph("Sprzedawca: " + this.seller, fontNormal);
        Paragraph co = new Paragraph("Faktura za " + what, fontRed);
        co.setSpacingAfter(20);

        PdfPTable table = new PdfPTable(4);
        table.addCell(new PdfPCell(new Phrase("lp", cellBold)));
        table.addCell(new PdfPCell(new Phrase("cena", cellBold)));
        table.addCell(new PdfPCell(new Phrase("vat", cellBold)));
        table.addCell(new PdfPCell(new Phrase("wartość", cellBold)));
        double fullValue = 0;
        for (int i = 0; i < list.size(); i++) {
            table.addCell(new PdfPCell(new Phrase(String.valueOf(i+1), cellNormal)));
            double val = list.get(i).getValue();
            double perVat = list.get(i).getpVat();
            table.addCell(new PdfPCell(new Phrase(String.valueOf(val), cellNormal)));
            table.addCell(new PdfPCell(new Phrase(String.valueOf(perVat) + "%", cellNormal)));
            double liczba = (double) Math.round((val + (val * perVat) / 100) * 100) /100;
            table.addCell(new PdfPCell(new Phrase(String.valueOf(liczba), cellNormal)));
            fullValue += (double) liczba;
        }
        fullValue = (double) Math.round(fullValue*100)/100;
        Paragraph cena = new Paragraph("Do zapłaty: " + String.valueOf(fullValue) + " PLN", fontBold);
        cena.setSpacingBefore(20);
        try {
            document.add(chunk);
            document.add(nabywca);
            document.add(sprzedawca);
            document.add(co);
            document.add(table);
            document.add(cena);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        document.close();
    }
}
