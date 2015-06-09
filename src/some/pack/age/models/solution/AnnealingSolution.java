package some.pack.age.models.solution;

import some.pack.age.models.labels.AbstractLabel;
import some.pack.age.models.labels.LabelState;

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

    public AnnealingSolution(int width, int height, Set<AbstractLabel> points) {
        super(width, height, points);
    }
    
    public void change(AbstractLabel current, AbstractLabel next)
    {
        this.oldQuality = this.getQuality();
        this.lastChange.set(LabelState.prepare(current, next));
        this.keep();
    }

    public void remove(AbstractLabel current)
    {
        this.oldQuality = this.getQuality();
        this.lastChange.set(LabelState.prepareRemove(current));
        this.keep();
    }

    public double getOldQuality()
    {
        return this.oldQuality;
    }
    
    public void reset()
    {
        this.lastChange.undo();
    }
    
    public void keep()
    {
        this.lastChange.update();
    }
}
