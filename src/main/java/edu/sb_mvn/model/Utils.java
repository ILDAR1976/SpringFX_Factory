package edu.sb_mvn.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import edu.sb_mvn.view.Anchor;
import edu.sb_mvn.view.AnchorList;
import edu.sb_mvn.view.BlockController;
import edu.sb_mvn.view.BlockIha;
import edu.sb_mvn.view.BoundLine;
import edu.sb_mvn.view.LineIha;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.Cursor;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.PickResult;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.Parent;

@SuppressWarnings("restriction")
public class Utils {
    private static boolean controlDown = false;
    private static final int TYPE_PANE = 1;
    private static final int TYPE_CONTROLLER = 2;
    private static final int TYPE_BLOCK = 3;
    private static final int TYPE_LINE = 4;
	

	private static final int LINK_DIRECT = 0;
	private static final int LINK_CIRCLE = 1;	public void makeLinks(Group root) {
		ArrayList<Anchor> anchorList = new ArrayList<Anchor>();

		for (Node item : root.getChildren()) {
			if (item instanceof LineIha) {
				anchorList.add(((LineIha) item).getStart());
				anchorList.add(((LineIha) item).getEnd());
			}

			if (item instanceof BlockIha) {
				for (Anchor anchor : ((BlockIha) item).getGroupList()) {
					anchorList.add(anchor);
				}
			}
		}

		Map<String, AnchorList> points = new HashMap<String, AnchorList>();

		for (Anchor item : anchorList) {
			String key = (int) item.centerXProperty().get() + "*" + (int) item.centerYProperty().get();
			AnchorList value = new AnchorList();
			value.list.add(item);

			if (points.get(key) != null) {
				value = points.get(key);
				value.list.add(item);
				points.put(key, value);
			} else {
				points.put(key, value);
			}
		}

		for (Map.Entry<String, AnchorList> item : points.entrySet()) {

			if (item.getValue().list.size() > 1) {
				ArrayList<Anchor> jobAnchor = item.getValue().list;
				Anchor main = jobAnchor.get(0);
				jobAnchor.remove(0);
				for (Anchor anchor : jobAnchor) {
					Utils.linkAnchor(main, anchor);
				}
			} else {
				ArrayList<Anchor> jobAnchor = item.getValue().list;
			}
		}

	}

