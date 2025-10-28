package shodrone.core.persistence.figure;

import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import shodrone.core.domain.figure.Figure;
import shodrone.core.domain.figure.FigureName;
import shodrone.core.domain.figure.FigureStatus;
import shodrone.core.domain.figure.figurekeyword.FigureKeyword;
import shodrone.core.domain.figurecategory.FigureCategory;

import java.util.Optional;


public class FigureRepositoryInMemory extends InMemoryDomainRepository<Figure, FigureName> implements FigureRepository {
    @Override
    public Iterable<Figure> figures() {
        return findAll();
    }


    @Override
    public Optional<Figure> findByName(final FigureName name){
        return matchOne(e -> e.getFigureName().equals(name));
    }

    @Override
    public Iterable<Figure> findAllComissioned() {
        return match(f ->f.getStatus().equals(FigureStatus.COMISSIONED));
    }

    @Override
    public Iterable<Figure> findAllCategoryFigures(FigureCategory selectedCategory) {
       return match(f ->f.getCategory().equals(selectedCategory) && f.getStatus().equals(FigureStatus.COMISSIONED));

    }

    @Override
    public Iterable<Figure> allFiguresWithKeyword(String keyword) {
        return match(f ->(f.getKeywords().contains(FigureKeyword.valueOf(keyword))) && f.getStatus().equals(FigureStatus.COMISSIONED));

    }
}
