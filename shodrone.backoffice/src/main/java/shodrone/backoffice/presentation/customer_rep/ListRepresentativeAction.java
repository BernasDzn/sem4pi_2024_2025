package shodrone.backoffice.presentation.customer_rep;

import eapli.framework.actions.Action;

public class ListRepresentativeAction implements Action {
	@Override
	public boolean execute() {
		return new ListRepresentativeUI().show();
	}
}
