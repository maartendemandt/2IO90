package some.pack.age.algorithm;

import some.pack.age.models.AxisAlignedBB;
import some.pack.age.models.labels.PosLabel;
import some.pack.age.models.solution.EIL3Solution;
import some.pack.age.models.Point;
import some.pack.age.models.solution.Solution;

import java.util.*;

import some.pack.age.models.labels.AbstractLabel;

/**
 * @author DarkSeraphim.
 */
public class EIL3Algorithm implements IAlgorithm
{
    // Array which contains all the changes points which haven't been processed again
    private final List<AbstractLabel> changes = new ArrayList<>();

    private int width, height;

    private boolean debug = false;

    public void debug(String line)
    {
        if (!debug) return;
        System.out.println(line);
    }

    @Override
    public Solution computePoints(Set<AbstractLabel> points, int width, int height)
    {
        this.width = width;
        this.height = height;
        EIL3Solution solution = new EIL3Solution(width, height, getInput(points));
        // Should initialize the candidates for the point
        solution.forEach(solution::getCandidates);

        /*AbstractLabel testlabel = PosLabel.create4PosLabel(893, 2204);
        AbstractLabel testlabel2 = PosLabel.create4PosLabel(890, 2224);
        System.out.println(solution.getNeighbours(testlabel));
        System.out.println(solution.getNeighbours(testlabel2));
        new ArrayList<>(solution.getCandidates(testlabel2)).forEach(solution::removeCandidate);
        System.out.println(solution.getCandidates(testlabel).size());
        System.out.println(applyRule1(solution, testlabel));
        System.out.println(solution.getCandidates(testlabel).size());

        System.exit(-1);*/
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

            /*if (old.isClone(label)) {
                // update point to newPoint
                solution.change(old, label);
            }*/

            // Apply rules to points in arrayOfChanges
        }
        processArrayOfChanges(solution, width, height);

        // ******* //
        // Phase 2 //
        // ******* //

