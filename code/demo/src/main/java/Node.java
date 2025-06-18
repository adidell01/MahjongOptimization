import java.util.HashSet;
import java.util.Set;

public class Node {
    final Game game;
    private Set<Node> children = new HashSet<>();
    
    public Node(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void addChild(Node child) {
        children.add(child);
    }

    public Set<Node> getChildren() {
        return children;
    }
}

class NodeTuple {
    private Node node;
    private double score;

    public NodeTuple(Node node, double score) {
        this.node = node;
        this.score = score;
    }

    public Node getNode() {
        return node;
    }

    public double getScore() {
        return score;
    }
}
