package com.urlShortener.embeddedJetty;

import com.urlShortener.db.DbConnector;
import com.urlShortener.servlet.RedirectServlet;
import com.urlShortener.servlet.Servlet;
import com.urlShortener.utils.PropertiesLoader;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletHandler;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import static com.urlShortener.utils.PropertiesLoader.JETTY_PORT;

/**
 * Created by tbuldina on 14/03/2018.
 */

/**
 * Starts jetty server and in-memory database
 */
public class EmbeddedJettyServer {
    public static DbConnector dbConnector;
    private static final Logger logger = Logger.getLogger(EmbeddedJettyServer.class.getName());
    public static PropertiesLoader properties;

    public static void main(String[] args) throws IOException {

        properties = new PropertiesLoader();
        // use jetty port from system property
        String systemJettyPort = System.getProperty(JETTY_PORT);
        if (systemJettyPort!= null) properties.setJettyPort(systemJettyPort);

        // use jetty port from default properties
        Server jettyServer = new Server(properties.getJettyPort());
        ServletHandler servletHandler = new ServletHandler();
        servletHandler.addServletWithMapping(Servlet.class, "/index");
        servletHandler.addServletWithMapping(RedirectServlet.class, "/");
        jettyServer.setHandler(servletHandler);

        dbConnector = new DbConnector();

        try {
            jettyServer.start();
            jettyServer.join();
            logger.log(Level.INFO, "Jetty server started");
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Exception on starting jetty server " + e.getMessage());
            throw new RuntimeException("[ERROR] " + e.getMessage());
        }
    }
}
