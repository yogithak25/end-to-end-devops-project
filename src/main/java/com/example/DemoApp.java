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

    public static void main(String[] args) throws Exception {

        HttpServer server = HttpServer.create(new InetSocketAddress(9090), 0);

        server.createContext("/", exchange -> {

            InputStream htmlStream =
                    DemoApp.class.getClassLoader().getResourceAsStream("index.html");

            byte[] response = htmlStream.readAllBytes();

            exchange.getResponseHeaders().add("Content-Type", "text/html");
            exchange.sendResponseHeaders(200, response.length);

            OutputStream os = exchange.getResponseBody();
            os.write(response);
            os.close();
        });

        server.setExecutor(null);
        server.start();

        LOGGER.info("HTTP Server started on port 9090");
    }
}
