package com.university.graphs;

import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        Graph graph = new Graph("src/main/resources/input.txt");
        Graph graph1 = new Graph(graph);
        graph.showVertexDegrees();
        graph.writeInFile("src/main/resources/output.txt");
    }
}
