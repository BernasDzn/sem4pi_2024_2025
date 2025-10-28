package shodrone.core.domain.figure;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.figure.figurekeyword.FigureKeyword;
import shodrone.core.domain.figurecategory.FigureCategory;
import shodrone.core.domain.common.Address;
import shodrone.core.domain.user.ShodronePasswordEncoder;
import shodrone.core.domain.user.ShodronePasswordPolicy;

import eapli.framework.infrastructure.authz.domain.model.Role;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.SystemUserBuilder;


import java.io.File;
import java.util.*;

import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


public class FigureTest {

    private FigureName name;
    private Description description;
    private VersionNumber version;
    private File dslCode;
    private FigureStatus status;
    private Customer customer;
    private FigureCategory category;
    private List<FigureKeyword> keywords;
    private Address address;
    SystemUser author;
    SystemUserBuilder userBuilder;

    @BeforeEach
    void setUp() {
        List<String> drones= new ArrayList<>();
        drones.add("drone1");
        userBuilder = new SystemUserBuilder(new ShodronePasswordPolicy(), new ShodronePasswordEncoder());
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
    }

    @Test
    public void ensureFigureCanBeCreatedSuccessfully(){
        Figure figure=new Figure(name, description, version, dslCode, status, customer, category, author, keywords);

        assertNotNull(figure);
        assertEquals(name, figure.getFigureName());
        assertEquals(description, figure.getDescription());
        assertEquals(version, figure.getVersionNumber());
        assertEquals(category, figure.getCategory());
        assertEquals(author, figure.getShowDesigner());
        assertEquals(status, figure.getStatus());
        assertEquals(keywords, figure.getKeywords());
    }

    @Test
    public void ensureFigureNameCannotBeNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Figure(null, description, version, dslCode, status, customer, category, author, keywords);
        });
    }


    @Test
    public void ensureVersionNumberCannotBeNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Figure(name, description, null, dslCode,  status, customer, category, author, keywords);
        });
    }

    @Test
    public void ensureStatusCannotBeNull(){
        assertThrows(IllegalArgumentException.class,() -> {
            new Figure(name, description, version, dslCode,  null, customer, category, author, keywords);
        });
    }

    @Test
    public void ensureCategoryCannotBeNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Figure(name, description, version, dslCode,  status, customer,null, author, keywords);
        });
    }

    @Test
    public void ensureShowDesignCannotBeNull(){
        assertThrows(IllegalArgumentException.class,() -> {
            new Figure(name, description, version, dslCode,  status, customer,category,null, keywords);
        });
    }

    @Test
    public void ensureKeywordsCannotBeNull(){
        assertThrows(IllegalArgumentException.class, () -> {
            new Figure(name, description, version, dslCode, status, customer, category, author, null);
        });
    }

}
