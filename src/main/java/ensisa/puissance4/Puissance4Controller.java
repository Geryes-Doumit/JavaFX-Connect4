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


    @FXML private Button newGameButton;
    @FXML private MenuButton gamemodeButton;
    @FXML private MenuItem playerVsPlayer;
    @FXML private MenuItem playerVsAI;

    @FXML
    private GridPane grid;
    private Circle[][] circles = new Circle[7][6];

    @FXML
    private Label label;

    // Update the label with information to give to the player
    public void updateLabel(String text, boolean isEnd) {
        label.setText(text);
        if(isEnd){
            label.setTextFill(Color.rgb(208, 89, 97));
        }
        else {
            label.setTextFill(Color.WHITE);
        }
    }

    // Switch from player 1 to player 2 and vice versa
    public void switchPlayer() {
        player = player % 2 + 1;
    }

    // Called when a button or a circle is clicked
    public void humanTurn(int column) {
        if(playing){
            int move = model.makeMove(column, player); // update the grid in the model

            if (move == -1) {
                updateLabel("Move not allowed !", true);
                return;
            }

            // animates the token and continues the game when the animation ends
            animateToken(column, player);
        }
    }

    // Called when the AI has to play
    public void AITurn() {
        int column = IA.AIMove(model, player, (byte)6, model.getTurn());
        model.makeMove(column, player);

        // animates the token and continues the game when the animation ends
        animateToken(column, player);
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

    // Called when the view is initialized, used for a new game
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

    // used to update the view after a move
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

    @FXML // Handles the click on the menu button
    public void playerVsAIMenuClick() {
        boolean oldState = againstAI;
        againstAI = true;
        gamemodeButton.setText("Player vs AI");
        label.setText("Player vs AI.");

        if (playing && !oldState) {
            newGameButtonClicked();
        }
    }

    @FXML // Handles the click on the menu button
    public void playerVsPlayerMenuClick() {
        boolean oldState = againstAI;
        againstAI = false;
        gamemodeButton.setText("Player vs Player");
        label.setText("Player vs Player.");

        if (playing && oldState) {
            newGameButtonClicked();
        }
    }

    // stops the hover effect on the circles, used when the game ends
    public void stopCircleHoverEffects() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++)
                circles[i][j].setOnMouseEntered(null);
        }
    }

    // dim the bottom circle of a column
    private void dimBottomCircle(int column) {
        for (int i = 5; i >= 0; i--) {
            if (circles[column][i].getFill() == emptyColor) {
                circles[column][i].setFill(dimColor);
                break;
            }
        }
    }

    // undim the bottom circle of a column
    private void undimBottomCircle(int column) {
        for (int i = 5; i >= 0; i--) {
            if (circles[column][i].getFill() == dimColor) {
                circles[column][i].setFill(emptyColor);
                break;
            }
        }
    }

    @FXML // Handles the click on the new game button
    private void newGameButtonClicked() {
        model.initialiseGrid();
        grid.getChildren().clear();
        if (againstAI)
            player = selectPlayer(); // select at random who starts, AI or player
        else
            player = 1;
        model.setTurn((byte)0);

        playing = true;
        initalizeView();

        if (player == 2 && againstAI) {
            AITurn();
        }
    }

    // select at random who starts, AI or player
    private int selectPlayer() {
        return (int)(Math.random() * 2) + 1;
    }

    // used to check if the game is over
    public void checkGameStatus() {
        if(model.checkVictory(model.getGrid()) != 0){
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
            updateLabel("Draw!", true);
            playing = false;
        }
    }

    // used for debugging
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