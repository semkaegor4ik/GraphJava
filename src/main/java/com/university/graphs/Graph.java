package com.university.graphs;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Data
public final class Graph {
    @Getter(value = AccessLevel.PRIVATE)
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
        typeOfGraph = graph.getTypeOfGraph();
        vertexHashMap = cloneGraph(graph.getVertexHashMap());
    }

    public Graph(TypeOfGraph typeOfGraph) {
        this.typeOfGraph = typeOfGraph;
        vertexHashMap = new HashMap<>();
    }

    public void show(){
        vertexHashMap.forEach((id, vertex) -> System.out.println(vertex));
    }

    private HashMap<Integer,Vertex> cloneGraph(HashMap<Integer,Vertex> otherGraph){
        HashMap<Integer,Vertex> vertexMap = new HashMap<>();

        otherGraph.forEach((id,vertex)->{
            Vertex vertex1 = new Vertex(id);
            vertex.getArcs().forEach((index, weight)->{
                vertex1.addArc(index, weight);
            });
            vertexMap.put(id, vertex1);
        });
        return vertexMap;
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
        if(TypeOfGraph.NOTORIENTEERINGWITHOUTWEIGHTS.equals(typeOfGraph)) {
            vertexHashMap.get(from).addArc(to, 1);
            vertexHashMap.get(to).addArc(from, 1);
        }
        else if(TypeOfGraph.ORIENTEERINGWITHOUTWEIGHTS.equals(typeOfGraph)){
            vertexHashMap.get(from).addArc(to, 1);
        }
        else if(typeOfGraph != null){
            throw new IllegalArgumentException("your graph has a weight, use addArcWithWeight()");
        }
        else{
            throw new NullPointerException("type of graph didn't be init");
        }
    }

    public void addArcWithWeight(int from, int to, int weight){
        if(TypeOfGraph.NOTORIENTEERINGWITHWEIGHTS.equals(typeOfGraph)) {
            vertexHashMap.get(from).addArc(to, weight);
            vertexHashMap.get(to).addArc(from, weight);
        }
        else if(TypeOfGraph.ORIENTEERINGWITHWEIGHTS.equals(typeOfGraph)){
            vertexHashMap.get(from).addArc(to, weight);
        }
        else if(typeOfGraph != null){
            throw new IllegalArgumentException("your graph hasn't a weight, use addArc()");
        }
        else{
            throw new NullPointerException("type of graph didn't be init");
        }
    }

    public void deleteArc(int from, int to){
        if(TypeOfGraph.NOTORIENTEERINGWITHWEIGHTS.equals(typeOfGraph) ||
                TypeOfGraph.NOTORIENTEERINGWITHOUTWEIGHTS.equals(typeOfGraph)) {
            vertexHashMap.get(from).deleteArc(to);
            vertexHashMap.get(to).deleteArc(from);
        }
        else if(TypeOfGraph.ORIENTEERINGWITHWEIGHTS.equals(typeOfGraph)||
                TypeOfGraph.ORIENTEERINGWITHOUTWEIGHTS.equals(typeOfGraph)){
            vertexHashMap.get(from).deleteArc(to);
        }
        else{
            throw new NullPointerException("type of graph didn't be init");
        }
    }

    public void showVertexDegrees(){
        vertexHashMap.forEach((id, vertex)->{
            int vertexDegree = vertex.getArcs().size();
            System.out.println("Vertex ID - " + id + " degree - " + vertexDegree);
        });
    }

    public void showHangingGraphVertices(){
        vertexHashMap.forEach((id, vertex)->{
            if(vertex.getArcs().size() == 0) {
                System.out.println("Vertex ID - " + id);
            }
        });
    }

    public boolean isIsomorphic(Graph otherGraph){
        boolean isomorphism = true;




        return isomorphism;
    }

    public void deleteVertex(int vertId){
        if(typeOfGraph == null){
            throw new NullPointerException("type of graph didn't be init");
        }
        vertexHashMap.forEach((index, vertex) -> {
            if(vertex.getArcs().containsKey(vertId)){
                vertex.getArcs().remove(vertId);
            }
        });
        vertexHashMap.remove(vertId);
    }

    private boolean isHasAVertexFrom(Integer id){
        AtomicBoolean flag = new AtomicBoolean(false);
        vertexHashMap.forEach((index,vertex)->{
            if(vertex.getArcs().containsKey(id)){
                flag.set(true);
            }
        });
        return flag.get();
    }

    public void writeInFile(String fileName){
        try(BufferedWriter fout = new BufferedWriter(new FileWriter(fileName))) {
            if(TypeOfGraph.NOTORIENTEERINGWITHOUTWEIGHTS.equals(typeOfGraph)||
                    TypeOfGraph.NOTORIENTEERINGWITHWEIGHTS.equals(typeOfGraph)){
                vertexHashMap.forEach((idFrom ,vertex)->{
                    if(!isHasAVertexFrom(idFrom) && vertex.getArcs().isEmpty()){
                        try {
                            fout.write(String.valueOf(idFrom));
                            fout.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    vertex.getArcs().forEach((idTo,weight)->{
                        try {
                            fout.write(idFrom + "-" + idTo);
                            if(TypeOfGraph.NOTORIENTEERINGWITHWEIGHTS.equals(typeOfGraph)){
                                fout.write("(" + weight + ")");
                            }
                            fout.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
            }
            else if(TypeOfGraph.ORIENTEERINGWITHOUTWEIGHTS.equals(typeOfGraph)||
                    TypeOfGraph.ORIENTEERINGWITHWEIGHTS.equals(typeOfGraph)){
                vertexHashMap.forEach((idFrom ,vertex)->{
                    if(!isHasAVertexFrom(idFrom) && vertex.getArcs().isEmpty()){
                        try {
                            fout.write(String.valueOf(idFrom));
                            fout.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    vertex.getArcs().forEach((idTo,weight)->{
                        try {
                            fout.write(idFrom + ">" + idTo);
                            if(TypeOfGraph.ORIENTEERINGWITHWEIGHTS.equals(typeOfGraph)){
                                fout.write("(" + weight + ")");
                            }
                            fout.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
            }
            else{
                throw new IllegalArgumentException();
            }
        }
        catch (IOException ex){
            ex.printStackTrace();
        }
    }


    private void initGraph(String fileName){
        try(BufferedReader fin=new BufferedReader(new FileReader(fileName)))
        {
            List<String> lines = fin.lines().collect(Collectors.toUnmodifiableList());

            for (String line:
                 lines) {
                if(line.contains(">")){
                    if(line.contains("(")){
                        typeOfGraph = TypeOfGraph.ORIENTEERINGWITHWEIGHTS;
                        break;
                    }
                    else{
                        typeOfGraph = TypeOfGraph.ORIENTEERINGWITHOUTWEIGHTS;
                        break;
                    }
                }
                else{
                    if(line.contains("(")){
                        typeOfGraph = TypeOfGraph.NOTORIENTEERINGWITHWEIGHTS;
                        break;
                    }
                    else{
                        typeOfGraph = TypeOfGraph.NOTORIENTEERINGWITHOUTWEIGHTS;
                    }
                }
            }

            if(TypeOfGraph.NOTORIENTEERINGWITHOUTWEIGHTS.equals(typeOfGraph)||
                    TypeOfGraph.ORIENTEERINGWITHOUTWEIGHTS.equals(typeOfGraph)) {
                lines.forEach(line->initWithOutWeight(line));
            }
            else{
                lines.forEach(line->initWithWeight(line));
            }
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }

    private void initWithWeight(String line){
        String strFirstId = "";
        String strSecondId = "";
        String strWeight = "";
        boolean twoVertex = true;

        outerLoop:
        for(int i = 0; i < line.length(); i++){
            while(Character.isDigit(line.charAt(i))){
                strFirstId += line.charAt(i);
                if(i == line.length() - 1){
                    twoVertex = false;
                    break outerLoop;
                }
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
        if(twoVertex){
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
        else{
            addVertex(firstId);
        }
    }

    private void initWithOutWeight(String line){
        String strFirstId = "";
        String strSecondId = "";
        boolean twoVertex = true;

        outerLoop:
        for(int i = 0; i < line.length();){
            while(Character.isDigit(line.charAt(i))){
                strFirstId += line.charAt(i);                  //ОЧЕНЬ ПЛОХО, НО КОНКАТЕНАЦИЯ НЕ РАБОТАЕТ(НЕ ЗНАЮ ПОЧЕМУ)
                if(i == line.length() - 1){
                    twoVertex = false;
                    break outerLoop;
                }
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
        if(twoVertex){
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
        else{
            addVertex(firstId);
        }

    }

}

