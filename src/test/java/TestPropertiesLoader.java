import com.urlShortener.utils.PropertiesLoader;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Created by tbuldina on 28/03/2018.
 */
/*
 * Test properties loaded from test/resources/config.properties
 */
public class TestPropertiesLoader {

    PropertiesLoader properties = new PropertiesLoader();

    @Test
    public void testDefaultJettyPort() {
        assertEquals("Default Jetty port is not correct", "1010", properties.getJettyPort().toString());
    }

    @Test
    public void testJettyHost() {
        assertEquals("Jetty host is not correct", "test_localhost", properties.getJettyHost().toString());
    }

    @Test
    public void testDbUrl() {
        assertEquals("DatabaseUrl is not correct", "jdbc:derby:memory:test_url_database;create=true", properties.getDatabaseUrl().toString());
    }

    @Test
    public void testSettingJettyPort() {
        properties.setJettyPort("7080");
        assertEquals("Jetty port set not correctly", "7080", properties.getJettyPort().toString());
    }

}