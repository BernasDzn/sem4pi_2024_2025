package shodrone.backoffice.presentation.figure;

import eapli.framework.infrastructure.authz.domain.model.Name;
import eapli.framework.infrastructure.authz.domain.model.SystemUser;
import eapli.framework.io.util.Console;
import eapli.framework.presentation.console.AbstractUI;
import eapli.framework.presentation.console.SelectWidget;
import shodrone.core.domain.figure.*;
import shodrone.core.domain.figure.figurekeyword.FigureKeyword;
import shodrone.core.domain.user.ShodroneRoles;
import shodrone.core.persistence.PersistenceContext;
import shodrone.core.presentation.console.ConsoleEvent;
import shodrone.backoffice.presentation.customer.CustomerPrinter;
import shodrone.backoffice.presentation.figurecategory.FigureCategoryPrinter;
import shodrone.core.application.customer.ListCustomerController;
import shodrone.core.application.figurecategory.ListFigureCategoryController;
import shodrone.core.application.figure.ListFigureKeywordsController;
import shodrone.core.application.figure.AddFigureController;
import shodrone.core.domain.customer.Customer;
import shodrone.core.domain.figurecategory.FigureCategory;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

public class AddFigureUI extends AbstractUI {

    private final AddFigureController theController = new AddFigureController();
    private final ListCustomerController customerController = new ListCustomerController();
    private final ListFigureCategoryController categoryController = new ListFigureCategoryController();
    private final ListFigureKeywordsController keywordController = new ListFigureKeywordsController();

    @Override
    protected boolean doShow() {

        FigureName name = askName();
        Description description = askDescription();
        File dslCode = askDSL();
        Customer customer = null;
        try {
            customer = askCustomer();
        } catch (Exception e) {
            if (e.getMessage().contains("Cancel")) {
                return false;
            } else {
                System.out.println("No customers available, terminating registration.");
                return false;
            }
        }

        FigureCategory category = null;
        try {
            category = askFigureCategory();
        } catch (Exception e) {
            System.out.println("No Figure Categories available, terminating registration.");
            return false;
        }

        SystemUser showDesigner = null;
        try {
            showDesigner = askShowDesigner();
        } catch (Exception e) {
            System.out.println("No Show Designers available, terminating registration.");
            return false;
        }

        List<FigureKeyword> keywords = null;
        try {
            keywords = askKeywords();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            if (customer != null) {
                this.theController.addFigure(name, description, VersionNumber.valueOf("1.0.0"), dslCode,
                        FigureStatus.PRIVATE, customer, category, showDesigner, keywords);
                ConsoleEvent.success("Public Figure Registered: " + name);

            } else if (customer == null) {
                this.theController.addFigure(name, description, VersionNumber.valueOf("1.0.0"), dslCode,
                        FigureStatus.COMISSIONED, null, category, showDesigner, keywords);
                ConsoleEvent.success("Public Figure Registered: " + name);

            }

        } catch (final Exception e) {
            ConsoleEvent.error(e.getMessage());
        }

        return false;
    }

    private List<FigureKeyword> askKeywords() throws Exception {
        List<FigureKeyword> keywords = new ArrayList<>();
        String input;

        do {
            input = Console.readLine("Enter keywords separated by spaces:");
            if (input.trim().isEmpty()) {
                ConsoleEvent.error("At least one keyword must be provided.");
            }
        } while (input.trim().isEmpty());

        String[] keywordArray = input.trim().split("\\s+");

        for (String keywordStr : keywordArray) {
            var opt = keywordController.findByName(keywordStr);

            if (opt.isPresent()) {
                keywords.add(opt.get());
            } else {
                keywordController.addFigureKeyword(keywordStr);
                keywords.add(FigureKeyword.valueOf(keywordStr));
            }
        }

        return keywords;
    }

    private FigureCategory askFigureCategory() throws Exception {

        FigureCategory selectedCategory = null;
        while (selectedCategory == null) {

            final Iterable<FigureCategory> allCategories = categoryController.allFigureCategories();

            if (allCategories.iterator().hasNext()) {
                final SelectWidget<FigureCategory> selector = new SelectWidget<>("\nSelect a category:", allCategories,
                        new FigureCategoryPrinter());
                selector.show();
                selectedCategory = selector.selectedElement();
                if (selectedCategory == null) {
                    String answer;
                    do {
                        answer = Console.readLine("Quit figure registration? Yes-y No-n");
                        if (answer.equalsIgnoreCase("y")) {
                            return null;

                        }
                        if (answer.equalsIgnoreCase("n")) {
                            askFigureCategory();
                        } else {
                            answer = "";
                        }
                    } while (answer.isEmpty());
                }

            } else {
                throw new Exception("No Figure Categories available.");
            }
        }
        return selectedCategory;
    }