        // If Phase 2 is needed, execute phase 2
        while (checkPhase2(solution))
        {
            debug("\n\n");
            int maxCandidates = 0;
            List<AbstractLabel> labelsMostCandidates = new ArrayList<>();

            // Find points with heighest amount of candidates
            for (AbstractLabel point : solution) {
                List<AbstractLabel> candidates = solution.getCandidates(point);
                int numOfCandidates = candidates.size();

                // if number of candidates is larger than the current maximum of candidates found --> empty array, add point to array and set new maximum
                if (numOfCandidates >= maxCandidates)
                {
                    if (numOfCandidates > maxCandidates)
                    {
                        labelsMostCandidates.clear();
                        maxCandidates = numOfCandidates;
                    }
                    labelsMostCandidates.add(point);
                }
                // In honour of this bug, I leave it commented. Thanks for the near heart-attack, friend.
                // labelsMostCandidates.add(point);
            }

            debug("Loop data:");
            for (AbstractLabel label : labelsMostCandidates)
            {
                debug("Found label " + label + ". Candidates: ");
                for (AbstractLabel candidate : solution.getCandidates(label))
                {
                    debug("  " + candidate + " with " + solution.getConflicts(candidate).size() + " conflicts");
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

                    for (AbstractLabel label : solution.getNeighbours(candidateMostConflicts))
                    {
                        AbstractLabel old = label;
                        label = applyRule1(solution, label); // Apply Rule 1 (L1)
                        label = applyRule2(solution, label); // Apply Rule 2 (L2)
                        label = applyRule3(solution, label, width, height); // Apply Rule 3 (L3)
                    }
                    // Apply rules to points in arrayOfChanges
                    processArrayOfChanges(solution, width, height);
                }
            }
            for (AbstractLabel label : solution)
            {
                AbstractLabel old = label;
                label = applyRule1(solution, label); // Apply Rule 1 (L1)
                label = applyRule2(solution, label); // Apply Rule 2 (L2)
                label = applyRule3(solution, label, width, height); // Apply Rule 3 (L3)
            }
            processArrayOfChanges(solution, width, height);
            debug("\n\n");
        }
        solution.getPoints().stream()
                .filter(point -> solution.getCandidates(point).size() <= 1)
                .forEach(this.changes::add);
        processArrayOfChanges(solution, width, height);
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
        Set<AbstractLabel> neighbours = solution.getNeighbours(point);
        for (AbstractLabel candidate : solution.getCandidates(point))
        {
            debug("Candidate " + candidate);
            // Remove all candidates of point except the candidate with no conflicts
            AxisAlignedBB aabb = candidate.getAABB(this.width, this.height);
            boolean possible = true;
            for (AbstractLabel neighbour : neighbours)
            {
                debug("  Neighbour " + neighbour);
                List<AbstractLabel> neighbourCandidates = solution.getCandidates(neighbour);
                if (!aabb.contains(neighbour))
                {
                    for (AbstractLabel neighbourCandidate : neighbourCandidates)
                    {
                        debug("    Neighbour candidate: " + neighbourCandidate);
                        if (!neighbourCandidate.isValid())
                        {
                            continue;
                        }
                        AxisAlignedBB other = neighbourCandidate.getAABB(this.width, this.height);
                        if (other.overlaps(aabb))
                        {
                            debug("      " + other + " overlaps " + aabb);
                            possible = false;
                            break;
                        }
                        else
                            debug("      No overlap");
                    }
                }
                else if (!neighbourCandidates.isEmpty())
                {
                    debug("    Contains");
                    possible = false;
                }
                if (!possible)
                {
                    break;
                }
            }
            debug("Possible? " + possible);
            if (!possible)
            {
                continue;
            }
            new ArrayList<>(solution.getCandidates(point)).forEach(solution::removeCandidate);
            this.changes.add(candidate);
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
            AbstractLabel pointConflictedLabel;

            // Check if there is only conflict, if so, check if it can be eliminated with rule 2
            if (conflicts.size() == 1)
            {
                // Get point of conflicted label
                // What is the parent?
                pointConflictedLabel = conflicts.get(0);

                AxisAlignedBB cAABB = candidate.getAABB(this.width, this.height);
                if (pointConflictedLabel.getAABB(this.width, this.height).contains(candidate) || cAABB.contains(pointConflictedLabel))
                {
                    continue; // Contained points can never be solutions
                }

                // Loop through all candidates of conflicted point
                for (AbstractLabel candidateConflictPoint : solution.getCandidates(pointConflictedLabel))
                {
                    // Check if its not the label which was in conflict with the candidate
                    if (!candidateConflictPoint.isClone(pointConflictedLabel) && candidateConflictPoint.equals(pointConflictedLabel))
                    {
                        List<AbstractLabel> conflictsCandidateConflictPoint = solution.getConflicts(candidateConflictPoint);

                        // Variable to check if all conflicts can be avoided with rule 2
                        boolean useableCandidate = true;

                        // Loop through all conflicts of candidate of conflict point
                        for (AbstractLabel conflictCandidateConflictPoint : conflictsCandidateConflictPoint)
                        {
                            // If the point of the label which is in conflict with the candidate of the conflict point --> Rule 2 cannot be applied
                            if (cAABB.overlaps(conflictCandidateConflictPoint.getAABB(this.width, this.height)))
                            {
                                useableCandidate = false;
                            }
                            // If there's a third point part of the story, disallow the change
                            else if (!conflictCandidateConflictPoint.equals(candidate))
                            {
                                useableCandidate = false;
                            }
                        }

                        // Check if there is no conflict found which prevents rule 2 from applying
                        if (useableCandidate)
                        {

                            conflictSolver = candidateConflictPoint;
                            break; // break out of foreach loop which loops through all candidates of conflicted point
                        }
                    }
                }
            }

            // If there is a conflictSolver, use it!
            if (conflictSolver != null) {
                final AbstractLabel cs = conflictSolver;
                // Remove all candidates of point except the candidate with no conflicts
                new ArrayList<>(solution.getCandidates(candidate)).forEach(solution::removeCandidate);

                this.changes.add(candidate);

                new ArrayList<>(solution.getCandidates(cs)).forEach(solution::removeCandidate);
                this.changes.add(conflictSolver);
                break;
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
            AbstractLabel onlyCandidate = candidates.get(0);
            AxisAlignedBB only = onlyCandidate.getAABB(width, height);
            for (AbstractLabel neighbour : solution.getNeighbours(onlyCandidate))
            {
                if (only.contains(neighbour) && !solution.getCandidates(neighbour).isEmpty())
                    return point;
            }
            List<AbstractLabel> conflicts = solution.getConflicts(onlyCandidate);

            boolean isOverlap = true;
            for (AbstractLabel conflict : conflicts)
            {
                if (!conflict.isValid())
                {
                    continue;
                }
                AxisAlignedBB aabb = conflict.getAABB(width, height);
                for (AbstractLabel other : conflicts)
                {
                    if (!other.isValid() || conflict.equals(other))
                    {
                        continue;
                    }
                    isOverlap = isOverlap && conflict.getAABB(width, height).overlaps(aabb);
                    if (!isOverlap)
                    {
                        break;
                    }
                }
                if (!isOverlap)
                {
                    break;
                }
            }

            // Check if all conflicts and candidate itself overlap (clique)
            if (isOverlap)
            {
                // Remove all conflicts that overlap the candidate
                conflicts.forEach(solution::removeCandidate);
                new ArrayList<>(solution.getCandidates(point)).forEach(solution::removeCandidate);
                this.changes.add(onlyCandidate);
            }
        }

        return point;
    }

    void processArrayOfChanges(EIL3Solution solution, int width, int height)
    {
        for(AbstractLabel point : changes)
        {
            AbstractLabel old = point;
            // point = applyRule1(solution, point); // Apply Rule 1 (L1)
            // point = applyRule2(solution, point); // Apply Rule 2 (L2)
            // point = applyRule3(solution, point, width, height); // Apply Rule 3 (L3)

            //if (!old.isClone(point)) {
                // update point to newPoint
                solution.change(old, point);
            //}
        }
        changes.clear();
    }

    private static int prev = -1;

    private static double q = -1;

    private static int loopnum = 0;

    boolean checkPhase2(EIL3Solution solution)
    {
        boolean b = false;
        int size = 0;
        for (AbstractLabel point : solution)
        {
            List<AbstractLabel> candidates = solution.getCandidates(point);
            size += candidates.size();
            if (candidates.size() > 1)
            {
                b = true;
            }
        }
        if (prev == size)
        {
            //this.debug = true;
            this.applyRule1(solution, PosLabel.create4PosLabel(587, 885));
            System.out.println("Previous quality: " + q);
            System.out.println("Current quality: " + solution.getQuality());
            throw new IllegalStateException("Loop without progress " + loopnum);
        }
        prev = size;
        q = solution.getQuality();
        loopnum++;
        return b;
    }
}
