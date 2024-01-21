package com.example.medicare;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import java.util.Locale;

public abstract class BaseActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        applyLanguage();
    }

    private void applyLanguage() {
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        String languageCode = preferences.getString("APP_LANG_CODE", "en"); // Default to English
        setLocale(this, languageCode);
    }

    protected static void setLocale(Context context, String languageCode) {
        Locale locale = new Locale(languageCode);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.setLocale(locale);
        context.getResources().updateConfiguration(config, context.getResources().getDisplayMetrics());
    }
}
