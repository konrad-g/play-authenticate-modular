import java.util.Arrays;

import controllers.ApplicationController;
import elements.auth.main.Auth;
import elements.auth.main.EntrySecurityRole;

import com.feth.play.module.pa.PlayAuthenticate;
import com.feth.play.module.pa.PlayAuthenticate.Resolver;
import com.feth.play.module.pa.exceptions.AccessDeniedException;
import com.feth.play.module.pa.exceptions.AuthException;

import controllers.routes;

import play.Application;
import play.GlobalSettings;
import play.mvc.Call;

public class Global extends GlobalSettings {

    static final String DEFAULT_LANG = "en";

    @Override
    public void onStart(Application app) {
        PlayAuthenticate.setResolver(new Resolver() {

            @Override
            public Call login() {
                // Your login page
                return routes.AuthController.login();
            }

            @Override
            public Call afterAuth() {
                // The user will be redirected to this page after authentication
                // if no original URL was saved
                return routes.ApplicationController.index();
            }

            @Override
            public Call afterLogout() {
                return routes.ApplicationController.index();
            }

            @Override
            public Call auth(final String provider) {
                // You can provide your own authentication implementation,
                // however the default should be sufficient for most cases
                return com.feth.play.module.pa.controllers.routes.AuthenticateDI
                        .authenticate(provider);
            }

            @Override
            public Call askMerge() {
                return routes.AccountController.askMerge();
            }

            @Override
            public Call askLink() {
                return routes.AccountController.askLink();
            }

            @Override
            public Call onException(final AuthException exception) {
                if (exception instanceof AccessDeniedException) {
                    return routes.AuthController
                            .oAuthDenied(((AccessDeniedException) exception)
                                    .getProviderKey());
                }

                // more custom problem handling here...
                return super.onException(exception);
            }
        });

        initialData();
    }

    private void initialData() {
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