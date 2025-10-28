package shodrone.backoffice.presentation.showrequest;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.presentation.console.AbstractUI;
import shodrone.backoffice.presentation.showrequest.menu.FilterSelectionMenu;
import shodrone.core.application.showrequest.EditShowRequestController;
import shodrone.core.domain.showrequest.ShowRequestDescription;
import shodrone.core.domain.showrequest.ShowRequest;
import shodrone.core.presentation.console.ConsoleEvent;

import java.awt.*;
import java.io.File;

public class EditShowRequestDescriptionUI extends AbstractUI {
    private final EditShowRequestController controller = new EditShowRequestController();
    @Override
    protected boolean doShow() {
        boolean isValid = false;
        FilterSelectionMenu selectionMenu = new FilterSelectionMenu();
        ShowRequest selectedShowRequest = selectionMenu.selectShow();

        if (selectedShowRequest == null) {
            return false;
        }

        while(!isValid){
            try {
                System.out.println("New description ");
                FileDialog fileDialog = new FileDialog((Frame) null, "Select a Description file", FileDialog.LOAD);
                fileDialog.setFile("*.pdf");
                fileDialog.setVisible(true);
                File newDescription = new File(fileDialog.getDirectory(), fileDialog.getFile());
                System.out.println(newDescription.getName());
                this.controller.editShowRequestDescription(selectedShowRequest, newDescription);
                isValid = true;
            } catch (final ConcurrencyException ex) {
                ConsoleEvent.error(
                        "It is not possible to change the show request description because it was changed by another user");
            } catch (final IllegalArgumentException ex) {
                ConsoleEvent.error(ex.getMessage());
            } catch (final Exception ex) {
                ConsoleEvent.error("Unexpected error in the application.");
            }
        }

        return true;
    }

    @Override
    public String headline() {
        return "Edit Show Request Description";
    }
}
