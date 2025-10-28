package shodrone.core.domain.common;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.framework.domain.model.ValueObject;
import jakarta.persistence.Embeddable;
import jakarta.xml.bind.annotation.XmlAttribute;

import java.io.Serial;

/**
 * Represents a postal code.
 */
@Embeddable
public class PostalCode implements ValueObject, Comparable<PostalCode> {

    @Serial
    private static final long serialVersionUID = 1L;

    /**
     * The postal code of the address.
     * <br><br>
     * A better implementation would probably require a "SEPARATOR" constant or similar
     * but for now, we are just using a patter matcher to validate the postal code.
     */
    @XmlAttribute
    @JsonProperty
    private String postalCode;

    /**
     * Constructor for creating a PostalCode object.
     * @param country
     * @param postalCode
     */
    public PostalCode(Country country, String postalCode) {
        if(PostalCodeValidator.isValid(country, postalCode)) {
            this.postalCode = postalCode;
        }
    }

    public PostalCode() {} // Default constructor for ORM

    @Override
    public String toString() {
        return postalCode;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        PostalCode that = (PostalCode) obj;

        return postalCode != null ? postalCode.equals(that.postalCode) : that.postalCode == null;
    }

    @Override
    public int compareTo(PostalCode o) {
        if (o == null) {
            return 1; // this object is greater than null
        }
        return this.postalCode.compareTo(o.postalCode);
    }
}
