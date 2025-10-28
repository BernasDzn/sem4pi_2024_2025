package shodrone.core.domain.showproposal;

import eapli.framework.general.domain.model.Designation;
import eapli.framework.infrastructure.authz.domain.model.Role;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.SystemUserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shodrone.core.domain.common.Address;
import shodrone.core.domain.common.DSL;
import shodrone.core.domain.common.Date;
import shodrone.core.domain.common.Place;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.drone_model.DroneModel;
import shodrone.core.domain.drone_model.DroneModelBuilder;
import shodrone.core.domain.figure.*;
import shodrone.core.domain.figure.figurekeyword.FigureKeyword;
import shodrone.core.domain.figurecategory.FigureCategory;
import shodrone.core.domain.showrequest.*;
import shodrone.core.domain.user.ShodronePasswordEncoder;
import shodrone.core.domain.user.ShodronePasswordPolicy;

import java.io.File;
import java.util.*;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShowProposalTest {
    Customer customer;
    ShowProposalNumberOfDrones SPnumberOfDrones;
    ShowProposalDuration SPduration;
    Place SPplace;
    Date SPdate;
    ShowProposalStatus SPstatus;
    ShowProposalDescription SPdescription;
    ShowProposalVideo SPvideo;
    ShowProposalFigures figures;
    List<ShowProposalDroneModelTypes> droneModelTypes;
    String path = "src/test/resources/";
    String name = "testDescription.pdf";
    SystemUser author;
    SystemUserBuilder userBuilder;

    ShowRequestNumberOfDrones SRnumberOfDrones;
    ShowRequestDuration SRduration;
    Place SRplace;
    Date SRdate;
    ShowRequestStatus SRstatus;
    ShowRequestDescription SRdescription;
    ShowRequest showRequest;

    Figure figure;
    Figure figure2;

    @BeforeEach
    void setUp() {
        userBuilder = new SystemUserBuilder(new ShodronePasswordPolicy(), new ShodronePasswordEncoder());
        Set<Role> roles = new HashSet<>();
        roles.add(Role.valueOf("POWER_USER"));
        userBuilder.with("User", "Password123", "User", "Author", "user@email.com").createdOn(Calendar.getInstance())
                .withRoles();
        author = userBuilder.build();
        customer = new Customer("PT123456789", "Bernardo", "bernardo@shodrone.com",
                "Rua 1", 123, "4000-100", "Portugal", "Porto");

        SPnumberOfDrones = new ShowProposalNumberOfDrones(10);
        SPduration = new ShowProposalDuration(15);
        SPdescription = new ShowProposalDescription(new File(path + name));
        SPplace = new Place("10,-15");
        SPdate = new Date("15/10/2026 12:15");
        SPstatus = ShowProposalStatus.CREATED;
        SPvideo = new ShowProposalVideo("https://example.com/video.mp4");

        SRnumberOfDrones = new ShowRequestNumberOfDrones(10);
        SRduration = new ShowRequestDuration(15);
        SRdescription = new ShowRequestDescription(new File(path + name));
        SRplace = new Place("10,-15");
        SRdate = new Date("15/10/2026 12:15");
        SRstatus = ShowRequestStatus.NEW;

        figure = new Figure(
                new FigureName("Test Figure"),
                new Description("Test Description"),
                new VersionNumber("1.0"),
                new File("src/test/resources/sample_DSL_figure_1.txt"),
                FigureStatus.COMISSIONED,
                new Customer("PT123456789", "Bernardo", "bernardo@gmail.com",
                        new Address("Rua 1", 123, "4000-100", "Portugal", "Porto")),
                new FigureCategory(
                        Designation.valueOf("Test Category"),
                        eapli.framework.general.domain.model.Description.valueOf("Test Category Description"),
                        Calendar.getInstance(),
                        Calendar.getInstance()),
                author,
                List.of(new FigureKeyword("Test Keyword")));

        figure2 = new Figure(
                new FigureName("Test Figure 2"),
                new Description("Test Description 2"),
                new VersionNumber("2.0"),
                new File("src/test/resources/sample_DSL_figure_1.txt"),
                FigureStatus.COMISSIONED,
                new Customer("PT123456789", "Francisco", "francisco@gmail.com",
                        new Address("Rua 2", 123, "4000-100", "Portugal", "Porto")),
                new FigureCategory(
                        Designation.valueOf("Test Category"),
                        eapli.framework.general.domain.model.Description.valueOf("Test Category Description"),
                        Calendar.getInstance(),
                        Calendar.getInstance()),
                author,
                List.of(new FigureKeyword("Test Keyword")));

        Map<Integer, Figure> figuresMap = new LinkedHashMap<>();
        figuresMap.put(1, figure);
        figuresMap.put(2, figure2);
        figuresMap.put(3, figure);

        figures = new ShowProposalFigures(figuresMap);

        droneModelTypes = List.of(new ShowProposalDroneModelTypes(
                new DroneModelBuilder().named("Test Drone Model")
                        .withDSL("1.0", "Test DSL")
                        .build(),
                figure,
                List.of("droneTypeA")));
    }

    @Test
    public void ensureShowProposalCreation() {
        showRequest = new ShowRequest(author, customer, SRnumberOfDrones, SRduration, SRdescription, SRplace, SRdate,
                SRstatus);
        ShowProposal showProposal = new ShowProposal(showRequest, SPstatus, SPdescription, SPduration, SPnumberOfDrones,
                figures, droneModelTypes, SPvideo, SPplace, SPdate);
        assertNotNull(showProposal);
        assertEquals(SPstatus, showProposal.getStatus());
        assertEquals(SPdescription, showProposal.getDescription());
        assertEquals(SPduration, showProposal.getDuration());
        assertEquals(SPnumberOfDrones, showProposal.getNumberOfDrones());
        assertEquals(SPplace, showProposal.getPlace());
        assertEquals(SPdate, showProposal.getDate());
        assertEquals(SPvideo, showProposal.getVideo());
        assertEquals(customer, showProposal.getShowRequest().getCustomer());
    }

    @Test
    public void ensureShowProposalCustomerCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposal(null, SPstatus, SPdescription, SPduration, SPnumberOfDrones, figures, droneModelTypes,
                    SPvideo, SPplace, SPdate);
        });
    }

    @Test
    public void ensureShowProposalNumberOfDronesCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposal(showRequest, SPstatus, SPdescription, SPduration, null, figures, droneModelTypes, SPvideo,
                    SPplace, SPdate);
        });
    }

    @Test
    public void ensureShowProposalDurationCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposal(showRequest, SPstatus, SPdescription, null, SPnumberOfDrones, figures, droneModelTypes,
                    SPvideo, SPplace, SPdate);
        });
    }

    @Test
    public void ensureShowProposalDescriptionCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposal(showRequest, SPstatus, null, SPduration, SPnumberOfDrones, figures, droneModelTypes,
                    SPvideo, SPplace, SPdate);
        });
    }

    @Test
    public void ensureShowProposalPlaceCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposal(showRequest, SPstatus, SPdescription, SPduration, SPnumberOfDrones, figures,
                    droneModelTypes, SPvideo, null, SPdate);
        });
    }

    @Test
    public void ensureShowProposalDateCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposal(showRequest, SPstatus, SPdescription, SPduration, SPnumberOfDrones, figures,
                    droneModelTypes, SPvideo, SPplace, null);
        });
    }

    @Test
    public void ensureShowProposalVideoCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposal(showRequest, SPstatus, SPdescription, SPduration, SPnumberOfDrones, figures,
                    droneModelTypes, null, SPplace, SPdate);
        });
    }

    @Test
    public void ensureShowProposalStatusCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowProposal(showRequest, null, SPdescription, SPduration, SPnumberOfDrones, figures, droneModelTypes,
                    SPvideo, SPplace, SPdate);
        });
    }
}