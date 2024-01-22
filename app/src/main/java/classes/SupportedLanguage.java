package classes;

import java.util.Locale;

public enum SupportedLanguage {
    ENGLISH("English", "en", "SG"),
    FRENCH("Français", "fr", "FR"),
    CHINESE_TRADITIONAL("繁體中文", "zh", "TW");

    private String name;
    private String languageCode;
    private String countryCode;

    SupportedLanguage(String name, String languageCode, String countryCode) {
        this.name = name;
        this.languageCode = languageCode;
        this.countryCode = countryCode;
    }

    public String getName() {
        return name;
    }

    public Locale getLocale() {
        return new Locale(languageCode, countryCode);
    }
}
