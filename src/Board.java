import java.util.Iterator;

/**
 * Created by hyx on 2016/10/15.
 */
public class Board {

    private int[][] blocks;
    private int n;

    public Board(int[][] blocks) // construct a board from an n-by-n array of blocks
    {
        this.blocks = blocks;
        this.n = blocks.length;
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
            if (blocks[i / n][i % n] != i + 1) {
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
            int val = blocks[cur_x][cur_y];
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
        while (blocks[i / n][i % n] == 0) i++;
        int a = i;
        i++;
        while (blocks[i / n][i % n] == 0) i++;
        int b = i;

        Board copy = new Board(blocks);
        copy.exch(a, b);
        return copy;
    }

    public boolean equals(Object y) // does this board equal y?
    {
        if (y instanceof Board) {
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

                while (!isLegalIndex(getNextExchIndex())
                        && next_v < vx.length) {
                    next_v++;
                }
                if (next_v < vx.length) return true;
                else return false;
            }

            @Override
            public Board next() {
                if (!hasNext()) return null;

                Board b = new Board(blocks);
                b.exch(getNextExchIndex(), blankIndex);
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
            int cur_x = i / n;
            int cur_y = i % n;
            if (blocks[cur_x][cur_y] == 0) {
                return i;
            }
        }
        return -1;
    }

    private boolean isLegalIndex(int index) {
        int i = index / n;
        int j = index % n;
        if (i < n && i >= 0 && j < n && j >= 0) return true;
        else return false;
    }

    private void exch(int indexA, int indexB) {
        int tmp = blocks[indexA / n][indexA % n];
        blocks[indexA / n][indexA % n] = blocks[indexB / n][indexB % n];
        blocks[indexB / n][indexB % n] = tmp;
    }

    private int getValue(int i, int j) {
        return blocks[i][j];
    }

    public String toString() // string representation of this board (in the output format specified below)
    {
        StringBuilder sb = new StringBuilder("");
        for (int i = 0; i < n * n; i++) {
            int cur_x = i / n;
            int cur_y = i % n;
            int val = blocks[cur_x][cur_y];
            sb.append(val);
            if (cur_y < n - 1) sb.append('\t');
            if (cur_y == n - 1 && cur_x != n - 1) sb.append('\n');
        }
        return sb.toString();
    }


    public static void main(String[] args) // unit tests (not graded)
    {

    }
}
