package com.university.graphs;

import lombok.Data;

import java.util.HashMap;

@Data
public class Vertex implements Cloneable {
    private final int id;
    private final HashMap<Integer,Integer> arcs;

    public Vertex(int id) {
        this.id = id;
        arcs = new HashMap<>();
    }

    public void addArc(Integer id, Integer weight){
        if(arcs.containsKey(id)){
            throw new IllegalArgumentException("Vertex have already been added before");
        }
        else{
            arcs.put(id, weight);
        }
    }

    public void addArcs(HashMap<Integer,Integer> mapOfArcs){
        arcs.putAll(mapOfArcs);
    }

    public void deleteArc(Integer id){
        if(!arcs.containsKey(id)){
            throw new IllegalArgumentException("Arc does not exist");
        }
        else {
            arcs.remove(id);
        }
    }


}
