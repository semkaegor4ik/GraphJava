package com.university.graphs;

import com.sun.prism.impl.TextureResourcePool;
import javafx.animation.*;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import lombok.Data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Data
public class UIController {

    private final Pane root;

    private final Map<Integer, UIVertex> vertexes;

    private final List<UIArc> arcs;

    private final TextField fileChooseText;

    private final Button readFileBtn;

    private final Button startAlg;

    private final AnimationTimer timer;

    private Graph graph;

    private final Thread boruvkiThread;

    public UIController() {
        root = new Pane();
        graph = new Graph(this);
        vertexes = new HashMap<>();
        arcs = new ArrayList<>();
        boruvkiThread = new Thread(new BoruvkiThread(this));
        root.setPrefSize(Main.WEIGHT, Main.HEIGHT);
        fileChooseText = new TextField();
        fileChooseText.setText("Write filename");
        fileChooseText.setTranslateX(Main.WEIGHT/2);
        fileChooseText.setTranslateY(Main.HEIGHT/3);

        startAlg = new Button();
        startAlg.setText("Start");
        startAlg.setOnAction(event -> {
            startThread();
        });

        readFileBtn = new Button();
        readFileBtn.setText("Enter");
        readFileBtn.setTranslateX(Main.WEIGHT/2);
        readFileBtn.setTranslateY(Main.HEIGHT/3*2);
        readFileBtn.setOnAction(event -> {
            try {
                graph.initGraph(fileChooseText.getText());
                if(TypeOfGraph.NOT_ORIENTEERING_WITH_WEIGHTS.equals(graph.getTypeOfGraph())) {
                    root.getChildren().removeAll(fileChooseText, readFileBtn);
                    writeGraph();
                }
                else{
                    fileChooseText.setText("your graph need to be not orienteering with weights");
                    graph = new Graph();
                }
            }
            catch (IllegalArgumentException e){
                fileChooseText.setText(e.getMessage());
                e.printStackTrace();
            }
        });

        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                if(!boruvkiThread.isAlive()){
                    arcs.forEach(arc->{
                        if(!arc.isPaint()){
                            root.getChildren().remove(arc.getStack());
                        }
                    });
                    timer.stop();
                }
            }
        };


        root.getChildren().addAll(fileChooseText, readFileBtn);
    }


    private void writeGraph() {
        List<Integer> vertexesList = graph.getVertexesList();
        AtomicInteger k = new AtomicInteger(0);
        int count = vertexesList.size();
        vertexesList.forEach(id->{
            double angle = 360/count * k.get();
            double xUsually = (Main.HEIGHT/2 - 20) * Math.cos(Math.toRadians(angle));
            double yUsually = (Main.HEIGHT/2 - 20) * Math.sin(Math.toRadians(angle));
            UIVertex uiVertex = new UIVertex(id, xUsually + (Main.HEIGHT/2 - 20), (Main.HEIGHT/2 - 20) - yUsually);
            uiVertex.getStack().setTranslateX(xUsually + (Main.HEIGHT/2 - 20));
            uiVertex.getStack().setTranslateY((Main.HEIGHT/2 - 20) - yUsually);
            vertexes.put(id, uiVertex);
            k.getAndIncrement();
        });

        List<Arc> allArcs = graph.getAllArcs();

        allArcs.forEach(arc->{
            UIArc uiArc = new UIArc(vertexes.get(arc.getFirstId()).getX(),
                    vertexes.get(arc.getFirstId()).getY(),
                    arc.getFirstId(),
                    vertexes.get(arc.getSecondId()).getX(),
                    vertexes.get(arc.getSecondId()).getY(),
                    arc.getSecondId(),
                    arc.getWeight());
            arcs.add(uiArc);
            root.getChildren().add(uiArc.getStack());
        });

        vertexes.forEach((id,vertex)->root.getChildren().add(vertex.getStack()));

        root.getChildren().add(startAlg);
    }


    private void startThread() {
        root.getChildren().remove(startAlg);
        boruvkiThread.start();
        timer.start();
    }

    public void start(){
        graph.UIGetMinOstTreeBorvki();
    }

    public void printArc(Integer firstId, Integer secondId){
        arcs.forEach(arc->{
            if(arc.getFirstId().equals(firstId) && arc.getSecondId().equals(secondId)
                    ||arc.getFirstId().equals(secondId) && arc.getSecondId().equals(firstId)){
                arc.paintArc();
            }
        });
    }


    public void printVertexes() {
        vertexes.forEach((id, vertex)->{
            vertex.paintVertex();
        });
    }
}
