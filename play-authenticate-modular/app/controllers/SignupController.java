package controllers;

import elements.auth.gui.signup.PageAuthSignup;
import elements.auth.main.*;
import elements.auth.main.EntryTokenAction.Type;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Result;

import com.feth.play.module.pa.PlayAuthenticate;
import elements.auth.gui.account.html.*;
import elements.auth.gui.signup.html.*;
import elements.auth.gui.password_remind.html.*;

import static play.data.Form.form;

public class SignupController extends BaseController {

	private static final Form<ModelAuth.PasswordReset> PASSWORD_RESET_FORM = form(ModelAuth.PasswordReset.class);

	public Result unverified() {
		com.feth.play.module.pa.controllers.AuthenticateDI.noCache(response());
		return ok(ViewSignupUnverified.render());
	}

	private static final Form<ModelAuth.Identity> FORGOT_PASSWORD_FORM = form(ModelAuth.Identity.class);

	public Result forgotPassword(final String email) {
		com.feth.play.module.pa.controllers.AuthenticateDI.noCache(response());
		Form<ModelAuth.Identity> form = FORGOT_PASSWORD_FORM;
		if (email != null && !email.trim().isEmpty()) {
			form = FORGOT_PASSWORD_FORM.fill(new ModelAuth.Identity(email));
		}
		return ok(ViewPasswordForgot.render(form));
	}

	public Result doForgotPassword() {
		com.feth.play.module.pa.controllers.AuthenticateDI.noCache(response());
		final Form<ModelAuth.Identity> filledForm = FORGOT_PASSWORD_FORM
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			// User did not fill in his/her email
			return badRequest(ViewPasswordForgot.render(filledForm));
		} else {
			// The email address given *BY AN UNKNWON PERSON* to the form - we
			// should find out if we actually have a user with this email
			// address and whether password login is enabled for him/her. Also
			// only send if the email address of the user has been verified.
			final String email = filledForm.get().email;

			// We don't want to expose whether a given email address is signed
			// up, so just say an email has been sent, even though it might not
			// be true - that's protecting our user privacy.
			flash(Auth.FLASH_MESSAGE_KEY,
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
					provider.sendPasswordResetMailing(user, ctx());
					// In case you actually want to let (the unknown person)
					// know whether a user was found/an email was sent, use,
					// change the flash message
				} else {
					// We need to change the message here, otherwise the user
					// does not understand whats going on - we should not verify
					// with the password reset, as a "bad" user could then sign
					// up with a fake email via OAuth and get it verified by an
					// a unsuspecting user that clicks the link.
					flash(Auth.FLASH_MESSAGE_KEY,
							Messages.get("playauthenticate.reset_password.message.email_not_verified"));

					// You might want to re-send the verification email here...
					provider.sendVerifyEmailMailingAfterSignup(user, ctx());
				}
			}

			return redirect(routes.ApplicationController.index());
		}
	}

	/**
	 * Returns a token object if valid, null if not
	 * 
	 * @param token
	 * @param type
	 * @return
	 */
	private static EntryTokenAction tokenIsValid(final String token, final Type type) {
		EntryTokenAction ret = null;
		if (token != null && !token.trim().isEmpty()) {
			final EntryTokenAction ta = EntryTokenAction.findByToken(token, type);
			if (ta != null && ta.isValid()) {
				ret = ta;
			}
		}

		return ret;
	}

	public Result resetPassword(final String token) {
		com.feth.play.module.pa.controllers.AuthenticateDI.noCache(response());
		final EntryTokenAction ta = tokenIsValid(token, Type.PASSWORD_RESET);
		if (ta == null) {
			return badRequest(ViewNoTokenOrInvalid.render());
		}

		return ok(ViewPasswordReset.render(PASSWORD_RESET_FORM
				.fill(new ModelAuth.PasswordReset(token))));
	}

	public Result doResetPassword() {
		com.feth.play.module.pa.controllers.AuthenticateDI.noCache(response());
		final Form<ModelAuth.PasswordReset> filledForm = PASSWORD_RESET_FORM
				.bindFromRequest();
		if (filledForm.hasErrors()) {
			return badRequest(ViewPasswordReset.render(filledForm));
		} else {
			final String token = filledForm.get().token;
			final String newPassword = filledForm.get().password;

			final EntryTokenAction ta = tokenIsValid(token, Type.PASSWORD_RESET);
			if (ta == null) {
				return badRequest(ViewNoTokenOrInvalid.render());
			}
			final EntryUser u = ta.targetUser;
			try {
				// Pass true for the second parameter if you want to
				// automatically create a password and the exception never to
				// happen
				u.resetPassword(new ProviderUsernamePasswordAuthUser(newPassword),
						false);
			} catch (final RuntimeException re) {
				flash(Auth.FLASH_MESSAGE_KEY,
						Messages.get("playauthenticate.reset_password.message.no_password_account"));
			}
			final boolean login = ProviderUsernamePasswordAuth.getProvider()
					.isLoginAfterPasswordReset();
			if (login) {
				// automatically log in
				flash(Auth.FLASH_MESSAGE_KEY,
						Messages.get("playauthenticate.reset_password.message.success.auto_login"));

				return PlayAuthenticate.loginAndRedirect(ctx(),
						new ProviderLoginUsernamePasswordAuthUser(u.email));
			} else {
				// send the user to the login page
				flash(Auth.FLASH_MESSAGE_KEY,
						Messages.get("playauthenticate.reset_password.message.success.manual_login"));
			}
			return redirect(routes.ApplicationController.login());
		}
	}

	public Result oAuthDenied(final String getProviderKey) {
		com.feth.play.module.pa.controllers.AuthenticateDI.noCache(response());
		return ok(ViewOAuthDenied.render(getProviderKey));
	}

	public Result exists() {
		com.feth.play.module.pa.controllers.AuthenticateDI.noCache(response());
		PageAuthSignup page = new PageAuthSignup(getSession(), this.onRenderListener);
		Result result = page.renderExists();

		return result;
	}

	public Result verify(final String token) {
		com.feth.play.module.pa.controllers.AuthenticateDI.noCache(response());
		final EntryTokenAction ta = tokenIsValid(token, Type.EMAIL_VERIFICATION);
		if (ta == null) {
			return badRequest(ViewNoTokenOrInvalid.render());
		}
		final String email = ta.targetUser.email;
		EntryUser.verify(ta.targetUser);
		flash(Auth.FLASH_MESSAGE_KEY,
				Messages.get("playauthenticate.verify_email.success", email));
		if (getSession().getCurrentUser().isPresent()) {
			return redirect(routes.ApplicationController.index());
		} else {
			return redirect(routes.ApplicationController.login());
		}
	}
}
