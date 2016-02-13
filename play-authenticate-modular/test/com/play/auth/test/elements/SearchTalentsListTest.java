package com.play.auth.test.elements;

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
public class SearchTalentsListTest extends BaseTest {

    /*
    Jobs numbers
                        |D	|M	|
    locationUKEdinburgh	|113|254|
    locationDEMunich	|64	|354|
    locationPLLodz		|305|453|
    					---------
    					|482|1061| = 1543

    locationUS			|1433|1323|
    locationAU			|1233|1325|
    locationCA			|1353|245|
     */

    private EntryLocation locationUKEdinburgh;
    private EntryLocation locationDEMunich;
    private EntryLocation locationPLLodz;
    private EntryLocation locationUS;
    private EntryLocation locationAU;
    private EntryLocation locationCA;


    @Override
    public void setUp() {
        super.setUp();
    }

    private void setupDatabase() {
        clearDatabase();
        initCommonData();
    }

    /**
     * Given no talents
     * When I add talents from all regions
     * And I fetch talent from EU
     * Then I get only talents from this region
     * And pagination is properly formatted - there are many pages
     *
     * @author Konrad Gadzinowski
     */
    @Test
    public void testTalentRegionsEU() {

        running(this.application, new Runnable() {
            public void run() {

                setupDatabase();

                // Given no talents
                Assert.assertEquals(0, Talent.count("", "", false, false));

                // When I add talents from all regions
                addTalents();


                // And I fetch talent from EU
                Controller controller = new BaseController();
                Session session = new Session(controller);
                session.changeLang("en-EU");

                Assert.assertEquals(1543, Talent.count("", "", false, false));

                // Then I get only talents from this region
                // And pagination is properly formatted - there are many pages
                PageTalentsList talentsPage = new PageTalentsList(session);

                int expectedPages = 10;
                for (int i = 0; i < expectedPages; i++) {

                    ModelTalentsResult talent = talentsPage.getTalents("Developer", "", false, false, i);
                    Assert.assertEquals(482, talent.talentsTotalCount);
                    Assert.assertEquals(i, talent.paginator.currentPage);
                    Assert.assertEquals(expectedPages, talent.paginator.totalPages);

                    if(i!= expectedPages - 1) {
                        Assert.assertEquals(50, talent.talents.size());
                    } else {
                        Assert.assertEquals(32, talent.talents.size());
                    }


                    for (int j = 0; j < talent.talents.size(); j++) {
                        EntryTalent talentRow = talent.talents.get(j);
                        Assert.assertTrue(talentRow.location.nameFormatted.contains(", UK") || talentRow.location.nameFormatted.contains(", DE") || talentRow.location.nameFormatted.contains(", PL"));
                        Assert.assertTrue(talentRow.profession.contains("Developer"));
                    }
                }

                expectedPages = 6;
                for (int i = 0; i < expectedPages; i++) {

                    ModelTalentsResult talent = talentsPage.getTalents("Manager", "Edinbur", false, false, i);
                    Assert.assertEquals(254, talent.talentsTotalCount);
                    Assert.assertEquals(expectedPages, talent.paginator.totalPages);
                    Assert.assertEquals(i, talent.paginator.currentPage);

                    if(i!= expectedPages - 1) {
                        Assert.assertEquals(50, talent.talents.size());
                    } else {
                        Assert.assertEquals(4, talent.talents.size());
                    }

                    for (int j = 0; j < talent.talents.size(); j++) {
                        EntryTalent talentRow = talent.talents.get(j);
                        Assert.assertTrue(talentRow.location.nameFormatted.contains(", UK") || talentRow.location.nameFormatted.contains(", DE") || talentRow.location.nameFormatted.contains(", PL"));
                        Assert.assertTrue(talentRow.profession.contains("Manager"));
                    }
                }
            }
        });
    }

