package ensisa.puissance4;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

public class Puissance4Controller {

    private Puissance4Model model = new Puissance4Model();
    private boolean playing = false;
    private boolean againstAI = false;

    private int player = 1;

    private Puissance4IA IA = new Puissance4IA();

    @FXML private MenuButton gamemodeButton;
    @FXML private MenuItem playerVsPlayer;
    @FXML private MenuItem playerVsAI;

    @FXML
    private GridPane grid;
    private Circle[][] circles = new Circle[7][6];

    @FXML
    private Label label;

    public void updateLabel(String text) {
        label.setText(text);
    }

    public void humanTurn(int column) {
        if(playing){
            int move = model.makeMove(column, player);

            if (move == -1) {
                // System.out.println("Move not allowed");
                updateLabel("Move not allowed !");
                return;
            }

            // printGrid();

            updateView();
            checkGameStatus();
            if (!playing){
                return;
            }

            model.setTurn((byte)(model.getTurn() + 1));
            player = player % 2 + 1;

            if(againstAI)
            {
                updateLabel("AI's turn");
                AITurn();
            }
            else
            {
                updateLabel("Player " + player + "'s turn");
            }
        }
    }

    public void AITurn() {
        int column = IA.AIMove(model, player, (byte)6, model.getTurn());
        model.makeMove(column, player);

        // printGrid();

        updateView();
        checkGameStatus();
        if (!playing){
            return;
        }

        model.setTurn((byte)(model.getTurn() + 1));
        player = player % 2 + 1;
        updateLabel("Player's turn");
    }

    @FXML
    public void playerVsAIMenuClick() {
        boolean oldState = againstAI;
        againstAI = true;
        gamemodeButton.setText("Player vs AI");
        label.setText("Player vs AI selected.");

        if (playing && !oldState) {
            newGameButtonClicked();
        }
    }

    @FXML
    public void playerVsPlayerMenuClick() {
        boolean oldState = againstAI;
        againstAI = false;
        gamemodeButton.setText("Player vs Player");
        label.setText("Player vs Player selected.");

        if (playing && oldState) {
            newGameButtonClicked();
        }
    }

    public void initalizeView() {
        updateLabel("Player" + (!againstAI ? " "+player : "") + "'s turn");
        gamemodeButton.setText("Player vs " + (againstAI ? "AI" : "Player"));
        final int rows = 6;
        final int cols = 7;
        final double radius = 40.0;
        final double spacing = 20.0;
        final double buttonHeight = 2 * radius;

        double gridWidth = cols * (2 * radius + spacing);
        double gridHeight = rows * (2 * radius + spacing);

        double startX = (800 - gridWidth) / 2 + radius;
        double startY = (700 - gridHeight) / 2 + radius + buttonHeight + 10; 

        for (int i = 0; i < rows + 1; i++) {
            for (int j = 0; j < cols; j++) {
                if (i == 0) {
                    Button button = new Button();
                    button.setPrefHeight(buttonHeight - 15);
                    button.setPrefWidth(2 * radius + spacing - 15);
                    // set image background
                    Image image = new Image(getClass().getResourceAsStream("img/down-arrow.png"), buttonHeight - 30, (buttonHeight - 30)*572/840, false, false);
                    button.setGraphic(new javafx.scene.image.ImageView(image));

                    final int column = j;
                    button.setOnAction(e -> humanTurn(column));
                    button.setOnMouseEntered(e -> dimBottomCircle(column));
                    button.setOnMouseExited(e -> undimBottomCircle(column));

                    // set Halignment and Valignment
                    GridPane.setHalignment(button, javafx.geometry.HPos.CENTER);
                    GridPane.setValignment(button, javafx.geometry.VPos.CENTER);
                    
                    // getting and applying css
                    grid.getStylesheets().add(getClass().getResource("DropTokenButton.css").toExternalForm());
                    button.getStyleClass().add("drop-token-button");

                    grid.add(button, j, i);
                }
                else {
                    Circle circle = new Circle(radius, Color.WHITE);
                    double centerX = startX + (j * (2 * radius + spacing));
                    double centerY = startY + (i * (2 * radius + spacing));
                    circle.setCenterX(centerX);
                    circle.setCenterY(centerY);

                    // set Halignment and Valignment
                    GridPane.setHalignment(circle, javafx.geometry.HPos.CENTER);
                    GridPane.setValignment(circle, javafx.geometry.VPos.CENTER);

                    // set action on click and hover
                    final int column = j;
                    circle.setOnMouseClicked(e -> humanTurn(column));
                    circle.setOnMouseEntered(e -> dimBottomCircle(column));
                    circle.setOnMouseExited(e -> undimBottomCircle(column));

                    grid.add(circle, j, i);
                    circles[j][i-1] = circle;
                }
            }
        }
    }

    private void dimBottomCircle(int column) {
        for (int i = 5; i >= 0; i--) {
            if (circles[column][i].getFill() == Color.WHITE) {
                circles[column][i].setFill(Color.LIGHTGRAY);
                break;
            }
        }
    }

    private void undimBottomCircle(int column) {
        for (int i = 5; i >= 0; i--) {
            if (circles[column][i].getFill() == Color.LIGHTGRAY) {
                circles[column][i].setFill(Color.WHITE);
                break;
            }
        }
    }

    @FXML
    private Button newGameButton;

    @FXML
    private void newGameButtonClicked() {
        model.initialiseGrid();
        grid.getChildren().clear();
        if (againstAI)
            player = selectPlayer();
        else
            player = 1;
        model.setTurn((byte)0);

        playing = true;
        initalizeView();

        if (player == 2 && againstAI) {
            AITurn();
        }
    }

    private int selectPlayer() {
        return (int)(Math.random() * 2) + 1;
    }

    public void checkGameStatus() {
        if(model.checkVictory(model.getGrid()) != 0){
            // System.out.println("Player " + model.checkVictory(model.getGrid()) + " won!");
            if (!againstAI)
                updateLabel("Player " + model.checkVictory(model.getGrid()) + " wins!");
            else {
                if (model.checkVictory(model.getGrid()) == 1)
                    updateLabel("Player wins!");
                else
                    updateLabel("AI wins!");
            }
            playing = false;
        }
        else if(model.getTurn() >= 41){
            // System.out.println("Draw!");
            updateLabel("Draw!");
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

    public void updateView() {
        int[][] modelGrid = model.getGrid();
        for (int i = 0; i<6; i++){
            for (int j = 0; j<7; j++){
                if(modelGrid[j][i] == 1){
                    circles[j][5-i].setFill(Color.RED);
                }
                else if(modelGrid[j][i] == 2){
                    circles[j][5-i].setFill(Color.YELLOW);
                }
            }
        }
    }
}