package elements.auth.gui.password_remind;

import elements.common.OnRenderListener;
import elements.session.Session;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 12/02/16.
 */
public class PageAuthRemindPassword {

    private Session session;
    private OnRenderListener onRenderListener;

    public PageAuthRemindPassword(Session session, OnRenderListener onRenderListener) {
        this.session = session;
        this.onRenderListener = onRenderListener;
    }
}
