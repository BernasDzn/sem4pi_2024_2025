package shodrone.core.domain.common;

import eapli.framework.general.domain.model.EmailAddress;
import shodrone.core.infrastructure.Application;

public class ShodroneEmail extends EmailAddress {

    public ShodroneEmail(final String email) {
        super(email);
        if (!isValidEmail(email)) {
            throw new IllegalArgumentException("Invalid email address");
        }
    }

    private boolean isValidEmail(String email) {
        // Check if the email contains the domain name
        return email != null && email.endsWith("@" + Application.settings().getShodroneDomain());
    }

}
