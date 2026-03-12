package com.example;

import com.sun.net.httpserver.HttpServer;
import org.junit.Test;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import static org.junit.Assert.*;

public class DemoAppTest {

    @Test
    public void testStartupMessage() {

        String msg = DemoApp.getStartupMessage();

        assertEquals(
                "End-to-End DevOps CI/CD Pipeline Deployment Successful",
                msg
        );
    }

    @Test
    public void testLoadHtml() throws Exception {

        byte[] html = DemoApp.loadHtml();

        assertNotNull(html);
        assertTrue(html.length > 0);
    }

    @Test
    public void testHttpEndpoint() throws Exception {

        int port = 9099;

        HttpServer server = DemoApp.startServer(port);

        URL url = new URL("http://localhost:" + port + "/");

        HttpURLConnection conn =
                (HttpURLConnection) url.openConnection();

        conn.setRequestMethod("GET");

        BufferedReader reader =
                new BufferedReader(
                        new InputStreamReader(conn.getInputStream()));

        String line = reader.readLine();

        reader.close();

        assertNotNull(line);

        server.stop(0);
    }
}
