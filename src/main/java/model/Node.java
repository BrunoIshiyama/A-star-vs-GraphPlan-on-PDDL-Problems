package model;

import fr.uga.pddl4j.util.BitExp;
import fr.uga.pddl4j.util.BitState;

public class Node extends BitState {

    // g(n)
    private int cost;
    // h(n)
    private int heuristic;

    public Node(BitExp init) {
    }

    // f(n) = g(n) + h(n)
    public int getTotalCost() {
        return cost + heuristic;
    }

    public void setCost(int cost) {
        this.cost = cost;
    }

    public void setHeuristic(int heuristic) {
        this.heuristic = heuristic;
    }

}
