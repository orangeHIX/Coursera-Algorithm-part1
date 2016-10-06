/**
 * Created by hyx on 2016/10/6.
 */
public class LinkedList<T> {
    private Node<T> head;
    private Node<T> last;
    private int size;

    public LinkedList() {
        head = new Node<>();
        last = head;
        size = 0;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public void add(T t) {
        last.next = new Node<>();
        last.next.item = t;
        last = last.next;
        size++;
    }

    public T get(int index) {
        if (index >= 0 && index < size()) {
            Node<T> p = head;
            for (int i = 0; i < index + 1; i++) {
                p = p.next;
            }
            return p.item;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public T remove(int index) {
        if (index >= 0 && index < size()) {
            Node<T> curr = head;
            Node<T> pre = null;
            for (int i = 0; i < index + 1; i++) {
                pre = curr;
                curr = curr.next;
            }
            if (last == curr) {
                last = pre;
            }
            pre.next = curr.next;
            size--;
            return curr.item;
        } else {
            throw new IndexOutOfBoundsException();
        }
    }

    public int size() {
        return size;
    }

    public String toString() {
        String s = "LinkedList size: " + size + " [ ";
        Node<T> p = head.next;
        while (p != null) {
            s += p.item;
            s += " ";
            p = p.next;
        }
        s += "]";
        return s;
    }

    private class Node<T> {
        private T item;
        private Node<T> next;

        public T item() {
            return item;
        }

        public Node<T> next() {
            return next;
        }
    }
}
