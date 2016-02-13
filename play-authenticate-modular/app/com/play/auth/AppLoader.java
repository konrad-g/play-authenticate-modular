package com.play.auth; /**
 * Created by Konrad Gadzinowski on 18/01/16.
 */

import com.play.auth.elements.i18n.I18NModule;
import play.ApplicationLoader;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;

import java.util.LinkedHashSet;
import java.util.Set;

public class AppLoader extends GuiceApplicationLoader {

    private final String DEFAULT_LANG = "en";

    @Override
    public GuiceApplicationBuilder builder(ApplicationLoader.Context context) {

        GuiceApplicationBuilder builder = (GuiceApplicationBuilder) ((GuiceApplicationBuilder) this.initialBuilder
                .in(context.environment()))
                .loadConfig(context.initialConfiguration());
        builder = I18NModule.setupI18N(builder, DEFAULT_LANG, getI18nFiles());
        builder = builder.overrides(this.overrides(context));

        return builder;
    }

    public String getDefaultLang() {
        return DEFAULT_LANG;
    }

    public Set<String> getI18nFiles() {

        Set<String> i18nLangs = new LinkedHashSet<>();

        // App
        i18nLangs.add("base-messages");
        i18nLangs.add("index-messages");
        i18nLangs.add("profile-messages");
        i18nLangs.add("restricted-messages");

        // Auth
        i18nLangs.add("auth-main-messages");
        i18nLangs.add("auth-account-messages");
        i18nLangs.add("auth-login-messages");
        i18nLangs.add("auth-pass-change-messages");
        i18nLangs.add("auth-pass-reset-messages");
        i18nLangs.add("auth-signup-messages");

        return i18nLangs;
    }
}