package edu.sb_mvn.view;

import java.io.IOException;
import java.util.ArrayList;

import de.felixroske.jfxsupport.FXMLController;
import edu.sb_mvn.view.Anchor;
import javafx.beans.property.DoubleProperty;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Cursor;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.springframework.beans.factory.annotation.Value; 
import org.springframework.beans.factory.annotation.Autowired;

@SuppressWarnings({ "restriction", "unused" })
@FXMLController
public class BlockController extends AnchorPane {
	
	private DoubleProperty x;

	private DoubleProperty y;

	private ArrayList<Anchor> groupList = null;
	
	private BlockController self = this;
        
	@SuppressWarnings("restriction")
	public BlockController(@Value("0") int x, @Value("0") int y) {

		groupList = new ArrayList<Anchor>();

		relocateBlock(x, y);
		
		FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/" + getPackageNamePattern() + "/block.fxml"));

		fxmlLoader.setRoot(this);
		fxmlLoader.setController(this);

		getStyleClass().add("block-blue");

		try {
			fxmlLoader.load();

		} catch (IOException exception) {
			throw new RuntimeException(exception);
		}

		enableDrag();
	}

	@FXML
	private void initialize() {
	}

	public void relocateBlock(int x, int y) {
		relocate((int) (x - (getBoundsInLocal().getWidth() / 2)), (int) (y - (getBoundsInLocal().getHeight() / 2)));
	}

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

				relocateBlock((int) mouseEvent.getSceneX(), (int) mouseEvent.getSceneY());
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

	public void setGroup(ArrayList<Anchor> groupList) {
		this.groupList = groupList;
	}

	private String getPackageNamePattern(){
		return getClass().getPackage().getName().replace(".", "/");
	}

}
