package some.pack.age.models;

import java.util.*;

/**
 * @author DarkSeraphim.
 */
public class AnnealingSolution extends Solution
{

    private double oldQuality;
    
    private SolutionChange lastChange = new SolutionChange();

    public AnnealingSolution(int width, int height)
    {
        super(width, height);
    }

    public AnnealingSolution(Solution other)
    {
        super(other);
    }

    public AnnealingSolution(int width, int height, Set<AbstractLabel<?>> points) {
        super(width, height, points);
    }
    
    public void change(AbstractLabel current, AbstractLabel next)
    {
        this.oldQuality = this.getQuality();
        this.lastChange.set();
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
