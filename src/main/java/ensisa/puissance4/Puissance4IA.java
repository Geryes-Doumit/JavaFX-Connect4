package ensisa.puissance4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Puissance4IA {
    public Puissance4Model model;
    public int AIMove(Puissance4Model model, int player, byte depth, byte turn){
        this.model = model;
        int[][] grid = model.getGrid();
        List<Integer> possibleMoves = model.getPossibleMoves(grid);

        int bestMove = possibleMoves.get(0);
        int bestScore = Integer.MIN_VALUE;
        int alpha = Integer.MIN_VALUE;
        int beta = Integer.MAX_VALUE;

        for (int move : possibleMoves){
            model.makeMove(move, player);
            int score = Min(player, (byte)(depth - 1), turn, alpha, beta);
            model.undoMove(move);
            if (bestScore < score){
                bestScore = score;
                bestMove = move;
            }
            alpha = Math.max(alpha, bestScore);
        }
        return bestMove;


    }

    public int Min(int player, byte depth, byte turn, int alpha, int beta){
        int[][] grid = model.getGrid();
        if (depth == 0 || model.checkVictory(grid) != 0 || turn >= 41){
            return evaluate(grid, player);
        }

        List<Integer> possibleMoves = model.getPossibleMoves(grid);
        turn = (byte)(turn + 1);
        int bestScore = Integer.MAX_VALUE;
        int opponent = player % 2 + 1;
        for (int move : possibleMoves){
            model.makeMove(move, opponent);
            int score = Max(player, (byte)(depth - 1), turn, alpha, beta);
            bestScore = Math.min(bestScore, score);
            model.undoMove(move);
            if (bestScore <= alpha){
                return bestScore;
            }
            beta = Math.min(beta, bestScore);
        }
        return bestScore;
    }

    public int Max(int player, byte depth, byte turn, int alpha, int beta){
        int[][] grid = model.getGrid();
        if (depth == 0 || model.checkVictory(grid) != 0 || turn == 42){
            return evaluate(grid, player);
        }

        List<Integer> possibleMoves = model.getPossibleMoves(grid);
        turn = (byte)(turn + 1);
        int bestScore = Integer.MIN_VALUE;
        for (int move : possibleMoves){
            model.makeMove(move, player);
            int score = Min(player, (byte)(depth - 1), turn, alpha, beta);
            bestScore = Math.max(bestScore, score);
            model.undoMove(move);
            if (bestScore >= beta){
                return bestScore;
            }
            alpha = Math.max(alpha, bestScore);
        }
        return bestScore;
    }

    public int evaluate(int[][] grid, int player){
        int score = 0;
        int opponent = player % 2 + 1;
        int victory = model.checkVictory(grid);
        if (victory == player){
            return 100000;
        } else if (victory == opponent){
            return -100000;
        }

        score += checkForThreat(player);
        score -= checkForThreat(opponent);

        return score;
    }

    public int checkForThreat(int player) {
        int[][] grid = model.getGrid();
        int threat = 0;
        for (int col = 0; col < 7; col++) {
            for (int row = 0; row < 6; row++) {
                if (grid[col][row] == player) {
                    threat += checkCompleteInDirection(col, row, 1, 0, player);
                    threat += checkCompleteInDirection(col, row, -1, 0, player);
                    threat += checkCompleteInDirection(col, row, 0, 1, player);
                    threat += checkCompleteInDirection(col, row, 0, -1, player);
                }
            }
        }
        return threat;
    }

    private int checkCompleteInDirection(int col, int row, int deltaX, int deltaY, int player) {
        int[][] grid = model.getGrid();
        ArrayList<Integer> subArray = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            int newCol = col + i * deltaX;
            int newRow = row + i * deltaY;
            if (newCol >= 0 && newCol < 7 && newRow >= 0 && newRow < 6) {
                subArray.add(grid[newCol][newRow]);
            } else {
                return 0;
            }
        }

        int playerCount = Collections.frequency(subArray, player);
        int emptyCount = Collections.frequency(subArray, 0);
        if (playerCount == 3 && emptyCount == 1) {
            return 1;
        }

        return 0;
    }

}
