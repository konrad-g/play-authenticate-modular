package elements.session;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;
import elements.auth.main.EntryUser;
import play.mvc.Controller;

import java.util.Optional;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 11/12/15.
 */
public class Session {

    private final String[] languages = {"de", "en", "es", "pl", "pt"};

    private Controller controller;

    public Session(final Controller controller) {
        this.controller = controller;
    }

    public play.mvc.Http.Context ctx() {
        return this.controller.ctx();
    }

    public play.mvc.Http.Request request() {
        return this.controller.request();
    }

    public play.i18n.Lang lang() {
        return this.controller.lang();
    }

    public boolean changeLang(String langCode) {
        return this.controller.changeLang(langCode);
    }

    public boolean changeLang(play.i18n.Lang lang) {
        return this.controller.changeLang(lang);
    }

    public void clearLang() {
        this.controller.clearLang();
    }

    public play.mvc.Http.Response response() {
        return this.controller.response();
    }

    public play.mvc.Http.Session session() {
        return this.controller.session();
    }

    public void session(String key, String value) {
        this.controller.session(key, value);
    }

    public String session(String key) {
        return this.controller.session(key);
    }

    public play.mvc.Http.Flash flash() {
        return this.controller.flash();
    }

    public void flash(String key, String value) {
        this.controller.flash(key, value);
    }

    public String flash(String text) {
        return this.controller.flash(text);
    }

    public String[] languages() {
        return this.languages;
    }

    public Optional<EntryUser> getCurrentUser() {

        final AuthUser currentAuthUser = PlayAuthenticate.getUser(this.session());
        EntryUser currentUser = EntryUser.findByAuthUserIdentity(currentAuthUser);

        Optional<EntryUser> userOptional = Optional.ofNullable(currentUser);
        return userOptional;
    }

    public boolean isLanguageSupported(String lang) {

        if (lang == null) {
            return false;
        }

        String[] languages = this.languages;
        for (int i = 0; i < languages.length; i++) {
            if (languages[i].equals(lang)) {
                return true;
            }
        }

        return false;
    }
}
