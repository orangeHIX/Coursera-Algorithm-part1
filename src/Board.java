import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Iterator;

/**
 * Created by hyx on 2016/10/15.
 */
public class Board {

    private char[] tiles;
    private int n;

    public Board(int[][] blocks) // construct a board from an n-by-n array of blocks
    {

        this.n = blocks.length;
        this.tiles = new char[n * n];
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                this.tiles[i * n + j] = (char) blocks[i][j];
            }
        }
        //StdOut.print(this.toString());
    }

    private Board(char[] blocks) {
        this.n = (int) Math.sqrt(blocks.length);
        this.tiles = new char[n * n];
        for (int i = 0; i < blocks.length; i++) {
            tiles[i] = blocks[i];
        }
    }

    // (where blocks[i][j] = block in row i, column j)
    public int dimension() // board dimension n
    {
        return n;
    }

    public int hamming() // number of blocks out of place
    {
        int count = 0;
        for (int i = 0; i < n * n - 1; i++) {
            if (tiles[i] != i + 1) {
                count++;
            }
        }
        return count;
    }

    public int manhattan() // sum of Manhattan distances between blocks and goal
    {
        int count = 0;
        for (int i = 0; i < n * n; i++) {
            int cur_x = i / n;
            int cur_y = i % n;
            int val = tiles[i];
            if (val != 0) {
                int x = (val - 1) / n;
                int y = (val - 1) % n;
                count += (Math.abs(cur_x - x) + Math.abs(cur_y - y));
            }
        }
        return count;
    }

    public boolean isGoal() // is this board the goal board?
    {
        if (hamming() == 0) return true;
        else return false;
    }

    public Board twin() // a board that is obtained by exchanging any pair of blocks
    {
        int i = 0;
        while (tiles[i] == 0) i++;
        int a = i;
        i++;
        while (tiles[i] == 0) i++;
        int b = i;

        Board copy = new Board(tiles);
        copy.exch(a, b);
        return copy;
    }

    public boolean equals(Object y) // does this board equal y?
    {

        if (y != null && y instanceof Board) {
            Board by = (Board) y;

            if (by.dimension() != dimension()) return false;

            for (int i = 0; i < n * n; i++) {
                int cur_x = i / n;
                int cur_y = i % n;
                if (getValue(cur_x, cur_y) != by.getValue(cur_x, cur_y)) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    public Iterable<Board> neighbors() // all neighboring boards
    {
        return () -> new Iterator<Board>() {
            int blankIndex = findBlankSquareIndex();
            int[] vx = {-1, 0, 0, 1};
            int[] vy = {0, -1, 1, 0};
            int next_v = 0;

            @Override
            public boolean hasNext() {
                int x = blankIndex / n;
                int y = blankIndex % n;
                while (next_v < vx.length) {
                    int ex_x = x + vx[next_v];
                    int ex_y = y + vy[next_v];
                    if (isLegalIndex(ex_x, ex_y))
                        break;
                    next_v++;
                }
                if (next_v < vx.length) return true;
                else return false;
            }

            @Override
            public Board next() {
                if (!hasNext()) return null;

                Board b = new Board(tiles);
                b.exch(getNextExchIndex(), blankIndex);
                next_v++;
                return b;
            }

            private int getNextExchIndex() {
                int x = blankIndex / n;
                int y = blankIndex % n;
                return (x + vx[next_v]) * n + y + vy[next_v];
            }
        };
    }

    private int findBlankSquareIndex() {
        for (int i = 0; i < n * n; i++) {
            if (tiles[i] == 0) {
                return i;
            }
        }
        return -1;
    }

    private boolean isLegalIndex(int i, int j) {
        if (i < n && i >= 0 && j < n && j >= 0) return true;
        else return false;
    }

    private void exch(int indexA, int indexB) {
        char tmp = tiles[indexA];
        tiles[indexA] = tiles[indexB];
        tiles[indexB] = tmp;
    }

    private int getValue(int i, int j) {
        return tiles[i * n + j];
    }

    public String toString() // string representation of this board (in the output format specified below)
    {
        StringBuilder sb = new StringBuilder("");
        sb.append(n + "\n");
        for (int i = 0; i < n * n; i++) {
            //int cur_x = i / n;
            int cur_y = i % n;
            //int val = tiles[i];
            sb.append(String.format("%2d ", (int) tiles[i]));
            if (cur_y == n - 1) sb.append('\n');
        }
        return sb.toString();
    }

    private static Board getBoard(String filename) {
        // create initial board from file
        In in = new In(filename);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        in.close();

        Board initial = new Board(blocks);
        return initial;
    }

    public static void main(String[] args) // unit tests (not graded)
    {

        Board board = getBoard("./test/8puzzle/puzzle01.txt");
//        StdOut.print(board);
//        board.exch(0,1);
        StdOut.print(board);
        for (Board b : board.neighbors()) {
            StdOut.print(b);
        }
    }
}
