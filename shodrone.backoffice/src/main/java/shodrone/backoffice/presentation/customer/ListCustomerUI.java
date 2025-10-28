package shodrone.backoffice.presentation.customer;

import eapli.framework.presentation.console.AbstractListUI;
import eapli.framework.visitor.Visitor;
import shodrone.core.application.customer.ListCustomerController;
import shodrone.core.domain.customer.Customer;

public class ListCustomerUI extends AbstractListUI<Customer> {

    private final ListCustomerController theController = new ListCustomerController();

    @Override
    public String headline() {
        return "List Customer";
    }

    @Override
    protected String emptyMessage() {
        return "No data.";
    }

    @Override
    protected Iterable<Customer> elements() {
        return theController.allCustomers();
    }

    @Override
    protected Visitor<Customer> elementPrinter() {
        return new CustomerPrinter();
    }

    @Override
    protected String elementName() {
        return "Customer";
    }

    @Override
    protected String listHeader() {
        return String.format("#  %-20s%-40s%-50s%-15s%-10s", "VAT Number", "Name", "Address", "Status", "Nº of Representatives");
    }


}
