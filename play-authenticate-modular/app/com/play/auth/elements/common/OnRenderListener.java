package com.play.auth.elements.common;

import com.play.auth.elements.gui.base.ContentInner;
import play.mvc.Result;
import play.twirl.api.Content;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 29/01/16.
 */
public interface OnRenderListener {
    public Content onRender(ContentInner contentInner, boolean disableIndexing);

    public Result redirectToLogin();

    public Result redirectToAccount();

    public Result redirectToMain();

}