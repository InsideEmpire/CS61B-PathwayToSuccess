import edu.princeton.cs.algs4.Queue;

public class QuickSort {
    /**
     * Returns a new queue that contains the given queues catenated together.
     *
     * The items in q2 will be catenated after all of the items in q1.
     */
    private static <Item extends Comparable> Queue<Item> catenate(Queue<Item> q1, Queue<Item> q2) {
        Queue<Item> catenated = new Queue<Item>();
        for (Item item : q1) {
            catenated.enqueue(item);
        }
        for (Item item: q2) {
            catenated.enqueue(item);
        }
        return catenated;
    }

    /** Returns a random item from the given queue. */
    private static <Item extends Comparable> Item getRandomItem(Queue<Item> items) {
        int pivotIndex = (int) (Math.random() * items.size());
        Item pivot = null;
        // Walk through the queue to find the item at the given index.
        for (Item item : items) {
            if (pivotIndex == 0) {
                pivot = item;
                break;
            }
            pivotIndex--;
        }
        return pivot;
    }

    /**
     * Partitions the given unsorted queue by pivoting on the given item.
     *
     * @param unsorted  A Queue of unsorted items
     * @param pivot     The item to pivot on
     * @param less      An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are less than the given pivot.
     * @param equal     An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are equal to the given pivot.
     * @param greater   An empty Queue. When the function completes, this queue will contain
     *                  all of the items in unsorted that are greater than the given pivot.
     */
    private static <Item extends Comparable> void partition(
            Queue<Item> unsorted, Item pivot,
            Queue<Item> less, Queue<Item> equal, Queue<Item> greater) {
        while (!unsorted.isEmpty()) {
            Item temp = unsorted.dequeue();
            if (temp.compareTo(pivot) < 0) {
                less.enqueue(temp);
            } else if (temp.compareTo(pivot) > 0) {
                greater.enqueue(temp);
            } else {
                equal.enqueue(temp);
            }
        }
    }

    /** Returns a Queue that contains the given items sorted from least to greatest. */
    public static <Item extends Comparable> Queue<Item> quickSort(
            Queue<Item> items) {
        Queue<Item> copy = new Queue<>();
        for (Item item : items) {
            copy.enqueue(item);
        }

        if (copy.isEmpty() || copy.size() == 1) {
            return copy;
        }
        Queue<Item> less = new Queue<>();
        Queue<Item> equal = new Queue<>();
        Queue<Item> greater = new Queue<>();
        partition(copy, getRandomItem(copy), less, equal, greater);
        less = quickSort(less);
        greater = quickSort(greater);
        return catenate(less, catenate(equal, greater));
    }

    public static void main(String[] args) {
        Queue<String> students = new Queue<>();
        students.enqueue("Vanessa");
        students.enqueue("Alice");
        students.enqueue("Josh");
        students.enqueue("Garrison");
        students.enqueue("Ethan");

        Queue<String> sortedStudents = quickSort(students);

        System.out.println("Original: " + students);
        System.out.println("Original size: " + students.size());

        System.out.println("Sorted: " + sortedStudents);
        System.out.println("Sorted size: " + sortedStudents.size());

    }

}
