package shodrone.backoffice.presentation.customer;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import shodrone.backoffice.presentation.ConfirmationUI;
import shodrone.core.presentation.console.ConsoleEvent;
import shodrone.core.application.customer.RegisterCustomerController;
import shodrone.core.domain.customer.Customer;

public class RegisterCustomerUI extends AbstractUI {

    private final RegisterCustomerController theController = new RegisterCustomerController();

    @Override
    protected boolean doShow() {
        
        String vat = askVAT();
        String customerName = askCustomerName();
        String email = askEmail();
        String streetName = askStreetName();
        int addressNumber = askAddressNumber();
        String country = askCountry();
        String postalCode = askPostalCode(country);
        String city = askCity();

        System.out.println();
        ConsoleEvent.blue("Register new representative for " + customerName + ":");
        String repFirstName = askName("First Name");
        String repLastName = askName("Last Name");
        String repEmail = askEmail();
        String repPosition = askName("Position");
        String repUsername = askUsername();
        String repPassword = askPassword();



        boolean canAdd = askConfirmation(
                vat,
                customerName,
                email,
                streetName,
                addressNumber,
                postalCode,
                country,
                city,
                repFirstName + " " + repLastName,
                repUsername
        );

        if(canAdd) {
            try {

                Customer newCustomer = this.theController.registerCustomer(
                        vat,
                        customerName,
                        email,
                        streetName,
                        addressNumber,
                        postalCode,
                        country,
                        city,
                        repEmail, repPosition, repFirstName,  repLastName, repUsername, repPassword
                );
                System.out.println();
                ConsoleEvent.success("Customer Registered: " + newCustomer.toString());
            }catch (final IntegrityViolationException | ConcurrencyException e) {
                ConsoleEvent.error(e.getMessage());
            }
        }else {
            System.out.println();
            ConsoleEvent.info("Operation cancelled by user.");
        }

        return false;
    }

    private String askPassword() {
        String repPassword = "";
        boolean allOk = false;
        while (!allOk) {
            try {
                repPassword = Console.readLine("Representative Password");
                theController.validatePassword(repPassword);
                allOk = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return repPassword;
    }

    private String askUsername() {
        String repUsername = "";
        boolean allOk = false;
        while (!allOk) {
            try {
                repUsername = Console.readLine("Representative Username");
                theController.validateUsername(repUsername);
                allOk = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return repUsername;
    }

    private boolean askConfirmation(String vat, String customerName, String email, String streetName,
            int addressNumber, String postalCode, String country, String city, String repName, String repUsername)
    {
        ConfirmationUI confirmationUI = new ConfirmationUI();
        String address = streetName + " " + addressNumber + ", " + postalCode + ", " + city + ", " + country;
        String representative = repName + " (@" + repUsername + ")";

        confirmationUI.add("VAT Number", vat).add("Customer Name", customerName)
                .add("Email", email).add("Address", address).add("Representative", representative);

        confirmationUI.show("Customer");
        return confirmationUI.option;

    }

    private String askEmail() {
        String email = "";
        boolean allOk = false;
        while (!allOk) {
            try {
                email = Console.readLine("Email");
                theController.validateEmail(email);
                allOk = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return email;
    }

    private String askVAT() {
        String vat = "";
        boolean allOk = false;
        while (!allOk) {
            try {
                vat = Console.readLine("VAT (country code + number)");
                theController.validateVAT(vat);
                allOk = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return vat;
    }

    private String askName(String fieldName) {
        String name = "";
        boolean allOk = false;
        while (!allOk) {
            try {
                name = Console.readLine(fieldName);
                theController.validateName(name);
                allOk = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return name;
    }

    private String askCustomerName() {
        String customerName = "";
        boolean allOk = false;
        while (!allOk) {
            try {
                customerName = Console.readLine("Customer Name");
                theController.validateCustomerName(customerName);
                allOk = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return customerName;
    }

    private String askStreetName() {
        String streetName = "";
        boolean allOk = false;
        while (!allOk) {
            try {
                streetName = Console.readLine("Street Name");
                theController.validateStreetName(streetName);
                allOk = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return streetName;
    }

    private int askAddressNumber() {
        int addressNumber = -1;
        boolean allOk = false;
        while (!allOk) {
            try {
                addressNumber = Console.readInteger("Address Number");
                theController.validateAddressNumber(addressNumber);
                allOk = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return addressNumber;
    }

    private String askPostalCode(String country) {
        String postalCode = "";
        boolean allOk = false;
        while (!allOk) {
            try {
                postalCode = Console.readLine("Postal Code");
                theController.validatePostalCode(postalCode, country);
                allOk = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return postalCode;
    }

    private String askCountry() {
        String country = "";
        boolean allOk = false;
        while (!allOk) {
            try {
                country = Console.readLine("Country");
                theController.validateCountry(country);
                allOk = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return country;
    }

    private String askCity() {
        String city = "";
        boolean allOk = false;
        while (!allOk) {
            try {
                city = Console.readLine("City");
                theController.validateCity(city);
                allOk = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return city;
    }

    @Override
    public String headline() {
        return "Register Customer";
    }
}
