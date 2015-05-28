package some.pack.age.models;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;

/**
 * @author DarkSeraphim.
 */
public class BSolution extends Solution
{

    private final Map<Point, List<Point>> candidates = new HashMap<Point, List<Point>>();

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

    public List<Point> getCandidates(Point point)
    {
        List<Point> points = this.candidates.get(point);
        if (point == null)
        {
            points = point.getCandidates(this);
            this.candidates.put(point, points);
        }
        return points;
    }

    public void removeCandidate(Point point)
    {
        // O sweet irony, why do you work so nicely
        getCandidates(point).remove(point);
    }
}
