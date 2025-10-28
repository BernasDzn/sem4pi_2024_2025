package shodrone.core.persistence.showproposaltemplate;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;

import shodrone.core.domain.showproposal.ShowProposal;
import shodrone.core.domain.showproposal.ShowProposalTemplate;
import shodrone.core.infrastructure.Application;

import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static java.util.stream.StreamSupport.stream;

public class ShowProposalTemplateRepositoryJpa extends JpaAutoTxRepository<ShowProposalTemplate, Long, Long>
        implements ShowProposalTemplateRepository {

    public ShowProposalTemplateRepositoryJpa(final TransactionalContext autoTx) {
        super(autoTx, "id");
    }

    public ShowProposalTemplateRepositoryJpa(String puid) {
        super(puid, Application.settings().getExtendedPersistenceProperties(), "id");
    }

    @Override
    public Optional<ShowProposalTemplate> findByPath(final String filePath) {
        return match("e.filePath=:filePath", "filePath", filePath).stream().findFirst();
    }

    @Override
    public Iterable<ShowProposalTemplate> findAllShowProposalTemplates() {
        return findAll();
    }

    @Override
    public List<ShowProposalTemplate> findCurrentShowProposalTemplate() {
       return match("e.additionDate < :now ORDER BY e.additionDate DESC","now", Calendar.getInstance());
    }
}
