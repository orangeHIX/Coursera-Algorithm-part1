import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

/**
 * Created by hyx on 2016/10/6.
 */
public class Subset {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        if (k <= 0) {
            return;
        }

        RandomizedQueue<String> pool = new RandomizedQueue<>();
        int i = 0;
        while (!StdIn.isEmpty()) {
            String s = StdIn.readString();
            if (pool.size() < k) {
                pool.enqueue(s);
            } else {
                String tmp = pool.dequeue();
                int r = StdRandom.uniform(0, i + 1);
                if (r < k) {
                    pool.enqueue(s);
                } else {
                    pool.enqueue(tmp);
                }
            }
            i++;
        }
        for (i = 0; i < k; i++) {
            StdOut.println(pool.dequeue());
        }
    }
}
