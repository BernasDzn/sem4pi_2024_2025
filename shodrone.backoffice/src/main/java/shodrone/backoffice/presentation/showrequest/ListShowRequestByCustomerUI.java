package shodrone.backoffice.presentation.showrequest;

import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.presentation.console.SelectWidget;
import eapli.framework.visitor.Visitor;
import shodrone.backoffice.presentation.customer.CustomerPrinter;
import shodrone.core.application.customer.ListCustomerController;
import shodrone.core.application.showrequest.ListShowRequestController;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.showrequest.ShowRequest;

import java.util.ArrayList;

public class ListShowRequestByCustomerUI extends AbstractListUI<ShowRequest> {

    private final ListShowRequestController theController = new ListShowRequestController();
    private final ListCustomerController customer_controller = new ListCustomerController();

    @Override
    public String headline() {
        return "List Show Request By Customer";
    }

    @Override
    protected String emptyMessage() {
        return "No data.";
    }

    @Override
    protected Iterable<ShowRequest> elements() {
        Customer selectedCustomer;
        try {
            final Iterable<Customer> allCustomers = customer_controller.allCustomers();
            if (!allCustomers.iterator().hasNext()) {
                return new ArrayList<>();
            }
            final SelectWidget<Customer> selector = new SelectWidget<>("Select a customer:", allCustomers,
                    new CustomerPrinter());
            selector.show();
            selectedCustomer = selector.selectedElement();
        } catch (Exception e) {
            System.out.println("Invalid customer selected.");
            return new ArrayList<>();
        }
        return theController.allShowRequestsByCustomerAndAuthor(selectedCustomer);
    }

    @Override
    protected Visitor<ShowRequest> elementPrinter() {
        return new ShowRequestPrinter();
    }

    @Override
    protected String elementName() {
        return "";
    }

    @Override
    protected String listHeader() {
        return String.format("#  %-20s%-20s%-20s%-10s%-20s%-20s%-20s", "AUTHOR", "CUSTOMER", "NUMBER OF DRONES", "DURATION", "PLACE",
                "DATE", "STATUS");
    }

}