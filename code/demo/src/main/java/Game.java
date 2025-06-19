
import java.util.LinkedList;
import java.util.Random;

/**
 * Simulates a game containing a main player of type {@link Hand} and 3 hidden dummy players.
 */
public class Game {
    private Hand player;
    private LinkedList<Tile> tiles = new LinkedList<>();
    private Hand[] dummies = new Hand[3];
    private int currentPlayer;
    private LinkedList<Tile> deadwall = new LinkedList<>();
    private Random randomizer = new Random();

    /**
     * Generates a game setup with a pool of tiles and a player.
     * 
     * @param firstToStart  Takes an integer argument of the interval [0, 3] of natural numbers 
     *                      to simulate which player starts first, with 3 being the main player
     *                      and 0 being the next going counterclockwise. If the argument is
     *                      outside of the interval an {@link IllegalArgumentException} will be
     *                      thrown.
     */
    public Game(int firstToStart){
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
        //Generate the deadwall
        for(int i = 0; i < 14; i++){
            this.deadwall.add(this.tiles.remove(new Random().nextInt(tiles.size())));
        }
        //Generate a random hand simulating the main player (other players are currently ignored)
        this.player = new Hand(tiles);
        //Generate dummy players (only present for consistency; can be safely ignored)
        for(int i = 0; i < 3; i++)
            this.dummies[i] = new Hand(tiles);
        //Sets the current player
        this.currentPlayer = firstToStart;
        //progress the game to the players turn
        nextPlayerTurn();
    }

    private Game(){
        
    }

    /**
     *          Getter method for the main player object.
     * @return  The main player as an Object of type {@link Hand}.
     */
    public Hand getPlayer(){
        return this.player;
    }

    /**
     *                                      Discards the tile of the main players hand at the index given in tile. Returns false once there are
     *                                      no tiles left to draw. Otherwise simulates dummy players drawing and discarding a tile until the
     *                                      main player draws a tile again, then returns true.
     * @param tile                          The index of the to-be-discarded tile. Must be a positive integer smaller than the hand size or 0.
     * @return                              True if the discard was successful and it's the main player's turn again. False if at some players
     *                                      turn (be it main player or dummy) before being able to draw, there are no more tiles left to draw.
     * @throws IndexOutOfBoundsException    If discard is negative or greater than the main players hand size.
     */
    public boolean discard(int tile){
        this.player.discard(tile);
        this.currentPlayer = 0;
        return nextPlayerTurn();
    }

    /**
     * Prints the discards of all players in the console with the first discard pile belonging to the main player and then going around counterclockwise.
     */
    public void getDiscards(){
        this.player.getDiscards();
        for(Hand dummy : this.dummies)
            dummy.getDiscards();
    }

    public LinkedList<Tile> getTiles(){
        return this.tiles;
    }

    public Game copyOf(){
        Game copy = new Game();
        copy.player = this.player.copyOf();
        for(Tile tile : this.tiles){
            copy.tiles.add(tile.copyOf());
        }
        for(int i = 0; i < 3; i++){
            copy.dummies[i] = this.dummies[i].copyOf();
        }
        copy.currentPlayer = this.currentPlayer;
        for(Tile tile : this.deadwall){
            copy.deadwall.add(tile.copyOf());
        }
        return copy;
    }

    private boolean nextPlayerTurn(){
        if(this.tiles.isEmpty())
            return false;
        currentPlayerDraw();
        while(this.currentPlayer < 3){
            currentPlayerDiscard();
            this.currentPlayer++;
            if(this.tiles.isEmpty())
                return false;
            currentPlayerDraw();
        }
        System.out.println("Discarded Tiles:");
        getDiscards();
        System.out.println("Remaning Amount: " + tiles.size() + "\n");
        return true;
    }

    private void currentPlayerDraw(){
        if(this.currentPlayer == 3)
            this.player.draw(tiles.remove(randomizer.nextInt(tiles.size())));
        else
            this.dummies[this.currentPlayer].draw(tiles.remove(randomizer.nextInt(tiles.size())));
    }

    private void currentPlayerDiscard(){
        if(this.currentPlayer == 3)
            this.player.discard();
        else
            this.dummies[this.currentPlayer].discard();
    }

}
