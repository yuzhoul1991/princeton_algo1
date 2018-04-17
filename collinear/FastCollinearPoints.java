import java.util.ArrayList;
import java.util.Collections;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.In;

public class FastCollinearPoints {

    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
    private ArrayList<Point> allPoints = new ArrayList<Point>();
    private ArrayList<Point> startPoints = new ArrayList<Point>();
    private ArrayList<Point> endPoints = new ArrayList<Point>();

    public FastCollinearPoints(Point[] points) {
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
        for (int i = 0; i < allPoints.size(); i++) {
            Point p = allPoints.get(i);
            ArrayList<Point> rest = (ArrayList<Point>) allPoints.clone();
            rest.remove(i);
            Collections.sort(rest, p.slopeOrder());
            double lastSlope = +0.0;
            int counter = 0;
            ArrayList<Point> segmentPoints = new ArrayList<Point>();
            // System.out.format("base point: %s\n", p);
            for (int j = 0; j < rest.size(); j++) {
                double currentSlope = p.slopeTo(rest.get(j));
                // System.out.format(" j: %d, counter: %d, points: %s, currentSlope: %f, lastSlope: %f\n", j, counter, rest.get(j), currentSlope, lastSlope);

                if (currentSlope == lastSlope && j != 0) {
                    counter++;
                    segmentPoints.add(rest.get(j));
                    if (counter >= 2) {
                        segmentPoints.add(p);
                        Point startPoint = Collections.min(segmentPoints);
                        Point endPoint = Collections.max(segmentPoints);
                        boolean existing = false;
                        double newSlope = startPoint.slopeTo(endPoint);

                        // System.out.format(" startPoint %s, endPoint %s, newSlope %f\n", startPoint, endPoint, newSlope);
                        if (startPoints.contains(startPoint)) {
                            int idx = startPoints.indexOf(startPoint);
                            Point existingEnd = endPoints.get(idx);
                            double testSlope = startPoint.slopeTo(existingEnd);
                            if (newSlope == testSlope && endPoint.compareTo(existingEnd) > 0) {
                                existing = true;
                                endPoints.set(idx, endPoint);
                            }
                        }

                        if (endPoints.contains(endPoint)) {
                            int idx = endPoints.indexOf(endPoint);
                            Point existingStart = startPoints.get(idx);
                            double testSlope = existingStart.slopeTo(endPoint);
                            if (newSlope == testSlope && startPoint.compareTo(existingStart) < 0) {
                                existing = true;
                                startPoints.set(idx, startPoint);
                            }
                        }

                        for (int x = 0; x < startPoints.size(); x++) {
                            if (existing) {
                                break;
                            }
                            Point existingStart = startPoints.get(x);
                            Point existingEnd = endPoints.get(x);
                            double existingSlope = existingStart.slopeTo(existingEnd);
                            double testSlope = existingStart.slopeTo(startPoint);
                            //  System.out.format("existingStart %s, startPoint %s\n", existingStart, startPoint);
                            //  System.out.format(" existingSlope %f, testSlope %f, newSlope %f\n", existingSlope, testSlope, newSlope);
                            if (newSlope == existingSlope && (newSlope == testSlope || testSlope == Double.NEGATIVE_INFINITY)) {
                                existing = true;
                                if (endPoint.compareTo(existingEnd) > 0) {
                                    endPoints.set(x, endPoint);
                                }
                                if (startPoint.compareTo(existingStart) < 0) {
                                    startPoints.set(x, startPoint);
                                }
                                break;
                            }
                        }
                        if (!existing) {
                            startPoints.add(startPoint);
                            endPoints.add(endPoint);
                        }
                    }
                }
                else {
                    segmentPoints.clear();
                    counter = 0;
                    lastSlope = currentSlope;
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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            System.out.println(segment);
            segment.draw();
        }
        StdDraw.show();
    }
}
