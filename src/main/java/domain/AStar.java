package domain;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.util.BitOp;
import fr.uga.pddl4j.util.BitState;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Class responsible for AStar Search
 */
public class AStar {

    private static final int DEFAULT_COST = 1;

    public AStar() {
    }

    public void search(CodedProblem codedProblem) {
        final PriorityQueue<Node> openSet = new PriorityQueue<>(100, new NodeComparator());
        final HashSet<Node> closedSet = new HashSet<>();

        BitState initialState = new BitState(codedProblem.getInit());
        Node initial = new Node(initialState);
        openSet.add(initial);
        Node solution = null;

        while (!openSet.isEmpty()){
            Node current = openSet.poll();
            openSet.remove(current);
            closedSet.add(current);

            if(current.include(codedProblem.getGoal().getPositive())
                    && current.exclude(codedProblem.getGoal().getNegative())) {
                solution = current;
                break;
            }

            List<BitOp> applicableOperators = findApplicableOperators(current, codedProblem.getOperators());
            applicableOperators.forEach(operator -> {
                Node successor = findSuccessorState(operator, current);
                // TODO: tratar quando já está no openSet e no closedSet
                openSet.add(successor);
            });
        }

        generatePlan(solution);

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
        successor.setAction(operator);
        successor.setParent(current);
        successor.setCost(current.getCost() + DEFAULT_COST);
        successor.setHeuristic(1);
        return successor;
    }

    public void generatePlan(Node solution) {
        //TODO: gerar plano
    }

}
