package shodrone.backoffice.presentation.showrequest;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import shodrone.backoffice.presentation.showrequest.menu.FilterSelectionMenu;
import shodrone.core.application.showrequest.EditShowRequestController;
import shodrone.core.domain.showrequest.ShowRequestNumberOfDrones;
import shodrone.core.domain.showrequest.ShowRequest;
import shodrone.core.presentation.console.ConsoleEvent;

public class EditShowRequestNumberOfDronesUI extends AbstractUI {

    private final EditShowRequestController controller = new EditShowRequestController();

    @Override
    protected boolean doShow() {
        boolean isValid = false;
        FilterSelectionMenu selectionMenu = new FilterSelectionMenu();
        ShowRequest selectedShowRequest = selectionMenu.selectShow();

        if (selectedShowRequest == null) {
            return false;
        }
        System.out.println("Current number of drones: " + selectedShowRequest.getNumberOfDrones());

        while (!isValid){
            try {
                int newNumberOfDrones = Integer.parseInt(Console.readLine("New number of drones: "));
                this.controller.editShowRequestNumberOfDrones(selectedShowRequest, newNumberOfDrones);
                isValid = true;
            } catch (final ConcurrencyException ex) {
                    ConsoleEvent.error(
                        "It is not possible to change the show request number of drones because it was changed by another user");
            } catch (final IllegalArgumentException ex) {
                ConsoleEvent.error(ex.getMessage());
            } catch (final Exception ex) {
                ConsoleEvent.error(
                        "Unexpected error in the application.");
            }
        }

        return true;
    }

    @Override
    public String headline() {
        return "Edit Show Request Number of Drones";
    }
}
