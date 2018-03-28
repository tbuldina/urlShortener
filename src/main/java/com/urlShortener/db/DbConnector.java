package com.urlShortener.db;

import com.urlShortener.utils.PropertiesLoader;
import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by tbuldina on 15/03/2018.
 */
/**
 * Class contains methods to work with database
 */
public class DbConnector {

    PropertiesLoader properties = new PropertiesLoader();
    public String dbUrl = properties.getDatabaseUrl();
    public Connection conn;
    public PreparedStatement prstmt;
    private static final Logger logger = Logger.getLogger(DbConnector.class.getName());

    public DbConnector() {
        createTable(dbUrl);
    }

    public void createTable(String dbUrl) {
        try {
            conn = DriverManager.getConnection(dbUrl);
            Statement stmt = conn.createStatement();
            stmt.executeUpdate("create table urls ("
                    + "id int not null generated always as identity, "
                    + "url varchar(10000), "
                    + "shorturl varchar(10000), "
                    + "primary key (id) "
                    + ")");
            stmt = conn.createStatement();
            stmt.executeUpdate("create index ix_url on urls (url)");
            stmt = conn.createStatement();
            stmt.executeUpdate("create index ix_shorturl on urls (shorturl)");
            stmt.close();
            conn.close();
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Exception on creating table: " + e.getMessage());
            throw new RuntimeException("[ERROR] " + e.getMessage());
        }
        logger.log(Level.INFO, "Created table and index");
    }

    public Integer insertUrl(String urlToInsert) {
        try {
            conn = DriverManager.getConnection(dbUrl);

            String query = "INSERT INTO urls (url) VALUES (?) ";
            prstmt = conn.prepareStatement(query);
            prstmt.setString(1, urlToInsert);
            prstmt.executeUpdate();
            logger.info("Inserted url '" + urlToInsert + "'");

            query = "SELECT MAX(id) from urls";
            prstmt = conn.prepareStatement(query);
            ResultSet maxIdRS = prstmt.executeQuery();
            maxIdRS.next();
            int maxId = maxIdRS.getInt(1);
            prstmt.close();
            conn.close();
            return maxId;
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Exception on inserting url " + urlToInsert + e.getMessage());
            throw new RuntimeException("[ERROR] " + e.getMessage());
        }
    }

    public Integer getRecordCountInDB() {
        try {
            conn = DriverManager.getConnection(dbUrl);
            String query = "SELECT COUNT(*) AS count FROM urls";
            prstmt = conn.prepareStatement(query);
            ResultSet rs = prstmt.executeQuery();
            rs.next();
            int totalRecordCount = rs.getInt("count");
            logger.log(Level.INFO, "Total record count: " + totalRecordCount);
            prstmt.close();
            conn.close();
            return totalRecordCount;
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Exception on selecting all records from table: " + e.getMessage());
            throw new RuntimeException("[ERROR] " + e.getMessage());
        }
    }

    public Boolean isUrlExist(String url) {
        try {
            conn = DriverManager.getConnection(dbUrl);
            String query = "SELECT COUNT(*) AS count FROM urls WHERE url = ? ";
            prstmt = conn.prepareStatement(query);
            prstmt.setString(1, url);
            ResultSet rs = prstmt.executeQuery();
            rs.next();
            if (rs.getInt("count")>0) {
                logger.log(Level.INFO, "Url '" + url + "' already exists in table");
                return true;
            }
            else {
                logger.log(Level.INFO, "There is no url '" + url + "' in table");
                return false;
            }
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Exception on selecting count of urls " + url + e.getMessage());
            throw new RuntimeException("[ERROR] " + e.getMessage());
        }
        finally {
            try {
                prstmt.close();
                conn.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public void insertShortUrl(String longUrl, String shortUrl) {
        try {
            conn = DriverManager.getConnection(dbUrl);

            String query = "UPDATE urls SET shorturl = ? WHERE url = ?";
            prstmt = conn.prepareStatement(query);
            prstmt.setString(1, shortUrl);
            prstmt.setString(2, longUrl);
            prstmt.executeUpdate();

            logger.info("Inserted shortUrl '" + shortUrl + "' for " + longUrl);
            prstmt.close();
            conn.close();
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Exception on inserting shortUrl " + shortUrl  + "' for " + longUrl + e.getMessage());
            throw new RuntimeException("[ERROR] " + e.getMessage());
        }
    }

    public String selectUrlByShortUrl(String shortUrl) {
        try {
            conn = DriverManager.getConnection(dbUrl);


            String query = "SELECT url FROM urls where shorturl = ?";
            prstmt = conn.prepareStatement(query);
            prstmt.setString(1, shortUrl);
            ResultSet rs = prstmt.executeQuery();
            rs.next();
            String url = rs.getString("url");
            logger.log(Level.INFO, "Url '" + rs.getString("url") + "' has short url = " + shortUrl);
            prstmt.close();
            conn.close();
            return url;
        }
        catch (SQLException e) {
            logger.log(Level.INFO, "There is no shortUrl '" + shortUrl + "' in database " + shortUrl);
            return "shortUrl not found";
        }
    }

    public String selectShortUrlByLongUrl(String longUrl) {
        try {
            conn = DriverManager.getConnection(dbUrl);

            String query = "SELECT shortUrl FROM urls where url = ?";
            prstmt = conn.prepareStatement(query);
            prstmt.setString(1, longUrl);
            ResultSet rs = prstmt.executeQuery();
            rs.next();
            String shortUrl = rs.getString("shortUrl");
            logger.log(Level.INFO, "LongUrl '" + longUrl + "' has shortUrl '" + rs.getString("shortUrl"));
            prstmt.close();
            conn.close();
            return shortUrl;
        }
        catch (SQLException e) {
            return "longUrl not found";
        }
    }
}
