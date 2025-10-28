package shodrone.backoffice.presentation;

import eapli.framework.io.util.Console;
import shodrone.core.presentation.console.ConsoleEvent;
import java.util.LinkedHashMap;

public class ConfirmationUI {


    LinkedHashMap<String, String> data;
    public boolean option = false;

    public ConfirmationUI() {
        data = new LinkedHashMap<String, String>();
    }

    public ConfirmationUI add(String context, String value) {
        data.put(context, value);
        return this;
    }


    public void show(String context) {
        System.out.println("==========================================================");
        ConsoleEvent.blue("You're about to register a new " + context + ". Please confirm the following data:");
        for (String key : data.keySet()) {
            System.out.println(key + ": " + data.get(key));
        }
        System.out.println("==========================================================");
        String answer = "";
        while (!answer.equals("y") && !answer.equals("n")) {
            answer = Console.readLine("Do you want to proceed? (y/n)");
            if (answer.equals("y")) {
                option = true;
                break;
            } else if (answer.equals("n")) {
                option = false;
                break;
            }
        }

    }

}
