package shodrone.core.application.showrequest;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import shodrone.core.domain.common.Date;
import shodrone.core.domain.common.Place;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.showrequest.*;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.showrequest.ShowRequestRepository;

import java.io.File;
import java.util.Calendar;

@UseCaseController
public class RegisterShowRequestController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    final ShowRequestRepository showRequestRepository = PersistenceContext.repositories().showRequests();

    public ShowRequest registerShowRequest(final Customer customer, final int numberOfDrones, final int duration,
                                    final File description, final String place, final String date) {
        SystemUser author = authz.session().get().authenticatedUser();
        ShowRequest sr = new ShowRequest(author, customer, new ShowRequestNumberOfDrones(numberOfDrones), new ShowRequestDuration(duration)
                , new ShowRequestDescription(description), new Place(place), new Date(date), ShowRequestStatus.NEW);
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER,ShodroneRoles.CRM_COLLABORATOR, ShodroneRoles.CRM_MANAGER);
        return showRequestRepository.save(sr);
    }

    public void validateNumberOfDrones(int numberOfDrones) {
        ShowRequestNumberOfDrones.validate(numberOfDrones);
    }

    public void validateDuration(int duration) {
        ShowRequestDuration.validate(duration);
    }

    public void validateDate(Calendar date) {
        Date.validate(date);
    }
    public void validateDate(String date) {
        Date.validate(date);
    }

    public void validatePlace(String coordinates) {
        Place.validate(coordinates);
    }

    public void validateDescriptionFile(File file) {
        ShowRequestDescription.validate(file);
    }

    public boolean isShowRequestAvailable(String place, String date) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR, ShodroneRoles.CRM_MANAGER);
        Place placeObj = new Place(place);
        Date dateObj = new Date(date);
        return showRequestRepository.findShowRequestsByDateAndPlace(placeObj, dateObj).isPresent();
    }
}
