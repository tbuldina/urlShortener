package com.urlShortener.utils;

/**
 * Created by tbuldina on 19/03/2018.
 */
public class UrlUtilities {

    public static String makeUrlClickable(String url) {
        if ((!url.startsWith("http://") && (!url.startsWith("https://")))) {
            url = "http://" + url;
        }
        return url;
    }

    public static Boolean isUrlEmpty(String longUrl) {
        Boolean isEmpty = false;
        if (longUrl==null || longUrl.trim().isEmpty())
            isEmpty = true;
        return isEmpty;
    }
}
