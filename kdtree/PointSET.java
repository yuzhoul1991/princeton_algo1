import java.util.TreeSet;
import java.util.Stack;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class PointSET {
    private TreeSet<Point2D> set;

    public PointSET() {
        set = new TreeSet<Point2D>();
    }

    public boolean isEmpty() {
        return set.isEmpty();
    }

    public int size() {
        return set.size();
    }

    public void insert(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException();
        set.add(p);
    }

    public boolean contains(Point2D p) {
        if (p == null) throw new java.lang.NullPointerException();
        return set.contains(p);
    }

    public void draw() {
        for (Point2D p : set) {
            p.draw();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new java.lang.NullPointerException();
        Stack<Point2D> return_val = new Stack<Point2D>();
        for (Point2D p : set) {
            if (rect.contains(p)) {
                return_val.push(p);
            }
        }
        return return_val;
    }

    public Point2D nearest(Point2D p) {
        if (isEmpty()) return null;
        Point2D closest = new Point2D(0.0, 0.0);
        double distance = 2;
        for (Point2D myp : set) {
            if (myp.distanceSquaredTo(p) < distance) {
                distance = myp.distanceSquaredTo(p);
                closest = myp;
            }
        }
        return closest;
    }
}
