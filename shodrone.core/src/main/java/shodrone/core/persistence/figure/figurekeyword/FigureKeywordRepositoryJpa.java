package shodrone.core.persistence.figure.figurekeyword;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import shodrone.core.domain.figure.figurekeyword.FigureKeyword;
import shodrone.core.infrastructure.Application;


import java.util.Optional;

public class FigureKeywordRepositoryJpa extends JpaAutoTxRepository<FigureKeyword, String, String>
        implements FigureKeywordRepository {

    public FigureKeywordRepositoryJpa(final TransactionalContext autoTx) {
        super(autoTx, "keyword");
    }

    public FigureKeywordRepositoryJpa(final String putag) {
        super(putag, Application.settings().getExtendedPersistenceProperties(), "keyword");
    }

    @Override
    public Iterable<FigureKeyword> keywords() {
        return findAll();
    }

    @Override
    public Optional<FigureKeyword> findByName(final String keyword) {
        return match("e.keyword=:keyword", "keyword", keyword).stream().findFirst();
    }

}
