package com.play.auth.test.elements.auth.gui.login;

import com.digitaljetty.workjetty.BaseTest;
import com.digitaljetty.workjetty.controllers.gui.BaseController;
import com.digitaljetty.workjetty.elements.auth.main.EntryUser;
import com.digitaljetty.workjetty.elements.gui.pages.search.list.talents.PageTalentsList;
import com.digitaljetty.workjetty.elements.gui.pages.search.list.talents.models.ModelTalentsResult;
import com.digitaljetty.workjetty.elements.location.EntryLocation;
import com.digitaljetty.workjetty.elements.location.Location;
import com.digitaljetty.workjetty.elements.session.Session;
import com.digitaljetty.workjetty.elements.storage.Entry;
import com.digitaljetty.workjetty.elements.talent.EntryTalent;
import com.digitaljetty.workjetty.elements.talent.Talent;
import com.play.auth.test.BaseTest;
import org.junit.Assert;
import org.junit.Test;
import play.mvc.Controller;

import java.util.ArrayList;

import static play.test.Helpers.fakeApplication;
import static play.test.Helpers.inMemoryDatabase;
import static play.test.Helpers.running;

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
     * Then I see empty form
     */
    @Test
    public void loginFirstGlimpse() {

        running(this.application, new Runnable() {
            public void run() {


            }
        });
    }

    /**
     * Given I'm anonymous
     * When I visit login page for the first time and I don;t fill it completely
     * And submit form
     * Then I see error and data I put is there
     */
    @Test
    public void loginError() {

        running(this.application, new Runnable() {
            public void run() {


            }
        });
    }
}