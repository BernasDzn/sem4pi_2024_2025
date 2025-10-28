package shodrone.core.domain.customer;

import eapli.framework.general.domain.model.Designation;
import eapli.framework.general.domain.model.EmailAddress;
import org.junit.jupiter.api.Test;
import shodrone.core.domain.common.Address;

import static org.junit.jupiter.api.Assertions.*;

class CustomerTest {
    @Test
    void ensureCustomerCreation() {
        String vatNumber = "PT123456789";
        String name = "Test Company";
        String email = "test@test.com";
        Address address = new Address("Test Street", 123, "1234-123", "Portugal", "Test City");
        Customer customer = new Customer(vatNumber, name, email, address);
        assertNotNull(customer);
        assertEquals(vatNumber, customer.getVatNumber().toString());
        assertEquals(name, customer.getCustomerName().toString());
        assertEquals(email, customer.getEmailAddress().toString());
        assertEquals(address, customer.getAddress());
    }
}