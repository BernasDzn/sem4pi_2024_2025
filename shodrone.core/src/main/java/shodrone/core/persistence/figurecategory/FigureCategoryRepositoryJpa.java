package shodrone.core.persistence.figurecategory;

import eapli.framework.domain.repositories.TransactionalContext;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.infrastructure.repositories.impl.jpa.JpaAutoTxRepository;
import shodrone.core.domain.figurecategory.FigureCategory;
import shodrone.core.infrastructure.Application;

import java.util.Optional;

public class FigureCategoryRepositoryJpa extends JpaAutoTxRepository<FigureCategory, Designation, Designation>
        implements FigureCategoryRepository {

    public FigureCategoryRepositoryJpa(final TransactionalContext autoTx) {
        super(autoTx, "name");
    }

    public FigureCategoryRepositoryJpa(final String puname) {
        super(puname, Application.settings().getExtendedPersistenceProperties(), "name");
    }

    @Override
    public Iterable<FigureCategory> figureCategories() {
        return findAll();
    }

    @Override
    public Optional<FigureCategory> findByName(final Designation name) {
        return match("e.name=:name", "name", name).stream().findFirst();
    }

    @Override
    public Iterable<FigureCategory> findAllActive() {
        return match("e.active = true");
    }

    @Override
    public Iterable<FigureCategory> findAllInactive() {
        return match("e.active = false");
    }
}
