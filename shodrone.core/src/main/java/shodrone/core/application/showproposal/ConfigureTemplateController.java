package shodrone.core.application.showproposal;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import java.io.File;
import java.util.Calendar;
import java.util.Optional;

import shodrone.core.domain.showproposal.ShowProposalTemplate;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.showproposaltemplate.ShowProposalTemplateRepository;

@UseCaseController
public class ConfigureTemplateController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    final ShowProposalTemplateRepository showProposalTemplateRepository = PersistenceContext.repositories().showProposalTemplates();

    public boolean validateShowProposalTemplate(File newTemplate) {
        return ShowProposalTemplate.validate(newTemplate);
    }

    public Iterable<ShowProposalTemplate> allShowProposalTemplates(){
        return showProposalTemplateRepository.findAllShowProposalTemplates();
    }

    public Optional<ShowProposalTemplate> findByPath(String filePath){
        return showProposalTemplateRepository.findByPath(filePath);
    }


    public void addShowProposalTemplate(File newTemplate) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER, ShodroneRoles.CRM_MANAGER, ShodroneRoles.CRM_COLLABORATOR);
        ShowProposalTemplate template= new ShowProposalTemplate(newTemplate, Calendar.getInstance(), newTemplate.toPath());
        showProposalTemplateRepository.save(template);
    }

    public void changeShowProposalTemplate(ShowProposalTemplate newTemplate) {
        showProposalTemplateRepository.remove(
                showProposalTemplateRepository.findByPath(newTemplate.getFilePath()).get()
        );
        newTemplate.setAdditionDate(Calendar.getInstance());
        showProposalTemplateRepository.save(newTemplate);
    }

}