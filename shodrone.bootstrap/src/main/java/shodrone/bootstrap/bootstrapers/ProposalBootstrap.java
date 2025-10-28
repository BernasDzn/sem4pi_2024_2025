package shodrone.bootstrap.bootstrapers;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import eapli.framework.actions.Action;
import eapli.framework.general.domain.model.Description;
import eapli.framework.general.domain.model.Designation;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import shodrone.core.application.customer.RegisterCustomerController;
import shodrone.core.application.drone_model.AddDroneModelController;
import shodrone.core.application.figure.AddFigureController;
import shodrone.core.application.figure.ListFigureKeywordsController;
import shodrone.core.application.figurecategory.AddFigureCategoryController;
import shodrone.core.application.showproposal.RegisterShowProposalController;
import shodrone.core.application.showrequest.RegisterShowRequestController;
import shodrone.core.domain.common.Date;
import shodrone.core.domain.common.Place;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.drone_model.DroneModel;
import shodrone.core.domain.figure.Figure;
import shodrone.core.domain.figure.FigureName;
import shodrone.core.domain.figure.FigureStatus;
import shodrone.core.domain.figure.VersionNumber;
import shodrone.core.domain.figure.figurekeyword.FigureKeyword;
import shodrone.core.domain.figurecategory.FigureCategory;
import shodrone.core.domain.showproposal.ShowProposal;
import shodrone.core.domain.showproposal.ShowProposalDescription;
import shodrone.core.domain.showproposal.ShowProposalDroneModelTypes;
import shodrone.core.domain.showproposal.ShowProposalDuration;
import shodrone.core.domain.showproposal.ShowProposalFigures;
import shodrone.core.domain.showproposal.ShowProposalNumberOfDrones;
import shodrone.core.domain.showproposal.ShowProposalStatus;
import shodrone.core.domain.showproposal.ShowProposalVideo;
import shodrone.core.domain.showrequest.ShowRequest;
import shodrone.core.domain.user.ShodroneRoles;

public class ProposalBootstrap extends AbstractUserBootstrapper implements Action {

	@Override
	public boolean execute() {
		Customer customer = registerCustomer(
			"PT123456789",
			"Your Average Customer, LDA.",
			"geral@yac.com",
			"Rua do Cliente",
			123,
			"1234-567",
			"Portugal",
			"Lisbon",
			"yacuser@yac.com",
			"Manager",
			"John",
			"Customer",
			"yacUser",
			"yacUser1"
		);

		File descFile = new File("test.pdf");
		ShowRequest request = registerShowRequest(customer, 5, 60,
			descFile, "41.11563539854244, -8.617785266181848", "24/12/2090 12:00");

		FigureCategory figureCategory = registerFigureCategory("Test Category", "A category for testing purposes");
		File figureDSL =  new File("figure_DSL_1.txt");
		SystemUser showDes = registerUser("showDes","showDes1"
			, "Show","Designer","showdes@shodrone.com", Set.of(ShodroneRoles.SHOW_DESIGNER));
		
		FigureKeyword keyword = registerKeywork("test");	
		Figure figure = registerFigure("Test", "Test Figure"
			, "v 1.1", figureDSL, figureCategory, showDes, keyword);

		DroneModel droneModel = registerDroneModel("Test Drone Model", "Drone DSL");

		registerShowProposal(
			droneModel,
			request,
			descFile,
			figure,
			60,
			5,
			"https://example.com/video.mp4",
			"41.11563539854244, -8.617785266181848",
			"24/12/2090 12:00"
		);
		return true;
	}

	private FigureKeyword registerKeywork(String string) {
		ListFigureKeywordsController controller = new ListFigureKeywordsController();
		return controller.addFigureKeyword(string);
	}

	private DroneModel registerDroneModel(String modName, String modDSL) {
		AddDroneModelController controller = new AddDroneModelController();
		return controller.addDroneModel(modName,modDSL);
	}

	private ShowProposal registerShowProposal(
		DroneModel dm,
		ShowRequest sr,
		File spDesc,
		Figure fig,
		int dur,
		int nOd,
		String videoURL,
		String placeCoordinates,
		String date
	) {
		RegisterShowProposalController controller = new RegisterShowProposalController();
		Map<Integer, Figure> spf = new HashMap<>();
		spf.put(1, fig);
		ShowProposalDroneModelTypes dmt = new ShowProposalDroneModelTypes(dm, fig, List.of("droneTypeA"));
		
		ShowProposal prop = new ShowProposal(sr, ShowProposalStatus.CREATED
			, new ShowProposalDescription(spDesc), new ShowProposalDuration(dur)
			, new ShowProposalNumberOfDrones(nOd),new ShowProposalFigures(spf)
			, List.of(dmt), new ShowProposalVideo(videoURL), new Place(placeCoordinates)
			, new Date(date));
		return controller.registerShowProposal(prop);
	}

	private FigureCategory registerFigureCategory(String catName, String catDescription) {
		AddFigureCategoryController controller = new AddFigureCategoryController();
		return controller.addFigureCategory(Designation.valueOf(catName), Description.valueOf(catDescription), Calendar.getInstance(), Calendar.getInstance());
	}

	protected Figure registerFigure(String figureName, String figDescription, String verNumber,
			File dslFile, FigureCategory figureCategory, SystemUser showDes, FigureKeyword keyword) {
		AddFigureController controller = new AddFigureController();
		List<FigureKeyword> keywords = new ArrayList<>();
		keywords.add(keyword);
		return controller.addFigure(new FigureName(figureName), new shodrone.core.domain.figure.Description(figDescription), 
				new VersionNumber(verNumber), dslFile, FigureStatus.COMISSIONED, null, figureCategory, showDes, keywords);
	}

	protected ShowRequest registerShowRequest(Customer customer, int numOfDrones, int duration,
			File descFile, String place, String date) {
		RegisterShowRequestController controller = new RegisterShowRequestController();
		return controller.registerShowRequest(customer, numOfDrones, duration, descFile, place, date);
	}

	protected Customer registerCustomer(String vat, String custName, String email, String streetName,
			int streetNum, String postCode, String country, String city,
			String repEmail, String repPos, String repFirstName, String repLastName,
			String repUsername, String repPassword)
	{
		RegisterCustomerController controller = new RegisterCustomerController();
		return controller.registerCustomer(vat,custName,email,streetName,streetNum, postCode,country,city,repEmail,repPos, repFirstName,repLastName,repUsername,repPassword);
	}


	
}
