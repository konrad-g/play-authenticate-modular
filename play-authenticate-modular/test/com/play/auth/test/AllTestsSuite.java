package com.play.auth.test;

import com.digitaljetty.workjetty.controllers.gui.*;
import com.digitaljetty.workjetty.elements.gui.pages.search.list.jobs.HtmlPaginatorTest;
import com.digitaljetty.workjetty.elements.gui.pages.search.list.jobs.SearchJobsListTest;
import com.play.auth.test.controllers.AppControllerTest;
import com.play.auth.test.elements.auth.gui.login.PageAuthLoginTest;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/**
 * Created by Konrad Gadzinowski on 31/07/2015.
 */
@RunWith(Suite.class)
@Suite.SuiteClasses({
        AppControllerTest.class,
        PageAuthLoginTest.class
})

public class AllTestsSuite {

    private static double time;

    @BeforeClass
    public static void setUp() {
        AllTestsSuite.time = System.currentTimeMillis()/ 1000d;
    }

    @AfterClass
    public static void tearDown() {

        double timePassed = (System.currentTimeMillis()/ 1000d) - AllTestsSuite.time;
        System.out.println();
        System.out.println("== Time to execute the tests: " + String.valueOf(timePassed) + "s. ==");
    }

}