package shodrone.dsl_plugin;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ShowDSLBuilder {

    private static final String DSL_VERSION = "DSL version 1.0.0;";
    private static final String DRONE_TYPE_DECLARATION = "DroneType droneTypeA;";

    public static String concatenateDslFiles(List<Path> dslFilePaths) throws IOException {
        StringBuilder commonDeclarations = new StringBuilder();
        StringBuilder beforeBlocks = new StringBuilder();
        StringBuilder mainCommands = new StringBuilder();
        StringBuilder afterBlocks = new StringBuilder();

        Set<String> declaredPositions = new HashSet<>();
        Set<String> declaredAxes = new HashSet<>();
        Set<String> declaredDistances = new HashSet<>();
        Map<String, String> renamedVariables = new HashMap<>(); // originalName -> newName
        int fileCounter = 0;

        // Add initial DSL version and drone type (assumed consistent)
        commonDeclarations.append(DSL_VERSION).append("\n\n");
        commonDeclarations.append(DRONE_TYPE_DECLARATION).append("\n\n");

        for (Path filePath : dslFilePaths) {
            fileCounter++;
            String content = new String(Files.readAllBytes(filePath));

            // Extract declarations (Positions, Velocities, Distances)
            Pattern declarationPattern = Pattern.compile("(Position|Velocity|Distance)\\s+([a-zA-Z0-9_]+)\\s*=\\s*(.*?);");
            Matcher declarationMatcher = declarationPattern.matcher(content);
            while (declarationMatcher.find()) {
                String type = declarationMatcher.group(1);
                String originalName = declarationMatcher.group(2);
                String value = declarationMatcher.group(3);

                if (type.equals("Position") || type.equals("Velocity") || type.equals("Distance")) {
                    // Check for existing identical positions/axes to avoid re-declaration
                    if (type.equals("Position") && (declaredPositions.contains(value) || declaredAxes.contains(value))) {
                        // If it's a known position or axis, we might reuse its existing name
                        // For simplicity in this example, we'll still rename but you could add more sophisticated logic here
                    } else {
                        if (type.equals("Position")) {
                            if (value.matches("\\(0,0,0\\)")) { // Common origin, reuse name if possible
                                if (!declaredPositions.contains("aPos = (0,0,0)")) {
                                    commonDeclarations.append("Position aPos = (0,0,0);\n");
                                    declaredPositions.add("aPos = (0,0,0)");
                                    renamedVariables.put(originalName, "aPos");
                                } else {
                                    renamedVariables.put(originalName, "aPos");
                                }
                                continue;
                            } else if (value.matches("\\(0,0,1\\)")) { // Common Z-axis
                                if (!declaredAxes.contains("zAxis = (0,0,1)")) {
                                    commonDeclarations.append("Position zAxis = (0,0,1);\n");
                                    declaredAxes.add("zAxis = (0,0,1)");
                                    renamedVariables.put(originalName, "zAxis");
                                } else {
                                    renamedVariables.put(originalName, "zAxis");
                                }
                                continue;
                            } else if (value.matches("\\(0,1,0\\)")) { // Common Y-axis
                                if (!declaredAxes.contains("yAxis = (0,1,0)")) {
                                    commonDeclarations.append("Position yAxis = (0,1,0);\n");
                                    declaredAxes.add("yAxis = (0,1,0)");
                                } else {
                                    renamedVariables.put(originalName, "yAxis");
                                }
                                continue;
                            } else if (value.matches("\\(0,10,0\\)")) { // Common anotherPos
                                if (!declaredPositions.contains("anotherPos = (0,10,0)")) {
                                    commonDeclarations.append("Position anotherPos = (0,10,0);\n");
                                    declaredPositions.add("anotherPos = (0,10,0)");
                                } else {
                                    renamedVariables.put(originalName, "anotherPos");
                                }
                                continue;
                            }
                        }
                    }
                    String newName = originalName + "_" + fileCounter;
                    renamedVariables.put(originalName, newName);
                    commonDeclarations.append(type).append(" ").append(newName).append(" = ").append(value).append(";\n");
                    if (type.equals("Distance")) {
                        declaredDistances.add(originalName); // Keep track of original names for common distances
                    }
                }
            }
            commonDeclarations.append("\n");

            // Extract object instantiations (Line, Rectangle, Circle, Circumference)
            Pattern objectInstantiationPattern = Pattern.compile("(Line|Rectangle|Circle|Circumference)\\s+([a-zA-Z0-9_]+)\\s*\\((.*?),\\s*(.*?)(?:,\\s*(.*?))?\\s*\\);");
            Matcher objectInstantiationMatcher = objectInstantiationPattern.matcher(content);
            while (objectInstantiationMatcher.find()) {
                String type = objectInstantiationMatcher.group(1);
                String originalName = objectInstantiationMatcher.group(2);
                String arg1 = objectInstantiationMatcher.group(3);
                String arg2 = objectInstantiationMatcher.group(4);
                String arg3 = objectInstantiationMatcher.group(5); // This will be droneTypeA

                String newName = originalName + "_" + fileCounter;
                renamedVariables.put(originalName, newName);

                // Replace arguments if they were renamed
                String newArg1 = renamedVariables.getOrDefault(arg1.trim(), arg1.trim());
                String newArg2 = renamedVariables.getOrDefault(arg2.trim(), arg2.trim());

                commonDeclarations.append(type).append(" ").append(newName).append("(").append(newArg1).append(", ").append(newArg2);
                if (arg3 != null) {
                    commonDeclarations.append(", ").append(arg3); // droneTypeA
                }
                commonDeclarations.append(");\n");
            }
            commonDeclarations.append("\n");


            // Extract and append "before" block
            Pattern beforePattern = Pattern.compile("before\\s*(.*?)\\s*endbefore", Pattern.DOTALL);
            Matcher beforeMatcher = beforePattern.matcher(content);
            if (beforeMatcher.find()) {
                String blockContent = beforeMatcher.group(1).trim();
                blockContent = replaceVariablesInBlock(blockContent, renamedVariables, fileCounter);
                beforeBlocks.append("before\n").append(blockContent).append("\nendbefore\n\n");
            }

            // Extract and append main commands (including "group" blocks and "pause")
            // This is a more complex step as it requires identifying commands outside of specific blocks.
            // For simplicity, we'll extract everything between 'endbefore' and 'after' and between 'endgroup' and 'after', etc.
            Pattern mainContentPattern = Pattern.compile("endbefore\\s*(.*?)(?=\\s*after|\\Z)", Pattern.DOTALL);
            Matcher mainContentMatcher = mainContentPattern.matcher(content);
            String rawMainContent = "";
            if (mainContentMatcher.find()) {
                rawMainContent = mainContentMatcher.group(1).trim();
            }

            // Extract "group" blocks first
            Pattern groupPattern = Pattern.compile("group\\s*(.*?)\\s*endgroup", Pattern.DOTALL);
            Matcher groupMatcher = groupPattern.matcher(rawMainContent);
            StringBuffer processedMainContent = new StringBuffer();
            while (groupMatcher.find()) {
                String groupContent = groupMatcher.group(1).trim();
                groupContent = replaceVariablesInBlock(groupContent, renamedVariables, fileCounter);
                groupMatcher.appendReplacement(processedMainContent, "group\n" + groupContent + "\nendgroup");
            }
            groupMatcher.appendTail(processedMainContent);
            String finalMainContent = processedMainContent.toString();

            // Replace remaining variables in the main content
            finalMainContent = replaceVariablesInBlock(finalMainContent, renamedVariables, fileCounter);

            // Handle pause commands
            finalMainContent = finalMainContent.replaceAll("pause\\((\\d+)\\);", "pause($1);");


            mainCommands.append(finalMainContent).append("\n\n");

            // Extract and append "after" block
            Pattern afterPattern = Pattern.compile("after\\s*(.*?)\\s*endafter", Pattern.DOTALL);
            Matcher afterMatcher = afterPattern.matcher(content);
            if (afterMatcher.find()) {
                String blockContent = afterMatcher.group(1).trim();
                blockContent = replaceVariablesInBlock(blockContent, renamedVariables, fileCounter);
                afterBlocks.append("after\n").append(blockContent).append("\nendafter\n\n");
            }
        }

        // Combine all parts
        StringBuilder fullDSL = new StringBuilder();
        fullDSL.append(commonDeclarations.toString());
        fullDSL.append(beforeBlocks.toString());
        fullDSL.append(mainCommands.toString());
        fullDSL.append(afterBlocks.toString());

        return fullDSL.toString();
    }

    private static String replaceVariablesInBlock(String blockContent, Map<String, String> renamedVariables, int fileCounter) {
        String processedBlock = blockContent;
        // This is a simple replacement. For more robust parsing, consider a proper AST.
        for (Map.Entry<String, String> entry : renamedVariables.entrySet()) {
            // Replace full word occurrences to avoid partial matches
            processedBlock = processedBlock.replaceAll("\\b" + Pattern.quote(entry.getKey()) + "\\b", entry.getValue());
        }
        return processedBlock;
    }


    public static void main(String[] args) {
        // Example usage with your provided files
        List<Path> dslFiles = new ArrayList<>();
        dslFiles.add(Paths.get("dsl_test/sample_DSL_figure_1.txt"));
        dslFiles.add(Paths.get("dsl_test/sample_DSL_figure_2.txt"));
        dslFiles.add(Paths.get("dsl_test/sample_DSL_figure_3.txt"));

        try {
            String combinedDSL = concatenateDslFiles(dslFiles);
            System.out.println(combinedDSL);
            // Optionally, write the combined DSL to a new file
            Files.createDirectories(Paths.get("dsl_test/generated/"));
            Files.write(Paths.get("dsl_test/generated/Show_DSL_Combined.txt"), combinedDSL.getBytes());
            System.out.println("\nCombined DSL saved to Show_DSL_Combined.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}