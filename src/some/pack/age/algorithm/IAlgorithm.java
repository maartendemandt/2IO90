package some.pack.age.algorithm;

import some.pack.age.models.solution.Solution;

import java.util.Set;
import some.pack.age.models.labels.AbstractLabel;

/**
 * @author DarkSeraphim.
 */
public interface IAlgorithm
{
    public Solution computePoints(Set<AbstractLabel> points, int width, int height);
}
