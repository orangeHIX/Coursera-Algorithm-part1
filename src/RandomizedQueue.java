import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Created by hyx on 2016/10/6.
 */
public class RandomizedQueue<Item> implements Iterable<Item> {

    private static final int DEFAULT_CAPACITY = 1;
    private Item[] list;
    private int capacity;
    private int size;

    public RandomizedQueue() // construct an empty randomized queue
    {
        list = (Item[]) new Object[DEFAULT_CAPACITY];
        capacity = DEFAULT_CAPACITY;
        size = 0;
    }

    public boolean isEmpty() // is the queue empty?
    {
        return size == 0;
    }

    public int size() // return the number of items on the queue
    {
        return size;
    }

    public void enqueue(Item item) // add the item
    {
        if (item == null)
            throw new NullPointerException();
        if (isFull())
            resize(capacity * 2);
        list[size] = item;
        size++;
    }

    public Item dequeue() // remove and return a random item
    {
        if (isEmpty())
            throw new NoSuchElementException();
        int index = getRandomIndex();
        Item i = list[index];
        list[index] = list[size - 1];
        list[size - 1] = null;
        size--;
        if (isThin() && capacity / 2 >= DEFAULT_CAPACITY)
            resize(capacity / 2);
        return i;
    }

    public Item sample() // return (but do not remove) a random item
    {
        if (isEmpty())
            throw new NoSuchElementException();
        return list[getRandomIndex()];
    }

    private void resize(int n) {
        Item[] temp = (Item[]) new Object[n];
        int s = size();
        for (int i = 0; i < s; i++) {
            temp[i] = list[i];
        }
        list = temp;
        capacity = n;
    }

    private boolean isFull() {
        return size() == capacity;
    }

    private boolean isThin() {
        return size() < capacity / 4;
    }

    @Override
    public Iterator<Item> iterator() // return an independent iterator over items in random order
    {
        return new RandomQueueIterator();
    }

    private class RandomQueueIterator implements Iterator<Item> {
        private int[] index;
        private int curr;

        public RandomQueueIterator() {
            index = new int[size()];
            for (int i = 0; i < size(); i++) {
                index[i] = i;
            }
            StdRandom.shuffle(index);
            curr = 0;
        }

        @Override
        public boolean hasNext() {
            return curr < size();
        }

        @Override
        public Item next() {
            if (hasNext()) {
                return list[index[curr++]];
            } else {
                throw new NoSuchElementException();
            }
        }

        @Override
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private int getRandomIndex() {
        return (int) (StdRandom.uniform() * size());
    }

    private String toS() {
        return list.toString(); // Arrays.toString(list);
    }

    public static void main(String[] args) // unit testing
    {
        RandomizedQueue<Integer> rq = new RandomizedQueue<>();
        StdOut.println(rq.toS());
        for (int i = 0; i < 18; i++) {
            rq.enqueue(i + 1);
            StdOut.println("" + i + "th: " + rq.toS());
        }
        for (int i : rq) {
            StdOut.println(i);
        }

        for (int i = 0; i < 12; i++) {
            StdOut.println("remove: " + rq.dequeue());
            StdOut.println("" + i + "th: " + rq.toS());
        }
        rq.isEmpty();
        rq.enqueue(4);
        rq.dequeue();
        rq.enqueue(3);
        rq.dequeue();
    }

}
