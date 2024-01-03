import java.util.ArrayList;
public class Car {
    private String model, year, color, uuid;
    private int id;
    private ArrayList<AirBag> airbags;
    private CustomDate buy;

    private ArrayList<String> photos;

    private boolean vat;
    private double value, pVat;

    public Car(String model, String year, String color, int id, String uuid, ArrayList<AirBag> airbags) {
        this.id = id;
        this.uuid = uuid;
        this.model = model;
        this.year = year;
        this.airbags = airbags;
        this.color = color;
        this.vat = false;
        this.photos = new ArrayList<>();
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public void setVat(boolean vat) {
        this.vat = vat;
    }

    public void setBuy(CustomDate buy) {
        this.buy = buy;
    }

    public void setPhotos(ArrayList<String> photos) {
        this.photos = photos;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setpVat(double pVat) {
        this.pVat = pVat;
    }

    public String getModel() {
        return model;
    }

    public String getYear() {
        return year;
    }

    public String getUuid() {
        return uuid;
    }

    public String getColor() {
        return color;
    }

    public ArrayList<AirBag> getAirbags() {
        return airbags;
    }

    public double getValue() {
        return value;
    }

    public double getpVat() {
        return pVat;
    }

    public ArrayList<String> getPhotos() {
        return photos;
    }
}