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

    private UIController controller;

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

    public Graph(UIController controller) {
        vertexHashMap = new HashMap<>();
        this.controller = controller;
    }

    public void show(){
        vertexHashMap.forEach((id, vertex) -> System.out.println(vertex));
    }

    public void addVertex(Integer id){
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

    public void addArc(Integer from, Integer to){
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

    public void addArcWithWeight(Integer from, Integer to, Integer weight){
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

    public void deleteArc(Integer from, Integer to){
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

        if(TypeOfGraph.NOT_ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)
                || TypeOfGraph.ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph))
            throw new IllegalArgumentException("your graph need to have weights");
        else if(typeOfGraph == null)
            throw new NullPointerException("type of graph didn't be init");

        vertexHashMap.forEach((id,vertex)->vertex.getArcs().forEach((index, weight)->{
            if(weight < 0)
                throw new IllegalArgumentException("your graph need to have only positive weights");
        }));

        Map<Integer, Integer> dijkstraResult = new HashMap<>();
        vertexHashMap.forEach((id,vertex)->{
            if(id!=from)
                dijkstraResult.put(id, Integer.MAX_VALUE);
            else
                dijkstraResult.put(id, 0);
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

    public boolean hasAWay(Integer from, Integer to, List<Integer> vertexesList){
        vertexesList.remove(from);
        for (Integer id:
                vertexHashMap.get(from).getArcs().keySet()) {
            if(id.equals(to)){
                return true;
            }
            else if(vertexesList.contains(id)){
                hasAWay(id, to, vertexesList);
            }
        }
        return false;
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

        otherGraph.vertexHashMap.forEach((otherId, otherVertex)-> vertexHashMap.forEach((id, vertex)->{
            if(!vertex.isIsomorphic(otherVertex)&&
                    (otherId.equals(id))){
                isomorphism.set(false);
            }
        }));
        return isomorphism.get();
    }

    public List<Arc> StronglyBondedComponents(){
        if(TypeOfGraph.ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)
                || TypeOfGraph.ORIENTEERING_WITH_WEIGHTS.equals(typeOfGraph)) {
            List<Arc> arcs = new ArrayList<>();
            vertexHashMap.forEach((id, vertex) -> vertexHashMap.forEach((id1, vertex1) -> {
                if (vertex.getArcs().containsKey(id1) &&
                        vertex1.getArcs().containsKey(id) &&
                        !arcs.contains(new Arc(id1, id, vertex1.getArcs().get(vertex)))) {
                    Arc newArc = new Arc(id, id1, vertex.getArcs().get(vertex1));
                    arcs.add(newArc);
                }
            }));
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

    public List<Set<Integer>> getConnectivityComponents(){
        if(typeOfGraph == null){
            throw new NullPointerException("type of graph didn't be init");
        }

        List<Set<Integer>> connectivityComponents =  new ArrayList<>();
        List<Integer> vertexes = getVertexesList();

        vertexHashMap.forEach((id, vertex)->{
            if(vertexes.contains(id)) {
                Set<Integer> component = new HashSet<>();
                recourseForConnectivityComponents(id, vertexes,component);
                connectivityComponents.add(component);
            }
        });
        return connectivityComponents;
    }

    public int numberOfConnectivityComponents(){
        return getConnectivityComponents().size();
    }

    public void deleteVertex(Integer vertId){
        if(typeOfGraph == null){
            throw new NullPointerException("type of graph didn't be init");
        }
        vertexHashMap.forEach((index, vertex) -> vertex.getArcs().remove(vertId));
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

    public Graph getMinOstTree(){
        if(!TypeOfGraph.NOT_ORIENTEERING_WITH_WEIGHTS.equals(typeOfGraph))
            throw new IllegalArgumentException("your graph need to be not orienteering with weights");
        Graph graph = new Graph(typeOfGraph);
        getVertexesList().forEach(graph::addVertex);

        while (true){
            List<Set<Integer>> components = graph.getConnectivityComponents();
            if(components.size()==1)
                break;
            components.forEach(set->{
                AtomicInteger min = new AtomicInteger(Integer.MAX_VALUE);
                AtomicInteger minIdFrom = new AtomicInteger(Integer.MIN_VALUE);
                AtomicInteger minIdTo = new AtomicInteger(Integer.MIN_VALUE);
                set.forEach(vertex-> vertexHashMap.get(vertex).getArcs().forEach((id, weight)->{
                    if(!set.contains(id) && weight< min.get()){
                        min.set(weight);
                        minIdFrom.set(vertex);
                        minIdTo.set(id);
                    }
                }));
                if(!graph.vertexHashMap.get(minIdTo.get()).getArcs().containsKey(minIdFrom.get())){
                    graph.addArcWithWeight(minIdFrom.get(), minIdTo.get(), min.get());
                }
            });
        }
        return graph;
    }

    public Map<Integer,Integer> fordBellman(Integer vertexId){
        if(TypeOfGraph.NOT_ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)
                || TypeOfGraph.ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph))
            throw new IllegalArgumentException("your graph need to have weights");
        else if(typeOfGraph == null)
            throw new NullPointerException("type of graph didn't be init");

        Set<Arc> arcs = new HashSet();
        Map<Integer,Integer> result = new HashMap<>();
        vertexHashMap.forEach((id, vertex)->{
            if(!id.equals(vertexId))
                result.put(id, Integer.MAX_VALUE);
            else
                result.put(id, 0);
            vertex.getArcs().forEach((index, weight)->arcs.add(new Arc(id, index, weight)));
        });
        result.forEach((id, distance) -> arcs.forEach(arc ->{
            if(result.get(arc.getFirstId()) < Integer.MAX_VALUE)
                result.replace(arc.getSecondId(), Math.min(result.get(arc.getSecondId()), result.get(arc.getFirstId()) + arc.getWeight()));
        }));
        return result;
    }

    public Set<Map<Integer, Integer>> allMinDistance(){
        Set<Map<Integer, Integer>> allMinDistance = new HashSet<>();
        vertexHashMap.forEach((id, vertex)->allMinDistance.add(fordBellman(id)));
        return allMinDistance;
    }

    public Map<Integer, Integer> allMinDistanceToVertex(Integer vertexId){
        Map<Integer, Integer> allMinDistanceToVertex = new HashMap<>();
        vertexHashMap.forEach((id, vertex)->{
            allMinDistanceToVertex.put(id,fordBellman(id).get(vertexId));
        });
        return allMinDistanceToVertex;
    }

    public List<Arc> getAllArcs(){
        List<Arc> allArcs = new ArrayList<>();
        vertexHashMap.forEach((id, vertex)->vertex.getArcs().forEach((index, weight)->allArcs.add(new Arc(id, index, weight))));
        return allArcs;
    }

    public int getMaxFlow(Integer source, Integer stock){
        if(TypeOfGraph.NOT_ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph)
                ||TypeOfGraph.ORIENTEERING_WITHOUT_WEIGHTS.equals(typeOfGraph))
            throw new IllegalArgumentException("your graph need to have weights");
        else if(inComeList(stock).size()==0
                ||vertexHashMap.get(source).getArcs().size()==0){
            return -1;
        }
        Set<List<Arc>> allWays = getAllWays(source, stock);
        List<ArcWithFlow> allArcsWithFlow= new ArrayList<>();
        getAllArcs().forEach(arc->allArcsWithFlow.add(new ArcWithFlow(arc.getFirstId(), arc.getSecondId(), arc.getWeight(), 0)));

        allWays.forEach(way->{
            AtomicInteger min = new AtomicInteger(Integer.MAX_VALUE);
            way.forEach(arc-> allArcsWithFlow.forEach(arcWithFlow -> {
                if (arcWithFlow.getWeight() < min.get()
                        &&arc.equalsArcWithFlow(arcWithFlow)) {
                    min.set(arcWithFlow.getWeight());
                }
            }));
            allArcsWithFlow.forEach(arcWithFlow->way.forEach(arc->{
                if(arc.equalsArcWithFlow(arcWithFlow)){
                    arcWithFlow.setFlow(arcWithFlow.getFlow() + min.get());
                    arcWithFlow.setWeight(arcWithFlow.getWeight() - min.get());
                }
            }));
        });

        return allArcsWithFlow.stream().filter(arc -> arc.getFirstId().equals(source)).mapToInt(ArcWithFlow::getFlow).sum();
    }

    public Set<List<Arc>> getAllWays(Integer from, Integer to){
        Set<List<Arc>> allWays = new HashSet<>();
        recourseForAllWays(from, to, new ArrayList<>(), allWays);
        return allWays;
    }

    private void recourseForAllWays(Integer from, Integer to, List<Arc> way, Set<List<Arc>>allWays){
        outer:
        for (Integer id:
                vertexHashMap.get(from).getArcs().keySet()) {
            for (Arc arc:
                 way) {
                if(arc.getFirstId().equals(id)
                        ||arc.getSecondId().equals(id))
                    continue outer;
            }
            if(id.equals(to)){
                way.add(new Arc(from, id, vertexHashMap.get(from).getArcs().get(id)));
                allWays.add(copyWay(way));
                way.remove(way.size()-1);
            }
            else {
                way.add(new Arc(from, id, vertexHashMap.get(from).getArcs().get(id)));
                recourseForAllWays(id, to, way, allWays);
                way.remove(way.size()-1);
            }
        }
    }

    private List<Arc> copyWay(List<Arc> way){
        List<Arc> copyWay = new ArrayList<>();
        way.forEach(arc -> copyWay.add(new Arc(arc.getFirstId(), arc.getSecondId(), arc.getWeight())));
        return copyWay;
    }

    private void recourseForConnectivityComponents(Integer id, List<Integer> vertexes, Set<Integer> component){
        if(vertexes.contains(id)){
            List<Integer> vertexesList = getVertexesList();
            vertexes.remove(id);
            component.add(id);
            vertexHashMap.get(id).getArcs().forEach((index, weight)-> {
                if(hasAWay(index, id, vertexesList)){
                    recourseForConnectivityComponents(index, vertexes, component);
                }
            });
        }
    }

    private HashMap<Integer,Vertex> cloneGraph(HashMap<Integer,Vertex> otherGraph){
        HashMap<Integer,Vertex> vertexMap = new HashMap<>();

        otherGraph.forEach((id,vertex)->{
            Vertex vertex1 = new Vertex(id);
            vertex.getArcs().forEach(vertex1::addArc);
            vertexMap.put(id, vertex1);
        });
        return vertexMap;
    }

    public void initGraph(String fileName){
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
                lines.forEach(this::initWithOutWeight);
            }
            else{
                lines.forEach(this::initWithWeight);
            }
        }
        catch(IOException ex){
            throw new IllegalArgumentException("Ошбка чтения файла");
        }
    }

    public Graph UIGetMinOstTree(){
        Graph graph = new Graph(typeOfGraph);
        getVertexesList().forEach(graph::addVertex);

        while (true){
            List<Set<Integer>> components = graph.getConnectivityComponents();
            if(components.size()==1)
                break;
            components.forEach(set->{
                AtomicInteger min = new AtomicInteger(Integer.MAX_VALUE);
                AtomicInteger minIdFrom = new AtomicInteger(Integer.MIN_VALUE);
                AtomicInteger minIdTo = new AtomicInteger(Integer.MIN_VALUE);
                set.forEach(vertex-> vertexHashMap.get(vertex).getArcs().forEach((id, weight)->{
                    if(!set.contains(id) && weight< min.get()){
                        min.set(weight);
                        minIdFrom.set(vertex);
                        minIdTo.set(id);
                    }
                }));
                if(!graph.vertexHashMap.get(minIdTo.get()).getArcs().containsKey(minIdFrom.get())){
                    graph.addArcWithWeight(minIdFrom.get(), minIdTo.get(), min.get());
                    controller.printArc(minIdFrom.get(), minIdTo.get());
                }
            });
            /*try {
                Thread.sleep(10000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
        }
        return graph;
    }

    private void initWithWeight(String line){
        StringBuilder strFirstId = new StringBuilder();
        StringBuilder strSecondId = new StringBuilder();
        StringBuilder strWeight = new StringBuilder();
        boolean twoVertex = true;

        outerLoop:
        for(int i = 0; i < line.length(); i++){
            while(Character.isDigit(line.charAt(i))){
                strFirstId.append(line.charAt(i));
                if(i == line.length() - 1){
                    twoVertex = false;
                    break outerLoop;
                }
                i++;
            }
            i++;
            while(Character.isDigit(line.charAt(i))){
                strSecondId.append(line.charAt(i));                  //ОЧЕНЬ ПЛОХО, НО КОНКАТЕНАЦИЯ НЕ РАБОТАЕТ(НЕ ЗНАЮ ПОЧЕМУ)
                i++;
            }
            i++;
            while(Character.isDigit(line.charAt(i))){
                strWeight.append(line.charAt(i));                  //ОЧЕНЬ ПЛОХО, НО КОНКАТЕНАЦИЯ НЕ РАБОТАЕТ(НЕ ЗНАЮ ПОЧЕМУ)
                i++;
            }
        }

        int firstId = Integer.parseInt(strFirstId.toString());
        if(twoVertex){
            int secondId = Integer.parseInt(strSecondId.toString());
            int weight = Integer.parseInt(strWeight.toString());

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
        StringBuilder strFirstId = new StringBuilder();
        StringBuilder strSecondId = new StringBuilder();
        boolean twoVertex = true;

        outerLoop:
        for(int i = 0; i < line.length();){
            while(Character.isDigit(line.charAt(i))){
                strFirstId.append(line.charAt(i));                  //ОЧЕНЬ ПЛОХО, НО КОНКАТЕНАЦИЯ НЕ РАБОТАЕТ(НЕ ЗНАЮ ПОЧЕМУ)
                if(i == line.length() - 1){
                    twoVertex = false;
                    break outerLoop;
                }
                i++;
            }
            i++;
            while(Character.isDigit(line.charAt(i))){
                strSecondId.append(line.charAt(i));                  //ОЧЕНЬ ПЛОХО, НО КОНКАТЕНАЦИЯ НЕ РАБОТАЕТ(НЕ ЗНАЮ ПОЧЕМУ)
                if(i == line.length() - 1){
                    break outerLoop;
                }
                i++;
            }
        }

        int firstId = Integer.parseInt(strFirstId.toString());
        if(twoVertex){
            int secondId = Integer.parseInt(strSecondId.toString());
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