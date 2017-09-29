import java.util.*;

public class TreeCompression
{
    private BalancedBinarySearchTree<CharacterFrequency> dictionaryTree = new BalancedBinarySearchTree<>();
    private LinkedList<Boolean> encoded = new LinkedList<>();

    public static void main(String[] args)
    {
        TreeCompression tc = new TreeCompression();
        tc.alphaCompress("Am I compressed yet? I bet the longer the string the more efficient the compression!");

        BalancedBinarySearchTree<CharacterFrequency> dictionaryTree = tc.getTree();
        LinkedList<Boolean> encoded = tc.getEncoded();

        System.out.println(TreeCompression.decode(dictionaryTree, encoded));
    }

    private static String decode(BalancedBinarySearchTree<CharacterFrequency> dictionaryTree, Queue<Boolean> encoded)
    {
        return decode0(dictionaryTree, dictionaryTree, encoded);
    }

    private static String decode0(BalancedBinarySearchTree<CharacterFrequency> dictionaryTree, BalancedBinarySearchTree<CharacterFrequency> subTree, Queue<Boolean> encoded)
    {
        if (!encoded.isEmpty())
        {
            BalancedBinarySearchTree<CharacterFrequency> newSubTree = encoded.remove() ? subTree.getLeft() : subTree.getRight();
            if (newSubTree.isLeaf())
            {
                return newSubTree.getValue() + decode0(dictionaryTree, dictionaryTree, encoded);
            }
            else
            {
                return decode0(dictionaryTree, subTree, encoded);
            }
        }
        else
        {
            return "";
        }
    }

    private void alphaCompress(String s)
    {
        Map<Character, Integer> frequencyMap = new HashMap<>();

        for(Character c : s.toCharArray())
        {
            frequencyMap.put(c, frequencyMap.getOrDefault(c, 0)+1);
        }

        for (Map.Entry<Character, Integer> charFreq : frequencyMap.entrySet())
        {
            CharacterFrequency charFreqObj = new CharacterFrequency();
            charFreqObj.character = charFreq.getKey();
            charFreqObj.frequency = charFreq.getValue();
            dictionaryTree.addChild(charFreqObj);
        }

        System.out.println(dictionaryTree.toString());

        for (Character c : s.toCharArray())
        {
            CharacterFrequency charFreqObj = new CharacterFrequency();
            charFreqObj.character = c;
            charFreqObj.frequency = frequencyMap.get(c);
            encoded.addAll(dictionaryTree.getTraversal(charFreqObj));
        }
    }

    public BalancedBinarySearchTree<CharacterFrequency> getTree()
    {
        return dictionaryTree;
    }

    public LinkedList<Boolean> getEncoded()
    {
        return encoded;
    }
}

class CharacterFrequency implements Comparable<CharacterFrequency>
{
    Character character;
    int frequency;

    @Override
    public int compareTo(CharacterFrequency cf)
    {
        return new Integer(frequency).compareTo(cf.frequency);
    }

    @Override
    public String toString()
    {
        return character.toString();
    }
}