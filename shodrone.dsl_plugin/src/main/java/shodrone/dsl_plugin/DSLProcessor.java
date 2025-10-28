package shodrone.dsl_plugin;

import java.io.File;
import java.io.IOException;
import java.util.List;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.tree.ParseTree;

import java.awt.*;

public class DSLProcessor {

    public static boolean validateSyntax(File dslFile) {
        try {
            DSLLexer lexer = new DSLLexer(CharStreams.fromPath(dslFile.toPath()));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            DSLParser parser = new DSLParser(tokens);
            DSLBaseVisitor visitor = new DSLBaseVisitor();
            visitor.visit(parser.code());
            return true;
        } catch (IOException e) {
            System.out.println("Error reading DSL file: " + e.getMessage());
            return false;
        } catch (RecognitionException e) {
            System.out.println("Error while parsing the DSL file! "+e.getMessage());
            e.printStackTrace();
            return false;
        }

    }

    public static List<String> extractDroneTypes(File dslFile) {
        DSLExtractor extractor = readWithExtractor(dslFile);
        return extractor.getDroneTypes();
    }

    public static String extractDSLVersion(File dslFile) {
        DSLExtractor versionExtractor = readWithExtractor(dslFile);
        return versionExtractor.getDSLVersion();
    }

    private static DSLExtractor readWithExtractor(File dslFile) {
        try {
            DSLLexer lexer = new DSLLexer(CharStreams.fromPath(dslFile.toPath()));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            DSLParser parser = new DSLParser(tokens);
            ParseTree tree = parser.code();

            DSLExtractor extractor = new DSLExtractor();
            extractor.visit(tree);
            return extractor;
        } catch (IOException e) {
            System.out.println("Error reading DSL file: " + e.getMessage());
            return null;
        }
    }

    // For testing purposes
    public static void main(String[] args) {

        try {
            File dslFile = selectFile();
            DSLProcessor processor = new DSLProcessor();
            if (!processor.validateSyntax(dslFile)) {
                System.out.println("Invalid DSL syntax in file: " + dslFile.getName());
                return;
            }
            List<String> droneTypes = processor.extractDroneTypes(dslFile);
            String dslVersion = processor.extractDSLVersion(dslFile);

            System.out.println("DSL Version: " + dslVersion);
            System.out.println("Drone Types: " + droneTypes);

            System.out.println("--- DSL Processing Completed ---");
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }

    }

    private static File selectFile() throws IOException {
        FileDialog chooser = new FileDialog((Frame) null, "Select a File", FileDialog.LOAD);
        chooser.setFile("*.txt");
        chooser.setVisible(true);

        if (chooser.getFile() == null || !chooser.getFile().endsWith(".txt")) {
            throw new IOException("Invalid File!");
        }

        File file = new File(chooser.getDirectory(), chooser.getFile());
        chooser.dispose();

        return file;
    }
}