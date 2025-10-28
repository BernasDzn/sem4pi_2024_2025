package shodrone.core.domain.customer_rep;

import lombok.Getter;

@Getter
public enum RepresentativeStatus  {

    ACTIVE,
    INACTIVE,
    DELETED,
    LEFT_COMPANY;

    public boolean isActive(){
        return this.equals(ACTIVE);
    }

}
