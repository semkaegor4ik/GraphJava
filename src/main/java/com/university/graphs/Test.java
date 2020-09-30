package com.university.graphs;

import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        Graph graph = new Graph("src/main/resources/input.txt");
        System.out.println(graph.numberOfConnectivityComponents());
        System.out.println(graph.numberOfStronglyBondedComponents());
        graph.writeInFile("src/main/resources/output.txt");
    }
}
