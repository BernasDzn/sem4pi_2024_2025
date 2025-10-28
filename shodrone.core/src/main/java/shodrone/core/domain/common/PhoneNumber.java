package shodrone.core.domain.common;

import eapli.framework.domain.model.ValueObject;

import java.io.Serial;

public class PhoneNumber implements ValueObject {

    @Serial
    private static final long serialVersionUID = 1L;

    @SuppressWarnings("unused")
    private int phoneNumber;

    public PhoneNumber() {
    } // for ORM

    public PhoneNumber(int phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
