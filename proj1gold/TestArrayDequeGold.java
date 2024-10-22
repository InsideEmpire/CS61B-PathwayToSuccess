import static org.junit.Assert.*;
import org.junit.Test;

public class TestArrayDequeGold {
    @Test
    public void testDeque() {
        // @source StudentArrayDequeLauncher
        StudentArrayDeque<Integer> actual = new StudentArrayDeque<>();
        ArrayDequeSolution<Integer> expected = new ArrayDequeSolution<>();
        String statement = "";

        for (int i = 0; i < 100; i += 1) {
            actual.addFirst(i);
            statement += "addFirst(" + i + ")\n";
            expected.addFirst(i);
        }
        //actual.printDeque();
//        for (int i = 0; i < 100; i++) {
//            assertEquals("Oh noooo!\nThis is bad:\n at " + String.valueOf(i) + " Deque number " + actual.get(i)
//                            + " not equal to " + expected.get(i) + "!",
//                    expected.get(i), actual.get(i));
//        }


        for (int i = 0; i < 100; i += 1) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.5) {
                expected.addLast(i);
                statement += "addLast(" + i + ")\n";
                actual.addLast(i);
            } else {
                expected.addFirst(i);
                statement += "addFirst(" + i + ")\n";
                actual.addFirst(i);
            }
        }
        //actual.printDeque();

//        for (int i = 0; i < 100; i++) {
//            assertEquals("Oh noooo!\nThis is bad:\n at " + String.valueOf(i) + " Deque number " + actual.get(i)
//                            + " not equal to " + expected.get(i) + "!",
//                    expected.get(i), actual.get(i));
//        }

        for (int i = 0; i < 30; i++) {
            double numberBetweenZeroAndOne = StdRandom.uniform();

            if (numberBetweenZeroAndOne < 0.5) {
                int tem1, tem2;
                tem1 = expected.removeFirst();
                tem2 = actual.removeFirst();
                statement += "removeFirst(): " + tem2;
                assertEquals(statement, tem1, tem2);
            } else {
                int tem1, tem2;
                tem1 = expected.removeLast();
                tem2 = actual.removeLast();
                statement += "removeLast(): " + tem2;
                assertEquals(statement, tem1, tem2);
            }
        }

//        for (int i = 0; i < 70; i++) {
//            assertEquals("Oh noooo!\nThis is bad:\n at " + String.valueOf(i) + " Deque number " + actual.get(i)
//                            + " not equal to " + expected.get(i) + "!",
//                    expected.get(i), actual.get(i));
//        }
    }
}
