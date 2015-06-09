package some.pack.age.algorithm;

import some.pack.age.models.AxisAlignedBB;
import some.pack.age.models.solution.EIL3Solution;
import some.pack.age.models.Point;
import some.pack.age.models.solution.Solution;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import some.pack.age.models.labels.AbstractLabel;

/**
 * @author DarkSeraphim.
 */
@SuppressWarnings("unchecked")
public class ThreePlusOneAlgorithm implements IAlgorithm
{
    // Array which contains all the changes points which haven't been processed again
    private final List<AbstractLabel> changes = new ArrayList<>();

    @Override
    public Solution computePoints(Set<AbstractLabel> points, int width, int height)
    {

        EIL3Solution solution = new EIL3Solution(width, height, getInput(points));
        // Should initialize the candidates for the point
        solution.forEach(solution::getCandidates);

        // ******* //
        // Phase 1 //
        // ******* //

        // Apply rules to each point in input
        for (AbstractLabel label : solution)
        {
            AbstractLabel old = label;
            label = applyRule1(solution, label); // Apply Rule 1 (L1)
            label = applyRule2(solution, label); // Apply Rule 2 (L2)
            label = applyRule3(solution, label, width, height); // Apply Rule 3 (L3)

            if (old.isClone(label)) {
                // update point to newPoint
                solution.change(old, label);
            }

            // Apply rules to points in arrayOfChanges
            processArrayOfChanges(solution, width, height);
        }

        // ******* //
        // Phase 2 //
        // ******* //

        // If Phase 2 is needed, execute phase 2
        while (checkPhase2(solution))
        {
            int maxCandidates = 0;
            List<AbstractLabel> labelsMostCandidates = new ArrayList<>();

            // Find points with heighest amount of candidates
            for (AbstractLabel point : solution) {
                List<AbstractLabel> candidates = solution.getCandidates(point);
                int numOfCandidates = candidates.size();

                // if number of candidates is larger than the current maximum of candidates found --> empty array, add point to array and set new maximum
                if (maxCandidates >= numOfCandidates)
                {
                    if (maxCandidates > numOfCandidates)
                    {
                        labelsMostCandidates.clear();
                    }
                    labelsMostCandidates.add(point);
                    maxCandidates = numOfCandidates;
                }
            }

            // For each point with most candidates delete candidate with maximum number of conflicts
            for (AbstractLabel point : labelsMostCandidates) {
                // Get candidates of point
                List<AbstractLabel> candidates = solution.getCandidates(point);
                AbstractLabel candidateMostConflicts = null;
                int maxNumberOfConflicts = 0;

                // Get candidate with most conflicts
                for (AbstractLabel candidate : candidates) {
                    int numOfConflicts = solution.getConflicts(candidate).size();
                    if (candidateMostConflicts == null) { // First label processed so just store it
                        candidateMostConflicts = candidate;
                        maxNumberOfConflicts = numOfConflicts;
                    } else if (numOfConflicts > maxNumberOfConflicts) { // More conflicts so store it
                        candidateMostConflicts = candidate;
                        maxNumberOfConflicts = numOfConflicts;
                    }
                }

                // Delete candidate with most conflicts if number of conflicts > 0
                if (maxNumberOfConflicts > 0) {
                    solution.removeCandidate(candidateMostConflicts);


                    // Put all points in the neighborhood of point in arrayOfChanges
                    this.changes.addAll(solution.getNeighbours(point));

                    // Apply rules to points in arrayOfChanges
                    processArrayOfChanges(solution, width, height);
                }
            }
        }

        // ******* //
        //  DONE!  //
        // ******* //
        return solution;
    }

    List<AbstractLabel> getInput(Set<AbstractLabel> input)
    {
        List<AbstractLabel> points = new ArrayList<>();
        for (AbstractLabel point : input) {
            // Add per point 2 or 4 candidates (depending on the model)
            points.add(point.getDefault());
        }
        return points;
    }

    AbstractLabel applyRule1(EIL3Solution solution, AbstractLabel point) {
        // Iterate over all points
        for (AbstractLabel candidate : solution.getCandidates(point)) {
            // Remove all candidates of point except the candidate with no conflicts
            if (solution.isPossible(candidate))
            {
                continue;
            }
            solution.getCandidates(point).stream()
                                         .filter(other -> !candidate.equals(other))
                                         .forEach(solution::removeCandidate);
            this.changes.addAll(solution.getNeighbours(candidate));
            break;
        }
        return point;
    }

