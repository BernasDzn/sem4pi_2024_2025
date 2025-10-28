package shodrone.dsl_plugin;

import java.util.ArrayList;
import java.util.List;

public class DSLExtractor extends DSLBaseVisitor<Void> {

    private List<String> droneTypes;
    private String dslVersion;

    public DSLExtractor() {
        this.droneTypes = new ArrayList<>();
        this.dslVersion = "";
    }

    @Override
    public Void visitDeclaration(DSLParser.DeclarationContext ctx) {
        String declaration = ctx.getText();

        if (declaration.startsWith("DroneType")) {

            String droneType = declaration.substring("DroneType".length()).trim();
            droneType = droneType.substring(0, droneType.length() - 1);

            if (!droneType.isEmpty() && !droneTypes.contains(droneType)) {

                List<String> parts = List.of(droneType.split(","));

                for (String part : parts) {
                    String trimmedPart = part.trim();
                    if (!trimmedPart.isEmpty() && !droneTypes.contains(trimmedPart)) {
                        droneTypes.add(trimmedPart);
                    }
                }
            }
        }

        return null;
    }

    @Override
    public Void visitVersion(DSLParser.VersionContext ctx) {
        String version = ctx.getText();
        if (version.startsWith("DSLversion")) {
            dslVersion = version.substring("DSLversion".length()).trim();
            dslVersion = dslVersion.substring(0, dslVersion.length() - 1);
        }
        return null;
    }

    public List<String> getDroneTypes() {
        return droneTypes;
    }

    public String getDSLVersion() {
        return dslVersion;
    }
    
}
