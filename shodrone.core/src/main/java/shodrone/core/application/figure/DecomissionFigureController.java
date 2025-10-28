package shodrone.core.application.figure;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import shodrone.core.domain.figure.Figure;
import shodrone.core.domain.figure.FigureStatus;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.figure.FigureRepository;

public class DecomissionFigureController {

    final FigureRepository figureRepository = PersistenceContext
            .repositories().figures();

    private final AuthorizationService authz = AuthzRegistry.authorizationService();


    public void decomissionFigure(Figure selectedFigure) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER, ShodroneRoles.CRM_MANAGER, ShodroneRoles.CRM_COLLABORATOR);
        selectedFigure.setStatus(FigureStatus.DECOMISSIONED);
        figureRepository.save(selectedFigure);
    }

    public Iterable<Figure> allComissionedFigures() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER, ShodroneRoles.CRM_MANAGER, ShodroneRoles.CRM_COLLABORATOR);
        return figureRepository.findAllComissioned();
    }
}