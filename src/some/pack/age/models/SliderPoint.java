package some.pack.age.models;

import java.util.List;
import java.util.Optional;

/**
 * @author DarkSeraphim.
 */
public class SliderPoint extends Point<SliderPoint>
{

    private static final int PRECISION = 100;

    private final Optional<Float> slider;

    public SliderPoint(int x, int y)
    {
        this(x, y, Optional.empty());
    }

    private SliderPoint(int x, int y, float slider)
    {
        this(x, y, Optional.of(slider));
    }

    public SliderPoint(int x, int y, Optional<Float> slider)
    {
        super(x, y);
        this.slider = slider;
    }

    private SliderPoint(SliderPoint point)
    {
        this(point.x, point.y, point.slider);
    }

    public SliderPoint(SliderPoint point, float slider)
    {
        this(point.x, point.y, slider);
    }

    private Optional<Point> getRandomExcept(Solution solution, Optional<Float> except)
    {
        for (int i = 0; i < PRECISION; i++)
        {
            float f = (float)i / PRECISION;
            if (except.isPresent() && same(f, except.get()))
            {
                continue;
            }
            SliderPoint point = new SliderPoint(this, f);
            if (solution.isPossible(point))
            {
                return Optional.of(point);
            }
        }
        return Optional.empty();
    }

    private boolean same(float a, float b)
    {
        return Math.round(a * PRECISION) == Math.round(b * PRECISION);
    }

    @Override
    public Optional<Point> getRandomFreeLabel(Solution solution)
    {
        return getRandomExcept(solution, Optional.empty());
    }

    @Override
    public Optional<Point> getMutation(Solution solution)
    {
        return getRandomExcept(solution, Optional.empty());
    }

    @Override
    public List<Point> getAllMutations(Solution solution)
    {
        return null;
    }

    @Override
    public AxisAlignedBB getAABB(int width, int height)
    {
        float negSlider = 1 - this.slider.get();
        int x = this.x - (int)(width * negSlider);
        int u = this.x + (int)(width * this.slider.get());
        int v = this.y + height;
        return new AxisAlignedBB(x, this.y, u, v);
    }

    public SliderPoint getDefault()
    {
        return new SliderPoint(this.x, this.y, Optional.empty());
    }

    @Override
    public boolean isClone(SliderPoint point)
    {
        return this.equals(point) && this.slider.isPresent() == point.slider.isPresent() && sameSlider(this.slider.get(), point.slider.get());
    }

    private boolean sameSlider(float a, float b)
    {
        return Math.round(a * PRECISION) == Math.round(b * PRECISION);
    }

    public boolean isValid()
    {
        return this.slider.isPresent();
    }

    @Override
    public String toString()
    {
        if (this.slider.isPresent())
        {
            return String.format("%d %d %f", this.x, this.y, this.slider.get());
        }
        return String.format("%d %d NIL", this.x, this.y);
    }
}
