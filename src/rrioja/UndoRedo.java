package rrioja;

import java.io.PrintStream;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import rrioja.commands.*;
import rrioja.replay.ReplayConversation;
 
public class UndoRedo extends Application {
	ReplayConversation replay = new ReplayConversation(new ResetCommand());
	boolean skipped = false;
    
    @Override
    public void start(Stage primaryStage) {
    	Label checkpoint = new Label("0");
    	Label history = new Label(checkpoint.getText());
    	Label apparentHistory = new Label(checkpoint.getText());
    	Region spacer = new Region();
    	Label result = new Label("0");
    	
    	history.setStyle("-fx-font-size: 20px;");
    	apparentHistory.setStyle("-fx-font-size: 20px;");
    	result.setStyle("-fx-font-size: 20px;");
    	
    	System.setOut(new PrintStream(System.out) {
    		public void print(String s) {
    			if(s.equals("skipped")) {
    				skipped = true;
    				super.print(s);
    				return;
    			}
    			
    			if(skipped) {
    				history.setText((history.getText() + "\n" + s).trim());
    				skipped = false;
    				super.print(s);
    				return;
    			} else {
    				history.setText((history.getText() + "\n" + s).trim());
    				apparentHistory.setText((apparentHistory.getText() + "\n" + s).trim());
    			}
    			
    			switch(s) {
					case "+1":
						result.setText((Integer.parseInt(result.getText()) + 1) + "");
						break;
    				case "x2":
    					result.setText((Integer.parseInt(result.getText()) * 2) + "");
    					break;
    				case "/3":
    					result.setText((Integer.parseInt(result.getText()) / 3) + "");
    					break;
    				case "reset":
    	    			history.setText(checkpoint.getText());
    	    			apparentHistory.setText(checkpoint.getText());
    	    			result.setText(checkpoint.getText());
    	    			break;
    				case "checkpoint":
    	    			checkpoint.setText(Integer.parseInt(result.getText()) + "");
    	    			replay = new ReplayConversation(new ResetCommand());
    	    			history.setText(checkpoint.getText());
    	    			break;
    			}

    			super.print(s);
    		}
    	});
        
        Button btnPlus1 = new Button();
        btnPlus1.setText("+ 1");
        btnPlus1.setStyle("-fx-font-size: 20px;");
        btnPlus1.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	replay.exec(new Plus1Command());
            }
        });
    	
        Button btnMultiply2 = new Button();
        btnMultiply2.setText("x 2");
        btnMultiply2.setStyle("-fx-font-size: 20px;");
        btnMultiply2.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	replay.exec(new Multiply2Command());
            }
        });
        
        Button btnDivide3 = new Button();
        btnDivide3.setText("/ 3");
        btnDivide3.setStyle("-fx-font-size: 20px;");
        btnDivide3.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	replay.exec(new Divide3Command());
            }
        });
        
        Button btnUndo = new Button();
        btnUndo.setText("Undo");
        btnUndo.setStyle("-fx-font-size: 20px;");
        btnUndo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	replay.undo();
            }
        });
        
        Button btnRedo = new Button();
        btnRedo.setText("Redo");
        btnRedo.setStyle("-fx-font-size: 20px;");
        btnRedo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	replay.redo();
            }
        });
        
        Button btnCheckpoint = new Button();
        btnCheckpoint.setText("Checkpoint");
        btnCheckpoint.setStyle("-fx-font-size: 20px;");
        btnCheckpoint.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	replay.exec(new CheckpointCommand());
            }
        });
        
        Region spacer2 = new Region();
        Region spacer4 = new Region();
        
        VBox buttons = new VBox(10, btnPlus1, btnMultiply2, btnDivide3, spacer2, btnUndo, btnRedo/*, spacer4, btnCheckpoint*/);
        VBox.setVgrow(spacer2, Priority.ALWAYS);
        VBox.setVgrow(spacer4, Priority.ALWAYS);
        
        Label title = new Label("Event store:");
        title.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        VBox eventStore = new VBox(title, history, spacer);
        VBox.setVgrow(spacer, Priority.ALWAYS);
        
        Label title3 = new Label("Apparent event store:");
        title3.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");
        
        Label title4 = new Label("Result:");
        title4.setStyle("-fx-font-size: 20px; -fx-font-weight: bold;");

    	Region spacer6 = new Region();
        
        VBox apparentEventStore = new VBox(title3, apparentHistory, spacer6, title4, result);
        VBox.setVgrow(spacer6, Priority.ALWAYS);
        
        Region spacer3 = new Region();
        Region spacer5 = new Region();
        
        HBox root = new HBox(buttons, spacer3, eventStore, spacer5, apparentEventStore);
        HBox.setHgrow(spacer3, Priority.ALWAYS);
        HBox.setHgrow(spacer5, Priority.ALWAYS);
        
        root.setPadding(new Insets(10));

        Scene scene = new Scene(root, 600, 600);

        primaryStage.setTitle("Undo Redo Demo");
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}