/**
 * Created by Konrad Gadzinowski on 18/01/16.
 */
import elements.i18n.I18NModule;
import play.Application;
import play.ApplicationLoader;
import play.Configuration;
import play.Logger;
import play.inject.guice.GuiceApplicationBuilder;
import play.inject.guice.GuiceApplicationLoader;
import play.libs.Scala;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

public class AppLoader extends GuiceApplicationLoader {

    @Override
    public GuiceApplicationBuilder builder(ApplicationLoader.Context context) {

        GuiceApplicationBuilder builder = (GuiceApplicationBuilder)((GuiceApplicationBuilder)this.initialBuilder
                .in(context.environment()))
                .loadConfig(context.initialConfiguration());
        builder = I18NModule.setupI18N(builder, Global.DEFAULT_LANG, getI18nFiles());
        builder = builder.overrides(this.overrides(context));

        return builder;
    }

    public Set<String> getI18nFiles() {

        Set<String> i18nLangs = new LinkedHashSet<>();
        i18nLangs.add("login-messages");
        i18nLangs.add("signup-messages");
        return i18nLangs;
    }
}