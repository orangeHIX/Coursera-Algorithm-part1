import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by hyx on 2016/10/9.
 */
public class FastCollinearPoints {

    private Node head;
    private int num;

    public FastCollinearPoints(Point[] points) // finds all line segments containing 4 or more points
    {
        if (points == null)
            throw new NullPointerException();

        Point[] clones = clonePoints(points);
        Arrays.sort(clones);
        checkRepeatedPoints(clones);

        head = new Node();
        num = 0;

        for (int i = 0; i < points.length; i++) {
            Point p = points[i];

            Comparator<Point> c = p.slopeOrder();
            Arrays.sort(clones, c);

            int j = 0;
            int len = 0;
            while (j < clones.length) {
                if ((len = getCollinearLength(p, clones, j)) >= 3) {
                    Arrays.sort(clones, j, j + len);
                    if (p.compareTo(clones[j]) < 0) {
                        addSegment(new LineSegment(p, clones[j + len - 1]));
                    }
//                    } else if (p.compareTo(clones[j + len - 1]) > 0) {
//                        addSegment(new LineSegment(clones[j], p));
//                    } else {
//                        addSegment(new LineSegment(clones[j], clones[j + len - 1]));
//                    }
                }
                j += len;
            }
        }

    }

    private Point[] clonePoints(Point[] points) {
        Point[] result = new Point[points.length];
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new NullPointerException();
            }
            result[i] = points[i];
        }
        return result;
    }

    private void checkRepeatedPoints(Point[] sortedPoints)
            throws IllegalArgumentException {
        for (int i = 0; i < sortedPoints.length - 1; i++) {
            if (sortedPoints[i].compareTo(sortedPoints[i + 1]) == 0) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * 返回的从points[i]开始的(包含点point[i])，
     * 与点p连成线段具有和（p->point[i]）有相同斜率的且毗邻的点的数目
     */
    private static int getCollinearLength(Point p, Point[] points, int i) {
        double slope = p.slopeTo(points[i]);
        for (int j = 1; i + j < points.length; j++) {
            if (slope != p.slopeTo(points[i + j])) {
                return j;
            }
        }
        return points.length - i;
    }

    private void addSegment(LineSegment lineSegment) {
        head.value = lineSegment;
        Node tmp = head;
        head = new Node();
        head.next = tmp;
        num++;
    }


    public int numberOfSegments()        // the number of line segments
    {
        return num;
    }

    public LineSegment[] segments()                // the line segments
    {
        LineSegment[] result = new LineSegment[num];

        Node tmp = head;
        for (int i = 0; i < num; i++) {
            result[i] = tmp.next.value;
            tmp = tmp.next;
        }
        return result;
    }


    private class Node {
        private LineSegment value;
        private Node next;
    }

    public static void main(String[] args) {
        // read the n points from a file
        //In in = new In(args[0]);
        In in = new In("./test/collinear/mystery10089.txt");
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        // draw the points
        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        // print and draw the line segments
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        StdOut.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
        StdDraw.show();


    }
}
