package shodrone.core.application.customer;

import eapli.framework.general.domain.model.EmailAddress;
import eapli.framework.infrastructure.authz.application.AuthorizationService;
import eapli.framework.infrastructure.authz.application.AuthzRegistry;
import shodrone.core.application.customer_rep.RegisterRepresentativeController;
import shodrone.core.application.user.AddUserController;
import shodrone.core.domain.common.Address;
import shodrone.core.domain.common.Country;
import shodrone.core.domain.common.PostalCodeValidator;
import shodrone.core.domain.common.StringValidator;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.customer.VATNumber;
import shodrone.core.domain.customer_rep.Representative;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.persistence.customer.CustomerRepository;

public class RegisterCustomerController {

    private final AuthorizationService authz = AuthzRegistry.authorizationService();
    private final CustomerRepository repo = PersistenceContext.repositories().customers();
    private final RegisterRepresentativeController ctrlRegRepresentative = new RegisterRepresentativeController();
    private final AddUserController addUserController = new AddUserController();

    public Customer registerCustomer(String vat, String customerName, String email, String streetName,
                                    int addressNumber, String postalCode, String country, String city,
                                    String repEmail, String repPosition,
                                    String repFirstName, String repLastName,
                                    String repUsername, String repPassword
    ) 
{

        Representative newCustomerRepresentative = ctrlRegRepresentative.registerCustomerRepresentative(
            repEmail, repPosition, repFirstName,  repLastName, repUsername, repPassword
        );
        
        Customer newCustomer = new Customer(
                vat,
                customerName,
                email,
                streetName,
                addressNumber,
                postalCode,
                country,
                city,
                newCustomerRepresentative
        );
        authz.ensureAuthenticatedUserHasAnyOf(ShodroneRoles.POWER_USER, ShodroneRoles.CRM_COLLABORATOR,
                ShodroneRoles.CRM_MANAGER);
        return repo.save(newCustomer);
    }

    public boolean validateVAT(String vat) {
        return VATNumber.isValid(vat);
    }

    public boolean validateCustomerName(String customerName) {
        return Customer.isValidCustomerName(customerName);
    }

    public boolean validateStreetName(String streetName) {
        return Address.isValidStreetName(streetName);
    }

    public boolean validateAddressNumber(int addressNumber) {
        return Address.isValidAddressNumber(addressNumber);
    }

    public boolean validatePostalCode(String postalCode, String countryCode) {
        return PostalCodeValidator.isValid(Country.fromCountryName(countryCode), postalCode);
    }

    public boolean validateCity(String city) {
        return Address.isValidCity(city);
    }

    public boolean validateCountry(String country) {
        return Country.isValid(country);
    }

    public boolean validateEmail(String email) {
        EmailAddress.valueOf(email);
        return true;
    }

    public void validateName(String name) {
        StringValidator.isValid(name, "Name", 255);
    }

    public void validateUsername(String repUsername) {
        addUserController.validateUserName(repUsername);
    }

    public void validatePassword(String repPassword) {
        addUserController.validatePassword(repPassword);
    }

}
