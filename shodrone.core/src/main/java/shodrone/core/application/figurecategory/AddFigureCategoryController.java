package shodrone.core.application.figurecategory;

import eapli.framework.application.UseCaseController;
import eapli.framework.general.domain.model.Description;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import shodrone.core.domain.figurecategory.FigureCategory;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.figurecategory.FigureCategoryRepository;

import java.util.Calendar;

@UseCaseController
public class AddFigureCategoryController {

    final FigureCategoryRepository figureCategoryRepository = PersistenceContext
            .repositories().figureCategories();

    private final AuthorizationService authz = AuthzRegistry.authorizationService();

    public FigureCategory addFigureCategory(final Designation name, final Description description, final Calendar creationDate,
            final Calendar lastUpdateDate) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER,
                ShodroneRoles.CRM_MANAGER, ShodroneRoles.CRM_COLLABORATOR);
        return figureCategoryRepository.save(new FigureCategory(name, description, creationDate, lastUpdateDate));
    }

    public boolean isFigureCategoryRegistered(final Designation name) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER,
                ShodroneRoles.CRM_MANAGER, ShodroneRoles.CRM_COLLABORATOR);
        return figureCategoryRepository.findByName(name).isPresent();
    }
}
