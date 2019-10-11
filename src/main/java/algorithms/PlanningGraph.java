package algorithms;

import java.util.Arrays;
import java.util.List;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.util.BitExp;
import fr.uga.pddl4j.util.BitOp;
import fr.uga.pddl4j.util.BitState;
import fr.uga.pddl4j.util.BitVector;
import fr.uga.pddl4j.util.CondBitExp;
import fr.uga.pddl4j.util.IntExp;

public abstract class PlanningGraph {

	private BitExp goal;
	private List<IntExp> facts;
	private List<BitOp> operators;
	private int[][] unconditionalOperators;
	private int[] precondCardinality;
	private int[] operatorsLevel;
	private int[] precondCounters;
	private int[] operatorsDifficulty;
	private int[] posPropLevel;
	private int[] negPropLevel;
	private BitExp[] precondEdges;
	private BitExp[] effectsEdges;
	private BitExp[] preconditions;
	private BitExp[] effects;
	private BitExp[] unconditionalEffects;
	private int goalCounter;
	private int goalCardinality;
	private int level;

	/**
	 * Generates the PlanningGraph for the heuristics.
	 *
	 * @param problem the PDDL problem.
	 */
	protected PlanningGraph(final CodedProblem problem) {
		final int nFacts = problem.getRelevantFacts().size();
		final int nOperators = problem.getOperators().size();
		int problemUnconditionalOperators = 0;
		operators = problem.getOperators();

		for (BitOp op : operators) {
			problemUnconditionalOperators += op.getCondEffects().size();
		}

		this.goal = problem.getGoal();
		this.facts = problem.getRelevantFacts();
		this.operatorsLevel = new int[problemUnconditionalOperators];
		this.precondCounters = new int[problemUnconditionalOperators];
		this.preconditions = new BitExp[problemUnconditionalOperators];
		this.posPropLevel = new int[nFacts];
		this.negPropLevel = new int[nFacts];
		this.effects = new BitExp[problemUnconditionalOperators];
		this.unconditionalEffects = new BitExp[nOperators];
		for (int i = 0; i < this.unconditionalEffects.length; i++) {
			this.unconditionalEffects[i] = new BitExp();
		}
		this.unconditionalOperators = new int[problemUnconditionalOperators][];
		this.precondEdges = new BitExp[nFacts];
		for (int i = 0; i < this.precondEdges.length; i++) {
			this.precondEdges[i] = new BitExp();
		}
		this.effectsEdges = new BitExp[nFacts];
		for (int i = 0; i < this.effectsEdges.length; i++) {
			this.effectsEdges[i] = new BitExp();
		}
		this.goalCardinality = problem.getGoal().cardinality();
		this.precondCardinality = new int[problemUnconditionalOperators];

		int uncondOpIndex = 0;

		for (int opIndex = 0; opIndex < operators.size(); opIndex++) {
			final BitOp op = operators.get(opIndex);
			final List<CondBitExp> condEffects = op.getCondEffects();

			for (int ceIndex = 0; ceIndex < condEffects.size(); ceIndex++) {
				final CondBitExp cEffect = condEffects.get(ceIndex);
				final int[] eff = { opIndex, ceIndex };
				this.unconditionalOperators[uncondOpIndex] = eff;

				final BitExp pre = new BitExp(op.getPreconditions());
				final BitVector posPreconditions = pre.getPositive();
				final BitVector negPreconditions = pre.getNegative();
				posPreconditions.or(cEffect.getCondition().getPositive());
				negPreconditions.or(cEffect.getCondition().getNegative());
				for (int p = posPreconditions.nextSetBit(0); p >= 0; p = posPreconditions.nextSetBit(p + 1)) {
					this.precondEdges[p].getPositive().set(uncondOpIndex);
				}
				for (int p = negPreconditions.nextSetBit(0); p >= 0; p = negPreconditions.nextSetBit(p + 1)) {
					this.precondEdges[p].getNegative().set(uncondOpIndex);
				}

				this.preconditions[uncondOpIndex] = pre;

				final BitExp effect = cEffect.getEffects();
				final BitVector pEff = effect.getPositive();
				final BitVector nEff = effect.getNegative();
				for (int p = pEff.nextSetBit(0); p >= 0; p = pEff.nextSetBit(p + 1)) {
					this.effectsEdges[p].getPositive().set(uncondOpIndex);
				}
				for (int p = nEff.nextSetBit(0); p >= 0; p = nEff.nextSetBit(p + 1)) {
					this.effectsEdges[p].getNegative().set(uncondOpIndex);
				}

				this.effects[uncondOpIndex] = effect;

				this.precondCardinality[uncondOpIndex] = pre.cardinality();

				if (cEffect.getCondition().isEmpty()) {
					final BitExp uncondEff = this.unconditionalEffects[opIndex];
					final BitExp condEff = cEffect.getEffects();
					uncondEff.getPositive().or(condEff.getPositive());
					uncondEff.getNegative().or(condEff.getNegative());
				}

				uncondOpIndex++;
			}
		}

		for (int i = 0; i < problemUnconditionalOperators; i++) {
			if (this.preconditions[i].isEmpty()) {
				for (BitExp pEdge : precondEdges) {
					pEdge.getPositive().set(i);
					pEdge.getNegative().set(i);
				}
			}
		}

	}
	
