
import java.util.Iterator;

public class Deque<Item> implements Iterable<Item> {
  private int N;
  private Node dummy;

  public Deque() {
    N = 0;
    dummy = new Node(null);
    dummy.next = dummy;
    dummy.prev = dummy;
  }

  private class Node {
    private Item item;
    private Node next;
    private Node prev;
    private Node(Item item) {
      this.item = item;
    }
  }

  public Iterator<Item> iterator() { return new DequeIterator(); }

  private class DequeIterator implements Iterator<Item> {
    private Node current = dummy.prev;
    public boolean hasNext() { return current != dummy; }
    public void remove() { throw new java.lang.UnsupportedOperationException(); }
    public Item next() {
      if (!hasNext()) { throw new java.util.NoSuchElementException(); }
      Item item = current.item;
      current = current.prev;
      return item;
    }
  }

  public boolean isEmpty() {
    return (dummy.next == dummy) && (dummy.prev == dummy);
  }

  public int size() {
    return N;
  }

  public void addFirst(Item item) {
    addNode(dummy.prev, item);
  }

  public void addLast(Item item) {
    addNode(dummy, item);
  }

  public Item removeFirst() {
    return removeNode(dummy.prev);
  }

  public Item removeLast() {
    return removeNode(dummy.next);
  }

  public static void main(String[] args) {
    Deque<Integer> deque = new Deque<Integer>();
    assert deque.isEmpty();
    assert deque.size() == 0;
    deque.addFirst(1);
    assert deque.size() == 1;
    deque.removeLast();
    assert deque.isEmpty();

    for (int i = 0; i < 10; i++) {
      if (i % 2 == 0) {
        deque.addFirst(i);
      }
      else {
        deque.addLast(i);
      }
    }

    for (int i = 0; i < 10; i++) {
      int item;
      if (i % 2 == 0) {
        item = deque.removeLast();
      }
      else {
        item = deque.removeFirst();
      }
    }
    assert deque.isEmpty();
  }

  private void addNode(Node previous, Item item) {
    checkAdd(item);
    Node new_node = new Node(item);
    new_node.prev = previous;
    new_node.next = previous.next;
    new_node.prev.next = new_node;
    new_node.next.prev = new_node;
    N++;
  }

  private Item removeNode(Node target) {
    checkRemove();
    target.prev.next = target.next;
    target.next.prev = target.prev;
    N--;
    return target.item;
  }

  private void checkAdd(Item item) {
    if (item == null) {
      throw new java.lang.NullPointerException();
    }
  }

  private void checkRemove() {
    if (isEmpty()) {
      throw new java.util.NoSuchElementException();
    }
  }
}
