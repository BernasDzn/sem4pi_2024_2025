package shodrone.core.domain.figure;

import java.util.EnumSet;
import java.util.Set;

public enum FigureStatus {

    COMISSIONED,
    DECOMISSIONED,
    PRIVATE;

    public boolean isComissioned() {
        Set<FigureStatus> notComissionedStatuses =
                EnumSet.of(DECOMISSIONED, PRIVATE);

        return !notComissionedStatuses.contains(this);
    }

}
