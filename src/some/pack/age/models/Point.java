package some.pack.age.models;

/**
 * @author DarkSeraphim.
 */
public class Point
{
    private final int x;

    private final int y;

    public Point(int x, int y)
    {
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public String toString()
    {
        return String.format("%d %d NIL", this.x, this.y);
    }

    @Override
    public boolean equals(Object object)
    {
        if (!(object instanceof Point))
        {
            return false;
        }
        Point point = (Point) object;
        return point.x == this.x && point.y == this.x;
    }

    @Override
    public int hashCode()
    {
        int result = 19;
        result = 37 * result + x;
        result = 37 * result + y;
        return result;
    }
}
