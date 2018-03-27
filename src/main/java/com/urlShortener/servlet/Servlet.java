package com.urlShortener.servlet;

import com.urlShortener.utils.UrlUtilities;
import org.eclipse.jetty.http.HttpStatus;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.io.IOUtils;
import com.urlShortener.shortener.UrlShortener;
import com.urlShortener.utils.PropertiesLoader;

/**
 * Created by tbuldina on 14/03/2018.
 */
public class Servlet extends HttpServlet {
    public HashMap<String, String> selects = new HashMap();
    public UrlShortener urlShortener = new UrlShortener();
    public PropertiesLoader properties = new PropertiesLoader();
    public String domain = "http://" + properties.getJettyHost() + ":" + properties.getJettyPort().toString() + "/";

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpStatus.OK_200);

        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/index.html"), "UTF-8");
        selects.put("$longUrl", "");
        selects.put("$shortUrl", "");

        for (Map.Entry entry : selects.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            xml = xml.replace(key.toString(), value.toString());
        }
        response.getWriter().println(xml);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String longUrl = request.getParameter("url");

        String xml = IOUtils.toString(this.getClass().getResourceAsStream("/index.html"), "UTF-8");

        if (!UrlUtilities.isUrlEmpty(longUrl)) {
            String shortUrl = urlShortener.encodeUrl(longUrl);
            selects.put("$longUrl", longUrl);
            selects.put("$shortUrl", domain + shortUrl);
            selects.put("$originalUrl", UrlUtilities.makeUrlClickable(longUrl));
        }
        else {
            selects.put("$longUrl", longUrl);
        }

        for (Map.Entry entry : selects.entrySet()) {
            Object key = entry.getKey();
            Object value = entry.getValue();
            xml = xml.replace(key.toString(), value.toString());
        }
        response.getWriter().println(xml);
    }
}




