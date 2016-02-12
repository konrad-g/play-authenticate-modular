package elements.auth.main;

import controllers.AccountController;
import play.data.format.Formats;
import play.data.validation.Constraints;
import play.i18n.Messages;

/**
 * Created by Konrad Gadzinowski<kgadzinowski@gmail.com> on 12/02/16.
 */
public class ModelAuth {

    public static class Identity {

        public Identity() {
        }

        public Identity(final String email) {
            this.email = email;
        }

        @Constraints.Required
        @Constraints.Email
        public String email;

    }

    public static class Login extends Identity
            implements
            com.feth.play.module.pa.providers.password.UsernamePasswordAuthProvider.UsernamePassword {

        @Constraints.Required
        @Constraints.MinLength(5)
        public String password;

        @Override
        public String getEmail() {
            return email;
        }

        @Override
        public String getPassword() {
            return password;
        }
    }

    public static class Signup extends Login {

        @Constraints.Required
        @Constraints.MinLength(5)
        public String repeatPassword;

        @Constraints.Required
        public String name;

        public String validate() {
            if (password == null || !password.equals(repeatPassword)) {
                return Messages
                        .get("playauthenticate.password.signup.error.passwords_not_same");
            }
            return null;
        }
    }

    public static class Accept {

        @Constraints.Required
        @Formats.NonEmpty
        public Boolean accept;

        public Boolean getAccept() {
            return accept;
        }

        public void setAccept(Boolean accept) {
            this.accept = accept;
        }

    }

    public static class PasswordChange {
        @Constraints.MinLength(5)
        @Constraints.Required
        public String password;

        @Constraints.MinLength(5)
        @Constraints.Required
        public String repeatPassword;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getRepeatPassword() {
            return repeatPassword;
        }

        public void setRepeatPassword(String repeatPassword) {
            this.repeatPassword = repeatPassword;
        }

        public String validate() {
            if (password == null || !password.equals(repeatPassword)) {
                return Messages
                        .get("playauthenticate.change_password.error.passwords_not_same");
            }
            return null;
        }
    }

    public static class PasswordReset extends PasswordChange {

        public PasswordReset() {
        }

        public PasswordReset(final String token) {
            this.token = token;
        }

        public String token;

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
