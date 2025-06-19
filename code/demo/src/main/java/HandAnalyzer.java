
import java.util.LinkedList;

public class HandAnalyzer {
    private Node rootNode;

    public HandAnalyzer(Node rootNode) {
        this.rootNode = rootNode;
    }

    /*
     * generate a graph of all possible hands for the given depth
     */
    public void generateGraph(int depth) {
        LinkedList<Node> queue = new LinkedList<>();
        queue.add(rootNode);

        if (depth <= 0) {
            return; // Do nothing if depth is zero or negative
        }

        LinkedList<Node> nextQueue = new LinkedList<>();
        LinkedList<Node> nodesNextDepth = new LinkedList<>();
        int counter = 0;
        while (counter < depth) {
        
            for (Node currentNode : queue) {
                if (counter == depth) {
                    break; // Stop processing if we reached the desired depth
                }
                
                LinkedList<Tile> myList = currentNode.game.getTiles();
                /* calculate the probability of a draw for every tile in myList
                 * then generate a copy of the current game for every tile in myList
                 * and add it as a child to the current node, with the probability as weight
                 */
                for (Tile tile : myList) {
                    for (int j = 0; j < 14; j++) {
                        Game gameCopy = currentNode.getGame().copyOf();
                        gameCopy.getTiles().remove(tile);
                        gameCopy.getPlayer().discard(j);
                        gameCopy.getPlayer().draw(tile);
                        Node childNode = new Node(gameCopy, 1.0 / (myList.size()*14)); // Assuming equal probability for simplicity
                        currentNode.addChild(childNode);
                    }
                }

                // Generate all possible next states from the current node
                for (Node child : currentNode.getChildren()) {
                    /* make sure that no node that generates the same hand exists on this level of depth */
                    for (Node existingChild : nodesNextDepth) {
                        if (existingChild.getGame().getPlayer().getHand().toString().equals(child.getGame().getPlayer().getHand().toString())) { //child's hand already exists
                            currentNode.removeChild(child);
                            currentNode.addChild(existingChild); // replace child with existing child
                            existingChild.setProb(existingChild.getProb() + child.getProb()); // increase the probability of the existing child
                            break; // no need to check further, we found a match
                        }
                    }
                    nextQueue.add(child);
                    nodesNextDepth.add(child);
                }
            }
            queue = nextQueue;
            counter++;
        }
        return; // the Graph has been generated, now it can be used for further analysis
    }


   
}
