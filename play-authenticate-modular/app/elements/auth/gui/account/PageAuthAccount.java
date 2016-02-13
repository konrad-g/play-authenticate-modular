package elements.auth.gui.account;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;
import java.util.Optional;
import controllers.ApplicationController;
import controllers.routes;
import elements.auth.main.*;
import elements.common.OnRenderListener;
import elements.gui.base.ContentInner;
import elements.session.Session;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Result;
import elements.auth.gui.account.html.*;
import play.mvc.Results;
import play.twirl.api.Content;

import static play.data.Form.form;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 12/02/16.
 */
public class PageAuthAccount {

    private Session session;
    private OnRenderListener onRenderListener;

    public PageAuthAccount(Session session, OnRenderListener onRenderListener) {
        this.session = session;
        this.onRenderListener = onRenderListener;
    }

    private Form<ModelAuth.Accept> getAcceptForm() {
        return form(ModelAuth.Accept.class);
    }
    private Form<ModelAuth.PasswordChange> getPasswordChangeForm() {
        return form(ModelAuth.PasswordChange.class);
    }

    public Result renderLink() {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(this.session.response());

        boolean disableIndexing = true;
        String title = Messages.get("playauthenticate.link.account.title");
        String description = Messages.get("playauthenticate.link.account.description");
        String keywords = Messages.get("playauthenticate.link.account.keywords");

        Content content = ViewLink.render();

        ContentInner contentInner = new ContentInner(title, description, keywords, content);
        return Results.ok(this.onRenderListener.onRender(contentInner, disableIndexing));
    }

    public Result renderVerifyEmail() {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(this.session.response());
        Optional<EntryUser> user = this.session.getCurrentUser();

        if(!user.isPresent()) {
            return Results.forbidden();
        } else {
            if (user.get().emailValidated) {
                // E-Mail has been validated already
                this.session.flash(Auth.FLASH_MESSAGE_KEY,
                        Messages.get("playauthenticate.verify_email.error.already_validated"));
            } else if (user.get().email != null && !user.get().email.trim().isEmpty()) {
                this.session.flash(Auth.FLASH_MESSAGE_KEY, Messages.get(
                        "playauthenticate.verify_email.message.instructions_sent",
                        user.get().email));
                ProviderUsernamePasswordAuth.getProvider()
                        .sendVerifyEmailMailingAfterSignup(user.get(), this.session.ctx());
            } else {
                this.session.flash(Auth.FLASH_MESSAGE_KEY, Messages.get(
                        "playauthenticate.verify_email.error.set_email_first",
                        user.get().email));
            }

            return this.onRenderListener.redirectToAccount();
        }
    }

    public Result renderAskLink() {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(this.session.response());
        final AuthUser u = PlayAuthenticate.getLinkUser(this.session.session());

        if (u == null) {
            // account to link could not be found, silently redirect to login
            return this.onRenderListener.redirectToMain();
        }

        ContentInner contentInner = renderAskLinkView(getAcceptForm(), u);

        boolean disableIndexing = true;
        return Results.ok(this.onRenderListener.onRender(contentInner, disableIndexing));
    }

    public Result doLink() {

        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(this.session.response());
        final AuthUser u = PlayAuthenticate.getLinkUser(this.session.session());

        if (u == null) {
            // account to link could not be found, silently redirect to login
            return this.onRenderListener.redirectToMain();
        }

        final Form<ModelAuth.Accept> filledForm = getAcceptForm().bindFromRequest();

        if (filledForm.hasErrors()) {
            // User did not select whether to link or not link

            ContentInner contentInner = renderAskLinkView(filledForm, u);
            boolean disableIndexing = true;

            return Results.badRequest(this.onRenderListener.onRender(contentInner, disableIndexing));

        } else {

            // User made a choice :)
            final boolean link = filledForm.get().accept;
            if (link) {
                this.session.flash(Auth.FLASH_MESSAGE_KEY,
                        Messages.get("playauthenticate.accounts.link.success"));
            }
            return PlayAuthenticate.link(this.session.ctx(), link);
        }
    }

