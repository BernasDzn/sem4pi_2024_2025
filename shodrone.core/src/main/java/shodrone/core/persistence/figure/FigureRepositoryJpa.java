package shodrone.core.persistence.figure;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import shodrone.core.domain.figure.FigureName;
import shodrone.core.domain.figure.Figure;
import shodrone.core.domain.figure.FigureStatus;
import shodrone.core.domain.figure.figurekeyword.FigureKeyword;
import shodrone.core.domain.figurecategory.FigureCategory;
import shodrone.core.infrastructure.Application;

import java.util.*;

public class FigureRepositoryJpa extends JpaAutoTxRepository<Figure, FigureName, FigureName> implements FigureRepository {

    public FigureRepositoryJpa(final TransactionalContext autoTx) {
        super(autoTx, "code");
    }

    public FigureRepositoryJpa(final String pucode) {
        super(pucode, Application.settings().getExtendedPersistenceProperties(), "code");
    }

    @Override
    public Iterable<Figure> figures() {
        return findAll();
    }

    @Override
    public Optional<Figure> findByName(final FigureName name){
        return match("e.figureName=:name", "name",name).stream().findFirst();
    }

    @Override
    public Iterable<Figure> findAllComissioned() {
        return match("e.status =:status", "status", FigureStatus.COMISSIONED);
    }

    @Override
    public Iterable<Figure> findAllCategoryFigures(FigureCategory selectedCategory) {
        List<Figure> figures = new ArrayList<>();
        for (Figure figure : findAll()) {
            figures.add(figure);
        }
        return figures.stream().filter(c -> c.getCategory().equals(selectedCategory)).toList();
    }

    @Override
    public Iterable<Figure> allFiguresWithKeyword(String keyword) {
        List<Figure> figures = new ArrayList<>();
        for (Figure figure : findAll()) {
            figures.add(figure);
        }
        return figures.stream().filter(c -> c.getKeywords().parallelStream().map(FigureKeyword::getKeyword).anyMatch(k -> k.equalsIgnoreCase(keyword))).toList();
    }
}
