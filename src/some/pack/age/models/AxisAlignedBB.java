package some.pack.age.models;

import some.pack.age.quadtree.QuadTree;

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
        this.x = Math.min(x, u);
        this.y = Math.min(y, v);
        this.u = Math.max(x, u);
        this.v = Math.max(y, v);
        assert this.x <= this.u : "x must be smaller than u";
        assert this.y <= this.v : "y must be smaller than v";
    }

    public boolean contains(Point point)
    {
        return this.x <= point.getX() && point.getX() <= this.u
            && this.y <= point.getY() && point.getY() <= this.v;
    }

    public boolean overlaps(AxisAlignedBB aabb)
    {
        return !(aabb.x > this.u || aabb.u < this.x || aabb.y > this.v || aabb.v < this.y);
    }

    public static AxisAlignedBB createLabel(int x, int y, int w, int h)
    {
        return new AxisAlignedBB(x, y, x + w, y + h);
    }

    public int getX()
    {
        return this.x;
    }

    public int getY()
    {
        return this.y;
    }

    public int getU()
    {
        return this.u;
    }

    public int getV()
    {
        return this.v;
    }

    @Override
    public String toString()
    {
        return String.format("{[%d, %d], [%d, %d]}", this.x, this.y, this.u, this.v);
    }
}
