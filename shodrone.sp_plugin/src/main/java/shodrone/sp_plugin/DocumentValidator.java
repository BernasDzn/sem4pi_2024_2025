package shodrone.sp_plugin;

import java.util.List;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.antlr.v4.runtime.BailErrorStrategy;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.misc.ParseCancellationException;
import org.antlr.v4.runtime.tree.ParseTreeWalker;
import org.antlr.v4.runtime.tree.TerminalNode;

public class DocumentValidator {

    private static final Pattern UNFILLED_PLACEHOLDER_PATTERN = Pattern.compile("\\[[^\\[\\]]+\\]");

    private static class PlaceholderFinder extends SPBaseListener {
        private final Set<String> foundPlaceholders = new HashSet<>();

        @Override
        public void visitTerminal(TerminalNode node) {
            String text = node.getText();

            if (text.startsWith("[") && text.endsWith("]")) {
                foundPlaceholders.add(text);
            }
        }

        public Set<String> getFoundPlaceholders() {
            return foundPlaceholders;
        }
    }

    public static boolean validatePreGeneration(File templateFile, Map<String, String> substitutions)
    {

        if (!templateFile.exists()) {
            System.out.println("Template file not found: " + templateFile.getAbsolutePath());
            return false;
        }

        List<String> errors = new ArrayList<>();

        Set<String> placeholdersInTemplate;
        try {

            SPLexer lexer = new SPLexer(CharStreams.fromPath(templateFile.toPath()));
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SPParser parser = new SPParser(tokens);

            parser.setErrorHandler(new BailErrorStrategy());
            lexer.removeErrorListeners();
            parser.removeErrorListeners();

            SPParser.ParseContext tree = parser.parse();
            PlaceholderFinder finder = new PlaceholderFinder();
            ParseTreeWalker walker = new ParseTreeWalker();
            walker.walk(finder, tree);
            placeholdersInTemplate = finder.getFoundPlaceholders();

        } catch (Exception e) {
            System.out.println("Error parsing template file: " + e.getMessage());
            return false;
        }

        if (!placeholdersInTemplate.isEmpty()) {
            for (String placeholder : placeholdersInTemplate) {
                if (!substitutions.containsKey(placeholder)) {
                    errors.add("Missing substitution for placeholder: " + placeholder);
                }
            }
        } else {
            System.out.println("No placeholders found in the template file.");
            return false;
        }

        if (!errors.isEmpty()) {
            System.out.println("Validation errors found:");
            for (String error : errors) {
                System.out.println(error);
            }
            return false;
        }

        return true;
    }

    public static boolean validateNoUnsubstitutedPlaceholders(String generatedDocument) {
        List<String> errors = new ArrayList<>();
        Matcher matcher = UNFILLED_PLACEHOLDER_PATTERN.matcher(generatedDocument);

        while (matcher.find()) {
            errors.add("Unsubstituted placeholder found in generated document: '" + matcher.group() + "'");
        }

        if (!errors.isEmpty()) {
            System.out.println("Validation errors found in generated document:");
            for (String error : errors) {
                System.out.println(error);
            }
            return false;
        }
        
        return true;
    }
}
