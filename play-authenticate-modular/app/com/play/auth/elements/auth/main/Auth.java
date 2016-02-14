package com.play.auth.elements.auth.main;

import java.util.Arrays;

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

    public static void addInitialData() {
        if (EntrySecurityRole.find.findRowCount() == 0) {
            for (final String roleName : Arrays
                    .asList(Auth.USER_ROLE)) {
                final EntrySecurityRole role = new EntrySecurityRole();
                role.roleName = roleName;
                role.save();
            }
        }
    }
}
