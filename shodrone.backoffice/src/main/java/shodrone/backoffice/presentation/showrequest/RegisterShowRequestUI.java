package shodrone.backoffice.presentation.showrequest;

import eapli.framework.domain.repositories.ConcurrencyException;
import eapli.framework.domain.repositories.IntegrityViolationException;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import eapli.framework.validations.Preconditions;
import shodrone.core.presentation.console.ConsoleEvent;
import shodrone.backoffice.presentation.customer.CustomerPrinter;
import shodrone.core.application.customer.ListCustomerController;
import shodrone.core.application.showrequest.RegisterShowRequestController;
import shodrone.core.domain.customer.Customer;

import java.awt.*;
import java.io.File;

public class RegisterShowRequestUI extends AbstractUI {
    private final RegisterShowRequestController controller = new RegisterShowRequestController();

    private final ListCustomerController customer_controller = new ListCustomerController();

    @Override
    protected boolean doShow() {

        Customer selectedCustomer = inputCustomer();
        if (selectedCustomer == null) {
            return false;
        }
        int numberOfDrones = inputNumberOfDrones();
        int duration = inputDuration();
        String place = "";
        String date = "";
        boolean isValid = false;
        while (!isValid) {
            place = inputPlace();
            date = inputDate();
            try {
                if (controller.isShowRequestAvailable(place, date)) {
                    ConsoleEvent.error("Show request already exists for this place and date");
                } else {
                    isValid = true;
                }
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        File description = inputDescriptionFile();

        try {
            Preconditions.noneNull(selectedCustomer, numberOfDrones, duration, description, place, date);
            controller.registerShowRequest(
                    selectedCustomer,
                    numberOfDrones,
                    duration,
                    description,
                    place,
                    date
            );
            System.out.println();
            ConsoleEvent.success("Show Request Registered");
        } catch (final IntegrityViolationException | ConcurrencyException e) {
            ConsoleEvent.error(e.getMessage());
        }

        return false;
    }

    private Customer inputCustomer() {
        Customer selectedCustomer = null;
        boolean isValid = false;
        while (!isValid) {
            try {
                final Iterable<Customer> allCustomers = customer_controller.allCustomers();

                if (allCustomers.iterator().hasNext()) {
                    final SelectWidget<Customer> selector = new SelectWidget<>("Select a customer:", allCustomers, new CustomerPrinter());
                    selector.show();
                    selectedCustomer = selector.selectedElement();
                    if (selectedCustomer == null) {
                        return null;
                    }
                    isValid = true;
                } else {
                    System.out.println("No customers available.");
                    return null;
                }
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return selectedCustomer;
    }

    private int inputNumberOfDrones() {
        int numberOfDrones = 0;
        boolean isValid = false;
        while (!isValid) {
            try {
                numberOfDrones = Console.readInteger("Number of drones");
                controller.validateNumberOfDrones(numberOfDrones);
                isValid = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return numberOfDrones;
    }

    private int inputDuration() {
        int duration = 0;
        boolean isValid = false;
        while (!isValid) {
            try {
                duration = Console.readInteger("Duration (Minutes)");
                controller.validateDuration(duration);
                isValid = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return duration;
    }

    private String inputDate() {
        String date = "";
        boolean isValid = false;
        while (!isValid) {
            try {
                date = Console.readNonEmptyLine("Date (dd/mm/yyyy hh:mm)", "Date cannot be empty");
                controller.validateDate(date);
                isValid = true;
            }catch(Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return date;
    }

    private String inputPlace() {
        String coordinates = "";
        boolean isValid = false;
        while (!isValid) {
            try {
                coordinates = Console.readNonEmptyLine("Place (latitude, longitude)", "Coordinates cannot be empty");
                controller.validatePlace(coordinates);
                isValid = true;
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return coordinates;
    }

    private File inputDescriptionFile() {
        File descriptionFile = null;
        boolean isValid = false;
        while (!isValid) {
            try {
                System.out.println("Select a description file");
                FileDialog dialog = new FileDialog((Frame)null, "Select a Description File", FileDialog.LOAD);
                dialog.setFile("*.pdf");
                dialog.setVisible(true);
                String file = dialog.getFile();
                dialog.dispose();
                descriptionFile = new File(dialog.getDirectory(), file);
                controller.validateDescriptionFile(descriptionFile);
                isValid = true;
                System.out.println(descriptionFile.getName());
            } catch (Exception e) {
                ConsoleEvent.error(e.getMessage());
            }
        }
        return descriptionFile;
    }

    @Override
    public String headline() {
        return "Register Show Request";
    }
}
