package some.pack.age.models;

import java.util.*;

/**
 * @author DarkSeraphim.
 */
public class BSolution extends Solution
{

    private final Map<Point, List<Point<?>>> candidates = new HashMap<>();

    public BSolution(int width, int height)
    {
        super(width, height);
    }

    public BSolution(Solution other)
    {
        super(other);
    }

    public BSolution(int width, int height, List<Point<?>> points) {
        super(width, height, new HashSet<>(points));
    }

    public List<Point<?>> getCandidates(Point<?> point)
    {
        List<Point<?>> points = this.candidates.get(point);
        if (points == null)
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

    public Set<Point<?>> getNeighbours(Point point)
    {
        return this.neighbours.get(point);
    }

    public List<Point<?>> getConflicts(Point candidate)
    {
        List<Point<?>> conflicts = new ArrayList<>();
        if (!candidate.isValid()) {
            AxisAlignedBB aabb = candidate.getAABB(this.width, this.height);
            for (Point<?> neighbour : getNeighbours(candidate)) {
                if (!neighbour.isValid()) {
                    continue;
                }
                AxisAlignedBB other = neighbour.getAABB(this.width, this.height);
                if (other.overlaps(aabb))
                {
                    conflicts.add(neighbour);
                }
            }
        }
        return conflicts;
    }
}
