import com.itextpdf.text.*;
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
        Font fontBold = FontFactory.getFont(FontFactory.COURIER, 20, Font.BOLD, BaseColor.BLACK);
        Font fontNormal = FontFactory.getFont(FontFactory.COURIER, 16, BaseColor.BLACK);
        Font fontRed = FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD, BaseColor.RED);

        Date date = new Date(this.time);
        DateFormat formatter = new SimpleDateFormat("yyyy/MM/dd/HH/mm/ss");
        String dateFormatted = formatter.format(date);
        Chunk chunk = new Chunk("Faktura: " + dateFormatted,fontBold);

        Paragraph nabywca = new Paragraph("Nabywca: " + this.buyer, fontNormal);
        Paragraph sprzedawca = new Paragraph("Sprzedawca: " + this.seller, fontNormal);
        Paragraph co = new Paragraph("Faktura za " + what, fontRed);
        try {
            document.add(chunk);
            document.add(nabywca);
            document.add(sprzedawca);
            document.add(co);
        } catch (DocumentException e) {
            throw new RuntimeException(e);
        }
        document.close();
    }
}
