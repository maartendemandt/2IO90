package some.pack.age.models.labels;

import some.pack.age.models.AxisAlignedBB;
import some.pack.age.models.Point;
import some.pack.age.models.solution.Solution;

import java.util.List;
import java.util.Optional;

/**
 * @author DarkSeraphim.
 */
public abstract class AbstractLabel implements Point
{
    protected final Point point;

    public AbstractLabel(int x, int y)
    {
        this.point = Point.construct(x, y);
    }

    private AbstractLabel(Point point)
    {
        this(point.getX(), point.getY());
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

    public abstract Optional<AbstractLabel> getRandomFreeLabel(Solution solution);

    public abstract Optional<AbstractLabel> getMutation(Solution solution);

    public abstract boolean isValid();

    public abstract AxisAlignedBB getAABB(int width, int height);

    public abstract AbstractLabel getDefault();

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

    public abstract boolean isClone(AbstractLabel point);

    @Override
    public final int hashCode()
    {
        return this.point.hashCode();
    }

    public abstract List<AbstractLabel> getCandidates(Solution solution);

    public abstract AbstractLabel copy();
}
