package some.pack.age.models;

import org.junit.Test;
import some.pack.age.quadtree.QuadTree;

public class SolutionTest
{

    private static final int WIDTH = 10;
    
    private static final int HEIGHT = 10;

    private QuadTree tree;

    private void addPoint(int x, int y)
    {
        tree.insert(Point.construct(x, y));
    }
    
    private void confirmNeighbours(int x, int y, int amount)
    {
        confirmNeighbours(x, y, amount, Collections.emptyList());
    }
    
    private void confirmNeighbours(int x, int y, int amount, List<Point> points)
    {
        Set<Point> intersections = this.tree.intersect(Point.construct(x, y)).size();
        int actual = intersections.size();
        Assert.equals(actual, amount, String.format("Invalid amount of intersections. Expected: %d, got: %d.", actual, amount));
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
        addPoint(0,0);
        addPoint(21, 21);
        addPoint(-21, 21);
        addPoint(-21, -21);
        addPoint(21, -21);
        confirmNeighbours(0, 0, 0);
    }
    
    // Add more tests!

}
