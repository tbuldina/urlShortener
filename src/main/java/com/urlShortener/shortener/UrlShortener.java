package com.urlShortener.shortener;

import com.urlShortener.utils.UrlUtilities;

import static com.urlShortener.embeddedJetty.EmbeddedJettyServer.dbConnector;

/**
 * Created by tbuldina on 16/03/2018.
 */
public class UrlShortener {
    private String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
    private int BASE = ALPHABET.length();

    /**
     * long url -> auto-incremented id from db -> shortened url
     * From not empty longUrl removes leading and trailing whites
     * In case database already contains url - use identificator of that url for encoding
     * Otherwise insert url to database and use its identificator for encoding
     * Encode id
     * Save short url in database for corresponding long url
     * <p>
     * Returns shortened url
     *
     * @param  longUrl
     * @return shortUrl
     */
    public String encodeUrl(String longUrl) {
        int id = 0;
        if (UrlUtilities.isUrlEmpty(longUrl)) return "";
        longUrl = longUrl.toLowerCase().trim();
        String shortUrl;
        String longUrlModified;

        if (!longUrl.startsWith("https://")) {
            if (longUrl.startsWith("http://")) longUrlModified = longUrl.substring(7);
            else longUrlModified = "http://" + longUrl;

            if (dbConnector.isUrlExist(longUrlModified)) {
                shortUrl = dbConnector.selectShortUrlByLongUrl(longUrlModified);
                return shortUrl;
            }
        }
        if (dbConnector.isUrlExist(longUrl)) {
            shortUrl = dbConnector.selectShortUrlByLongUrl(longUrl);
        } else {
            id = dbConnector.insertUrl(longUrl);
            shortUrl = encode(id);
            dbConnector.insertShortUrl(longUrl, shortUrl);
        }
        return shortUrl;
    }

    /**
     * Convert id to base62 string
     * @param num
     * @return short url
     */
    public String encode(int num) {
        StringBuilder sb = new StringBuilder();
        while (num > 0) {
            sb.append(ALPHABET.charAt(num % BASE));
            num /= BASE;
        }
        return sb.reverse().toString();
    }
}
