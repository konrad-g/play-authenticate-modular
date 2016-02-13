package elements.auth.gui.password_change;

import controllers.ApplicationController;
import controllers.routes;
import elements.auth.main.Auth;
import elements.auth.main.EntryUser;
import elements.auth.main.ModelAuth;
import elements.auth.main.ProviderUsernamePasswordAuthUser;
import elements.common.OnRenderListener;
import elements.gui.base.ContentInner;
import elements.session.Session;
import elements.auth.gui.password_change.html.*;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;
import static play.data.Form.form;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 12/02/16.
 */
public class PageAuthChangePassword {

    private Session session;
    private OnRenderListener onRenderListener;

    public PageAuthChangePassword(Session session, OnRenderListener onRenderListener) {
        this.session = session;
        this.onRenderListener = onRenderListener;
    }

    public Form<ModelAuth.PasswordChange> getChangePasswordForm() {
        Form<ModelAuth.PasswordChange> changePasswordForm = form(ModelAuth.PasswordChange.class);
        return changePasswordForm;
    }

    public Result renderChangePassword() {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(this.session.response());
        final EntryUser user = this.session.getCurrentUser().get();

        if (!user.emailValidated) {

            String title = Messages.get("playauthenticate.verify.account.title");
            String description = Messages.get("playauthenticate.verify.account.description");
            String keywords = Messages.get("playauthenticate.verify.account.keywords");

            Content content = ViewAccountUnverified.render();
            ContentInner contentInner = new ContentInner(title, description, keywords, content);

            boolean disableIndexing = false;
            return Results.ok(this.onRenderListener.onRender(contentInner, disableIndexing));
        } else {
            ContentInner contentInner = renderChangePasswordView(getChangePasswordForm());
            boolean disableIndexing = false;
            return Results.ok(this.onRenderListener.onRender(contentInner, disableIndexing));
        }
    }

    public Result doChangePassword() {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(this.session.response());
        final Form<ModelAuth.PasswordChange> filledForm = getChangePasswordForm()
                .bindFromRequest();
        if (filledForm.hasErrors()) {
            // User did not select whether to link or not link
            ContentInner contentInner = renderChangePasswordView(filledForm);
            boolean disableIndexing = false;
            return Results.badRequest(this.onRenderListener.onRender(contentInner, disableIndexing));
        } else {
            final EntryUser user = this.session.getCurrentUser().get();
            final String newPassword = filledForm.get().password;
            user.changePassword(new ProviderUsernamePasswordAuthUser(newPassword),
                    true);
            this.session.flash(Auth.FLASH_MESSAGE_KEY,
                    Messages.get("playauthenticate.change_password.success"));
            return Results.redirect(routes.ApplicationController.profile());
        }
    }

    private ContentInner renderChangePasswordView(Form<ModelAuth.PasswordChange> form) {

        String title = Messages.get("playauthenticate.change.password.title");
        String description = Messages.get("playauthenticate.change.password.description");
        String keywords = Messages.get("playauthenticate.change.password.keywords");

        Content content = ViewPasswordChange.render(form);

        ContentInner contentInner = new ContentInner(title, description, keywords, content);

        return contentInner;
    }
}
