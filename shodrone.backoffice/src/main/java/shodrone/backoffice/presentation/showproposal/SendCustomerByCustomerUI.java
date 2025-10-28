package shodrone.backoffice.presentation.showproposal;


import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lombok.extern.slf4j.Slf4j;
import shodrone.backoffice.presentation.customer.CustomerPrinter;
import shodrone.backoffice.presentation.showrequest.ShowRequestPrinter;
import shodrone.core.application.customer.ListCustomerController;
import shodrone.core.application.showproposal.SendCustomerController;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.showproposal.ShowProposal;
import shodrone.core.domain.showproposal.ShowProposalTemplate;
import shodrone.core.domain.showrequest.ShowRequest;
import shodrone.core.presentation.console.ConsoleEvent;

import java.awt.*;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class SendCustomerByCustomerUI extends AbstractUI {

    private final SendCustomerController controller = new SendCustomerController();
    private final ListCustomerController customerController = new ListCustomerController();

    @Override
    protected boolean doShow() {

        try {
            Customer selectedCustomer = getCustomer();

            if(selectedCustomer!=null){

                ShowProposal selectedShowProposal = getShowProposal(selectedCustomer);

                if(selectedShowProposal!=null) {
                    if(controller.testShowProposal(selectedShowProposal)){
                        if(createDocument(selectedShowProposal)) {
                            controller.changeShowProposalStatus(selectedShowProposal);
                            ConsoleEvent.info(String.format("%d status changed", selectedShowProposal.identity()));
                            return true;
                        }
                    }
                }
            }
        } catch (RuntimeException e) {
            ConsoleEvent.error(e.getMessage());
            return false;
        }
        return false;
    }

    private boolean createDocument(ShowProposal selectedShowProposal){

        Map<String, String> substitutions= new HashMap<>();
        ShowProposalTemplate template= null;
        Iterable<ShowProposalTemplate> opt = controller.getCurrentTemplate();

        if (opt.iterator().hasNext()) {

            template= opt.iterator().next();
            Path path = Paths.get(template.getFilePath());
            File templateFile = path.toFile();

            substitutions= controller.getSubstitutions(selectedShowProposal);
            String filePath = "shodrone.core/src/main/resources/Doc"+selectedShowProposal.identity().toString()+".txt";
            controller.generateFile(substitutions,templateFile, filePath);
            return true;

        }else{
            throw new RuntimeException("No Show Proposal export file template in the system");
        }
    }

    private ShowProposal getShowProposal(Customer selectedCustomer){
        Iterable<ShowProposal> foundProposals = controller.allSpecificCustomerProposalsReadyToTest(selectedCustomer);

        if (foundProposals.iterator().hasNext()) {
                final SelectWidget<ShowProposal> proposalSelector = new SelectWidget<>("Select a show Proposal:", foundProposals);
            proposalSelector.show();
           return proposalSelector.selectedElement();
        }else{
            throw new RuntimeException("Customer has no Show Proposals");
        }
    }

    private Customer getCustomer() {
        final Iterable<Customer> customers = customerController.allCustomers();
        if (customers.iterator().hasNext()) {
            final SelectWidget<Customer> selector = new SelectWidget<>("Select a customer:", customers, new CustomerPrinter());
            selector.show();
            return selector.selectedElement();
        }else{
            throw new RuntimeException("No existing customers.");
        }
    }

    @Override
    public String headline() {
        return "Send Customer Show Proposal";
    }
}
