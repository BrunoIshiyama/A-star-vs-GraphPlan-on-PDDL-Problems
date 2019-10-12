package algorithms;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.util.BitExp;
import fr.uga.pddl4j.util.BitOp;
import fr.uga.pddl4j.util.BitState;
import fr.uga.pddl4j.util.BitVector;
import fr.uga.pddl4j.util.CondBitExp;
import fr.uga.pddl4j.util.IntExp;

import java.util.Arrays;
import java.util.List;

public abstract class PlanningGraph {

	private BitExp goal;
	private List<IntExp> facts;
	private List<BitOp> operators;
	private int[][] unconditionalOperators;
	private int[] precondCardinality;
	private int[] operatorsLevel;
	private int[] precondCounters;
	private int[] propositionLevel;
	private BitExp[] precondEdges;
	private BitExp[] effectsEdges;
	private BitExp[] preconditions;
	private BitExp[] effects;
	private BitExp[] unconditionalEffects;
	private int goalCounter;
	private int goalPropositionCost;
	private int level;

	/**
	 * Generates the PlanningGraph structure for the heuristics.
	 *
	 * @param problem the PDDL problem.
	 */
	protected PlanningGraph(final CodedProblem problem) {
		this.goal = problem.getGoal();
		this.goalPropositionCost = goal.cardinality();
		this.facts = problem.getRelevantFacts();
		this.operators = problem.getOperators();
		final int nFacts = facts.size();
		final int nOperators = problem.getOperators().size();
		int nUnconditionalOperators = 0;
		final List<BitOp> operators = problem.getOperators();
		for (BitOp op : operators) {
			nUnconditionalOperators += op.getCondEffects().size();
		}
		this.propositionLevel = new int[nFacts];
		this.operatorsLevel = new int[nUnconditionalOperators];
		this.precondCounters = new int[nUnconditionalOperators];
		this.preconditions = new BitExp[nUnconditionalOperators];
		this.effects = new BitExp[nUnconditionalOperators];
		this.unconditionalEffects = new BitExp[nOperators];
		for (int i = 0; i < this.unconditionalEffects.length; i++) {
			this.unconditionalEffects[i] = new BitExp();
		}
		this.unconditionalOperators = new int[nUnconditionalOperators][];
		this.precondEdges = new BitExp[nFacts];
		for (int i = 0; i < this.precondEdges.length; i++) {
			this.precondEdges[i] = new BitExp();
		}
		this.effectsEdges = new BitExp[nFacts];
		for (int i = 0; i < this.effectsEdges.length; i++) {
			this.effectsEdges[i] = new BitExp();
		}
		this.goalPropositionCost = goal.cardinality();
		this.precondCardinality = new int[nUnconditionalOperators];
		int unconditionalOperationIndex = 0;

		for (int opIndex = 0; opIndex < operators.size(); opIndex++) {
			final BitOp op = operators.get(opIndex);
			final List<CondBitExp> condEffects = op.getCondEffects();

			for (int ceIndex = 0; ceIndex < condEffects.size(); ceIndex++) {
				final CondBitExp cEffect = condEffects.get(ceIndex);
				final int[] eff = { opIndex, ceIndex };
				this.unconditionalOperators[unconditionalOperationIndex] = eff;

				final BitExp pre = new BitExp(op.getPreconditions());
				final BitVector posPreconditions = pre.getPositive();
				posPreconditions.or(cEffect.getCondition().getPositive());
				for (int p = posPreconditions.nextSetBit(0); p >= 0; p = posPreconditions.nextSetBit(p + 1)) {
					this.precondEdges[p].getPositive().set(unconditionalOperationIndex);
				}

				this.preconditions[unconditionalOperationIndex] = pre;

				final BitExp effect = cEffect.getEffects();
				final BitVector pEff = effect.getPositive();
				final BitVector nEff = effect.getNegative();
				for (int p = pEff.nextSetBit(0); p >= 0; p = pEff.nextSetBit(p + 1)) {
					this.effectsEdges[p].getPositive().set(unconditionalOperationIndex);
				}
				for (int p = nEff.nextSetBit(0); p >= 0; p = nEff.nextSetBit(p + 1)) {
					this.effectsEdges[p].getNegative().set(unconditionalOperationIndex);
				}

				this.effects[unconditionalOperationIndex] = effect;

				this.precondCardinality[unconditionalOperationIndex] = pre.cardinality();

				if (cEffect.getCondition().isEmpty()) {
					final BitExp unconditionalEffect = this.unconditionalEffects[opIndex];
					final BitExp condEff = cEffect.getEffects();
					unconditionalEffect.getPositive().or(condEff.getPositive());
					unconditionalEffect.getNegative().or(condEff.getNegative());
				}

				unconditionalOperationIndex++;
			}
		}

	}

	/**
	 * This method expands the planning graph until it hits its goal
	 *
	 * @param state the initial state of the relaxed planning graph.
	 * @return 
	 * @return the level of the graph built.
	 */
	protected final void expandPlanningGraph(final BitState state) {

		Arrays.fill(this.operatorsLevel, Integer.MAX_VALUE);
		Arrays.fill(this.propositionLevel, Integer.MAX_VALUE);
		Arrays.fill(this.precondCounters, 0);

		final BitVector posGoals = goal.getPositive();
		this.goalCounter = 0;

		this.level = 0;
		BitVector posPropositions = new BitVector(state);
		for (int p = posPropositions.nextSetBit(0); p >= 0; p = posPropositions.nextSetBit(p + 1)) {
			this.propositionLevel[p] = 0;
			if (posGoals.get(p)) {
				this.goalCounter++;
			}
		}

		final BitVector propositionsReached = new BitVector();

		while (this.goalCounter != this.goalPropositionCost && !posPropositions.isEmpty()) {
			final BitVector newOperations = new BitVector();
			for (int p = posPropositions.nextSetBit(0); p >= 0; p = posPropositions.nextSetBit(p + 1)) {
				final BitVector posEdges = this.precondEdges[p].getPositive();
				propositionsReached.set(p);
				for (int pe = posEdges.nextSetBit(0); pe >= 0; pe = posEdges.nextSetBit(pe + 1)) {
					if (this.precondCardinality[pe] != 0) {
						this.precondCounters[pe]++;
					}
					if (this.precondCounters[pe] == this.precondCardinality[pe]) {
						newOperations.set(pe);
					}
				}
			}
			final BitVector newPropositions = new BitVector();
			for (int o = newOperations.nextSetBit(0); o >= 0; o = newOperations.nextSetBit(o + 1)) {
				this.operatorsLevel[o] = this.level;
				newPropositions.or(this.effects[o].getPositive());
			}

			posPropositions = newPropositions;
			posPropositions.andNot(propositionsReached);

			this.level++;
			for (int p = posPropositions.nextSetBit(0); p >= 0; p = posPropositions.nextSetBit(p + 1)) {
				this.propositionLevel[p] = this.level;
				if (posGoals.get(p)) {
					this.goalCounter++;
				}
			}
		}
	}

	protected int getPropositionLevel(int n) {
		return propositionLevel[n];
	}
	
	protected int getLevel() {
		return this.level;
	}
	
	protected BitExp getEffectsEdges(int n) {
		return effectsEdges[n];
	}
	
	protected BitExp getPreconditions(int n) {
		return preconditions[n];
	}
	
	protected BitExp getEffects(int n) {
		return effects[n];
	}
	
	protected List<BitOp> getOperators() {
		return operators;
	}

}
