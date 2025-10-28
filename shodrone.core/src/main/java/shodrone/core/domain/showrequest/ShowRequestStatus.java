package shodrone.core.domain.showrequest;

import java.util.ArrayList;

public enum ShowRequestStatus {
    NEW("New"),
    VALID("Valid"),
    IMPOSSIBLE("Impossible"),
    MISSING_FIGURES("Missing Figures"),
    TO_PROPOSAL("To Proposal"),
    IN_PRODUCTION("In Production");

    private final String statusName;

    ShowRequestStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

    public static ArrayList<String> getAllStatus() {
        ArrayList<String> statusList = new ArrayList<>();
        for (ShowRequestStatus status : ShowRequestStatus.values()) {
            statusList.add(status.getStatusName());
        }
        return statusList;
    }

    public static ShowRequestStatus getStatusByName(String name) {
        for (ShowRequestStatus status : ShowRequestStatus.values()) {
            if (status.getStatusName().equalsIgnoreCase(name)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No status found with name: " + name);
    }
}
