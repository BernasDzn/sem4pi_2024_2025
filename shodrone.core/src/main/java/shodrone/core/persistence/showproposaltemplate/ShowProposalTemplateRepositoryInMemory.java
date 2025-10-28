package shodrone.core.persistence.showproposaltemplate;

import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;


import shodrone.core.domain.showproposal.ShowProposalTemplate;

import java.nio.file.Path;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;


public class ShowProposalTemplateRepositoryInMemory extends InMemoryDomainRepository<ShowProposalTemplate, Long> implements ShowProposalTemplateRepository {

    @Override
    public Iterable<ShowProposalTemplate> findAllShowProposalTemplates() {
        return findAll();
    }

    public Optional<ShowProposalTemplate> findByPath(final String filePath) {
        return matchOne(e -> e.getFilePath().equals(filePath));
    }

    @Override
    public Iterable<ShowProposalTemplate> findCurrentShowProposalTemplate() {
        List<ShowProposalTemplate> all = (List<ShowProposalTemplate>) match(x -> true);
        return all.stream().max(Comparator.comparing(ShowProposalTemplate::getAdditionDate)).stream().toList();
    }

}
