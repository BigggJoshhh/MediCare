package classes;

public class Language {
    private String name;
    private String detail;

    public Language(){

    }
    public Language(String name, String detail) {
        this.name = name;
        this.detail = detail;
    }

    public String getName() {
        return name;
    }

    public String getDetail() {
        return detail;
    }
}
