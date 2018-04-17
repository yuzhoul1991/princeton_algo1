import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import java.util.Stack;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {

    private Node root;
    private int size;
    private Stack<Node> nodeStack;
    private Stack<Boolean> orientationStack;
    private double distance;
    private Point2D champion;

    private static class Node {
        private Point2D p;
        private RectHV rect;
        private Node lb;
        private Node rt;

        public Node(Point2D _p, Node _lb, Node _rt, double xmin, double ymin, double xmax, double ymax) {
            p = _p;
            lb = _lb;
            rt = _rt;
            rect = new RectHV(xmin, ymin, xmax, ymax);
        }

        public String toString() {
            StringBuilder s = new StringBuilder();
            s.append(String.format("%s\n%s", p.toString(), rect.toString()));
            return s.toString();
        }
    }

    public KdTree() {
        size = 0;
        nodeStack = new Stack<Node>();
        orientationStack = new Stack<Boolean>();
    }

    public boolean isEmpty() {
        return root == null;
    }

    public int size() {
        return size;
    }

    public void insert(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException();
        root = put(root, p, true, 0.0, 0.0, 1.0, 1.0);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException();
        return get(root, p, true);
    }

    public void draw() {
        // System.out.format("size: %d\n", nodeStack.size());
        Stack<Node> newNodeStack = new Stack<Node>();
        Stack<Boolean> newOrientationStack = new Stack<Boolean>();
        newNodeStack.addAll(nodeStack);
        newOrientationStack.addAll(orientationStack);
        while(!newNodeStack.empty()) {
            Node node = newNodeStack.pop();
            // System.out.format("%s\n", node.toString());
            drawNode(node, newOrientationStack.pop());
        }
        // System.out.println();
    }

    public Point2D nearest(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException();
        if (isEmpty()) return null;
        distance = 2.0;
        champion = root.p;
        nearest(root, p);
        return champion;
    }

    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> rangeStack = new Stack<Point2D>();
        range(root, rect, rangeStack);
        return rangeStack;
    }

    public static void main(String[] args) {
        KdTree kdtree = new KdTree();
        System.out.println("0.7, 0.2");
        kdtree.insert(new Point2D(0.7, 0.2));
        System.out.println("0.5, 0.4");
        kdtree.insert(new Point2D(0.5, 0.4));
        System.out.println("0.2, 0.3");
        kdtree.insert(new Point2D(0.2, 0.3));
        System.out.println("0.4, 0.7");
        kdtree.insert(new Point2D(0.4, 0.7));
        System.out.println("0.9, 0.6");
        kdtree.insert(new Point2D(0.9, 0.6));
        assert(kdtree.contains(new Point2D(0.9, 0.6)));
        assert(kdtree.contains(new Point2D(0.4, 0.7)));
        assert(kdtree.contains(new Point2D(0.2, 0.3)));
        assert(kdtree.contains(new Point2D(0.5, 0.4)));
        assert(kdtree.contains(new Point2D(0.7, 0.2)));
        assert(!kdtree.contains(new Point2D(0.1, 0.2)));
    }

    private void range(Node node, RectHV rect, Stack<Point2D> rangeStack) {
        if (node == null) return;
        if (rect.contains(node.p)) {
            rangeStack.push(node.p);
        }
        if (node.lb != null && rect.intersects(node.lb.rect)) range(node.lb, rect, rangeStack);
        if (node.rt != null && rect.intersects(node.rt.rect)) range(node.rt, rect, rangeStack);
    }

    private void nearest(Node node, Point2D p) {
        if (node.p.distanceSquaredTo(p) <= distance) {
            distance = node.p.distanceSquaredTo(p);
            champion = node.p;
        }
        if (node.lb == null && node.rt == null) return;
        if (node.lb != null && node.lb.rect.contains(p)) {
            nearest(node.lb, p);
            if (node.rt == null) return;
            if (node.rt.rect.distanceSquaredTo(p) > distance) return;
        }
        if (node.rt != null && node.rt.rect.contains(p)) {
            nearest(node.rt, p);
            if (node.lb == null) return;
            if (node.lb.rect.distanceSquaredTo(p) > distance) return;
        }
        if (node.lb != null && distance > node.lb.rect.distanceSquaredTo(p)) nearest(node.lb, p);
        if (node.rt != null && distance > node.rt.rect.distanceSquaredTo(p)) nearest(node.rt, p);
    }

    private void drawNode(Node node, boolean use_x) {
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        node.p.draw();
        StdDraw.setPenRadius();
        if (use_x) {
            StdDraw.setPenColor(StdDraw.RED);
            RectHV left = new RectHV(node.rect.xmin(), node.rect.ymin(), node.p.x(), node.rect.ymax());
            left.draw();
        }
        else {
            StdDraw.setPenColor(StdDraw.BLUE);
            RectHV top = new RectHV(node.rect.xmin(), node.p.y(), node.rect.xmax(), node.rect.ymax());
            top.draw();
        }
    }

    private boolean get(Node x, Point2D p, boolean use_x) {
        if (x == null) return false;
        if (x.p.compareTo(p) == 0) return true;
        if (use_x) {
            if (p.x() < x.p.x()) return get(x.lb, p, !use_x);
            else return get(x.rt, p, !use_x);
        }
        else {
            if(p.y() < x.p.y()) return get(x.lb, p, !use_x);
            else return get(x.rt, p, !use_x);
        }
    }

    private Node put(Node x, Point2D p, boolean use_x, double xmin, double ymin, double xmax, double ymax) {
        if (x == null) {
            size++;
            Node new_node = new Node(p, null, null, xmin, ymin, xmax, ymax);
            nodeStack.push(new_node);
            orientationStack.push(use_x);
            return new_node;
        }
        if (x.p.compareTo(p) == 0) return x;
        if (use_x) {
            if (p.x() < x.p.x()) { x.lb = put(x.lb, p, !use_x, xmin, ymin, x.p.x(), ymax); }
            else { x.rt = put(x.rt, p, !use_x, x.p.x(), ymin, xmax, ymax); }
        }
        else {
            if (p.y() < x.p.y()) { x.lb = put(x.lb, p, !use_x, xmin, ymin, xmax, x.p.y()); }
            else { x.rt = put(x.rt, p, !use_x, xmin, x.p.y(), xmax, ymax); }
        }
        return x;
    }

}
