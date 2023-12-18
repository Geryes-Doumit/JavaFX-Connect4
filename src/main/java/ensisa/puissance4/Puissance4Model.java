package ensisa.puissance4;

public class Puissance4Model {

    private int[][] grid = new int[7][7];
    public int player = 1;

    public Puissance4Model() {
        for (int i = 0; i < 7; i++) {
            for (int j = 0; j < 7; j++)
                grid[i][j] = 0;
        }
    }

    public int[][] getGrid() {
        return grid;
    }

    public int makeMove(int column, int player) {
        for (int row = 6; row >= 0; row--) {
            if (grid[row][column] == 0) {
                grid[row][column] = player;
                player = player % 2 + 1;
                return 1; // move successful
            }
        }
        return -1; // move not allowed
    }
}