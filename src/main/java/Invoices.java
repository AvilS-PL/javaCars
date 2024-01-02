import java.util.ArrayList;

public class Invoices {
    private ArrayList<Invoice> all,year,value;

    public Invoices() {
        this.all = new ArrayList<Invoice>();
        this.year = new ArrayList<Invoice>();
        this.value = new ArrayList<Invoice>();
    }

    public void addAll(Invoice one) {
        this.all.add(one);
    }

    public void addYear(Invoice one) {
        this.year.add(one);
    }

    public void addValue(Invoice one) {
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
