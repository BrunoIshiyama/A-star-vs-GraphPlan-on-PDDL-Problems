package algorithms;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.util.BitExp;
import fr.uga.pddl4j.util.BitState;
import fr.uga.pddl4j.util.BitVector;

public class LevelSum extends PlanningGraph {

	public LevelSum(CodedProblem problem) {
		super(problem);
	}

	public int cost(final BitState state, final BitExp goal) {
		//create planning graph
		this.expandPlanningGraph(state);
		int sum = 0;
		final BitVector posGoal = goal.getPositive();
		final BitVector negGoal = goal.getNegative();
		//extract
		for (int g = posGoal.nextSetBit(0); g >= 0; g = posGoal.nextSetBit(g + 1))
			sum += super.getPosPropositionLevel(g);
		for (int g = negGoal.nextSetBit(0); g >= 0; g = negGoal.nextSetBit(g + 1))
			sum += super.getNegPropositionLevel(g);
		return sum;
	}
}
