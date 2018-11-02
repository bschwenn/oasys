package com.oasys.util;

import javax.persistence.Embeddable;
import java.io.Serializable;

@Embeddable
public class RelationshipId implements Serializable {
    private Long idTableOne;
    private Long idTableTwo;

    public RelationshipId(long idTableOne, long idTableTwo) {
        this.idTableOne = idTableOne;
        this.idTableTwo = idTableTwo;
    }
}
