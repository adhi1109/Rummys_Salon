package application.adhiraj.testscroll;

/**
 * Created by Adhiraj on 10/06/17.
 */

public class Appointment {
    private String name;
    private String date;
    private String time;
    private String services;
    private int id;
    private String stars;
    private String feedback;

    public Appointment() {
    }

    public Appointment(String name, String date, String time, String services, int id, String stars, String feedback) {
        this.name = name;
        this.date = date;
        this.time = time;
        this.services = services;
        this.id = id;
        this.stars = stars;
        this.feedback = feedback;
    }

    public String getServices() {
        return services;
    }

    public String getTime() {
        return time;
    }

    public String getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public int getID() {
        return id;
    }

    public String getFeedback() {
        return feedback;
    }

    public String getStars() {
        return stars;
    }

    public void setStars(String stars) {
        this.stars = stars;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }
}
