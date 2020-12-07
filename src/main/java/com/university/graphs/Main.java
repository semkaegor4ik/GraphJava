package com.university.graphs;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class Main extends Application {
    private Pane root;
    public static final int WEIGHT = 700;
    public static final int HEIGHT = 700;
    @Override
    public void start(Stage primaryStage) throws Exception {
        UIController controller = new UIController();
        root = controller.getRoot();
        //primaryStage.setResizable(false);
        primaryStage.setTitle("Boruvki");
        primaryStage.setScene(new Scene(root));
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
