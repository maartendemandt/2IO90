package some.pack.age.algorithm;

import some.pack.age.models.Point;
import some.pack.age.models.labels.AbstractLabel;
import some.pack.age.models.solution.Solution;

import java.util.Set;

/**
 * @author DarkSeraphim.
 */
public class DummyAlgorithm implements IAlgorithm
{

    @Override
    public Solution computePoints(Set<AbstractLabel> points, int width, int height)
    {
        Solution s = new Solution(width, height);
        points.forEach(s::add);
        return s;
    }

}
