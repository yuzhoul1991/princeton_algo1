import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {

  private int T;
  private int N;
  private double[] result;

  public PercolationStats(int N, int T) {
    if (N <= 0 || T <= 0) {
      throw new java.lang.IllegalArgumentException();
    }
    this.T = T;
    this.N = N;
    this.result = new double[T];

    for (int i = 0; i < T; i++) {
      Percolation perc = new Percolation(N);
      this.oneRun(i, perc);
    }
  }

  public double mean() {
    return StdStats.mean(this.result);
  }

  public double stddev() {
    return StdStats.stddev(this.result);
  }

  public double confidenceLo() {
    return (this.mean() - 1.96*this.stddev()/Math.sqrt(this.T));
  }

  public double confidenceHi() {
    return (this.mean() + 1.96*this.stddev()/Math.sqrt(this.T));
  }

  public static void main(String[] args) {
    int N = Integer.parseInt(args[0]);
    int T = Integer.parseInt(args[1]);
    PercolationStats obj = new PercolationStats(N, T);
    System.out.printf("mean                      = %f\n", obj.mean());
    System.out.printf("stddev                    = %f\n", obj.stddev());
    System.out.printf("95%% confidence interval  = %f, %f\n", obj.confidenceLo(), obj.confidenceHi());
  }

  private void oneRun(int index, Percolation perc) {
    int counter = 0;
    while (!perc.percolates()) {
      int i = StdRandom.uniform(1, this.N+1);
      int j = StdRandom.uniform(1, this.N+1);

      if (perc.isOpen(i, j)) { continue; }

      perc.open(i, j);
      counter++; 
    }
    this.result[index] = (double) counter/(this.N*this.N);
  }
}
