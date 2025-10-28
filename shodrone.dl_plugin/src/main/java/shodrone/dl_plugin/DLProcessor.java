package shodrone.dl_plugin;

import org.antlr.v4.runtime.*;

public class DLProcessor {

    private final String DL_VERSION;

    public DLProcessor(String version) {
        this.DL_VERSION = version.replace(".", "_");
    }

    public static void main(String[] args) {
        DLProcessor processor = new DLProcessor("0.86");
        System.out.println("Valid syntax test:");
        String text = "Position aPosition = (0, 1, 1.5);\n" +
                "Vector aDirection = (0,0,1)-(0,1,0);\n" +
                "\n" +
                "LinearVelocity aVelocity = 6.2;\n" +
                "AngularVelocity rotVelocity = PI/10;\n" +
                "\n" +
                "Position arrayOfPositions = ((0, 0, 0), (0, 0, 20), (0, 20, 20), (0, 30, 20), (-10, 50, 25));";
        if (processor.validateSyntax(text)) {
            System.out.println("Syntax is VALID.");
        } else {
            System.out.println("Syntax is INVALID.");
        }

        System.out.println("\nInvalid syntax test:");
        String errorText = "Postion pos = (1,1,2);\n";
        if (processor.validateSyntax(errorText)) {
            System.out.println("Syntax is VALID.");
        } else {
            System.out.println("Syntax is INVALID.");
        }
    }

    public boolean validateSyntax(String text) {
        ClassLoader classLoader = DLProcessor.class.getClassLoader();
        classLoader.setDefaultAssertionStatus(true);
        try {
            CharStream input = CharStreams.fromString(text);

            Class<?> lexerClass = Class.forName("shodrone.dl_" + DL_VERSION + ".DLLexer");
            Object lexer = lexerClass.getDeclaredConstructor(CharStream.class).newInstance(input);

            Class<?> parserClass = Class.forName("shodrone.dl_" + DL_VERSION + ".DLParser");
            Object parser = parserClass.getDeclaredConstructor(TokenStream.class)
                    .newInstance(new CommonTokenStream((Lexer) lexer));

            parserClass.getMethod("parse").invoke(parser);

            return (int) parserClass.getMethod("getNumberOfSyntaxErrors").invoke(parser) <= 0;

        } catch (ClassNotFoundException e) {
            System.err.println("ANTLR classes not found. Ensure the ANTLR runtime is in the classpath.");
            return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}