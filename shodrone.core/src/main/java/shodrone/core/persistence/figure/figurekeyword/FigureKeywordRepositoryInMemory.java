package shodrone.core.persistence.figure.figurekeyword;

import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import shodrone.core.domain.figure.figurekeyword.FigureKeyword;

import java.util.Optional;


public class FigureKeywordRepositoryInMemory extends InMemoryDomainRepository<FigureKeyword, String> implements FigureKeywordRepository {
    @Override
    public Iterable<FigureKeyword> keywords() {
        return findAll();
    }


    @Override
    public Optional<FigureKeyword> findByName(final String keyword){
        return matchOne(e -> e.getKeyword().equals(keyword));
    }


}
