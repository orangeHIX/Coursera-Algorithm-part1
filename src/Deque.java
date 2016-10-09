import edu.princeton.cs.algs4.StdOut;

import java.util.Collections;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by hyx on 2016/10/5.
 */
public class Deque<Item> implements Iterable<Item> {

    private static final int DEFAULT_CAPACITY = 1;

    private Item[] array;
    private int head;
    private int tail;
    private int capacity;

    public Deque() // construct an empty deque
    {
        head = 0;
        tail = 1;
        capacity = DEFAULT_CAPACITY;
        array = (Item[]) new Object[capacity + 1];
    }

    public boolean isEmpty() // is the deque empty?
    {
        return (array.length + tail - head) % array.length == 1;
    }

    public int size() // return the number of items on the deque
    {
        return (array.length + tail - head - 1) % array.length;
    }

    public void addFirst(Item item) // add the item to the front
    {
        if (item == null) {
            throw new NullPointerException();
        }
        if (isFull())
            resize(2 * capacity);
        array[head] = item;
        head = backward(head);
    }

    public void addLast(Item item) // add the item to the end
    {
        if (item == null) {
            throw new NullPointerException();
        }
        if (isFull())
            resize(2 * capacity);
        array[tail] = item;
        tail = forward(tail);
    }

    public Item removeFirst() // remove and return the item from the front
    {
        if (isEmpty())
            throw new NoSuchElementException();

        head = forward(head);
        Item item = array[head];
        array[head] = null;
        if (isThin() && capacity / 2 >= DEFAULT_CAPACITY)
            resize(capacity / 2);

        return item;
    }

    public Item removeLast() // remove and return the item from the end
    {
        if (isEmpty())
            throw new NoSuchElementException();

        tail = backward(tail);
        Item item = array[tail];
        array[tail] = null;
        if (isThin() && capacity / 2 >= DEFAULT_CAPACITY)
            resize(capacity / 2);

        return item;
    }

    @Override
    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private int curr;

        public DequeIterator() {
            curr = head;
        }

        @Override
        public boolean hasNext() {
            return forward(curr) != tail;
        }

        @Override
        public Item next() {
            if (hasNext()) {
                curr = forward(curr);
                return array[curr];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private void resize(int n) {
        Item[] temp = (Item[]) new Object[n + 1];
        int s = size();
        for (int i = 1; i < s + 1; i++) {
            temp[i] = array[(head + array.length + i) % array.length];
        }
        array = temp;
        head = 0;
        tail = head + s + 1;
        capacity = n;
    }

    private boolean isFull() {
        return head == tail;
    }

    private boolean isThin() {
        return size() < capacity / 4;
    }


    private int forward(int i) {
        if (i + 1 < array.length) {
            ++i;
        } else {
            i = 0;
        }
        return i;
    }

    private int backward(int i) {
        if (i - 1 >= 0) {
            --i;
        } else {
            i = array.length - 1;
        }
        return i;
    }

    private String toS() {
        return array// Arrays.toString(array)
                + " head:" + head
                + " tail:" + tail
                + " size:" + size()
                + " capacity:" + capacity;
    }

    public static void main(String[] args) // unit testing
    {
        Deque<Integer> d = new Deque<>();
//        StdOut.println(d.toS());
//        for (int i = 0; i < 18; i++) {
//            d.addLast(i + 1);
//            StdOut.println("" + i + "th: " + d.toS());
//        }
//        for (int i : d) {
//            StdOut.println(i);
//        }
//
//        for (int i = 0; i < 12; i++) {
//            d.removeLast();
//            StdOut.println("" + i + "th: " + d.toS());
//        }
        d.addFirst(0);
        d.removeLast();
        StdOut.println(d.toS() + "\n" + d.isEmpty());
    }
}
