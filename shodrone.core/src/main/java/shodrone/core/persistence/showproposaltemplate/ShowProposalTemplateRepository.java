package shodrone.core.persistence.showproposaltemplate;

import eapli.framework.domain.repositories.DomainRepository;
import shodrone.core.domain.figure.Figure;
import shodrone.core.domain.figure.FigureName;
import shodrone.core.domain.showproposal.ShowProposalTemplate;

import java.nio.file.Path;
import java.util.Optional;

public interface ShowProposalTemplateRepository extends DomainRepository<Long, ShowProposalTemplate> {
    Iterable<ShowProposalTemplate> findAllShowProposalTemplates();
    Iterable<ShowProposalTemplate> findCurrentShowProposalTemplate();
    Optional<ShowProposalTemplate> findByPath(String filePath);



}