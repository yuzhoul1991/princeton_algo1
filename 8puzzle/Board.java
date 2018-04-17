import java.lang.Math;
import java.util.Stack;
import edu.princeton.cs.algs4.In;

public class Board {
    private int N;
    private int[] board;

    public Board(int[][] blocks) {
        this.N = blocks.length;
        this.board = new int[N*N];
        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                board[i * N + j] = blocks[i][j];
            }
        }
    }

    public int dimension() {
        return N;
    }

    public int hamming() {
        int count = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) {
                continue;
            }
            if (board[i] - 1 != i) {
                count++;
            }
        }
        return count;
    }

    public int manhattan() {
        int distance = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0 || board[i] - 1 == i) {
                continue;
            }
            distance += Math.abs(getX(board[i] - 1) - getX(i));
            distance += Math.abs(getY(board[i] - 1) - getY(i));
        }
        return distance;
    }

    public boolean isGoal() {
        return hamming() == 0;
    }

    public Board twin() {
        int[][] newBlocks = new int[N][N];
        int a = -1;
        int b = -1;
        for (int i = 0; i < board.length; i++) {
            newBlocks[i/N][i%N] = board[i];
            if (board[i] != 0 && a == -1) {
                a = i;
                continue;
            }
            if (board[i] != 0 && a != -1 && b == -1) { b = i; }
        }
        int temp = newBlocks[a/N][a%N];
        newBlocks[a/N][a%N] = newBlocks[b/N][b%N];
        newBlocks[b/N][b%N] = temp;
        return new Board(newBlocks);
    }

    public boolean equals(Object y) {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (that.dimension() != this.dimension()) return false;
        for (int i = 0; i < this.board.length; i++) {
            if (this.board[i] != that.board[i]) return false;
        }
        return true;
    }

    public Iterable<Board> neighbors() {
        Stack<Board> allNeighbors = new Stack<Board>();
        int emptyBlockIdx = 0;
        for (int i = 0; i < board.length; i++) {
            if (board[i] == 0) {
                emptyBlockIdx = i;
                break;
            }
        }
        int above, down, left, right;
        above = down = left = right = -1;

        if (emptyBlockIdx >= N) {
            above = emptyBlockIdx - N;
            allNeighbors.push(generateNeighbor(emptyBlockIdx, above));
        }
        if (emptyBlockIdx + N < board.length) {
            down = emptyBlockIdx + N;
            allNeighbors.push(generateNeighbor(emptyBlockIdx, down));
        }
        if (emptyBlockIdx % N != 0) {
            left = emptyBlockIdx - 1;
            allNeighbors.push(generateNeighbor(emptyBlockIdx, left));
        }
        if ((emptyBlockIdx + 1) % N != 0 && (emptyBlockIdx + 1) < board.length) {
            right = emptyBlockIdx + 1;
            allNeighbors.push(generateNeighbor(emptyBlockIdx, right));
        }
        return allNeighbors;
    }

    public String toString() {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < board.length; i++) {
            s.append(String.format("%2d ", board[i]));
            if ((i + 1) % N == 0) {
                s.append("\n");
            }
        }
        return s.toString();
    }

    public static void main(String[] args) {
        for (String filename : args) {

            // read in the board specified in the filename
            In in = new In(filename);
            int n = in.readInt();
            int[][] tiles = new int[n][n];
            for (int i = 0; i < n; i++) {
                for (int j = 0; j < n; j++) {
                    tiles[i][j] = in.readInt();
                }
            }

            // solve the slider puzzle
            Board board = new Board(tiles);
            System.out.println("Board layout:\n");
            System.out.println(board.toString());
            assert (n == board.dimension());
            System.out.format("hamming %d, manhattan %d\n", board.hamming(), board.manhattan());
            System.out.println("Twin layout:\n");
            System.out.println(board.twin().toString());
            System.out.println("All neighbors:\n");
            for (Board b : board.neighbors()) {
                System.out.println(b.toString());
                System.out.println("");
            }
        }
    }

    private int getX(int index) {
        return index % N;
    }

    private int getY(int index) {
        return index  / N;
    }

    private Board generateNeighbor(int emptyBlockIdx, int exchangeIndex) {
        int[][] updateBlocks = new int[N][N];
        for (int i = 0; i < board.length; i++) {
            if (i == emptyBlockIdx) {
                updateBlocks[i/N][i%N] = board[exchangeIndex];
                continue;
            }
            if (i == exchangeIndex) {
                updateBlocks[i/N][i%N] = board[emptyBlockIdx];
                continue;
            }
            updateBlocks[i/N][i%N] = board[i];
        }
        return new Board(updateBlocks);
    }

}
