package shodrone.sp_plugin;

import org.antlr.v4.runtime.tree.TerminalNode;

import java.util.Map;

public class PlaceholderSubstituter extends SPBaseListener {

    private final StringBuilder outputDocument = new StringBuilder();
    private final Map<String, String> substitutionMap;

    // Internal state to suppress placeholder output inside lists
    private boolean insideDroneList = false;
    private boolean insideFigureList = false;

    public PlaceholderSubstituter(Map<String, String> substitutions) {
        this.substitutionMap = substitutions;
    }

    @Override
    public void enterDrone_list(SPParser.Drone_listContext ctx) {
        insideDroneList = true;

        String[] models = getSplit("[model]");
        String[] quantities = getSplit("[quantity]");

        if (models.length != quantities.length) {
            throw new RuntimeException("Mismatched model and quantity list lengths");
        }

        outputDocument.append(ctx.DRONELIST_HEADER().getText())
                .append("\n");

        for (int i = 0; i < models.length; i++) {
            outputDocument.append(models[i].trim())
                    .append(" – ")
                    .append(quantities[i].trim())
                    .append(" ")
                    .append(ctx.UNITS_LABEL(0).getText().trim())
                    .append("\n");
        }
        outputDocument.append("\n");
    }

    @Override
    public void exitDrone_list(SPParser.Drone_listContext ctx) {
        insideDroneList = false;
    }

    @Override
    public void enterFigure_list(SPParser.Figure_listContext ctx) {
        insideFigureList = true;

        String[] positions = getSplit("[position in show]");
        String[] figureNames = getSplit("[figure name]");

        if (positions.length != figureNames.length) {
            throw new RuntimeException("Mismatched figure positions and names list lengths");
        }

        outputDocument.append(ctx.FIGURELIST_HEADER().getText())
                .append("\n");

        for (int i = 0; i < positions.length; i++) {
            outputDocument.append(positions[i].trim())
                    .append(" – ")
                    .append(figureNames[i].trim())
                    .append("\n");
        }
    }

    @Override
    public void exitFigure_list(SPParser.Figure_listContext ctx) {
        insideFigureList = false;
    }

    @Override
    public void visitTerminal(TerminalNode node) {
        String tokenText = node.getText();

        if (insideDroneList || insideFigureList)
            return;

        String substituted = substitutionMap.getOrDefault(tokenText, tokenText);
        outputDocument.append(substituted);
    }

    private String[] getSplit(String key) {
        String value = substitutionMap.get(key);
        return value.split(";");
    }

    public String getSubstitutedDocument() {
        return outputDocument.toString();
    }
}
