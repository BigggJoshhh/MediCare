package classes;

public class User {
    private boolean notifications;
    private String language;
    private String phone_number;
    private String email;
    private String username;
    private String gender;

    public User(){

    }
    public User(String username, String email, String phone_number, String language, String gender, boolean notifications){
        this.email = email;
        this.username = username;
        this.phone_number = phone_number;
        this.language = language;
        this.gender = gender;
        this.notifications= notifications;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
    public boolean isNotifications() {
        return notifications;
    }

    public void setNotifications(boolean notifications) {
        this.notifications = notifications;
    }

    public String getLanguage() {
        return language;
    }

    public void setLanguage(String language) {
        this.language = language;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
