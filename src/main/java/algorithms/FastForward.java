package algorithms;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.util.BitExp;
import fr.uga.pddl4j.util.BitState;
import fr.uga.pddl4j.util.BitVector;

public class FastForward extends PlanningGraph {

	public FastForward(CodedProblem problem) {
		super(problem);
	}
	
	public final int cost(final BitState state, final BitExp goal) {
		//create planning graph
		super.expandPlanningGraph(state);
        int value = 0;
        final BitExp[] goals = new BitExp[super.getLevel() + 1];
        for (int lvl = 0; lvl <= super.getLevel(); lvl++) {
            goals[lvl] = new BitExp();
        }
        final BitVector pGoal = goal.getPositive();
        for (int g = pGoal.nextSetBit(0); g >= 0; g = pGoal.nextSetBit(g + 1)) {
            goals[super.getPosPropositionLevel(g)].getPositive().set(g);
        }
        //extract
        for (int lvl = super.getLevel(); lvl > 0; lvl--) {
            final BitExp glvl = goals[lvl];
            final BitVector pGlvl = glvl.getPositive();
            final BitExp glvl1 = goals[lvl - 1];
            final BitVector pGlvl1 = glvl1.getPositive();
            for (int pg = pGlvl.nextSetBit(0); pg >= 0; pg = pGlvl.nextSetBit(pg + 1)) {
                final int resolverIndex = super.getEffectsEdges(pg).getPositive().cardinality();
                if (resolverIndex != -1) {
                    final BitExp pre = super.getPreconditions(resolverIndex);
                    final BitVector posPreconditions = pre.getPositive();
                    for (int p = posPreconditions.nextSetBit(0); p >= 0; p = posPreconditions.nextSetBit(p + 1)) {
                        final int pLevel = super.getPosPropositionLevel(p);
                        if (pLevel != 0 && !pGlvl1.get(p)) {
                            goals[pLevel].getPositive().set(p);
                        }
                    }
                    final BitExp effect = super.getEffects(resolverIndex);
                    final BitVector pEffect = effect.getPositive();
                    pGlvl1.andNot(pEffect);
                    pGlvl.andNot(pEffect);
                    value += super.getOperators().get(resolverIndex).getCost();
                } else {
                    pGlvl1.clear(pg);
                    pGlvl.clear(pg);
                }
            }
        }
        return value;
    }

}