    public Result renderAskMerge() {

        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(this.session.response());
        // this is the currently logged in user
        final AuthUser aUser = PlayAuthenticate.getUser(this.session.session());

        // this is the user that was selected for a login
        final AuthUser bUser = PlayAuthenticate.getMergeUser(this.session.session());
        if (bUser == null) {
            // user to merge with could not be found, silently redirect to login
            return this.onRenderListener.redirectToMain();
        }

        // You could also get the local user object here via
        // User.findByAuthUserIdentity(newUser)

        ContentInner contentInner = renderMergeAccountsView(getAcceptForm(), aUser, bUser);
        boolean disableIndexing = true;

        return Results.ok(this.onRenderListener.onRender(contentInner, disableIndexing));
    }

    public Result doMerge() {

        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(this.session.response());
        // this is the currently logged in user
        final AuthUser aUser = PlayAuthenticate.getUser(this.session.session());

        // this is the user that was selected for a login
        final AuthUser bUser = PlayAuthenticate.getMergeUser(this.session.session());
        if (bUser == null) {
            // user to merge with could not be found, silently redirect to login
            return this.onRenderListener.redirectToMain();
        }

        final Form<ModelAuth.Accept> filledForm = getAcceptForm().bindFromRequest();
        if (filledForm.hasErrors()) {
            // User did not select whether to merge or not merge

            ContentInner contentInner = renderMergeAccountsView(filledForm, aUser, bUser);
            boolean disableIndexing = true;

            return Results.badRequest(this.onRenderListener.onRender(contentInner, disableIndexing));
        } else {
            // User made a choice :)
            final boolean merge = filledForm.get().accept;
            if (merge) {
                this.session.flash(Auth.FLASH_MESSAGE_KEY,
                        Messages.get("playauthenticate.accounts.merge.success"));
            }
            return PlayAuthenticate.merge(this.session.ctx(), merge);
        }
    }

    public Result renderOAuthDenied(final String getProviderKey) {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(this.session.response());

        boolean disableIndexing = true;
        String title = Messages.get("playauthenticate.oauth.access.denied.title");
        String description = Messages.get("playauthenticate.oauth.access.denied.description");
        String keywords = Messages.get("playauthenticate.oauth.access.denied.keywords");

        Content content = ViewOAuthDenied.render(getProviderKey);
        ContentInner contentInner = new ContentInner(title, description, keywords, content);

        return Results.ok(this.onRenderListener.onRender(contentInner, disableIndexing));
    }

    public Result renderVerify(final String token) {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(this.session.response());
        final EntryTokenAction ta = Auth.isTokenValid(token, EntryTokenAction.Type.EMAIL_VERIFICATION);
        if (ta == null) {

            boolean disableIndexing = true;

            ContentInner contentInner = renderNoTokenOrInvalidView();
            return Results.badRequest(this.onRenderListener.onRender(contentInner, disableIndexing));
        }
        final String email = ta.targetUser.email;
        EntryUser.verify(ta.targetUser);
        this.session.flash(Auth.FLASH_MESSAGE_KEY,
                Messages.get("playauthenticate.verify_email.success", email));
        if (this.session.getCurrentUser().isPresent()) {
            return Results.redirect(routes.ApplicationController.index());
        } else {
            return Results.redirect(routes.AuthController.login());
        }
    }

    public ContentInner renderNoTokenOrInvalidView() {
        String title = Messages.get("playauthenticate.token.error.title");
        String description = Messages.get("playauthenticate.token.error.description");
        String keywords = Messages.get("playauthenticate.token.error.keywords");

        Content content = ViewNoTokenOrInvalid.render();
        ContentInner contentInner = new ContentInner(title, description, keywords, content);

        return contentInner;
    }

    private ContentInner renderAskLinkView(Form<ModelAuth.Accept> form, AuthUser user) {

        String title = Messages.get("playauthenticate.link.account.title");
        String description = Messages.get("playauthenticate.link.account.description");
        String keywords = Messages.get("playauthenticate.link.account.keywords");

        Content content = ViewAskLink.render(form, user);
        ContentInner contentInner = new ContentInner(title, description, keywords, content);

        return contentInner;
    }

    private ContentInner renderMergeAccountsView(Form<ModelAuth.Accept> form, AuthUser aUser, AuthUser bUser) {

        String title = Messages.get("playauthenticate.merge.accounts.title");
        String description = Messages.get("playauthenticate.merge.accounts.description");
        String keywords = Messages.get("playauthenticate.merge.accounts.keywords");

        // You could also get the local user object here via
        // User.findByAuthUserIdentity(newUser)
        Content content = ViewAskMerge.render(form, aUser, bUser);
        ContentInner contentInner = new ContentInner(title, description, keywords, content);

        return contentInner;
    }
}
