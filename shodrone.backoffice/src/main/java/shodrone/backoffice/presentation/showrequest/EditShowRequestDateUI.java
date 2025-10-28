package shodrone.backoffice.presentation.showrequest;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import shodrone.backoffice.presentation.showrequest.menu.FilterSelectionMenu;
import shodrone.core.application.showrequest.EditShowRequestController;
import shodrone.core.domain.common.Date;
import shodrone.core.domain.showrequest.ShowRequest;
import shodrone.core.presentation.console.ConsoleEvent;

public class EditShowRequestDateUI extends AbstractUI {
    private final EditShowRequestController controller = new EditShowRequestController();

    @Override
    protected boolean doShow() {
        boolean isValid = false;
        FilterSelectionMenu selectionMenu = new FilterSelectionMenu();
        ShowRequest selectedShowRequest = selectionMenu.selectShow();

        if (selectedShowRequest == null) {
            return false;
        }

        System.out.println("Current date: " + selectedShowRequest.getDate());

        while (!isValid){
            try {
                String newDate = Console.readNonEmptyLine("Enter new date (dd/mm/yyyy hh:mm): ", "Date cannot be empty");
                if (this.controller.isShowRequestAvailable(selectedShowRequest.getPlace(), newDate)) {
                    throw new IllegalArgumentException("Show request already exists for this date and place");
                }
                this.controller.editShowRequestDate(selectedShowRequest, newDate);
                isValid = true;
            } catch (final ConcurrencyException ex) {
                ConsoleEvent.error(
                        "It is not possible to change the show request date because it was changed by another user");
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
        return "Edit Show Request Date";
    }
}
