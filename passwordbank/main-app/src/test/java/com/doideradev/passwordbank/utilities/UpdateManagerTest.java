package com.doideradev.passwordbank.utilities;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.sun.net.httpserver.HttpServer;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.InetSocketAddress;
import java.net.URL;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;



public class UpdateManagerTest {

    private HttpURLConnection mockConn;
    private URL mockUrl;

    @BeforeEach
    void setup() throws Exception {
        // Create mocks before each test
        mockConn = mock(HttpURLConnection.class);
        mockUrl = mock(URL.class);
        when(mockUrl.openConnection()).thenReturn(mockConn);
    }

    
    @Test
    void testUpdateAvailable() throws Exception {
        AppConfigs configs = new AppConfigs("v1.0.0");
        
        HttpServer server = HttpServer.create(new InetSocketAddress(0), 0);
        server.createContext("/latest", exchange -> {
            String fakeJson = "{ \"tag_name\":\"v2.0.0\", \"assets\":[{\"browser_download_url\":\"http://example.com/app.jar\"}] }";
            exchange.sendResponseHeaders(200, fakeJson.length());
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(fakeJson.getBytes());
            }
        });
        server.start();
        
        // Point configs to fake server URL
        String url = "http://localhost:" + server.getAddress().getPort() + "/latest";
        
        // Prepare configs
        configs.setReleaseURL(url);

        // Call method with mocked URL
        UpdaterManager.lookForUpdates(configs);

        // Assertions (after thread runs)
        Thread.sleep(2500); // wait briefly for background thread
        assertTrue(UpdaterManager.updateAvailable, "Update flag should be true");
        assertEquals("http://example.com/app.jar", configs.getDownloadURL());

        server.stop(0);
    }


}