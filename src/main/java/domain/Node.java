package domain;

import fr.uga.pddl4j.util.BitOp;
import fr.uga.pddl4j.util.BitState;

public class Node {

    // parent state
    private Node parent;
    // action taken that resulted in this state
    private BitOp action;
    // g(n)
    private int cost;
    // h(n)
    private int heuristic;

    private BitState state;

    public Node() {
    }

    // f(n) = g(n) + h(n)
    public int getTotalCost() {
        return cost + heuristic;
    }

    public int getCost() { return cost; }

    public Node getParent() { return parent; }

    public BitOp getAction() { return action; }

    public BitState getState() {
        return state;
    }

    public void setState(BitState state) {
        this.state = state;
    }

    public void setCost(int cost) { this.cost = cost; }

    public void setParent(Node parent) { this.parent = parent; }

    public void setAction(BitOp action) { this.action = action; }

    public void setHeuristic(int heuristic) { this.heuristic = heuristic; }

}