	protected final void setGoal(final BitExp goal) {
		this.goalCardinality = goal.cardinality();
	}

	/**
	 * This method creates the relaxed planning graph from a specified initial
	 * state.
	 *
	 * @param state the initial state of the relaxed planning graph.
	 * @return the level of the graph built.
	 */
	protected final int expandPlanningGraph(final BitState state) {

		final BitVector posGoal = goal.getPositive();
		final BitVector negGoal = goal.getNegative();
		this.goalCounter = 0;
		
		Arrays.fill(this.operatorsLevel, Integer.MAX_VALUE);
		Arrays.fill(this.posPropLevel, Integer.MAX_VALUE);
		Arrays.fill(this.negPropLevel, Integer.MAX_VALUE);
		Arrays.fill(this.precondCounters, 0);

		this.level = 0;
		BitVector posPropositions = new BitVector(state);
		BitVector negPropositions = new BitVector();
		negPropositions.flip(0, facts.size());
		negPropositions.andNot(state);
		for (int p = posPropositions.nextSetBit(0); p >= 0; p = posPropositions.nextSetBit(p + 1)) {
			this.posPropLevel[p] = 0;
			if (posGoal.get(p)) {
				this.goalCounter++;
			}
		}
		for (int p = negPropositions.nextSetBit(0); p >= 0; p = negPropositions.nextSetBit(p + 1)) {
			this.negPropLevel[p] = 0;
			if (negGoal.get(p)) {
				this.goalCounter++;
			}
		}

		final BitVector posAcc = new BitVector();
		final BitVector negAcc = new BitVector();

		while (this.goalCounter != this.goalCardinality && (!posPropositions.isEmpty() || !negPropositions.isEmpty())) {
			final BitVector newOperations = new BitVector();
			for (int p = posPropositions.nextSetBit(0); p >= 0; p = posPropositions.nextSetBit(p + 1)) {
				final BitVector posEdges = this.precondEdges[p].getPositive();
				posAcc.set(p);
				for (int pe = posEdges.nextSetBit(0); pe >= 0; pe = posEdges.nextSetBit(pe + 1)) {
					if (this.precondCardinality[pe] != 0) {
						this.precondCounters[pe]++;
					}
					if (this.precondCounters[pe] == this.precondCardinality[pe]) {
						newOperations.set(pe);
					}
				}
			}
			for (int p = negPropositions.nextSetBit(0); p >= 0; p = negPropositions.nextSetBit(p + 1)) {
				final BitVector negEdges = this.precondEdges[p].getNegative();
				negAcc.set(p);
				for (int pe = negEdges.nextSetBit(0); pe >= 0; pe = negEdges.nextSetBit(pe + 1)) {
					if (this.precondCardinality[pe] != 0) {
						this.precondCounters[pe]++;
					}
					if (this.precondCounters[pe] == this.precondCardinality[pe]) {
						newOperations.set(pe);
					}
				}
			}
			final BitVector posNewPropositions = new BitVector();
			final BitVector negNewPropositions = new BitVector();
			posPropositions = posNewPropositions;
			negPropositions = negNewPropositions;
			posPropositions.andNot(posAcc);
			negPropositions.andNot(negAcc);

			this.level++;
			for (int p = posPropositions.nextSetBit(0); p >= 0; p = posPropositions.nextSetBit(p + 1)) {
				this.posPropLevel[p] = this.level;
				if (posGoal.get(p)) {
					this.goalCounter++;
				}
			}

			for (int p = negPropositions.nextSetBit(0); p >= 0; p = negPropositions.nextSetBit(p + 1)) {
				this.negPropLevel[p] = this.level;
				if (negGoal.get(p)) {
					this.goalCounter++;
				}
			}
		}
		return this.level;
	}
	
	
	protected int getPosPropLevel(int n) {
		return posPropLevel[n];
	}
	
	protected int getNegPropLevel(int n) {
		return negPropLevel[n];
	}
}


