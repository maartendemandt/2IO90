package some.pack.age.models;

import some.pack.age.LabelPosition;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author DarkSeraphim.
 */
public class PosPoint extends Point<PosPoint>
{

    private final boolean four;

    private final LabelPosition pos;

    private PosPoint(int x, int y, boolean four)
    {
        this(x, y, LabelPosition.NONE, four);
    }

    public PosPoint(int x, int y, LabelPosition pos, boolean four)
    {
        super(x, y);
        assert four || (pos == LabelPosition.NONE || pos == LabelPosition.NORTH_EAST || pos == LabelPosition.NORTH_WEST)
               : "2pos only accepts NORTH_EAST and NORTH_WEST";
        this.four = four;
        this.pos = pos;
    }

    private PosPoint(PosPoint point, LabelPosition newPos)
    {
        this(point.x, point.y, newPos, point.four);
    }

    public String toString()
    {
        return String.format("%d %d %s", this.x, this.y, this.pos.toString());
    }

    private Optional<Point> getRandomExcept(Solution solution, LabelPosition exclude)
    {
        Optional<Point> newPoint = Optional.empty();
        if (this.four)
        {
            LabelPosition[] poss = new LabelPosition[]{LabelPosition.NORTH_EAST, LabelPosition.NORTH_WEST, LabelPosition.SOUTH_EAST, LabelPosition.SOUTH_WEST};
            int start = ThreadLocalRandom.current().nextInt(poss.length);
            for (int i = 0; i < 4; i++)
            {
                int index = (start + i) % poss.length;
                LabelPosition pos = poss[index];
                if ((pos == exclude) || pos == LabelPosition.NONE)
                {
                    continue;
                }

                PosPoint point = new PosPoint(this, pos);

                if (solution.isPossible(point))
                {
                    newPoint = Optional.of(point);
                    break;
                }
            }
        }
        else
        {
            if (this.pos == LabelPosition.NONE)
            {
                PosPoint point = new PosPoint(this, LabelPosition.NORTH_EAST);
                if (solution.isPossible(point))
                {
                    return Optional.of(point);
                }
                point = new PosPoint(this, LabelPosition.NORTH_WEST);
                if (solution.isPossible(point))
                {
                    return Optional.of(point);
                }
                return Optional.empty();
            }

            PosPoint point = new PosPoint(this, this.pos.getOtherTwoPos());
            if (!solution.isPossible(point))
            {
                newPoint = Optional.empty();
            }
            else
            {
                newPoint = Optional.of(point);
            }
        }
        return newPoint;
    }

    @Override
    public Optional<Point> getRandomFreeLabel(Solution solution)
    {
        return this.getRandomExcept(solution, null);
    }

    @Override
    public Optional<Point> getMutation(Solution solution)
    {
        Optional<Point> mutation = this.getRandomExcept(solution, this.pos);
        if (!mutation.isPresent())
        {
            if (this.isValid() && solution.isPossible(this))
            {
                mutation = Optional.of(this);
            }
        }
        return mutation;
    }

    @Override
    public AxisAlignedBB getAABB(int width, int height)
    {
        int x = this.getX();
        int y = this.getY();
        int u = x + width * this.pos.getScaleX();
        int v = y + height * this.pos.getScaleY();
        if (u < x)
        {
            x = u;
            u = this.getX();
        }

        if (v < y)
        {
            y = v;
            v = this.getY();
        }
        return new AxisAlignedBB(x, y, u, v);
    }

    public PosPoint getDefault()
    {
        return new PosPoint(this, LabelPosition.NONE);
    }

    public List<Point> getCandidates(Solution solution)
    {
        final PosPoint self = this;
        if (!this.four)
        {
            return new ArrayList<Point>(){{
                add(self);
                add(new PosPoint(self, self.pos.getOtherTwoPos()));
            }};
        }
        return new ArrayList<Point>() {{
            add(new PosPoint(self, LabelPosition.NORTH_EAST));
            add(new PosPoint(self, LabelPosition.NORTH_WEST));
            add(new PosPoint(self, LabelPosition.SOUTH_WEST));
            add(new PosPoint(self, LabelPosition.SOUTH_EAST));
        }};
    }

    @Override
    public boolean isClone(PosPoint point)
    {
        return this.equals(point) && this.pos == point.pos;
    }

    public boolean isValid()
    {
        return this.pos != LabelPosition.NONE;
    }

    public static Point create4posPoint(int x, int y)
    {
        return new PosPoint(x, y, true);
    }

    public static Point create2posPoint(int x, int y)
    {
        return new PosPoint(x, y, false);
    }

}
