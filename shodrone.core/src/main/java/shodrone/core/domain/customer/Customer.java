package shodrone.core.domain.customer;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.domain.model.DomainEntities;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.general.domain.model.EmailAddress;
import eapli.framework.representations.RepresentationBuilder;
import eapli.framework.representations.Representationable;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;
import shodrone.core.domain.common.Address;
import shodrone.core.domain.common.StringValidator;
import shodrone.core.domain.customer_rep.Representative;

import java.util.HashSet;
import java.util.Set;

/**
 * Customer entity.
 */
@XmlRootElement
@Entity
public class Customer implements AggregateRoot<VATNumber>, Representationable {

    /**
     * ORM primary key. This is an implementation detail and is never exposed to the outside of the
     * class.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;

    /**
     * ORM versioning field.
     */
    @Version
    private Long version;

    /**
     * VAT Number of the customer. (Typically another european company)
     * <br><br>
     * Business identity. Thus unique and not nullable.
     */
    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    @Column(unique = true, nullable = false)
    private VATNumber vatNumber;

    /**
     * Name of the customer.
     * <br><br>
     * Override the name of the column in the database to be customerName.
     */
    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    @AttributeOverride(name = "name", column = @Column(name = "customerName", nullable = false))
    private Designation customerName;
    public static final int CUSTOMER_NAME_MAX_LENGTH = 255;

    /**
     * Email address of the customer.
     */
    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    private EmailAddress emailAddress;

    /**
     * Address of the customer.
     */
    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    private Address address;

    /**
     * Status of the customer.
     * To see all possible statuses, see {@link CustomerStatus}
     */
    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private CustomerStatus status;

    /**
     * Set of customer representatives.
     * <br><br>
     * One to Many relationship since a customer can have multiple representatives.
     */
    @XmlElement
    @JsonProperty
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Set<Representative> representatives;

    /**
     * Default constructor for ORM
     */
    protected Customer(){}

    /**
     * Constructor for the customer.
     * Without the representatives.
     * @param completeVAT
     * @param customerName
     * @param streetName
     * @param addressNumber
     * @param postalCode
     * @param country
     * @param city
     */
    public Customer(String completeVAT, String customerName, String email, String streetName, int addressNumber, String postalCode, String country, String city) {
        new Customer(completeVAT, customerName, email, streetName, addressNumber, postalCode, country, city, null);
    }

    public Customer(
        String completeVAT, String customerName, String email, String streetName, 
        int addressNumber, String postalCode, String country, String city,
        Representative customerRepresentative
    ){

        VATNumber vat = new VATNumber(completeVAT);
        isValidCustomerName(customerName);
        EmailAddress emailAddress = EmailAddress.valueOf(email);
        Address address = new Address(streetName, addressNumber, postalCode, country, city);
        CustomerStatus status = CustomerStatus.CREATED;
        Set<Representative> customerRepresentatives = new HashSet<>();
        customerRepresentatives.add(customerRepresentative);

        this.vatNumber = vat;
        this.customerName = Designation.valueOf(customerName);
        this.emailAddress = emailAddress;
        this.address = address;
        this.status = status;
        this.representatives = customerRepresentatives;
    }

    public Customer(String completeVAT, String cName, String cEmail, Address cAddress){
        this.vatNumber = VATNumber.valueOf(completeVAT);
        this.customerName = Designation.valueOf(cName);
        this.emailAddress = EmailAddress.valueOf(cEmail);
        this.address = cAddress;
    }

    public boolean isActive() {
        return this.status.isActive();
    }

    public boolean isVip() {return this.status.isVip();}

    @Override
    public boolean equals(Object obj) {return DomainEntities.areEqual(this, obj);}

    @Override
    public int hashCode() {return DomainEntities.hashCode(this);}

    @Override
    public boolean sameAs(Object other) {

        if (!(other instanceof Customer that)) {
            return false;
        }
        if (this == that) {
            return true;
        }
        return identity().equals(that.identity()) &&
                this.vatNumber.equals(that.vatNumber) &&
                this.customerName.equals(that.customerName) &&
                this.address.equals(that.address) &&
                this.status.equals(that.status) &&
                this.representatives.equals(that.representatives);
    }

    @Override
    public boolean hasIdentity(final VATNumber id) {
        return id.equals(this.vatNumber);
    }

    @Override
    public VATNumber identity() {return this.vatNumber;}

    @Override
    public String toString() {
        return this.customerName.toString();
    }

    @Override
    public <R> R buildRepresentation(RepresentationBuilder<R> builder) {
        return null;
    }

    public int getNumberOfRepresentatives() {
        return representatives.size();
    }

    public Set<Representative> getRepresentatives() {
        return representatives;
    }

    public void addRepresentative(Representative representative) {
        this.representatives.add(representative);
    }

    // ========== Validation Section ===========
    // These methods are public and can be called anywhere in the code,
    // therefore, it is up to the caller to catch the exceptions.
    // This also means the UI can call these methods to validate the inputs.

    public static boolean isValidCustomerName(String customerName) {
        return StringValidator.isValid(customerName, "Customer name", CUSTOMER_NAME_MAX_LENGTH);
    }


}
