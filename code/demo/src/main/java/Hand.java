import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.Set;

/**
 * Simulates a players hand as a {@link LinkedList} of 13 {@link Tile}s (14 on
 * the players turn).
 */
public class Hand {
    private LinkedList<Tile> hand = new LinkedList<>();
    private Random randomizer = new Random();
    private int shanten = 8;
    private Set<TileNode> triSet = new HashSet<>();
    private Set<TileNode> seqSet = new HashSet<>();
    private Set<Group> groups = new HashSet<>();
    private Set<Group> pairs = new HashSet<>();
    private Map<Tile, TileNode> triToNode = new HashMap<>();
    private Map<Tile, TileNode> seqToNode = new HashMap<>();
    private LinkedList<Tile> discards = new LinkedList<>();
    private Set<Tile> posDisc = new HashSet<>();

    private boolean DEBUG = false;

    /**
     * Draw 13 tiles from the available pool of tiles.
     * 
     * @param tiles List of all currently drawable tiles.
     * @throws IllegalArgumentException If there are less than 13 tiles to draw
     *                                  from.
     */
    public Hand(LinkedList<Tile> tiles) {
        for (int i = 0; i < 13; i++) {
            softDraw(tiles.remove(randomizer.nextInt(tiles.size())));
        }
        hand.sort(Comparator.naturalOrder());
    }

    private Hand() {

    }

    /**
     * This method returns an integer value that indicates the least necessary
     * amount
     * of tiles needed to be one step away from completing the hand and winning.
     * 
     * @return
     *         Least amount of tiles necessary to reach a ready hand (Tenpai).
     */
    public int getShanten() {
        return this.shanten;
    }

    /**
     * Randomly discards a tile from the given hand.
     */
    public Tile discard() {
        return discard(randomizer.nextInt(hand.size()));
    }

    /**
     * Discards the tile at the index given in discard.
     * 
     * @param discard The index of the to-be-discarded tile. Must be a positive
     *                integer smaller than the hand size or 0.
     * @throws IndexOutOfBoundsException If discard is negative or greater than the
     *                                   hand size.
     */
    public Tile discard(int discard) {
        Tile discardTile = hand.remove(discard);
        this.discards.add(discardTile);

        TileNode discardTileNode = this.triToNode.get(discardTile);
        this.triSet.remove(discardTileNode);
        for (TileNode curNode : discardTileNode.neighbours) {
            curNode.neighbours.remove(discardTileNode);
        }

        discardTileNode = this.seqToNode.get(discardTile);
        this.seqSet.remove(discardTileNode);
        for (TileNode curNode : discardTileNode.neighbours) {
            curNode.neighbours.remove(discardTileNode);
        }

        return discardTile;
    }

    public Tile simDiscard(int discard) {
        Tile discardTile = hand.remove(discard);

        TileNode discardTileNode = this.triToNode.get(discardTile);
        this.triSet.remove(discardTileNode);
        for (TileNode curNode : discardTileNode.neighbours) {
            curNode.neighbours.remove(discardTileNode);
        }

        discardTileNode = this.seqToNode.get(discardTile);
        this.seqSet.remove(discardTileNode);
        for (TileNode curNode : discardTileNode.neighbours) {
            curNode.neighbours.remove(discardTileNode);
        }

        return discardTile;
    }

    public void simDiscard(Set<Tile> discards){
        for(Tile tile : discards){
            simDiscard(this.hand.indexOf(tile));
        }
    }

    public void simDraw(Set<Tile> draws){
        for(Tile tile : draws){
            softDraw(tile);
        }
        findGroups();
        setShanten();
    }

    /**
     *                                  Simulates drawing the specified Tile, adding it to the hand.
     * @param tile                      The to-be-drawn tile.
     * @throws NullPointerException     If tile is null.
     */
    public void draw(Tile tile) {
        if (tile == null)
            throw new NullPointerException("Cannot draw Tile \"null\"");
        hand.sort(Comparator.naturalOrder());
        softDraw(tile);
        findGroups();
        if (DEBUG) {
            System.out.println("GROUPS: " + groups);
        }
        setShanten();
        if (DEBUG) {
            System.out.println("SHANTEN: " + shanten);
        }
    }

    /**
     * Getter method for the hand
     * 
     * @return Returns the hand as a {@link LinkedList} of type {@link Tile}
     */
    public LinkedList<Tile> getHand() {
        return hand;
    }

    /**
     * Prints the current discards of this hand in order of time with the first
     * element being the first discard.
     */
    public void getDiscards() {
        System.out.println(this.discards);
    }

    public LinkedList<Tile> getDiscardsList() {
        return this.discards;
    }

    public Hand copyOf() {
        Hand copy = new Hand();
        for (int i = 0; i < this.hand.size(); i++) {
            Tile next = this.hand.get(i).copyOf();
            if (i < 13) {
                copy.softDraw(next);
            } else {
                copy.draw(next);
            }
        }
        if (this.hand.size() < 14) {
            copy.hand.sort(Comparator.naturalOrder());
        }
        return copy;
    }

