package domain;

import java.util.Comparator;

public class NodeComparator implements Comparator<Node> {

    public NodeComparator() {
    }

    @Override
    public int compare(Node node1, Node node2) {
        return Double.compare(node1.getTotalCost(), node2.getTotalCost());
    }
}
