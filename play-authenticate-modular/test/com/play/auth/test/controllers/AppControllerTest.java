package com.play.auth.test.controllers;

import com.play.auth.test.BaseTest;
import org.junit.*;

import play.mvc.*;

import static play.test.Helpers.*;
import static org.junit.Assert.*;


/**
*
* Simple (JUnit) tests that can call all parts of a play app.
* If you are interested in mocking a whole application, see the wiki for more details.
*
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

                // Given I'm anonymous
                // When I call main page
                /*
                MainController mainController = new MainController();

                // Then I receive page
                Result result =  mainController.indexI18N("", "", "", 0, Configuration.DEFAULT_LANGUAGE_CODE);
                assertEquals(200, result.status());
                assertEquals("text/html", result.contentType());

                assertTrue(contentAsString(result).contains("<h3>All jobs in one place</h3>"));
                */
            }
        });
    }

    /**
     * Given I'm anonymous
     * When I call profile page
     * Then I can't access it
     */
    public void profileNotLoggedIn() {
        running(this.application, new Runnable() {
            public void run() {


            }
        });
    }

    /**
     * Given I'm logged int
     * When I call profile page
     * Then I get it
     */
    public void profileLoggedIn() {
        running(this.application, new Runnable() {
            public void run() {


            }
        });
    }
}
