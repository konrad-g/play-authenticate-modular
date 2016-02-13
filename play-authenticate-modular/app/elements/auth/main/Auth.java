package elements.auth.main;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 13/02/16.
 */
public class Auth {

    public static final String FLASH_MESSAGE_KEY = "message";
    public static final String FLASH_ERROR_KEY = "error";
    public static final String USER_ROLE = "user";

    /**
     * Returns a token object if valid, null if not
     *
     * @param token
     * @param type
     * @return
     */
    public static EntryTokenAction isTokenValid(final String token, final EntryTokenAction.Type type) {
        EntryTokenAction ret = null;
        if (token != null && !token.trim().isEmpty()) {
            final EntryTokenAction ta = EntryTokenAction.findByToken(token, type);
            if (ta != null && ta.isValid()) {
                ret = ta;
            }
        }

        return ret;
    }
}
