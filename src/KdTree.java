import edu.princeton.cs.algs4.*;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by hyx on 2016/10/19.
 */
public class KdTree {

    private Node root;
    private int size;

    public KdTree() // construct an empty set of points
    {
        size = 0;
    }


    public boolean isEmpty() // is the set empty?
    {
        return size == 0;
    }

    public int size() // number of points in the set
    {
        return size;
    }

    public void insert(Point2D p) // add the point to the set (if it is not already in the set)
    {
        if (p == null) throw new NullPointerException();


        if (root == null) {
            root = new Node();
            root.p = p;
            root.rect = new RectHV(0, 0, 1, 1);
            size++;
        } else {
            insert(root, true, p);
        }

    }

    private void insert(Node node, boolean isNodeVertical, Point2D p) {

        if (node.p.equals(p)) return;    //if it is already in the set, we should not insert.

        if (isLB(p, node.p, isNodeVertical)) {
            if (node.lb != null) {
                //StdOut.print("lb ");
                insert(node.lb, !isNodeVertical, p);
            } else {
                Node x = new Node();
                x.p = p;
                x.rect = getRect(true, isNodeVertical, node);
                node.lb = x;
                size++;
                //StdOut.println("lb" + p);
            }
        } else {
            if (node.rt != null) {
                //StdOut.print("rt ");
                insert(node.rt, !isNodeVertical, p);
            } else {
                Node x = new Node();
                x.p = p;
                x.rect = getRect(false, isNodeVertical, node);
                node.rt = x;
                size++;
                //StdOut.println( "rt:" + p);
            }
        }
    }

    private boolean isLB(Point2D p, Point2D nodeP, boolean isX) {
        if (isX) return p.x() - nodeP.x() < 0;
        else return p.y() - nodeP.y() < 0;
    }

    private RectHV getRect(boolean isLB, boolean isFormer, Node node) {
        if (isLB && isFormer) {
            return new RectHV(
                    node.rect.xmin(), node.rect.ymin(),
                    node.p.x(), node.rect.ymax());
        } else if (isLB && !isFormer) {
//            StdOut.println("B"+node.rect.xmin()+","+ node.rect.ymin()+","+
//                    node.rect.xmax()+","+ node.p.y());
            return new RectHV(
                    node.rect.xmin(), node.rect.ymin(),
                    node.rect.xmax(), node.p.y());
        } else if (!isLB && isFormer) {
            return new RectHV(
                    node.p.x(), node.rect.ymin(),
                    node.rect.xmax(), node.rect.ymax());
        } else {
            return new RectHV(
                    node.rect.xmin(), node.p.y(),
                    node.rect.xmax(), node.rect.ymax());
        }
    }


    public boolean contains(Point2D p) // does the set contain point p?
    {
        if (p == null) throw new NullPointerException();
        return contains(root, true, p);
    }

    private boolean contains(Node node, boolean isNodeVertical, Point2D p) {

        if (node == null) return false;

        if (node.p.equals(p)) return true;
        else if (isLB(p, node.p, isNodeVertical)) {
            return contains(node.lb, !isNodeVertical, p);
        } else {
            return contains(node.rt, !isNodeVertical, p);
        }
    }


    public void draw() // draw all points to standard draw
    {
        draw(root, true);
    }

    private void draw(Node node, boolean isVertical) {
        StdDraw.setPenRadius();
        if (isVertical) {
            StdDraw.setPenColor(StdDraw.RED);
            StdDraw.line(node.p.x(), node.rect.ymin(), node.p.x(), node.rect.ymax());
        } else {
            StdDraw.setPenColor(StdDraw.BLUE);
            StdDraw.line(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.p.y());
        }
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();

        if (node.lb != null) draw(node.lb, !isVertical);
        if (node.rt != null) draw(node.rt, !isVertical);
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

        Stack<Node> stack = new Stack<>();
        if (root != null) stack.push(root);
        while (!stack.isEmpty()) {
            Node node = stack.pop();

            if (champion == null) {
                champion = node.p;
                championSquaredDist = p.distanceSquaredTo(champion);
            } else {
                double sqDist = node.p.distanceSquaredTo(p);
                if (sqDist < championSquaredDist) {
                    champion = node.p;
                    championSquaredDist = sqDist;
                }
            }

            double lbDis;
            double rtDis;
            if (node.lb != null) lbDis = node.lb.rect.distanceSquaredTo(p);
            else lbDis = Double.POSITIVE_INFINITY;

            if (node.rt != null) rtDis = node.rt.rect.distanceSquaredTo(p);
            else rtDis = Double.POSITIVE_INFINITY;

            if (lbDis < championSquaredDist && championSquaredDist <= rtDis)
                stack.push(node.lb);
            else if (rtDis < championSquaredDist && championSquaredDist <= lbDis)
                stack.push(node.rt);
            else if (lbDis < rtDis && rtDis < championSquaredDist) {
                stack.push(node.rt);
                stack.push(node.lb);
            } else if (rtDis <= lbDis && lbDis < championSquaredDist) {
                stack.push(node.lb);
                stack.push(node.rt);
            }
        }
        return champion;
    }


    public static void main(String[] args) {
        int n = 10;
//        0.017996 0.658058
//        true
//        0.274610 0.500106
//        true
//        0.876673 0.570484
//        true
//        0.400766 0.265736
        KdTree kdTree = new KdTree();

//        Point2D p = new Point2D(0.017, 0.658);
//        kdTree.insert(p);
//        p = new Point2D(0.274, 0.500);
//        kdTree.insert(p);
//        p = new Point2D(0.876, 0.570);
//        kdTree.insert(p);
//        p = new Point2D(0.400, 0.265);
//        kdTree.insert(p);


        for (int i = 0; i < n; i++) {
            double x = StdRandom.uniform(0.0, 1.0);
            double y = StdRandom.uniform(0.0, 1.0);
            StdOut.printf("%8.6f %8.6f\n", x, y);
            Point2D p = new Point2D(x, y);
            kdTree.insert(p);
            StdOut.println(kdTree.contains(p) + " size:" + kdTree.size());
        }


    }

    private static class Node {
        private Point2D p; // the point
        private RectHV rect; // the axis-aligned rectangle corresponding to this node
        private Node lb; // the left/bottom subtree
        private Node rt; // the right/top subtree
    }

    private class RangeRect implements Iterator<Point2D> {

        RectHV rect;
        Stack<Node> stack;
        Point2D nextp;

        public RangeRect(RectHV rectHV) {
            rect = rectHV;
            nextp = null;
            stack = new Stack<>();
            if (root != null && rectHV.intersects(root.rect)) {
                stack.push(root);
                findNext();
            }
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

            while (!stack.isEmpty()) {
                Node node = stack.pop();
                if (node.lb != null && node.lb.rect.intersects(rect))
                    stack.push(node.lb);
                if (node.rt != null && node.rt.rect.intersects(rect))
                    stack.push(node.rt);

                if (rect.contains(node.p)) {
                    nextp = node.p;
                    isFinish = false;
                    break;
                }
            }

            if (isFinish) nextp = null;
        }

    }
}
