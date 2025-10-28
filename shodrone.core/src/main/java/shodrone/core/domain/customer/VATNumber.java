package shodrone.core.domain.customer;

import eapli.framework.domain.model.ValueObject;
import eapli.framework.validations.Preconditions;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import shodrone.core.domain.common.Country;

import java.io.Serial;

/**
 * Represents a VAT number
 */
@Embeddable
public class VATNumber implements ValueObject, Comparable<VATNumber> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The country code of the VAT Number
     * <br>
     * <br>
     * Using Country enum because it already has the ISO code
     */
    @Enumerated(EnumType.STRING)
    private Country countryCode;

    /**
     * The unique part of the VAT
     */
    private String uniqueNumber;

    public VATNumber() {
    } // for ORM

    /**
     * Constructor for VATNumber
     * 
     * @param vat
     */
    public VATNumber(String vat) {
        isValid(vat);
        String[] vatParts = separateVAT(vat);
        this.countryCode = Country.fromIsoCode(vatParts[0]);
        this.uniqueNumber = vatParts[1];
    }

    public static String[] separateVAT(String vat) {
        String[] vatParts = new String[2];
        vat = vat.trim();
        String countryCode = vat.substring(0, 2);
        String uniqueNumber = vat.substring(2);

        vatParts[0] = countryCode;
        vatParts[1] = uniqueNumber;
        return vatParts;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof VATNumber)) {
            return false;
        }

        final VATNumber that = (VATNumber) obj;
        return this.uniqueNumber.equals(that.uniqueNumber);
    }

    @Override
    public String toString() {
        return countryCode.getIsoCode() + uniqueNumber;
    }

    @Override
    public int compareTo(VATNumber o) {
        return uniqueNumber.compareTo(o.uniqueNumber);
    }

    // ========== Validation Section ===========
    // These methods are public and can be called anywhere in the code,
    // therefore, it is up to the caller to catch the exceptions.
    // This also means the UI can call these methods to validate the inputs.

    public static boolean isValid(String vat) {
        String[] vatParts;
        Country country;
        try {
            vat = vat.trim();
            vatParts = separateVAT(vat);
            country = Country.fromIsoCode(vatParts[0]);
        } catch (Exception e) {
            throw new IllegalArgumentException("VAT Number is not valid");
        }

        Preconditions.nonEmpty(vat, "VAT Number cannot be empty");
        VATValidator.isValid(country, vatParts[1]);

        return true;
    }

    public static VATNumber valueOf(String completeVAT) {
        isValid(completeVAT);
        return new VATNumber(completeVAT);
    }

}
