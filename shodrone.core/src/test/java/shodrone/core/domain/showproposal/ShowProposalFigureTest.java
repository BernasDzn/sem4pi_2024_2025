package shodrone.core.domain.showproposal;

import static org.junit.jupiter.api.Assertions.*;

import eapli.framework.infrastructure.authz.domain.model.Role;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.SystemUserBuilder;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shodrone.core.domain.common.Address;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.figure.*;
import shodrone.core.domain.figure.figurekeyword.FigureKeyword;
import shodrone.core.domain.figurecategory.FigureCategory;
import shodrone.core.domain.user.ShodronePasswordEncoder;
import shodrone.core.domain.user.ShodronePasswordPolicy;

import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.io.File;
import java.util.*;

class ShowProposalFigureTest {

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
    Figure figure2;
    Map<Integer, Figure> figuresMap;

    @BeforeEach
    void setUp() {
        userBuilder = new SystemUserBuilder(new ShodronePasswordPolicy(), new ShodronePasswordEncoder());
        List<String> drones= new ArrayList<>();
        drones.add("drone1");
        Set<Role> roles = new HashSet<>();
        roles.add(Role.valueOf("POWER_USER"));
        userBuilder.with("User", "Password123", "User", "Author", "user@email.com").createdOn(Calendar.getInstance()).withRoles();
        author = userBuilder.build();
        name = FigureName.valueOf("TestFigure");
        description = Description.valueOf("A test figure description");
        version = VersionNumber.valueOf("1.0.0");
        dslCode = new File("src/test/resources/sample_DSL_figure_1.txt");
        status = FigureStatus.PRIVATE;
        address = new Address();
        customer = new Customer("PT123456789", "Customer", "customer@email.pt", address );
        category = new FigureCategory();
        keywords = List.of(FigureKeyword.valueOf("test"));

        figure = new Figure(name, description, version, dslCode, status, customer, category, author, keywords);
        figure2 = new Figure(FigureName.valueOf("TestFigure2"), description, version, dslCode, status, customer, category, author, keywords);
        figuresMap = new LinkedHashMap<>();
        figuresMap.put(1, figure);
        figuresMap.put(2, figure2);
        figuresMap.put(3, figure);
    }

    @Test
    void ensureValidShowProposalFigureCanBeCreated() {
        ShowProposalFigures showProposalFigures = new ShowProposalFigures(figuresMap);
        assertEquals(figure, showProposalFigures.getFigures().get(0).getFigure());
        assertEquals(1, showProposalFigures.getFigures().get(0).getOrderNumber());
    }

    @Test
    void ensureInvalidOrderNumberThrowsException() {
        figuresMap.put(0, figure2);
        assertThrows(IllegalArgumentException.class, () -> new ShowProposalFigures(figuresMap));
        figuresMap.remove(0);
        figuresMap.put(-1, figure2);
        assertThrows(IllegalArgumentException.class, () -> new ShowProposalFigures(figuresMap));
    }

    @Test
    void ensureNullFigureThrowsException() {
        figuresMap.put(1, null);
        assertThrows(IllegalArgumentException.class, () -> new ShowProposalFigures(figuresMap));
    }

    @Test
    void ensureCorrectOrderNumbering() {
        figuresMap.put(5, figure2);
        figuresMap.put(4, figure);
        assertThrows(IllegalArgumentException.class, () -> new ShowProposalFigures(figuresMap));
    }

    @Test
    void ensureConsecutiveOrderNumbers() {
        figuresMap.put(5, figure2);
        assertThrows(IllegalArgumentException.class, () -> new ShowProposalFigures(figuresMap));
    }

    @Test
    void ensureFirstOrderNumberIsOne() {
        figuresMap.remove(1);
        assertThrows(IllegalArgumentException.class, () -> new ShowProposalFigures(figuresMap));
    }

    @Test
    void ensureSameFigureConsecutivelyThrowsException() {
        figuresMap.put(4, figure);
        assertThrows(IllegalArgumentException.class, () -> new ShowProposalFigures(figuresMap));
    }

    @Test
    void ensureEmptyMapThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new ShowProposalFigures(Map.of()));
    }

    @Test
    void ensureNullMapThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> new ShowProposalFigures(null));
    }

}