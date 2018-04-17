import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree {

      private Node root;
          private int size;

              private static class Node {
                        private Point2D p;
                                private RectHV rect;
                                        private Node lb;
                                                private Node rt;

                                                        public Node(Point2D _p, Node _lb, Node _rt) {
                                                                      p = _p;
                                                                                  lb = _lb;
                                                                                              rt = _rt;
                                                                                                      }
                                                            }

                  public KdTree() {
                            size = 0;
                                }

                      public boolean isEmpty() {
                                return root == null;
                                    }

                          public int size() {
                                    return size;
                                        }

                              public void insert(Point2D p) {
                                        if (p == null) throw new java.lang.NullPointerException();
                                                root = put(root, p, true);
                                                    }

                                  public boolean contains(Point2D p) {
                                            if (p == null) throw new java.lang.NullPointerException();
                                                    return get(root, p, true);
                                                        }

                                      public static void main(String[] args) {
                                                KdTree kdtree = new KdTree();
                                                        kdtree.insert(new Point2D(0.7, 0.2));
                                                                kdtree.insert(new Point2D(0.5, 0.4));
                                                                        kdtree.insert(new Point2D(0.2, 0.3));
                                                                                kdtree.insert(new Point2D(0.4, 0.7));
                                                                                        kdtree.insert(new Point2D(0.9, 0.6));
                                                                                                assert(kdtree.get(new Point2D(0.9, 0.6)));
                                                                                                    }

                                          private boolean get(Node x, Point2D p, boolean use_x) {
                                                    if (x == null) return false;
                                                            if (x.compareTo(p) == 0) return true;
                                                                    if (use_x) {
                                                                                  if (p.x() < x.x()) return get(x.lb, p, !use_x);
                                                                                              else return get(x.rt, p, !use_x);
                                                                                                      }
                                                                            else {
                                                                                          if(p.y() < x.y()) return get(x.lb, p, !use_x);
                                                                                                      else return get(x.rt, p, !use_x);
                                                                                                              }
                                                                                }

                                              private Node put(Node x, Point2D p, boolean use_x) {
                                                        if (x == null) {
                                                                      size++;
                                                                                  return new Node(p, null, null);
                                                                                          }
                                                                if (x.compareTo(p) == 0) return x;
                                                                        if (use_x) {
                                                                                      if (p.x() < x.x()) { x.lb = put(x.lb, p, !use_x) }
                                                                                                  else { x.rt = put(x.rt, p, !use_x) }
                                                                                                          }
                                                                                else {
                                                                                              if (p.y() < x.y()) { x.lb = put(x.lb, p, !use_x) }
                                                                                                          else { x.rt = put(x.rt, p, !use_x) }
                                                                                                                  }
                                                                                        return x;
                                                                                            }

}

