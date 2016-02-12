package elements.auth.gui.login;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;
import elements.auth.main.ModelAuth;
import elements.auth.main.ProviderUsernamePasswordAuth;
import elements.common.OnRenderListener;
import elements.gui.base.ContentInner;
import elements.session.Session;
import play.Logger;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;
import elements.auth.gui.login.html.*;

import java.util.Optional;

import static play.data.Form.form;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 12/02/16.
 */
public class PageAuthLogin {

    private Session session;
    private OnRenderListener onRenderListener;

    public PageAuthLogin(Session session, OnRenderListener onRenderListener) {
        this.session = session;
        this.onRenderListener = onRenderListener;
    }

    public static ContentInner renderLoginContent(Optional<Form<ModelAuth.Login>> form) {

        String title = Messages.get("playauthenticate.login.title");
        String description = Messages.get("playauthenticate.login.description");
        String keywords = Messages.get("playauthenticate.login.keywords");

        Content content;

        if(form.isPresent()) {
            content = ViewLogin.render(form.get());
        } else {
            content = ViewLogin.render(getLoginForm());
        }
        ContentInner innerContent = new ContentInner(title, description, keywords, content);

        return innerContent;
    }

    public static Form<ModelAuth.Login> getLoginForm() {
        return form(ModelAuth.Login.class);
    }

    public Result renderLogin() {

        boolean disableIndexing = false;
        ContentInner innerContent = PageAuthLogin.renderLoginContent(Optional.empty());
        Content contentFinal = this.onRenderListener.onRender(innerContent, disableIndexing);

        return Results.ok(contentFinal);
    }

    public Result doLogin() {

        com.feth.play.module.pa.controllers.Authenticate.noCache(this.session.response());
        final Form<ModelAuth.Login> filledForm = getLoginForm().bindFromRequest();

        if (filledForm.hasErrors()) {
            // User did not fill everything properly

            boolean disableIndexing = false;
            ContentInner innerContent = PageAuthLogin.renderLoginContent(
                    Optional.ofNullable(filledForm));

            Content contentFinal = this.onRenderListener.onRender(innerContent, disableIndexing);
            return Results.badRequest(contentFinal);

        } else {
            // Everything was filled
            return UsernamePasswordAuthProvider.handleLogin(this.session.ctx());
        }
    }

}
