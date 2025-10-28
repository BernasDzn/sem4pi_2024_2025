package shodrone.backoffice.presentation.showrequest;

import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import shodrone.backoffice.presentation.showrequest.menu.FilterSelectionMenu;
import shodrone.core.application.showrequest.EditShowRequestController;
import shodrone.core.domain.showrequest.ShowRequest;
import shodrone.core.domain.showrequest.ShowRequestStatus;
import shodrone.core.presentation.console.ConsoleEvent;

import java.util.Objects;

public class EditShowRequestStatusUI extends AbstractUI {
    private final EditShowRequestController controller = new EditShowRequestController();
    @Override
    protected boolean doShow() {
        FilterSelectionMenu selectionMenu = new FilterSelectionMenu();
        ShowRequest selectedShowRequest = selectionMenu.selectShow();

        if (selectedShowRequest == null) {
            return false;
        }

        System.out.println("Current status: " + selectedShowRequest.getStatus());

        try {
            SelectWidget<String> statusSelect = new SelectWidget<>("Select new status", ShowRequestStatus.getAllStatus());
            statusSelect.show();
            ShowRequestStatus newStatus = ShowRequestStatus.getStatusByName(statusSelect.selectedElement());
            if (Objects.requireNonNull(newStatus).getStatusName().equals(selectedShowRequest.getStatus().getStatusName())) {
                System.out.println("No changes made.");
                return false;
            }
            this.controller.editShowRequestStatus(selectedShowRequest, newStatus);
        } catch (final IllegalArgumentException ex) {
            ConsoleEvent.error("Invalid status. Please try again.");
        } catch (final Exception ex) {
            ConsoleEvent.error("Unexpected error in the application.");
        }

        return true;
    }

    @Override
    public String headline() {
        return "Edit Show Request Status";
    }
}
