package com.university.graphs;

import java.util.HashMap;

public class Test {
    public static void main(String[] args) {
        Graph graph = new Graph("src/main/resources/input.txt");
        graph.getVertexHashMap().forEach((index, vertex)-> System.out.println(vertex));
    }
}
