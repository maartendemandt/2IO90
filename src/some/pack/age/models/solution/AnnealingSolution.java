package some.pack.age.models.solution;

import some.pack.age.LabelPosition;
import some.pack.age.models.Point;
import some.pack.age.models.labels.AbstractLabel;
import some.pack.age.models.labels.LabelState;
import some.pack.age.models.labels.PosLabel;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.function.Consumer;

/**
 * @author DarkSeraphim.
 */
public class AnnealingSolution extends Solution
{

    private List<AbstractLabel> interesting = new ArrayList<>();

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

    @Override
    public void recomputeNeighbours()
    {
        super.recomputeNeighbours();
        this.interesting.clear();
        this.neighbours.entrySet().stream()
                                  .filter(entry -> entry.getValue() != null && entry.getValue().size() > 0)
                                  .filter(entry -> entry.getKey() instanceof AbstractLabel)
                                  .map(Map.Entry::getKey)
                                  .forEach(entry -> this.interesting.add((AbstractLabel) entry));
    }

    @Override
    public AbstractLabel getRandomLabel()
    {
        if (this.interesting.isEmpty())
        {
            return super.getRandomLabel();
        }
        return this.interesting.get(ThreadLocalRandom.current().nextInt(this.interesting.size()));
    }

    public AnnealingSolution copy()
    {
        Set<AbstractLabel> clone = new LinkedHashSet<>();
        this.getPoints().stream()
                        .map(AbstractLabel::copy)
                        .forEach(clone::add);
        return new AnnealingSolution(this.width, this.height, clone);
    }
}
