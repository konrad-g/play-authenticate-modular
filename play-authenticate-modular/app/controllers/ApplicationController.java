package controllers;

import elements.auth.main.Auth;
import elements.gui.restricted.PageRestricted;
import elements.gui.index.PageIndex;
import elements.gui.profile.PageProfile;
import play.mvc.Result;

import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.Restrict;

/**
 * Main app pages
 */
public class ApplicationController extends BaseController {
	
	public Result index() {
		PageIndex page = new PageIndex(getSession(), this.onRenderListener);
		return page.render();
	}

	@Restrict(@Group(Auth.USER_ROLE))
	public Result restricted() {
		PageRestricted page = new PageRestricted(getSession(), this.onRenderListener);
		return page.renderRestricted();
	}

	@Restrict(@Group(Auth.USER_ROLE))
	public Result profile() {
		PageProfile pageProfile = new PageProfile(getSession(), this.onRenderListener);
		return pageProfile.render();
	}
}