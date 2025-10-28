package shodrone.core.domain.showproposal;

import eapli.framework.infrastructure.authz.domain.model.Role;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.SystemUserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shodrone.core.domain.common.Address;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.drone_model.DroneModelBuilder;
import shodrone.core.domain.figure.*;
import shodrone.core.domain.figure.figurekeyword.FigureKeyword;
import shodrone.core.domain.figurecategory.FigureCategory;
import shodrone.core.domain.user.ShodronePasswordEncoder;
import shodrone.core.domain.user.ShodronePasswordPolicy;

import java.io.File;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class ShowProposalDroneModelTypesTest {

    FigureName name;
    Description description;
    VersionNumber version;
    File dslCode;
    FigureStatus status;
    Customer customer;
    FigureCategory category;
    List<FigureKeyword> keywords;
    Address address;
    SystemUser author;
    SystemUserBuilder userBuilder;
    Figure figure;

    @BeforeEach
    void setUp() {
        userBuilder = new SystemUserBuilder(new ShodronePasswordPolicy(), new ShodronePasswordEncoder());
        List<String> drones= new ArrayList<>();
        drones.add("drone1");
        Set<Role> roles = new HashSet<>();
        roles.add(Role.valueOf("POWER_USER"));
        userBuilder.with("User", "Password123", "User", "Author", "user@email.com").createdOn(Calendar.getInstance())
                .withRoles();
        author = userBuilder.build();
        name = FigureName.valueOf("TestFigure");
        description = Description.valueOf("A test figure description");
        version = VersionNumber.valueOf("1.0.0");
        dslCode = new File("src/test/resources/sample_DSL_figure_1.txt");
        status = FigureStatus.PRIVATE;
        address = new Address();
        customer = new Customer("PT123456789", "Customer", "customer@email.pt", address);
        category = new FigureCategory();
        keywords = List.of(FigureKeyword.valueOf("test"));

        figure = new Figure(name, description, version, dslCode, status, customer, category, author, keywords);
    }

    @Test
    void ensureDronesTypesExistInFigure() {
        assertThrows(IllegalArgumentException.class, () -> {
            ShowProposalDroneModelTypes.validate(
                    new DroneModelBuilder().named("Test").withDSL("1.0", "Test").build(), figure,
                    List.of("default1", "default2"));
        });
    }

    @Test
    void ensureDroneModelIsNotNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ShowProposalDroneModelTypes.validate(null, figure, List.of("default1"));
        });
        assertEquals("The drone type [default1] need to have a drone model!", exception.getMessage());
    }

    @Test
    void ensureFigureIsNotNull() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ShowProposalDroneModelTypes.validate(
                    new DroneModelBuilder().named("Test").withDSL("1.0", "Test").build(), null, List.of("default1"));
        });
        assertEquals("The drone type [default1] is not associated to a valid figure!", exception.getMessage());
    }

    @Test
    void ensureDroneTypeIsNotNullOrEmpty() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ShowProposalDroneModelTypes.validate(
                    new DroneModelBuilder().named("Test").withDSL("1.0", "Test").build(), figure, null);
        });
        assertEquals("A drone model Test need to have a drone type!", exception.getMessage());
    }

}