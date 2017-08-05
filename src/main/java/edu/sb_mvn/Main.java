package edu.sb_mvn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import edu.sb_mvn.ObjectsManipulationView;
import edu.sb_mvn.model.Utils;
import edu.sb_mvn.view.BlockIha;
import edu.sb_mvn.view.LineIha;
import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.NumberBinding;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.StrokeType;
import javafx.stage.Stage;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import de.felixroske.jfxsupport.AbstractJavaFxApplicationSupport;
import org.springframework.beans.factory.annotation.Autowired;
import javafx.scene.shape.Ellipse;
import edu.sb_mvn.ObjectsManipulation;

@SpringBootApplication
public class Main extends AbstractJavaFxApplicationSupport {
	public Stage stage = null;

	public static void main(String[] args){
		launchApp(Main.class,ObjectsManipulationView.class, args);
	}


	@SuppressWarnings("restriction")
	@Override
	public void start(final Stage stage) throws Exception {
	        this.stage = stage;
		stage.setTitle("Line Manipulation Sample");
		Group root = new Group();
		 
		root.getChildren().addAll(new BlockIha(50, 100), new LineIha(10, 10, 590, 10, Color.RED),
				new LineIha(10, 35, 590, 35, Color.BLUE), new LineIha(10, 60, 590, 60, Color.GREEN),
				new BlockIha(50, 400), new BlockIha(450, 100), new BlockIha(450, 400),
				new LineIha(70, 250, 300, 400, Color.RED), new LineIha(300, 400, 530, 250, Color.CHOCOLATE),
				new LineIha(150, 420, 450, 120, Color.BROWN), new LineIha(150, 120, 450, 420, Color.BLACK));
		
		Scene scene = new Scene(root, 600, 600, Color.ALICEBLUE);

		scene.getStylesheets().add(getClass().getResource("/application.css").toExternalForm());
		Utils.enableDragBasic(scene, root);

		stage.setScene(scene);

		stage.show();

		Utils.makeLinksBasic(root);
	}

}