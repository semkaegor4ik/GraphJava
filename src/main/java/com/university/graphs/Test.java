package com.university.graphs;

import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        Graph graph = new Graph("src/main/resources/input.txt");
        Graph graph1 = new Graph(graph);
        System.out.println(graph.isIsomorphic(graph1));
        graph.deleteVertex(2);
        System.out.println(graph.isIsomorphic(graph1));
        graph.writeInFile("src/main/resources/output.txt");
    }
}
