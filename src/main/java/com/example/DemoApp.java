package com.example;

import com.sun.net.httpserver.HttpServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class DemoApp {

    private static final Logger LOGGER =
            LoggerFactory.getLogger(DemoApp.class);

    public static final String STARTUP_MESSAGE =
            "End-to-End DevOps CI/CD Pipeline Deployment Successful";

    public static String getStartupMessage() {
        return STARTUP_MESSAGE;
    }
    public static String generateHtml() {
        return "<html><h1>" + STARTUP_MESSAGE + "</h1></html>";
    }

    public static byte[] loadHtml() throws Exception {

        InputStream htmlStream =
                DemoApp.class.getClassLoader().getResourceAsStream("index.html");

        return htmlStream.readAllBytes();
    }

    public static void startServer(int port) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/", exchange -> {

            try {

                byte[] response = loadHtml();

                exchange.getResponseHeaders().add("Content-Type", "text/html");
                exchange.sendResponseHeaders(200, response.length);

                OutputStream os = exchange.getResponseBody();
                os.write(response);
                os.close();

            } catch (Exception e) {

                String error = "Internal Server Error";

                exchange.sendResponseHeaders(500, error.length());
                OutputStream os = exchange.getResponseBody();
                os.write(error.getBytes());
                os.close();

            }

        });

        server.setExecutor(null);
        server.start();

        LOGGER.info("HTTP Server started on port {}", port);
    }

    public static void main(String[] args) throws Exception {

        startServer(9090);

    }

}
