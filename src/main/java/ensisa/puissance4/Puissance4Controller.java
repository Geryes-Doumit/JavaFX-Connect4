package ensisa.puissance4;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

public class Puissance4Controller {

    private Puissance4Model model = new Puissance4Model();

    @FXML
    private GridPane grid;
    
    // button 0 to 6
    @FXML
    private Button button0;

    @FXML
    private Button button1;

    @FXML
    private Button button2;

    @FXML
    private Button button3;

    @FXML
    private Button button4;

    @FXML
    private Button button5;

    @FXML
    private Button button6;

    @FXML
    private void button0Clicked() {
        buttonClicked(0);
    }

    @FXML
    private void button1Clicked() {
        buttonClicked(1);
    }

    @FXML
    private void button2Clicked() {
        buttonClicked(2);
    }

    @FXML
    private void button3Clicked() {
        buttonClicked(3);
    }

    @FXML
    private void button4Clicked() {
        buttonClicked(4);
    }

    @FXML
    private void button5Clicked() {
        buttonClicked(5);
    }

    @FXML
    private void button6Clicked() {
        buttonClicked(6);
    }

    public void buttonClicked(int column) {
        model.makeMove(column);

        // print grid for debugging
        int[][] modelGrid = model.getGrid();
        for (int i = 0; i<6; i++){
            System.out.print("|");
            for (int j = 0; j<7; j++){
                System.out.print(modelGrid[j][5-i] + "|" );
            }
            System.out.println();
        }
        System.out.println("-------");

        // updateView();
    }

    public void updateView() {
        int[][] modelGrid = model.getGrid();
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++) {
                switch (modelGrid[i][j]) {
                    case 0:
                        break;
                        
                    case 1:
                        Circle circle = new Circle(40);
                        circle.setStyle("-fx-fill: red");
                        break;
                    
                    case 2:
                        Circle circle2 = new Circle(40);
                        circle2.setStyle("-fx-fill: yellow");
                        break;

                    default:
                        break;
                }
            }
        }
    }
}