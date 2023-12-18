package ensisa.puissance4;

import java.util.ArrayList;
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
        byte bestScore = -128;
        byte alpha = -128;
        byte beta = 127;

        for (int move : possibleMoves){
            model.makeMove(move, player);
            byte score = AlphaBetaMin(player % 2 + 1, (byte)(depth - 1), alpha, beta, turn);
            model.undoMove(move);
            if (score > bestScore){
                System.out.println("best score : " + score + " for move : " + move);
                bestScore = score;
                bestMove = move;
            }
            alpha = (byte)Math.max(alpha, score);
        }
        System.out.println("best move : " + bestMove);
        System.out.println("best score : " + bestScore);
        return bestMove;


    }

    public byte AlphaBetaMin(int player, byte depth, byte alpha, byte beta, byte turn){
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

    public byte AlphaBetaMax(int player, byte depth, byte alpha, byte beta, byte turn){
        int[][] grid = model.getGrid();
        if (depth == 0 || model.checkVictory(grid) != 0 || turn == 42){
            return evaluate(grid, player);
        }

        List<Integer> possibleMoves = model.getPossibleMoves(grid);
        turn = (byte)(turn + 1);
        for (int move : possibleMoves){
            model.makeMove(move, player);
            byte score = AlphaBetaMin(player, (byte)(depth - 1), alpha, beta, turn);
            model.undoMove(move);
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
            score = 10;
        } else if (victory == opponent){
            score = -50;
        }
        return score;
    }
}
