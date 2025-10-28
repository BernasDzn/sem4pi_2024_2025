package shodrone.backoffice.presentation.customer_rep;

import java.util.Set;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.presentation.console.AbstractUI;
import shodrone.core.application.customer_rep.RegisterRepresentativeController;
import shodrone.core.application.user.AddUserController;
import shodrone.core.domain.customer_rep.Representative;
import shodrone.core.domain.customer_rep.RepresentativeStatus;
import shodrone.core.domain.user.ShodroneRoles;

public class RegisterRepresentativeUI extends AbstractUI{

    private final RegisterRepresentativeController ctrlRegisterRepresentative = new RegisterRepresentativeController();
    private final AddUserController ctrlAddUser = new AddUserController();

    @Override
    protected boolean doShow() {

        String repFirstName = "";
        String repLastName = "";
        String repEmail = "";
        String repPosition = "";
        String repUsername = "";
        String repPassword = "";

        SystemUser newRepresentativeUser = ctrlAddUser.addUser(
            repUsername, repPassword, repFirstName, repLastName, repEmail, Set.of(ShodroneRoles.CRM_COLLABORATOR)
        );

        Representative newRepresentative = ctrlRegisterRepresentative.registerCustomerRepresentative(
            repEmail, repPosition,
            repFirstName, repLastName,
            repUsername, repPassword
        );
        
        return false;

    }

    @Override
    public String headline() {
        return "Register Customer Representative";
    }

}
