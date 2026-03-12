package com.example;

import org.junit.Test;

import static org.junit.Assert.*;

public class DemoAppTest {

    @Test
    public void testStartupMessage() {

        assertEquals(
                "End-to-End DevOps CI/CD Pipeline Deployment Successful",
                DemoApp.getStartupMessage()
        );

    }

    @Test
    public void testLoadHtml() throws Exception {

        byte[] html = DemoApp.loadHtml();

        assertNotNull(html);
        assertTrue(html.length > 0);

    }

    @Test
    public void testServerStarts() throws Exception {

        Thread serverThread = new Thread(() -> {

            try {
                DemoApp.startServer(9095);
            } catch (Exception ignored) {
            }

        });

        serverThread.start();

        Thread.sleep(1000);

        assertTrue(serverThread.isAlive());

    }

}
