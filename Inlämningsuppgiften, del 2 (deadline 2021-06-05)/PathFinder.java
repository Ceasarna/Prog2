import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Optional;
import java.util.Set;

import javax.imageio.ImageIO;

import javafx.application.Application;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Pair;


public class PathFinder extends Application{
	private static final String PATH_MAP = "europa.gif";
	private ListGraph<Dot> listGraph = new ListGraph<Dot>();
    private Dot b1;
    private Dot b2;
	private Button newPlaceButton;
	private Pane center;
	private Stage stage;
    private BorderPane root;
    private boolean changed;
    private Image image;
    private ImageView imageView;
    
	@Override
	public void start(Stage stage) {
		this.stage = stage;
		root = new BorderPane();
        root.setStyle("-fx-font-size: 14");

        center = new Pane();
        root.setCenter(center);
        center.setId("outputArea");
        
		VBox vbox = new VBox();
		MenuBar menuBar = new MenuBar();
		menuBar.setId("menu");
		vbox.getChildren().add(menuBar);
		root.setTop(vbox);
		
		Menu fileMenu = new Menu("File");
		menuBar.getMenus().add(fileMenu);
		fileMenu.setId("menuFile");
		
		MenuItem newMap = new MenuItem("New Map");
        fileMenu.getItems().add(newMap);
        newMap.setOnAction(new NewMapHandler());
        newMap.setId("menuNewMap");
        
		MenuItem openItem = new MenuItem("Open");
        fileMenu.getItems().add(openItem);
        openItem.setOnAction(new OpenHandler());
        openItem.setId("menuOpenFile");
        
        MenuItem saveItem = new MenuItem("Save");
        fileMenu.getItems().add(saveItem);
        saveItem.setOnAction(new SaveHandler());
        saveItem.setId("menuSaveFile");
        
        MenuItem saveImageItem = new MenuItem("Save Image");
        fileMenu.getItems().add(saveImageItem);
        saveImageItem.setOnAction(new SaveImageHandler());
        saveImageItem.setId("menuSaveImage");
        
        MenuItem exitItem = new MenuItem("Exit");
        fileMenu.getItems().add(exitItem);
        exitItem.setOnAction(new ExitItemHandler());
        exitItem.setId("menuExit");
                
        FlowPane flow = new FlowPane();
        vbox.getChildren().add(flow);
        flow.setAlignment(Pos.CENTER);
        
        Button findButton = new Button("Find Path");
        findButton.setOnAction(new NewFindHandler());
        findButton.setId("btnFindPath");
        
        Button showButton = new Button("Show Connection");
        showButton.setOnAction(new NewShowHandler());
        showButton.setId("btnShowConnection");
        
        newPlaceButton = new Button("New Place");
        newPlaceButton.setOnAction(new NewPlaceHandler());
        newPlaceButton.setId("btnNewPlace");
        
        Button newConnectionButton = new Button("New Connection");
        newConnectionButton.setOnAction(new NewConnectionHandler());
        newConnectionButton.setId("btnNewConnection");
        
        Button changeButton = new Button("Change Connection");
        changeButton.setOnAction(new NewChangeHandler());
        changeButton.setId("btnChangeConnection");
           
        flow.getChildren().addAll(findButton, showButton, newPlaceButton,
        		newConnectionButton, changeButton);
        
        flow.setPadding(new Insets(10));
        flow.setHgap(10);
		
		Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.setTitle("PathFinder");
        stage.setOnCloseRequest(new ExitHandler());
        stage.show();
	}

	class ExitItemHandler implements EventHandler<ActionEvent>{
        @Override public void handle(ActionEvent event){
            stage.fireEvent(new WindowEvent(stage, WindowEvent.WINDOW_CLOSE_REQUEST));
        }
    } 

