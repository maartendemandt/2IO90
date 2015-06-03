package some.pack.age.quadtree;

import some.pack.age.models.NodeType;
import some.pack.age.models.Point;
import some.pack.age.models.Node;

import java.util.ArrayList;
import java.util.List;

public class QuadTree {

    private Node root;
    private int insertedPoints = 0;
    
    public void insert(Point point) {     
        if(insertedPoints == 0){
            root = new Node(point.getX(), point.getY(), point, null);
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
        }
        return neighbour;
    }
    
}