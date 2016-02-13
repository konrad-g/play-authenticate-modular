package controllers;

import elements.auth.main.EntryUser;
import elements.common.OnRenderListener;
import elements.gui.base.ContentInner;
import elements.gui.base.LayoutBase;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;
import elements.session.Session;

import java.util.Optional;

/**
 * Created by Konrad Gadzinowski on 27/07/2015.
 */
public class BaseController extends Controller {

    protected OnRenderListener onRenderListener = new OnRenderListener() {
        @Override
        public Content onRender(ContentInner contentInner, boolean disableIndexing) {
            Content content = renderMainLayout(contentInner, disableIndexing);
            return content;
        }

        @Override
        public Result redirectToLogin() {
            return redirect(routes.ApplicationController.login());
        }

        @Override
        public Result redirectToAccount() {
            return redirect(routes.ApplicationController.profile());
        }

        @Override
        public Result redirectToMain() {
            return redirect(routes.ApplicationController.index());
        }
    };

    private Session session;
    private Optional<EntryUser> user = Optional.empty();

    public BaseController() { }

    public Session getSession() {

        if(null == this.session) {
            this.session = new Session(this);
        }

        return this.session;
    }

    public Optional<Result> setLanguage(String lang) {

        // Check language
        String currentLang = getSession().lang().code();

        if(currentLang != null && currentLang.equals(lang)) {
            // Don't do nothing. Language is already set
        } else if(!getSession().isLanguageSupported(lang) ) {
            Result result = Results.redirect(routes.ApplicationController.index());
            return Optional.of(result);
        } else {
            getSession().ctx().changeLang(lang);
        }

        return Optional.empty();
    }

    public Content renderMainLayout(ContentInner innerContent, boolean disableIndexing) {

        LayoutBase renderer = new LayoutBase(getSession());
        Content content = renderer.getContent(innerContent, disableIndexing);
        return content;
    }
}
