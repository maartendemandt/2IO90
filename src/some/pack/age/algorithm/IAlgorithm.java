package some.pack.age.algorithm;

import some.pack.age.models.Point;
import some.pack.age.models.Solution;

import java.util.Set;
import some.pack.age.models.AbstractLabel;

/**
 * @author DarkSeraphim.
 */
public interface IAlgorithm
{
    public Solution computePoints(Set<AbstractLabel> points, int width, int height);
}
