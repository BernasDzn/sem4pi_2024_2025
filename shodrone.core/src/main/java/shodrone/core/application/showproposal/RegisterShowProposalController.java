package shodrone.core.application.showproposal;

import eapli.framework.application.UseCaseController;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import shodrone.core.domain.common.Date;
import shodrone.core.domain.common.Place;
import shodrone.core.domain.drone_model.DroneModel;
import shodrone.core.domain.figure.Figure;
import shodrone.core.domain.showproposal.*;
import shodrone.core.domain.showrequest.*;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.drone_model.DroneModelRepository;
import shodrone.core.persistence.showproposal.ShowProposalRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@UseCaseController
public class RegisterShowProposalController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    final ShowProposalRepository showProposalRepository = PersistenceContext.repositories().showProposals();
    final DroneModelRepository droneModelRepository = PersistenceContext.repositories().droneModels();

    private List<DroneModel> selectedDroneModels = new ArrayList<>();
    private List<ShowProposalDroneModelTypes> selectedDroneModelTypes = new ArrayList<>();
    private ShowProposalFigures selectedFigures;

    public ShowProposal registerShowProposal(ShowRequest showRequest, ShowProposalDescription description,
            ShowProposalDuration duration, ShowProposalNumberOfDrones numberOfDrones, ShowProposalVideo video,
            Place place, Date date) {
        ShowProposal showProposal = new ShowProposal(showRequest,
                ShowProposalStatus.CREATED, description, duration, numberOfDrones, selectedFigures,
                selectedDroneModelTypes, video, place, date);
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR,
                ShodroneRoles.CRM_MANAGER);
        try {
            return showProposalRepository.save(showProposal);
        } catch (final Exception e) {
            throw new IllegalStateException("abc: " + e.getMessage(), e);
        }
    }

    public ShowProposal registerShowProposal(ShowProposal showProposal) {
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR,
                ShodroneRoles.CRM_MANAGER);
        try {
            return showProposalRepository.save(showProposal);
        } catch (final Exception e) {
            throw new IllegalStateException("Failed to register show proposal: " + e.getMessage(), e);
        }
    }

    public Iterable<DroneModel> getAllDroneModels() {
        return droneModelRepository.findAll();
    }

    public void validateFigures(Map<Integer, Figure> figures) {
        ShowProposalFigures.validate(figures);
    }
    public void addFigures(Map<Integer, Figure> figures) {
        selectedFigures = new ShowProposalFigures(figures);
    }

    public void addDroneModel(DroneModel droneModel) {
        selectedDroneModels.add(droneModel);
    }

    public void addDroneModelType(DroneModel droneModel, Figure figure, List<String> types) {
        ShowProposalDroneModelTypes newDMT = new ShowProposalDroneModelTypes(droneModel, figure, types);
        selectedDroneModelTypes.add(newDMT);
    }

    public List<DroneModel> getSelectedDroneModels() {
        return new ArrayList<>(selectedDroneModels);
    }

    public List<ShowProposalDroneModelTypes> getSelectedDroneModelTypes() {
        return new ArrayList<>(selectedDroneModelTypes);
    }
}
