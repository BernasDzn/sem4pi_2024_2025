package shodrone.core.domain.showrequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;

import eapli.framework.infrastructure.authz.domain.model.Role;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.infrastructure.authz.domain.model.SystemUserBuilder;
import eapli.framework.infrastructure.authz.domain.model.Username;
import shodrone.core.domain.common.Date;
import shodrone.core.domain.common.Place;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.user.ShodronePasswordEncoder;
import shodrone.core.domain.user.ShodronePasswordPolicy;

import java.io.File;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ShowRequestTest {
    Customer customer;
    ShowRequestNumberOfDrones numberOfDrones;
    ShowRequestDuration duration;
    Place place;
    Date date;
    ShowRequestStatus status;
    ShowRequestDescription description;
    String path = "src/test/resources/";
    String name = "testDescription.pdf";
    SystemUser author;
    SystemUserBuilder userBuilder;
    
    @BeforeEach
    void setUp() {
        userBuilder = new SystemUserBuilder(new ShodronePasswordPolicy(), new ShodronePasswordEncoder());
        Set<Role> roles = new HashSet<>();
        roles.add(Role.valueOf("POWER_USER"));
        userBuilder.with("User", "Password123", "User", "Author", "user@email.com").createdOn(Calendar.getInstance()).withRoles();
        author = userBuilder.build();
        customer = new Customer("PT123456789", "Francisco", "francisco@shodrone.com",
                "Rua 1", 123, "4000-100", "Portugal", "Porto");

        numberOfDrones = new ShowRequestNumberOfDrones(10);
        duration = new ShowRequestDuration(15);
        description = new ShowRequestDescription(new File(path + name));
        place = new Place("10,-15");
        date = new Date("15/10/2026 12:15");
        status = ShowRequestStatus.NEW;

    }

    @Test
    public void ensureShowRequestCreation() {
        ShowRequest showRequest = new ShowRequest(author, customer, numberOfDrones, duration, description, place, date, status);
        assertNotNull(showRequest);
        assertEquals(customer, showRequest.getCustomer());
        assertEquals(numberOfDrones, showRequest.getNumberOfDrones());
        assertEquals(duration, showRequest.getDuration());
        assertEquals(description, showRequest.getDescription());
        assertEquals(place, showRequest.getPlace());
        assertEquals(date, showRequest.getDate());
        assertEquals(status, showRequest.getStatus());
    }

    @Test
    public void ensureShowRequestCustomerCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequest(author,null, numberOfDrones, duration, description, place, date, status);
        });
    }

    @Test
    public void ensureShowRequestNumberOfDronesCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequest(author, customer, null, duration, description, place, date, status);
        });
    }

    @Test
    public void ensureShowRequestDurationCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequest(author, customer, numberOfDrones, null, description, place, date, status);
        });
    }

    @Test
    public void ensureShowRequestDescriptionCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequest(author, customer, numberOfDrones, duration, null, place, date, status);
        });
    }

    @Test
    public void ensureShowRequestPlaceCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequest(author, customer, numberOfDrones, duration, description, null, date, status);
        });
    }

    @Test
    public void ensureShowRequestDateCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequest(author, customer, numberOfDrones, duration, description, place, null, status);
        });
    }

    @Test
    public void ensureShowRequestStatusCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new ShowRequest(author, customer, numberOfDrones, duration, description, place, date, null);
        });
    }
}