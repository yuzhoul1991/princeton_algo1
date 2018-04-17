
import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {

  private WeightedQuickUnionUF quickUnionObj;
  private int N;
  private int[][] square;
  private int[][] full;

  public Percolation(int N) {
    if (N <= 0) {
      throw new java.lang.IllegalArgumentException();
    }
    this.N = N;
    this.square = new int[N][N];
    this.full = new int[N][N];
    for (int i = 0; i < N; i++) {
      for (int j = 0; j < N; j++) {
        this.square[i][j] = 0;
        this.full[i][j] = 0;
      }
    }
    this.quickUnionObj = new WeightedQuickUnionUF(N*N + 2);
    int counter = 1;
    while (counter <= N) {
      this.quickUnionObj.union(0, counter);
      this.quickUnionObj.union(N*N+1, N*N+1-counter);
      counter++;
    }
  }

  public void open(int i, int j) {
    this.checkIndex(i, j);
    this.square[i-1][j-1] = 1;
    int union_index = getIndex(i, j);
    int top = union_index - this.N;
    int left = union_index - 1;
    int right = union_index + 1;
    int bottom = union_index + this.N;

    if (i == 1) {
      this.setFull(1, j);
    }

    if (this.hasTop(i, j) && this.isOpen(i-1, j)) {
      this.quickUnionObj.union(union_index, top);
      if (this.isFull(i-1, j)) {
        this.setFull(i, j);
      }
    }

    if (this.hasLeft(i, j) && this.isOpen(i, j-1)) {
      this.quickUnionObj.union(union_index, left);
      if (this.isFull(i, j-1)) {
        this.setFull(i, j);
      }
    }

    if (this.hasRight(i, j) && this.isOpen(i, j+1)) {
      this.quickUnionObj.union(union_index, right);
      if (this.isFull(i, j+1)) {
        this.setFull(i, j);
      }
    }

    if (this.hasBottom(i, j) && this.isOpen(i+1, j)) {
      this.quickUnionObj.union(union_index, bottom);
      if (this.isFull(i+1, j)) {
        this.setFull(i, j);
      }
    }
  }

  public boolean isOpen(int i, int j) {
    this.checkIndex(i, j);
    return this.square[i-1][j-1] == 1;
  }

  public boolean isFull(int i, int j) {
    this.checkIndex(i, j);
    return this.full[i-1][j-1] == 1;
  }

  public boolean percolates() {
    if (this.N == 1) {
      return this.isOpen(1, 1);
    }
    return this.quickUnionObj.connected(0, this.N*this.N+1);
  }

  private void setFull(int i, int j) {
    if (this.full[i-1][j-1] == 1) { return; }
    this.full[i-1][j-1] = 1;

    if (this.hasTop(i, j) && this.isOpen(i-1, j)) {
      this.setFull(i-1, j);
    }

    if (this.hasLeft(i, j) && this.isOpen(i, j-1)) {
      this.setFull(i, j-1);
    }

    if (this.hasRight(i, j) && this.isOpen(i, j+1)) {
      this.setFull(i, j+1);
    }

    if (this.hasBottom(i, j) && this.isOpen(i+1, j)) {
      this.setFull(i+1, j);
    }
  }

  private boolean hasTop(int i, int j) {
    return i > 1;
  }

  private boolean hasBottom(int i, int j) {
    return i < this.N;
  }

  private boolean hasLeft(int i, int j) {
    return j > 1;
  }

  private boolean hasRight(int i, int j) {
    return j < this.N;
  }

  private int getIndex(int i, int j) {
    return 1 + (i-1)*this.N + (j-1);
  }

  private void checkIndex(int i, int j) {
    if (i <= 0 || i > this.N) {
      throw new java.lang.IndexOutOfBoundsException("row index i out of bounds");
    }
    if (j <= 0 || j > this.N) {
      throw new java.lang.IndexOutOfBoundsException("column index j out of bounds");
    }
  }

}
