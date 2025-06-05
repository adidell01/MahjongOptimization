
import java.util.LinkedList;

public class Game {
    Hand player;
    LinkedList<Tile> tiles;

    public Game(){
        for(int type = 0; type < 10; type++){
            if(type < 3){
                for(int val = 1; val <= 9; val++){
                    for(int i = 0; i < 4; i++){
                        tiles.add(new Tile(type, val));
                    }
                }
            } else {
                for(int i = 0; i < 4; i++){
                    tiles.add(new Tile(type, 1));
                }
            }
        }

    }
}
