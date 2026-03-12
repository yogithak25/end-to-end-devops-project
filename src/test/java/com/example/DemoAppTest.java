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
    public void testGenerateHtml() {
        String html = DemoApp.generateHtml();
        assertTrue(html.contains("DevOps"));
    }

    @Test
    public void testGenerateHtmlNotNull() {
        assertNotNull(DemoApp.generateHtml());
    }

}
