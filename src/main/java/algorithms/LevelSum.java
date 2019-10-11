package algorithms;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.util.BitExp;
import fr.uga.pddl4j.util.BitState;
import fr.uga.pddl4j.util.BitVector;

public class LevelSum extends PlanningGraph {

	public LevelSum(CodedProblem problem) {
		super(problem);
	}

	public int calculate(final BitState state, final BitExp goal) {
		super.setGoal(goal);
		this.expandPlanningGraph(state);
		int value = 0;
		final BitVector posGoal = goal.getPositive();
		final BitVector negGoal = goal.getNegative();
		for (int g = posGoal.nextSetBit(0); g >= 0; g = posGoal.nextSetBit(g + 1))
			value += super.getPosPropLevel(g);
		for (int g = negGoal.nextSetBit(0); g >= 0; g = negGoal.nextSetBit(g + 1))
			value += super.getNegPropLevel(g);
		return value;
	}
}
