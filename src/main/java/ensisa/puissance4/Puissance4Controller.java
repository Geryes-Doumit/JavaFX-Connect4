package ensisa.puissance4;

import javafx.animation.PauseTransition;
import javafx.animation.TranslateTransition;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.util.Duration;
// import Node class from javafx


public class Puissance4Controller {
    private static Color dimColor = Color.rgb(117, 117, 117);
    private static Color emptyColor = Color.rgb(182, 180, 175);
    private static Color redColor = Color.rgb(187, 1, 11);
    private static Color yellowColor = Color.rgb(249, 200, 14);
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

    public void updateLabel(String text, boolean isEnd) {
        label.setText(text);
        if(isEnd){
            label.setTextFill(Color.rgb(208, 89, 97));
        }
        else {
            label.setTextFill(Color.WHITE);
        }
    }

    public void humanTurn(int column) {
        if(playing){
            int move = model.makeMove(column, player); // update the grid in the model

            if (move == -1) {
                // System.out.println("Move not allowed");
                updateLabel("Move not allowed !", true);
                return;
            }

            animateToken(column, player); 
            // animate and update continues the game when the animation ends
            
        }
    }

    public void switchPlayer() {
        player = player % 2 + 1;
    }

    public void AITurn() {
        int column = IA.AIMove(model, player, (byte)6, model.getTurn());
        model.makeMove(column, player);

        // printGrid();

        animateToken(column, player); 
        // animate and update continues the game when the animation ends
    }

    @FXML
    public void playerVsAIMenuClick() {
        boolean oldState = againstAI;
        againstAI = true;
        gamemodeButton.setText("Player vs AI");
        label.setText("Player vs AI.");

        if (playing && !oldState) {
            newGameButtonClicked();
        }
    }

    @FXML
    public void playerVsPlayerMenuClick() {
        boolean oldState = againstAI;
        againstAI = false;
        gamemodeButton.setText("Player vs Player");
        label.setText("Player vs Player.");

        if (playing && oldState) {
            newGameButtonClicked();
        }
    }

    public void animateToken(int column, int player) {
        // wait for the animation to finish
        playing = false;

        // calculate pane width and height
        int paneWidth = (int) grid.getWidth()/7;
        int paneHeight = (int) grid.getHeight()/6;

        Circle circle = new Circle(40, player == 1 ? redColor : yellowColor);
        double centerX = (column * paneWidth);
        double centerY = 500;
        for (int i = 5; i >= 0; i--) {
            if (circles[column][i].getFill() == emptyColor 
                || circles[column][i].getFill() == dimColor) {
                centerY = circles[column][i].getCenterY() - 180 - i*15;
                break;
            }
        }
        circle.setCenterX(0);
        circle.setCenterY(0);
        // set Halignment and Valignment
        GridPane.setHalignment(circle, javafx.geometry.HPos.CENTER);
        GridPane.setValignment(circle, javafx.geometry.VPos.CENTER);

        grid.getChildren().add(circle);

        TranslateTransition transition = new TranslateTransition();
        transition.setDuration(Duration.seconds(0.5));
        transition.setNode(circle);
        transition.setFromX(centerX);
        transition.setToX(centerX);
        transition.setFromY(paneHeight - paneHeight/2);
        transition.setToY(centerY);
        transition.play();

        transition.setOnFinished(e -> {
            playing = true;
            new PauseTransition(Duration.seconds(0.5)).play();
            grid.getChildren().remove(circle);
            updateView();
            checkGameStatus();

            if (!playing) return;

            model.setTurn((byte)(model.getTurn() + 1));
            switchPlayer();

            if(againstAI && this.player == 2)
            {
                updateLabel("Player 2's turn (AI)", false);
                AITurn();
            }
            else
            {
                updateLabel("Player " + this.player + "'s turn", false);
            }
        });
    }

    public void initalizeView() {
        updateLabel("Player" + (!againstAI ? " "+player : "") + "'s turn", false);
        gamemodeButton.setText("Player vs " + (againstAI ? "AI" : "Player"));
        final int rows = 6;
        final int cols = 7;
        final double radius = 40.0;
        final double spacing = 20.0;
        final double buttonHeight = 2 * radius;

        double gridWidth = cols * (2 * radius + spacing);
        double gridHeight = rows * (2 * radius + spacing);

        double startX = (700 - gridWidth) / 2 + radius;
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
                    Circle circle = new Circle(radius, emptyColor);
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

    public void stopCircleHoverEffects() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++)
                circles[i][j].setOnMouseEntered(null);
        }
    }

    private void dimBottomCircle(int column) {
        for (int i = 5; i >= 0; i--) {
            if (circles[column][i].getFill() == emptyColor) {
                circles[column][i].setFill(dimColor);
                break;
            }
        }
    }

    private void undimBottomCircle(int column) {
        for (int i = 5; i >= 0; i--) {
            if (circles[column][i].getFill() == dimColor) {
                circles[column][i].setFill(emptyColor);
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
            strokeWinningTokens();
            // System.out.println("Player " + model.checkVictory(model.getGrid()) + " won!");
            if (!againstAI)
                updateLabel("Player " + model.checkVictory(model.getGrid()) + " wins!", true);
            else {
                if (model.checkVictory(model.getGrid()) == 1)
                    updateLabel("Player wins!", true);
                else
                    updateLabel("AI wins!", true);
            }
            playing = false;
            stopCircleHoverEffects();
        }
        else if(model.getTurn() >= 41){
            // System.out.println("Draw!");
            updateLabel("Draw!", true);
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
                    circles[j][5-i].setFill(redColor);
                }
                else if(modelGrid[j][i] == 2){
                    circles[j][5-i].setFill(yellowColor);
                }
            }
        }
    }

    public void strokeWinningTokens(){
        int[][] winningTokens = model.getWinningTokens();
        for (int i = 0; i<4; i++){
            circles[winningTokens[i][0]][5 - winningTokens[i][1]].setStroke(Color.web("#768bad"));
            circles[winningTokens[i][0]][5 - winningTokens[i][1]].setStrokeWidth(5);
        }
    }
}