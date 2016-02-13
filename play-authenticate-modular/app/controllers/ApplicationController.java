package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import elements.auth.gui.login.PageAuthLogin;
import elements.auth.gui.signup.PageAuthSignup;
import elements.auth.main.Auth;
import elements.auth.main.EntryUser;
import elements.gui.error.PageError;
import elements.gui.index.PageIndex;
import elements.gui.profile.PageProfile;
import play.Routes;
import play.mvc.Http.Session;
import play.mvc.Result;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.user.AuthUser;

import elements.gui.index.html.*;
import elements.gui.profile.html.*;
import elements.gui.error.html.*;
import elements.auth.gui.signup.html.*;
import elements.auth.gui.login.html.*;

/**
 * Custom app pages
 */
public class ApplicationController extends BaseController {
	
	public Result index() {
		PageIndex page = new PageIndex(getSession(), this.onRenderListener);
		return page.render();
	}

	@Restrict(@Group(Auth.USER_ROLE))
	public Result restricted() {
		PageError page = new PageError(getSession(), this.onRenderListener);
		return page.renderRestricted();
	}

	@Restrict(@Group(Auth.USER_ROLE))
	public Result profile() {
		PageProfile pageProfile = new PageProfile(getSession(), this.onRenderListener);
		return pageProfile.render();
	}
}