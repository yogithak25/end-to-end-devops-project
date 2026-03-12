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
    public void testStartupMessageNotNull() {

        assertNotNull(DemoApp.getStartupMessage());
    }

    @Test
    public void testGenerateHtmlContainsMessage() {

        String html = DemoApp.generateHtml();

        assertTrue(html.contains("End-to-End DevOps CI/CD Pipeline Deployment Successful"));
    }

    @Test
    public void testGenerateHtmlStructure() {

        String html = DemoApp.generateHtml();

        assertTrue(html.startsWith("<html>"));
        assertTrue(html.contains("<h1>"));
    }

    @Test
    public void testLoadHtml() throws Exception {

        byte[] htmlBytes = DemoApp.loadHtml();

        assertNotNull(htmlBytes);
        assertTrue(htmlBytes.length > 0);
    }

}
