package ensisa.puissance4;

import java.util.ArrayList;
import java.util.List;

public class Puissance4Model {

    private int[][] grid = new int[7][6];
    public int[][] getGrid() {
        return grid;
    }
    //public int player = 1;

    private byte turn = 0;
    public byte getTurn() {
        return turn;
    }
    public void setTurn(byte turn) {
        this.turn = turn;
    }


    public Puissance4Model() {
        initialiseGrid();
    }

    public void initialiseGrid() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 6; j++)
                grid[i][j] = 0;
        }
    }

    public int makeMove(int column, int player) {
        for (int row = 0; row < 6; row++) {
            if (grid[column][row] == 0) {
                grid[column][row] = player;
                //player = player % 2 + 1;
                return 1; // move successful
            }
        }
        return -1; // move not allowed
    }

    public void undoMove(int column) {
        for (int row = 5; row >= 0; row--) {
            if (grid[column][row] != 0) {
                grid[column][row] = 0;
                return;
            }
        }
    }

    public List<Integer> getPossibleMoves(int[][] gridEvaluate){
        List<Integer> possibleMoves = new ArrayList<Integer>();
        for (int i = 0; i < 7; i++) {
            if (gridEvaluate[i][5] == 0) {
                possibleMoves.add(i);
            }
        }
        return possibleMoves;
    }

    public int checkVictory(int[][] gridEvaluate){
        // Check horizontal alignment
        for (int line = 0; line <6; line++){
            for (int column_shift = 0; column_shift<4; column_shift ++){
                if(gridEvaluate[column_shift][line] != 0 && gridEvaluate[column_shift][line] == gridEvaluate[column_shift + 1][line] && gridEvaluate[column_shift][line] == gridEvaluate[column_shift + 2][line] && gridEvaluate[column_shift][line] == gridEvaluate[column_shift + 3][line]){
                    return gridEvaluate[column_shift][line];
                }
            }
        }

        for (int column = 0; column <7; column++){
            for (int line_shift = 0; line_shift<3; line_shift ++){
                if(gridEvaluate[column][line_shift] != 0 && gridEvaluate[column][line_shift] == gridEvaluate[column][line_shift + 1] && gridEvaluate[column][line_shift] == gridEvaluate[column][line_shift + 2] && gridEvaluate[column][line_shift] == gridEvaluate[column][line_shift + 3]){
                    return gridEvaluate[column][line_shift];
                }
            }
        }

        for (int column_shift = 0; column_shift<4; column_shift++){
            for (int line_shift = 0; line_shift<3; line_shift++){
                if (gridEvaluate[column_shift][line_shift] != 0 && gridEvaluate[column_shift][line_shift] == gridEvaluate[column_shift+1][line_shift+1] && gridEvaluate[column_shift][line_shift] == gridEvaluate[column_shift+2][line_shift+2] && gridEvaluate[column_shift][line_shift] == gridEvaluate[column_shift+3][line_shift+3]){
                    return gridEvaluate[column_shift][line_shift];
                } else if (gridEvaluate[column_shift][5 - line_shift] != 0 && gridEvaluate[column_shift][5 - line_shift] == gridEvaluate[column_shift+1][4 - line_shift] && gridEvaluate[column_shift][5 - line_shift] == gridEvaluate[column_shift+2][3 - line_shift] && gridEvaluate[column_shift][5 - line_shift] == gridEvaluate[column_shift+3][2 - line_shift]) {
                    return gridEvaluate[column_shift][5 - line_shift];
                }
            }
        }

        return 0;
    }
}