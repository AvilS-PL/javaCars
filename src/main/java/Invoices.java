import java.util.ArrayList;

public class Invoices {
    private ArrayList<Invoice> all,year,value;

    public void setAll(Invoice one) {
        this.all.add(one);
    }

    public void setYear(Invoice one) {
        this.year.add(one);
    }

    public void setValue(Invoice one) {
        this.value.add(one);
    }

    public ArrayList<Invoice> getAll() {
        return all;
    }

    public ArrayList<Invoice> getYear() {
        return year;
    }

    public ArrayList<Invoice> getValue() {
        return value;
    }
}
