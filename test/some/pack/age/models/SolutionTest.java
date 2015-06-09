package some.pack.age.models;

import org.junit.Test;
import some.pack.age.LabelPosition;
import some.pack.age.models.labels.PosLabel;

public class SolutionTest
{

    @Test
    public void test4pos()
    {
        PosLabel point = new PosLabel(1, 1, LabelPosition.NORTH_EAST, true);
        AxisAlignedBB bb = point.getAABB(10, 10);
        System.out.println(bb);
        for (LabelPosition pos : LabelPosition.values())
        {
            if (pos == LabelPosition.NONE)
            {
                continue;
            }
            PosLabel other = new PosLabel(0, 0, pos, true);
            AxisAlignedBB otherAABB = other.getAABB(10, 10);
            System.out.println(otherAABB);
            System.out.println(otherAABB.overlaps(bb));
            System.out.println(bb.overlaps(otherAABB));
        }
    }

}