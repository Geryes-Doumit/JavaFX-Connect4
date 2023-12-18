package ensisa.puissance4;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Puissance4Controller {

    private Puissance4Model model = new Puissance4Model();
    private boolean playing = true;
    private boolean againstAI = true;

    private Puissance4IA IA = new Puissance4IA();

    @FXML
    private GridPane grid;
        private Circle[][] circles = new Circle[7][6];

    
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

    public void initalizeView() {
        final int rows = 6;
        final int cols = 7;
        final double radius = 40.0;
        final double spacing = 20.0;
        final double buttonHeight = 2 * radius;

        double gridWidth = cols * (2 * radius + spacing);
        double gridHeight = rows * (2 * radius + spacing);

        double startX = (720 - gridWidth) / 2 + radius;
        double startY = (620 - gridHeight) / 2 + radius + buttonHeight + 10; 

        
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0) {
                    Button button = new Button();
                    button.setPrefHeight(buttonHeight);
                    button.setPrefWidth(2 * radius + spacing);
                    button.setText("Col " + j);
                    final int column = j;
                    button.setOnAction(e -> humanTurn(column));
                    grid.add(button, j, i);
                }
                Circle circle = new Circle(radius, Color.WHITE);
                double centerX = startX + (j * (2 * radius + spacing));
                double centerY = startY + (i * (2 * radius + spacing));
                circle.setCenterX(centerX);
                circle.setCenterY(centerY);
                grid.getChildren().add(circle);
                circles[j][i] = circle;
            }
        }
    }

    @FXML
    private Button newGameButton;

    @FXML
    private void newGameButtonClicked() {
        model.initialiseGrid();
        if (grid == null) {
            System.out.println("grid is null");
            return;
        }
        grid.getChildren().clear();
        initalizeView();
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