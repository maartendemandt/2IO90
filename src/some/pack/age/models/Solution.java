package some.pack.age.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author DarkSeraphim.
 */
public class Solution implements Iterable<Point>
{

    private final int width;

    private final int height;

    // TODO: Neither Set nor List are great data structures here.
    private final Set<Point> points;

    private final Map<Point, List<Point>> collisions = new HashMap<>();

    public Solution(int width, int height)
    {
        this(width, height, new HashSet<>());
    }

    public Solution(Solution other)
    {
        this(other.width, other.height, other.points);
    }

    public Solution(int width, int height, Set<Point> points)
    {
        this.width = width;
        this.height = height;
        this.points = new HashSet<>(points);
    }

    public double getQuality()
    {
        return this.size();
    }

    @Override
    public Iterator<Point> iterator()
    {
        return this.points.iterator();
    }

    public void add(Point point)
    {
        this.points.add(point);
    }

    public void change(Point p, Point mutation)
    {
        this.points.remove(p);
        this.points.add(mutation);
    }

    public void remove(Point p)
    {
        this.points.remove(p);
        this.points.add(p.getDefault());
    }

    private List<Long> times = new ArrayList<>();

    public void printAverage()
    {
        System.out.println("Average: " + times.stream().mapToDouble(l -> l).average().getAsDouble());
    }

    public boolean isPossible(Point point)
    {
        long start = System.nanoTime();
        AxisAlignedBB box = point.getAABB(this.width, this.height);
        for (Point other : this.points)
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

    public Point getRandomPoint()
    {
        Point[] points = this.points.toArray(new Point[this.points.size()]);
        return points[ThreadLocalRandom.current().nextInt(points.length)];
    }

    public int size()
    {
        return (int) this.points.stream().filter(Point::isValid).count();
    }

    public Set<Point> getPoints()
    {
        return this.points;
    }

    public Set<Point> getNeighbours(Point point)
    {
        return null;
    }

    public List<Point> getConflicts(Point candidate)
    {
        return null;
    }
}
