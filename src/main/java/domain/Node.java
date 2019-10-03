package domain;

import fr.uga.pddl4j.util.BitOp;
import fr.uga.pddl4j.util.BitState;

public class Node extends BitState {

    // parent state
    private Node parent;
    // action taken that resulted in this state
    private BitOp action;
    // g(n)
    private int cost;
    // h(n)
    private int heuristic;

    public Node(BitState state) {
        super(state);
    }

    // f(n) = g(n) + h(n)
    public int getTotalCost() {
        return cost + heuristic;
    }

    public int getCost() { return cost; }

    public Node getParent() { return parent; }

    public BitOp getAction() { return action; }

    public void setCost(int cost) { this.cost = cost; }

    public void setParent(Node parent) { this.parent = parent; }

    public void setAction(BitOp action) { this.action = action; }

    public void setHeuristic(int heuristic) { this.heuristic = heuristic; }

}
