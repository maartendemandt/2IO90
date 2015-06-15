package some.pack.age.quadtree;

<<<<<<< HEAD
import some.pack.age.models.AxisAlignedBB;
import some.pack.age.models.quadtree.Node;
=======
import some.pack.age.models.NodeType;
>>>>>>> fcf8983d963b0bf795fec2a116970fb86c46cb4d
import some.pack.age.models.Point;
import some.pack.age.models.Node;

import java.util.ArrayList;
import java.util.List;

public class QuadTree {

    private Node root;
    private int insertedPoints = 0;
    
    public Node insert(Point point) {     
        if(this.root == null){
            this.insertedPoints++;
            return root = new Node(point.getX(), point.getY(), point, null);
        }
        else {
            return getInsertLeaf(point, root);
        }
    }
    
    public Node getQuadrant(Point point, Node node){
        Node returnNode = null;
        if(point.getX() <= node.getX() && point.getY() > node.getY()){
            returnNode = node.getNw();
        }
        else if(point.getX() > node.getX() && point.getY() >= node.getY()){
            returnNode = node.getNe();
        }
        else if(point.getX() >= node.getX() && point.getY() < node.getY()){
            returnNode = node.getSe();
        }
        else if(point.getX() < node.getX() && point.getY() <= node.getY()){
            returnNode = node.getSw();
        }
        return returnNode;
    }
    
    public Node setQuadrant(Point point, Node node){
        Node child = new Node(point.getX(), point.getY(), point, node);
        if(point.getX() <= node.getX() && point.getY() > node.getY()){
            node.setNw(child);
        }
        else if(point.getX() > node.getX() && point.getY() >= node.getY()){
            node.setNe(child);
        }
        else if(point.getX() >= node.getX() && point.getY() < node.getY()){
            node.setSe(child);
        }
        else if(point.getX() < node.getX() && point.getY() <= node.getY()){
            node.setSw(child);
        }
        else {
            child = null;
        }
        return child;
    }
    
    public Node getInsertLeaf(Point point, Node node){
        Node quadrant = getQuadrant(point, node);
        if(quadrant != null){
            return getInsertLeaf(point, quadrant);
        }
        else{
            insertedPoints++;
            return setQuadrant(point, node);
        }
    }
    
<<<<<<< HEAD
    public Set<Point> intersect(final Point point, int width, int height) {
        int xMax = point.getX() + 2 * width;
        int xMin = point.getX() - 2 * width;
        int yMax = point.getY() + 2 * height;
        int yMin = point.getY() - 2 * height;
        Set<Point> reporter = new HashSet<Point>()
        {
            @Override
            public boolean add(Point point1)
            {
                // Do not allow the point we are searching for to be found
                if (point1.equals(point))
                {
                    return false;
                }
                return super.add(point1);
            }
        };
        if (this.root != null)
        {
            intersectQuadrant(point, root, new AxisAlignedBB(xMin, yMin, xMax, yMax), reporter);
        }
        return reporter;
    }
    
    public Set<Point> intersectQuadrant(Point point, Node node, AxisAlignedBB aabb, Set<Point> reporter){
        if(aabb.contains(node.getPoint())) {
            //Point inside the region, report it
            reporter.add(node.getPoint());
        }

        if(node.hasNw() && node.getNw().getBoundingBox().overlaps(aabb)){
            intersectQuadrant(point, node.getNw(), aabb, reporter);
        }
        if(node.hasNe() && node.getNe().getBoundingBox().overlaps(aabb)){
            intersectQuadrant(point, node.getNe(), aabb, reporter);
        }
        if(node.hasSe() && node.getSe().getBoundingBox().overlaps(aabb)){
            intersectQuadrant(point, node.getSe(), aabb, reporter);
        }
        if(node.hasSw() && node.getSw().getBoundingBox().overlaps(aabb)){
            intersectQuadrant(point, node.getSw(), aabb, reporter);
=======
    public void intersect(Point point, int width, int height){
        int xMax = point.getX() + 2*width;
        int xMin = point.getX() - 2*width;
        int yMax = point.getY() + 2*height;
        int yMin = point.getY() - 2*height;
        
        intersectQuadrant(point, root, xMax, xMin, yMax, yMin);
    }
    
    public Boolean intersectQuadrant(Point point, Node node, int xMax, int xMin, int yMax, int yMin){
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
        else {
            if(point.getX() <= node.getX()  && node.getY() > point.getY()){
                if(node.hasNw()){
                    intersectQuadrant(point, node.getNw(), xMax, xMin, yMax, yMin);
                }
            }
            else if(point.getX() > node.getX() && point.getY() >= node.getY()){
                if(node.hasNe()){
                    intersectQuadrant(point, node.getNe(), xMax, xMin, yMax, yMin);
                }
            }
            else if(point.getX() >= node.getX() && point.getY() < node.getY()){
                if(node.hasSe()){
                    intersectQuadrant(point, node.getSe(), xMax, xMin, yMax, yMin);
                }
            }
            else if(point.getX() < node.getX() && point.getY() <= node.getY()){
                if(node.hasSw()){
                    intersectQuadrant(point, node.getSw(), xMax, xMin, yMax, yMin);
                }
            }
>>>>>>> fcf8983d963b0bf795fec2a116970fb86c46cb4d
        }
        return neighbour;
    }
}
