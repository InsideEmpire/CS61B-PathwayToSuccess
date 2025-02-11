import java.util.ArrayList;
import java.util.List;

public class HuffmanDecoder {
    public static void main(String[] args) {
        String encodeFileName = args[0];
        String decodeFileName = args[1];

        // 1: Read the Huffman coding trie.
        BinaryTrie huffmanTree;
        ObjectReader objectReader = new ObjectReader(encodeFileName);
        huffmanTree = (BinaryTrie) objectReader.readObject();

        // 3: Read the massive bit sequence corresponding to the original txt.
        BitSequence hugeBitSequence = (BitSequence) objectReader.readObject();

        // 4: Repeat until there are no more symbols:
        List<Match> matchList = new ArrayList<>();
        List<Character> characterList = new ArrayList<>();

        // 4a: Decode the massive bit sequence
        while (hugeBitSequence.length() > 0) {
            Match match = huffmanTree.longestPrefixMatch(hugeBitSequence);
            if (match != null) {
                int matchLength = match.getSequence().length();

                // Check if the length of the match sequence is greater than the remaining bits
                if (matchLength > hugeBitSequence.length()) {
                    // Handle this case: break or handle the remaining unmatched bits
                    System.out.println("Warning: Match length exceeds remaining bits.");
                    break;
                }

                hugeBitSequence = hugeBitSequence.allButFirstNBits(matchLength);
                matchList.add(match);
                characterList.add(match.getSymbol());
            } else {
                // Handle case where no match is found
                System.out.println("Error: No match found for the current bit sequence.");
                break;  // or throw an exception if you prefer
            }
        }

        // 5: Write the decoded symbols to the specified file.
        char[] charArray = new char[characterList.size()];
        for (int i = 0; i < characterList.size(); i++) {
            charArray[i] = characterList.get(i);
        }
        FileUtils.writeCharArray(decodeFileName, charArray);
    }
}
