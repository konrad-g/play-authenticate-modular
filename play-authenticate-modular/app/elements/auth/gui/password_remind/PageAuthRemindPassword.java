package elements.auth.gui.password_remind;

import com.feth.play.module.pa.PlayAuthenticate;
import controllers.routes;
import elements.auth.gui.account.html.ViewNoTokenOrInvalid;
import elements.auth.gui.password_remind.html.ViewPasswordForgot;
import elements.auth.gui.password_remind.html.ViewPasswordReset;
import elements.auth.main.*;
import elements.common.OnRenderListener;
import elements.session.Session;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;

import static play.data.Form.form;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 12/02/16.
 */
public class PageAuthRemindPassword {

    private Session session;
    private OnRenderListener onRenderListener;

    public PageAuthRemindPassword(Session session, OnRenderListener onRenderListener) {
        this.session = session;
        this.onRenderListener = onRenderListener;
    }

    private static final Form<ModelAuth.PasswordReset> PASSWORD_RESET_FORM = form(ModelAuth.PasswordReset.class);
    private static final Form<ModelAuth.Identity> FORGOT_PASSWORD_FORM = form(ModelAuth.Identity.class);

    public Result renderRemindPassword(final String email) {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(this.session.response());
        Form<ModelAuth.Identity> form = FORGOT_PASSWORD_FORM;
        if (email != null && !email.trim().isEmpty()) {
            form = FORGOT_PASSWORD_FORM.fill(new ModelAuth.Identity(email));
        }
        ViewPasswordForgot.render(form)

        return ok();
    }

    public Result doRemindPassword() {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(this.session.response());
        final Form<ModelAuth.Identity> filledForm = FORGOT_PASSWORD_FORM
                .bindFromRequest();
        if (filledForm.hasErrors()) {
            // User did not fill in his/her email
            Content content = ViewPasswordForgot.render(filledForm);
            return badRequest();
        } else {
            // The email address given *BY AN UNKNWON PERSON* to the form - we
            // should find out if we actually have a user with this email
            // address and whether password login is enabled for him/her. Also
            // only send if the email address of the user has been verified.
            final String email = filledForm.get().email;

            // We don't want to expose whether a given email address is signed
            // up, so just say an email has been sent, even though it might not
            // be true - that's protecting our user privacy.
            this.session.flash(Auth.FLASH_MESSAGE_KEY,
                    Messages.get(
                            "playauthenticate.reset_password.message.instructions_sent",
                            email));

            final EntryUser user = EntryUser.findByEmail(email);
            if (user != null) {
                // yep, we have a user with this email that is active - we do
                // not know if the user owning that account has requested this
                // reset, though.
                final ProviderUsernamePasswordAuth provider = ProviderUsernamePasswordAuth
                        .getProvider();
                // User exists
                if (user.emailValidated) {
                    provider.sendPasswordResetMailing(user, this.session.ctx());
                    // In case you actually want to let (the unknown person)
                    // know whether a user was found/an email was sent, use,
                    // change the flash message
                } else {
                    // We need to change the message here, otherwise the user
                    // does not understand whats going on - we should not verify
                    // with the password reset, as a "bad" user could then sign
                    // up with a fake email via OAuth and get it verified by an
                    // a unsuspecting user that clicks the link.
                    this.session.flash(Auth.FLASH_MESSAGE_KEY,
                            Messages.get("playauthenticate.reset_password.message.email_not_verified"));

                    // You might want to re-send the verification email here...
                    provider.sendVerifyEmailMailingAfterSignup(user, this.session.ctx());
                }
            }

            return redirect(controllers.routes.ApplicationController.index());
        }
    }

    public Result renderResetPassword(final String token) {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(response());
        final EntryTokenAction ta = Auth.isTokenValid(token, EntryTokenAction.Type.PASSWORD_RESET);
        if (ta == null) {

            ViewNoTokenOrInvalid.render()

            return badRequest();
        }

        return ok(ViewPasswordReset.render(PASSWORD_RESET_FORM
                .fill(new ModelAuth.PasswordReset(token))));
    }

    public Result doResetPassword() {
        com.feth.play.module.pa.controllers.AuthenticateDI.noCache(response());
        final Form<ModelAuth.PasswordReset> filledForm = PASSWORD_RESET_FORM
                .bindFromRequest();
        if (filledForm.hasErrors()) {
            Content content = ViewPasswordReset.render(filledForm);
            return badRequest();
        } else {
            final String token = filledForm.get().token;
            final String newPassword = filledForm.get().password;

            final EntryTokenAction ta = Auth.isTokenValid(token, EntryTokenAction.Type.PASSWORD_RESET);
            if (ta == null) {

                Content content = ViewNoTokenOrInvalid.render();

                return badRequest();
            }
            final EntryUser u = ta.targetUser;
            try {
                // Pass true for the second parameter if you want to
                // automatically create a password and the exception never to
                // happen
                u.resetPassword(new ProviderUsernamePasswordAuthUser(newPassword),
                        false);
            } catch (final RuntimeException re) {
                this.session.flash(Auth.FLASH_MESSAGE_KEY,
                        Messages.get("playauthenticate.reset_password.message.no_password_account"));
            }
            final boolean login = ProviderUsernamePasswordAuth.getProvider()
                    .isLoginAfterPasswordReset();
            if (login) {
                // automatically log in
                this.session.flash(Auth.FLASH_MESSAGE_KEY,
                        Messages.get("playauthenticate.reset_password.message.success.auto_login"));

                return PlayAuthenticate.loginAndRedirect(ctx(),
                        new ProviderLoginUsernamePasswordAuthUser(u.email));
            } else {
                // send the user to the login page
                this.session.flash(Auth.FLASH_MESSAGE_KEY,
                        Messages.get("playauthenticate.reset_password.message.success.manual_login"));
            }
            return Results.redirect(routes.ApplicationController.login());
        }
    }
}
