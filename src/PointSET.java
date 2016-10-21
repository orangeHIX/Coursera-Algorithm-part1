import edu.princeton.cs.algs4.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by hyx on 2016/10/19.
 */
public class PointSET {

    private SET<Point2D> set;

    public PointSET() // construct an empty set of points
    {
        set = new SET<>();
    }

    public boolean isEmpty() // is the set empty?
    {
        return set.isEmpty();
    }

    public int size() // number of points in the set
    {
        return set.size();
    }

    public void insert(Point2D p) // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new NullPointerException();

        if (!contains(p)) {
            set.add(p);
        }
    }

    public boolean contains(Point2D p) // does the set contain point p?
    {
        if (p == null) throw new NullPointerException();
        return set.contains(p);
    }

    public void draw() // draw all points to standard draw
    {
        for (Point2D p : set) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) // all points that are inside the rectangle
    {
        if (rect == null) throw new NullPointerException();
        return () -> new RangeRect(rect);
    }

    public Point2D nearest(Point2D p) // a nearest neighbor in the set to point p; null if the set is empty
    {
        if (p == null) throw new NullPointerException();
        Point2D champion = null;
        double championSquaredDist = Double.MAX_VALUE;
        for (Point2D pi : set) {
            if (champion == null) {
                champion = pi;
                championSquaredDist = p.distanceSquaredTo(champion);
            } else {
                double sqDist = p.distanceSquaredTo(pi);
                if (sqDist < championSquaredDist) {
                    champion = pi;
                    championSquaredDist = sqDist;
                }
            }
        }
        return champion;
    }

    public static void main(String[] args) // unit testing of the methods (optional)
    {
        int n = 5;
        PointSET ps = new PointSET();
        for (int i = 0; i < n; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            StdOut.printf("%8.6f %8.6f\n", x, y);
            ps.insert(new Point2D(x, y));
        }

        Point2D nearest = ps.nearest(new Point2D(0, 0));
        StdOut.printf(" nearest point: %8.6f %8.6f\n",
                nearest.x(), nearest.y());
        RectHV rectHV = new RectHV(0, 0, 1, 1);
        for (Point2D p : ps.range(rectHV)) {
            StdOut.printf("%8.6f %8.6f\n", p.x(), p.y());
        }
    }

    private class RangeRect implements Iterator<Point2D> {

        RectHV rect;
        Iterator<Point2D> iter;
        Point2D nextp;

        public RangeRect(RectHV rectHV) {
            rect = rectHV;
            iter = set.iterator();
            nextp = null;
            if (iter == null) throw new NullPointerException();
            findNext();
        }

        @Override
        public boolean hasNext() {
            if (nextp == null) return false;
            else return true;
        }

        @Override
        public Point2D next() {
            if (hasNext()) {
                Point2D result = nextp;
                findNext();
                return result;
            }
            throw new NoSuchElementException();
        }

        private void findNext() {
            boolean isFinish = true;
            while (iter.hasNext()) {
                nextp = iter.next();
                if (rect.contains(nextp)) {
                    isFinish = false;
                    break;
                }
            }
            if (isFinish) nextp = null;
        }

    }
}
