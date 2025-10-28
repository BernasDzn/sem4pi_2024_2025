package shodrone.backoffice.presentation.customer_rep;

import eapli.framework.actions.Action;

public class RegisterRepresentativeAction implements Action {
	@Override
	public boolean execute() {
		return new RegisterRepresentativeUI().show();
	}
}
