package shodrone.core.persistence.figure;

import java.util.Optional;

import eapli.framework.domain.repositories.DomainRepository;
import shodrone.core.domain.figure.FigureName;
import shodrone.core.domain.figure.Figure;
import shodrone.core.domain.figurecategory.FigureCategory;


/**
 * @author Jorge Santos ajs@isep.ipp.pt 02/04/2016
 */
public interface FigureRepository extends DomainRepository<FigureName, Figure> {
    Iterable<Figure> figures();

    Optional<Figure> findByName(FigureName name);

    Iterable<Figure> findAllComissioned();

    Iterable<Figure> findAllCategoryFigures(FigureCategory selectedCategory);

    Iterable<Figure> allFiguresWithKeyword(String keyword);


}
