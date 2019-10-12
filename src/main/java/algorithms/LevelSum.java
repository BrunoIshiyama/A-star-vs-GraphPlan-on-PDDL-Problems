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
		this.expandPlanningGraph(state);
		int value = 0;
		final BitVector posGoal = goal.getPositive();
		for (int g = posGoal.nextSetBit(0); g >= 0; g = posGoal.nextSetBit(g + 1))
			value += super.getPropositionLevel(g);
		return value;
	}
}