	@SuppressWarnings("restriction")
	public static void enableDragBasic(Scene scene, Group root) {
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				scene.setCursor(Cursor.DEFAULT);

				if (mouseEvent.isPrimaryButtonDown()) {

					PickResult pickResult = mouseEvent.getPickResult();
					Node point = pickResult.getIntersectedNode();
					
					if (point != null) {
						if (point.getClass().getSuperclass().getName() == "javafx.scene.shape.Circle"
								|| point.getClass().getSuperclass().getName() == "javafx.scene.shape.Line") {
							if (controlDown) {
								if (point instanceof Circle) {
									if (!((Anchor) point).isSelected()) {
										Anchor anchor = (Anchor) point;

										Utils.s();
										Utils.p("<<<< Create the new line >>>>");
										Utils.s();

										LineIha line = new LineIha((int) anchor.centerXProperty().get(),
												(int) anchor.centerYProperty().get(), Color.GREEN);

										Utils.linkAnchor(anchor, line.getStart());
										

										root.getChildren().add(line);
									}
								}
							} else {
								if (point instanceof Circle) {
									System.out.println("--------------Lines list this point---------");

									int count = 0;

									for (Anchor item : ((Anchor) point).getLinkAnchor()) {
										count++;
										System.out.println("| " + count 
																+ ") "
																+ item.getID().substring(33) 
												                + " - " 
												                + item.getParent().toString().substring(20) 
								                                + " NotMoved: " 
												                + item.isNotMoved());
									}

									System.out.println("------------------------------------------");
								}

								if (point instanceof BoundLine) {
									System.out.println(
											"---------------Linked list this line-----------------------------------");

									System.out.println("|   <This line:" + ((LineIha) point.getParent()) + ">");
									System.out.println(
											"|--------------Linked list this line-----------------------------------");

									int count = 0;

									for (Anchor item : ((LineIha) point.getParent()).getStart().getLinkAnchor()) {
										if (item != ((LineIha) point.getParent()).getStart()) {
											count++;
											System.out.println("| " + count + ") " + item.getParent());
										}
									}

									count = (count == 0) ? 0 : count--;

									for (Anchor item : ((LineIha) point.getParent()).getEnd().getLinkAnchor()) {
										if (item != ((LineIha) point.getParent()).getEnd()) {
											count++;
											System.out.println("| " + count + ") " + item.getParent());
										}
									}

									System.out.println(
											"-------------------------------------------------------------------");

								}
							}
						}
					}
				}

				if (mouseEvent.isSecondaryButtonDown()) {

					PickResult pickResult = mouseEvent.getPickResult();
					Node point = pickResult.getIntersectedNode();
					
					int type = 0;
					
					if (point != null) {
						if (point.getParent() instanceof Pane) type = TYPE_PANE;
						if (point.getParent() instanceof BlockController) type = TYPE_CONTROLLER;
						if (point.getParent() instanceof BlockIha) type = TYPE_BLOCK;
						if (point.getParent() instanceof LineIha) type = TYPE_LINE;
					}
					
					if ((type >= TYPE_PANE) && (type <= TYPE_BLOCK)){
						
						BlockIha block = null;
						
						switch (type){
							case TYPE_PANE:
								block = (BlockIha) point.getParent().getParent().getParent();
								break;
							case TYPE_CONTROLLER:
								block = (BlockIha) point.getParent().getParent();
								break;
							case TYPE_BLOCK:
								block = (BlockIha) point.getParent();
								break;	
						}
						
						deleteBlock(block, root);
					}
					
					
					if (point instanceof Circle) {
						if (point.getParent() instanceof BlockIha) {
							deleteBlock(((BlockIha) point.getParent()), root);
						}

						if (point.getParent() instanceof LineIha) {
							((LineIha) point.getParent()).getStart().dispose();
							((LineIha) point.getParent()).getEnd().dispose();
							root.getChildren().remove(point.getParent());
						}
					}

					if (point instanceof BoundLine) {
						((LineIha) point.getParent()).getStart().dispose();
						((LineIha) point.getParent()).getEnd().dispose();
						root.getChildren().remove(point.getParent());
					}
				}

			}
		});
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				scene.setCursor(Cursor.CROSSHAIR);
				controlDown = keyEvent.isControlDown();
			}
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				scene.setCursor(Cursor.DEFAULT);
				controlDown = false;
			}
		});

	}

	public static Node pick(Node node, double sceneX, double sceneY) {
		Point2D p = node.sceneToLocal(sceneX, sceneY, true);
		if (!node.contains(p))
			return null;
		if (node instanceof Parent) {
			Node bestMatchingChild = null;
			List<Node> children = ((Parent) node).getChildrenUnmodifiable();
			for (int i = children.size() - 1; i >= 0; i--) {
				Node child = children.get(i);
				p = child.sceneToLocal(sceneX, sceneY, true);
				if (child.isVisible() && !child.isMouseTransparent() && child.contains(p)) {
					bestMatchingChild = child;
					break;
				}
			}

			if (bestMatchingChild != null) {
				return pick(bestMatchingChild, sceneX, sceneY);
			}
		}

		return node;
	}

	public static void linkAnchor(Anchor main, Anchor anchor) {

		if (anchor != main) {
			
			Anchor master = (anchor.getParent() instanceof BlockIha) ? anchor : main;
			Anchor slave = (anchor.getParent() instanceof BlockIha) ? main : anchor;
			
			if (main.getParent() instanceof BlockIha ||
			    anchor.getParent() instanceof BlockIha ) {
				master.setNotMoved(true);
				slave.setNotMoved(true);
			}
			

			master.getLinkAnchor().addAll(slave.getLinkAnchor());
			
			slave.getParent().toBack();
			master.getParent().toFront();
			
			if (master.getLinkAnchor().size() <= 2){
				link(master, slave, LINK_DIRECT);
			} else {
				link(master, slave, LINK_CIRCLE);
			}
		}
	}

	private static void link(Anchor master, Anchor slave, int selector) {
		switch (selector) {
		case LINK_DIRECT:
			
			slave.getLinkAnchor().addAll(master.getLinkAnchor());

			slave.centerXProperty().bind(master.centerXProperty());
			slave.centerYProperty().bind(master.centerYProperty());
			
			break;
		
		case LINK_CIRCLE:
			slave.centerXProperty().bind(master.centerXProperty());
			slave.centerYProperty().bind(master.centerYProperty());
			
			for (Anchor item : master.getLinkAnchor()) {

				if (item != master) {
				
					item.getLinkAnchor().addAll(master.getLinkAnchor());
		
					if (master.isNotMoved()) item.setNotMoved(true);
					
					item.getParent().toBack();
				}
			}
			
			break;
		}
	}
	
	@SuppressWarnings("restriction")
	public static void deleteBlock(BlockIha block, Group root){
		for (Anchor item : block.getGroupList()) {
			item.getLinkAnchor().remove(item);
			for (Anchor item2 : item.getLinkAnchor()) {
				if (item2.getParent() instanceof LineIha) {
					((LineIha) item2.getParent()).getStart().getLinkAnchor().remove(item2);
					((LineIha) item2.getParent()).getStart().getLinkAnchor().remove(item);
					((LineIha) item2.getParent()).getStart().dispose();
					((LineIha) item2.getParent()).getEnd().getLinkAnchor().remove(item2);
					((LineIha) item2.getParent()).getEnd().getLinkAnchor().remove(item);
					((LineIha) item2.getParent()).getEnd().dispose();
					root.getChildren().remove((LineIha)item2.getParent());
				}
			}

			item.dispose();
			root.getChildren().remove(item.getParent());
		}		
	}

	public static void makeLinksBasic(Group root) {
		ArrayList<Anchor> anchorList = new ArrayList<Anchor>();

		for (Node item : root.getChildren()) {
			if (item instanceof LineIha) {
				anchorList.add(((LineIha) item).getStart());
				anchorList.add(((LineIha) item).getEnd());
			}

			if (item instanceof BlockIha) {
				for (Anchor anchor : ((BlockIha) item).getGroupList()) {
					anchorList.add(anchor);
				}
			}
		}

		Map<String, AnchorList> points = new HashMap<String, AnchorList>();

		for (Anchor item : anchorList) {
			String key = (int) item.centerXProperty().get() + "*" + (int) item.centerYProperty().get();
			AnchorList value = new AnchorList();
			value.list.add(item);

			if (points.get(key) != null) {
				value = points.get(key);
				value.list.add(item);
				points.put(key, value);
			} else {
				points.put(key, value);
			}
		}

		for (Map.Entry<String, AnchorList> item : points.entrySet()) {

			if (item.getValue().list.size() > 1) {
				ArrayList<Anchor> jobAnchor = item.getValue().list;
				Anchor main = jobAnchor.get(0);
				jobAnchor.remove(0);
				for (Anchor anchor : jobAnchor) {
					Utils.linkAnchor(main, anchor);
				}
			} else {
				ArrayList<Anchor> jobAnchor = item.getValue().list;
			}
		}

	}

	@SuppressWarnings("restriction")
	public void enableDrag(Scene scene, Group root) {
		scene.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent mouseEvent) {
				scene.setCursor(Cursor.DEFAULT);

				if (mouseEvent.isPrimaryButtonDown()) {

					PickResult pickResult = mouseEvent.getPickResult();
					Node point = pickResult.getIntersectedNode();
					
					if (point != null) {
						if (point.getClass().getSuperclass().getName() == "javafx.scene.shape.Circle"
								|| point.getClass().getSuperclass().getName() == "javafx.scene.shape.Line") {
							if (controlDown) {
								if (point instanceof Circle) {
									if (!((Anchor) point).isSelected()) {
										Anchor anchor = (Anchor) point;

										Utils.s();
										Utils.p("<<<< Create the new line >>>>");
										Utils.s();

										LineIha line = new LineIha((int) anchor.centerXProperty().get(),
												(int) anchor.centerYProperty().get(), Color.GREEN);

										Utils.linkAnchor(anchor, line.getStart());
										

										root.getChildren().add(line);
									}
								}
							} else {
								if (point instanceof Circle) {
									System.out.println("--------------Lines list this point---------");

									int count = 0;

									for (Anchor item : ((Anchor) point).getLinkAnchor()) {
										count++;
										System.out.println("| " + count 
																+ ") "
																+ item.getID().substring(33) 
												                + " - " 
												                + item.getParent().toString().substring(20) 
								                                + " NotMoved: " 
												                + item.isNotMoved());
									}

									System.out.println("------------------------------------------");
								}

								if (point instanceof BoundLine) {
									System.out.println(
											"---------------Linked list this line-----------------------------------");

									System.out.println("|   <This line:" + ((LineIha) point.getParent()) + ">");
									System.out.println(
											"|--------------Linked list this line-----------------------------------");

									int count = 0;

									for (Anchor item : ((LineIha) point.getParent()).getStart().getLinkAnchor()) {
										if (item != ((LineIha) point.getParent()).getStart()) {
											count++;
											System.out.println("| " + count + ") " + item.getParent());
										}
									}

									count = (count == 0) ? 0 : count--;

									for (Anchor item : ((LineIha) point.getParent()).getEnd().getLinkAnchor()) {
										if (item != ((LineIha) point.getParent()).getEnd()) {
											count++;
											System.out.println("| " + count + ") " + item.getParent());
										}
									}

									System.out.println(
											"-------------------------------------------------------------------");

								}
							}
						}
					}
				}

				if (mouseEvent.isSecondaryButtonDown()) {

					PickResult pickResult = mouseEvent.getPickResult();
					Node point = pickResult.getIntersectedNode();
					
					int type = 0;
					
					if (point != null) {
						if (point.getParent() instanceof Pane) type = TYPE_PANE;
						if (point.getParent() instanceof BlockController) type = TYPE_CONTROLLER;
						if (point.getParent() instanceof BlockIha) type = TYPE_BLOCK;
						if (point.getParent() instanceof LineIha) type = TYPE_LINE;
					}
					
					if ((type >= TYPE_PANE) && (type <= TYPE_BLOCK)){
						
						BlockIha block = null;
						
						switch (type){
							case TYPE_PANE:
								block = (BlockIha) point.getParent().getParent().getParent();
								break;
							case TYPE_CONTROLLER:
								block = (BlockIha) point.getParent().getParent();
								break;
							case TYPE_BLOCK:
								block = (BlockIha) point.getParent();
								break;	
						}
						
						deleteBlock(block, root);
					}
					
					
					if (point instanceof Circle) {
						if (point.getParent() instanceof BlockIha) {
							deleteBlock(((BlockIha) point.getParent()), root);
						}

						if (point.getParent() instanceof LineIha) {
							((LineIha) point.getParent()).getStart().dispose();
							((LineIha) point.getParent()).getEnd().dispose();
							root.getChildren().remove(point.getParent());
						}
					}

					if (point instanceof BoundLine) {
						((LineIha) point.getParent()).getStart().dispose();
						((LineIha) point.getParent()).getEnd().dispose();
						root.getChildren().remove(point.getParent());
					}
				}

			}
		});
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				scene.setCursor(Cursor.CROSSHAIR);
				controlDown = keyEvent.isControlDown();
			}
		});
		scene.setOnKeyReleased(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent keyEvent) {
				scene.setCursor(Cursor.DEFAULT);
				controlDown = false;
			}
		});

	}
	
	
	public static void p(String msg) {
		System.out.println(msg);
	}

	public static void p(Anchor anr) {
		System.out.println("--------------" + anr.getID() + "--------------");
		for (Anchor item : anr.getLinkAnchor()) {
			System.out.println("| " + item.getParent());
		}
		System.out.println("-------------- End anchor list--------------");
	}

	public static void p(String mark, Anchor anr, int selector) {

		System.out.println("--------------" + mark + " " + anr.getID() + "--------------");
		switch (selector) {
		case 0:
			for (Anchor item : anr.getLinkAnchor()) {
				System.out.println("| " + item.getID().substring(33) + " - " + item.getParent().toString().substring(20));
			}
			break;
		case 1:
			System.out.println("| " + anr.getID().substring(33));
			break;
		case 2:
			System.out.println("| " + anr.getID().substring(33) + " - " + anr.getParent().toString().substring(20));
			break;
		case 3:
			System.out.println("| " + anr.getID().substring(33) + " - " + anr.getParent().toString().substring(20) 
					                + " NotMoved: " + anr.isNotMoved());
			break;
		}
		System.out.println("-------------- End anchor list--------------");
	}

	public static void p(Set<Anchor> anr) {
		System.out.println("-------------- Anchor list --------------");
		for (Anchor item : anr) {
			System.out.println("| " + item.getParent());
		}
		System.out.println("-------------- End anchor list--------------");
	}

	public static void p(String msg, Set<Anchor> anr, int selector) {
		System.out.println("--------------" + msg + " Anchor list --------------");
		for (Anchor item : anr) {
			switch (selector) {
			case 0:
				System.out.println("| " + item.getParent().toString().substring(20));
				break;
			case 1:
				System.out.println("| " + item.getID());

			case 2:
				System.out.println("| " + item.getID() + " - " + item.getParent().toString().substring(20));

			case 3:
				System.out
						.println("| " + item.getID().substring(33) + " - " + item.getParent().toString().substring(20));
			}
		}
		System.out.println("-------------- End anchor list--------------");
	}

	public static void p(boolean parent, Anchor anr) {
		if (parent)
			System.out.println("---Anchor: " + anr.getID() + "--------");
		else
			System.out.println("---Anchor: " + anr.getParent() + "--------");
	}

	public static void s() {
		System.out.println();
	}

	

}
