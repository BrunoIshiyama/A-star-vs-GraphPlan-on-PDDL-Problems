package model;

import fr.uga.pddl4j.encoding.CodedProblem;

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

        Node initial = new Node(codedProblem.getInit());
    }

    /**
     * Responsible for finding all operators that can be applied to a specific state
     *
     * @return List of operators
     */
    public List<String> findApplicableOperators(Node node) {
        return new ArrayList<String>();
    }

    /**
     * Responsible for finding the successor state after applying an operator
     *
     * @param operator - action taken
     * @param node - current node (state)
     * @return successor node(state)
     */
    public Node findSuccessorState (String operator, Node node) {
        return node;
    }

}
