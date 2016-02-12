I18N - Modular internationalization
===================================

This element enables you to use modular languages files (messages).

## To activate it ##
1. Activate this module and disable default i18n module. To do this create custom GuiceApplicationLoader, for e.g.:

```java
public class AppLoader extends GuiceApplicationLoader {

    @Override
    public GuiceApplicationBuilder builder(ApplicationLoader.Context context) {

        GuiceApplicationBuilder builder = (GuiceApplicationBuilder)((GuiceApplicationBuilder)this.initialBuilder
                .in(context.environment()))
                .loadConfig(context.initialConfiguration());
        builder = I18NModule.setupI18N(builder, getI18nFiles());
        builder = builder.overrides(this.overrides(context));

        return builder;
    }

    public Set<String> getI18nFiles() {

        Set<String> i18nLangs = new LinkedHashSet<>();
        i18nLangs.add("base-page-messages");
        i18nLangs.add("about-page-messages");
        i18nLangs.add("error-messages");

        return i18nLangs;
    }
}
```

and then add to appropriate 'application.conf':
play.application.loader = "AppLoader"

## To add additional languages files (messages) ##
1. Add additional resource folders to 'build.sbt', where the messages files are stored. For e.g.:

unmanagedResourceDirectories in Compile += baseDirectory.value / "app/com/digitaljetty/workjetty/elements/gui/pages/about/i18n"

2. Name messages files with any prefix and required postfix "-messages" and then language code. For e.g.:
"about-page-messages"
"about-page-messages.en-US"
"about-page-messages.en-CA"
"about-page-messages.pl"
"about-page-messages.de"

3. Add the same name to custom GuiceApplicationLoader i.e.:

```java
public Set<String> getI18nFiles() {

    Set<String> i18nLangs = new LinkedHashSet<>();
    i18nLangs.add("base-page-messages");
    i18nLangs.add("about-page-messages");
    i18nLangs.add("error-messages");

    return i18nLangs;
}
```