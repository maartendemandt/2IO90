package some.pack.age.models.solution;

import some.pack.age.models.AxisAlignedBB;
import some.pack.age.models.Point;
import some.pack.age.models.labels.AbstractLabel;
import some.pack.age.quadtree.QuadTree;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import some.pack.age.util.ConvertedSet;

/**
 * @author DarkSeraphim.
 */
public class Solution implements Iterable<AbstractLabel>
{

    protected final int width;

    protected final int height;

    protected final Set<AbstractLabel> labels;

    protected final Map<Point, List<Point>> collisions = new HashMap<>();

    private QuadTree quadTree;

    protected Map<Point, Set<AbstractLabel>> neighbours;

    public Solution(int width, int height)
    {
        this(width, height, new LinkedHashSet<AbstractLabel>());
    }

    public Solution(Solution other)
    {
        this(other.width, other.height, other.labels);
    }

    public Solution(int width, int height, Set<AbstractLabel> points)
    {
        this.width = width;
        this.height = height;
        this.labels = new LinkedHashSet<>(points);
        this.quadTree = new QuadTree();
        for (Point point : points)
        {
           this.quadTree.insert(point);
        }
        this.neighbours = new HashMap<>();
        for (Point point : points)
        {
            this.neighbours.put(point, new ConvertedSet<>(quadTree.intersect(point, width, height), AbstractLabel.class));
        }
    }

    public double getQuality()
    {
        return this.size();
    }

    @Override
    public Iterator<AbstractLabel> iterator()
    {
        return this.labels.iterator();
    }

    public void add(AbstractLabel point)
    {
        if (this.labels.add(point))
        {
            this.quadTree.insert(point);
        }
    }

    public void change(AbstractLabel p, AbstractLabel mutation)
    {
        this.labels.remove(p);
        this.labels.add(mutation);
    }

    public void remove(AbstractLabel p)
    {
        this.labels.remove(p);
        this.labels.add(p.getDefault());
    }

    private List<Long> times = new ArrayList<>();

    public void printAverage()
    {
        System.out.println(times.stream().mapToDouble(l -> l).average().getAsDouble());
    }

    public boolean isPossible(AbstractLabel point)
    {
        long start = System.nanoTime();
        AxisAlignedBB box = point.getAABB(this.width, this.height);
        for (AbstractLabel other : this.getNeighbours(point))
        {
            if (other.equals(point) || !other.isValid())
            {
                continue;
            }
            AxisAlignedBB otherBox = other.getAABB(this.width, this.height);
            if (box.overlaps(otherBox))
            {
                times.add(System.nanoTime() - start);
                return false;
            }
        }
        times.add(System.nanoTime() - start);
        return true;
    }

    public AbstractLabel getRandomLabel()
    {
        AbstractLabel[] points = this.labels.toArray(new AbstractLabel[this.labels.size()]);
        return points[ThreadLocalRandom.current().nextInt(points.length)];
    }

    public int size()
    {
        return (int) this.labels.stream().filter(AbstractLabel::isValid).count();
    }

    public Set<AbstractLabel> getPoints()
    {
        return this.labels;
    }

    public Set<AbstractLabel> getNeighbours(Point point)
    {
        Set<AbstractLabel> points = this.neighbours.get(point);
        if (points != null)
        {
            return points;
        }
        return new ConvertedSet<>(this.quadTree.intersect(point, this.width, this.height), AbstractLabel.class);
    }

    public List<AbstractLabel> getConflicts(Point candidate)
    {
        return null;
    }

    public void recomputeNeighbours()
    {
        this.quadTree = new QuadTree();
        this.labels.forEach(this.quadTree::insert);
        this.labels.forEach(this::setNeighbours);
    }

    private void setNeighbours(Point point)
    {
        Set<AbstractLabel> set = new ConvertedSet<>(this.quadTree.intersect(point, this.width, this.height), AbstractLabel.class);
        this.neighbours.put(point, set);
    }
}
