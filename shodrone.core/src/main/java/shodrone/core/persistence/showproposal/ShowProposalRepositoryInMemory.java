package shodrone.core.persistence.showproposal;

import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;

import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.showproposal.ShowProposal;
import shodrone.core.domain.showproposal.ShowProposalStatus;
import shodrone.core.domain.showrequest.ShowRequest;

import java.util.Optional;


public class ShowProposalRepositoryInMemory extends InMemoryDomainRepository<ShowProposal, Long> implements ShowProposalRepository {

    @Override
    public Iterable<ShowProposal> findAllShowProposals() {
        return findAll();
    }
    @Override
    public Iterable<ShowProposal> findAllShowProposalsReadyToTest() {
        return match(f ->f.getStatus().equals(ShowProposalStatus.CREATED));

    }

    public Optional<ShowProposal> findShowProposalsById(Long proposalId){
        return matchOne(e-> e.identity().equals(proposalId));
    }


    @Override
    public Optional<ShowProposal> findByShowRequest(final ShowRequest showRequest) {
        return matchOne(e -> e.getShowRequest().equals(showRequest));
    }

    public Iterable<ShowProposal> findAllCustomerProposals(Customer selectedCustomer) {
        return match(f ->f.getShowRequest().getCustomer().equals(selectedCustomer) && f.getStatus().equals(ShowProposalStatus.WAITING_APPROVAL));

    }

    public Iterable<ShowProposal> findAllCustomerProposalsReadyToTest(Customer selectedCustomer) {
        return match(f ->f.getShowRequest().getCustomer().equals(selectedCustomer) && f.getStatus().equals(ShowProposalStatus.CREATED));

    }

}
