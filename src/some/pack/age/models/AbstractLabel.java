package some.pack.age.models;

import java.util.List;
import java.util.Optional;

/**
 * @author DarkSeraphim.
 */
public abstract class AbstractLabel<T extends AbstractLabel> implements Point
{
    protected final Point point;

    public AbstractLabel(int x, int y)
    {
        this.point = Point.construct(x, y);
    }

    private AbstractLabel(Point point)
    {
        this(point.x, point.y);
    }

    @Override
    public int getX()
    {
        return this.point.getX();
    }

    @Override
    public int getY()
    {
        return this.point.getY();
    }

    public String toString()
    {
        return String.format("%d %d NIL", this.getX(), this.getY());
    }

    public abstract Optional<Point> getRandomFreeLabel(Solution solution);

    public abstract Optional<Point> getMutation(Solution solution);

    public abstract boolean isValid();

    public abstract AxisAlignedBB getAABB(int width, int height);

    public abstract T getDefault();

    @Override
    public final boolean equals(Object object)
    {
        if (!(object instanceof AbstractLabel))
        {
            return false;
        }
        AbstractLabel other = (AbstractLabel) object;
        return this.point.equals(other.point);
    }

    public abstract boolean isClone(T point);

    @Override
    public final int hashCode()
    {
        return this.point.hashCode();
    }

    public abstract List<Point<?>> getCandidates(Solution solution);
}
