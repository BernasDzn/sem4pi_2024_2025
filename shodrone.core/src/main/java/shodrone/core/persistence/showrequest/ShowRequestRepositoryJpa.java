package shodrone.core.persistence.showrequest;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import shodrone.core.domain.common.Date;
import shodrone.core.domain.common.Place;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.showrequest.ShowRequest;
import shodrone.core.infrastructure.Application;
import java.util.Optional;

public class ShowRequestRepositoryJpa extends JpaAutoTxRepository<ShowRequest, Long, Long>
        implements ShowRequestRepository {

    public ShowRequestRepositoryJpa(final TransactionalContext autoTx) {
        super(autoTx, "id");
    }

    public ShowRequestRepositoryJpa(String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(), "id");
    }

    @Override
    public Iterable<ShowRequest> findAllShowRequests() {
        return findAll();
    }

    @Override
    public Iterable<ShowRequest> findShowRequestsByCustomer(Customer c) {
        return match("e.customer = :customer", "customer", c);
    }

    @Override
    public Iterable<ShowRequest> findShowRequestsByAuthor(SystemUser author) {
        return match("e.author = :author", "author", author);
    }

    @Override
    public Iterable<ShowRequest> findShowRequestsByCustomerAndAuthor(Customer customer, SystemUser author) {
        return match("e.customer = :customer and e.author = :author", "customer", customer,
                "author", author);
    }

    @Override
    public Optional<ShowRequest> findShowRequestsByDateAndPlace(Place place, Date date) {
        return match("e.place = :place and FUNCTION('TO_CHAR', e.date, 'DD/MM/YYYY HH24:MI') = :date", "place", place,
                "date", date.toString()).stream().findFirst();
    }
}
