
import edu.princeton.cs.algs4.StdIn;

public class Subset {
  public static void main(String[] args) {
    int k = Integer.parseInt(args[0]);

    RandomizedQueue<String> q = new RandomizedQueue<String>();

    while (!StdIn.isEmpty()) {
      String in = StdIn.readString();
      q.enqueue(in);
    }

    for (int i = 0; i < k; i++) {
      System.out.printf("%s\n", q.dequeue());
    }

  }
}
