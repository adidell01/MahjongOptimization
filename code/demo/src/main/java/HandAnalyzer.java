
import java.util.Set;

public class HandAnalyzer {
    private Game game;

    public HandAnalyzer(Game game) {
        this.game = game;
    }

    public Game getGame() {
        return this.game;
    }

    public Tile analyze() {
        Set<Tile> posDisc = this.game.getPlayer().getPosDisc();
        //select a random tile from the possible discards
        if (posDisc.isEmpty()) {
            System.out.println("No possible discards available.");
            return null;
        }
        int index = (int) (Math.random() * posDisc.size());
        return (Tile) posDisc.toArray()[index];
        
    }
}
