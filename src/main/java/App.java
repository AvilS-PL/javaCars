import com.fasterxml.uuid.Generators;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;
import spark.Request;
import spark.Response;

import javax.servlet.MultipartConfigElement;
import javax.servlet.ServletException;
import javax.servlet.http.Part;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

import static spark.Spark.*;

class App {
    private static ArrayList<Car> cars = new ArrayList<>();
    private static int liczba = 0;
    private static Gson gson;

    private static ArrayList<String> carNames = new ArrayList<>(){
        {
            add("BMW");
            add("Audi");
            add("Opel");
            add("Fiat");
            add("Ford");
        }
    };
    private static ArrayList<String> carColors = new ArrayList<>(){
        {
            add("red");
            add("green");
            add("blue");
            add("pink");
            add("yellow");
            add("black");
            add("magenta");
        }
    };
    private static ArrayList<Double> perVat = new ArrayList<>(){
        {
            add(22d);
            add(0d);
            add(7d);
        }
    };

    private static Invoices invoices = new Invoices();

    public static void main(String[] args) {
        gson = new Gson();

        //folder
        String projectDir = System.getProperty("user.dir");
        String staticDir = "/src/main/resources/public";
        staticFiles.externalLocation(projectDir + staticDir);

        //ADD
        post("/add", (req, res) -> addCar(req, res));
        get("/get", (req,res)-> getCars(req,res));
        patch("/edit", (req,res)-> editCar(req,res));
        post("/delete", (req,res)-> deleteCar(req,res));
        post("/generate", (req,res)-> generateCars(req,res));
        post("/invoice", (req,res)-> invoiceCar(req,res));
        get("/invoice", (req,res)-> getInvoice(req,res));
        post("/invoiceAll", (req,res)-> invoiceAll(req,res));
        post("/invoiceYear", (req,res)-> invoiceYear(req,res));
        post("/invoiceValue", (req,res)-> invoiceValue(req,res));
        get("/invoices", (req,res)-> getInvoices(req,res));
        post("/sendPics", (req,res)-> sendPics(req,res));
        post("/savePics", (req,res)-> savePics(req,res));
        get("/thumb", (req,res)-> thumb(req,res));
        get("/photos", (req,res)-> photos(req,res));
        post("/editPic", (req,res)-> editPic(req,res));

    }

    private static Object editPic(Request req, Response res) {
        switch (gson.fromJson(req.body(), Edit.class).getAction()) {
            case "rotate":
                Edit.rotate(gson.fromJson(req.body(), Edit.class).getName());
                break;
            case "flipH":
                Edit.flipH(gson.fromJson(req.body(), Edit.class).getName());
                break;
            case "flipV":
                Edit.flipV(gson.fromJson(req.body(), Edit.class).getName());
                break;
        }


        res.type("application/json");
        return gson.toJson("done editing");
    }

