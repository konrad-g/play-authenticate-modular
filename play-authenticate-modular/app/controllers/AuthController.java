package controllers;

import elements.auth.gui.account.PageAuthAccount;
import elements.auth.gui.login.PageAuthLogin;
import elements.auth.gui.password_remind.PageAuthRemindPassword;
import elements.auth.gui.signup.PageAuthSignup;
import elements.auth.main.*;
import elements.auth.main.EntryTokenAction.Type;
import play.Routes;
import play.data.Form;
import play.i18n.Messages;
import play.mvc.Result;

import com.feth.play.module.pa.PlayAuthenticate;
import elements.auth.gui.account.html.*;
import elements.auth.gui.signup.html.*;
import elements.auth.gui.password_remind.html.*;

import static play.data.Form.form;

/**
 * Controller that consists all publicly available authentication methods
 */
public class AuthController extends BaseController {

	public Result login() {
		PageAuthLogin page = new PageAuthLogin(getSession(), this.onRenderListener);
		Result result = page.renderLogin();
		return result;
	}

	public Result doLogin() {
		PageAuthLogin page = new PageAuthLogin(getSession(), this.onRenderListener);
		Result result = page.doLogin();
		return result;
	}

	public Result signup() {
		PageAuthSignup page = new PageAuthSignup(getSession(), this.onRenderListener);
		Result result = page.renderSignup();
		return result;
	}

	public Result doSignup() {
		PageAuthSignup page = new PageAuthSignup(getSession(), this.onRenderListener);
		Result result = page.doSignup();
		return result;
	}

	public Result unverified() {
		PageAuthSignup page = new PageAuthSignup(getSession(), this.onRenderListener);
		return page.renderUnverified();
	}

	public Result remindPassword(final String email) {
		PageAuthRemindPassword page = new PageAuthRemindPassword(getSession(), this.onRenderListener);
		return page.renderRemindPassword(email);
	}

	public Result doRemindPassword() {
		PageAuthRemindPassword page = new PageAuthRemindPassword(getSession(), this.onRenderListener);
		return page.doRemindPassword();
	}

	public Result resetPassword(final String token) {
		PageAuthRemindPassword page = new PageAuthRemindPassword(getSession(), this.onRenderListener);
		return page.renderResetPassword(token);
	}

	public Result doResetPassword() {
		PageAuthRemindPassword page = new PageAuthRemindPassword(getSession(), this.onRenderListener);
		return page.doResetPassword();
	}

	public Result oAuthDenied(final String providerKey) {
		PageAuthAccount page = new PageAuthAccount(getSession(), this.onRenderListener);
		return page.renderOAuthDenied(providerKey);
	}

	public Result exists() {
		com.feth.play.module.pa.controllers.AuthenticateDI.noCache(response());
		PageAuthSignup page = new PageAuthSignup(getSession(), this.onRenderListener);
		Result result = page.renderExists();
		return result;
	}

	public Result verify(final String token) {
		PageAuthAccount page = new PageAuthAccount(getSession(), this.onRenderListener);
		return page.renderVerify(token);
	}

	public Result jsRoutes() {
		return ok(
				Routes.javascriptRouter("jsRoutes",
						routes.javascript.AuthController.remindPassword()))
				.as("text/javascript");
	}
}