    /**
     * Given no talents
     * When I add it
     * And I fetch talent from US who are willing to relocate
     * Then I get only people who are willing to relocate
     * When I get people willing to relocate and work remotely
     * Then I get them
     *
     * @author Konrad Gadzinowski
     */
    @Test
    public void testTalentRegionsUS() {

        running(this.application, new Runnable() {
            public void run() {

                setupDatabase();

                // Given no talents
                Assert.assertEquals(0, Talent.count("", "", false, false));

                // When I add it
                addTalents();

                Controller controller = new BaseController();
                Session session = new Session(controller);
                session.changeLang("en-US");

                Assert.assertEquals(2756, Talent.count("", "", false, false));

                // And I fetch talent from US who are willing to relocate
                PageTalentsList talentsPage = new PageTalentsList(session);
                ModelTalentsResult talent = talentsPage.getTalents("", "", true, false, 0);


                for (int i = 0; i < talent.talents.size(); i++) {
                    // Then I get only people who are willing to relocate
                    EntryTalent talentRow  = talent.talents.get(i);
                    Assert.assertTrue(talentRow.isWillingToRelocate);
                    Assert.assertTrue(talentRow.location.nameFormatted.contains(", CA"));
                }

                // When I get people willing to relocate and work remotely
                talent = talentsPage.getTalents("", "", true, true, 0);

                // Then I get them
                Assert.assertTrue(talent.talents.size() > 0);
                for (int i = 0; i < talent.talents.size(); i++) {
                    EntryTalent talentRow  = talent.talents.get(i);
                    Assert.assertTrue(talentRow.isWillingToRelocate);
                    Assert.assertTrue(talentRow.isWillingToWorkRemotely);
                    Assert.assertTrue(talentRow.location.nameFormatted.contains(", CA"));
                }

            }
        });
    }

    /**
     * Given no talents
     * When I add talents from all regions
     * And I fetch talent from AU who are willing to relocate
     * Then I get only people who are willing to relocate
     * When I get people willing to relocate and work remotely
     * Then I get them
     *
     * @author Konrad Gadzinowski
     */
    @Test
    public void testTalentRegionsAU() {

        running(this.application, new Runnable() {
            public void run() {

                setupDatabase();

                // Given no talents
                Assert.assertEquals(0, Talent.count("", "", false, false));

                // When I add talents from all regions
                addTalents();

                Controller controller = new BaseController();
                Session session = new Session(controller);
                session.changeLang("en-AU");

                Assert.assertEquals(2558, Talent.count("", "", false, false));

                // And I fetch talent from AU who are willing to relocate
                PageTalentsList talentsPage = new PageTalentsList(session);
                ModelTalentsResult talent = talentsPage.getTalents("", "", true, false, 0);


                for (int i = 0; i < talent.talents.size(); i++) {
                    // Then I get only people who are willing to relocate
                    EntryTalent talentRow  = talent.talents.get(i);
                    Assert.assertTrue(talentRow.isWillingToRelocate);
                    Assert.assertTrue(talentRow.location.nameFormatted.contains(", NSW"));
                }

                // When I get people willing to relocate and work remotly
                talent = talentsPage.getTalents("", "", true, true, 0);

                // Then I get them
                Assert.assertTrue(talent.talents.size() > 0);
                for (int i = 0; i < talent.talents.size(); i++) {
                    EntryTalent talentRow  = talent.talents.get(i);
                    Assert.assertTrue(talentRow.isWillingToRelocate);
                    Assert.assertTrue(talentRow.isWillingToWorkRemotely);
                    Assert.assertTrue(talentRow.location.nameFormatted.contains(", NSW"));
                }
            }
        });
    }

