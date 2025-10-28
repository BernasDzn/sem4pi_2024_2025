package shodrone.backoffice.presentation.customer;

import eapli.framework.visitor.Visitor;
import shodrone.core.domain.customer.Customer;

public class CustomerPrinter implements Visitor<Customer> {
    @Override
    public void visit(final Customer visitee) {
        String address = visitee.getAddress().toString();
        if (address.length() > 50) {
            address = address.substring(0, 45) + "...";
        }
        System.out.printf("%-20s%-40s%-50s%-15s%-10s",
                visitee.getVatNumber(), visitee.getCustomerName(), address, visitee.getStatus(), visitee.getNumberOfRepresentatives());
    }
}
