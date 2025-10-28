package shodrone.core.domain.customer_rep;

import com.fasterxml.jackson.annotation.JsonProperty;
import eapli.framework.domain.model.AggregateRoot;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.general.domain.model.EmailAddress;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import jakarta.persistence.*;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.Getter;
import lombok.Setter;

/**
 * Customer representative entity.
 */
@XmlRootElement
@Entity
public class Representative implements AggregateRoot<EmailAddress> {

    /**
     * ORM primary key. This is an implementation detail and is never exposed to the outside of the
     * class.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * ORM versioning field.
     */
    @Version
    private Long version;

    /**
     * A Customer representative is a SystemUser with access to the client Application.
     * Therefore, we can save a reference to the system user the representative is associated with.
     */
    @XmlElement
    @JsonProperty
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private SystemUser user;

    /**
     * Name of the representative.
     */
    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    @AttributeOverride(name = "name", column = @Column(name = "representativeName"))
    private Designation representativeName;

    /**
     * Email of the representative.
     * <br><br>
     * Business identity since we can create 2 representatives for 2 different companies with the same
     * name, position and status.
     * <br><br>
     * for example: "Foo Bar" "System Administrator" "CREATED"
     * <br><br>
     * but since they are from different companies, they have different company emails.
     */
    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    @Column(unique = true, nullable = false)
    private EmailAddress representativeEmail;

    /**
     * Position of the representative.
     */
    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    @AttributeOverride(name = "name", column = @Column(name = "representativePosition"))
    private Designation representativePosition;

    /**
     * Status of the representative.
     */
    @XmlElement
    @JsonProperty
    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private RepresentativeStatus status;

    public Representative(){} // for ORM

    public Representative(
        String repName, String repEmail, String repPosition, RepresentativeStatus repStatus, SystemUser repUser
    ){
        this.representativeName = Designation.valueOf(repName);
        this.representativeEmail = EmailAddress.valueOf(repEmail);
        this.representativePosition = Designation.valueOf(repPosition);
        this.status = repStatus;
        this.user = repUser;
    }

    public SystemUser user() {
        return this.user;
    }

    @Override
    public EmailAddress identity() {return this.representativeEmail;}

    @Override
    public boolean sameAs(Object other) {
        return false;
    }

}