    public Set<Tile> getPosDisc(){
        return this.posDisc;
    }

    private void softDraw(Tile curTile){
        TileNode curTriNode = new TileNode(curTile);
        TileNode curSeqNode = new TileNode(curTile);
        this.triToNode.put(curTile, curTriNode);
        this.seqToNode.put(curTile, curSeqNode);

        for (TileNode tileNode : this.triSet) {
            if (tileNode.tile.compareTo(curTile) == 0) {
                curTriNode.neighbours.add(tileNode);
                tileNode.neighbours.add(curTriNode);
            }
        }
        for (TileNode tileNode : this.seqSet) {
            if (tileNode.tile.getType() == curTile.getType() && Math.abs(curTile.getVal() - tileNode.tile.getVal()) <= 2
                    && curTile.getVal() != tileNode.tile.getVal()) {
                curSeqNode.neighbours.add(tileNode);
                tileNode.neighbours.add(curSeqNode);
            }
        }
        hand.add(curTile);
        triSet.add(curTriNode);
        seqSet.add(curSeqNode);
    }

    private void setShanten() {
        this.shanten = 99;
        this.posDisc.clear();
        this.posDisc.addAll(this.hand);
        if (pairs.isEmpty()){
            int shanten = 8;
            int biggestGroupSize = 0;
            Set<Group> newSet = new HashSet<Group>();
            Set<Group> oldSet = new HashSet<Group>();
            Set<Group> consider = oldSet;
            for(Group group : this.groups)
                oldSet.add(group);

            for(Group baseGroup : this.groups){
                for(Group oldGroup : oldSet){
                    if(baseGroup.isDisjunct(oldGroup)){
                            Group group = new Group(baseGroup, oldGroup);
                            newSet.add(group);
                            shanten = Math.min(shanten, 10 - group.get().length);
                            biggestGroupSize = Math.max(biggestGroupSize, group.get().length);
                            if(DEBUG){
                                System.out.println("Candidate: " + shanten + ", Group: " + group);
                            }
                    }
                }
                oldSet.remove(baseGroup);
            }
            if(!newSet.isEmpty()){
                consider = newSet;
            }
            oldSet = newSet;
            newSet = new HashSet<Group>();
            if (DEBUG)
                System.out.println(oldSet);

            for (int i = 3; i <= 5; i++) {
                for (Group baseGroup : this.groups) {
                    for (Group oldGroup : oldSet) {
                        if (baseGroup.isDisjunct(oldGroup)) {
                            Group group = new Group(baseGroup, oldGroup);
                            newSet.add(group);
                            shanten = Math.min(shanten, 8 + i - group.get().length);
                            biggestGroupSize = Math.max(biggestGroupSize, group.get().length);
                            if(DEBUG){
                                System.out.println("Candidate: " + shanten + ", Group: " + group);
                            }
                        }
                    }
                }
                if(!newSet.isEmpty()){
                    consider = newSet;
                }
                oldSet = newSet;
                newSet = new HashSet<Group>();
                if (DEBUG)
                    System.out.println(oldSet);
            }

            for (Group group : newSet) {
                shanten = Math.min(13 - group.get().length, shanten);
                biggestGroupSize = Math.max(biggestGroupSize, group.get().length);
                    if(DEBUG){
                        System.out.println("Candidate: " + shanten + ", Group: " + group);
                    }
            }
            this.shanten = Math.max(shanten, 1);

            Set<Tile> essential = new HashSet<>();
            essential.addAll(this.posDisc);
            for(Group group : consider){
                if(group.get().length == biggestGroupSize){
                    //System.out.println(essential);
                    //System.out.println(Arrays.asList(group.get()));
                    essential.retainAll(Arrays.asList(group.get()));
                    //System.out.println(essential);
                }
            }
            this.posDisc.removeAll(essential);

        } else {
            int biggestGroupSize = 0;
            Set<Group> consider = new HashSet<>();
            for (Group pair : pairs) {
                int shanten = 7;
                Set<Group> newSet = new HashSet<Group>();
                Set<Group> oldSet = new HashSet<Group>();
                Set<Group> considersub = oldSet;
                oldSet.add(pair);
                if (DEBUG)
                    System.out.println(oldSet);
                for (int i = 2; i <= 5; i++) {
                    for (Group baseGroup : this.groups) {
                        for (Group oldGroup : oldSet) {
                            if (baseGroup.isDisjunct(oldGroup)) {
                                Group group = new Group(baseGroup, oldGroup);
                                newSet.add(group);
                                shanten = Math.min(shanten, 8 + i - group.get().length);
                                biggestGroupSize = Math.max(biggestGroupSize, group.get().length);
                                if(DEBUG){
                                    System.out.println("Candidate: " + shanten + ", Group: " + group);
                                }
                            }
                        }
                    }
                    if(!newSet.isEmpty()){
                        considersub = newSet;
                    }
                    oldSet = newSet;
                    newSet = new HashSet<Group>();
                    if (DEBUG)
                        System.out.println(oldSet);
                }

                for (Group group : newSet) {
                    shanten = Math.min(13 - group.get().length, shanten);
                    biggestGroupSize = Math.max(biggestGroupSize, group.get().length);
                    if(DEBUG){
                        System.out.println("Candidate: " + shanten + ", Group: " + group);
                    }
                }
                this.shanten = Math.min(shanten, this.shanten);

                consider.addAll(considersub);
            }

            Set<Tile> essential = new HashSet<>();
            essential.addAll(this.posDisc);
            for(Group group : consider){
                if(group.get().length == biggestGroupSize){
                    //System.out.println(essential);
                    //System.out.println(Arrays.asList(group.get()));
                    essential.retainAll(Arrays.asList(group.get()));
                    //System.out.println(essential);
                }
            }
            //System.out.println(this.posDisc);
            this.posDisc.removeAll(essential);
            //System.out.println(this.posDisc);
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        }
        //System.out.println("Possible Discards: " + this.posDisc);
    }
    
