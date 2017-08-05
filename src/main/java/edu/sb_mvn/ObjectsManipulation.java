package edu.sb_mvn;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import edu.sb_mvn.model.Utils;
import edu.sb_mvn.view.Anchor;
import edu.sb_mvn.view.BlockController;
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
import javafx.scene.Scene;
import de.felixroske.jfxsupport.FXMLController;
import javafx.fxml.FXML;
import de.felixroske.jfxsupport.FXMLView;

@SuppressWarnings("restriction")
@FXMLController
public class ObjectsManipulation extends Pane {
    @SuppressWarnings("restriction")
	@FXML
	private void initialize() {
	}

	
	//For tests
	
	public LineIha testLineIha(int startX, int startY, int endX, int endY, Color color){
		return new LineIha(startX, startY, endX, endY, color);
	}
	
	public BlockIha testBlockIha(int startX, int startY){
		return new BlockIha(startX, startY);
	}
	
	public void testEnableDrag(Scene scene, Group root){
		Utils.enableDragBasic(scene, root);

		
	}

	public void testMakeLinks(Group root){
		Utils.makeLinksBasic(root);
	}
}
