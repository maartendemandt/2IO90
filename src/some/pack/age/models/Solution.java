package some.pack.age.models;

import some.pack.age.quadtree.QuadTree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;
import some.pack.age.util.ConvertedSet;

/**
 * @author DarkSeraphim.
 */
public class Solution implements Iterable<AbstractLabel<?>>
{

    protected final int width;

    protected final int height;

    protected final Set<AbstractLabel<?>> labels;

    protected final Map<Point, List<Point>> collisions = new HashMap<>();
    
    private Map<Point, Set<AbstractLabel<?>>> neighbours;

    public Solution(int width, int height)
    {
        this(width, height, new LinkedHashSet<AbstractLabel<?>>());
    }

    public Solution(Solution other)
    {
        this(other.width, other.height, other.labels);
    }

    public Solution(int width, int height, Set<AbstractLabel<?>> points)
    {
        this.width = width;
        this.height = height;
        this.labels = new HashSet<>(points);
        QuadTree quadTree = new QuadTree();
        for (Point point : points) {
            quadTree.insert(point);
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
    public Iterator<AbstractLabel<?>> iterator()
    {
        return this.labels.iterator();
    }

    public void add(AbstractLabel point)
    {
        this.labels.add(point);
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
        return (int) this.labels.stream().filter(Point::isValid).count();
    }

    public Set<AbstractLabel<?>> getPoints()
    {
        return this.labels;
    }

    public Set<AbstractLabel<?>> getNeighbours(Point point)
    {
        return this.neighbours.get(point);
    }

    public List<AbstractLabel<?>> getConflicts(Point candidate)
    {
        return null;
    }
}
