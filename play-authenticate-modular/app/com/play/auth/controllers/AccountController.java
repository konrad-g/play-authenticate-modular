package com.play.auth.controllers;

import com.play.auth.elements.auth.gui.account.PageAuthAccount;
import com.play.auth.elements.auth.gui.password_change.PageAuthChangePassword;
import com.play.auth.elements.auth.main.*;
import be.objectify.deadbolt.java.actions.Restrict;
import be.objectify.deadbolt.java.actions.Group;
import be.objectify.deadbolt.java.actions.SubjectPresent;

import play.mvc.Result;

import static play.data.Form.form;

/**
 * Controller that consists user specific authentication methods
 */
public class AccountController extends BaseController {

    @SubjectPresent
    public Result link() {
        PageAuthAccount page = new PageAuthAccount(getSession(), this.onRenderListener);
        return page.renderLink();
    }

    @Restrict(@Group(Auth.USER_ROLE))
    public Result verifyEmail() {
        PageAuthAccount page = new PageAuthAccount(getSession(), this.onRenderListener);
        return page.renderVerifyEmail();
    }

    @Restrict(@Group(Auth.USER_ROLE))
    public Result changePassword() {
        PageAuthChangePassword page = new PageAuthChangePassword(getSession(), this.onRenderListener);
        return page.renderChangePassword();
    }

    @Restrict(@Group(Auth.USER_ROLE))
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
