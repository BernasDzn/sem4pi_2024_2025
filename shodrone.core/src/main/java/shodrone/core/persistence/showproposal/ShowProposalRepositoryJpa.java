package shodrone.core.persistence.showproposal;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.showproposal.ShowProposal;
import shodrone.core.domain.showproposal.ShowProposalStatus;
import shodrone.core.domain.showrequest.ShowRequest;
import shodrone.core.infrastructure.Application;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ShowProposalRepositoryJpa extends JpaAutoTxRepository<ShowProposal, Long, Long>
        implements ShowProposalRepository {

    public ShowProposalRepositoryJpa(final TransactionalContext autoTx) {
        super(autoTx, "id");
    }

    public ShowProposalRepositoryJpa(String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(), "id");
    }

    @Override
    public Iterable<ShowProposal> findAllShowProposals() {
        return findAll();
    }

    @Override
    public Iterable<ShowProposal> findAllShowProposalsReadyToTest() {
        return match("e.status=:status", "status", ShowProposalStatus.CREATED);
    }


    @Override
    public Optional<ShowProposal> findByShowRequest(final ShowRequest showRequest) {
        return match("e.showRequest=:showRequest", "showRequest", showRequest).stream().findFirst();
    }

    @Override
    public Optional<ShowProposal> findShowProposalsById(final Long identity) {
        return match("e.id=:id", "id", identity).stream().findFirst();
    }


    @Override
    public Iterable<ShowProposal>  findAllCustomerProposals(Customer selectedCustomer) {
        List<ShowProposal> proposals = new ArrayList<>();
        for (ShowProposal proposal : findAll()) {
            proposals.add(proposal);
        }
        return proposals.stream().filter(c -> c.getShowRequest().getCustomer().equals(selectedCustomer)).toList();
    }

    @Override
    public Iterable<ShowProposal>  findAllCustomerProposalsReadyToTest(Customer selectedCustomer) {
        List<ShowProposal> proposals = new ArrayList<>();
        for (ShowProposal proposal : findAll()) {
             proposals.add(proposal);
        }
        return proposals.stream().filter(c -> c.getShowRequest().getCustomer().equals(selectedCustomer)).filter(p -> p.getStatus() == ShowProposalStatus.CREATED).toList();
    }
}
