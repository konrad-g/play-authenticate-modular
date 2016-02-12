package elements.auth.gui.account;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.SubjectPresent;
import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;
import java.util.Optional;
import controllers.ApplicationController;
import controllers.routes;
import elements.auth.main.EntryUser;
import elements.auth.main.ModelAuth;
import elements.auth.main.ProviderUsernamePasswordAuth;
import elements.auth.main.ProviderUsernamePasswordAuthUser;
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
                this.session.flash(ApplicationController.FLASH_MESSAGE_KEY,
                        Messages.get("playauthenticate.verify_email.error.already_validated"));
            } else if (user.get().email != null && !user.email.trim().isEmpty()) {
                this.session.flash(ApplicationController.FLASH_MESSAGE_KEY, Messages.get(
                        "playauthenticate.verify_email.message.instructions_sent",
                        user.get().email));
                ProviderUsernamePasswordAuth.getProvider()
                        .sendVerifyEmailMailingAfterSignup(user, ctx());
            } else {
                this.session.flash(ApplicationController.FLASH_MESSAGE_KEY, Messages.get(
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
        return ok(ViewAskLink.render(ACCEPT_FORM, u));
    }

    public Result doLink() {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(response());
        final AuthUser u = PlayAuthenticate.getLinkUser(session());
        if (u == null) {
            // account to link could not be found, silently redirect to login
            return this.onRenderListener.redirectToMain();
        }

        final Form<ModelAuth.Accept> filledForm = ACCEPT_FORM.bindFromRequest();
        if (filledForm.hasErrors()) {
            // User did not select whether to link or not link
            return badRequest(ViewAskLink.render(filledForm, u));
        } else {
            // User made a choice :)
            final boolean link = filledForm.get().accept;
            if (link) {
                flash(ApplicationController.FLASH_MESSAGE_KEY,
                        Messages.get("playauthenticate.accounts.link.success"));
            }
            return PlayAuthenticate.link(ctx(), link);
        }
    }

    public Result renderAskMerge() {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(response());
        // this is the currently logged in user
        final AuthUser aUser = PlayAuthenticate.getUser(session());

        // this is the user that was selected for a login
        final AuthUser bUser = PlayAuthenticate.getMergeUser(session());
        if (bUser == null) {
            // user to merge with could not be found, silently redirect to login
            return this.onRenderListener.redirectToMain();
        }

        // You could also get the local user object here via
        // User.findByAuthUserIdentity(newUser)
        return ok(ViewAskMerge.render(ACCEPT_FORM, aUser, bUser));
    }

    public Result doMerge() {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(response());
        // this is the currently logged in user
        final AuthUser aUser = PlayAuthenticate.getUser(session());

        // this is the user that was selected for a login
        final AuthUser bUser = PlayAuthenticate.getMergeUser(session());
        if (bUser == null) {
            // user to merge with could not be found, silently redirect to login
            return this.onRenderListener.redirectToMain();
        }

        final Form<ModelAuth.Accept> filledForm = ACCEPT_FORM.bindFromRequest();
        if (filledForm.hasErrors()) {
            // User did not select whether to merge or not merge
            return badRequest(ViewAskMerge.render(filledForm, aUser, bUser));
        } else {
            // User made a choice :)
            final boolean merge = filledForm.get().accept;
            if (merge) {
                flash(ApplicationController.FLASH_MESSAGE_KEY,
                        Messages.get("playauthenticate.accounts.merge.success"));
            }
            return PlayAuthenticate.merge(ctx(), merge);
        }
    }
}
