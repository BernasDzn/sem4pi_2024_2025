package shodrone.core.domain.customer;

import java.util.EnumSet;
import java.util.Set;

public enum CustomerStatus {

    INFRINGEMENT,
    DELETED,
    CREATED,
    REGULAR,
    VIP;

    public boolean isActive() {
        Set<CustomerStatus> notActiveStatuses =
                EnumSet.of(INFRINGEMENT, DELETED);

        return !notActiveStatuses.contains(this);
    }

    public boolean isVip() {
        Set<CustomerStatus> vipStatuses =
                EnumSet.of(VIP);

        return vipStatuses.contains(this);
    }

}
