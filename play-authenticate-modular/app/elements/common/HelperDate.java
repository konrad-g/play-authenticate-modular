package elements.common;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 14/12/15.
 */
public class HelperDate {

    public static String formatTimestamp(final long t) {
        return formatTimestamp(new Date(t));
    }

    public static String formatTimestamp(final Date date) {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date);
    }

    public static String formatDateDay(final long t) {
        return formatDateDay(new Date(t));
    }

    public static String formatDateDay(final Date date) {
        return new SimpleDateFormat("yyyy-MM-dd").format(date);
    }
}