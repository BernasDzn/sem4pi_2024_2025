package shodrone.core.presentation.console;

public class ConsoleEvent {

    public static void error(String message) {
        System.out.println(Colorize.toRed("<ERROR> ") + message + "\n");
    }

    public static void success(String message) {
        System.out.println(Colorize.toGreen("<SUCCESS> ") + message + "\n");
    }

    public static void info(String message) {
        System.out.println(Colorize.toYellow("<INFO> ") + message + "\n");
    }

    public static void blue(String message) {
        System.out.println(Colorize.toBlue(message) + "\n");
    }
}