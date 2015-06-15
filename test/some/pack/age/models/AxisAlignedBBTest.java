package some.pack.age.models;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class AxisAlignedBBTest
{

    private AxisAlignedBB mainBox;

    @Before
    public void setUp() throws Exception
    {
        this.mainBox = new AxisAlignedBB(0, 0, 5, 5);
    }

    @Test
    public void testOnNorth()
    {
        check(0, 5, 5, 6);
    }

    @Test
    public void testOnSouth()
    {
        check(0, -2, 5, 3);
    }

    @Test
    public void testOnEast()
    {
        check(-2, 0, 2, 5);
    }

    @Test
    public void testOnWest()
    {
        check(3, 0, 7, 5);
    }

    @Test
    public void testContains()
    {
        check(-2, -2, 7, 7);
    }

    @Test
    public void testIsContained()
    {
        check(1, 1, 4, 4);
    }

    @Test
    public void testPartialOverlap()
    {
        check(-2, -2, 3, 3);
    }

    @Test
    public void testPartialOverlap1()
    {
        this.mainBox = new AxisAlignedBB(0, 1, 2, 3);
        check(1, 0, 5, 4);
    }

    @Test
    public void testPartialOverlap2()
    {
        this.mainBox = new AxisAlignedBB(0, 0, 2, 2);
        check(1, 1, 3, 3);
        System.out.println("Overlap 2");
    }

    @Test
    public void testCustom()
    {
        System.out.println(" == CUSTOM TEST == ");
        this.mainBox = new AxisAlignedBB(890, 2224, 890 + 10, 2224 - 20);
        check(893, 2204, 893 + 10, 2204 + 20);
    }

    private void check(int x, int y, int u, int v)
    {
        AxisAlignedBB aabb = new AxisAlignedBB(x, y, u, v);
        assertTrue(String.format("[%d, %d], [%d, %d] is not contained in main box", x, y, u, v), this.mainBox.overlaps(aabb));
    }
}