package elements.gui.base;

import elements.auth.main.EntryUser;
import elements.gui.base.html.ViewMain;
import elements.gui.base.html.ViewAjax;
import elements.session.Session;
import play.twirl.api.Content;

import java.util.Optional;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 11/12/15.
 */
public class LayoutBase {

    private Session session;

    public LayoutBase(Session session) {
        this.session = session;
    }

    public Content getContent(ContentInner innerContent, boolean disableIndexing) {
        Content content;

        boolean isAjax = isAjax();

        if (!isAjax) {
            // Full content
            Optional<EntryUser> user = this.session.getCurrentUser();
            content = ViewMain.render(
                    innerContent.title,
                    innerContent.description,
                    innerContent.keywords,
                    disableIndexing,
                    user,
                    innerContent.content
            );
        } else {
            // Only inner content
            content = ViewAjax.render(
                    innerContent.title,
                    innerContent.description,
                    innerContent.keywords,
                    innerContent.content
            );
        }

        return content;
    }

    private boolean isAjax() {
        boolean isAjax = this.session.request().headers().get("X-PJAX") != null;
        return isAjax;
    }
}
