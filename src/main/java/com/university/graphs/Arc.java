package com.university.graphs;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@AllArgsConstructor
@Data
public class Arc {
    private Integer firstId;
    private Integer secondId;
    private Integer weight;
}
