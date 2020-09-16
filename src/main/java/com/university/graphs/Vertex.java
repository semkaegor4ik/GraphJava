package com.university.graphs;

import lombok.Data;

import java.util.HashMap;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vertex vertex = (Vertex) o;
        return id == vertex.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    public int getId() {
        return id;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "id=" + id +
                ", arcs=" + arcs +
                '}';
    }
}