    private void findGroups(){
        groups.clear();
        pairs.clear();
        Set<TileNode> visited = new HashSet<TileNode>();

        for (TileNode tileNode : triSet) {
            Set<TileNode> innerVisited = new HashSet<TileNode>();
            visited.add(tileNode);
            for (TileNode child : tileNode.neighbours) {
                if (visited.contains(child) || innerVisited.contains(child))
                    continue;
                innerVisited.add(child);
                Group pair = new Group(tileNode.tile, child.tile);
                this.groups.add(pair);
                this.pairs.add(pair);
                for (TileNode subChild : child.neighbours) {
                    if (visited.contains(subChild) || innerVisited.contains(subChild))
                        continue;
                    if (subChild.neighbours.contains(tileNode)) {
                        Group group = new Group(tileNode.tile, child.tile, subChild.tile);
                        this.groups.add(group);
                        if (DEBUG)
                            System.out.println("Identical group added: " + group);
                    }
                }
            }
        }
        visited.clear();

        for (TileNode tileNode : seqSet) {
            Set<TileNode> innerVisited = new HashSet<TileNode>();
            visited.add(tileNode);
            for (TileNode child : tileNode.neighbours) {
                if (visited.contains(child) || innerVisited.contains(child))
                    continue;
                innerVisited.add(child);
                this.groups.add(new Group(tileNode.tile, child.tile));
                for (TileNode subChild : child.neighbours) {
                    if (visited.contains(subChild) || innerVisited.contains(subChild))
                        continue;
                    if (subChild.neighbours.contains(tileNode)) {
                        Group group = new Group(tileNode.tile, child.tile, subChild.tile);
                        this.groups.add(group);
                        if (DEBUG)
                            System.out.println("Sequential group added: " + group);
                    }
                }
            }
        }

        if (DEBUG) {
            for (Group group : this.groups) {
                System.out.println(group);
            }
        }
    }
}

class Group {
    private Tile[] tiles;

    public Group(Tile t1, Tile t2, Tile t3) {
        tiles = new Tile[3];
        tiles[0] = t1;
        tiles[1] = t2;
        tiles[2] = t3;
    }

    public Group(Tile t1, Tile t2) {
        tiles = new Tile[2];
        tiles[0] = t1;
        tiles[1] = t2;
    }

    public Group(Group group1, Group group2) {
        tiles = new Tile[group1.tiles.length + group2.tiles.length];
        int counter = 0;

        for (Tile tile : group1.tiles) {
            this.tiles[counter] = tile;
            counter++;
        }
        for (Tile tile : group2.tiles) {
            this.tiles[counter] = tile;
            counter++;
        }
    }

    private Group(Group group) {
        Tile[] copy = group.tiles;
        this.tiles = new Tile[copy.length];
        for (int i = 0; i < copy.length; i++) {
            this.tiles[i] = copy[i].copyOf();
        }
    }

    public boolean isDisjunct(Group other) {
        for (Tile this_t : this.tiles)
            for (Tile other_t : other.tiles)
                if (this_t == other_t)
                    return false;
        return true;
    }

    public Tile[] get() {
        return tiles;
    }

    public boolean contains(Tile tile) {
        for (Tile curTile : tiles)
            if (curTile == tile)
                return true;
        return false;
    }

    @Override
    public String toString() {
        return Arrays.toString(tiles);
    }

    public Group copyOf() {
        return new Group(this);
    }
}

class TileNode {
    final Tile tile;
    Set<TileNode> neighbours = new HashSet<TileNode>();

    public TileNode(Tile tile) {
        this.tile = tile;
    }

    private TileNode(TileNode tileNode) {
        this.tile = tileNode.tile.copyOf();

    }

    public TileNode copyOf() {
        return new TileNode(this);
    }
}
