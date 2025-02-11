import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HuffmanEncoder {

    public static Map<Character, Integer> buildFrequencyTable(char[] inputSymbols) {
        Map<Character, Integer> frequencyTable = new HashMap<>();
        for (int i = 0; i < inputSymbols.length; i++) {
            frequencyTable.put(inputSymbols[i], frequencyTable.getOrDefault(inputSymbols[i], 0) + 1);
        }
        return frequencyTable;
    }

    public static void main(String[] args) {
        String fileName = args[0];
        char[] inputSymbols = FileUtils.readFile(fileName);

        // 2: Build frequency table.
        Map<Character, Integer> frequencyTable = buildFrequencyTable(inputSymbols);

        // 3: Use frequency table to construct a binary decoding trie.
        BinaryTrie huffmanTree = new BinaryTrie(frequencyTable);

        // 4: Write the binary decoding trie to the .huf file.
        String newFileName = fileName + ".huf";
        ObjectWriter objectWriter = new ObjectWriter(newFileName);
        objectWriter.writeObject(huffmanTree);

        // 6: Use binary trie to create lookup table for encoding.
        Map<Character, BitSequence> lookupTable = huffmanTree.buildLookupTable();

        // 7: Create a list of bitsequences based on the input symbols.
        List<BitSequence> bitSequenceList = new ArrayList<>();
        for (char ch : inputSymbols) {
            // Look up the corresponding BitSequence for the current symbol
            BitSequence bitSequence = lookupTable.get(ch);
            bitSequenceList.add(bitSequence);
        }

        // 9: Assemble all bit sequences into one huge bit sequence.
        BitSequence hugeBitSequence = BitSequence.assemble(bitSequenceList);

        // 10: Write the huge bit sequence to the .huf file.
        objectWriter.writeObject(hugeBitSequence); // Write the encoded data
    }
}
