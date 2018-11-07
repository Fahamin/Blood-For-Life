package app.seeker.bloodbank.model;

public class User {

    public String user_id;
    public String country;
    public String city;
    public String number;
    public String blood;


    public User() {
    }

    public User(String user_id, String country, String city, String number, String blood) {
        this.user_id = user_id;
        this.country = country;
        this.city = city;
        this.number = number;
        this.blood = blood;
    }
}
