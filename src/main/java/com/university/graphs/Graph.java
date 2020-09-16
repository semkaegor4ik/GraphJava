package com.university.graphs;

import lombok.Data;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Data
public final class Graph {
    private final HashMap<Integer,Vertex> vertexHashMap;
    private TypeOfGraph typeOfGraph;

    public Graph() {
        vertexHashMap = new HashMap<>();
    }

    public Graph(String filePath){
        vertexHashMap = new HashMap<>();
        initGraph(filePath);
    }

    public Graph(Graph graph){
        vertexHashMap = new HashMap<>();
        typeOfGraph = graph.getTypeOfGraph();
        vertexHashMap.putAll(graph.getVertexHashMap());
    }

    public Graph(TypeOfGraph typeOfGraph) {
        this.typeOfGraph = typeOfGraph;
        vertexHashMap = new HashMap<>();
    }

    public void addVertex(int id){
        if(typeOfGraph == null){
            throw new NullPointerException("type of graph didn't be init");
        }
        vertexHashMap.forEach((index, vertex) -> {
            if(vertex.getId() == id){
                throw new IllegalArgumentException();
            }
        });
        vertexHashMap.put(id, new Vertex(id));
    }

    public void addArc(int from, int to){
        if(typeOfGraph.equals(TypeOfGraph.NOTORIENTEERINGWITHOUTWEIGHTS)) {
            vertexHashMap.get(from).addArc(vertexHashMap.get(to), 1);
            vertexHashMap.get(to).addArc(vertexHashMap.get(from), 1);
        }
        else if(typeOfGraph.equals(TypeOfGraph.ORIENTEERINGWITHOUTWEIGHTS)){
            vertexHashMap.get(from).addArc(vertexHashMap.get(to), 1);
        }
        else if(typeOfGraph != null){
            throw new IllegalArgumentException("your graph has a weight, use addArcWithWeight()");
        }
        else{
            throw new NullPointerException("type of graph didn't be init");
        }
    }

    public void addArcWithWeight(int from, int to, int weight){
        if(typeOfGraph.equals(TypeOfGraph.NOTORIENTEERINGWITHWEIGHTS)) {
            vertexHashMap.get(from).addArc(vertexHashMap.get(to), weight);
            vertexHashMap.get(to).addArc(vertexHashMap.get(from), weight);
        }
        else if(typeOfGraph.equals(TypeOfGraph.ORIENTEERINGWITHWEIGHTS)){
            vertexHashMap.get(from).addArc(vertexHashMap.get(to), weight);
        }
        else if(typeOfGraph != null){
            throw new IllegalArgumentException("your graph hasn't a weight, use addArc()");
        }
        else{
            throw new NullPointerException("type of graph didn't be init");
        }
    }

    public void deleteArc(int from, int to){
        if(typeOfGraph.equals(TypeOfGraph.NOTORIENTEERINGWITHWEIGHTS) ||
                typeOfGraph.equals(TypeOfGraph.NOTORIENTEERINGWITHOUTWEIGHTS)) {
            vertexHashMap.get(from).deleteArc(vertexHashMap.get(to));
            vertexHashMap.get(to).deleteArc(vertexHashMap.get(from));
        }
        else if(typeOfGraph.equals(TypeOfGraph.ORIENTEERINGWITHWEIGHTS)||
                typeOfGraph.equals(TypeOfGraph.ORIENTEERINGWITHOUTWEIGHTS)){
            vertexHashMap.get(from).deleteArc(vertexHashMap.get(to));
        }
        else{
            throw new NullPointerException("type of graph didn't be init");
        }
    }

    public void deleteVertex(int vertId){
        if(typeOfGraph == null){
            throw new NullPointerException("type of graph didn't be init");
        }
        vertexHashMap.forEach((index, vertex) -> {
            vertex.deleteArc(vertexHashMap.get(vertId));
        });
        vertexHashMap.remove(vertexHashMap.get(vertId));
    }

    public void show(){

    }

