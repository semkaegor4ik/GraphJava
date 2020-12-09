package com.university.graphs;

import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.Group;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import javafx.util.Duration;
import lombok.Data;

@Data
public class UIArc {
    private final double firstX;
    private final double firstY;
    private final Integer firstId;
    private final double secondX;
    private final double secondY;
    private final Integer secondId;
    private final int weight;

    private final Group stack = new Group();

    private final Line line = new Line();
    private final Text text;

    private boolean isPaint = false;

    public UIArc(double firstX, double firstY,Integer firstId, double secondX, double secondY, Integer secondId, int weight) {
        this.firstX = firstX;
        this.firstY = firstY;
        this.firstId = firstId;
        this.secondX = secondX;
        this.secondY = secondY;
        this.secondId = secondId;
        this.weight = weight;

        text = new Text(String.valueOf(weight));
        text.setFont(new Font(30));
        text.setBoundsType(TextBoundsType.VISUAL);
        text.setX((firstX+secondX)/2);
        text.setY((firstY+secondY)/2);

        line.setStartX(firstX + UIVertex.RADIUS);
        line.setStartY(firstY + UIVertex.RADIUS);
        line.setEndX(secondX + UIVertex.RADIUS);
        line.setEndY(secondY + UIVertex.RADIUS);
        line.setStrokeWidth(3);

        stack.getChildren().addAll(line, text);
    }

    public void paintArc(){

        Timeline timeline = new Timeline(
        );
        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
        /*line.setStroke(Color.RED);
        line.setStrokeWidth(3);
        text.setFill(Color.RED);
        isPaint = true;*/
    }
}
