package shodrone.backoffice.presentation.showrequest;

import eapli.framework.visitor.Visitor;
import shodrone.core.domain.showrequest.ShowRequest;

public class ShowRequestPrinter implements Visitor<ShowRequest> {

    @Override
    public void visit(ShowRequest visitee) {
        System.out.printf("%-20s%-20s%-20s%-10s%-20s%-20s%-20s", visitee.getAuthor().name(), visitee.getCustomer().getCustomerName(),
                visitee.getNumberOfDrones(), visitee.getDuration(), visitee.getPlace(),
                visitee.getDate().toString(), visitee.getStatus().getStatusName());
    }

}
