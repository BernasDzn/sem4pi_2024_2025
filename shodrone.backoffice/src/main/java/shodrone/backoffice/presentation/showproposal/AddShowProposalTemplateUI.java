package shodrone.backoffice.presentation.showproposal;

import eapli.framework.presentation.console.AbstractUI;
import shodrone.core.application.showproposal.ConfigureTemplateController;

import java.awt.*;
import java.io.File;

public class AddShowProposalTemplateUI extends AbstractUI {

    private final ConfigureTemplateController theController = new ConfigureTemplateController();

    @Override
    protected boolean doShow() {
        File file = null;
        do {
            System.out.println("Select a Show Proposal Template file");
            FileDialog dialog = new FileDialog((Frame) null, "Select a Template File", FileDialog.LOAD);
            dialog.setFile("*.txt");
            dialog.setVisible(true);
            dialog.dispose();
            file = new File(dialog.getDirectory(), dialog.getFile());
            if (!file.canRead())
                file = null;
        } while (file == null);

        if(theController.validateShowProposalTemplate(file)){
            theController.addShowProposalTemplate(file);
            System.out.println("\nShow Proposal Template file added successfully");
            return true;
        }else{
            System.out.println("file not a valid Show Proposal Template");
            return false;
        }
    }


    @Override
    public String headline() {
        return "Add Show Proposal Template";
    }
}
