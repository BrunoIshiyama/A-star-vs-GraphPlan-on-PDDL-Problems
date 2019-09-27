package model;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.util.BitOp;
import fr.uga.pddl4j.util.BitState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Class responsible for AStar Search
 */
public class AStar {

    public AStar() {
    }

    public void search(CodedProblem codedProblem) {
        PriorityQueue openSet = new PriorityQueue(100, new NodeComparator());
        Set closedSet = new HashSet();

        BitState initialState = new BitState(codedProblem.getInit());
        Node initial = new Node(initialState);
    }

    /**
     * * Responsible for finding all operators that can be applied to a specific state
     *
     * @param node current state
     * @param operators all actions
     * @return list of applicable operators
     */
    public List<BitOp> findApplicableOperators(Node node, List<BitOp> operators) {
        List<BitOp> applicableOperators = new ArrayList<>();
        operators.forEach(operator -> {
            if(node.include(operator.getPreconditions().getPositive())
                    && node.exclude(operator.getPreconditions().getNegative())) {
                applicableOperators.add(operator);
            }
        });
        return applicableOperators;
    }

    /**
     * Responsible for finding the successor state after applying an operator
     *
     * @param operator - action taken
     * @param current - current node (state)
     * @return successor node(state)
     */
    public Node findSuccessorState (BitOp operator, Node current) {
        Node successor = new Node(current);
        operator.getCondEffects().forEach(condEffect ->{
            if(current.include(condEffect.getEffects().getPositive())
                    && current.exclude(condEffect.getEffects().getNegative())) {
                successor.apply(condEffect.getEffects());
            }
        });
        return successor;
    }

}
