import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

    private static final byte TOP = 0x4;
    private static final byte BOTTOM = 0x2;
    private static final byte BLOCKED = 0x0;
    private static final byte OPEN = 0x1;
    private WeightedQuickUnionUF backwashUF;
    private boolean percolates;
    /**
     * In the following statements, x can be 0 or 1.
     * 0bxx1 indicates "open", 0bxx0 indicates "blocked",
     * 0b1x1 indicates "linked to top"
     * 0bx11 indicates "linked to bottom"
     */
    private byte[] status;

    private int n;

    public Percolation(int n)               // create n-by-n grid, with all sites blocked
    {
        if (n <= 0)
            throw new IllegalArgumentException("\'n\' should not less than or equal to 0");
        this.n = n;

        backwashUF = new WeightedQuickUnionUF(n * n);

        status = new byte[n * n];
        for (int i = 0; i < n * n; i++) {
            status[i] = BLOCKED;
        }
        percolates = false;
    }

    private int to1D(int x, int y) {
        return x * n + y;
    }

    public void open(int i, int j)          // open site (row i, column j) if it is not open already
    {
        validate(i, j);
        int x = i - 1;
        int y = j - 1;
        int curID = to1D(x, y);
        if (!isOpen(i, j)) {
            status[curID] = (byte) (status[curID] | OPEN);
            int[] dx = {1, -1, 0, 0};
            int[] dy = {0, 0, 1, -1};
            byte tmpStatus = 0;
            for (int dir = 0; dir < 4; dir++) {
                int posX = x + dx[dir];
                int posY = y + dy[dir];
                int neighbourID = to1D(posX, posY);
                if (posX < n && posX >= 0
                        && posY < n && posY >= 0
                        && isOpen(posX + 1, posY + 1)) {
                    tmpStatus = (byte) (findStatus(neighbourID) | tmpStatus);
                    backwashUF.union(curID, neighbourID);
                }
            }
            int parentID = backwashUF.find(curID);
            status[parentID] = (byte) (tmpStatus | status[parentID]);
            if (x == 0) {
                status[parentID] = (byte) (TOP | status[parentID]);
            }
            if (x == n - 1) {
                status[parentID] = (byte) (BOTTOM | status[parentID]);
            }

            if ((status[parentID] & TOP) == TOP
                    && (status[parentID] & BOTTOM) == BOTTOM) {
                percolates = true;
            }
        }
    }

    private byte findStatus(int id) {
        int parentId = backwashUF.find(id);
        return status[parentId];
    }

    public boolean isOpen(int i, int j)     // is site (row i, column j) open?
    {
        validate(i, j);
        int x = i - 1;
        int y = j - 1;
        // return cell[x][y] == 0;
        return (status[to1D(x, y)] & OPEN) == OPEN;
    }

    public boolean isFull(int i, int j)     // is site (row i, column j) full?
    {
        validate(i, j);
        int x = i - 1;
        int y = j - 1;
        // return cell[x][y] == 0 && uf.connected(0, x * n + y + 1);
        return (findStatus(to1D(x, y)) & TOP) == TOP;
    }

    public boolean percolates()             // does the system percolate?
    {
        return percolates;
    }

//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        for (int i = 0; i < n; i++) {
//            for (int j = 0; j < n; j++) {
//                sb.append(status[to1D(i, j)] & OPEN).append(' ');
//            }
//            sb.append('\n');
//        }
//        sb.replace(sb.length() - 1, sb.length(), "");
//        return sb.toString();
//    }

    private void validate(int i, int j) {
        if (i <= 0 || i > n
                || j <= 0 || j > n)
            throw new IndexOutOfBoundsException();
    }

    public static void main(String[] args)  // test client (optional)
    {
        Percolation percolation = new Percolation(2);
//        StdOut.println(percolation);
//        StdOut.println(percolation.percolates());
//        StdOut.println("" + percolation.isOpen(1, 1)
//                + "," + percolation.isFull(1, 1));
        percolation.open(1, 1);
        StdOut.println(percolation);
        StdOut.println("" + percolation.isOpen(1, 1)
                + "," + percolation.isFull(1, 1));

        StdOut.println("" + percolation.isOpen(1, 2)
                + "," + percolation.isFull(1, 2));
        percolation.open(1, 2);
        StdOut.println(percolation);
        StdOut.println("" + percolation.isOpen(1, 2)
                + "," + percolation.isFull(1, 2));


        StdOut.println(percolation.percolates());
        percolation.open(2, 2);
        StdOut.println(percolation.percolates());
        StdOut.println(percolation);
        StdOut.println("============================");
        Percolation p2 = new Percolation(1);
        StdOut.println("" + p2.isOpen(1, 1)
                + "," + p2.isFull(1, 1)
                + "," + p2.percolates());
        p2.open(1, 1);
        StdOut.println("" + p2.isOpen(1, 1)
                + "," + p2.isFull(1, 1)
                + "," + p2.percolates());
        StdOut.println(p2);
    }
}
