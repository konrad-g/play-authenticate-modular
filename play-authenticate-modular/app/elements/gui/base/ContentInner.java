package elements.gui.base;

import play.twirl.api.Content;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 10/01/16.
 */
public class ContentInner {

    public String title;
    public String description;
    public String keywords;
    public Content content;

    public ContentInner(String title, String description, String keywords, Content content) {
        this.title = title;
        this.description = description;
        this.keywords = keywords;
        this.content = content;
    }
}
