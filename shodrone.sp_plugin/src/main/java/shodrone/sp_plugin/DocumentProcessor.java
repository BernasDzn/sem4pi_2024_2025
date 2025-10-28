package shodrone.sp_plugin;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTreeWalker;

import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

public class DocumentProcessor {
    
    public String processTemplate(File templateFile, Map<String, String> substitutions)
            {
        
        if (!DocumentValidator.validatePreGeneration(templateFile, substitutions)) 
            return null;

        String finalDocument;

        try {
            SPLexer lexer = new SPLexer(CharStreams.fromPath(templateFile.toPath()));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SPParser parser = new SPParser(tokens);

            SPParser.ParseContext tree = parser.parse();

            ParseTreeWalker walker = new ParseTreeWalker();
            PlaceholderSubstituter substituter = new PlaceholderSubstituter(substitutions);

            walker.walk(substituter, tree);

            finalDocument = substituter.getSubstitutedDocument();

            System.out.println("--- Document Generated ---");
            System.out.println(finalDocument.substring(0, Math.min(finalDocument.length(), 500)) + "...");

        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
            return null;
        }

        if (!DocumentValidator.validateNoUnsubstitutedPlaceholders(finalDocument))
            return null;

        return finalDocument;
    }


    //For testing purposes only
    public static void main(String[] args) throws Exception {
        File inputFile = selectFile();
        String fileName = inputFile.getPath().replace(inputFile.getName(), "new_" + inputFile.getName());
        File outputFile = new File(fileName);

        if (!DocumentValidator.validatePreGeneration(inputFile, getSubstitutions())) {
            System.err.println("Template validation failed. Please check the template and substitutions.");
            return;
        }

        Map<String, String> substitutions = getSubstitutions();
        DocumentProcessor processor = new DocumentProcessor();
        String finalDocument = processor.processTemplate(inputFile, substitutions);
        if (finalDocument == null) {
            System.err.println("Document generation failed. Please check the input file and substitutions.");
            return;
        }

        if (!DocumentValidator.validateNoUnsubstitutedPlaceholders(finalDocument)) {
            System.err.println("The document contains unfilled placeholders. Please check the substitutions.");
            return;
        }

        try (FileWriter writer = new FileWriter(outputFile, StandardCharsets.UTF_8)) {
            writer.write(finalDocument);
            System.out.println("\nSuccessfully wrote substituted document to: " + outputFile.getAbsolutePath());
        } catch (IOException e) {
            System.err.println("Error writing output file: " + e.getMessage());
        }
    }

    private static Map<String, String> getSubstitutions() {
        Map<String, String> substitutions = new HashMap<>();
        substitutions.put("[Customer Representative Name]", "Alice Smith");
        substitutions.put("[Company name]", "DroneOps Inc.");
        substitutions.put("[Company Name]", "DroneOps Inc.");
        substitutions.put("[Address with postal code and country]",
                "123 Drone Lane, 4400-001 Vila Nova de Gaia, Portugal");
        substitutions.put("[VAT Number]", "PT123456789");
        substitutions.put("[proposal number]", "2025-DRONE-007");
        substitutions.put("[show proposal number]", "2025-DRONE-007");
        substitutions.put("[date]", "June 7, 2025"); // Current date
        substitutions.put("[date of the event]", "July 15, 2025");
        substitutions.put("[time of the event]", "14:00 WEST");
        substitutions.put("[GPS coordinates of the location]", "41.1399° N, 8.6186° W");
        substitutions.put("[duration]", "60");
        substitutions.put("[model]", "DroneX;DroneY;DroneZ");
        substitutions.put("[quantity]", "5;10;7");
        substitutions.put("[position in show]", "1;2;3");
        substitutions.put("[figure name]", "Spiral Ascend;Spiral Descend;Looping Spiral");
        substitutions.put("[page break]", "\f"); // ASCII form feed for page break
        substitutions.put("[CRM Manager Name]", "Bob Johnson");
        substitutions.put("[link to show video]", "https://example.com/sim-video-7");
        substitutions.put("[link to show's simulation video]", "https://example.com/sim-video-7");
        substitutions.put("[insurance amount]", "1,000,000 EUR");
        substitutions.put("[valor do seguro]", "1,000,000 EUR");
        substitutions.put("<EOF>", "");
        return substitutions;
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