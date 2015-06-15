package some.pack.age;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import some.pack.age.models.labels.AbstractLabel;
import some.pack.age.quadtree.QuadTree;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

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
        assertEquals(String.format("Invalid amount of intersections. Expected: %d, got: %d.", amount, actual),
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
    public void testQuadrantNeighbours()
    {
        addPoint(0, 0);
        addPoint(WIDTH, HEIGHT);
        addPoint(WIDTH, -HEIGHT);
        addPoint(-WIDTH, HEIGHT);
        addPoint(-WIDTH, -HEIGHT);
        confirmNeighbours(0, 0, 4);
    }

    @Test
    public void testTwoOutOfFourNeighbours()
    {
        addPoint(0, 0);
        addPoint(WIDTH, HEIGHT);
        addPoint(WIDTH, -HEIGHT);
        addPoint(-WIDTH, HEIGHT * 2 + 1);
        addPoint(-WIDTH, -HEIGHT * 2 - 1);
        confirmNeighbours(0, 0, 2);
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

    @Test
    public void testMultiDepthRandom20()
    {
        List<Point> points = new ArrayList<Point>(){{
            add(Point.construct(0, 0));
            for (int i = 1; i <= 5; i++)
            {
                add(Point.construct(i, i));
                add(Point.construct(-i, i));
                add(Point.construct(i, -i));
                add(Point.construct(-i, -i));
            }
        }};

        for (int i = 0; i < 20; i++)
        {
            this.tree = new QuadTree();
            Collections.shuffle(points);
            for (Point point : points)
            {
                addPoint(point.getX(), point.getY());
            }
            confirmNeighbours(0, 0, 20);
        }
    }
}
