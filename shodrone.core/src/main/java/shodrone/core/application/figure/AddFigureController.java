package shodrone.core.application.figure;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.figure.*;
import shodrone.core.domain.figure.figurekeyword.FigureKeyword;
import shodrone.core.domain.figurecategory.FigureCategory;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.figure.FigureRepository;
import shodrone.core.domain.common.DSL;


import java.io.File;
import java.util.List;


@UseCaseController
public class AddFigureController {

    final FigureRepository figureRepository = PersistenceContext
            .repositories().figures();

    private final AuthorizationService authz = AuthzRegistry.authorizationService();

    public Figure addFigure(final FigureName name, final Description description,
                          final VersionNumber version, final File DSLFile, FigureStatus status,
                          final Customer exclusiveTo, final FigureCategory category, final SystemUser showDesigner,final List<FigureKeyword> keywords) {

        return figureRepository.save(new Figure(name, description, version, DSLFile, status, exclusiveTo, category,showDesigner,keywords));
    }

    public boolean isFigureRegistered(final FigureName name) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER, ShodroneRoles.CRM_MANAGER, ShodroneRoles.CRM_COLLABORATOR);
        return figureRepository.findByName(name).isPresent();
    }

    public boolean validateDSL(File dslCode) {
        return DSL.validate(dslCode);
    }


}
