package com.play.auth.test.elements.auth.gui.login;

import com.play.auth.controllers.AppController;
import com.play.auth.elements.auth.gui.login.PageAuthLogin;
import com.play.auth.test.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.ArrayList;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static play.test.Helpers.*;

/**
 * Created by gadzinow on 15/12/15.
 */
public class PageAuthLoginTest extends BaseTest {

    @Override
    public void setUp() {
        super.setUp();
    }

    /**
     * Given I'm anonymous
     * When I visit login page for the first time
     * Then I see login form
     */
    @Test
    public void loginFirstGlimpse() {

        running(this.application, new Runnable() {
            public void run() {

                AppController controller = new AppController();

                PageAuthLogin page = new PageAuthLogin(controller.getSession(), controller.getOnRenderListener());

                Result result = page.renderLogin();
                assertEquals(200, result.status());
                assertEquals("text/html", result.contentType());

                assertTrue(contentAsString(result).contains("Login"));
            }
        });
    }
}