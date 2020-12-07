package com.university.graphs;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
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

    private final Graph graph;

    public UIController() {
        root = new Pane();
        graph = new Graph(this);
        vertexes = new HashMap<>();
        arcs = new ArrayList<>();
        root.setPrefSize(Main.WEIGHT, Main.HEIGHT);
        fileChooseText = new TextField();
        fileChooseText.setText("Write filename");
        fileChooseText.setTranslateX(Main.WEIGHT/2);
        fileChooseText.setTranslateY(Main.HEIGHT/3);

        readFileBtn = new Button();
        readFileBtn.setText("Enter");
        readFileBtn.setTranslateX(Main.WEIGHT/2);
        readFileBtn.setTranslateY(Main.HEIGHT/3*2);
        readFileBtn.setOnAction(event -> {
            try {
                graph.initGraph(fileChooseText.getText());
                root.getChildren().removeAll(fileChooseText, readFileBtn);
                writeGraph();
            }
            catch (IllegalArgumentException e){
                fileChooseText.setText(e.getMessage());
                e.printStackTrace();
            }
        });



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
            root.getChildren().add(uiVertex.getStack());
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



    }

    public void start(Graph graph){
        
    }
}
