package edu.sb_mvn.view;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import edu.sb_mvn.model.Utils;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.StrokeType;

@SuppressWarnings("restriction")
public class Anchor extends Circle {
	private Anchor self = this;
	private Set<Anchor> linkAnchor = new HashSet<Anchor>();
	private boolean selected = false;
	private String id;
	private boolean notMoved = false;

	public Anchor(Color color, DoubleProperty x, DoubleProperty y, boolean selected) {
		super(x.get(), y.get(), 1);
		id = UUID.randomUUID().toString();
		setFill(color.deriveColor(1, 1, 1, 0.5));
		setStroke(color);
		setStrokeWidth(2);
		setStrokeType(StrokeType.OUTSIDE);

		x.bind(centerXProperty());
		y.bind(centerYProperty());

		this.selected = selected;

		enableDrag();
		
		linkAnchor.add(self);
	}

	public Set<Anchor> getLinkAnchor() {
		return linkAnchor;
	}

	public void dispose() {
		this.centerXProperty().unbind();
		this.centerYProperty().unbind();
		this.getLinkAnchor().remove(this);

		Anchor main = null;

		for (Anchor item : this.linkAnchor) {
			if (item.getParent() instanceof BlockIha) {
				main = item;
				break;
			} else {
				main = item;
				main.centerXProperty().unbind();
				main.centerYProperty().unbind();
			}
		}

		if (main == null)
			return;

		main.getLinkAnchor().remove(this);
		main.getParent().toFront();
		this.getLinkAnchor().remove(main);

		for (Anchor item : this.getLinkAnchor()) {

			item.getLinkAnchor().remove(this);
			item.centerXProperty().unbind();
			item.centerYProperty().unbind();
			item.centerXProperty().bind(main.centerXProperty());
			item.centerYProperty().bind(main.centerYProperty());
			item.getParent().toBack();

		}

	}
	
	public boolean isNotMoved() {
		return notMoved;
	}

	public void setNotMoved(boolean notMoved) {
		this.notMoved = notMoved;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public String getID() {
		return id;
	}

	@SuppressWarnings("restriction")
	private void enableDrag() {

		setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				getScene().setCursor(Cursor.MOVE);
			}
		});
		setOnMouseReleased(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (getScene() != null)
					getScene().setCursor(Cursor.HAND);
			}
		});
		setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				double newX = mouseEvent.getSceneX();
				double newY = mouseEvent.getSceneY();

				PickResult pickResult = mouseEvent.getPickResult();
				Node point = pickResult.getIntersectedNode();

				if (isSelected()) {
					if (point instanceof Circle) {
						if (((Anchor) point) != self) {
							setSelected(false);
						}
					}
				}

				if (point instanceof Circle && !selected)
					if (point.getClass().getSuperclass().getName() == "javafx.scene.shape.Circle") {
						
						if (point.getParent() instanceof BlockIha)
							if (self.getParent() instanceof BlockIha) 
								if (point != self) setNotMoved(true);
						
						if (self.getParent() instanceof BlockIha)
							if (point.getParent() instanceof BlockIha) 
								if (point != self) setNotMoved(true);
						
						if (!isNotMoved() && ((Anchor)point) != self  ) {
							Utils.linkAnchor(self, (Anchor) point);
						}
					}

				if (!self.centerXProperty().isBound())
					self.setCenterX(newX);
				if (!self.centerYProperty().isBound())
					self.setCenterY(newY);
			}

		});
		setOnMouseEntered(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (!mouseEvent.isPrimaryButtonDown()) {
					getScene().setCursor(Cursor.HAND);
				}
			}
		});
		setOnMouseExited(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				if (!mouseEvent.isPrimaryButtonDown()) {
					getScene().setCursor(Cursor.DEFAULT);
				}
			}
		});
	}
}
