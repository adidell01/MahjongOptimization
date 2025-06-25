import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Set;

public class DrawAnalyzer {
    DrawNode root;
    Set<DrawNode> layer;
    int layerDepth = 0;
    Game game;

    public DrawAnalyzer(Game game) {
        this.game = game;
        root = new DrawNode(game);
    }

    public int getBestDiscard(){
        LinkedList<DrawNode> nodes = new LinkedList<>();
        LinkedList<Integer> amounts = new LinkedList<>();
        int total = 0;
        int[] discardValues = new int[14];
        Iterator<DrawNode> it = layer.iterator();
        while(it.hasNext()){
            DrawNode cur = it.next();
            int combinations = cur.getCombinaitons();
            nodes.add(cur);
            amounts.add(combinations);
            total += combinations;
        }
        Hand copy = game.getPlayer().copyOf();
        LinkedList<Tile> originalHand = new LinkedList<>();
        originalHand.addAll(copy.getHand());
        for(int i = 0; i < nodes.size(); i++){
            copy.simDraw(nodes.get(i).getTiles());
            for(Tile tile : copy.getPosDisc()){
                if(originalHand.contains(tile)){
                    discardValues[originalHand.indexOf(tile)] += amounts.get(i);
                }
            }
            copy.simDiscard(nodes.get(i).getTiles());
        }
        int res = 0;
        for(int i = 0; i < 14; i++){
            if(discardValues[res] < discardValues[i])
            res = i;
        }
        return res;
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
        this.layerDepth = depth;
        if(depth < 1)
            throw new IllegalArgumentException("Depth must be above 0");
        layer = new HashSet<>();
        for(int i = 0; i < 34; i++){
            DrawNode cur = new DrawNode(game, Tile.tileAt(i));
            root.add(cur);
            layer.add(cur);
        }
        while(depth > 1){
            layer = nextSet(layer);
            depth--;
        }
        System.out.println(layer.size());
    }

    private Set<DrawNode> nextSet(Set<DrawNode> set) {
        // System.out.println(set.size());
        Set<DrawNode> res = new HashSet<>();
        Set<DrawNode> backup = new HashSet<>();
        backup.addAll(set);
        for (int i = 0; i < 34; i++) {
            Tile tile = new Tile(i);
            Iterator<DrawNode> it = set.iterator();
            LinkedList<DrawNode> remove = new LinkedList<>();
            while (it.hasNext()) {
                DrawNode cur = it.next();
                Set<Tile> subset = cur.getTiles();
                DrawNode toAdd = new DrawNode(cur, tile);
                Iterator<Tile> subit = subset.iterator();
                while (subit.hasNext()) {
                    Tile subtile = subit.next();
                    if (subtile.compareTo(tile) == 0) {
                        remove.add(cur);
                    }
                }
                cur.add(toAdd);
                Iterator<DrawNode> backit = backup.iterator();
                while (backit.hasNext()) {
                    DrawNode resNode = backit.next();
                    if (toAdd.contains(resNode)) {
                        resNode.add(toAdd);
                        // System.out.println("Connected " + resNode + " to " + toAdd);
                    }
                }
                // System.out.println("Node: " + toAdd);
                res.add(toAdd);
            }
            for (DrawNode cur : remove) {
                set.remove(cur);
            }
        }
        return res;
    }

}

class DrawNode {
    private Set<Tile> tiles = new HashSet<>();
    LinkedList<DrawNode> nbrs = new LinkedList<>();
    private int[] unkowns;

    public DrawNode(Game game) {
        this.unkowns = game.getUnkowns();
    }

    public DrawNode(Game game, Tile tile) {
        this.unkowns = game.getUnkowns();
        this.tiles.add(tile);
    }

    public DrawNode(DrawNode drawNode, Tile tile) {
        this.unkowns = drawNode.unkowns;
        Iterator<Tile> it = drawNode.getTiles().iterator();
        while (it.hasNext()) {
            this.tiles.add(it.next());
        }
        this.tiles.add(tile);
    }

    public void add(DrawNode node) {
        this.nbrs.add(node);
    }

    public Set<Tile> getTiles() {
        return this.tiles;
    }

    public int getCombinaitons() {
        int res = 1;
        int[] amounts = unkowns.clone();
        for (Tile tile : this.tiles) {
            res *= amounts[tile.orderPos()];
            amounts[tile.orderPos()]--;
        }
        return res;
    }

    @Override
    public String toString() {
        return this.tiles.toString();
    }

    public boolean contains(DrawNode node) {
        // if this.tiles contains node.tiles, return true. otherwise false.
        // ex. [CHARACTER 1, CHARACTER 1, RED] contains [CHARACTER 1, CHARACTER 1], but
        // not [RED, RED]
        // note: Tiles between this.tiles and node.tiles may be distinct and posess the
        // same value. In this case the result should still be true.

        Set<Tile> myCopy = new HashSet<>(this.tiles);

        boolean iscontaining = true;
        Iterator<Tile> iterator = node.getTiles().iterator();
        while (iterator.hasNext()) {
            Tile tile = iterator.next();

            boolean hasCurrentTile = false;
            Iterator<Tile> CopyIt = myCopy.iterator();
            while (CopyIt.hasNext()) {
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