    class ExitHandler implements EventHandler<WindowEvent>{
        @Override public void handle(WindowEvent event){
            if (changed()){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                        "Unsaved changes, continue anyway?");
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ButtonType.CANCEL)
                    event.consume();
            }
        }
    }
	
	private boolean changed(){
	        if (changed) {
	        	return true;
	        }
	        for(Node node : center.getChildren()) {
	        	if(node instanceof Dot) {
	        		if(((Dot) node).getChanged()) {
	        			return true;
	        		}
	        }
	   }
	        return false;
	}
	class SaveImageHandler implements EventHandler<ActionEvent>{
        @Override public void handle(ActionEvent event){
            try{
                WritableImage image = center.snapshot(null, null);
                BufferedImage bufferedImage = SwingFXUtils.fromFXImage(image,null);

                ImageIO.write(bufferedImage, "png", new File("capture.png"));

            }catch(IOException e){
            	Alert alert = new Alert(Alert.AlertType.ERROR,"IO-fel " + e.getMessage());
            	alert.showAndWait();
            }
        }
    }
	
	class OpenHandler implements EventHandler<ActionEvent>{
		 @Override public void handle(ActionEvent event){
			 boolean flag1 = true;
			 if (changed()){
	                Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
	                        "Unsaved changes, continue anyway?");
	                Optional<ButtonType> result = alert.showAndWait();
	                if (result.isPresent() && result.get() == ButtonType.CANCEL)
	                    flag1 = false;
	            }
			if(flag1) { 				
				try {
					FileReader reader = new FileReader("europa.graph");
					BufferedReader in = new BufferedReader(reader);
					String line;
					boolean flag = true;

					line = in.readLine();
					resetMap();
					
					image = new Image(line);
					imageView.setImage(image);	   
					center.getChildren().add(imageView);
			        stage.sizeToScene();
					while ((line = in.readLine()) != null) {

						String[] tokens = line.split(";");
						int counter = tokens.length;
						if(flag) {
							for(int i = 0; i < counter; i+=3) {
								String place = tokens[i];
								double x = Double.parseDouble(tokens[i+1]);
								double y = Double.parseDouble(tokens[i+2]);
								
								Dot dot = new Dot(x, y, place);    
								dot.setId(place);
								listGraph.add(dot);		
								Text text = new Text();
								text.setText(place);
				            	text.setX(dot.getCenterX());
				            	text.setY(dot.getCenterY()+35);
				            	text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16)); 
				                center.getChildren().addAll(dot, text);
				                text.setDisable(true);
				                dot.setOnMouseClicked(new ClickDotHandler());
							}//for
							flag = false;
							
						}else if(!flag) {
							
							String fromName = tokens[0];
							String toName = tokens[1];
							String name = tokens[2];
							int weight = Integer.parseInt(tokens[3]);	            	
			            	
							Set<Dot> set = listGraph.getNodes();
							
							
							
							for(Dot dotFrom : set) {
								if(dotFrom.getName().equals(fromName)) {
									for(Dot dotTo : set) {
										if(dotTo.getName().equals(toName)){
											if(listGraph.getEdgeBetween(dotFrom, dotTo) == null) {
												listGraph.connect(dotFrom, dotTo, name, weight);
												Line drawLine = new Line();
												drawLine.setId(name);
												drawLine.setStartX(dotFrom.getCenterX());
												drawLine.setStartY(dotFrom.getCenterY());
												drawLine.setEndX(dotTo.getCenterX());
												drawLine.setEndY(dotTo.getCenterY());
												center.getChildren().add(drawLine);
												drawLine.setDisable(true);
											}//getEdgeBetween
										}//if
									}//dotTo
								}//if
							}//dotFrom	
						}//!flag
	                }//While	
					
	                in.close();
	                reader.close();	 
	                
	                changed = false;
	                for(Node node : center.getChildren())
	                	if(node instanceof Dot) {
	                		((Dot) node).setChanged(false);
	                	}
	                
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		
		 } 
	}
	
	public void resetMap() {
		center.getChildren().clear();
		listGraph = new ListGraph<Dot>();
		b1 = null;
		b2 = null;
	}
	
	class SaveHandler implements EventHandler<ActionEvent>{
        public void handle(ActionEvent event){
            try{
            	

                FileWriter writer = new FileWriter("europa.graph");
                PrintWriter out = new PrintWriter(writer);

                out.println("file:europa.gif");
                
                for(Node node : center.getChildren()){
                	if(node instanceof Dot){               		
                		Dot dot = (Dot)node;
                		out.print(dot.getName() + ";");
                		out.print(dot.getCenterX() + ";");
                		out.print(dot.getCenterY() + ";");               		
                	}
                }
                
                out.println();
                
            	Set<Edge<Dot>> set = new HashSet<Edge<Dot>>();
                for(Node node : center.getChildren()) {
                	if(node instanceof Line) {
                		for(Dot dot : listGraph.getNodes()) {
                			for(Edge<Dot> edge : listGraph.getEdgesFrom(dot)) {   				
            					if(!set.contains(edge)) {
            						out.print(dot.getName());
            	            		out.print(";" + edge.getDestination());
            	            		out.print(";" + edge.getName());
            	            		out.print(";" + edge.getWeight()+ "\n");
            	            		

            	            		set.add(edge);
            					}

                			}//edge
                		}//dot
                	}//if      
                }
                
                out.close();
                writer.close();
                changed = false;
                for(Node node : center.getChildren())
                	if(node instanceof Dot) {
                		((Dot) node).setChanged(false);
                	}
                  
            }catch(IOException e){
                Alert alert = new Alert(Alert.AlertType.ERROR, "IO:fel " + e.getMessage());
                alert.showAndWait();
            }catch(NullPointerException e) {
            	Alert alert = new Alert(Alert.AlertType.ERROR, "NULL fel " + e.getMessage());
                alert.showAndWait();

            }      
        }
    }
	
	class NewShowHandler implements EventHandler<ActionEvent>{
        @Override public void handle(ActionEvent event){
           
        	if(errorShow()) {
	        	Alert alert = new Alert(AlertType.CONFIRMATION);
	        	GridPane pane = new GridPane();
	        	alert.setHeaderText("Connection from " + b1.getName() + " to " + b2.getName());
	        	
	        	TextField name = new TextField(listGraph.getEdgeBetween(b1, b2).getName());
	        	TextField weight = new TextField(""+listGraph.getEdgeBetween(b1, b2).getWeight());
	        	name.setEditable(false);
	        	weight.setEditable(false);

	        	pane.addRow(0, new Label("Name: "), name);
	        	pane.addRow(1, new Label("Time: "), weight);

	        	alert.getDialogPane().setContent(pane);
	        	alert.setTitle("Connection");
	        	alert.showAndWait();
        	}else {
        		return;
        	}
        }
    }
	
	class NewFindHandler implements EventHandler<ActionEvent>{

		@Override public void handle(ActionEvent event) {		
			if(b1 == null || b2 == null) {
	         	Alert alertNoDots = new Alert(AlertType.ERROR, "Two places must be selected!", ButtonType.OK);
					alertNoDots.showAndWait();
				return;
			}
			
			try {
				ArrayList<Edge<Dot>> lista = new ArrayList<Edge<Dot>>(listGraph.getPath(b1, b2));
				Alert alert = new Alert(AlertType.INFORMATION);
				StringBuilder sb = new StringBuilder();
				Iterator<Edge<Dot>> edgesIterator = lista.iterator();
				int totalTid = 0;
				while(edgesIterator.hasNext()) {
					Edge<Dot> current = edgesIterator.next();
					sb.append(current.toString() + "\n");
					totalTid+= current.getWeight();
				}
				sb.append("Total " + totalTid);
				TextArea textArea = new TextArea(sb.toString());
				textArea.setEditable(false);
				textArea.setWrapText(true);
				Pane pane = new Pane();
				pane.getChildren().add(textArea);
				alert.getDialogPane().setContent(pane);
				alert.setHeaderText("The Path from " + b1.getName() + " to " + b2.getName());
				alert.setTitle("Message");
				alert.showAndWait();
			
			}catch(NullPointerException e) {
				Alert alert = new Alert(AlertType.CONFIRMATION, b1.getName() +" and "+  
		         		b2.getName() + "" + " doesn't have a path!", ButtonType.OK);
				alert.showAndWait();
			}	
		}
	}
	
	class NewChangeHandler implements EventHandler<ActionEvent>{
		@Override public void handle(ActionEvent event) {
			if(errorShow()) {
				Alert alert = new Alert(AlertType.CONFIRMATION);
				
				TextField nameField = new TextField(listGraph.getEdgeBetween(b1, b2).getName());
				nameField.setEditable(false);
				TextField timeField = new TextField();
				GridPane grid = new GridPane();
				grid.addRow(0, new Label("Name:"), nameField);
				grid.addRow(1, new Label("Time:"), timeField);
				alert.getDialogPane().setContent(grid);
				Optional<ButtonType> result = alert.showAndWait();
				if(result.get() == ButtonType.OK) {
					listGraph.setConnectionWeight(b1, b2, Integer.parseInt(timeField.getText()));
					changed = true;
				}else {
					return;
				}
			}
		}
	}
	
	private boolean errorShow() {
		if(b1 == null || b2 == null) {
         	Alert alertNoDots = new Alert(AlertType.ERROR, "Two places must be selected!", ButtonType.OK);
				alertNoDots.showAndWait();
			return false;
		}
		if(listGraph.getEdgeBetween(b1, b2) == null) {
         	Alert alertNoConnection = new Alert(AlertType.ERROR, b1.getName() +" and "+  
         		b2.getName() + "" + " doesn't have a connection!", ButtonType.OK);
			alertNoConnection.showAndWait();
			return false;
        }
		return true;
	}
	
	class NewConnectionHandler implements EventHandler<ActionEvent>{
		@Override public void handle(ActionEvent event) {
			if(b1 == null || b2 == null) {
				Alert alert = new Alert(AlertType.ERROR, "Two places must be selected!", ButtonType.OK);
				alert.showAndWait();
			}else {
				AlertWithFields a = new AlertWithFields();
				try {
					if(listGraph.getEdgeBetween(b1, b2) != null) {
						throw new IllegalStateException("Error");
					}
					Pair<String, Integer> connection = a.getAlert(stage);
					if(connection == null) {
						return;
					}else if(connection.getKey().equals("") || connection.getValue() == 0){
						throw new NumberFormatException();
					}
					
					
					listGraph.connect(b1, b2, connection.getKey(), connection.getValue());
					
					Line line = new Line();
					line.setStartX(b1.getCenterX());
					line.setStartY(b1.getCenterY());
					line.setEndX(b2.getCenterX());
					line.setEndY(b2.getCenterY());
					
					line.setId(connection.getKey());
					center.getChildren().add(line);
					line.setDisable(true);
					changed = true;
					
				}catch(IllegalStateException e) {
					Alert alert = new Alert(AlertType.ERROR, b1.getName() + " and " +
							b2.getName() + " already has a connection!", ButtonType.OK);
					alert.showAndWait();
				}catch(NumberFormatException e) {
					Alert b = new Alert(Alert.AlertType.ERROR, "Fel inmatning!");
					b.showAndWait();
					return;
				}
				
			}
		}
	}
	
	class NewPlaceHandler implements EventHandler<ActionEvent>{
        @Override public void handle(ActionEvent event){
            center.setOnMouseClicked(new ClickHandler());
            newPlaceButton.setDisable(true);
            center.setCursor(Cursor.CROSSHAIR);
        }
    }
	
	class ClickHandler implements EventHandler<MouseEvent>{
        @Override public void handle(MouseEvent event){
            double x = event.getX();
            double y = event.getY();
            
            
            TextInputDialog dialog = new TextInputDialog("name");
            dialog.setTitle("Name");
            dialog.setContentText("Name of place:");

            Optional<String> result = dialog.showAndWait();
            if(result.isPresent()){
            	
            	String place = dialog.getEditor().getText();            	
            	Dot dot = new Dot(x, y, place);    
            	listGraph.add(dot);
            	dot.setId(place);
            	dot.setOnMouseClicked(new ClickDotHandler());
            	
            	Text text = new Text();
            	text.setText(place);

            	text.setX(dot.getCenterX());
            	text.setY(dot.getCenterY()+35);
            	text.setFont(Font.font("verdana", FontWeight.BOLD, FontPosture.REGULAR, 16)); 
                center.getChildren().addAll(dot,text);
                center.setOnMouseClicked(null);
                newPlaceButton.setDisable(false);
                center.setCursor(Cursor.DEFAULT);
                text.setDisable(true);
            	changed = true;
            	

            }else {
            	center.setOnMouseClicked(null);
                newPlaceButton.setDisable(false);
                center.setCursor(Cursor.DEFAULT);
            }
        }
    }
	
	class ClickDotHandler implements EventHandler<MouseEvent> {
        @Override public void handle(MouseEvent event) {
        	
            Dot b = (Dot)event.getSource();         
            if (b1 == null && b != b2){
                b1 = b;
                b1.setCovered(false);
                //test();          
            }
            else if (b2 == null && b != b1){
                b2 = b;
                b2.setCovered(false);
                //test();
                

            }else if(b == b1){
            	b1.setCovered(true);
            	b1 = null;
            	//test();
            	
            
            }else if(b == b2){
            	b2.setCovered(true);
            	b2 = null;  
            	//test();
            }
        }
    }

	/*public void test() {
		if(b1 != null && b2 != null) {
            System.out.println("Current B1: " + b1.getName() + ". Current B2: " + b2.getName());
    	}else if(b1 == null && b2 != null) {
            System.out.println("Current B2: " + b2.getName());
        }else if(b2 == null  && b1 != null) {
        	System.out.println("Current B1: " + b1.getName());
        }else {
        	System.out.println("Båda är null");
        }
	}
	*/
	class NewMapHandler implements EventHandler<ActionEvent>{
        @Override public void handle(ActionEvent event){
        	resetMap();
            image = new Image("europa.gif");
            imageView = new ImageView(image);
            center.getChildren().add(imageView);
        	stage.sizeToScene();
        	changed = true;
        }
    }

	public static void main(String[] args) {
		launch(args);
	}
}
