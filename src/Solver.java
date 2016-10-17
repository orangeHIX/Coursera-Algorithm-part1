import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;

/**
 * Created by hyx on 2016/10/15.
 */
public class Solver {

    private ArrayList<Board> solution;
    private MinPQ<Node> q;

    private boolean isSolved = false;

    public Solver(Board initial) // find a solution to the initial board (using the A* algorithm)
    {
        q = new MinPQ<>(initial.dimension(), new Comparator<Node>() {
            @Override
            public int compare(Node o1, Node o2) {
                int diff = o1.priority - o2.priority;
                if (diff != 0) {
                    return diff;
                } else {
                    return o1.moves - o2.moves;
                }
            }
        });

        Board twin = initial.twin();
        q.insert(getNode(null, initial, 0));
        q.insert(getNode(null, twin, 0));

        //StdOut.println("Step 0:\n" + q);
        //int i = 0;
        Node node = null;
        while (!q.isEmpty() && !(node = q.delMin()).val.isGoal()) {
            Board curr = node.val;
            Board preb = node.pre != null ? node.pre.val : null;
            for (Board b : curr.neighbors()) {
                if (!b.equals(preb)) {
                    q.insert(getNode(node, b, node.moves + 1));
                }
            }
            //StdOut.println("Step "+(++i)+":\n"+q);
        }

        if (node != null) {
            solution = new ArrayList<>();
            solution.add(node.val);
            while (node.pre != null) {
                node = node.pre;
                solution.add(node.val);
            }
            if (solution.get(solution.size() - 1).equals(initial)) {
                isSolved = true;
            } else {
                solution = null;
            }
        }
        q = null;
    }

    private static Node getNode(Node pre, Board val, int moves) {
        return new ManhattanNode(pre, val, moves);
    }

//    private String toQString(){
//        StringBuffer sb = new StringBuffer("");
//        q.forEach(new Consumer<Node>() {
//            @Override
//            public void accept(Node node) {
//
//            }
//        });
//    }

    public boolean isSolvable() // is the initial board solvable?
    {
        return isSolved;
    }

    public int moves() // min number of moves to solve initial board; -1 if unsolvable
    {
        if (isSolvable()) {
            return solution.size() - 1;
        } else {
            return -1;
        }
    }

    public Iterable<Board> solution() // sequence of boards in a shortest solution; null if unsolvable
    {
        if (!isSolvable()) return null;

        return () -> new Iterator<Board>() {

            int index = solution.size() - 1;

            @Override
            public boolean hasNext() {
                return index >= 0;
            }

            @Override
            public Board next() {
                if (hasNext()) {
                    return solution.get(index--);
                } else {
                    return null;
                }
            }
        };
    }

    public static void main(String[] args) // solve a slider puzzle (given below)
    {
        // create initial board from file
        //In in = new In(args[0]);
        In in = new In("./test/8puzzle/puzzle50.txt");
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

    private static abstract class Node {
        protected Node pre;
        protected Board val;
        protected int moves;
        protected int priority;

        public Node(Node pre, Board val, int moves) {
            this.pre = pre;
            this.val = val;
            this.moves = moves;
            priority = Integer.MAX_VALUE;
            //this.hamming = val.hamming();
        }

        @Override
        public String toString() {
            StringBuilder sb = new StringBuilder();
            sb.append("priority = ").append(priority)
                    .append("\nmoves = ").append(moves)
                    .append('\n')
                    .append(val);

            return sb.toString();
        }

    }

    private static class ManhattanNode extends Node {

        public ManhattanNode(Node pre, Board val, int moves) {
            super(pre, val, moves);
            priority = this.val.manhattan() + moves;
            //this.hamming = val.hamming();
        }
    }

    private static class HammingNode extends Node {

        public HammingNode(Node pre, Board val, int moves) {
            super(pre, val, moves);
            priority = this.val.hamming() + moves;
            //this.hamming = val.hamming();
        }
    }
}
