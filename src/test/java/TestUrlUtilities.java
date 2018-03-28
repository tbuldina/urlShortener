import com.urlShortener.utils.UrlUtilities;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Created by tbuldina on 28/03/2018.
 */
public class TestUrlUtilities {

    @Test
    public void testEmptyUrl() {
        assertTrue("Url '' is not empty", UrlUtilities.isUrlEmpty(""));
    }

    @Test
    public void testUrlFromOneWhitespace() {
        assertTrue("Url ' ' is not empty", UrlUtilities.isUrlEmpty(" "));
    }

    @Test
    public void testNullUrl() {
        assertTrue("Null url is not empty", UrlUtilities.isUrlEmpty(null));
    }

    @Test
    public void testUrlWithWhitespaces() {
        assertFalse("Url ' n ' is empty", UrlUtilities.isUrlEmpty(" n "));
    }

    @Test
    public void testClickableUrlWithoutProtocol() {
        assertEquals("'apple.com' is not transformed to clickable", "http://apple.com", UrlUtilities.makeUrlClickable("apple.com"));
    }

    @Test
    public void testClickableUrlWithHttp() {
        assertEquals("'http://apple.com' is changed, but should not", "http://apple.com", UrlUtilities.makeUrlClickable("http://apple.com"));
    }

    @Test
    public void testClickableUrlWithHttps() {
        assertEquals("'https://apple.com' is changed, but should not", "https://apple.com", UrlUtilities.makeUrlClickable("https://apple.com"));
    }

}