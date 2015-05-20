




import java.util.Set;

/**
 * @author DarkSeraphim.
 */
public class ThreePlusOneAlgorithm implements IAlgorithm
{

    @Override
    public Solution computePoints(Set<Point> points, int width, int height)
    {
        return null;
    }

   /* // Array which contains all the changes points which haven't been processed again
    private final List<Point> changes = new ArrayList<>();

    Solution quickerAlgorithm(Set<Point> points, int width, int height) {
        Solution solution = new Solution(width, height, getInput(points));

        // ******* //
        // Phase 1 //
        // ******* //

        // Apply rules to each point in input
        for (Point point : solution)
        {
            Point newPoint;
            newPoint = applyRule1(point); // Apply Rule 1 (L1)
            newPoint = applyRule2(point); // Apply Rule 2 (L2)
            newPoint = applyRule3(point); // Apply Rule 2 (L2)

            if (newPoint != point) {
                // update point to newPoint
                solution.change(point, newPoint);
            }

            // Apply rules to points in arrayOfChanges
            processArrayOfChanges(solution);
        }

        // ******* //
        // Phase 2 //
        // ******* //

        // Check if Phase 2 is needed
        boolean needed = checkPhase2();

        // If Phase 2 is needed, execute phase 2
        while (needed)
        {
            int maxCandidates = 0;
            List<Point> pointsMostCandidates = new ArrayList<>();

            // Find points with heighest amount of candidates
            for (Point point : solution) {
                List<Point> candidates = point.getCandidates();
                int numOfCandidates = candidates.size();

                // if number of candidates is larger than the current maximum of candidates found --> empty array, add point to array and set new maximum
                if (maxCandidates >= numOfCandidates)
                {
                    if (maxCandidates > numOfCandidates)
                    {
                        pointsMostCandidates.clear();
                    }
                    pointsMostCandidates.add(point);
                    maxCandidates = numOfCandidates;
                }
            }

            // For each point with most candidates delete candidate with maximum number of conflicts
            for (Point point : pointsMostCandidates) {
                // Get candidates of point
                candidates = point.getCandidates();
                candidateMostConflicts = null;
                maxNumberOfConflicts = 0;

                // Get candidate with most conflicts
                foreach (Label candidate : candidates) {
                    numOfConflicts = getConflicts(candidate);
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
                    point.removeCandidate(candidateMostConflicts);


                    // Put all points in the neighborhood of point in arrayOfChanges
                    foreach (Point nearbyPoint : point.getNeighbours()) {
                        arrayOfChanges.add(nearbyPoint);
                    }

                    // Apply rules to points in arrayOfChanges
                    processArrayOfChanges();
                }
            }

            // Check if Phase 2 is needed again
            needed = checkPhase2();
        }

        // ******* //
        //  DONE!  //
        // ******* //
        return solution;
    }

    List<Point> getInput(Set<Point> input)
    {
        List<Point> points = new ArrayList<>();
        for (Point point : input) {
            // Add per point 2 or 4 candidates (depending on the model)
            for (Point candidate: point.getCandidates(null))
            {
                points.add(candidate);
            }
        }
        return points;
    }

    Point applyRule1(Solution solution, Point point) {
        // Iterate over all points
        for (Point candidate : point.getCandidates(null)) {
            // Remove all candidates of point except the candidate with no conflicts
            if (solution.isPossible())
            for () {
                if (candidate != goodCandidate) {
                    point.removeCandidate(candidate);
                }
            }

            // Put all points in the neighborhood of point in arrayOfChanges
            foreach (Point nearbyPoint : point.getNeighbours()) {
                arrayOfChanges.add(nearbyPoint);
            }
        }
        return point;
    }

    Point applyRule2(point) {
        foreach (Label candidate : point.candidates) {
            // This will store the label which makes sure the candidate can be part of the end solution
            Label conflictSolver = null;

            // Get the conflicts of the candidate
            conflicts = getConflicts(candidate);

            // Check if there is only conflict, if so, check if it can be eliminated with rule 2
            if (conflicts.length == 1) {
                // Get point of conflicted label
                pointConflictedLabel = conflicts[0].getParent();

                // Loop through all candidates of conflicted point
                foreach (Label candidateConflictPoint : pointConflictedLabel.getCandidates()) {
                    // Check if its not the label which was in conflict with the candidate
                    if (candidateConflictPoint != conflict) {
                        conflictsCandidateConflictPoint = getConflicts(candidateConflictPoint);

                        // Variable to check if all conflicts can be avoided with rule 2
                        useableCandidate = true;

                        // Loop through all conflicts of candidate of conflict point
                        foreach (Label conflictCandidateConflictPoint : conflictsCandidateConflictPoint) {
                            // If the point of the label which is in conflict with the candidate of the conflict point --> Rule 2 cannot be applied
                            if (conflictCandidateConflictPoint.getParent() != point) {
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
            if (conflictSolver) {
                // Remove all candidates of point except the candidate with no conflicts
                foreach (Label label : point.getCandidates()) {
                    if (label != candidate) {
                        point.removeCandidate(label);
                    }
                }

                // Put all points in the neighborhood of point in arrayOfChanges
                foreach (Point nearbyPoint : point.getNeighbours()) {
                    arrayOfChanges.add(nearbyPoint);
                }

                // Remove all candidates of the conflict point except the conflictSolver
                foreach (Label label : pointConflictedLabel.getCandidates()) {
                    if (label != conflictSolver) {
                        pointConflictedLabel.removeCandidate(label);
                    }
                }

                // Put all points in the neighborhood of point in arrayOfChanges
                foreach (Point nearbyPoint : pointConflictedLabel.getNeighbours()) {
                    arrayOfChanges.add(nearbyPoint);
                }
            }
        }

        return point;
    }

    Point applyRule3(point) {
        // Get candidates of point
        candidates = point.getCandidates();

        // Check if there is only conflict, if so, check if it can be eliminated with rule 2
        if (candidates.length == 1) {
            // Get conflicts of candidate
            conflicts = getConflicts(candidate[0]);

            // Check if all conflicts and candidate itself overlap (clique)
            if (isOverlap(conflicts, candidate[0])) {
                // Remove all conflicts that overlap the candidate
                foreach (Label conflict : conflicts) {
                    pointOfConflict = conflict.getParent();
                    pointOfConflict.removeCandidate(conflict);
                }

                // Put all points in the neighborhood of point in arrayOfChanges
                foreach (Point nearbyPoint : pointOfConflict.getNeighbours()) {
                    arrayOfChanges.add(nearbyPoint);
                }
            }
        }

        return point;
    }

    void processArrayOfChanges(Solution solution)
    {
        for(Point point : changes)
        {
            Point newPoint;
            newPoint = applyRule1(point); // Apply Rule 1 (L1)
            newPoint = applyRule2(point); // Apply Rule 2 (L2)
            newPoint = applyRule3(point); // Apply Rule 2 (L2)

            if (!newPoint.isClone(point)) {
                // update point to newPoint
                solution.change(point, newPoint);
            }
        }
        changes.clear();
    }

    boolean checkPhase2(Solution solution)
    {
        for (Point point : solution)
        {
            List<Point> candidates = point.getCandidates();

            if (candidates.size() > 1)
            {
                return true;
            }
        }
        return false;
    }*/
}
