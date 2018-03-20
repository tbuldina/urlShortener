package com.urlShortener.db;

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

    public String dbUrl;
    public Connection conn;
    public Statement stmt;
    private static final Logger logger = Logger.getLogger(DbConnector.class.getName());

    public DbConnector(String dbUrl) {
        connectToDb(dbUrl);
        createTable();
    }

    public void connectToDb(String dbUrl) {
        try {
            conn = DriverManager.getConnection(dbUrl + ";create=true");
            stmt = conn.createStatement();

        } catch (SQLException e) {
            logger.log(Level.SEVERE, "Exception on connecting to database: " + e.getMessage());
            throw new RuntimeException("[ERROR] " + e.getMessage());
        }
        logger.log(Level.INFO, "Connected to database");
    }

    public void createTable() {
        try {
            stmt.executeUpdate("create table urls ("
                    + "id int not null generated always as identity, "
                    + "url varchar(300), "
                    + "shorturl varchar(300), "
                    + "primary key (id) "
                    + ")");
            stmt.executeUpdate("create index ix_url on urls (url)");
            stmt.executeUpdate("create index ix_shorturl on urls (shorturl)");

        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Exception on creating table: " + e.getMessage());
            throw new RuntimeException("[ERROR] " + e.getMessage());
        }
        logger.log(Level.INFO, "Created table and index");
    }

    public void insertUrl(String urlToInsert) {
        try {
            if (!isUrlExist(urlToInsert)) {
                stmt.executeUpdate("INSERT INTO urls (url) VALUES ('" + urlToInsert + "')");
                logger.info("Inserted url '" + urlToInsert + "'");
            }
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Exception on inserting url " + urlToInsert + e.getMessage());
            throw new RuntimeException("[ERROR] " + e.getMessage());
        }
    }

    public Integer getRecordCountInDB() {
        try {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM urls");
            rs.next();
            int totalRecordCount = rs.getInt("count");
            logger.log(Level.INFO, "Total record count: " + totalRecordCount);
            return totalRecordCount;
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Exception on selecting all records from table: " + e.getMessage());
            throw new RuntimeException("[ERROR] " + e.getMessage());
        }
    }

    public Boolean isUrlExist(String url) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) AS count FROM urls WHERE url = '" + url + "'");
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
    }

    public int selectIdByUrl(String url) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT id FROM urls WHERE url = '" + url + "'");

            if (rs.next())
                return rs.getInt("id");
            else
                return 0;
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Exception on selecting url " + url + e.getMessage());
            throw new RuntimeException("[ERROR] " + e.getMessage());
        }
    }

    public void insertShortUrl(String longUrl, String shortUrl) {
        try {
            stmt.executeUpdate("UPDATE urls SET shorturl = '" + shortUrl + "' WHERE url = '" + longUrl + "'");
            logger.info("Inserted shortUrl '" + shortUrl + "' for " + longUrl);
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Exception on inserting shortUrl " + shortUrl  + "' for " + longUrl + e.getMessage());
            throw new RuntimeException("[ERROR] " + e.getMessage());
        }
    }

    public String selectUrlByShortUrl(String shortUrl) {
        try {
            ResultSet rs = stmt.executeQuery("SELECT url FROM urls where shorturl = '" + shortUrl + "'");
            rs.next();
            logger.log(Level.INFO, "Url '" + rs.getString("url") + "' has short url = " + shortUrl);
            return rs.getString("url");
        }
        catch (SQLException e) {
            logger.log(Level.INFO, "There is no shortUrl '" + shortUrl + "' in database " + shortUrl);
            return "shortUrl not found";
        }
    }

    public void shutdown() {
        try {
            if (stmt != null) stmt.close();
            if (conn != null) {
                DriverManager.getConnection(dbUrl + ";shutdown=true");
                conn.close();
            }
        }
        catch (SQLException e) {
            logger.log(Level.SEVERE, "Exception on shutting down DB" + e.getMessage());
        }
        logger.log(Level.INFO, "DB shutted down successfully");
    }
}
