
import java.util.LinkedList;

/**
 * Simulates a game
 */
public class Game {
    Hand player;
    LinkedList<Tile> tiles = new LinkedList<>();
    /**
     * Generates a game setup with a pool of tiles and a player.
     */
    public Game(){
        //Generate tiles for each type
        for(int type = 0; type < 10; type++){
            //Case for numbered Tiles (Bamboo, Character and Pin)
            if(type < 3){
                //Generate tiles for each value of given type
                for(int val = 1; val <= 9; val++){
                    //Generate 4 copies of given tile
                    for(int i = 0; i < 4; i++){
                        tiles.add(new Tile(type, val));
                    }
                }
            //Case for Winds (North, South, East and West) and Dragons (Red, Green and White)
            } else {
                //Generate 4 copies of given tile
                for(int i = 0; i < 4; i++){
                    tiles.add(new Tile(type, 1));
                }
            }
        }
        //Generate a random hand simulating the main player (other players are currently ignored)
        this.player = new Hand(tiles);
    }
}
