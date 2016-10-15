import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.PriorityQueue;

/**
 * Created by hyx on 2016/10/15.
 */
public class Solver {

    private ArrayList<Board> solution;
    private PriorityQueue<Board> q;

    public Solver(Board initial) // find a solution to the initial board (using the A* algorithm)
    {
        // todo
    }

    public boolean isSolvable() // is the initial board solvable?
    {
        // todo
        return false;
    }

    public int moves() // min number of moves to solve initial board; -1 if unsolvable
    {
        if ( isSolvable() ){
            return solution.size() - 1;
        }else{
            return -1;
        }
    }

    public Iterable<Board> solution() // sequence of boards in a shortest solution; null if unsolvable
    {
        return solution;
    }

    public static void main(String[] args) // solve a slider puzzle (given below)
    {
        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        // solve the puzzle
        Solver solver = new Solver(initial);
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}
