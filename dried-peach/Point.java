

import java.util.List;
import java.util.Optional;

/**
 * @author DarkSeraphim.
 */
public abstract class Point<T extends Point>
{
    protected final int x;

    protected final int y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    private Point(Point point)
    {
        this(point.x, point.y);
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public String toString()
    {
        return String.format("%d %d NIL", this.x, this.y);
    }

    public abstract Optional<Point> getRandomFreeLabel(Solution solution);

    public abstract Optional<Point> getMutation(Solution solution);

    public abstract List<Point> getAllMutations(Solution solution);

    public abstract AxisAlignedBB getAABB(int width, int height);

    @Override
    public final boolean equals(Object object)
    {
        if (!(object instanceof Point))
        {
            return false;
        }
        Point point = (Point) object;
        return point.x == this.x && point.y == this.y;
    }

    public abstract boolean isClone(T point);

    @Override
    public final int hashCode()
    {
        int result = 19;
        result = 37 * result + x;
        result = 37 * result + y;
        return result;
    }

    public List<Point> getCandidates(Solution solution)
    {
        return this.getAllMutations(solution);
    }
}
