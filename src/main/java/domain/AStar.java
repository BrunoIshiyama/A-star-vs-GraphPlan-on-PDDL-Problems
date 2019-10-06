package domain;

import fr.uga.pddl4j.encoding.CodedProblem;
import fr.uga.pddl4j.util.BitOp;
import fr.uga.pddl4j.util.BitState;
import fr.uga.pddl4j.util.CondBitExp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Class responsible for AStar Search
 */
public class AStar {

    private static final int DEFAULT_COST = 1;

    private final HashMap<BitState, Node> openSet = new HashMap<>();
    private final HashMap<BitState, Node> closedSet = new HashMap<>();
    private final CodedProblem codedProblem;
    private int branchingFactorAverage = 0;

    public AStar(CodedProblem codedProblem) {
        this.codedProblem = codedProblem;
    }

    public String search() {
        long begin = System.currentTimeMillis();
        final PriorityQueue<Node> open = new PriorityQueue<>(100, new NodeComparator());

        BitState initialState = new BitState(codedProblem.getInit());
        Node initial = new Node();
        initial.setState(initialState);
        open.add(initial);
        Node solution = null;

        while (!open.isEmpty()){
            Node current = open.poll();
            openSet.remove(current.getState());
            closedSet.put(current.getState(), current);

            if(current.getState().include(codedProblem.getGoal().getPositive())
                    && current.getState().exclude(codedProblem.getGoal().getNegative())) {
                solution = current;
                break;
            }

            List<BitOp> applicableOperators = findApplicableOperators(current, codedProblem.getOperators());
            for(BitOp operator: applicableOperators){
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
            }
            if(branchingFactorAverage == 0) {
                branchingFactorAverage = applicableOperators.size();
            } else {
                branchingFactorAverage = (branchingFactorAverage + applicableOperators.size())/2;
            }
        }
        long algorithmTime = System.currentTimeMillis() - begin;
        String plan = generatePlan(solution);
        StringBuilder result = new StringBuilder();
        result.append(plan)
                .append("\nALGORITHM TIME: ").append(algorithmTime).append(" ms").append("\n")
                .append("\nTOTAL VISITED NODES: ").append(closedSet.size()).append("\n")
                .append("\nTOTAL CREATED NODES: ").append(closedSet.size() + openSet.size()).append("\n")
                .append("\nBRANCHING FACTOR: ").append(branchingFactorAverage).append("\n");


        return result.toString();

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
        BitState newState = new BitState(current.getState());
        for(CondBitExp condEffect: operator.getCondEffects()) {
            if(current.getState().include(condEffect.getCondition().getPositive())
                    && current.getState().exclude(condEffect.getCondition().getNegative())) {
                newState.apply(condEffect.getEffects());
            }
        }
        successor.setState(newState);
        successor.setAction(operator);
        successor.setParent(current);
        successor.setCost(current.getCost() + operator.getCost());
        successor.setHeuristic(1);
        return successor;
    }

    public String generatePlan(Node solution) {
        StringBuilder plan = new StringBuilder();
        int planSize = 0;
        for(Node n = solution; n.getParent()!=null; n = n.getParent()) {
            plan.insert(0,String.format("%s [%d]%n",
                    codedProblem.toShortString(n.getAction()),
                    (int)n.getAction().getDuration()));
            planSize++;
        }
        plan.insert(0,"PLAN: \n");
        plan.append("\nPLAN SIZE: ").append(planSize).append("\n");
        return plan.toString();
    }

}
