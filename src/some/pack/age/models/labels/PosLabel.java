package some.pack.age.models.labels;

import some.pack.age.LabelPosition;
import some.pack.age.models.AxisAlignedBB;
import some.pack.age.models.Point;
import some.pack.age.models.solution.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author DarkSeraphim.
 */
public class PosLabel extends AbstractLabel
{

    private final boolean four;

    private LabelPosition pos;

    private PosLabel(int x, int y, boolean four)
    {
        this(x, y, LabelPosition.NONE, four);
    }

    public PosLabel(int x, int y, LabelPosition pos, boolean four)
    {
        super(x, y);
        assert four || (pos == LabelPosition.NONE || pos == LabelPosition.NORTH_EAST || pos == LabelPosition.NORTH_WEST)
               : "2pos only accepts NORTH_EAST and NORTH_WEST";
        this.four = four;
        this.pos = pos;
    }

    private PosLabel(PosLabel point, LabelPosition newPos)
    {
        this(point.getX(), point.getY(), newPos, point.four);
    }

    private PosLabel(PosLabel label)
    {
        this(label.getX(), label.getY(), label.getPosition(), label.four);
    }

    public LabelPosition getPosition()
    {
        return this.pos;
    }
    
    public void remove()
    {
        this.setPosition(LabelPosition.NONE);
    }
    
    public void setPosition(LabelPosition pos)
    {
        this.pos = pos;
    }

    public String toString()
    {
        return String.format("%d %d %s", this.getX(), this.getY(), this.pos.toString());
    }

    private Optional<AbstractLabel> getRandomExcept(Solution solution, LabelPosition exclude)
    {
        Optional<AbstractLabel> newPoint = Optional.empty();
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

                PosLabel point = new PosLabel(this, pos);

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
                PosLabel point = new PosLabel(this, LabelPosition.NORTH_EAST);
                if (solution.isPossible(point))
                {
                    return Optional.of(point);
                }
                point = new PosLabel(this, LabelPosition.NORTH_WEST);
                if (solution.isPossible(point))
                {
                    return Optional.of(point);
                }
                return Optional.empty();
            }

            PosLabel point = new PosLabel(this, this.pos.getOtherTwoPos());
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
    public Optional<AbstractLabel> getRandomFreeLabel(Solution solution)
    {
        return this.getRandomExcept(solution, null);
    }

    @Override
    public Optional<AbstractLabel> getMutation(Solution solution)
    {
        Optional<AbstractLabel> mutation = this.getRandomExcept(solution, this.pos);
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

    public PosLabel getDefault()
    {
        return new PosLabel(this, LabelPosition.NONE);
    }

    public List<AbstractLabel> getCandidates(Solution solution)
    {
        final PosLabel self = this;
        if (!this.four)
        {
            return new ArrayList<AbstractLabel>(){
                {
                    add(new PosLabel(self, LabelPosition.NORTH_EAST));
                    add(new PosLabel(self, LabelPosition.NORTH_WEST));
                }

                public boolean remove(Object o)
                {
                    AbstractLabel.STRICT_EQUALS = true;
                    try
                    {
                        return super.remove(o);
                    }
                    finally
                    {
                        AbstractLabel.STRICT_EQUALS = false;
                    }
                }
            };
        }
        return new ArrayList<AbstractLabel>() {
            {
                add(new PosLabel(self, LabelPosition.NORTH_EAST));
                add(new PosLabel(self, LabelPosition.NORTH_WEST));
                add(new PosLabel(self, LabelPosition.SOUTH_WEST));
                add(new PosLabel(self, LabelPosition.SOUTH_EAST));
            }

            public boolean remove(Object o)
            {
                AbstractLabel.STRICT_EQUALS = true;
                try
                {
                    return super.remove(o);
                }
                finally
                {
                    AbstractLabel.STRICT_EQUALS = false;
                }
            }
        };
    }

    @Override
    public boolean isClone(AbstractLabel obj)
    {
        if (!(obj instanceof PosLabel))
        {
            return false;
        }
        PosLabel other = (PosLabel) obj;
        if (AbstractLabel.STRICT_EQUALS)
        {
            return this.pos == other.pos;
        }
        return this.equals(other) && this.pos == other.pos;
    }

    public PosLabel copy()
    {
        return new PosLabel(this);
    }

    public boolean isValid()
    {
        return this.pos != LabelPosition.NONE;
    }

    public static PosLabel create4PosLabel(int x, int y)
    {
        return new PosLabel(x, y, true);
    }

    public static PosLabel create2PosLabel(int x, int y)
    {
        return new PosLabel(x, y, false);
    }

}
