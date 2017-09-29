import com.sun.istack.internal.NotNull;
import jdk.nashorn.internal.runtime.regexp.joni.constants.OPCode;

import java.nio.file.OpenOption;
import java.util.*;
import java.util.stream.Stream;

public class BalancedBinarySearchTree<E extends Comparable<E>>
{
    private BalancedBinarySearchTree<E> parent = null;
    private E value = null;
    private Optional<BalancedBinarySearchTree<E>> left = Optional.empty();
    private Optional<BalancedBinarySearchTree<E>> right = Optional.empty();
    private int balancingFactor = 0;

    private BalancedBinarySearchTree(E value, BalancedBinarySearchTree<E> parent)
    {
        this.value = value;
        this.parent = parent;
    }

    public BalancedBinarySearchTree() {}

    public BalancedBinarySearchTree<E> getLeft()
    {
        return left.orElse(null);
    }

    public BalancedBinarySearchTree<E> getRight()
    {
        return right.orElse(null);
    }

    public boolean isLeaf()
    {
        return !left.isPresent() && !right.isPresent();
    }

    public E getValue()
    {
        return value;
    }

    private void balance()
    {
        int leftVal = left.map(BalancedBinarySearchTree::size).orElse(0);
        int rightVal = right.map(BalancedBinarySearchTree::size).orElse(0);

        balancingFactor = leftVal - rightVal;

        if (balancingFactor >= 2)
        {
            rotateClockwise();
        }
        if (balancingFactor <= -2)
        {
            rotateCounterclockwise();
        }

        if (parent != null)
        {
            parent.balance();
        }
    }

    private void rotateCounterclockwise()
    {
        E oldCurrentValue = value;
        value = removeMin(right).value;
        addChild(oldCurrentValue);

        balancingFactor--;
    }

    private void rotateClockwise()
    {
        E oldCurrentValue = value;
        value = removeMax(left).value;
        addChild(oldCurrentValue);

        balancingFactor--;
    }

    private static <E extends Comparable<E>> BalancedBinarySearchTree<E> removeMin(Optional<BalancedBinarySearchTree<E>> tree)
    {
        if (tree.isPresent())
        {
            if(tree.get().right.isPresent())
            {
                return removeMin(tree.get().right);
            }
            else
            {
                tree.get().parent.right = tree.get().left;
                return tree.get();
            }
        }
        return null;
    }

    private static <E extends Comparable<E>> BalancedBinarySearchTree<E> removeMax(Optional<BalancedBinarySearchTree<E>> tree)
    {
        if (tree.isPresent())
        {
            if(tree.get().left.isPresent())
            {
                return removeMax(tree.get().left);
            }
            else
            {
                tree.get().parent.left = tree.get().right;
                return tree.get();
            }
        }
        return null;
    }

    public void addChild(@NotNull E value)
    {
        if (this.value == null)
        {
            this.value = value;
        }
        else
        {
            if (value.compareTo(this.value) > 0)
            {
                if (left.isPresent())
                {
                    left.get().addChild(value);
                }
                else
                {
                    left = Optional.of(new BalancedBinarySearchTree<>(value, this));
                    this.balance();
                }
            }
            else
            {
                if (right.isPresent())
                {
                    right.get().addChild(value);
                }
                else
                {
                    right = Optional.of(new BalancedBinarySearchTree<>(value, this));
                    this.balance();
                }
            }
        }
    }

    public int size()
    {
        return getChildren().mapToInt(c -> c.map(BalancedBinarySearchTree::size).orElse(0)).sum() + 1;
    }


    @Override
    public String toString()
    {
        String res = value.toString();

        if (!isLeaf())
        {
            res += "(";
            res += left.map(BalancedBinarySearchTree::toString).orElse("_");
            res += ",";
            res += right.map(BalancedBinarySearchTree::toString).orElse("_");
            res += ")";
        }

        return res;
    }

    public String indent(int factor)
    {
        String result = NSpaces(factor, value.toString() + " : " + balancingFactor);

        result += left.map(l -> l.indent(factor + 4))
                .orElse(NSpaces(factor + 4, "X"));

        result += right.map(r -> r.indent(factor + 4))
                .orElse(NSpaces(factor + 4, "X"));

        return result;
    }

    private static String NSpaces(int factor, String str)
    {
        return String.format(String.format("%%%ds\n", factor+ str.length()), str);
    }

    public LinkedList<Boolean> getTraversal(E value)
    {
        if (value.compareTo(this.value) < 0)
        {
            LinkedList<Boolean> result = new LinkedList<>(Collections.singletonList(true));
            result.addAll(left.get().getTraversal(value));
            return result;
        }
        else if (value.compareTo(this.value) > 0)
        {
            LinkedList<Boolean> result = new LinkedList<>(Collections.singletonList(false));
            result.addAll(right.get().getTraversal(value));
            return result;
        }
        else
        {
            return new LinkedList<>();
        }

    }

    public Stream<Optional<BalancedBinarySearchTree<E>>> getChildren()
    {
        return Stream.of(left, right);
    }
}
