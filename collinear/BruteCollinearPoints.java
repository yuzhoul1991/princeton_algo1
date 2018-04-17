import java.util.ArrayList;
import java.util.Collections;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

public class BruteCollinearPoints {

    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
    private ArrayList<Point> allPoints = new ArrayList<Point>();
    private ArrayList<Point> startPoints = new ArrayList<Point>();
    private ArrayList<Point> endPoints = new ArrayList<Point>();

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new java.lang.NullPointerException();
        }
        for (int i = 0; i < points.length; i++) {
            if (allPoints.contains(points[i])) {
                throw new java.lang.IllegalArgumentException();
            }
            allPoints.add(points[i]);
        }
        Collections.sort(allPoints);
        int size = allPoints.size();
        for (int p = 0; p < size - 3; p++) {
            for (int q = p + 1; q < size - 2; q++) {
                for (int r = q + 1; r < size - 1; r++) {
                    for (int s = r + 1; s < size; s++) {
                        Point a = allPoints.get(p);
                        Point b = allPoints.get(q);
                        Point c = allPoints.get(r);
                        Point d = allPoints.get(s);
                        if (a == null || b == null || c == null || d == null) {
                            throw new java.lang.NullPointerException();
                        }
                        if (a.slopeTo(b) == a.slopeTo(c) && a.slopeTo(c) == a.slopeTo(d)) {
                            boolean existing = false;
                            for (int i = 0; i < startPoints.size(); i++) {
                                Point existingStart = startPoints.get(i);
                                Point existingEnd = endPoints.get(i);
                                double existingSlope = existingStart.slopeTo(existingEnd);
                                double newSlope = a.slopeTo(b);
                                double testSlope = existingStart.slopeTo(a);
                                if (newSlope == existingSlope && (newSlope == testSlope || testSlope == Double.NEGATIVE_INFINITY)) {
                                    existing = true;
                                    if (d.compareTo(existingEnd) > 0) {
                                        endPoints.set(i, d);
                                    }
                                    if (a.compareTo(existingStart) < 0) {
                                        startPoints.set(i, a);
                                    }
                                }
                            }
                            if (!existing) {
                                startPoints.add(a);
                                endPoints.add(d);
                            }
                        }
                    }
                }
            }
        }
        assert (startPoints.size() == endPoints.size());
        for (int i = 0; i < startPoints.size(); i++) {
            segments.add(new LineSegment(startPoints.get(i), endPoints.get(i)));
        }
    }

    public int numberOfSegments() {
        return segments.size();
    }

    public LineSegment[] segments() {
        LineSegment[] segarr = new LineSegment[segments.size()];
        for (int i = 0; i < segments.size(); i++) {
            segarr[i] = segments.get(i);
        }
        return segarr;
    }

    public static void main(String[] args) {
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }

        StdDraw.enableDoubleBuffering();
        StdDraw.setXscale(0, 32768);
        StdDraw.setYscale(0, 32768);
        for (Point p : points) {
            p.draw();
        }
        StdDraw.show();
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            System.out.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
