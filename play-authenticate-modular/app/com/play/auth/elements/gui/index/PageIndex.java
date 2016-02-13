package com.play.auth.elements.gui.index;

import com.play.auth.elements.common.OnRenderListener;
import com.play.auth.elements.gui.base.ContentInner;
import com.play.auth.elements.session.Session;
import play.i18n.Messages;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;
import com.play.auth.elements.gui.index.html.ViewIndex;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 12/02/16.
 */
public class PageIndex {

    private Session session;
    private OnRenderListener onRenderListener;

    public PageIndex(Session session, OnRenderListener onRenderListener) {
        this.session = session;
        this.onRenderListener = onRenderListener;
    }

    public Result render() {

        boolean disableIndexing = true;
        String title = Messages.get("playauthenticate.index.intro");
        String description = Messages.get("playauthenticate.index.intro.description");
        String keywords = Messages.get("playauthenticate.index.intro.keywords");

        Content content = ViewIndex.render();

        ContentInner contentInner = new ContentInner(title, description, keywords, content);
        return Results.ok(this.onRenderListener.onRender(contentInner, disableIndexing));
    }
}
