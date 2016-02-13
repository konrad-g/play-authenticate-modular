package com.play.auth.test.controllers;

import com.digitaljetty.workjetty.BaseTest;
import com.digitaljetty.workjetty.Configuration;
import com.digitaljetty.workjetty.controllers.gui.MainController;
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
     * Then I receive page
     *
     * @author Konrad Gadzinowski
     */
    @Test
    public void index() {

        running(fakeApplication(), new Runnable() {
            public void run() {

                // Given I'm anonymous
                // When I call main page
                MainController mainController = new MainController();

                // Then I receive page
                Result result =  mainController.indexI18N("", "", "", 0, Configuration.DEFAULT_LANGUAGE_CODE);
                assertEquals(200, result.status());
                assertEquals("text/html", result.contentType());

                assertTrue(contentAsString(result).contains("<h3>All jobs in one place</h3>"));
            }
        });
    }

    /**
     * Given I'm anonymous
     * When I call about page
     * Then I receive page
     *
     * @author Konrad Gadzinowski
     */
    @Test
    public void about() {

        running(fakeApplication(), new Runnable() {

            @Override
            public void run() {

                // Given I'm anonymous
                // When I call about page
                MainController mainController = new MainController();

                // Then I receive page
                Result result =  mainController.about(Configuration.DEFAULT_LANGUAGE_CODE);
                assertEquals(200, result.status());
                assertEquals("text/html", result.contentType());

                assertTrue(contentAsString(result).contains("<h3>About</h3>"));
            }
        });

    }

    /**
     *
     *
     *
     *
     * @author Konrad Gadzinowski
     */
    @Test
    public void contact() {

        // TODO: Add
    }

    /**
     * Given I'm anonymous
     * When I call write to us page
     * Then I receive page with all fields
     *
     * @author Konrad Gadzinowski
     */
    @Test
    public void writeToUs() {

        running(fakeApplication(), new Runnable() {

            @Override
            public void run() {
                /*
                // TODO: Fix
                // Given I'm anonymous

                // When I call write to us page
                MainController mainController = new MainController();

                // Then I receive page with all fields
                Result result =  mainController.writeToUs(Configuration.DEFAULT_LANGUAGE_CODE);
                assertEquals(200, result.status());
                assertEquals("text/html", result.contentType());

                assertTrue(contentAsString(result).contains("<h3>Contact</h3>"));
                assertTrue(contentAsString(result).contains("<label for=\"name\">Name</label>"));
                assertTrue(contentAsString(result).contains("<label for=\"company\">Company Name</label>"));
                assertTrue(contentAsString(result).contains("<label for=\"title\">Title</label>"));
                assertTrue(contentAsString(result).contains("<label for=\"email\">Email</label>"));
                assertTrue(contentAsString(result).contains("<label for=\"message\">Message</label>"));
                assertTrue(contentAsString(result).contains("<label for=\"humanTestAnswer\">"));
                */
            }
        });

    }

    /**
     * Given I'm anonymous
     * When I call change log page
     * Then I receive page
     *
     * @author Konrad Gadzinowski
     */
    @Test
    public void changeLog() {

        running(fakeApplication(), new Runnable() {

            @Override
            public void run() {
                // Given I'm anonymous
                // When I call change log page
                MainController mainController = new MainController();

                // Then I receive page
                Result result =  mainController.changeLog(Configuration.DEFAULT_LANGUAGE_CODE);
                assertEquals(200, result.status());
                assertEquals("text/html", result.contentType());

                assertTrue(contentAsString(result).contains("<h3>Change log</h3>"));
            }
        });

    }

    /**
     * Given I'm anonymous
     * When I call pricing page
     * Then I receive page
     *
     * @author Konrad Gadzinowski
     */
    @Test
    public void pricing() {

        running(fakeApplication(), new Runnable() {

            @Override
            public void run() {

                // Given I'm anonymous
                // When I call pricing page
                MainController mainController = new MainController();

                // Then I receive page
                Result result =  mainController.pricing(Configuration.DEFAULT_LANGUAGE_CODE);
                assertEquals(200, result.status());
                assertEquals("text/html", result.contentType());

                assertTrue(contentAsString(result).contains("<h3>Pricing</h3>"));
            }
        });

    }

    /**
     * Given I'm anonymous
     * When I call privacy policy page
     * Then I receive page
     *
     * @author Konrad Gadzinowski
     */
    @Test
    public void privacy() {

        running(fakeApplication(), new Runnable() {

            @Override
            public void run() {

                // Given I'm anonymous
                // When I call privacy policy page
                MainController mainController = new MainController();

                // Then I receive page
                Result result =  mainController.privacy(Configuration.DEFAULT_LANGUAGE_CODE);
                assertEquals(200, result.status());
                assertEquals("text/html", result.contentType());

                assertTrue(contentAsString(result).contains("<h3>Privacy Policy</h3>"));
            }
        });

    }

    /**
     * Given I'm anonymous
     * When I call terms & conditions page
     * Then I receive page
     *
     * @author Konrad Gadzinowski
     */
    @Test
    public void terms() {

        running(fakeApplication(), new Runnable() {

            @Override
            public void run() {

                // Given I'm anonymous
                // When I call terms & conditions page
                MainController mainController = new MainController();

                // Then I receive page
                Result result =  mainController.terms(Configuration.DEFAULT_LANGUAGE_CODE);
                assertEquals(200, result.status());
                assertEquals("text/html", result.contentType());

                assertTrue(contentAsString(result).contains("<h3>Terms and Conditions</h3>"));
            }
        });

    }
}
