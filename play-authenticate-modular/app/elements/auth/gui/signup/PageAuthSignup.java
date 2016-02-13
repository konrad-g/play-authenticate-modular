package elements.auth.gui.signup;

import com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider;
import elements.auth.gui.account.html.ViewSignupUnverified;
import elements.auth.main.ModelAuth;
import elements.common.OnRenderListener;
import elements.gui.base.ContentInner;
import elements.session.Session;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;
import elements.auth.gui.signup.html.*;

import java.util.Optional;

import static play.data.Form.form;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 12/02/16.
 */
public class PageAuthSignup {

    private Session session;
    private OnRenderListener onRenderListener;

    public static Form<ModelAuth.Signup> getSignUpForm() {
        return form(ModelAuth.Signup.class);
    }

    public PageAuthSignup(Session session, OnRenderListener onRenderListener) {
        this.session = session;
        this.onRenderListener = onRenderListener;
    }

    public Result renderSignup() {

        boolean disableIndexing = false;
        ContentInner innerContent = renderSignupContent(Optional.empty());

        Content contentFinal = this.onRenderListener.onRender(innerContent, disableIndexing);
        return Results.ok(contentFinal);
    }

    public Result renderUnverified() {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(this.session.response());

        String title = Messages.get("playauthenticate.verify.email.title");
        String description = Messages.get("playauthenticate.verify.email.description");
        String keywords = Messages.get("playauthenticate.verify.email.keywords");

        boolean disableIndexing = true;
        Content content = ViewSignupUnverified.render();
        ContentInner innerContent = new ContentInner(title, description, keywords, content);

        return Results.ok(this.onRenderListener.onRender(innerContent, disableIndexing));
    }

    public Result renderExists() {

        boolean disableIndexing = true;

        String title = Messages.get("playauthenticate.user.exists.title");
        String description = Messages.get("playauthenticate.user.exists.description");
        String keywords = Messages.get("playauthenticate.user.exists.keywords");

        com.feth.play.module.pa.controllers.Authenticate.noCache(this.session.response());

        Content content = ViewUserAlreadyExists.render();
        ContentInner innerContent = new ContentInner(title, description, keywords, content);

        Content contentFinal = this.onRenderListener.onRender(innerContent, disableIndexing);
        return Results.ok(contentFinal);
    }

    public Result doSignup() {

        com.feth.play.module.pa.controllers.Authenticate.noCache(this.session.response());
        final Form<ModelAuth.Signup> filledForm = getSignUpForm().bindFromRequest();

        if (filledForm.hasErrors()) {

            // User did not fill everything properly
            boolean disableIndexing = false;
            ContentInner innerContent = renderSignupContent(Optional.ofNullable(filledForm));

            Content contentFinal = this.onRenderListener.onRender(innerContent, disableIndexing);
            return Results.badRequest(contentFinal);
        } else {
            // Everything was filled
            return UsernamePasswordAuthProvider.handleSignup(this.session.ctx());
        }
    }

    private ContentInner renderSignupContent(Optional<Form<ModelAuth.Signup>> form) {

        String title = Messages.get("playauthenticate.signup.title");
        String description = Messages.get("playauthenticate.signup.description");
        String keywords = Messages.get("playauthenticate.signup.keywords");

        Content content;

        if(form.isPresent()) {
            content = ViewSignup.render(form.get());
        } else {
            content = ViewSignup.render(getSignUpForm());
        }

        ContentInner innerContent = new ContentInner(title, description, keywords, content);
        return innerContent;
    }
}
