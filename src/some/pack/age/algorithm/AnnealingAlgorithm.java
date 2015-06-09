package some.pack.age.algorithm;

import some.pack.age.models.solution.Solution;
import some.pack.age.models.solution.AnnealingSolution;

import java.util.Optional;
import java.util.Set;
import some.pack.age.models.labels.AbstractLabel;

/**
 * @author G to the Foks and J to the Adegeest
 */
public class AnnealingAlgorithm implements IAlgorithm
{
    @Override
    public Solution computePoints(Set<AbstractLabel> points, int width, int height)
    {
        // NEIGHBOUR SOLUTION IMPROVEMENT
        double solutionQuality;
        // END NEIGHBOUR SOLUTION IMPROVEMENT
        
        //#ADDED
        long startTime = System.currentTimeMillis(); //JG
        double numberOfMinutes = 4.9; //JG
        //end
        
        AnnealingSolution solution = getRandomSolution(width, height, points);
        solution.recomputeNeighbours();
        double temperature = getAppropiateTemperature(points);
        double initialTemperature = temperature;
        //double decreaseRate = getAppropiateDecreaseRate(temperature);
        System.out.println("Temperature = " + temperature);
        //System.out.println("DecreaseRate = " + decreaseRate);
        double minTemperature = getAppropiateMinTempreature();
        Solution bestSolution = solution;
        double maxQuality = solution.getQuality();
        solutionQuality = maxQuality; // NEIGHBOUR SOLUTION IMPROVEMENT
        System.out.println("Quality at start: " + maxQuality);
        //Scheduler s = new Scheduler();
        //s.setMaxPhases((int) Math.round((temperature - minTemperature) / decreaseRate));
        while (annualSchedule(temperature, minTemperature) && solutionQuality != points.size() ){
            proposeNeighborSolution(solution);

            maxQuality = Math.max(solution.getQuality(), maxQuality);
            double probability = getProbability(solution.getQuality(), solutionQuality, temperature);

            if (Math.round(solution.getQuality()) != Math.round(maxQuality))
            {
                System.out.println("Quality: " + solution.getQuality());
            }

            if( checkIfAccepted(probability) ){
                solutionQuality = solution.getQuality(); // NEIGHBOUR SOLUTION IMPROVEMENT
            } else {
                solution.reset();
            }

            if(solutionQuality > bestSolution.getQuality()) { // NEIGHBOUR SOLUTION IMPROVEMENT
                // We need a copy >.<
                bestSolution = new AnnealingSolution(solution);
            }

            //#ADDED
            temperature = calculateTemperature(initialTemperature, startTime, numberOfMinutes);
            
            //s.bumpPhase();
        }
        //s.kill();
        System.out.println("Max quality: "+maxQuality);
        return bestSolution;
    }

    //notes: the = sign should stand for clone not a reference

//TODO: uitzoeken wat een goed annealig schedule is. (temperature en decrease rate en waneer we stoppen)

    //generates a random solution
    AnnealingSolution getRandomSolution(int width, int height, Set<AbstractLabel> problem){
        AnnealingSolution solution = new AnnealingSolution(width, height);

        int i = 0;
        for(AbstractLabel label : problem) {
            Optional<AbstractLabel> optionalLabel = label.getRandomFreeLabel(solution);
            if (optionalLabel.isPresent())
            {
                solution.add(optionalLabel.get());
            }
            else
            {
                solution.add(label.getDefault());
            }
        }

        return solution;
    }

    double getAppropiateTemperature(Set<AbstractLabel> problem){
        //Gekke calculations ask Gerson
        return 3.5;

    }

    /*
    double getAppropiateDecreaseRate(double temperature){
        return temperature * 0.00002;//temperature * 0.1; //decrease with one procent

    }*/

    double getAppropiateMinTempreature(){
        return 0;
    }

    //returns if we are done with the algorithm according to the schedule
    boolean annualSchedule(double temperature, double minTemperature){
        return temperature > minTemperature;
    }


    void proposeNeighborSolution(Solution solution){
        //get a random point that has an option to change the label position
        AbstractLabel label = solution.getRandomLabel(); //TODO alleen interessnten punten teruggeven
        //change the current label with a random other label that has no conflicts
        Optional<AbstractLabel> point = label.getMutation(solution);
        if (point.isPresent())
        {
            solution.change(label, point.get());
        }
        else
        {
            solution.remove(label);
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
            chance = Math.exp( -1 / temperature);
        }

        return chance;
    }
    
    double calculateTemperature(double initialTemperature, long startTime, double numberOfMinutes) {
        long currentTime = System.currentTimeMillis();
        long timePassed = currentTime - startTime;
        

        return initialTemperature - (initialTemperature * (timePassed/(60000*numberOfMinutes)));
    }

//nog meer general notes:
//quality of the solution should be updated every time a solution changed:
//how to determine quality (objective function)
// #of deleted labels
// #of obstructed labels
}
