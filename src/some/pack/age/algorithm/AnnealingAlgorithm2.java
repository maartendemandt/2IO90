/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package some.pack.age.algorithm;

import java.util.Set;
import some.pack.age.models.Point;


public class AnnealingAlgorithm2 {
    long startTime = System.currentTimeMillis() % 1000;
    double numberOfMinutes = 4.5;

    double getAppropiateTemperature(Set<Point> problem){
        //one way is just starting with the problem size
        return 4;
    }

    double getAppropiateDecreaseRate(double temperature) {
        
        
        
        return temperature * 0.0001;//temperature * 0.1; //decrease with one procent

    }

    double getAppropiateMinTempreature(double temperature, double decreaseRate){
        return 1;
    }

    //returns if we are done with the algorithm according to the schedule
    boolean annualSchedule(double temperature, double decreaseRate, double minTemperature){
        long currentTime = System.currentTimeMillis() % 1000;
        double timePassed = currentTime - startTime;
        
        
        return temperature > minTemperature;
    }
}
