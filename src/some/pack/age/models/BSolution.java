package some.pack.age.models;

import java.util.HashSet;
import java.util.List;

/**
 * @author DarkSeraphim.
 */
public class BSolution extends Solution
{

    public BSolution(int width, int height)
    {
        super(width, height);
    }

    public BSolution(Solution other)
    {
        super(other);
    }

    public BSolution(int width, int height, List<Point> points)
    {
        super(width, height, new HashSet<>(points));
    }
}
