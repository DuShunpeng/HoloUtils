package com.meevii.holoutils;

import android.content.Context;

import java.util.Locale;

public class LocaleUtil {
    public static final String LANGUAGE_JP = "ja";

    public static String getLanguage() {
        String language = "en";
        String l = Locale.getDefault().getLanguage();
        if (LANGUAGE_JP.equals(l)) {
            language = l;
        }
        return language;
    }

    public static boolean isJaLanguage() {
        return LANGUAGE_JP.equalsIgnoreCase(getLanguage());
    }

    public static String getCountry(){
        return Locale.getDefault().getCountry();
    }

    public static String getCurrentLanguage(Context context) {
        return context.getResources().getConfiguration().locale.getLanguage();
    }

}