    private SystemUser askShowDesigner() throws Exception {

        SystemUser selectedUser = null;
        while (selectedUser == null) {
            final Iterable<SystemUser> allUsers = PersistenceContext.repositories().users().findByActive(true);
            List<Name> allShowDesigners = new ArrayList<>();
            for (SystemUser user : allUsers) {
                if (user.roleTypes().contains(ShodroneRoles.SHOW_DESIGNER)) {
                    allShowDesigners.add(user.name());
                }
            }

            if (allShowDesigners.iterator().hasNext()) {
                final SelectWidget<Name> selector = new SelectWidget<>("\nSelect a Show Designer:", allShowDesigners);
                selector.show();
                if (selector.selectedElement() == null) {
                    String answer;
                    do {
                        answer = Console.readLine("Quit figure registration? Yes-y No-n");
                        if (answer.equalsIgnoreCase("y")) {
                            return null;
                        }
                        if (answer.equalsIgnoreCase("n")) {
                            askShowDesigner();
                        } else {
                            answer = "";
                        }
                    } while (answer.isEmpty());
                }
                for (SystemUser user : allUsers) {
                    if (user.name().equals(selector.selectedElement())) {
                        selectedUser = user;
                    }
                }

            } else {
                throw new Exception("No Show Designers available.");
            }
        }
        return selectedUser;
    }

    private Customer askCustomer() {
        String publicFigure;
        Customer selectedCustomer = null;
        do {
            publicFigure = Console.readLine("Is the figure public? Yes-y No-n");
            if (publicFigure.equalsIgnoreCase("y")) {
                return null;
            }
            if (publicFigure.equalsIgnoreCase("n")) {
                try {
                    final Iterable<Customer> allCustomers = customerController.allCustomers();

                    if (allCustomers.iterator().hasNext()) {
                        final SelectWidget<Customer> selector = new SelectWidget<>("Select a customer:", allCustomers,
                                new CustomerPrinter());
                        selector.show();
                        selectedCustomer = selector.selectedElement();
                        if (selectedCustomer == null) {
                            String answer;
                            do {
                                answer = Console.readLine("Quit figure registration? Yes-y No-n");
                                if (answer.equalsIgnoreCase("y")) {
                                    throw new Exception("Cancel");
                                }
                                if (answer.equalsIgnoreCase("n")) {
                                    askCustomer();
                                } else {
                                    answer = "";
                                }
                            } while (answer.isEmpty());
                        }
                    } else {
                        throw new Exception("No customers available.");
                    }
                } catch (Exception e) {
                    ConsoleEvent.error(e.getMessage());
                }

                return selectedCustomer;
            } else {
                publicFigure = "";
            }
        } while (publicFigure.isEmpty());
        return selectedCustomer;
    }

    private File askDSL() {
        File file = null;
        String DSLCodeToString = "";

        do {
            System.out.println("Select a DSL Code file");
            FileDialog dialog = new FileDialog((Frame) null, "Select a Description File", FileDialog.LOAD);
            dialog.setFile("*.txt");
            dialog.setVisible(true);
            dialog.dispose();
            file = new File(dialog.getDirectory(), dialog.getFile());

            if (!file.canRead())
                file = null;

            if(!theController.validateDSL(file)){
                System.out.println("\nFile not a valid input");
                file=null;
            }

        } while (file == null);

        return file;
    }

    private Description askDescription() {
        String descriptionString;
        do {
            descriptionString = Console.readLine("Figure Description");
            if (descriptionString.isEmpty()) {
                ConsoleEvent.error("Description cannot be empty.");
                descriptionString = "";
            }
        } while (descriptionString.isEmpty());
        Description description = Description.valueOf(descriptionString);
        return description;
    }

    private FigureName askName() {
        String name;
        do {
            name = Console.readLine("Figure Name");
            if (name.isEmpty()) {
                ConsoleEvent.error("Name cannot be empty.");
            }
            if (theController.isFigureRegistered(FigureName.valueOf(name))) {
                ConsoleEvent.error("Name already in use.");
                name = "";
            }
        } while (name.isEmpty());
        FigureName figureName = FigureName.valueOf(name);
        return figureName;
    }

    @Override
    public String headline() {
        return "Register Figure ";
    }
}
