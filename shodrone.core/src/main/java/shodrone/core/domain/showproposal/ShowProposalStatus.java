package shodrone.core.domain.showproposal;

import java.util.ArrayList;

public enum ShowProposalStatus {
    CREATED("Created"),
    READY_SEND("Ready to be sent"),
    WAITING_APPROVAL("Waiting for Approval"),
    APPROVED("Approved"),
    REJECTED("Rejected");

    private final String statusName;

    ShowProposalStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }

    public static ArrayList<String> getAllStatus() {
        ArrayList<String> statusList = new ArrayList<>();
        for (ShowProposalStatus status : ShowProposalStatus.values()) {
            statusList.add(status.getStatusName());
        }
        return statusList;
    }

    public static ShowProposalStatus getStatusByName(String name) {
        for (ShowProposalStatus status : ShowProposalStatus.values()) {
            if (status.getStatusName().equalsIgnoreCase(name)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No status found with name: " + name);
    }
}
