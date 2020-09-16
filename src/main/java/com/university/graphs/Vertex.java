package com.university.graphs;

import lombok.Data;

import java.util.HashMap;

@Data
public class Vertex {
    private final int id;
    private final HashMap<Vertex,Integer> arcs;

    public Vertex(int id) {
        this.id = id;
        arcs = new HashMap<>();
    }

    public void addArc(Vertex vertex, Integer weight){
        if(arcs.containsKey(vertex)){
            throw new IllegalArgumentException("Vertex have already been added before");
        }
        else{
            arcs.put(vertex, weight);
        }
    }

    public void addArcs(HashMap<Vertex,Integer> mapOfArcs){
        arcs.putAll(mapOfArcs);
    }

    public void deleteArc(Vertex vertex){
        if(!arcs.containsKey(vertex)){
            throw new IllegalArgumentException("Arc does not exist");
        }
        else {
            arcs.remove(vertex);
        }
    }

}
