package edu.sb_mvn.view;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;

@SuppressWarnings("restriction")
public class LineIha extends Group {

	private Line line;
	private Anchor start;
	private Anchor end;

	DoubleProperty startX;
	DoubleProperty startY;
	DoubleProperty endX;
	DoubleProperty endY;

	public LineIha(int startX, int startY, int endX, int endY, Color color) {

		this.startX = new SimpleDoubleProperty(startX);
		this.startY = new SimpleDoubleProperty(startY);
		this.endX = new SimpleDoubleProperty(endX);
		this.endY = new SimpleDoubleProperty(endY);

		this.start = new Anchor(color, this.startX, this.startY, false);
		this.end = new Anchor(color, this.endX, this.endY, false);
		this.line = new BoundLine(color, this.startX, this.startY, this.endX, this.endY);

		getChildren().addAll(line, start, end);
	}

	public LineIha(int startX, int startY, Color color) {

		this.startX = new SimpleDoubleProperty(startX);
		this.startY = new SimpleDoubleProperty(startY);
		this.endX = new SimpleDoubleProperty(startX);
		this.endY = new SimpleDoubleProperty(startY);

		this.start = new Anchor(color, this.startX, this.startY, false);
		this.end = new Anchor(color, this.endX, this.endY, true);
		this.line = new BoundLine(color, this.startX, this.startY, this.endX, this.endY);

		getChildren().addAll(line, start, end);
	}

	public Anchor getStart() {
		return start;
	}

	public Anchor getEnd() {
		return end;
	}
	

}


