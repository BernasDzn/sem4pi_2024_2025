package shodrone.backoffice.presentation.settings;

import eapli.framework.presentation.console.AbstractUI;
import shodrone.core.infrastructure.Application;

import static eapli.framework.io.util.Console.readInteger;

public class ChangeMenuLayoutUI extends AbstractUI {

    @Override
    protected boolean doShow() {
        System.out.println("Change Menu Layout");
        System.out.println("1. Horizontal");
        System.out.println("2. Vertical");
        System.out.println("0. Exit");
        boolean validInput = false;

        while (!validInput) {
            int option = readInteger("Select an option: ");
            switch (option) {
                case 1:
                    Application.settings().setMenuLayoutHorizontal(true);
                    validInput = true;
                    break;
                case 2:
                    Application.settings().setMenuLayoutHorizontal(false);
                    validInput = true;
                    break;
                case 0:
                    return false;
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
        return true;
    }

    @Override
    public String headline() {
        return "Change Menu Layout";
    }
}