    private void initCommonData() {

        this.locationUKEdinburgh = new EntryLocation(
                Location.getState("GB", "GB-SCT"),
                "Edinburgh",
                "Edinburgh, UK",
                -3.184061,
                55.951126
        );
        new Entry(this.locationUKEdinburgh).save();

        this.locationDEMunich = new EntryLocation(
                Location.getState("DE", ""),
                "Munich",
                "Munich, DE",
                11.568270,
                48.146643
        );
        new Entry(this.locationDEMunich).save();

        this.locationPLLodz = new EntryLocation(
                Location.getState("PL", ""),
                "Lodz",
                "Lodz, PL",
                19.461365,
                51.770973
        );
        new Entry(this.locationPLLodz).save();

        this.locationUS = new EntryLocation(
                Location.getState("US", "CA"),
                "San Francisco",
                "San Francisco, CA",
                -122.457513,
                37.755930
        );
        new Entry(this.locationUS).save();

        this.locationAU = new EntryLocation(
                Location.getState("AU", "NSW"),
                "Sydney",
                "Sydney, NSW",
                151.102307,
                -33.887855
        );
        new Entry(this.locationAU).save();

        this.locationCA = new EntryLocation(
                Location.getState("CA", "QC"),
                "Montreal",
                "Montreal, QC",
                -73.618839,
                45.549943
        );
        new Entry(this.locationCA).save();
    }

    private void addTalents() {

        for (int i = 0; i < 113; i++) {
            addTalent("Developer", locationUKEdinburgh, Math.random() >= 0.5 ? true : false, Math.random() >= 0.5 ? true : false);
        }

        for (int i = 0; i < 254; i++) {
            addTalent("Manager", locationUKEdinburgh, Math.random() >= 0.5 ? true : false, Math.random() >= 0.5 ? true : false);
        }

        for (int i = 0; i < 64; i++) {
            addTalent("Developer", locationDEMunich, Math.random() >= 0.5 ? true : false, Math.random() >= 0.5 ? true : false);
        }

        for (int i = 0; i < 354; i++) {
            addTalent("Manager", locationDEMunich, Math.random() >= 0.5 ? true : false, Math.random() >= 0.5 ? true : false);
        }

        for (int i = 0; i < 305; i++) {
            addTalent("Developer", locationPLLodz, Math.random() >= 0.5 ? true : false, Math.random() >= 0.5 ? true : false);
        }

        for (int i = 0; i < 453; i++) {
            addTalent("Manager", locationPLLodz, Math.random() >= 0.5 ? true : false, Math.random() >= 0.5 ? true : false);
        }

        for (int i = 0; i < 1433; i++) {
            addTalent("Developer", locationUS, Math.random() >= 0.5 ? true : false, Math.random() >= 0.5 ? true : false);
        }

        for (int i = 0; i < 1323; i++) {
            addTalent("Manager", locationUS, Math.random() >= 0.5 ? true : false, Math.random() >= 0.5 ? true : false);
        }

        for (int i = 0; i < 1233; i++) {
            addTalent("Developer", locationAU, Math.random() >= 0.5 ? true : false, Math.random() >= 0.5 ? true : false);
        }

        for (int i = 0; i < 1325; i++) {
            addTalent("Manager", locationAU, Math.random() >= 0.5 ? true : false, Math.random() >= 0.5 ? true : false);
        }

        for (int i = 0; i < 1353; i++) {
            addTalent("Developer", locationCA, Math.random() >= 0.5 ? true : false, Math.random() >= 0.5 ? true : false);
        }

        for (int i = 0; i < 245; i++) {
            addTalent("Manager", locationCA, Math.random() >= 0.5 ? true : false, Math.random() >= 0.5 ? true : false);
        }

    }

    private void addTalent(
            String profession,
            EntryLocation location,
            boolean isWillingToRelocate,
            boolean isWillingToWorkRemotely) {
        EntryUser user = new EntryUser();
        new Entry(user).save();

        EntryTalent talent = new EntryTalent(
                user,
                profession,
                location,
                true,
                Math.random() >= 0.5 ? true : false,
                Math.random() >= 0.5 ? true : false,
                EntryTalent.EmploymentSearchStatus.ACTIVE,
                new ArrayList<>(),
                new ArrayList<>()
        );
        new Entry(talent).save();

    }
}