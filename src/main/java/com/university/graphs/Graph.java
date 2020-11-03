package com.university.graphs;

import lombok.AccessLevel;
import lombok.Data;
import lombok.Getter;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
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
        if(TypeOfGraph.NOT_ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)) {
            vertexHashMap.get(from).addArc(to, 1);
            vertexHashMap.get(to).addArc(from, 1);
        }
        else if(TypeOfGraph.ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)){
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
        if(TypeOfGraph.NOT_ORIENTEERING_WITH_WEIGHTS.equals(typeOfGraph)) {
            vertexHashMap.get(from).addArc(to, weight);
            vertexHashMap.get(to).addArc(from, weight);
        }
        else if(TypeOfGraph.ORIENTEERING_WITH_WEIGHTS.equals(typeOfGraph)){
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
        if(TypeOfGraph.NOT_ORIENTEERING_WITH_WEIGHTS.equals(typeOfGraph) ||
                TypeOfGraph.NOT_ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)) {
            vertexHashMap.get(from).deleteArc(to);
            vertexHashMap.get(to).deleteArc(from);
        }
        else if(TypeOfGraph.ORIENTEERING_WITH_WEIGHTS.equals(typeOfGraph)||
                TypeOfGraph.ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)){
            vertexHashMap.get(from).deleteArc(to);
        }
        else{
            throw new NullPointerException("type of graph didn't be init");
        }
    }

    public void showVertexesDegrees(){
        if(TypeOfGraph.NOT_ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)||
                TypeOfGraph.NOT_ORIENTEERING_WITH_WEIGHTS.equals(typeOfGraph)) {
            vertexHashMap.forEach((id, vertex)->{
                int vertexDegree = vertex.getArcs().size();
                System.out.println("Vertex ID - " + id + " degree - " + vertexDegree);
            });
        }
        else if(TypeOfGraph.ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)||
                TypeOfGraph.ORIENTEERING_WITH_WEIGHTS.equals(typeOfGraph)) {
            vertexHashMap.forEach((id, vertex)->{
                int vertexDegree = vertex.getArcs().size();
                System.out.println("Vertex ID - " + id + " outcome " + vertexDegree);
                AtomicInteger inComeCount = new AtomicInteger();
                vertexHashMap.forEach((id1, vertex1)->{
                    if(!id.equals(id1) &&
                            vertex1.getArcs().containsKey(id)){
                        inComeCount.getAndIncrement();
                    }
                });
                System.out.println("Vertex ID - " + id + " income " + inComeCount);
                System.out.println();
            });
        }
        else{
            throw new NullPointerException("type of graph didn't be init");
        }
    }

    public void showHangingGraphVertices(){
        if(TypeOfGraph.NOT_ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)||
                TypeOfGraph.NOT_ORIENTEERING_WITH_WEIGHTS.equals(typeOfGraph)) {
            vertexHashMap.forEach((id, vertex) -> {
                if (vertex.getArcs().size() == 1) {
                    System.out.println("Vertex ID - " + id);
                }
            });
        }
        else if(TypeOfGraph.ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)||
                TypeOfGraph.ORIENTEERING_WITH_WEIGHTS.equals(typeOfGraph)) {
            vertexHashMap.forEach((id, vertex)->{
                AtomicInteger vertexDegree = new AtomicInteger(vertex.getArcs().size());
                vertexHashMap.forEach((id1, vertex1)->{
                    if(!id.equals(id1) &&
                            vertex1.getArcs().containsKey(id)){
                        vertexDegree.getAndIncrement();
                    }
                });
                if(vertexDegree.get() == 1) {
                    System.out.println("Vertex ID - " + id);
                    System.out.println();
                }
            });
        }
        else{
            throw new NullPointerException("type of graph didn't be init");
        }
    }

    public Map<Integer, Integer> dijkstra(int from){
        Map<Integer, Integer> dijkstraResult = new HashMap<>();
        vertexHashMap.forEach((id,vertex)->{
            if(id!=from)
                dijkstraResult.put(id,Integer.MAX_VALUE);
            else
                dijkstraResult.put(id,0);
        });

        Map<Integer, Boolean> used = new HashMap<>();
        vertexHashMap.forEach((id,vertex)->used.put(id, false));

        AtomicInteger v = new AtomicInteger();

        for (int i = 0; i < vertexHashMap.size(); i++) {
            v.set(-1);
            vertexHashMap.forEach((id, vertex)->{
                if(!used.get(id)
                        && (v.get() == -1 || dijkstraResult.get(id) < dijkstraResult.get(v.get())))
                    v.set(id);
            });

            if(dijkstraResult.get(v.get()) == Integer.MAX_VALUE)
                break;
            used.put(v.get(),true);

            vertexHashMap.get(v.get()).getArcs().forEach((id,weight)->{
                if(dijkstraResult.get(id) > dijkstraResult.get(v.get()) + weight){
                    dijkstraResult.put(id, dijkstraResult.get(v.get()) + weight);
                }
            });
        }
        return dijkstraResult;
    }

    public boolean hasAWay(int from, int to, List<Integer> vertexesList){
        vertexesList.remove(from);
        AtomicBoolean hasAWay = new AtomicBoolean(false);
        vertexHashMap.get(from).getArcs().forEach((id, weight)->{
            if(id == to){
                hasAWay.set(true);
            }
            else if(vertexesList.contains(id)){
                hasAWay.set(hasAWay(id, to, vertexesList));
            }
        });
        return hasAWay.get();
    }

    public boolean isIsomorphic(Graph otherGraph){
        AtomicBoolean isomorphism = new AtomicBoolean(true);
        otherGraph.vertexHashMap.forEach((id, vertex)->{
            if(!vertexHashMap.containsKey(id))
                isomorphism.set(false);
        });
        vertexHashMap.forEach((id, vertex)->{
            if(!otherGraph.vertexHashMap.containsKey(id))
                isomorphism.set(false);
        });

        otherGraph.vertexHashMap.forEach((otherId, otherVertex)->{
            vertexHashMap.forEach((id, vertex)->{
                if(!vertex.isIsomorphic(otherVertex)&&
                        (otherId == id)){
                    isomorphism.set(false);
                }
            });
        });
        return isomorphism.get();
    }

    public List<Arc> StronglyBondedComponents(){
        if(TypeOfGraph.ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)
                || TypeOfGraph.ORIENTEERING_WITH_WEIGHTS.equals(typeOfGraph)) {
            List<Arc> arcs = new ArrayList<>();
            vertexHashMap.forEach((id, vertex) -> {
                vertexHashMap.forEach((id1, vertex1) -> {
                    if (vertex.getArcs().containsKey(id1) &&
                            vertex1.getArcs().containsKey(id) &&
                            !arcs.contains(new Arc(id1, id))) {
                        Arc newArc = new Arc(id, id1);
                        arcs.add(newArc);
                    }
                });
            });
            return arcs;
        }
        else if(TypeOfGraph.NOT_ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)
                || TypeOfGraph.NOT_ORIENTEERING_WITH_WEIGHTS.equals(typeOfGraph)){
            throw new IllegalArgumentException("graph needs to be orienteering");
        }
        else{
            throw new NullPointerException("type of graph didn't be init");
        }
    }

    public List<Integer> inComeList(Integer id){
        List<Integer> inComeList = new ArrayList<>();
        vertexHashMap.forEach((index, vertex)->{
            if(vertex.getArcs().containsKey(id)){
                inComeList.add(index);
            }
        });
        return inComeList;
    }

    public int numberOfConnectivityComponents(){
        if(typeOfGraph == null){
            throw new NullPointerException("type of graph didn't be init");
        }

        AtomicInteger count = new AtomicInteger(0);
        List<Integer> vertexes = getVertexesList();

        vertexHashMap.forEach((id, vertex)->{
            if(vertexes.contains(id)) {
                recourseForNumberOfConnectivityComponents(id, vertexes);
                count.getAndIncrement();
            }
        });
        return count.get();
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

    public void writeInFile(String fileName){
        try(BufferedWriter fout = new BufferedWriter(new FileWriter(fileName))) {
            if(TypeOfGraph.NOT_ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)||
                    TypeOfGraph.NOT_ORIENTEERING_WITH_WEIGHTS.equals(typeOfGraph)){
                vertexHashMap.forEach((idFrom ,vertex)->{
                    if(inComeList(idFrom).isEmpty() && vertex.getArcs().isEmpty()){
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
                            if(TypeOfGraph.NOT_ORIENTEERING_WITH_WEIGHTS.equals(typeOfGraph)){
                                fout.write("(" + weight + ")");
                            }
                            fout.newLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                });
            }
            else if(TypeOfGraph.ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)||
                    TypeOfGraph.ORIENTEERING_WITH_WEIGHTS.equals(typeOfGraph)){
                vertexHashMap.forEach((idFrom ,vertex)->{
                    if(inComeList(idFrom).isEmpty() && vertex.getArcs().isEmpty()){
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
                            if(TypeOfGraph.ORIENTEERING_WITH_WEIGHTS.equals(typeOfGraph)){
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

    public List<Integer> getVertexesList(){
        List<Integer> vertexesList = new ArrayList<>();
        vertexHashMap.forEach((id,vertex)->vertexesList.add(id));
        return vertexesList;
    }

    private void recourseForNumberOfConnectivityComponents(Integer id, List<Integer> vertexes){
        if(vertexes.contains(id)){
            List<Integer> vertexesList = getVertexesList();
            vertexes.remove(id);
            vertexHashMap.get(id).getArcs().forEach((index, weight)-> {
                if(hasAWay(index, id, vertexesList)){
                    recourseForNumberOfConnectivityComponents(index, vertexes);
                }
            });
            //inComeList(id).forEach(index-> recourseForNumberOfConnectivityComponents(index, vertexes));
        }
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

    private void initGraph(String fileName){
        try(BufferedReader fin=new BufferedReader(new FileReader(fileName)))
        {
            List<String> lines = fin.lines().collect(Collectors.toUnmodifiableList());

            for (String line:
                 lines) {
                if(line.contains(">")){
                    if(line.contains("(")){
                        typeOfGraph = TypeOfGraph.ORIENTEERING_WITH_WEIGHTS;
                        break;
                    }
                    else{
                        typeOfGraph = TypeOfGraph.ORIENTEERING_WITHOUT_WEIGHTS;
                        break;
                    }
                }
                else{
                    if(line.contains("(")){
                        typeOfGraph = TypeOfGraph.NOT_ORIENTEERING_WITH_WEIGHTS;
                        break;
                    }
                    else{
                        typeOfGraph = TypeOfGraph.NOT_ORIENTEERING_WITHOUT_WEIGHTS;
                    }
                }
            }

            if(TypeOfGraph.NOT_ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)||
                    TypeOfGraph.ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)) {
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