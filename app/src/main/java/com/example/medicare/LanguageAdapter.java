package com.example.medicare;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import classes.Language;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class LanguageAdapter extends ArrayAdapter<Language> {
    private List<Language> languages;

    public LanguageAdapter(Context context, List<Language> languages) {
        super(context, 0, languages);
        this.languages = new ArrayList<>(languages);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_item_language, parent, false);
        }
        TextView tvLanguageName = convertView.findViewById(R.id.tvLanguageName);
        TextView tvLanguageDetail = convertView.findViewById(R.id.tvLanguageDetail);

        Language language = getItem(position);
        if (language != null) {
            tvLanguageName.setText(language.getName());
            tvLanguageDetail.setText(language.getDetail());
        }

        return convertView;
    }

    // Filter logic for search
    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        clear();
        if (charText.length() == 0) {
            addAll(languages);
        } else {
            for (Language lang : languages) {
                if (lang.getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    add(lang);
                }
            }
        }
        notifyDataSetChanged();
    }
}
