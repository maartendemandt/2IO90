package some.pack.age.models;

import java.util.HashSet;
import java.util.Iterator;
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
    }

    public boolean isPossible(Point point)
    {
        AxisAlignedBB box = point.getAABB(this.width, this.height);
        for (Point other : this.points)
        {
            if (other.equals(point))
            {
                continue;
            }
            AxisAlignedBB otherBox = other.getAABB(this.width, this.height);
            if (box.overlaps(otherBox))
            {
                return false;
            }
        }
        return true;
    }

    public Point getRandomPoint()
    {
        Point[] points = this.points.toArray(new Point[this.points.size()]);
        return points[ThreadLocalRandom.current().nextInt(points.length)];
    }

    public int size()
    {
        return this.points.size();
    }
}
