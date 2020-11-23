package com.university.graphs;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class ArcWithFlow {
    private Integer firstId;
    private Integer secondId;
    private Integer weight;
    private Integer flow;
}
