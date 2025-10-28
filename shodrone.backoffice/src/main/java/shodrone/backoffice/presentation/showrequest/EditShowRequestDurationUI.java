package shodrone.backoffice.presentation.showrequest;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import shodrone.core.presentation.console.ConsoleEvent;
import shodrone.backoffice.presentation.showrequest.menu.FilterSelectionMenu;
import shodrone.core.application.showrequest.EditShowRequestController;
import shodrone.core.domain.showrequest.ShowRequest;

public class EditShowRequestDurationUI extends AbstractUI {
    private final EditShowRequestController controller = new EditShowRequestController();
    @Override
    protected boolean doShow() {
        boolean isValid = false;
        FilterSelectionMenu selectionMenu = new FilterSelectionMenu();
        ShowRequest selectedShowRequest = selectionMenu.selectShow();

        if (selectedShowRequest == null) {
            return false;
        }

        System.out.println("Current duration: " + selectedShowRequest.getDuration());

        while (!isValid) {
            try {
                int newDuration = Console.readInteger("New duration (Minutes): ");
                this.controller.editShowRequestDuration(selectedShowRequest, newDuration);
                isValid = true;
            } catch (final ConcurrencyException ex) {
                ConsoleEvent.error(
                        "It is not possible to change the show request duration because it was changed by another user");
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
        return "Edit Show Request Duration";
    }
}
