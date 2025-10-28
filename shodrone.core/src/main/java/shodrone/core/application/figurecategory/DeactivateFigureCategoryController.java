package shodrone.core.application.figurecategory;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import shodrone.core.domain.figurecategory.FigureCategory;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.figurecategory.FigureCategoryRepository;

public class DeactivateFigureCategoryController {

    final FigureCategoryRepository figureCategoryRepository = PersistenceContext
            .repositories().figureCategories();

    private final AuthorizationService authz = AuthzRegistry.authorizationService();


    public void deactivateFigureCategory(FigureCategory selectedFigureCategory) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER, ShodroneRoles.CRM_MANAGER, ShodroneRoles.CRM_COLLABORATOR);
        selectedFigureCategory.setActive(false);
        selectedFigureCategory.updateDate();
        figureCategoryRepository.save(selectedFigureCategory);
    }

    public Iterable<FigureCategory> allActiveFigureCategories() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER, ShodroneRoles.CRM_MANAGER, ShodroneRoles.CRM_COLLABORATOR);
        return figureCategoryRepository.findAllActive();
    }
}