    private static Object photos(Request req, Response res) {
        ArrayList<String> photos = null;
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getUuid().equals(req.queryParams("uuid"))) {
                photos = cars.get(i).getPhotos();
                break;
            }
        }
        res.type("application/json");
        return gson.toJson(photos);
    }

    private static Object thumb(Request req, Response res) {
        File file = new File("images/" + req.queryParams("name"));
        res.type("image/jpeg");

        OutputStream outputStream = null;
        try {
            outputStream = res.raw().getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        try {
            outputStream.write(Files.readAllBytes(file.toPath()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            outputStream.flush();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        res.type("application/json");
        return gson.toJson("image sended");
    }

    private static Object savePics(Request req, Response res) {
        for (int i = 0; i < cars.size(); i++) {
            if (cars.get(i).getUuid().equals(gson.fromJson(req.body(), Price.class).getUuid())){
                ArrayList<String> saveTab = gson.fromJson(req.body(), Price.class).getTab();
                for (int j = 0; j < saveTab.size(); j++) {
                    if (!cars.get(i).getPhotos().contains(saveTab.get(j))) cars.get(i).getPhotos().add(saveTab.get(j));
                }
                break;
                // cars.get(i).setPhotos(saveTab)
            }
        }
        res.type("application/json");
        return gson.toJson("image saved to car");
    }

    private static Object sendPics(Request req, Response res) {
        req.attribute("org.eclipse.jetty.multipartConfig", new MultipartConfigElement("/images"));
        ArrayList<String> nazwy = new ArrayList<>();
        try {
            for(Part p : req.raw().getParts()){
                InputStream inputStream = p.getInputStream();

                byte[] bytes = inputStream.readAllBytes();
                //MOŻNA UUID ABY BYŁY DUPLIKATY
                UUID uuid2 = Generators.randomBasedGenerator().generate();
//                String[] exte = p.getSubmittedFileName().split("\\.");
//                String fileName = uuid2 +"."+ exte[exte.length-1];
                String fileName = uuid2 +".jpg";
                FileOutputStream fos = new FileOutputStream("images/" + fileName);
                fos.write(bytes);
                fos.close();
                if (!nazwy.contains(fileName)) {
                    nazwy.add(fileName);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (ServletException e) {
            throw new RuntimeException(e);
        }
        res.type("application/json");
        return gson.toJson(nazwy);
    }

    private static Object getInvoices(Request req, Response res) {

        Map<String, ArrayList<Invoice>> map = new HashMap<>();
        map.put("all", invoices.getAll());
        map.put("year", invoices.getYear());
        map.put("value", invoices.getValue());

        res.type("application/json");
        return gson.toJson(map);
    }

    private static Object invoiceValue(Request req, Response res) {
        ArrayList<Car> tempCars = new ArrayList<Car>();
        double max = gson.fromJson(req.body(), Price.class).getMax();
        double min = gson.fromJson(req.body(), Price.class).getMin();
        if (max < min) {
            max = gson.fromJson(req.body(), Price.class).getMin();
            min = gson.fromJson(req.body(), Price.class).getMax();
        }
        for(int i = 0;i<cars.size();i++) {
            if (cars.get(i).getValue() >= min && cars.get(i).getValue() <= max) {
                tempCars.add(cars.get(i));
            }
        }
        Invoice faktura = new Invoice(System.currentTimeMillis(),"invoice_value","firma sprzedająca", "nabywca", tempCars, "auta o wartościach: " + (int) min + "-" + (int) max + " PLN");
        faktura.generateInvoice();
        invoices.addValue(faktura);
        res.type("application/json");
        return gson.toJson("generated value invoices");
    }

    private static Object invoiceYear(Request req, Response res) {
        ArrayList<Car> tempCars = new ArrayList<Car>();
        for(int i = 0;i<cars.size();i++) {
            if (cars.get(i).getYear().equals(gson.fromJson(req.body(), Car.class).getYear())) {
                tempCars.add(cars.get(i));
            }
        }
        Invoice faktura = new Invoice(System.currentTimeMillis(),"invoice_year","firma sprzedająca", "nabywca", tempCars, "auta z roku " + gson.fromJson(req.body(), Car.class).getYear());
        faktura.generateInvoice();
        invoices.addYear(faktura);
        res.type("application/json");
        return gson.toJson("generated year invoices");
    }

    private static Object invoiceAll(Request req, Response res) {
        Invoice faktura = new Invoice(System.currentTimeMillis(),"invoice_all","firma sprzedająca", "nabywca", cars, "wszystkie auta");
        faktura.generateInvoice();
        invoices.addAll(faktura);
        res.type("application/json");
        return gson.toJson("generated all invoices");
    }

    private static Object getInvoice(Request req, Response res) {
        OutputStream outputStream = null;
        res.type("application/octet-stream");
        res.header("Content-Disposition", "attachment; filename="+req.queryParams("name")+".pdf");
        try {
            outputStream = res.raw().getOutputStream();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            outputStream.write(Files.readAllBytes(Path.of(req.queryParams("path")+"/"+req.queryParams("name")+".pdf")));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        res.type("application/json");
        return gson.toJson("downloaded");
    }

    private static Object invoiceCar(Request req, Response res) {
        for(int i = 0;i<cars.size();i++){
            if (cars.get(i).getUuid().equals(gson.fromJson(req.body(), Car.class).getUuid())) {
                cars.get(i).setVat(true);

                Document document = new Document();
                String path = "katalog/" + cars.get(i).getUuid() + ".pdf";
//                String path = "katalog/plik.pdf";
                try {
                    PdfWriter.getInstance(document, new FileOutputStream(path));
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                } catch (FileNotFoundException e) {
                    throw new RuntimeException(e);
                }

                document.open();
                Font fontBold = FontFactory.getFont(FontFactory.COURIER, 16, Font.BOLD, BaseColor.BLACK);
                Font fontBig = FontFactory.getFont(FontFactory.COURIER, 20, BaseColor.BLACK);
                Font fontSmall = FontFactory.getFont(FontFactory.COURIER, 12, BaseColor.BLACK);
                HashMap<String, BaseColor> map = new HashMap<>() {{
                    put("red", BaseColor.RED);
                    put("green", BaseColor.GREEN);
                    put("blue", BaseColor.BLUE);
                    put("pink", BaseColor.PINK);
                    put("yellow", BaseColor.YELLOW);
                    put("black", BaseColor.BLACK);
                    put("magenta", BaseColor.MAGENTA);
                }};
                Font fontColor = FontFactory.getFont(FontFactory.COURIER, 12, map.get(cars.get(i).getColor()));
                Chunk chunk = new Chunk("Faktura dla: " + cars.get(i).getUuid(), fontBold);
                Paragraph model = new Paragraph("model: " + cars.get(i).getModel(), fontBig);
                Paragraph color = new Paragraph("kolor: " + cars.get(i).getColor(), fontColor);
                Paragraph rok = new Paragraph("rok: " + cars.get(i).getYear(), fontSmall);
                try {
                    document.add(chunk);
                    document.add(model);
                    document.add(color);
                    document.add(rok);
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                }
                cars.get(i).getAirbags().forEach((n) -> {
                    Paragraph poduszka = new Paragraph("poduszka: " + n.getDescription() + " - " + n.getVal(), fontSmall);
                    try {
                        document.add(poduszka);
                    } catch (DocumentException e) {
                        throw new RuntimeException(e);
                    }
                });
                Image img = null;
                try {
                    img = Image.getInstance("src/main/resources/public/templateCars/"+cars.get(i).getModel()+".jpg");
                } catch (BadElementException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                try {
                    document.add(img);
                } catch (DocumentException e) {
                    throw new RuntimeException(e);
                }

                document.close();
            }
        }
        res.type("application/json");
        return gson.toJson("invoiced");
    }

    private static Object generateCars(Request req, Response res) {
        for (int i = 0; i < 5; i++) {
            liczba++;
            String model = carNames.get((int) (Math.random()*carNames.size()));
            String year = "200" + (int) (Math.random()*10);
            String color = carColors.get((int) (Math.random()*carColors.size()));
            UUID uuid = Generators.randomBasedGenerator().generate();
            ArrayList<AirBag> bagsy = new ArrayList<AirBag>(){{
                add(new AirBag("kierowca",(Math.random() > 0.5 ? "true" : "false")));
                add(new AirBag("pasazer",(Math.random() > 0.5 ? "true" : "false")));
                add(new AirBag("kanapa",(Math.random() > 0.5 ? "true" : "false")));
                add(new AirBag("boczne",(Math.random() > 0.5 ? "true" : "false")));
            }};

            cars.add(new Car(model, year, color.toString(), liczba, uuid.toString(), bagsy));

            CustomDate cd = new CustomDate(Helpers.generateNumber(1,30),Helpers.generateNumber(1,12),Helpers.generateNumber(2000,2009));
            cars.get(cars.toArray().length - 1).setBuy(cd);

            cars.get(cars.toArray().length - 1).setValue(Helpers.generateNumber(10000,40000));
            cars.get(cars.toArray().length - 1).setpVat(perVat.get((int) (Math.random()*perVat.size())));
        }
        res.type("application/json");
        return gson.toJson("generated");
    }

    private static Object deleteCar(Request req, Response res) {
        for(int i = 0;i<cars.size();i++){
            if (cars.get(i).getUuid().equals(gson.fromJson(req.body(), Car.class).getUuid())) {
                cars.remove(i);
            }
        }
        res.type("application/json");
        return gson.toJson("removed");
    }


    private static Object editCar(Request req, Response res) {
        for(int i = 0;i<cars.size();i++){
            if (cars.get(i).getUuid().equals(gson.fromJson(req.body(), Car.class).getUuid())) {
                cars.get(i).setModel(gson.fromJson(req.body(), Car.class).getModel());
                cars.get(i).setYear(gson.fromJson(req.body(), Car.class).getYear());
            }
        }
        res.type("application/json");
        return gson.toJson("edited");
    }

    private static Object getCars(Request req, Response res) {
        return gson.toJson(cars);
    }

    private static Object addCar(Request req, Response res) {
        liczba++;
        Car c = gson.fromJson(req.body(), Car.class);

        UUID uuid = Generators.randomBasedGenerator().generate();

        c.setId(liczba);
        c.setUuid(uuid.toString());
        cars.add(c);

        res.type("application/json");
        return gson.toJson(cars.get(cars.toArray().length - 1));
    }
}