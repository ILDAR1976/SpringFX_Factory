package edu.sb_mvn.view;

import javafx.beans.property.DoubleProperty;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

@SuppressWarnings("restriction")
public class BoundLine extends Line {
	BoundLine(Color color, DoubleProperty startX, DoubleProperty startY, DoubleProperty endX, DoubleProperty endY) {
		startXProperty().bind(startX);
		startYProperty().bind(startY);
		endXProperty().bind(endX);
		endYProperty().bind(endY);

		setStroke(color.deriveColor(0, 1, 1, 0.5));

	}
}

