import static org.junit.Assert.*;
import org.junit.Test;

public class RadixSortTest {

    // 测试普通的字符串数组排序
    @Test
    public void testSortRegularArray() {
        String[] input = {"apple", "banana", "orange", "grape", "pear"};
        String[] expected = {"apple", "banana", "grape", "orange", "pear"};
        String[] sorted = RadixSort.sort(input);
        assertArrayEquals(expected, sorted);
    }

    // 测试不同长度字符串的排序
    @Test
    public void testSortArrayWithVariableLengthStrings() {
        String[] input = {"a", "abc", "ab", "abcd", "ab"};
        String[] expected = {"a", "ab", "ab", "abc", "abcd"};
        String[] sorted = RadixSort.sort(input);
        assertArrayEquals(expected, sorted);
    }

    // 测试已经排序的数组
    @Test
    public void testSortAlreadySortedArray() {
        String[] input = {"apple", "banana", "grape", "orange", "pear"};
        String[] expected = {"apple", "banana", "grape", "orange", "pear"};
        String[] sorted = RadixSort.sort(input);
        assertArrayEquals(expected, sorted);
    }

    // 测试空数组
    @Test
    public void testSortEmptyArray() {
        String[] input = {};
        String[] expected = {};
        String[] sorted = RadixSort.sort(input);
        assertArrayEquals(expected, sorted);
    }

    // 测试只包含一个元素的数组
    @Test
    public void testSortSingleElementArray() {
        String[] input = {"apple"};
        String[] expected = {"apple"};
        String[] sorted = RadixSort.sort(input);
        assertArrayEquals(expected, sorted);
    }

    // 测试包含数字和字母的数组
    @Test
    public void testSortArrayWithNumbersAndLetters() {
        String[] input = {"abc123", "123abc", "abc456", "456abc"};
        String[] expected = {"123abc", "456abc", "abc123", "abc456"};
        String[] sorted = RadixSort.sort(input);
        assertArrayEquals(expected, sorted);
    }

    // 测试包含特殊字符的数组
    @Test
    public void testSortArrayWithSpecialCharacters() {
        String[] input = {"!apple", "#banana", "$grape", "@orange", "&pear"};
        String[] expected = {"!apple", "#banana", "$grape", "&pear", "@orange"};
        String[] sorted = RadixSort.sort(input);
        assertArrayEquals(expected, sorted);
    }

    // 测试字符大小写敏感
    @Test
    public void testSortCaseSensitive() {
        String[] input = {"apple", "Apple", "banana", "Banana"};
        String[] expected = {"Apple", "Banana", "apple", "banana"};
        String[] sorted = RadixSort.sort(input);
        assertArrayEquals(expected, sorted);
    }

    // 测试长字符串中的字母和数字的排序
    @Test
    public void testSortLongStringsWithNumbers() {
        String[] input = {
                "apple1234567890",
                "banana1234567890",
                "orange9876543210",
                "grape0123456789",
                "pear0012345678"
        };
        String[] expected = {
                "apple1234567890",
                "banana1234567890",
                "grape0123456789",
                "orange9876543210",
                "pear0012345678"
        };

        String[] sorted = RadixSort.sort(input);
        assertArrayEquals(expected, sorted);
    }

    // 测试完全相同的字符串
    @Test
    public void testSortIdenticalStrings() {
        String[] input = {"apple", "apple", "apple", "apple", "apple"};
        String[] expected = {"apple", "apple", "apple", "apple", "apple"};

        String[] sorted = RadixSort.sort(input);
        assertArrayEquals(expected, sorted);
    }
}
