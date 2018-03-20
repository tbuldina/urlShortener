import com.urlShortener.db.DbConnector;
import com.urlShortener.utils.PropertiesLoader;
import org.junit.*;
import com.urlShortener.shortener.UrlShortener;
import static com.urlShortener.embeddedJetty.EmbeddedJettyServer.dbConnector;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * Created by tbuldina on 16/03/2018.
 */
public class TestUrlShortener {
    UrlShortener urlShortener = new UrlShortener();

    @BeforeClass
    public static void connectToDb() {
        PropertiesLoader prop = new PropertiesLoader();
        dbConnector = new DbConnector(prop.getDatabaseUrl());
    }

    // Encoding
    @Test
    public void testTheSameUrls() {
        assertEquals("The same urls have different short urls",
                urlShortener.encodeUrl("theSameUrl"), urlShortener.encodeUrl("theSameUrl"));
    }

    @Test
    public void testTheSameUrlDbCount() {
        urlShortener.encodeUrl("theSameUrlDbCount");
        int countBefore = dbConnector.getRecordCountInDB();

        urlShortener.encodeUrl("theSameUrlDbCount");
        int countAfter = dbConnector.getRecordCountInDB();

        assertEquals("Record count actually increased after encoding the same urls, but should not",
                countBefore, countAfter);
    }

    // Register
    @Test
    public void testUrlsDifferentRegister() {
        assertEquals("Urls in different register have different short urls, but should not",
                urlShortener.encodeUrl("verifyregister"), urlShortener.encodeUrl("VeriFyRegister"));
    }

    @Test
    public void testUrlsDifferentRegisterUrlDbCount() {
        urlShortener.encodeUrl("register");
        int countBefore = dbConnector.getRecordCountInDB();

        urlShortener.encodeUrl("rEgIsTeR");
        int countAfter = dbConnector.getRecordCountInDB();

        assertEquals("Record count actually increased after encoding the urls in different register, but should not",
                countBefore, countAfter);
    }

    // Whitespaces
    @Test
    public void testUrlsWithLeadingWhitespace() {
        assertEquals("Leading whitespace actually affects short url",
                urlShortener.encodeUrl("leadingWhitespace"), urlShortener.encodeUrl(" leadingWhitespace"));
    }

    @Test
    public void testUrlWithLeadingWhitespaceDbCount() {
        urlShortener.encodeUrl("leadingWhitespaceDbCount");
        int countBefore = dbConnector.getRecordCountInDB();

        urlShortener.encodeUrl(" leadingWhitespaceDbCount");
        int countAfter = dbConnector.getRecordCountInDB();

        assertEquals("Record count actually increased after encoding url with leading whitespace, but should not",
                countBefore, countAfter);
    }

    @Test
    public void testUrlsWithTrailingWhitespace() {
        assertEquals("Trailing whitespace actually affects short url",
                urlShortener.encodeUrl("trailingWhitespace"), urlShortener.encodeUrl("trailingWhitespace "));
    }

    @Test
    public void testUrlsWithTrailingWhitespaceDbCount() {
        urlShortener.encodeUrl("trailingWhitespaceDbCount");
        int countBefore = dbConnector.getRecordCountInDB();

        urlShortener.encodeUrl("trailingWhitespaceDbCount ");
        int countAfter = dbConnector.getRecordCountInDB();

        assertEquals("Record count actually increased after encoding url with trailing whitespace, but should not",
                countBefore, countAfter);
    }

    @Test
    public void testUrlsWithLeadingAndTrailingWhitespaces() {
        assertEquals("Leading and trailing whitespace actually affects short url",
                urlShortener.encodeUrl("leadingAndTrailingWhitespaces"), urlShortener.encodeUrl(" leadingAndTrailingWhitespaces "));
    }

    // Empty url
    @Test
    public void testEmptyUrl() {
        assertEquals("Empty Url should not be encoded", "", urlShortener.encodeUrl(""));
    }

    @Test
    public void testEmptyUrlDbCount() {
        int countBefore = dbConnector.getRecordCountInDB();

        urlShortener.encodeUrl("");
        int countAfter = dbConnector.getRecordCountInDB();

        assertEquals("Record count actually increased after encoding empty url, but should not",
                countBefore, countAfter);
    }

    @Test
    public void testUrlWhitespace() {
        assertEquals("Url from one whitespace should not be encoded", "", urlShortener.encodeUrl(" "));
    }

    @Test
    public void testUrlWhitespaceDbCount() {
        int countBefore = dbConnector.getRecordCountInDB();

        urlShortener.encodeUrl(" ");
        int countAfter = dbConnector.getRecordCountInDB();

        assertEquals("Record count actually increased after encoding url from whitespace, but should not",
                countBefore, countAfter);
    }

    @Test
    public void testUrlFromTwoWhitespace() {
        assertEquals("Url from two whitespace should not be encoded", "", urlShortener.encodeUrl("  "));
    }

    @Test
    public void testUrlTwoWhitespacesDbCount() {
        int countBefore = dbConnector.getRecordCountInDB();

        urlShortener.encodeUrl("  ");
        int countAfter = dbConnector.getRecordCountInDB();

        assertEquals("Record count actually increased after encoding url from two whitespaces, but should not",
                countBefore, countAfter);
    }

    // http - https - no protocol
    @Test
    public void testUrlsWithProtocolPart() {
        assertNotEquals("Urls with http:// and without actually resulted the same short urls, but should not",
                urlShortener.encodeUrl("http://example"), urlShortener.encodeUrl("example"));
    }

    @Test
    public void testUrlsWithDifferentProtocol() {
        assertNotEquals("Urls with http:// and https:// actually resulted the same short urls, but should not",
                urlShortener.encodeUrl("http://example"), urlShortener.encodeUrl("https://example"));
    }

    @AfterClass
    public static void shutDownDb() {
        dbConnector.shutdown();
    }
}