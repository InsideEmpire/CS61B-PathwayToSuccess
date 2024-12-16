import java.util.ArrayList;

/**
 * Class for doing Radix sort
 *
 * @author Akhil Batra, Alexander Hwang
 */
public class RadixSort {
    /**
     * Does LSD radix sort on the passed in array with the following restrictions:
     * The array can only have ASCII Strings (sequence of 1 byte characters)
     * The sorting is stable and non-destructive
     * The Strings can be variable length (all Strings are not constrained to 1 length)
     *
     * @param asciis String[] that needs to be sorted
     * @return String[] the sorted array
     */
    public static String[] sort(String[] asciis) {
        int numDigitsInAnInteger = 0;

        for (int i = 0; i < asciis.length; i++) {
            if (asciis[i].length() > numDigitsInAnInteger) {
                numDigitsInAnInteger = asciis[i].length();
            }
        }

        String[] sortedAsciis = asciis;

        for (int i = 0; i < numDigitsInAnInteger; i++) {
            sortedAsciis = sortHelperLSD(sortedAsciis, i, numDigitsInAnInteger);
        }
        // print(asciis);
        // print(sortedAsciis);
        return sortedAsciis;
    }

    private static void print(String[] strings) {
        for (int i = 0; i < strings.length; i++) {
            System.out.print(strings[i] + ' ');
        }
        System.out.println();
    }

    /**
     * LSD helper method that performs a destructive counting sort the array of
     * Strings based off characters at a specific index.
     *
     * @param asciis Input array of Strings
     * @param index  The position to sort the Strings on.
     */
    private static String[] sortHelperLSD(String[] asciis, int index, int numDigitsInAnInteger) {
        ArrayList<String>[] buckets = new ArrayList[256];
        for (int i = 0; i < 256; i++) {
            buckets[i] = new ArrayList<>();
        }

        for (String str : asciis) {
            int charIndex;
            if (numDigitsInAnInteger - index > str.length()) {
                charIndex = 0;
            } else {
                charIndex = (int) str.charAt(numDigitsInAnInteger - index - 1);
            }
            buckets[charIndex].add(str);
        }

        int currentIndex = 0;
        for (ArrayList<String> bucket : buckets) {
            for (String str : bucket) {
                asciis[currentIndex++] = str;
            }
        }

        return asciis;
    }

    /**
     * MSD radix sort helper function that recursively calls itself to achieve the sorted array.
     * Destructive method that changes the passed in array, asciis.
     *
     * @param asciis String[] to be sorted
     * @param start  int for where to start sorting in this method (includes String at start)
     * @param end    int for where to end sorting in this method (does not include String at end)
     * @param index  the index of the character the method is currently sorting on
     **/
    private static void sortHelperMSD(String[] asciis, int start, int end, int index) {
        // Optional MSD helper method for optional MSD radix sort
        return;
    }
}
