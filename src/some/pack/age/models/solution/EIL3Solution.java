package some.pack.age.models.solution;

import some.pack.age.models.labels.AbstractLabel;
import some.pack.age.models.AxisAlignedBB;
import some.pack.age.models.Point;

import java.util.*;

/**
 * @author DarkSeraphim.
 */
public class EIL3Solution extends Solution
{

    private final Map<Point, List<AbstractLabel>> candidates = new HashMap<>();

    public EIL3Solution(int width, int height)
    {
        super(width, height);
    }

    public EIL3Solution(Solution other)
    {
        super(other);
    }

    public EIL3Solution(int width, int height, List<AbstractLabel> points) {
        super(width, height, new LinkedHashSet<>(points));
    }

    public List<AbstractLabel> getCandidates(AbstractLabel label)
    {
        List<AbstractLabel> labels = this.candidates.get(label);
        if (labels == null)
        {
            labels = label.getCandidates(this);
            this.candidates.put(label, labels);
        }
        return labels;
    }

    public void change(AbstractLabel old, AbstractLabel _new)
    {
        super.change(old, _new);
    }

    public void removeCandidate(AbstractLabel label)
    {
        // O sweet irony, why do you work so nicely
        getCandidates(label).remove(label);
    }

    public List<AbstractLabel> getConflicts(AbstractLabel candidate)
    {
        List<AbstractLabel> conflicts = new ArrayList<>();
        if (candidate.isValid())
        {
            AxisAlignedBB aabb = candidate.getAABB(this.width, this.height);
            for (AbstractLabel neighbour : getNeighbours(candidate))
            {
                for (AbstractLabel neighbourCandidate : this.getCandidates(neighbour))
                {
                    if (!neighbourCandidate.isValid())
                    {
                        continue;
                    }
                    AxisAlignedBB other = neighbourCandidate.getAABB(this.width, this.height);
                    if (other.overlaps(aabb))
                    {
                        conflicts.add(neighbourCandidate);
                    }
                }
            }
        }
        return conflicts;
    }
}
