package shodrone.core.domain.figurecategory;

import eapli.framework.general.domain.model.Description;
import eapli.framework.general.domain.model.Designation;
import org.junit.jupiter.api.Test;

import java.util.Calendar;

import static org.junit.jupiter.api.Assertions.*;

public class FigureCategoryTest {

    @Test
    public void ensureFigureCategoryCreation() {
        FigureCategory figureCategory = new FigureCategory(Designation.valueOf("Test Category"),
                Description.valueOf("This is a test category"),
                Calendar.getInstance(),
                Calendar.getInstance()
        );
        assertNotNull(figureCategory);
        assertEquals("Test Category", figureCategory.getName().toString());
        assertEquals("This is a test category", figureCategory.getDescription().toString());
    }

    @Test
    public void ensureFigureCategoryNameCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FigureCategory(null, Description.valueOf("This is a test category"), Calendar.getInstance(), Calendar.getInstance());
        });
    }

    @Test
    public void ensureFigureCategoryDescriptionCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FigureCategory(Designation.valueOf("Test Category"), null, Calendar.getInstance(), Calendar.getInstance());
        });
    }

    @Test
    public void ensureFigureCategoryCreationDateCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FigureCategory(Designation.valueOf("Test Category"), Description.valueOf("This is a test category"), null, Calendar.getInstance());
        });
    }

    @Test
    public void ensureFigureCategoryLastUpdateDateCantBeNull() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FigureCategory(Designation.valueOf("Test Category"), Description.valueOf("This is a test category"), Calendar.getInstance(), null);
        });
    }

    @Test
    public void ensureFigureCategoryNameCantBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FigureCategory(Designation.valueOf(""), Description.valueOf("This is a test category"), Calendar.getInstance(), Calendar.getInstance());
        });
    }

    @Test
    public void ensureFigureCategoryDescriptionCantBeEmpty() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FigureCategory(Designation.valueOf("Test Category"), Description.valueOf(""), Calendar.getInstance(), Calendar.getInstance());
        });
    }

    @Test
    public void ensureFigureCategoryNameCantHaveLeadingSpaces() {
        assertThrows(IllegalArgumentException.class, () -> {
            new FigureCategory(Designation.valueOf(" Test Category"), Description.valueOf("This is a test category"), Calendar.getInstance(), Calendar.getInstance());
        });
    }
}
