import javafx.application.*;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import javafx.stage.*;
import javafx.scene.*;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.event.*;
import javafx.geometry.*;

public class Main extends Application {
	
	// Global application flags
	boolean valid_array = true;
	boolean first_start = true;
	private String array;
	private int array_no = 0;
	private int last_index = 0;
	private StringBuilder history_temp = new StringBuilder();
	DataReader data;
	Automaton automaton = new Automaton();
	final FileChooser fileChooser = new FileChooser();
	
	public void start(Stage primaryStage) {
		System.out.println("START");
		
		// Buttons declaration
		
		Button btn_start = new Button();
		btn_start.setText("START");
		btn_start.setDisable(true);
		
		Button btn_next_array = new Button();
		btn_next_array.setText("Next array");
        
        Button btn_next_element = new Button();
        btn_next_element.setText("Next element");
        
        Button btn_file = new Button();
        btn_file.setText("Choose a file");
        
        
        // info elements
        
        final Text cur_state_text = new Text("Current state: ");
        final Text prev_element_text = new Text("Previous element: ");
        final Text next_element_text = new Text("Next element: ");
        
        Text cur_state = new Text("-");
        cur_state.setFont(Font.font ("Verdana", FontWeight.BOLD, 12));
        Text prev_element = new Text("-");
        prev_element.setFont(Font.font ("Verdana", FontWeight.BOLD, 12));
        Text next_element = new Text("-");
        next_element.setFont(Font.font ("Verdana", FontWeight.BOLD, 12));

        // display array elements
        
        final Text curr_array_text = new Text("Currently analyzed array:");
        Text curr_array = new Text("Choose the file");
        curr_array.setFont(Font.font ("Verdana", FontWeight.BOLD, 20));
        
        // history elements
        Text history_text = new Text("History:");
        history_text.setFont(Font.font ("Verdana", FontWeight.BOLD, 12));
        TextArea history = new TextArea();
        history.setEditable(false);
        history.setPrefRowCount(8);
        history.setWrapText(true);
        history.setPrefWidth(200);
        history.setText("No arrays analyzed yet!");
        
        
        // general stackpane
		
        StackPane root = new StackPane();
        	
        	
        	// button row
	        HBox hb = new HBox();
	        hb.setSpacing(15);
	        hb.setAlignment(Pos.CENTER);
	        hb.getChildren().addAll(btn_file,btn_start);
	          	
        	// info row
        	GridPane gridpane = new GridPane();
            gridpane.setPadding(new Insets(10));
            gridpane.setHgap(10);
            gridpane.setVgap(10);
            gridpane.setAlignment(Pos.CENTER);
            
            gridpane.add(prev_element_text, 0, 0);
            gridpane.add(cur_state_text, 1, 0);
            gridpane.add(next_element_text, 2, 0);
            gridpane.add(prev_element, 0, 1);
            gridpane.add(cur_state, 1, 1);
            gridpane.add(next_element, 2, 1);
            
            
	        // general vbox
	        VBox vb = new VBox();
        	vb.setSpacing(30);
        	vb.setAlignment(Pos.CENTER);
        	vb.setPadding(new Insets(20));
        	vb.getChildren().addAll(curr_array,hb);
            
        root.getChildren().addAll(vb);	
        
        
        
        // button handlers initialization
        
        btn_start.setOnAction(new EventHandler<ActionEvent>() {
        	 
            @Override
            public void handle(ActionEvent event) {
            	// first start
            		hb.getChildren().clear();
            		hb.getChildren().addAll(btn_next_array, btn_next_element);
            		vb.getChildren().clear();
            		vb.getChildren().addAll(curr_array_text, curr_array, gridpane,hb,history_text,history);
            		
            		btn_next_array.fire();

            }
        });
        
        btn_next_element.setOnAction(new EventHandler<ActionEvent>() {
        	 
            @Override
            public void handle(ActionEvent event) {

                Integer element = Integer.valueOf(array.charAt(last_index)) - 48;
                String state = automaton.getState(element);
                cur_state.setText(state);
                history_temp.append(", "+state);
                prev_element.setText(String.valueOf(element));
                last_index++;
                if (last_index == array.length()) {
                	btn_next_element.setDisable(true);
                	next_element.setText("-");
                }
                else {
                	element = Integer.valueOf(array.charAt(last_index)) - 48;
                	next_element.setText(String.valueOf(element));
                }
                
            }
        });
        
        btn_file.setOnAction(new EventHandler<ActionEvent>() {
       	 
            @Override
            public void handle(ActionEvent event) {
            	File file = fileChooser.showOpenDialog(primaryStage);
                if (file != null) {
                	data = new DataReader(file.getPath());
                	btn_start.setDisable(false);
                	curr_array.setText("Click the start button!");
                	history.setText("Detected "+data.getLen()+ " arrays separeted by #!\n");
                }
                else {
                	curr_array.setText("Choose another one!");
                }
                
            }
        });
        
        btn_next_array.setOnAction(new EventHandler<ActionEvent>() {
       	 
            @Override
            public void handle(ActionEvent event) {
            	if (!first_start){
            		if (!valid_array) {
            			history.appendText("Array nr "+String.valueOf(array_no)+", elements: "+array+" is not valid!");
            			valid_array = true;
            		}
            		else {
	            		history.appendText("Array nr "+String.valueOf(array_no)+", elements: "+array+", history of states: "+history_temp.toString());
	            		if (last_index != array.length()) {
	            			history.appendText(" -SKIPPED!");
	            		}
            		}
            		history.appendText("\n");
            	}
            		first_start = false;
            		automaton = new Automaton();
            		history_temp = new StringBuilder();
            		array_no ++;
            		last_index = 0;

	                array = data.getElement();
	                if (array == null) {
	                	System.out.println("END");
	                	vb.getChildren().clear();
	                	curr_array.setText("No data left!");
	            		vb.getChildren().addAll(curr_array,history_text,history);
	                } else {
	                	if (checkData(array)) {
	                		curr_array.setText(array);
			        		cur_state.setText("Q0");
			        		prev_element.setText("-");
			        		next_element.setText(String.valueOf(array.charAt(last_index)));
			        		history_temp.append("Q0");
			        		btn_next_element.setDisable(false);
	                	} else {
	                		curr_array.setText(array+" - NOT VALID!");
			        		cur_state.setText("-");
			        		prev_element.setText("-");
			        		next_element.setText("-");
			        		valid_array = false;
			        		btn_next_element.setDisable(true);
	                	}
	                }

            }
        });
        
        /// Stage initializtaion
        
        Scene scene = new Scene(root, 600, 500);
        primaryStage.setTitle("NFA - LAB 2");
        primaryStage.setScene(scene);
        primaryStage.show();
        
	}
	
	public static void main(String[] args) {
		launch(args);
	}
	
	private static boolean checkData(String s) {
		boolean valid = true;
		Set <Character> acceptable = new HashSet<Character>();
		for(int i = 0; i<10 ;i++) {
			acceptable.add((Character.valueOf((char) (i+48))));
		}
		for (int i = 0; i<s.length(); i++) {
			char c = s.charAt(i);
			if (acceptable.contains(c)) {
				continue;
			} else {
				valid = false;
			}
		}
		return valid;
	}
	
}

