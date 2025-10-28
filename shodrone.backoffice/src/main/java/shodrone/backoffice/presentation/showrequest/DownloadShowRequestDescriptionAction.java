package shodrone.backoffice.presentation.showrequest;

import eapli.framework.actions.Action;

public class DownloadShowRequestDescriptionAction implements Action {

    @Override
    public boolean execute() {
        return new DownloadShowRequestUI().show();
    }
}
