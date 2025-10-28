package shodrone.backoffice.presentation.customer_rep;

import eapli.framework.visitor.Visitor;
import shodrone.core.domain.customer_rep.Representative;

public class RepresentativePrinter implements Visitor<Representative> {
    @Override
    public void visit(final Representative visitee) {
        System.out.printf("%-30s%-40s%-20s%-10s",
                visitee.getRepresentativeName(), visitee.getRepresentativeEmail(), 
                visitee.getRepresentativePosition(), visitee.getStatus());
    }
}
