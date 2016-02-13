package elements.gui.restricted;

import elements.auth.main.EntryUser;
import elements.common.OnRenderListener;
import elements.gui.base.ContentInner;
import elements.session.Session;
import play.i18n.Messages;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;
import elements.gui.restricted.html.ViewRestricted;
/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 12/02/16.
 */
public class PageRestricted {

    private Session session;
    private OnRenderListener onRenderListener;

    public PageRestricted(Session session, OnRenderListener onRenderListener) {
        this.session = session;
        this.onRenderListener = onRenderListener;
    }

    public Result renderRestricted() {

        boolean disableIndexing = true;
        String title = Messages.get("playauthenticate.navigation.restricted");
        String description = Messages.get("playauthenticate.navigation.restricted.description");
        String keywords = Messages.get("playauthenticate.navigation.restricted.keywords");

        Content content = ViewRestricted.render();

        ContentInner contentInner = new ContentInner(title, description, keywords, content);
        return Results.ok(this.onRenderListener.onRender(contentInner, disableIndexing));
    }
}
