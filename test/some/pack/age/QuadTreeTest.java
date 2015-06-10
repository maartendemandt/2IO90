package some.pack.age;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import some.pack.age.models.labels.AbstractLabel;
import some.pack.age.quadtree.QuadTree;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import some.pack.age.models.Point;

import static org.junit.Assert.*;
import some.pack.age.models.quadtree.Node;

public class QuadTreeTest
{

    private static final int WIDTH = 10;
    
    private static final int HEIGHT = 10;

    private QuadTree tree;

    private Node addPoint(int x, int y)
    {
        return tree.insert(Point.construct(x, y));
    }
    
    private void confirmNeighbours(int x, int y, int amount)
    {
        confirmNeighbours(x, y, amount, Collections.<Point>emptyList());
    }
    
    private void confirmNeighbours(int x, int y, int amount, List<Point> points)
    {
        Set<Point> intersections = this.tree.intersect(Point.construct(x, y), WIDTH, HEIGHT);
        int actual = intersections.size();
        assertEquals(String.format("Invalid amount of intersections. Expected: %d, got: %d.", actual, amount),
                actual, amount);
        if (!points.isEmpty())
        {
            // Does this CME? It shouldn't, right?
            points.stream().filter(intersections::contains).forEach(points::remove);
            if (points.isEmpty())
            {
                System.out.println("The following points were missing:");
                points.forEach(System.out::println);
                fail("^");
            }
        }
    }

    @Before
    public void init()
    {
        this.tree = new QuadTree();
    }

    @Test
    public void testNoNeighbours()
    {
        addPoint(0, 0);
        addPoint(21, 21);
        addPoint(-21, 21);
        addPoint(-21, -21);
        addPoint(21, -21);
        confirmNeighbours(0, 0, 0);
    }
    
    // Add more tests!
    @Test
    public void testSingleNeighbour()
    {
        addPoint(0, 0);
        addPoint(1, 1);
        confirmNeighbours(0, 0, 1);
    }
    
    @Test
    public void testSingleNeighbourOnEdge()
    {
        Node root = addPoint(0, 0);
        Node node = addPoint(WIDTH * 2, HEIGHT * 2);
        confirmNeighbours(0, 0, 1);
    }
    
    @Test
    public void testSingleNeighbourJustOverEdge()
    {
        addPoint(0, 0);
        addPoint(WIDTH * 2 + 1, HEIGHT * 2 + 1);
        confirmNeighbours(0, 0, 0);
    }

}
