package shodrone.core.persistence.customer;

import java.util.Optional;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.customer.CustomerStatus;
import shodrone.core.domain.customer.VATNumber;
import shodrone.core.domain.customer_rep.Representative;
import shodrone.core.infrastructure.Application;

public class CustomerRepositoryJpa
        extends JpaAutoTxRepository<Customer, VATNumber, VATNumber>
        implements CustomerRepository {

    public CustomerRepositoryJpa(final TransactionalContext autoTx) {
        super(autoTx, "vatNumber");
    }

    public CustomerRepositoryJpa(final String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(),
                "vatNumber");
    }

    @Override
    public Iterable<Customer> customers() {
        return findAll();
    }

    @Override
    public Iterable<Customer> findAllActive() {
        return match("e.status != DELETED AND e.status != INFRINGEMENT");
    }

    @Override
    public Iterable<Customer> findAllByStatus(CustomerStatus status) {
        return match("e.status = :status", "status", status);
    }

    @Override
    public Iterable<Representative> findRepresentativesOf(Customer selectedCustomer) {
        Iterable<Customer> customer = match("e.vatNumber = :vatNumber", "vatNumber", selectedCustomer.getVatNumber());
        return customer.iterator().next().getRepresentatives();
    }

    @Override
    public Optional<Customer> findByRepresentative(Representative representative) {
        Iterable<Customer> customers = match(":representative MEMBER OF e.representatives", "representative", representative);
        if (customers.iterator().hasNext()) {
            return Optional.of(customers.iterator().next());
        } else {
            return Optional.empty();
        }
    }

}
