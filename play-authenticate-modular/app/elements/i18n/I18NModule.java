package elements.i18n;

import play.api.Configuration;
import play.api.Environment;
import play.api.i18n.*;
import play.api.inject.Binding;
import play.api.inject.Module;
import play.inject.guice.GuiceApplicationBuilder;
import scala.collection.Seq;

import java.util.List;
import java.util.Set;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 06/01/16.
 */
public class I18NModule extends Module {

    private static Set<String> i18nFiles;
    private static String defaultLangCode = "en";

    public I18NModule(Set<String> i18nFiles) {
        I18NModule.i18nFiles = i18nFiles;
    }

    public static Set<String> getI18nFiles() {
        return I18NModule.i18nFiles;
    }

    public static String getDefaultLangCode() {
        return I18NModule.defaultLangCode;
    }

    public static GuiceApplicationBuilder setupI18N(GuiceApplicationBuilder builder, String defaultLangCode, Set<String> i18nFiles) {
        I18NModule.defaultLangCode = defaultLangCode;
        return builder
                .disable(play.api.i18n.I18nModule.class)
                .bindings(new elements.i18n.I18NModule(i18nFiles));
    }

    @Override
    public Seq<Binding<?>> bindings(Environment environment, Configuration configuration) {

        return seq(
                bind(Langs.class).to(DefaultLangs.class),
                bind(MessagesApi.class).to(ModularMessagesApi.class)
        );
    }
}