    private void initGraph(String fileName){
        try(BufferedReader fin=new BufferedReader(new FileReader(fileName)))
        {
            List<String> lines = fin.lines().collect(Collectors.toUnmodifiableList());

            lines.forEach(line->{
                if(line.contains(">")){
                    if(line.contains("(")){
                        typeOfGraph = TypeOfGraph.ORIENTEERINGWITHWEIGHTS;

                        initWithWeight(line);
                    }
                    else{
                        typeOfGraph = TypeOfGraph.ORIENTEERINGWITHOUTWEIGHTS;

                        initWithOutWeight(line);
                    }
                }
                else{
                    if(line.contains("(")){
                        typeOfGraph = TypeOfGraph.NOTORIENTEERINGWITHWEIGHTS;

                        initWithWeight(line);
                    }
                    else{
                        typeOfGraph = TypeOfGraph.NOTORIENTEERINGWITHOUTWEIGHTS;

                        initWithOutWeight(line);
                    }
                }
            });
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void initWithWeight(String line){
        String strFirstId = "";
        String strSecondId = "";
        String strWeight = "";

        for(int i = 0; i < line.length(); i++){
            while(Character.isDigit(line.charAt(i))){
                strFirstId += line.charAt(i);
                i++;
            }
            i++;
            while(Character.isDigit(line.charAt(i))){
                strSecondId += line.charAt(i);                  //ОЧЕНЬ ПЛОХО, НО КОНКАТЕНАЦИЯ НЕ РАБОТАЕТ(НЕ ЗНАЮ ПОЧЕМУ)
                i++;
            }
            i++;
            while(Character.isDigit(line.charAt(i))){
                strWeight += line.charAt(i);                  //ОЧЕНЬ ПЛОХО, НО КОНКАТЕНАЦИЯ НЕ РАБОТАЕТ(НЕ ЗНАЮ ПОЧЕМУ)
                i++;
            }
        }

        int firstId = Integer.parseInt(strFirstId);
        int secondId = Integer.parseInt(strSecondId);
        int weight = Integer.parseInt(strWeight);

        AtomicBoolean first = new AtomicBoolean(false);
        AtomicBoolean second = new AtomicBoolean(false);
        vertexHashMap.forEach((index, vertex) ->{
            if(vertex.getId() == firstId){
                first.set(true);
            }
            else if(vertex.getId() == secondId){
                second.set(true);
            }
        });
        if(!first.get()) {
            addVertex(firstId);
        }
        if(!second.get()) {
            addVertex(secondId);
        }
        addArcWithWeight(firstId,secondId,weight);
    }

    private void initWithOutWeight(String line){
        String strFirstId = "";
        String strSecondId = "";

        outerLoop:
        for(int i = 0; i < line.length();){
            while(Character.isDigit(line.charAt(i))){
                strFirstId += line.charAt(i);                  //ОЧЕНЬ ПЛОХО, НО КОНКАТЕНАЦИЯ НЕ РАБОТАЕТ(НЕ ЗНАЮ ПОЧЕМУ)
                i++;
            }
            i++;
            while(Character.isDigit(line.charAt(i))){
                strSecondId += line.charAt(i);                  //ОЧЕНЬ ПЛОХО, НО КОНКАТЕНАЦИЯ НЕ РАБОТАЕТ(НЕ ЗНАЮ ПОЧЕМУ)
                if(i == line.length() - 1){
                    break outerLoop;
                }
                i++;
            }
        }

        int firstId = Integer.parseInt(strFirstId);
        int secondId = Integer.parseInt(strSecondId);


        AtomicBoolean first = new AtomicBoolean(false);
        AtomicBoolean second = new AtomicBoolean(false);
        vertexHashMap.forEach((index, vertex) ->{
            if(vertex.getId() == firstId){
                first.set(true);
            }
            else if(vertex.getId() == secondId){
                second.set(true);
            }
        });
        if(!first.get()) {
            addVertex(firstId);
        }
        if(!second.get()) {
            addVertex(secondId);
        }
        addArc(firstId, secondId);
    }

}

