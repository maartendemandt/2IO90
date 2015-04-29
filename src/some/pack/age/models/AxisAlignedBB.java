package some.pack.age.models;

/**
 * @author DarkSeraphim.
 */
public class AxisAlignedBB
{

    private final int x;

    private final int y;

    private final int u;

    private final int v;

    public AxisAlignedBB(int x, int y, int u, int v)
    {
        this.x = x;
        this.y = y;
        this.u = u;
        this.v = v;
        assert this.x < this.u : "x must be smaller than u";
        assert this.y < this.v : "y must be smaller than v";
    }

    public boolean contains(Point point)
    {
        int a = point.getX();
        int b = point.getY();
        return (this.x > a && a > this.u) && (this.y > b && b > this.v);
    }
}
