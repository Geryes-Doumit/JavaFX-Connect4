package ensisa.puissance4;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Puissance4IA {
    public Puissance4Model model;
    public int AIMove(Puissance4Model model, int player, byte depth, byte turn){
        this.model = model;
        int[][] grid = model.getGrid();
        List<Integer> possibleMoves = model.getPossibleMoves(grid);

        /*
        Random random = new Random();
        return possibleMoves.get(random.nextInt(possibleMoves.size()));*/

        int bestMove = possibleMoves.get(0);
        int bestScore = -100000000;
        int alpha = -100000000;
        int beta = 100000000;

        for (int move : possibleMoves){
            model.makeMove(move, player);
            int score = AlphaBetaMin(player % 2 + 1, (byte)(depth - 1), alpha, beta, turn);
            model.undoMove(move);
            if (score > bestScore){
                bestScore = score;
                bestMove = move;
            }
            alpha = (byte)Math.max(alpha, score);
        }
        return bestMove;


    }

    public int AlphaBetaMin(int player, byte depth, int alpha, int beta, byte turn){
        int[][] grid = model.getGrid();
        if (depth == 0 || model.checkVictory(grid) != 0 || turn == 42){
            return evaluate(grid, player);
        }

        List<Integer> possibleMoves = model.getPossibleMoves(grid);
        turn = (byte)(turn + 1);
        for (int move : possibleMoves){
            model.makeMove(move, player);
            int score = AlphaBetaMax(player % 2 + 1, (byte)(depth - 1), alpha, beta, turn);
            model.undoMove(move);
            beta = (byte)Math.min(beta, score);
            if (beta <= alpha){
                return beta;
            }
        }
        return beta;
    }

    public int AlphaBetaMax(int player, byte depth, int alpha, int beta, byte turn){
        int[][] grid = model.getGrid();
        if (depth == 0 || model.checkVictory(grid) != 0 || turn == 42){
            return evaluate(grid, player);
        }

        List<Integer> possibleMoves = model.getPossibleMoves(grid);
        turn = (byte)(turn + 1);
        for (int move : possibleMoves){
            model.makeMove(move, player);
            int score = AlphaBetaMin(player, (byte)(depth - 1), alpha, beta, turn);
            model.undoMove(move);
            alpha = (byte)Math.max(alpha, score);
            if (beta <= alpha){
                return alpha;
            }
        }
        return alpha;
    }
    
    public int evaluate(int[][] grid, int player){
        int score = 0;
        int opponent = player % 2 + 1;
        int victory = model.checkVictory(grid);
        if (victory == player){
            score = 1000;
        } else if (victory == opponent){
            score = -50;
        }

        score += checkForThreat(player) * 10;
        score -= checkForThreat(opponent) * 5;

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
