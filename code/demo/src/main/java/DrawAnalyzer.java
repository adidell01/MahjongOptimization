import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class DrawAnalyzer {
    DrawNode root;
    Game game;

    public DrawAnalyzer(Game game){
        this.game = game;
        root = new DrawNode(game);
    }

    public void generateGraph(int depth){
        /*
        Set<DrawNode> curSet = new HashSet<>();
        curSet.add(this.root);
        while(depth > 0){
            Iterator<DrawNode> curIt = curSet.iterator();
            Set<DrawNode> newSet = new HashSet<>();
            while(curIt.hasNext()){
                DrawNode curNode = curIt.next();
                for(int i = 0; i < 34; i++){
                    DrawNode nextNode = new DrawNode(curNode, Tile.tileAt(i));
                    newSet.add(nextNode);
                }
            }
        } */
        if(depth < 1)
            throw new IllegalArgumentException("Depth must be above 0");
        Set<DrawNode> set = new HashSet<>();
        for(int i = 0; i < 34; i++){
            DrawNode cur = new DrawNode(game, Tile.tileAt(i));
            root.add(cur);
            set.add(cur);
        }
        while(depth > 1){
            set = nextSet(set);
            depth--;
        }
        System.out.println(set.size());
    }

    private Set<DrawNode> nextSet(Set<DrawNode> set){
        System.out.println(set.size());
        Set<DrawNode> res = new HashSet<>();
        for(int i = 0; i < 34; i++){
            Tile tile = new Tile(i);
            Iterator<DrawNode> it = set.iterator();
            LinkedList<DrawNode> remove = new LinkedList<>();
            while(it.hasNext()){
                DrawNode cur = it.next();
                Set<Tile> subset = cur.getTiles();
                DrawNode toAdd = new DrawNode(cur, tile);
                Iterator<Tile> subit = subset.iterator();
                while(subit.hasNext()){
                    Tile subtile = subit.next();
                    if(subtile.compareTo(tile) == 0){
                        remove.add(cur);
                    }
                }
                cur.add(toAdd);
                Iterator<DrawNode> resit = res.iterator();
                while(resit.hasNext()){
                    DrawNode resNode = resit.next();
                    if(resNode.contains(cur)){
                        cur.add(resNode);
                    }
                }
                res.add(toAdd);
            }
            for(DrawNode cur : remove){
                set.remove(cur);
            }
        }
        return res;
    }

}

class DrawNode{
    private Set<Tile> tiles = new HashSet<>();
    private LinkedList<DrawNode> nbrs = new LinkedList<>();
    private int[] unkowns;

    public DrawNode(Game game){
        this.unkowns = game.getUnkowns();
    }

    public DrawNode(Game game, Tile tile){
        this.unkowns = game.getUnkowns();
        this.tiles.add(tile);
    }

    public DrawNode (DrawNode drawNode, Tile tile){
        this.unkowns = drawNode.unkowns;
        Iterator<Tile> it = drawNode.getTiles().iterator();
        while(it.hasNext()){
            this.tiles.add(it.next());
        }
        this.tiles.add(tile);
    }

    public void add(DrawNode node){
        this.nbrs.add(node);
    }

    public Set<Tile> getTiles(){
        return this.tiles;
    }

    public int getCombinaitons(){
        int res = 1;
        int[] amounts = unkowns.clone();
        for(Tile tile : this.tiles){
            res *= amounts[tile.orderPos()];
            amounts[tile.orderPos()]--;
        }
        return res;
    }

    public boolean contains(DrawNode node){
        //if this.tiles contains node.tiles, return true. otherwise false.
        //ex. [CHARACTER 1, CHARACTER 1, RED] contains [CHARACTER 1, CHARACTER 1], but not [RED, RED]
        //note: Tiles between this.tiles and node.tiles may be distinct and posess the same value. In this case the result should still be true.

        Set<Tile> myCopy = new HashSet<>(this.tiles);

        boolean iscontaining = true;
        Iterator<Tile> iterator = node.getTiles().iterator();
        while(iterator.hasNext()){
            Tile tile = iterator.next();
            
            boolean hasCurrentTile = false;
            Iterator<Tile> CopyIt = myCopy.iterator();
            while(CopyIt.hasNext()){
                Tile thisTile = CopyIt.next();
                if (thisTile.getVal() == tile.getVal() && thisTile.getType() == tile.getType()) {
                    hasCurrentTile = true;
                    CopyIt.remove(); // Remove the tile to ensure we don't count it again
                    break; // Found a matching tile, no need to check further
                } else {
                    hasCurrentTile = false;
                }
            }
            iscontaining = iscontaining && hasCurrentTile;
        }
        return iscontaining;
    }
}
