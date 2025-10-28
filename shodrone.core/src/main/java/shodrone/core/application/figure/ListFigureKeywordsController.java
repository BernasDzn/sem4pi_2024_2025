package shodrone.core.application.figure;

import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import shodrone.core.domain.figure.figurekeyword.FigureKeyword;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.figure.figurekeyword.FigureKeywordRepository;

import java.util.Optional;

public class ListFigureKeywordsController {

    private final FigureKeywordRepository figureKeywordRepository = PersistenceContext
            .repositories().keywords();
    private final AuthorizationService authz = AuthzRegistry.authorizationService();

    public Iterable<FigureKeyword> allFigureKeywords() {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER,
                ShodroneRoles.CRM_MANAGER, ShodroneRoles.CRM_COLLABORATOR);
        return figureKeywordRepository.keywords();
    }

    public FigureKeyword addFigureKeyword(final String keyword) {
        return figureKeywordRepository.save(new FigureKeyword(keyword));
    }

    public Optional<FigureKeyword> findByName(final String keyword){
        return figureKeywordRepository.findByName(keyword);
    }
}
