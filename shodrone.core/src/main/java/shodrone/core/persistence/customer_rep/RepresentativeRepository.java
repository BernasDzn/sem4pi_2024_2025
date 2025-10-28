package shodrone.core.persistence.customer_rep;

import eapli.framework.domain.repositories.DomainRepository;
import eapli.framework.general.domain.model.EmailAddress;
import shodrone.core.domain.customer_rep.Representative;

import java.util.Optional;

public interface RepresentativeRepository extends DomainRepository<EmailAddress, Representative> {

    default Optional<Representative> findByEmailAddress(final EmailAddress email){
        return ofIdentity(email);
    }

    Iterable<Representative> representatives();

    Iterable<Representative> findAllActive();

}
