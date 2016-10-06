import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;
import edu.princeton.cs.algs4.Stopwatch;

/**
 * Created by hyx on 2016/9/19.
 */
public class PercolationStats {

    private int n;
    private int trials;
    private double[] xArr;

    private double mean;
    private double stddev;

    public PercolationStats(int n, int trials) // perform trials independent experiments on an n-by-n grid
    {
        if (n <= 0 || trials <= 0)
            throw new IllegalArgumentException();
        this.n = n;
        this.trials = trials;
        int[] order = new int[n * n];
        xArr = new double[trials];

        for (int i = 0; i < trials; i++) {
            xArr[i] = conductExperiment(order);
        }
        mean = StdStats.mean(xArr);
        stddev = StdStats.stddev(xArr);
    }

    private double conductExperiment(int[] order) {
        Percolation percolation = new Percolation(n);
        shuffleOpenCellOrder(order);
        int i = 0;
        for (; !percolation.percolates() && i < n * n; i++) {
            percolation.open(order[i] / n + 1, order[i] % n + 1);
        }
        return ((double) i) / (n * n);
    }

    private void shuffleOpenCellOrder(int[] order) {
        for (int i = 0; i < n * n; i++) {
            order[i] = i;
        }
        StdRandom.shuffle(order);
    }


    public double mean() // sample mean of percolation threshold
    {
        return mean;
    }

    public double stddev() // sample standard deviation of percolation threshold
    {
        return stddev;
    }

    public double confidenceLo() // low endpoint of 95% confidence interval
    {
        return mean - 1.96 * stddev / Math.sqrt(trials);
    }

    public double confidenceHi() // high endpoint of 95% confidence interval
    {
        return mean + 1.96 * stddev / Math.sqrt(trials);
    }

    public static void main(String[] args) // test client (described below)
    {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        Stopwatch stopwatch = new Stopwatch();
        PercolationStats p = new PercolationStats(n, trials);
        StdOut.println(stopwatch.elapsedTime());
        StdOut.println("mean\t\t\t\t\t = " + p.mean());
        StdOut.println("stddev\t\t\t\t\t = " + p.stddev());
        StdOut.println("95% confidence interval\t = "
                + p.confidenceLo() + ", " + p.confidenceHi());
    }
}
