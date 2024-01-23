package com.example.medicare;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SearchView;

import java.util.ArrayList;
import java.util.List;

public class LanguageSelect extends BaseActivity {

    ListView listView;
    SearchView searchView;
    ArrayAdapter<String> adapter;
    List<String> languageList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_language_select);

        listView = findViewById(R.id.language_list);
        searchView = findViewById(R.id.search_language);

        // Populate the language list
        languageList = new ArrayList<>();
        languageList.add("English");
        languageList.add("Français");
        languageList.add("繁體中文");

        // Set the adapter
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, languageList);
        listView.setAdapter(adapter);

        // Set the list item click listener
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String selectedLanguageCode = getLanguageCode(position);
                saveLanguagePreference(selectedLanguageCode);
                setLocale(LanguageSelect.this, selectedLanguageCode);
                recreate(); // Recreate to apply the language change
            }
        });

        // Implement search functionality
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter.getFilter().filter(newText);
                return false;
            }
        });
    }

    private String getLanguageCode(int position) {
        switch (position) {
            case 0: return "en"; // English
            case 1: return "fr"; // French
            case 2: return "zh"; // Chinese
            default: return "en";
        }
    }

    private void saveLanguagePreference(String languageCode) {
        SharedPreferences preferences = getSharedPreferences("AppSettings", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("APP_LANG_CODE", languageCode);
        editor.apply();
    }
}
