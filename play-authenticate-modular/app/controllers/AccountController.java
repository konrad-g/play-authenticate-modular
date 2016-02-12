package controllers;

import elements.auth.gui.account.PageAuthAccount;
import elements.auth.gui.password_change.PageAuthChangePassword;
import elements.auth.main.EntryUser;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.SubjectPresent;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;

import elements.auth.main.ModelAuth;
import elements.gui.profile.PageProfile;
import play.data.Form;
import play.data.format.Formats.NonEmpty;
import play.data.validation.Constraints.MinLength;
import play.data.validation.Constraints.Required;
import play.i18n.Messages;
import play.mvc.Result;
import elements.auth.main.ProviderUsernamePasswordAuth;
import elements.auth.main.ProviderUsernamePasswordAuthUser;
import static play.data.Form.form;

public class AccountController extends BaseController {


	@SubjectPresent
	public Result link() {
		PageAuthAccount page = new PageAuthAccount(getSession(), this.onRenderListener);
		return page.renderLink();
	}

	@Restrict(@Group(ApplicationController.USER_ROLE))
	public Result verifyEmail() {
		PageAuthAccount page = new PageAuthAccount(getSession(), this.onRenderListener);
		return page.renderVerifyEmail();
	}

	@Restrict(@Group(ApplicationController.USER_ROLE))
	public Result changePassword() {
		PageAuthChangePassword page = new PageAuthChangePassword(getSession(), this.onRenderListener);
		return page.renderChangePassword();
	}

	@Restrict(@Group(ApplicationController.USER_ROLE))
	public Result doChangePassword() {
		PageAuthChangePassword page = new PageAuthChangePassword(getSession(), this.onRenderListener);
		return page.doChangePassword();
	}

	@SubjectPresent
	public Result askLink() {
		PageAuthAccount page = new PageAuthAccount(getSession(), this.onRenderListener);
		return page.renderAskLink();
	}

	@SubjectPresent
	public Result doLink() {
		PageAuthAccount page = new PageAuthAccount(getSession(), this.onRenderListener);
		return page.doLink();
	}

	@SubjectPresent
	public Result askMerge() {
		PageAuthAccount page = new PageAuthAccount(getSession(), this.onRenderListener);
		return page.renderAskMerge();
	}

	@SubjectPresent
	public Result doMerge() {
		PageAuthAccount page = new PageAuthAccount(getSession(), this.onRenderListener);
		return page.doMerge();
	}

}
