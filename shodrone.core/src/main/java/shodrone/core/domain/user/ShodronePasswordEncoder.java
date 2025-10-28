package shodrone.core.domain.user;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * This class is used to encode passwords using the BCrypt algorithm.
 */
public class ShodronePasswordEncoder implements PasswordEncoder {

    /**
     * The encoder. In this case, since the EAPLI framework imports the springframework
     * we decided to use BCrypt since it's also very well known and used in the
     * industry. It is also very secure and has a good performance.
     * <br><br>
     * Source: <a href="https://security.stackexchange.com/questions/4781/do-any-security-experts-recommend-bcrypt-for-password-storage">First Answer in this thread</a>
     */
    protected BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    @Override
    public String encode(CharSequence rawPassword) {
        return encoder.encode(rawPassword);
    }

    @Override
    public boolean matches(CharSequence rawPassword, String encodedPassword) {
        return encoder.matches(rawPassword, encodedPassword);
    }

}
