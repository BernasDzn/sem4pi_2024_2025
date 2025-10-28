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

@UseCaseController
public class EditFigureCategoryController {

    final FigureCategoryRepository figureCategoryRepository = PersistenceContext
            .repositories().figureCategories();

    private final AuthorizationService authz = AuthzRegistry.authorizationService();


    public void editFigureCategoryName(FigureCategory figureCategory, Designation name) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER, ShodroneRoles.CRM_MANAGER, ShodroneRoles.CRM_COLLABORATOR);
        figureCategory.setName(name);
        figureCategory.updateDate();
        figureCategoryRepository.save(figureCategory);
    }

    public void editFigureCategoryDescription(FigureCategory figureCategory, Description description) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER, ShodroneRoles.CRM_MANAGER, ShodroneRoles.CRM_COLLABORATOR);
        figureCategory.setDescription(description);
        figureCategory.updateDate();
        figureCategoryRepository.save(figureCategory);
    }

    public Iterable<FigureCategory> allFigureCategories() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER, ShodroneRoles.CRM_MANAGER, ShodroneRoles.CRM_COLLABORATOR);
        return figureCategoryRepository.findAll();
    }

    public boolean isFigureCategoryRegistered(final Designation name) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER, ShodroneRoles.CRM_MANAGER, ShodroneRoles.CRM_COLLABORATOR);
        return figureCategoryRepository.findByName(name).isPresent();
    }
}
