package shodrone.backoffice.presentation.showproposal;

import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import lombok.extern.slf4j.Slf4j;
import shodrone.backoffice.presentation.customer.CustomerPrinter;
import shodrone.core.application.showproposal.ConfigureTemplateController;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.showproposal.ShowProposal;
import shodrone.core.domain.showproposal.ShowProposalTemplate;
import shodrone.core.presentation.console.ConsoleEvent;

import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class ChangeShowProposalTemplateUI extends AbstractUI {

    private final ConfigureTemplateController controller = new ConfigureTemplateController();

    @Override
    protected boolean doShow() {
        File file = null;
            Iterable<ShowProposalTemplate> templates = controller.allShowProposalTemplates();
            List templatePaths= new ArrayList<>();
            if (!(templates==null)) {
                for (ShowProposalTemplate template : templates) {
                    templatePaths.add(template.getFilePath());
                }
                if (!templatePaths.isEmpty()) {
                    final SelectWidget<String> proposalSelector = new SelectWidget<>("Select a show Proposal Template:", templatePaths);
                    proposalSelector.show();
                    var opt = controller.findByPath(proposalSelector.selectedElement());
                    if (opt.isPresent()) {
                        controller.changeShowProposalTemplate(opt.get());
                        System.out.println("\nShow Proposal Template file changed successfully");
                        return true;
                    }
                }
            }else {
                ConsoleEvent.error("No templates available");
                return false;
            }

        return false;
    }


    @Override
    public String headline() {
        return "Change Show Proposal Template";
    }
}
