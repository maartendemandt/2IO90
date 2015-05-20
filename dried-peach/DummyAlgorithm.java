




import java.util.Set;

/**
 * @author DarkSeraphim.
 */
public class DummyAlgorithm implements IAlgorithm
{

    @Override
    public Solution computePoints(Set<Point> points, int width, int height)
    {
        return new Solution(width, height);
    }

}
