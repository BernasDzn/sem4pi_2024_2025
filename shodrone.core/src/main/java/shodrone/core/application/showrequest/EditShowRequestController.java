package shodrone.core.application.showrequest;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import shodrone.core.domain.common.*;
import shodrone.core.domain.showrequest.*;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.showrequest.ShowRequestRepository;

import java.io.File;

@UseCaseController
public class EditShowRequestController {

    final ShowRequestRepository showRequestRepository = PersistenceContext
            .repositories().showRequests();

    private final AuthorizationService authz = AuthzRegistry.authorizationService();

    public void editShowRequestNumberOfDrones(ShowRequest showRequest, int numberOfDrones) {
        ShowRequestNumberOfDrones numberOfDronesObj = new ShowRequestNumberOfDrones(numberOfDrones);
        showRequest.setNumberOfDrones(numberOfDronesObj);
        showRequest.updateVersions(showRequest);
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER,ShodroneRoles.CRM_COLLABORATOR, ShodroneRoles.CRM_MANAGER);
        showRequestRepository.save(showRequest);
    }

    public void editShowRequestDuration(ShowRequest showRequest, int duration) {
        ShowRequestDuration durationObj = new ShowRequestDuration(duration);
        showRequest.setDuration(durationObj);
        showRequest.updateVersions(showRequest);
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER,ShodroneRoles.CRM_COLLABORATOR, ShodroneRoles.CRM_MANAGER);
        showRequestRepository.save(showRequest);
    }

    public void editShowRequestDescription(ShowRequest showRequest, File description) {
        ShowRequestDescription descriptionObj = new ShowRequestDescription(description);
        showRequest.setDescription(descriptionObj);
        showRequest.updateVersions(showRequest);
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER,ShodroneRoles.CRM_COLLABORATOR, ShodroneRoles.CRM_MANAGER);
        showRequestRepository.save(showRequest);
    }

    public void editShowRequestPlace(ShowRequest showRequest, String place) {
        Place placeObj = new Place(place);
        showRequest.setPlace(placeObj);
        showRequest.updateVersions(showRequest);
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER,ShodroneRoles.CRM_COLLABORATOR, ShodroneRoles.CRM_MANAGER);
        showRequestRepository.save(showRequest);
    }

    public void editShowRequestDate(ShowRequest showRequest, String date) {
        Date dateObj = new Date(date);
        showRequest.setDate(dateObj);
        showRequest.updateVersions(showRequest);
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER,ShodroneRoles.CRM_COLLABORATOR, ShodroneRoles.CRM_MANAGER);
        showRequestRepository.save(showRequest);
    }

    public void editShowRequestStatus(ShowRequest showRequest, ShowRequestStatus status) {
        showRequest.setStatus(status);
        showRequest.updateVersions(showRequest);
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER,ShodroneRoles.CRM_COLLABORATOR, ShodroneRoles.CRM_MANAGER);
        showRequestRepository.save(showRequest);
    }

    public boolean isShowRequestAvailable(Place place, String date) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR, ShodroneRoles.CRM_MANAGER);
        return showRequestRepository.findShowRequestsByDateAndPlace(place, new Date(date)).isPresent();
    }
    public boolean isShowRequestAvailable(String place, Date date) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR, ShodroneRoles.CRM_MANAGER);
        return showRequestRepository.findShowRequestsByDateAndPlace(new Place(place), date).isPresent();
    }
}
