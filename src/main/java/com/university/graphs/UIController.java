package com.university.graphs;

import javafx.scene.layout.Pane;
import lombok.Data;

import java.util.List;

@Data
public class UIController {

    private final Pane root;

    private List<UIVertex> vertexes;

    public UIController() {
        root = new Pane();
        root.setPrefSize(Main.WEIGHT, Main.HEIGHT);
    }

    public void start(Graph graph){
        
    }
}
