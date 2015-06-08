package some.pack.age.models;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author DarkSeraphim.
 */
public class SliderLabel extends AbstractLabel<SliderLabel>
{

    private static final int PRECISION = 100;

    private Optional<Float> slider;

    public SliderLabel(int x, int y)
    {
        this(x, y, Optional.empty());
    }

    private SliderLabel(int x, int y, float slider)
    {
        this(x, y, Optional.of(slider));
    }

    public SliderLabel(int x, int y, Optional<Float> slider)
    {
        super(x, y);
        this.slider = slider;
    }

    private SliderLabel(SliderLabel point)
    {
        this(point.getX(), point.getY(), point.slider);
    }

    public SliderLabel(SliderLabel point, float slider)
    {
        this(point.getX(), point.getY(), slider);
    }
    
    public Optional<Float> getSlider() 
    {
        return this.slider;
    }
    
    public void remove()
    {
        this.setSlider(Optional.empty());
    }
    
    public void setSlider(Optional<Float> slider)
    {
        this.slider = slider;
    }

    private Optional<Point> getRandomExcept(Solution solution, Optional<Float> except)
    {
        int start = ThreadLocalRandom.current().nextInt(PRECISION);
        for (int i = 0; i < PRECISION; i++)
        {
            float f = (float)((start + i) % PRECISION) / PRECISION;
            if (except.isPresent() && same(f, except.get()))
            {
                continue;
            }
            SliderLabel point = new SliderLabel(this, f);
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
    public AxisAlignedBB getAABB(int width, int height)
    {
        float negSlider = 1 - this.slider.get();
        int x = this.getX() - (int)(width * negSlider);
        int u = this.getX() + (int)(width * this.slider.get());
        int v = this.getY() + height;
        return new AxisAlignedBB(x, this.getY(), u, v);
    }

    public SliderLabel getDefault()
    {
        return new SliderLabel(this.getX(), this.getY(), Optional.empty());
    }

    @Override
    public boolean isClone(SliderLabel point)
    {
        return this.equals(point) && this.slider.isPresent() == point.slider.isPresent() && sameSlider(this.slider.get(), point.slider.get());
    }

    @Override
    public List<AbstractLabel<SliderLabel>> getCandidates(Solution solution)
    {
        final int x = this.getX();
        final int y = this.getY();
        return new ArrayList<AbstractLabel<SliderLabel>>() {{
            for (int i = 0; i < PRECISION; i++)
            {
                float slider = ((float)i / PRECISION);
                add(new SliderLabel(x, y, slider));
            }
        }};
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
            return String.format("%d %d %f", this.getX(), this.getY(), this.slider.get());
        }
        return String.format("%d %d NIL", this.getX(), this.getY());
    }
}
