package shodrone.backoffice.presentation.customer;

import eapli.framework.visitor.Visitor;
import shodrone.core.domain.customer.Customer;

public class CustomerPrinterShort implements Visitor<Customer> {
    @Override
    public void visit(final Customer visitee) {
        System.out.printf("%-20s%-40s%-10s",
                visitee.getVatNumber(), visitee.getCustomerName(), visitee.getNumberOfRepresentatives());
    }
}