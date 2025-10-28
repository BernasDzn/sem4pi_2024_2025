package shodrone.core.persistence.figurecategory;

import eapli.framework.domain.repositories.DomainRepository;
import eapli.framework.general.domain.model.Designation;
import shodrone.core.domain.figurecategory.FigureCategory;

import java.util.Optional;

/**
 * @author Jorge Santos ajs@isep.ipp.pt 02/04/2016
 */
public interface FigureCategoryRepository extends DomainRepository<Designation, FigureCategory> {
    Iterable<FigureCategory> figureCategories();

    Optional<FigureCategory> findByName(Designation name);

    Iterable<FigureCategory> findAllActive();

    Iterable<FigureCategory> findAllInactive();
}
