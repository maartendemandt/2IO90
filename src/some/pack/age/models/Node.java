package some.pack.age.models;

import some.pack.age.models.NodeType;
import some.pack.age.models.Point;

public class Node {

    private double x;
    private double y;
    private Node opt_parent;
    private Point point;
    private NodeType nodetype;
    private Node nw;
    private Node ne;
    private Node sw;
    private Node se;

    public Node(double x, double y, Point point, Node opt_parent) {
        this.x = x;
        this.y = y;
        this.point = point;
        this.opt_parent = opt_parent;
    }

    public double getX() {
        return x;
    }

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
    }

    public void setNe(Node ne) {
        this.ne = ne;
    }

    public void setSw(Node sw) {
        this.sw = sw;
    }

    public void setSe(Node se) {
        this.se = se;
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
    
    public Boolean hasNe() {
        if(ne==null){
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean hasNw() {
        if(nw==null){
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean hasSw() {
        if(sw==null){
            return false;
        }
        else {
            return true;
        }
    }

    public Boolean hasSe() {
        if(se==null){
            return false;
        }
        else {
            return true;
        }
    }
}