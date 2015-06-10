package some.pack.age.models.quadtree;

import some.pack.age.models.AxisAlignedBB;
import some.pack.age.models.Point;
import some.pack.age.quadtree.QuadTree;

public class Node {

    // We could use Optional here instead of null values
    private int x;
    private int y;
    private Node opt_parent;
    private Point point;
    private NodeType nodetype;
    private Node nw;
    private Node ne;
    private Node sw;
    private Node se;
    
    private int minX;
    private int minY;
    private int maxX;
    private int maxY;

    public Node(int x, int y, Point point, Node opt_parent) {
        this.minX = this.maxX = this.x = x;
        this.minY = this.maxY = this.y = y;
        this.point = point;
        this.opt_parent = opt_parent;
    }

    public double getX() {
        return x;
    }

    // The setters should not be called, they could break the 
    // representation invariant of the QuadTree
    public void setX(int x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public Node getParent() {
        return opt_parent;
    }

    public void setParent(Node opt_parent) {
        this.opt_parent = opt_parent;
    }

    public void setPoint(Point point) {
        this.point = point;
    }

    public Point getPoint() {
        return this.point;
    }

    public void setNodeType(NodeType nodetype) {
        this.nodetype = nodetype;
    }

    public NodeType getNodeType() {
        return this.nodetype;
    }

    public void setNw(Node nw) {
        this.nw = nw;
        checkMinX();
        checkMaxY();
    }

    public void setNe(Node ne) {
        this.ne = ne;
        checkMaxX();
        checkMaxY();
    }

    public void setSw(Node sw) {
        this.sw = sw;
        checkMinX();
        checkMinY();
    }

    public void setSe(Node se) {
        this.se = se;
        checkMaxX();
        checkMinY();
    }

    public Node getNe() {
        return ne;
    }

    public Node getNw() {
        return nw;
    }

    public Node getSw() {
        return sw;
    }

    public Node getSe() {
        return se;
    }
    
    public boolean hasNe() {
        return this.ne != null;
    }

    public boolean hasNw() {
        return this.nw != null;
    }

    public boolean hasSw() {
        return this.sw != null;
    }

    public boolean hasSe() {
        return this.se != null;
    }
    
    public int getMinX()
    {
        return this.minX;
    }
    
    public int getMaxX()
    {
        return this.maxX;
    }
    
    public int getMinY()
    {
        return this.minY;
    }
    
    public int getMaxY()
    {
        return this.maxY;
    }

    public AxisAlignedBB getBoundingBox()
    {
        return new AxisAlignedBB(this.minX, this.minY, this.maxX, this.maxY);
    }

    private void checkMinX()
    {
        int old = this.minX;
        int minX = this.minX = this.x;
        if (this.hasSw())
        {
            minX = Math.min(this.getSw().getMinX(), minX);
        }
        if (this.hasNw())
        {
            minX = Math.min(this.getNw().getMinX(), minX);
        }
        if (this.hasNe())
        {
            minX = Math.min(this.getNe().getMinX(), minX);
        }
        if (this.hasSe())
        {
            minX = Math.min(this.getSe().getMinX(), minX);
        }
        this.minX = Math.min(this.minX, minX);
        if (this.opt_parent != null)
        {
            this.opt_parent.checkMinX();
        }
    }
    
    private void checkMinY()
    {
        int old = this.minY;
        int minY = this.minY = this.y;
        if (this.hasSw())
        {
            minY = Math.min(this.getSw().getMinY(), minY);
        }
        if (this.hasNw())
        {
            minY = Math.min(this.getNw().getMinY(), minY);
        }
        if (this.hasNe())
        {
            minY = Math.min(this.getNe().getMinY(), minY);
        }
        if (this.hasSe())
        {
            minY = Math.min(this.getSe().getMinY(), minY);
        }
        this.minY = Math.min(this.minY, minY);
        if (this.opt_parent != null)
        {
            this.opt_parent.checkMinY();
        }
    }
    
    private void checkMaxX()
    {
        int old = this.maxX;
        int maxX = this.maxX = this.x;
        if (this.hasSw())
        {
            maxX = Math.max(this.getSw().getMaxX(), maxX);
        }
        if (this.hasNw())
        {
            maxX = Math.max(this.getNw().getMaxX(), maxX);
        }
        if (this.hasNe())
        {
            maxX = Math.max(this.getNe().getMaxX(), maxX);
        }
        if (this.hasSe())
        {
            maxX = Math.max(this.getSe().getMaxX(), maxX);
        }
        this.maxX = Math.max(this.maxX, maxX);
        if (this.opt_parent != null)
        {
            this.opt_parent.checkMaxX();
        }
    }
    
    private void checkMaxY()
    {
        int old = this.maxY;
        int maxY = this.maxY = this.y;
        if (this.hasSw())
        {
            maxY = Math.max(this.getSw().getMaxY(), maxY);
        }
        if (this.hasNw())
        {
            maxY = Math.max(this.getNw().getMaxY(), maxY);
        }
        if (this.hasNe())
        {
            maxY = Math.max(this.getNe().getMaxY(), maxY);
        }
        if (this.hasSe())
        {
            maxY = Math.max(this.getSe().getMaxY(), maxY);
        }
        this.maxY = Math.max(this.maxY, maxY);
        if (this.opt_parent != null)
        {
            this.opt_parent.checkMaxY();
        }
    }
}
