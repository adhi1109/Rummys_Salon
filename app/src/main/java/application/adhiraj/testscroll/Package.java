package application.adhiraj.testscroll;

/**
 * Created by Adhiraj on 13/06/17.
 */

public class Package {
    private String name;
    private String services;
    private int price;
    private String rating;

    public Package() {
    }

    public Package(String name, String services, int price, String rating) {
        this.name = name;
        this.services = services;
        this.price = price;
        this.rating = rating;
    }

    public String getRating() {
        return rating;
    }

    public int getPrice() {

        return price;
    }

    public String getServices() {

        return services;
    }

    public String getName() {

        return name;
    }
}
