package shodrone.core.presentation.console;

public class Colorize {

	// ANSI escape codes for colors
	public static final String RED = "\u001B[31m";
	public static final String GREEN = "\u001B[32m";
	public static final String YELLOW = "\u001B[33m";
	public static final String BLUE = "\u001B[34m";
	public static final String RESET = "\u001B[0m";

	public static String toRed(String message) {
		return RED + message + RESET;
	}

	public static String toGreen(String message) {
		return GREEN + message + RESET;
	}

	public static String toYellow(String message) {
		return YELLOW + message + RESET;
	}

	public static String toBlue(String message) {
		return BLUE + message + RESET;
	}
	
}
