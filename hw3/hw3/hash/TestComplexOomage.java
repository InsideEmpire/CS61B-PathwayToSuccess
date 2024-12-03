package hw3.hash;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import edu.princeton.cs.algs4.StdRandom;

public class TestComplexOomage {

    @Test
    public void testHashCodeDeterministic() {
        ComplexOomage so = ComplexOomage.randomComplexOomage();
        int hashCode = so.hashCode();
        for (int i = 0; i < 100; i += 1) {
            assertEquals(hashCode, so.hashCode());
        }
    }

    /* This should pass if your OomageTestUtility.haveNiceHashCodeSpread
       is correct. This is true even though our given ComplexOomage class
       has a flawed hashCode. */
    @Test
    public void testRandomOomagesHashCodeSpread() {
        List<Oomage> oomages = new ArrayList<>();
        int N = 10000;

        for (int i = 0; i < N; i += 1) {
            oomages.add(ComplexOomage.randomComplexOomage());
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(oomages, 10));
    }

    /* Create a list of Complex Oomages called deadlyList
     * that shows the flaw in the hashCode function.
     */

    @Test
    public void testWithDeadlyParams() {
        List<Oomage> deadlyList = new ArrayList<>();

        int N = 100;
        int randomN = StdRandom.uniform(1, 10);
        ArrayList<Integer> randomList = new ArrayList<>(randomN);
        for (int i = 0; i < 1; i += 1) {
            randomList.add(StdRandom.uniform(0, 255));
        }

        ArrayList<Integer> suffix = new ArrayList<>();
        suffix.add(1);
        suffix.add(2);
        suffix.add(3);
        suffix.add(4);
        for (int i = 0; i < N; i++) {
            ArrayList<Integer> params = randomList;
            params.addAll(suffix);
            //
            for (Integer j : params) {
                System.out.print(j);
                System.out.print(" ");
            }
            System.out.println();
            Oomage temp = new ComplexOomage(params);
            System.out.println(temp.hashCode());
            //
            deadlyList.add(new ComplexOomage(params));
        }

        assertTrue(OomageTestUtility.haveNiceHashCodeSpread(deadlyList, 10));
    }

    /** Calls tests for SimpleOomage. */
    public static void main(String[] args) {
        jh61b.junit.textui.runClasses(TestComplexOomage.class);
    }
}
