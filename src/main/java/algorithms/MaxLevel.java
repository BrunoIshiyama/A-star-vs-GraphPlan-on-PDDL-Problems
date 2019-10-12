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
        super.expandPlanningGraph(state);
        int max = Integer.MIN_VALUE;
        final BitVector pGoal = goal.getPositive();
        for (int g = pGoal.nextSetBit(0); g >= 0; g = pGoal.nextSetBit(g + 1)) {
            final int gl = super.getPropositionLevel(g);
            if (gl > max) {
                max = gl;
            }
        }
        return max;
    }

}
