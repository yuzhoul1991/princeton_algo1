import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.In;
import java.util.Stack;

public class Solver {
    private MinPQ<Node> PQ = new MinPQ<Node>();
    private MinPQ<Node> twinPQ = new MinPQ<Node>();
    private Node finalNode;
    private boolean solvable;

    private class Node implements Comparable<Node> {
        private Board board;
        private int moves;
        private Node previous;
        private int priority;

        public Node(Board board, int moves, Node previous) {
            this.board = board;
            this.moves = moves;
            this.previous = previous;
            this.priority = moves + board.manhattan();
        }

        public int priority() {
            return priority;
        }

        public int compareTo(Node that) {
            return this.priority() - that.priority();
        }
    }

    public Solver(Board initial) {
        if (initial == null) {
            throw new java.lang.NullPointerException();
        }
        PQ.insert(new Node(initial, 0, null));
        twinPQ.insert(new Node(initial.twin(), 0, null));
        Node last = PQ.delMin();
        Node lastTwin = twinPQ.delMin();
        int counter = 0;
        while (!last.board.isGoal() && !lastTwin.board.isGoal()) {
            if (counter % 2 == 0) {
                for (Board b : last.board.neighbors()) {
                    if (last.previous != null && b.equals(last.previous.board)) continue;
                    PQ.insert(new Node(b, last.moves + 1, last));
                }
                last = PQ.delMin();
            }
            else {
                for (Board b : lastTwin.board.neighbors()) {
                    if (lastTwin.previous != null && b.equals(lastTwin.previous.board)) continue;
                    twinPQ.insert(new Node(b, lastTwin.moves + 1, last));
                }
                lastTwin = twinPQ.delMin();
            }
            counter++;
        }
        finalNode = (last.board.isGoal()) ? last : lastTwin;
        solvable = last.board.isGoal();
    }

    public boolean isSolvable() {
        return solvable;
    }

    public int moves() {
        return isSolvable() ? finalNode.moves : -1;
    }

    public Iterable<Board> solution() {
        if (!isSolvable()) return null;
        Stack<Board> boards = new Stack<Board>();
        Stack<Board> reverseBoards = new Stack<Board>();
        Node walk = finalNode;
        while (walk != null) {
            boards.push(walk.board);
            walk = walk.previous;
        }
        while (!boards.empty()) {
            reverseBoards.push(boards.pop());
        }
        return reverseBoards;
    }

    public static void main(String[] args) {

        // create initial board from file
        In in = new In(args[0]);
        int n = in.readInt();
        int[][] blocks = new int[n][n];
        for (int i = 0; i < n; i++)
            for (int j = 0; j < n; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);
        System.out.println(initial.toString());

        // solve the puzzle
        Solver solver = new Solver(initial);

        // print solution to standard output
        if (!solver.isSolvable())
            System.out.println("No solution possible");
        else {
            System.out.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                System.out.println(board);
        }
    }
}
