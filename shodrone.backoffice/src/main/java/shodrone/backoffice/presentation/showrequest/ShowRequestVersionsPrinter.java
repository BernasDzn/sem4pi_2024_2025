package shodrone.backoffice.presentation.showrequest;

import eapli.framework.visitor.Visitor;
import shodrone.core.domain.showrequest.ShowRequestVersion;

import java.util.Calendar;

public class ShowRequestVersionsPrinter implements Visitor<ShowRequestVersion> {

    @Override
    public void visit(ShowRequestVersion visitee) {
        Calendar date = visitee.getCreationDate();
        System.out.printf("%02d/%02d/%04d %02d:%02d%-4s%-20s%-10s%-10s%-20s%-20s",date.get(Calendar.DAY_OF_MONTH), date.get(Calendar.MONTH) + 1, date.get(Calendar.YEAR), date.get(Calendar.HOUR_OF_DAY), date.get(Calendar.MINUTE),
           "",visitee.getNumberOfDrones(), visitee.getDuration(), visitee.getPlace(), visitee.getDate().toString(), visitee.getStatus().getStatusName());
    }

}
