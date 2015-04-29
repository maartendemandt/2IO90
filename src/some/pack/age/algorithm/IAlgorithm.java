package some.pack.age.algorithm;

import some.pack.age.models.Point;

import java.util.Set;

/**
 * @author DarkSeraphim.
 */
public interface IAlgorithm
{
    public Set<Point> computePoints(Set<Point> points, int width, int height);
}
