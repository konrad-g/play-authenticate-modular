package com.play.auth.test.controllers;

import com.play.auth.controllers.AppController;
import com.play.auth.test.BaseTest;
import org.junit.*;

import play.mvc.*;

import static play.test.Helpers.*;
import static org.junit.Assert.*;


/**
 * Simple (JUnit) tests that can call all parts of a play app.
 * If you are interested in mocking a whole application, see the wiki for more details.
 */
public class AppControllerTest extends BaseTest {

    /**
     * Given I'm anonymous
     * When I call main page
     * Then I it
     */
    @Test
    public void index() {

        running(this.application, new Runnable() {
            public void run() {

                AppController appController = new AppController();

                Result result = appController.index();
                assertEquals(200, result.status());
                assertEquals("text/html", result.contentType());

                assertTrue(contentAsString(result).contains("This is a template for a simple application with authentication."));
            }
        });
    }

    /**
     * Given I'm anonymous
     * When I call profile page
     * Then I can't access it
     */
    @Test
    public void profileNotLoggedIn() {
        running(this.application, new Runnable() {
            public void run() {

                AppController appController = new AppController();

                Result result = appController.profile();
                assertEquals(403, result.status());
                assertEquals(null, result.contentType());
            }
        });
    }
}
