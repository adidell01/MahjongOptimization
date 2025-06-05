import java.util.LinkedList;
import java.util.Random;

/**
 * Simulates a players hand, containing 14 tiles on the players turn
 */
public class Hand {
    LinkedList<Tile> hand = new LinkedList<>();
    Random randomizer = new Random();

    /**
     * Draw 14 tiles from the available pool of tiles.
     * @param tiles List of all currently drawable tiles.
     */
    public Hand(LinkedList<Tile> tiles){
        for(int i = 0; i < 14; i++){
            hand.add(tiles.remove(randomizer.nextInt(tiles.size())));
        }
    }

    /**
     * This method returns an integer value that indicates the least necessary amount
     * of tiles needed to be one step away from completing the hand and winning.
     * 
     * @return  
     * Least amount of tiles necessary to reach a ready hand (Tenpai).
     */
    public int getShanten(){
        /*  TODO:   this method should return an integer value that indicates the least necessary amount
         *          of tiles needed to complete the hand minus one. To achieve a win, a hand requires 4
         *          groups of triplets or sequences (color always needs to be the same across a group)
         *          and a pair. If necessary, add methods and fields to this class.
         */
        return 0;
    }
}
