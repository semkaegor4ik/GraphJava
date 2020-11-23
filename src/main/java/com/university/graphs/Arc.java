package com.university.graphs;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class Arc {
    private Integer firstId;
    private Integer secondId;
    private Integer weight;

    public boolean equalsArcWithFlow(ArcWithFlow arc){
        return firstId.equals(arc.getFirstId())
                && secondId.equals(arc.getSecondId());
    }
}
