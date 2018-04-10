package application.adhiraj.testscroll;

/**
 * Created by Adhiraj on 30/05/17.
 */

public class Service {
    private String name;
    private int price;

    public Service(String mName, int mPrice) {
        this.name = mName;
        this.price = mPrice;
    }
    public Service (){ }

    public String getPrice() {
        return Integer.toString(price);
    }

    public String getName() {
        return name;
    }
}
