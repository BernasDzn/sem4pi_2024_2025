package shodrone.core.persistence.show;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.show.Show;
import shodrone.core.infrastructure.Application;

public class ShowRepositoryJpa 
	extends JpaAutoTxRepository<Show, Long, Long>
	implements ShowRepository{

	public ShowRepositoryJpa(final TransactionalContext autoTx) {
        super(autoTx, "pk");
    }

    public ShowRepositoryJpa(final String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(),
                "pk");
    }

	@Override
	public Iterable<Show> findAllShows() {
		return findAll();
	}

	@Override
	public Iterable<Show> findAllShowsByCustomerId(Customer customer) {
		return match("e.showProposal.showRequest.customer = :customer", "customer", customer);
	}
	

	@Override
	public Iterable<Show> findAllScheduledShowsByCustomerId(Customer customer) {
		return match("e.showProposal.showRequest.customer = :customer and e.showProposal.date.date > current_timestamp", "customer", customer);
	}
}
