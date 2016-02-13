package com.play.auth.elements.gui.base;

import com.play.auth.elements.auth.main.EntryUser;
import com.play.auth.elements.gui.base.html.ViewMain;
import com.play.auth.elements.gui.base.html.ViewAjax;
import com.play.auth.elements.session.Session;
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
