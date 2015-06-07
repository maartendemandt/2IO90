package some.pack.age.models;

import some.pack.age.models.NodeType;
import some.pack.age.models.Point;

public class Node {

    // We could use Optional here instead of null values
    private double x;
    private double y;
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

    public Node(double x, double y, Point point, Node opt_parent) {
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
    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
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
    
    private void checkMinX()
    {
        int minX = this.minX = this.x;
        if (this.hasNw())
        {
            minX = Math.min(this.getNw().getMinX(), minX);
        }
        else if (this.hasSw())
        {
            minX = Math.min(this.getSw().getMinX(), minX);
        }
        this.minX = Math.min(this.minX, minX);
    }
    
    private void checkMinY()
    {
        int minY = this.minY = this.y;
        if (this.hasSw())
        {
            minY = Math.min(this.getSw().getMinY(), minY);
        }
        else if (this.hasSe())
        {
            minY = Math.min(this.getSe().getMinY(), minY);
        }
        this.minY = Math.min(this.minY, minY);
    }
    
    private void checkMaxX()
    {
        int maxX = this.maxX = this.x;
        if (this.hasNe())
        {
            maxX = Math.max(this.getNe().getMaxX(), maxX);
        }
        else if (this.hasSe())
        {
            maxX = Math.max(this.getSe().getMaxX(), maxX);
        }
        this.maxX = Math.max(this.maxX, maxX);
    }
    
    private void checkMaxY()
    {
        int maxY = this.maxY = this.y;
        if (this.hasNw())
        {
            maxY = Math.max(this.getNw().getMaxY(), maxY);
        }
        else if (this.hasSe())
        {
            maxY = Math.max(this.getNe().getMaxY(), maxY);
        }
        this.maxY = Math.max(this.maxY, maxY);
    }
}