    AbstractLabel applyRule2(EIL3Solution solution, AbstractLabel point) {
        for (AbstractLabel candidate : solution.getCandidates(point)) {
            // This will store the label which makes sure the candidate can be part of the end solution
            AbstractLabel conflictSolver = null;

            // Get the conflicts of the candidate
            List<AbstractLabel> conflicts = solution.getConflicts(candidate);
            AbstractLabel pointConflictedLabel = null;

            // Check if there is only conflict, if so, check if it can be eliminated with rule 2
            if (conflicts.size() == 1) {
                // Get point of conflicted label
                // What is the parent?
                pointConflictedLabel = candidate;

                // Loop through all candidates of conflicted point
                for (AbstractLabel candidateConflictPoint : solution.getCandidates(pointConflictedLabel)) {
                    // Check if its not the label which was in conflict with the candidate
                    if (!candidateConflictPoint.isClone(conflicts.get(0)) && candidateConflictPoint.equals(conflicts.get(0))) {
                        List<AbstractLabel> conflictsCandidateConflictPoint = solution.getConflicts(candidateConflictPoint);

                        // Variable to check if all conflicts can be avoided with rule 2
                        boolean useableCandidate = true;

                        // Loop through all conflicts of candidate of conflict point
                        for (Point conflictCandidateConflictPoint : conflictsCandidateConflictPoint) {
                            // If the point of the label which is in conflict with the candidate of the conflict point --> Rule 2 cannot be applied
                            if (conflictCandidateConflictPoint.equals(point)) {
                                useableCandidate = false;
                            }
                        }

                        // Check if there is no conflict found which prevents rule 2 from applying
                        if (useableCandidate) {
                            conflictSolver = candidateConflictPoint;
                            break; // break out of foreach loop which loops through all candidates of conflicted point
                        }
                    }
                }
            }

            // If there is a conflictSolver, use it!
            if (conflictSolver != null) {
                // Remove all candidates of point except the candidate with no conflicts
                solution.getCandidates(point).stream()
                                         .filter(candidate::isClone)
                                         .forEach(solution::removeCandidate);

                this.changes.addAll(solution.getNeighbours(point));

                // Remove all candidates of the conflict point except the conflictSolver
                for (AbstractLabel label : solution.getCandidates(pointConflictedLabel)) {
                    if (label != conflictSolver) {
                        solution.removeCandidate(label);
                    }
                }

                this.changes.addAll(solution.getNeighbours(pointConflictedLabel));
            }
        }

        return point;
    }

    AbstractLabel applyRule3(EIL3Solution solution, AbstractLabel point, int width, int height) {
        // Get candidates of point
        List<AbstractLabel> candidates = solution.getCandidates(point);

        // Check if there is only conflict, if so, check if it can be eliminated with rule 2
        if (candidates.size() == 1) {
            // Get conflicts of candidate
            List<AbstractLabel> conflicts = solution.getConflicts(candidates.get(0));

            boolean isOverlap = false;
            AxisAlignedBB aabb = candidates.get(0).getAABB(width, height);
            for (AbstractLabel conflict : conflicts)
            {
                if (!conflict.isValid())
                {
                    continue;
                }
                isOverlap = isOverlap || conflict.getAABB(width, height).overlaps(aabb);
            }

            // Check if all conflicts and candidate itself overlap (clique)
            if (isOverlap)
            {
                // Remove all conflicts that overlap the candidate
                Point pointOfConflict = candidates.get(0);
                conflicts.forEach(solution::removeCandidate);
                this.changes.addAll(solution.getNeighbours(pointOfConflict));
            }
        }

        return point;
    }

    void processArrayOfChanges(EIL3Solution solution, int width, int height)
    {
        for(AbstractLabel point : changes)
        {
            AbstractLabel old = point;
            point = applyRule1(solution, point); // Apply Rule 1 (L1)
            point = applyRule2(solution, point); // Apply Rule 2 (L2)
            point = applyRule3(solution, point, width, height); // Apply Rule 3 (L3)

            if (!old.isClone(point)) {
                // update point to newPoint
                solution.change(old, point);
            }
        }
        changes.clear();
    }

    boolean checkPhase2(EIL3Solution solution)
    {
        for (AbstractLabel point : solution)
        {
            List<AbstractLabel> candidates = solution.getCandidates(point);

            if (candidates.size() > 1)
            {
                return true;
            }
        }
        return false;
    }
}
