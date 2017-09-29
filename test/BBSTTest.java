import org.junit.Test;

import java.util.Random;

public class BBSTTest
{

    @Test
    public void test()
    {
        BalancedBinarySearchTree<Integer> tree = new BalancedBinarySearchTree<>();

        for (int i = 0; i < 3; i++)
        {
            tree.addChild(new Random().nextInt(100));
        }

        System.out.println(tree.indent(0));
    }
}
