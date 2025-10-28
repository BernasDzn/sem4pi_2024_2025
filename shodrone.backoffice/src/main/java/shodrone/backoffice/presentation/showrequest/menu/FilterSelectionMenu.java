package shodrone.backoffice.presentation.showrequest.menu;

import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.SelectWidget;
import shodrone.backoffice.presentation.customer.CustomerPrinter;
import shodrone.backoffice.presentation.showrequest.ShowRequestPrinter;
import shodrone.core.application.customer.ListCustomerController;
import shodrone.core.application.showrequest.ListShowRequestController;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.showrequest.ShowRequest;
import shodrone.core.domain.showrequest.ShowRequestStatus;

import java.util.ArrayList;

public class FilterSelectionMenu {

    private final ListShowRequestController controller = new ListShowRequestController();
    private final ListCustomerController customerController = new ListCustomerController();
    private boolean filterNonProposalShowRequests = true;
    public ShowRequest selectShow() {
        System.out.println("Select a list view option:");
        System.out.println("1. See all show requests");
        System.out.println("2. See show requests by customer");
        System.out.println("0. Exit");

        int option = -1;
        while (option < 0 || option > 2) {
            option = Console.readInteger("Option: ");
            if (option < 0 || option > 2) {
                System.out.println("Invalid option. Please try again.");
            }
        }

        switch (option) {
            case 1:
                return seeAllShowRequests();
            case 2:
                return seeShowRequestsByCustomer();
            case 0:
                return null;
            default:
                System.out.println("Invalid option. Please try again.");
        }
        return null;
    }

    private ShowRequest seeAllShowRequests() {
        Iterable<ShowRequest> showRequests;
        if (filterNonProposalShowRequests) {
            showRequests = filterNonProposalShowRequests(this.controller.allShowRequestsByAuthor());
        }
        else {
            showRequests = this.controller.allShowRequestsByAuthor();
        }
        return getShowRequest(showRequests);
    }

    private ShowRequest seeShowRequestsByCustomer() {
        final Iterable<Customer> customers = this.customerController.allCustomers();
        if (!customers.iterator().hasNext()) {
            System.out.println("There are no registered Customers");
            return null;
        }
        SelectWidget<Customer> customerSelector = new SelectWidget<>("Customers:", customers, new CustomerPrinter());
        customerSelector.show();
        Customer selectedCustomer = customerSelector.selectedElement();
        if (selectedCustomer == null) {
            System.out.println("No customer selected.");
            return null;
        }

        Iterable<ShowRequest> showRequests;
        if (filterNonProposalShowRequests) {
            showRequests = filterNonProposalShowRequests(this.controller.allShowRequestsByCustomerAndAuthor(selectedCustomer));
        }
        else {
            showRequests = this.controller.allShowRequestsByCustomerAndAuthor(selectedCustomer);
        }
        return getShowRequest(showRequests);
    }

    private ShowRequest getShowRequest(Iterable<ShowRequest> showRequests) {
        if (!showRequests.iterator().hasNext()) {
            System.out.println("There are no registered Show Requests");
        } else {
            SelectWidget<ShowRequest> showRequestSelector = new SelectWidget<>("Show Requests:", showRequests, new ShowRequestPrinter());
            showRequestSelector.show();
            return showRequestSelector.selectedElement();
        }

        return null;
    }

    private Iterable<ShowRequest> filterNonProposalShowRequests(Iterable<ShowRequest> allShowRequests) {
        ArrayList<ShowRequest> filteredShowRequests = new ArrayList<>();
        for (ShowRequest showRequest : allShowRequests) {
            if (!(showRequest.getStatus().equals(ShowRequestStatus.TO_PROPOSAL) || showRequest.getStatus().equals(ShowRequestStatus.IN_PRODUCTION))) {
                filteredShowRequests.add(showRequest);
            }
        }
        return filteredShowRequests;
    }

    public void withFilter(boolean filterNonProposalShowRequests) {
        this.filterNonProposalShowRequests = filterNonProposalShowRequests;
    }
}
