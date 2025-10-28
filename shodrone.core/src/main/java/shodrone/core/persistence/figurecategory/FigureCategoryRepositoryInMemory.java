package shodrone.core.persistence.figurecategory;

import eapli.framework.general.domain.model.Designation;
import shodrone.core.domain.figurecategory.FigureCategory;
import eapli.framework.infrastructure.repositories.impl.inmemory.InMemoryDomainRepository;
import java.util.Optional;

public class FigureCategoryRepositoryInMemory extends InMemoryDomainRepository<FigureCategory, Designation>
        implements FigureCategoryRepository {
    @Override
    public Iterable<FigureCategory> figureCategories() {
        return findAll();
    }

    @Override
    public Optional<FigureCategory> findByName(final Designation name) {
        return matchOne(e -> e.getName().equals(name));
    }

    @Override
    public Iterable<FigureCategory> findAllActive() {
        return match(FigureCategory::isActive);
    }

    @Override
    public Iterable<FigureCategory> findAllInactive() {
        return match(e -> !e.isActive());
    }
}
