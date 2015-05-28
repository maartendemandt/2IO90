/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package some.pack.age.models;

/**
 *
 * @author jasperadegeest
 */
public class solutionChange {
    public String command;
    public Point p;
    public Point point;
    
    public solutionChange(Point pInput) {
        command = "remove";
        p = pInput;
    }
    
    public solutionChange(Point pInput, Point pointInput) {
        command = "change";
        p = pInput;
        point = pointInput;
    }
    
    public Solution execute(Solution solution) {
        if ("change".equals(command)) {
            solution.change(p, point);
        } else {
            solution.remove(p);
        }
        
        return solution;
    }
    
    public Solution revert(Solution solution) {
        if ("change".equals(command)) {
            solution.change(point, p);
        } else {
            solution.add(p);
        }
        
        return solution;
    }
}
