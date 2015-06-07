package some.pack.age.quadtree;

import some.pack.age.models.Node;
import some.pack.age.models.Point;

import java.util.HashSet;
import java.util.Set;

public class QuadTree {

    private Node root;
    private int insertedPoints = 0;
    
    public void insert(Point point) {     
        if(this.root == null){
            root = new Node(point.getX(), point.getY(), point, null);
            this.insertedPoints++;
        }
        else {
            getInsertLeaf(point, root);
        }
    }
    
    public Node getQuadrant(Point point, Node node){
        Node returnNode = null;
        if(node.getX() <= point.getX() && node.getY() > point.getY()){
            returnNode = node.getNw();
        }
        else if(node.getX() > point.getX() && node.getY() >= point.getY()){
            returnNode = node.getNe();
        }
        else if(node.getX() >= point.getX() && node.getY() < point.getY()){
            returnNode = node.getSe();
        }
        else if(node.getX() < point.getX() && node.getY() <= point.getY()){
            returnNode = node.getSw();
        }
        return returnNode;
    }
    
    public void setQuadrant(Point point, Node node){
        if(node.getX() <= point.getX() && node.getY() > point.getY()){
            node.setNw(new Node(point.getX(), point.getY(), point, node));
        }
        else if(node.getX() > point.getX() && node.getY() >= point.getY()){
            node.setNe(new Node(point.getX(), point.getY(), point, node));
        }
        else if(node.getX() >= point.getX() && node.getY() < point.getY()){
            node.setSe(new Node(point.getX(), point.getY(), point, node));
        }
        else if(node.getX() < point.getX() && node.getY() <= point.getY()){
            node.setSw(new Node(point.getX(), point.getY(), point, node));
        }
    }
    
    public void getInsertLeaf(Point point, Node node){
        Node quadrant = getQuadrant(point, node);
        if(quadrant != null){
            getInsertLeaf(point,quadrant);
        }
        else{
           setQuadrant(point, node);
           insertedPoints++;
        }
    }
    
    public Set<Point<?>> intersect(Point point, int width, int height) {
        int xMax = point.getX() + 2 * width;
        int xMin = point.getX() - 2 * width;
        int yMax = point.getY() + 2 * height;
        int yMin = point.getY() - 2 * height;
        Set<Point<?>> reporter = new HashSet<Point<?>>()
        {
            public boolean add(Point<?> point1)
            {
                // Do not allow the point we are searching for to be found
                if (point1.equals(point))
                {
                    return false;
                }
                return super.add(point1);
            }
        };
        return intersectQuadrant(point, root, xMax, xMin, yMax, yMin, reporter);
    }
    
    public Set<Point<?>> intersectQuadrant(Point point, Node node, int xMax, int xMin, int yMax, int yMin, Set<Point<?>> reporter){
        boolean neighbour = false;
        if(node.getX() <= xMax && node.getX() >= xMin && node.getY() <= yMax && node.getY() >= yMin){
            //Point Inside
            neighbour = true;
            /* if(node.hasNe()){
                intersectQuadrant(point, node.getNe(), xMax, xMin, yMax, yMin);
            }
            if(node.hasNw()){
                intersectQuadrant(point, node.getNw(), xMax, xMin, yMax, yMin);
            }
            if(node.hasSw()){
                intersectQuadrant(point, node.getSe(), xMax, xMin, yMax, yMin);
            }
            if(node.hasSe()){
                intersectQuadrant(point, node.getSw(), xMax, xMin, yMax, yMin);
            } */
        }
        if(point.getX() <= node.getX()  && node.getY() > point.getY()){
            if(node.hasNw()){
                intersectQuadrant(point, node.getNw(), xMax, xMin, yMax, yMin, reporter);
            }
        }
        else if(point.getX() > node.getX() && point.getY() >= node.getY()){
            if(node.hasNe()){
                intersectQuadrant(point, node.getNe(), xMax, xMin, yMax, yMin, reporter);
            }
        }
        else if(point.getX() >= node.getX() && point.getY() < node.getY()){
            if(node.hasSe()){
                intersectQuadrant(point, node.getSe(), xMax, xMin, yMax, yMin, reporter);
            }
        }
        else if(point.getX() < node.getX() && point.getY() <= node.getY()){
            if(node.hasSw()){
                intersectQuadrant(point, node.getSw(), xMax, xMin, yMax, yMin, reporter);
            }
        }
        return reporter;
    }
    
}
