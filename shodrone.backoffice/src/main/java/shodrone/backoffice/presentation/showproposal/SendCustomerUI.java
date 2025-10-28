package shodrone.backoffice.presentation.showproposal;


import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import shodrone.backoffice.presentation.customer.CustomerPrinter;
import shodrone.core.application.customer.ListCustomerController;
import shodrone.core.application.showproposal.SendCustomerController;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.showproposal.ShowProposal;
import shodrone.core.domain.showproposal.ShowProposalTemplate;
import shodrone.core.presentation.console.ConsoleEvent;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class SendCustomerUI extends AbstractUI {
    private final SendCustomerController controller = new SendCustomerController();

    @Override
    protected boolean doShow() {
        ShowProposal selectedShowProposal = null;

        Iterable<ShowProposal> foundProposals = controller.allProposalsReadyToTest();

        if (foundProposals.iterator().hasNext()) {
            final SelectWidget<ShowProposal> selector = new SelectWidget<>("Select a show Proposal:", foundProposals);
            selector.show();
            selectedShowProposal = selector.selectedElement();
        } else {
            ConsoleEvent.error("No Show Proposals");
            return false;
        }
        if (selectedShowProposal != null) {
            if (controller.testShowProposal(selectedShowProposal)) {
                if (createDocument(selectedShowProposal)) {
                    controller.changeShowProposalStatus(selectedShowProposal);
                    ConsoleEvent.info(String.format("%d status changed", selectedShowProposal.identity()));
                    return true;
                }
            }
        }
        return false;
    }


    private boolean createDocument(ShowProposal selectedShowProposal) {

        Map<String, String> substitutions = new HashMap<>();
        ShowProposalTemplate template = null;
        Iterable<ShowProposalTemplate> opt = controller.getCurrentTemplate();

        if (opt.iterator().hasNext()) {

            template = opt.iterator().next();
            File templateFile = new File(template.getFilePath());

            substitutions = controller.getSubstitutions(selectedShowProposal);
            String filePath = "shodrone.core/src/main/resources/Doc"+selectedShowProposal.identity().toString()+".txt";
            controller.generateFile(substitutions, templateFile, filePath);
            return true;

        } else {
            ConsoleEvent.error("No Show Proposal export file template in the system");
            return false;
        }
    }
    @Override
    public String headline() {
        return "Send Customer Show Proposal";
    }
}
