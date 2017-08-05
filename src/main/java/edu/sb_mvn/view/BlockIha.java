package edu.sb_mvn.view;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.scene.Group;
import javafx.scene.paint.Color;

@SuppressWarnings("restriction")
public class BlockIha extends Group {
	DoubleProperty startX;
	DoubleProperty startY;
	BlockController main;
	ArrayList<Bind> binder = new ArrayList<>();
	ArrayList<Anchor> groupList = new ArrayList<Anchor>();
	Set<Object> link = new HashSet<Object>();

	public BlockIha(int startX, int startY) {

		this.startX = new SimpleDoubleProperty(startX);
		this.startY = new SimpleDoubleProperty(startY);

		this.main = new BlockController(startX, startY);
		this.main.toBack();

		Bind bind = new Bind();

		int count = 22;
		int Height = 150;
		int Width = 100;

		for (int i = 0; i < count; i++) {

			bind.anchor.add(new Anchor(Color.GRAY, new SimpleDoubleProperty(startX - i - 1),
					new SimpleDoubleProperty(startY + i + 1), false));

			if (i < 7) {
				bind.property.add(new SimpleDoubleProperty(0));
				bind.property.add(new SimpleDoubleProperty((i + 1) * 20));
			} else if ((i > 6) && (i < 11)) {
				bind.property.add(new SimpleDoubleProperty((i - 6) * 20));
				bind.property.add(new SimpleDoubleProperty(Height));
			} else if ((i > 10) && (i < 18)) {
				bind.property.add(new SimpleDoubleProperty(Width));
				bind.property.add(new SimpleDoubleProperty((i - 10) * 20));
			} else if ((i > 17) && (i < 23)) {
				bind.property.add(new SimpleDoubleProperty((i - 17) * 20));
				bind.property.add(new SimpleDoubleProperty(0));
			}

			bind.summa.add(Bindings.add(bind.property.get(i * 2), this.main.layoutXProperty()));
			bind.summa.add(Bindings.add(bind.property.get((i * 2) + 1), this.main.layoutYProperty()));

			bind.anchor.get(i).centerXProperty().bind(bind.summa.get((i * 2)));
			bind.anchor.get(i).centerYProperty().bind(bind.summa.get((i * 2) + 1));

			bind.anchor.get(i).setNotMoved(true);
			
			groupList.add(bind.anchor.get(i));

		}

		this.main.setGroup(groupList);

		getChildren().add(main);

		for (int i = 0; i < count; i++) {
			getChildren().add(bind.anchor.get(i));
		}

	}

	public ArrayList<Anchor> getGroupList() {
		return groupList;
	}

	class Bind {
		public ArrayList<Anchor> anchor = null;
		public ArrayList<SimpleDoubleProperty> property = null;
		public ArrayList<NumberBinding> summa = null;

		public Bind() {
			this.anchor = new ArrayList<Anchor>();
			this.property = new ArrayList<SimpleDoubleProperty>();
			this.summa = new ArrayList<NumberBinding>();
		}
	}
}

