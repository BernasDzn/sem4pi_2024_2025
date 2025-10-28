package shodrone.core.persistence.customer_rep;

import eapli.framework.general.domain.model.EmailAddress;
import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import shodrone.core.domain.customer_rep.Representative;

public class RepresentativeRepositoryInMemory
        extends InMemoryDomainRepository<Representative, EmailAddress>
        implements RepresentativeRepository
{

    @Override
    public Iterable<Representative> representatives() {
        return findAll();
    }

    @Override
    public Iterable<Representative> findAllActive() {
        return match(e -> e.getStatus().isActive());
    }


}
