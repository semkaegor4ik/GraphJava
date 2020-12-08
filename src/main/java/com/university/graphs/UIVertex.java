package com.university.graphs;

import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextBoundsType;
import lombok.Data;

@Data
public class UIVertex {
    private final Integer id;
    public static final double RADIUS = 15;
    private final Circle circle = new Circle(RADIUS);;
    private final Text text;
    private final StackPane stack = new StackPane();

    private final double x;
    private final double y;

    public UIVertex(Integer id, double x, double y) {
        this.x = x;
        this.y = y;
        this.id = id;
        circle.setFill(Color.CADETBLUE);
        text = new Text(id.toString());
        text.setFont(new Font(30));
        text.setBoundsType(TextBoundsType.VISUAL);
        stack.getChildren().addAll(circle, text);
    }


}
