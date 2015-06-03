package some.pack.age.algorithm;

import java.util.ArrayList;
import some.pack.age.models.Point;
import some.pack.age.models.Solution;
import some.pack.age.test.Scheduler;

import java.util.Optional;
import java.util.Set;
import some.pack.age.models.solutionChange;

/**
 * @author DarkSeraphim.
 */
public class AnnealingAlgorithm implements IAlgorithm
{
    @Override
    public Solution computePoints(Set<Point> points, int width, int height)
    {
        // NEIGHBOUR SOLUTION IMPROVEMENT
        double solutionQuality;
        // END NEIGHBOUR SOLUTION IMPROVEMENT
        
        Solution solution = getRandomSolution(width, height, points);
        double temperature = getAppropiateTemperature(points);
        double decreaseRate = getAppropiateDecreaseRate(temperature);
        System.out.println("Temperature = " + temperature);
        System.out.println("DecreaseRate = " + decreaseRate);
        double minTemperature = getAppropiateMinTempreature(temperature, decreaseRate);
        Solution bestSolution = solution;
        double maxQuality = solution.getQuality();
        solutionQuality = maxQuality; // NEIGHBOUR SOLUTION IMPROVEMENT
        System.out.println("Quality at start: " + maxQuality);
        Scheduler s = new Scheduler();
        s.setMaxPhases((int) Math.round((temperature - minTemperature) / decreaseRate));
        while (annualSchedule(temperature, decreaseRate, minTemperature)){
            solutionChange toChange = getNeighborSolutions(solution);
            solution = toChange.execute(solution);
            
            maxQuality = Math.max(solution.getQuality(), maxQuality);
            double probability = getProbability(solution.getQuality(), solutionQuality, temperature);

            if (Math.round(solution.getQuality()) != Math.round(maxQuality))
            {
                System.out.println("Quality: " + solution.getQuality());
            }

            if( checkIfAccepted(probability) ){
                solutionQuality = solution.getQuality(); // NEIGHBOUR SOLUTION IMPROVEMENT
            } else {
                solution = toChange.revert(solution);
            }

            if(solutionQuality > bestSolution.getQuality()){ // NEIGHBOUR SOLUTION IMPROVEMENT
                bestSolution = solution;
            }

            temperature = temperature - decreaseRate;
            s.bumpPhase();
        }
        //s.kill();
        System.out.println("max quality: "+maxQuality);
        return bestSolution;
    }

    //notes: the = sign should stand for clone not a reference

//TODO: uitzoeken wat een goed annealig schedule is. (temperature en decrease rate en waneer we stoppen)

    //generates a random solution
    Solution getRandomSolution(int width, int height, Set<Point> problem){
        Solution solution = new Solution(width, height);

        int i = 0;
        for(Point point : problem) {
            Optional<Point> p = point.getRandomFreeLabel(solution);
            if (p.isPresent())
            {
                solution.add(p.get());
            }
            else
            {
                solution.add(point.getDefault());
            }
        }

        return solution;
    }

    double getAppropiateTemperature(Set<Point> problem){
        //one way is just starting with the problem size
        return problem.size();

    }

    double getAppropiateDecreaseRate(double temperature){
        return temperature * 0.0001;//temperature * 0.1; //decrease with one procent

    }

    double getAppropiateMinTempreature(double temperature, double decreaseRate){
        return 1;
    }

    //returns if we are done with the algorithm according to the schedule
    boolean annualSchedule(double temperature, double decreaseRate, double minTemperature){
        temperature = temperature - decreaseRate;
        return temperature > minTemperature;
    }


    solutionChange getNeighborSolutions(Solution solution){
        //get a random point that has an option to change the label position
        Point p = solution.getRandomPoint();
        //change the current label with a random other label that has no conflicts
        Optional<Point> point = p.getMutation(solution);
        if (point.isPresent())
        {
            return new solutionChange(p, point.get());
        }
        else
        {
            return new solutionChange(p);
        }
    }

    boolean checkIfAccepted(double probability){
        double acceptedChange = Math.random();

        if(probability >= acceptedChange){
            return true;
        } else{
            return false;
        }
    }

    //could be the case that we have to switch the "<" sign because of the way we meassure the quality
    double getProbability(double neighborQuality, double currentQuality, double temperature){
        double chance = 0;
        if(neighborQuality > currentQuality){
            chance = 1;
        } else {
            //expression:  chance = e^((neighborQuality - currentQuality) / temperature)
            chance = Math.exp((neighborQuality - currentQuality) / temperature);
        }

        return chance;
    }

//nog meer general notes:
//quality of the solution should be updated every time a solution changed:
//how to determine quality (objective function)
// #of deleted labels
// #of obstructed labels
}
