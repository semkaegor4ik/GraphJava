package com.university.graphs;

import javafx.scene.layout.StackPane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
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

    private final StackPane stack = new StackPane();

    private final Line line = new Line();
    private final Text text;

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

        line.setStartX(firstX);
        line.setStartY(firstY);
        line.setEndX(secondX);
        line.setEndY(secondY);
        stack.getChildren().addAll(line, text);

    }
}
