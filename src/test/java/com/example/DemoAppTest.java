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
    public void testHtmlResponse() {

        String html = DemoApp.getHtmlResponse();

        assertTrue(html.contains("DevOps"));
    }
}
