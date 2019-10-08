package domain;

import fr.uga.pddl4j.util.BitOp;
import fr.uga.pddl4j.util.BitState;

public class Node {
    
    // parent state
    private Node parent;
    // action taken that resulted in this state
    private BitOp action;
    // g(n)
    private double cost;
    // h(n)
    private double heuristic;

    private BitState state;

    public Node() {
    }

    // f(n) = g(n) + h(n)
    public double getTotalCost() {
        return cost + heuristic;
    }

    public double getCost() { return cost; }

    public Node getParent() { return parent; }

    public BitOp getAction() { return action; }

    public BitState getState() {
        return state;
    }

    public void setState(BitState state) {
        this.state = state;
    }

    public void setCost(double cost) { this.cost = cost; }

    public void setParent(Node parent) { this.parent = parent; }

    public void setAction(BitOp action) { this.action = action; }

    public void setHeuristic(double heuristic) { this.heuristic = heuristic; }

}
