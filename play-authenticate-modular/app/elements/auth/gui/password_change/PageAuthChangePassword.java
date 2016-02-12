package elements.auth.gui.password_change;

import controllers.ApplicationController;
import controllers.routes;
import elements.auth.main.EntryUser;
import elements.auth.main.ModelAuth;
import elements.auth.main.ProviderUsernamePasswordAuthUser;
import elements.common.OnRenderListener;
import elements.session.Session;
import elements.auth.gui.password_change.html.*;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Result;

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

    ////////////

    public Result renderChangePassword() {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(response());
        final EntryUser u = getSession().getCurrentUser().get();

        if (!u.emailValidated) {
            return ok(ViewAccountUnverified.render());
        } else {
            return ok(ViewPasswordChange.render(PASSWORD_CHANGE_FORM));
        }
    }

    public Result doChangePassword() {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(response());
        final Form<ModelAuth.PasswordChange> filledForm = PASSWORD_CHANGE_FORM
                .bindFromRequest();
        if (filledForm.hasErrors()) {
            // User did not select whether to link or not link
            return badRequest(ViewPasswordChange.render(filledForm));
        } else {
            final EntryUser user = getSession().getCurrentUser().get();
            final String newPassword = filledForm.get().password;
            user.changePassword(new ProviderUsernamePasswordAuthUser(newPassword),
                    true);
            flash(ApplicationController.FLASH_MESSAGE_KEY,
                    Messages.get("playauthenticate.change_password.success"));
            return redirect(routes.ApplicationController.profile());
        }
    }
}
