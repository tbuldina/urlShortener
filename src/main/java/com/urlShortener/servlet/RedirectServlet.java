package com.urlShortener.servlet;

import com.urlShortener.shortener.UrlShortener;
import com.urlShortener.utils.UrlUtilities;
import org.eclipse.jetty.http.HttpStatus;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static com.urlShortener.embeddedJetty.EmbeddedJettyServer.dbConnector;

public class RedirectServlet extends HttpServlet {
    public UrlShortener urlShortener = new UrlShortener();

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setStatus(HttpStatus.OK_200);

        String path = request.getServletPath();
        String url = dbConnector.selectUrlByShortUrl(path.substring(1));
        if (!url.equals("shortUrl not found")) {
            response.sendRedirect(UrlUtilities.makeUrlClickable(url));
        }
        else {
            response.sendRedirect(path);
        }
    }
}
