
import java.util.Iterator;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
  private Item[] arr;
  private int N;

  public RandomizedQueue() {
    arr = (Item[]) new Object[2];
    N = 0;
  }

  public boolean isEmpty() {
    return N == 0;
  }

  public int size() {
    return N;
  }

  public void enqueue(Item item) {
    if (item == null) {
      throw new java.lang.NullPointerException();
    }

    if (N == arr.length) { resize(2*N); }
    arr[N++] = item;
  }

  /*
  public void print() {
    for (int i = 0; i < arr.length; i++) {
      System.out.printf("%s\n", arr[i]);
    }
  }
  */

  public Item sample() {
    if (isEmpty()) { throw new java.util.NoSuchElementException(); }
    int index = StdRandom.uniform(N);
    return arr[index];
  }

  public Item dequeue() {
    if (isEmpty()) { throw new java.util.NoSuchElementException(); }
    int index = StdRandom.uniform(N);
    Item choosen = arr[index];
    Item last = arr[N-1];
    N--;

    arr[index] = last;
    //System.out.printf("index: %d, N: %d\n", index, N);

    arr[N] = null;

    if (N > 0 && N == arr.length/4) { resize(arr.length/2); }
    return choosen;
  }

  public static void main(String[] args) {
    RandomizedQueue<Integer> q = new RandomizedQueue<Integer>();
    assert q.isEmpty();
    assert q.size() == 0;
    for (int i = 0; i < 10; i++) {
      q.enqueue(i);
    }

    for (int i : q) {
      System.out.println(i);
    }

    for (int i = 0; i < 10; i++) {
      q.dequeue();
    }

    assert q.isEmpty();

    RandomizedQueue<String> rq = new RandomizedQueue<String>();
    rq.enqueue("FF");
    rq.dequeue();
    //rq.print();
  }

  public Iterator<Item> iterator() { return new RandomizedQueueIterator(); }

  private class RandomizedQueueIterator implements Iterator<Item> {
    private Item[] cpy;
    private int i;

    public RandomizedQueueIterator() {
      cpy = (Item[]) new Object[N];
      for (int j = 0; j < N; j++) {
        cpy[j] = arr[j];
      }
      i = N-1;
      StdRandom.shuffle(cpy);
    }

    public boolean hasNext() {
      return i >= 0;
    }

    public Item next() {
      if (!hasNext()) { throw new java.util.NoSuchElementException(); }
      return cpy[i--];
    }

    public void remove() {
      throw new java.lang.UnsupportedOperationException();
    }
  }

  private void resize(int capacity) {
    assert capacity >= N;
    Item[] temp = (Item[]) new Object[capacity];
    for (int i = 0; i < N; i++) {
      temp[i] = arr[i];
    }
    arr = temp;
  }
}
