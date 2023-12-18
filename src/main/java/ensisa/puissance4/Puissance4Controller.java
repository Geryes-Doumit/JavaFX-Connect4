package ensisa.puissance4;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.shape.Circle;

public class Puissance4Controller {

    private Puissance4Model model = new Puissance4Model();
    private boolean playing = true;
    private boolean againstAI = true;

    private Puissance4IA IA = new Puissance4IA();

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
        humanTurn(0);
    }

    @FXML
    private void button1Clicked() {
        humanTurn(1);
    }

    @FXML
    private void button2Clicked() {
        humanTurn(2);
    }

    @FXML
    private void button3Clicked() {
        humanTurn(3);
    }

    @FXML
    private void button4Clicked() {
        humanTurn(4);
    }

    @FXML
    private void button5Clicked() {
        humanTurn(5);
    }

    @FXML
    private void button6Clicked() {
        humanTurn(6);
    }

    public void humanTurn(int column) {
        if(playing){
            model.makeMove(column);

            printGrid();

            // updateView();
            checkGameStatus();
            if (!playing){
                return;
            }

            model.setTurn((byte)(model.getTurn() + 1));
            model.player = model.player % 2 + 1;

            if(againstAI)
            {
                AITurn();
            }
        }
    }

    public void AITurn() {
        int column = IA.AIMove(model, model.player, (byte)6, model.getTurn());
        model.makeMove(column);

        printGrid();

        // updateView();
        checkGameStatus();
        if (!playing){
            return;
        }

        model.setTurn((byte)(model.getTurn() + 1));
        model.player = model.player % 2 + 1;
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

    public void checkGameStatus() {
        if(model.checkVictory(model.getGrid()) != 0){
            System.out.println("Player " + model.checkVictory(model.getGrid()) + " won!");
            playing = false;
        }
        else if(model.getTurn() == 42){
            System.out.println("Draw!");
            playing = false;
        }
    }

    public void printGrid(){
        int[][] modelGrid = model.getGrid();
        for (int i = 0; i<6; i++){
            System.out.print("|");
            for (int j = 0; j<7; j++){
                System.out.print(modelGrid[j][5-i] + "|" );
            }
            System.out.println();
        }
        System.out.println("-------");
    }
}