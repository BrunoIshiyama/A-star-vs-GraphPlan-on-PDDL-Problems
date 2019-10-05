package domain;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.util.BitOp;
import fr.uga.pddl4j.util.BitState;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Class responsible for AStar Search
 */
public class AStar {

    private static final int DEFAULT_COST = 1;

    private final HashMap<BitState, Node> openSet = new HashMap<>();
    private final HashMap<BitState, Node> closedSet = new HashMap<>();

    public AStar() {
    }

    public void search(CodedProblem codedProblem) {
        final PriorityQueue<Node> open = new PriorityQueue<>(100, new NodeComparator());

        BitState initialState = new BitState(codedProblem.getInit());
        Node initial = new Node();
        initial.setState(initialState);
        open.add(initial);
        Node solution = null;

        while (!open.isEmpty()){
            Node current = open.poll();
            open.remove(current);
            closedSet.put(current.getState(), current);

            if(current.getState().include(codedProblem.getGoal().getPositive())
                    && current.getState().exclude(codedProblem.getGoal().getNegative())) {
                solution = current;
                break;
            }

            List<BitOp> applicableOperators = findApplicableOperators(current, codedProblem.getOperators());
            applicableOperators.forEach(operator -> {
                Node successor = findSuccessorState(operator, current);
                Node resultNode = openSet.get(successor.getState());
                if( resultNode == null) {
                    resultNode = closedSet.get(successor.getState());
                    if(resultNode == null) {
                        open.add(successor);
                        openSet.put(successor.getState(), successor);
                    } else {
                        if (successor.getCost() < resultNode.getCost()) {
                            open.add(successor);
                            openSet.put(successor.getState(), successor);
                            closedSet.remove(successor.getState());
                        }
                    }
                } else if(successor.getCost() < resultNode.getCost()) {
                    openSet.put(successor.getState(), successor);
                    open.remove(resultNode);
                    open.add(successor);
                }
                // TODO: validar se a comparacao deve ser por custo ou por custo+heuristica
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
            if(node.getState().include(operator.getPreconditions().getPositive())
                    && node.getState().exclude(operator.getPreconditions().getNegative())) {
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
        Node successor = new Node();
        BitState newState = new BitState();
        operator.getCondEffects().forEach(condEffect ->{
            if(current.getState().include(condEffect.getEffects().getPositive())
                    && current.getState().exclude(condEffect.getEffects().getNegative())) {
                newState.apply(condEffect.getEffects());
            }
        });
        successor.setState(newState);
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
