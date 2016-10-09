import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;
import java.util.Comparator;

/**
 * Created by hyx on 2016/10/9.
 */
public class BruteCollinearPoints {

    private LineSegment[] lineSegments;
    private Node head;
    private int num;

    public BruteCollinearPoints(Point[] points)    // finds all line segments containing 4 points
    {
        if (points == null)
            throw new NullPointerException();
        Point[] clones = clonePoints(points);

        Arrays.sort(clones);
        checkRepeatedPoints(clones);

        head = new Node();
        num = 0;

        for (int i = 0; i < clones.length - 3; i++) {
            Comparator<Point> pc = clones[i].slopeOrder();
            for (int j = i + 1; j < clones.length - 2; j++) {
                for (int k = j + 1; k < clones.length - 1; k++) {
                    if (pc.compare(clones[j], clones[k]) != 0)
                        continue;
                    for (int l = k + 1; l < clones.length; l++) {
                        if (pc.compare(clones[k], clones[l]) == 0) {
                            addSegment(new LineSegment(clones[i], clones[l]));
                        }
                    }
                }
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

        // read the N points from a file
        // In in = new In(args[0]);
        In in = new In("./test/collinear/input100.txt");
        int N = in.readInt();
        StdOut.println(N);
        Point[] points = new Point[N];
        for (int i = 0; i < N; i++) {
            int x = in.readInt();
            int y = in.readInt();
            StdOut.println("x:" + x + " y:" + y);
            points[i] = new Point(x, y);
        }

        // draw the points
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();

        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        StdOut.println(collinear.numberOfSegments());
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
            segment.draw();
        }
    }
}
