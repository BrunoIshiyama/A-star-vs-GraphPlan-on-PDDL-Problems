package algorithms;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.util.BitExp;
import fr.uga.pddl4j.util.BitState;
import fr.uga.pddl4j.util.BitVector;

public class MaxLevel extends PlanningGraph  {

	public MaxLevel(CodedProblem problem) {
		super(problem);
	}
	
    public int cost(final BitState state, final BitExp goal) {
    	//create planning graph
        super.expandPlanningGraph(state);
        int max = 0;
        final BitVector pGoal = goal.getPositive();
        final BitVector negGoal = goal.getNegative();
        //extract
        for (int g = pGoal.nextSetBit(0); g >= 0; g = pGoal.nextSetBit(g + 1)) {
            max = Math.max(max, super.getPosPropositionLevel(g));
        }
        for (int g = negGoal.nextSetBit(0); g >= 0; g = negGoal.nextSetBit(g + 1)) {
            max = Math.max(max, super.getNegPropositionLevel(g));
        }
        return max;
    }

}
