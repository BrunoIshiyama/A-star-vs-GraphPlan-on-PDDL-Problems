package domain;

import algorithms.FastForward;
import algorithms.LevelSum;
import algorithms.MaxLevel;
import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.util.BitState;

public class Heuristic {

    public enum Type {
        Default,
        LevelSum,
        MaxLevel,
        FastForward
    }

    private CodedProblem codedProblem;

    public Heuristic(CodedProblem codedProblem) {
        this.codedProblem = codedProblem;
    }

    public double calculate(Type heuristic, BitState state) {
        switch (heuristic) {
            case Default:
                return 1;
            case LevelSum:
                LevelSum levelSum  = new LevelSum(codedProblem);
                return levelSum.cost(state, codedProblem.getGoal());
            case MaxLevel:
                MaxLevel maxLevel  = new MaxLevel(codedProblem);
                return maxLevel.cost(state, codedProblem.getGoal());
            case FastForward:
                FastForward fastForward  = new FastForward(codedProblem);
                return fastForward.cost(state, codedProblem.getGoal());
        }
        return 1;
    }
}
