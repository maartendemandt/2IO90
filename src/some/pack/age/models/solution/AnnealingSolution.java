package some.pack.age.models;

import java.util.*;

/**
 * @author DarkSeraphim.
 */
public class AnnealingSolution extends Solution
{

    private int oldQuality = 0;

    private SolutionChange lastChange = new SolutionChange();

    public AnnealingSolution(int width, int height)
    {
        super(width, height);
    }

    public AnnealingSolution(Solution other)
    {
        super(other);
    }

    public AnnealingSolution(int width, int height, Set<Point<?>> points) {
        super(width, height, new HashSet<>(points));
    }
    
    public void change(LabelState current, LabelState next)
    {
        this.oldQuality = this.getQuality();
        this.lastChange.set(old, next);
    }
    
    public void reset()
    {
        this.lastChange.reset();
    }
    
    public void keep()
    {
        this.lastChange.update();
    }
}
