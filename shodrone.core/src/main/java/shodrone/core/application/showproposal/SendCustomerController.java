package shodrone.core.application.showproposal;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.showproposal.*;
import shodrone.core.domain.showrequest.ShowRequest;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.showproposal.ShowProposalRepository;
import shodrone.core.persistence.showproposaltemplate.ShowProposalTemplateRepository;
import shodrone.sp_plugin.DocumentProcessor;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Optional;



import java.util.Map;
@UseCaseController
public class SendCustomerController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    final ShowProposalRepository showProposalRepository = PersistenceContext.repositories().showProposals();
    final ShowProposalTemplateRepository showProposalTemplateRepository = PersistenceContext.repositories().showProposalTemplates();
    final DocumentProcessor documentProcessor = new DocumentProcessor();


    public Iterable<ShowProposal> allSpecificCustomerProposalsReadyToTest(Customer selectedCustomer) {
        return showProposalRepository.findAllCustomerProposalsReadyToTest(selectedCustomer);
    }

    public Iterable<ShowProposal> allProposalsReadyToTest() {
        return showProposalRepository.findAllShowProposalsReadyToTest();
    }

    public Optional<ShowProposal> findShowProposalByShowRequest(ShowRequest showRequest) {
        return showProposalRepository.findByShowRequest(showRequest);
    }

    public void changeShowProposalStatus(ShowProposal selectedShowProposal) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.SHOW_DESIGNER, ShodroneRoles.CRM_MANAGER, ShodroneRoles.CRM_COLLABORATOR);
        selectedShowProposal.setStatus(ShowProposalStatus.WAITING_APPROVAL);
        showProposalRepository.save(selectedShowProposal);
    }

    public boolean testShowProposal(ShowProposal showRequest) {
        //TODO: usar socket
        return true;
    }

    public Iterable<ShowProposalTemplate> getCurrentTemplate() {
        return showProposalTemplateRepository.findCurrentShowProposalTemplate();
    }

    public File generateFile(Map<String, String> substitutions, File template,String outputFilePath) {
        String content= documentProcessor.processTemplate(template, substitutions);
        File outputFile = new File(outputFilePath);

        try (FileWriter writer = new FileWriter(outputFile)) {
            writer.write(content);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return outputFile;
    }

    public Map<String, String> getSubstitutions( ShowProposal showProposal) {
        return showProposal.getSubstitutions();
    }

}
