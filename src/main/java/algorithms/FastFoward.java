package algorithms;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.util.BitExp;
import fr.uga.pddl4j.util.BitState;
import fr.uga.pddl4j.util.BitVector;

public class FastFoward extends PlanningGraph {

	public FastFoward(CodedProblem problem) {
		super(problem);
	}
	
	public final int cost(final BitState state, final BitExp goal) {
		super.expandPlanningGraph(state);
        int value = 0;
        final BitExp[] goals = new BitExp[super.getLevel() + 1];
        for (int k = 0; k <= super.getLevel(); k++) {
            goals[k] = new BitExp();
        }
        final BitVector pGoal = goal.getPositive();
        for (int g = pGoal.nextSetBit(0); g >= 0; g = pGoal.nextSetBit(g + 1)) {
            goals[super.getPropositionLevel(g)].getPositive().set(g);
        }

        for (int k = super.getLevel(); k > 0; k--) {
            final BitExp gk = goals[k];
            final BitVector pGk = gk.getPositive();
            final BitExp gk1 = goals[k - 1];
            final BitVector pGk1 = gk1.getPositive();
            for (int pg = pGk.nextSetBit(0); pg >= 0; pg = pGk.nextSetBit(pg + 1)) {
                final int resolverIndex = super.getEffectsEdges(pg).getPositive().cardinality();
                if (resolverIndex != -1) {
                    final BitExp pre = super.getPreconditions(resolverIndex);
                    final BitVector posPreconditions = pre.getPositive();
                    for (int p = posPreconditions.nextSetBit(0); p >= 0; p = posPreconditions.nextSetBit(p + 1)) {
                        final int pLevel = super.getPropositionLevel(p);
                        if (pLevel != 0 && !pGk1.get(p)) {
                        	System.out.println(pLevel);
                            goals[pLevel].getPositive().set(p);
                        }
                    }
                    final BitExp effect = super.getEffects(resolverIndex);
                    final BitVector pEffect = effect.getPositive();
                    pGk1.andNot(pEffect);
                    pGk.andNot(pEffect);
                    value += super.getOperators().get(resolverIndex).getCost();
                } else {
                    pGk1.clear(pg);
                    pGk.clear(pg);
                }
            }
        }
        return value;
    }

}
