package com.play.auth.test;

import com.play.auth.elements.i18n.I18NModule;
import org.junit.After;
import org.junit.Before;
import play.Application;
import play.Mode;
import play.inject.guice.GuiceApplicationBuilder;
import play.mvc.Http;
import com.play.auth.AppLoader;

import java.util.Collections;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static play.test.Helpers.fakeRequest;

/**
 * Created by Konrad Gadzinowski on 31/07/2015.
 */
public class BaseTest {

    public Application application;
    private double time;

    @Before
    public void setUp() {
        this.time = System.currentTimeMillis()/ 1000d;
        System.setProperty("config.resource", "application-test.conf");
        setContext();
        this.application = createTestApp();
    }

    @After
    public void tearDown() {

        double timePassed = (System.currentTimeMillis()/ 1000d) - this.time;

        System.out.println("-- OK -- Time: " + String.valueOf(timePassed) + "s. --");
    }

    private void setContext() {
        Map<String, String> flashData = Collections.emptyMap();
        Map<String, Object> argData = Collections.emptyMap();
        Long id = 2L;
        play.api.mvc.RequestHeader header = mock(play.api.mvc.RequestHeader.class);
        Http.Context context = new Http.Context(id, header, fakeRequest().build(), flashData, flashData, argData);
        Http.Context.current.set(context);
    }

    private Application createTestApp() {

        AppLoader loader = new AppLoader();

        GuiceApplicationBuilder builder = new GuiceApplicationBuilder()
                .in(Mode.TEST);
        builder = I18NModule.setupI18N(builder, loader.getDefaultLang(), loader.getI18nFiles());

        Application application = builder.build();

        return application;
    }

}
