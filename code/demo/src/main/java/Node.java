import java.util.HashSet;
import java.util.Set;

public class Node {
    final Game game;
    private Set<Node> children = new HashSet<>();
    private double prob = 1.0;
    
    public Node(Game game) {
        this.game = game;
    }

    public Node(Game game, double prob) {
        this.game = game;
        this.prob = prob;
    }

    public Game getGame() {
        return game;
    }

    public void addChild(Node child) {
        children.add(child);
    }   

    public void removeChild(Node child) {
        children.remove(child);
    }

    public Set<Node> getChildren() {
        return children;
    }

    public double getProb() {
        return prob;
    }

    public void setProb(double prob) {
        this.prob = prob;
    }
}

