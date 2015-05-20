package some.pack.age.models;

import org.junit.Test;
import some.pack.age.LabelPosition;

public class SolutionTest
{

    @Test
    public void test4pos()
    {
        PosPoint point = new PosPoint(1, 1, LabelPosition.NORTH_EAST, true);
        AxisAlignedBB bb = point.getAABB(10, 10);
        System.out.println(bb);
        for (LabelPosition pos : LabelPosition.values())
        {
            if (pos == LabelPosition.NONE)
            {
                continue;
            }
            PosPoint other = new PosPoint(0, 0, pos, true);
            AxisAlignedBB otherAABB = other.getAABB(10, 10);
            System.out.println(otherAABB);
            System.out.println(otherAABB.overlaps(bb));
            System.out.println(bb.overlaps(otherAABB));
        }
    }

}