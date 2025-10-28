package shodrone.core.persistence.customer_rep;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.general.domain.model.EmailAddress;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import shodrone.core.domain.customer_rep.Representative;
import shodrone.core.infrastructure.Application;

public class RepresentativeRepositoryJpa
    extends JpaAutoTxRepository<Representative, EmailAddress, EmailAddress>
    implements RepresentativeRepository
{

    public RepresentativeRepositoryJpa(final TransactionalContext autoTx) {
        super(autoTx, "representativeEmail");
    }

    public RepresentativeRepositoryJpa(final String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(),
                "representativeEmail");
    }

    @Override
    public Iterable<Representative> representatives() {
        return findAll();
    }

    @Override
    public Iterable<Representative> findAllActive() {
        return match("e.status == ACTIVE");
    }

}
