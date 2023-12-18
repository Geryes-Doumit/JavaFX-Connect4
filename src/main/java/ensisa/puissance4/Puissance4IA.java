package ensisa.puissance4;

import java.util.ArrayList;
import java.util.List;

public class Puissance4IA {
    public Puissance4Model model;
    public int AIMove(Puissance4Model model, int player, byte depth, byte turn){
        this.model = model;
        int[][] grid = model.getGrid();
        List<Integer> possibleMoves = model.getPossibleMoves(grid);

        int bestMove = possibleMoves.get(0);
        byte bestScore = -1;
        byte alpha = -1;
        byte beta = 1;

        for (int move : possibleMoves){
            int[][] gridCopy = grid.clone();
            gridCopy[move][5] = player;
            byte score = AlphaBetaMin(gridCopy, player, (byte)(depth - 1), alpha, beta, turn);
            grid[move][5] = 0;
            if (score > bestScore){
                bestScore = score;
                bestMove = move;
            }
            alpha = (byte)Math.max(alpha, score);
        }

        return bestMove;
    }

    public byte AlphaBetaMin(int[][] grid, int player, byte depth, byte alpha, byte beta, byte turn){
        if (depth == 0 || model.checkVictory(grid) != 0 || turn == 42){
            return evaluate(grid, player);
        }

        List<Integer> possibleMoves = model.getPossibleMoves(grid);
        turn = (byte)(turn + 1);
        for (int move : possibleMoves){
            int[][] gridCopy = grid.clone();
            gridCopy[move][5] = player;
            int score = AlphaBetaMax(gridCopy, player, (byte)(depth - 1), alpha, beta, turn);
            grid[move][5] = 0;
            beta = (byte)Math.min(beta, score);
            if (beta <= alpha){
                return beta;
            }
        }
        return beta;
    }

    public byte AlphaBetaMax(int[][] grid, int player, byte depth, byte alpha, byte beta, byte turn){
        if (depth == 0 || model.checkVictory(grid) != 0 || turn == 42){
            return evaluate(grid, player);
        }

        List<Integer> possibleMoves = model.getPossibleMoves(grid);
        turn = (byte)(turn + 1);
        for (int move : possibleMoves){
            int[][] gridCopy = grid.clone();
            gridCopy[move][5] = player;
            byte score = AlphaBetaMin(gridCopy, player, (byte)(depth - 1), alpha, beta, turn);
            grid[move][5] = 0;
            alpha = (byte)Math.max(alpha, score);
            if (beta <= alpha){
                return alpha;
            }
        }
        return alpha;
    }
    
    public byte evaluate(int[][] grid, int player){
        byte score = 0;
        int opponent = player % 2 + 1;
        int victory = model.checkVictory(grid);
        if (victory == player){
            score = 1;
        } else if (victory == opponent){
            score = -1;
        }
        return score;
    }
}
