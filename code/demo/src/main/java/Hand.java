import java.util.Arrays;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

/**
 * Simulates a players hand, containing 14 tiles on the players turn
 */
public class Hand {
    LinkedList<Tile> hand = new LinkedList<>();
    Random randomizer = new Random();
    int shanten = 8;
    Set<TileNode> triSet = new HashSet<TileNode>();
    Set<TileNode> seqSet = new HashSet<TileNode>();
    Set<Group> groups = new HashSet<Group>();
    Set<Group> pairs = new HashSet<Group>();

    boolean DEBUG = false;
    /**
     * Draw 14 tiles from the available pool of tiles.
     * @param tiles List of all currently drawable tiles.
     */
    public Hand(LinkedList<Tile> tiles){
        for(int i = 0; i < 14; i++){
            Tile curTile = tiles.remove(randomizer.nextInt(tiles.size()));
            TileNode curTriNode = new TileNode(curTile);
            TileNode curSeqNode = new TileNode(curTile);

            for(TileNode tileNode : triSet){
                if(tileNode.tile.compareTo(curTile) == 0){
                    curTriNode.neighbours.add(tileNode);
                    tileNode.neighbours.add(curTriNode);
                }
            }
            for(TileNode tileNode : seqSet){
                if(tileNode.tile.getType() == curTile.getType() && Math.abs(curTile.getVal() - tileNode.tile.getVal()) <= 2 && curTile.getVal() != tileNode.tile.getVal()){
                    curSeqNode.neighbours.add(tileNode);
                    tileNode.neighbours.add(curSeqNode);
                }
            }
            hand.add(curTile);
            triSet.add(curTriNode);
            seqSet.add(curSeqNode);
        }
        hand.sort(Comparator.naturalOrder());
        findGroups();
        if(DEBUG){
            System.out.println("GROUPS: " + groups);
        }
        setShanten();
        if(DEBUG){
            System.out.println("SHANTEN: " + shanten);
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
        return this.shanten;
    }

    private void setShanten(){
        if (pairs.isEmpty()){
            int shanten = 8;
            Set<Group> newSet = new HashSet<Group>();
            Set<Group> oldSet = new HashSet<Group>();
            for(Group group : this.groups)
                oldSet.add(group);

            for(Group baseGroup : this.groups){
                for(Group oldGroup : oldSet){
                    if(baseGroup.isDisjunct(oldGroup)){
                            Group group = new Group(baseGroup, oldGroup);
                            newSet.add(group);
                            shanten = Math.min(shanten, 10 - group.get().length);
                            if(DEBUG){
                                System.out.println("Candidate: " + shanten + ", Group: " + group);
                            }
                    }
                }
                oldSet.remove(baseGroup);
            }
            oldSet = newSet;
            newSet = new HashSet<Group>();
            if(DEBUG)
                System.out.println(oldSet);

            for(int i = 3; i <= 5; i++){
                for(Group baseGroup : this.groups){
                    for(Group oldGroup : oldSet){
                        if(baseGroup.isDisjunct(oldGroup)){
                            Group group = new Group(baseGroup, oldGroup);
                            newSet.add(group);
                            shanten = Math.min(shanten, 8 + i - group.get().length);
                            if(DEBUG){
                                System.out.println("Candidate: " + shanten + ", Group: " + group);
                            }
                        }
                    }
                }
                oldSet = newSet;
                newSet = new HashSet<Group>();
                if(DEBUG)
                    System.out.println(oldSet);
            }

            for(Group group : newSet){
                shanten = Math.min(13 - group.get().length, shanten);
                    if(DEBUG){
                        System.out.println("Candidate: " + shanten + ", Group: " + group);
                    }
            }
            this.shanten = Math.max(shanten, 1);

        } else {
            for (Group pair : pairs){
                int shanten = 7;
                Set<Group> newSet = new HashSet<Group>();
                Set<Group> oldSet = new HashSet<Group>();
                oldSet.add(pair);
                if(DEBUG)
                    System.out.println(oldSet);

                for(int i = 2; i <= 5; i++){
                    for(Group baseGroup : this.groups){
                        for(Group oldGroup : oldSet){
                            if(baseGroup.isDisjunct(oldGroup)){
                                Group group = new Group(baseGroup, oldGroup);
                                newSet.add(group);
                                shanten = Math.min(shanten, 8 + i - group.get().length);
                                if(DEBUG){
                                    System.out.println("Candidate: " + shanten + ", Group: " + group);
                                }
                            }
                        }
                    }
                    oldSet = newSet;
                    newSet = new HashSet<Group>();
                    if(DEBUG)
                        System.out.println(oldSet);
                }

                for(Group group : newSet){
                    shanten = Math.min(13 - group.get().length, shanten);
                    if(DEBUG){
                        System.out.println("Candidate: " + shanten + ", Group: " + group);
                    }
                }
                this.shanten = Math.min(shanten, this.shanten);
            }
        }
    }
    
    private void findGroups(){
        Set<TileNode> visited = new HashSet<TileNode>();
        
        for(TileNode tileNode : triSet){
            Set<TileNode> innerVisited = new HashSet<TileNode>();
            visited.add(tileNode);
            for(TileNode child : tileNode.neighbours){
                if(visited.contains(child) || innerVisited.contains(child))
                    continue;
                innerVisited.add(child);
                Group pair = new Group(tileNode.tile, child.tile);
                this.groups.add(pair);
                this.pairs.add(pair);
                for(TileNode subChild : child.neighbours){
                    if(visited.contains(subChild) || innerVisited.contains(subChild))
                        continue;
                    if(subChild.neighbours.contains(tileNode)){
                        Group group = new Group(tileNode.tile, child.tile, subChild.tile);
                        this.groups.add(group);
                        if(DEBUG) System.out.println("Identical group added: " + group);
                    }
                }
            }
        }
        visited.clear();

        for(TileNode tileNode : seqSet){
            Set<TileNode> innerVisited = new HashSet<TileNode>();
            visited.add(tileNode);
            for(TileNode child : tileNode.neighbours){
                if(visited.contains(child) || innerVisited.contains(child))
                    continue;
                innerVisited.add(child);
                this.groups.add(new Group(tileNode.tile, child.tile));
                for(TileNode subChild : child.neighbours){
                    if(visited.contains(subChild) || innerVisited.contains(subChild))
                        continue;
                    if(subChild.neighbours.contains(tileNode)){
                        Group group = new Group(tileNode.tile, child.tile, subChild.tile);
                        this.groups.add(group);
                        if(DEBUG) System.out.println("Sequential group added: " + group);
                    }
                }
            }
        }

        if(DEBUG){
            for(Group group : this.groups){
                System.out.println(group);
            }
        }
    }
}

class Group{
    private Tile[] tiles;

    public Group(Tile t1, Tile t2, Tile t3){
        tiles = new Tile[3];
        tiles[0] = t1;
        tiles[1] = t2;
        tiles[2] = t3;
    }

    public Group(Tile t1, Tile t2){
        tiles = new Tile[2];
        tiles[0] = t1;
        tiles[1] = t2;
    }

    public Group(Group group1, Group group2){
        tiles = new Tile[group1.tiles.length + group2.tiles.length];
        int counter = 0;

        for(Tile tile : group1.tiles){
            this.tiles[counter] = tile;
            counter++;
        }
        for(Tile tile : group2.tiles){
            this.tiles[counter] = tile;
            counter++;
        }
    }

    public boolean isDisjunct(Group other){
        for(Tile this_t : this.tiles)
            for(Tile other_t : other.tiles)
                if(this_t == other_t)
                    return false;
        return true;
    }

    public Tile[] get(){
        return tiles;
    }

    @Override
    public String toString(){
        return Arrays.toString(tiles);
    }
}

class TileNode{
    final Tile tile;
    Set<TileNode> neighbours = new HashSet<TileNode>();

    public TileNode(Tile tile){
        this.tile = tile;
    }
}
