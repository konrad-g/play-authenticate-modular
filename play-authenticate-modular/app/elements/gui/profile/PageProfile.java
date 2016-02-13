package elements.gui.profile;

import elements.auth.main.EntryUser;
import elements.common.OnRenderListener;
import elements.gui.base.ContentInner;
import elements.session.Session;
import elements.gui.profile.html.*;
import play.i18n.Messages;
import play.mvc.Result;
import play.mvc.Results;
import play.twirl.api.Content;

import java.util.Optional;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 12/02/16.
 */
public class PageProfile {

    private Session session;
    private OnRenderListener onRenderListener;

    public PageProfile(Session session, OnRenderListener onRenderListener) {
        this.session = session;
        this.onRenderListener = onRenderListener;
    }

    public Result render() {

        final Optional<EntryUser> localUser = this.session.getCurrentUser();

        if (localUser.isPresent()) {
            boolean disableIndexing = false;
            String title = Messages.get("playauthenticate.profile.title");
            String description = Messages.get("playauthenticate.profile.description");
            String keywords = Messages.get("playauthenticate.profile.keywords");

            Content content = ViewProfile.render(localUser.get());

            ContentInner contentInner = new ContentInner(title, description, keywords, content);
            return Results.ok(this.onRenderListener.onRender(contentInner, disableIndexing));
        }

        return Results.forbidden();
    }
}
