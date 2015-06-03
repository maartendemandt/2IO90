/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package some.pack.age.algorithm;

import java.util.Set;
import some.pack.age.models.Point;
import some.pack.age.models.Solution;
import some.pack.age.models.Point;
import some.pack.age.models.Solution;
import some.pack.age.test.Scheduler;

import java.util.Optional;
import java.util.Set;

/**
 *
 * @author s132042
 */
public class AnnealingAlgorithm2 extends AnnealingAlgorithm {
    
    @Override
    double getAppropiateTemperature(Set<Point> problem){
        return problem.size();
    }
    
    @Override
     double getAppropiateDecreaseRate(double temperature){
        return temperature * 0.000002;

    }
     
    @Override 
    boolean annualSchedule(double temperature, double decreaseRate, double minTemperature){
        temperature = temperature - decreaseRate;
        return temperature > minTemperature;
    }
    

}
