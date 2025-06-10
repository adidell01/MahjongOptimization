import java.util.LinkedList;
import java.util.Random;
import java.util.Set;

/**
 * Simulates a players hand, containing 14 tiles on the players turn
 */
public class Hand {
    LinkedList<Tile> hand = new LinkedList<>();
    Random randomizer = new Random();
    Set<Tile> GroupCandidates = new java.util.HashSet<>();
    Set<Tile> SequenceCandidates = new java.util.HashSet<>();
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
        int minShanten = 8; // Maximum shanten value for a standard hand is 8
        if (this.hand.size() != 14) {
            throw new IllegalArgumentException("Hand must contain exactly 14 tiles.");
        }

        LinkedList<Tile> cloneHandGroups = (LinkedList<Tile>) this.hand.clone(); // Create a copy of the hand to avoid modifying the original
        LinkedList<Tile> cloneHandSequences = (LinkedList<Tile>) this.hand.clone(); // Create a copy of the hand for sequences
        int triplets = countTriplets(cloneHandGroups);
        int pairs = countPairs(cloneHandGroups);
        int groups = triplets + pairs;

        int sequences3 = countSequences3(cloneHandSequences);
        int sequences2 = countSequences2(cloneHandSequences);

        System.out.println("Triplets: " + triplets);
        System.out.println("Pairs: " + pairs); 
        System.out.println("Group Candidates: " + this.GroupCandidates);
        System.out.println("Sequences3: " + sequences3);
        System.out.println("Sequences2: " + sequences2);
        System.out.println("Sequence Candidates: " + this.SequenceCandidates);
        /*
         * We need to make sure that the tiles are not counted multiple times.
         * And that the optimal distribution of triplets, pairs, and sequences is considered.
         */
        return minShanten;
    }

    /*
     * This method counts the number of triplets in the hand disregarding pairs and sequences.
     * A triplet consists of three tiles of the same type and value.
     */
    public int countTriplets(LinkedList<Tile> hand) {
        int triplets = 0;

        for (int i = 0; i < hand.size(); i++) {
            Tile tile1 = hand.get(i);
            for (int j = i + 1; j < hand.size(); j++) {
                Tile tile2 = hand.get(j);
                if (tile1.getType() == tile2.getType() && tile1.getVal() == tile2.getVal()) {
                    //found a pair
                    for (int k = j + 1; k < hand.size(); k++) {
                        Tile tile3 = hand.get(k);
                        if (tile1.getType() == tile3.getType() && tile1.getVal() == tile3.getVal()) {
                            // Found a triplet
                            triplets++;
                            this.GroupCandidates.add(tile1); // Add the triplet to candidates
                            this.GroupCandidates.add(tile2); // Add the triplet to candidates
                            this.GroupCandidates.add(tile3); // Add the triplet to candidates
                            hand.remove(k); // Remove the third tile to avoid counting it again
                            hand.remove(j); // Remove the second tile to avoid counting it again
                            hand.remove(i); // Remove the first tile to avoid counting it again
                            break; // Exit the loop since we found a triplet
                        }
                    }  
                }
            }
        }
        return triplets;
    }

    /*
     * This method counts the number of pairs in the hand disregarding sequences
     * and assuming all existing triplets have already been found.
     * A pair consists of two tiles of the same type and value.
     */
    public int countPairs(LinkedList<Tile> hand) {
        int pairs = 0;

        for (int i = 0; i < hand.size(); i++) {
            Tile tile1 = hand.get(i);
            for (int j = i + 1; j < hand.size(); j++) {
                Tile tile2 = hand.get(j);
                if (tile1.getType() == tile2.getType() && tile1.getVal() == tile2.getVal()) {
                    // Found a pair
                    pairs++;
                    this.GroupCandidates.add(tile1); // Add the pair to candidates
                    this.GroupCandidates.add(tile2); // Add the pair to candidates
                    hand.remove(j); // Remove the second tile to avoid counting it again
                    hand.remove(i); // Remove the first tile to avoid counting it again
                    break; // Exit the loop since we found a pair and there are no triplets left
                }
            }
        }
        return pairs;
    }

    public int countSequences3(LinkedList<Tile> hand) {
        int sequences = 0;

        for (int i = 0; i < hand.size(); i++) {
            Tile tile1 = hand.get(i);
            if (tile1.getType() == tileType.BAMBOO || tile1.getType() == tileType.CHARACTER || tile1.getType() == tileType.PIN) {
                for (int j = i + 1; j < hand.size(); j++) {
                    Tile tile2 = hand.get(j);
                    if (tile2.getType() == tile1.getType() && Math.abs(tile2.getVal() - tile1.getVal()) == 1) {
                        for (int k = j + 1; k < hand.size(); k++) {
                            Tile tile3 = hand.get(k);
                            if (tile3.getType() == tile1.getType() && Math.abs(tile3.getVal() - tile1.getVal()) == 2 && Math.abs(tile3.getVal() - tile2.getVal()) == 1 ||
                                tile3.getType() == tile1.getType() && Math.abs(tile3.getVal() - tile2.getVal()) == 2 && Math.abs(tile3.getVal() - tile1.getVal()) == 1) {
                                // Found a sequence
                                sequences++;
                                this.SequenceCandidates.add(tile1); // Add the sequence to candidates
                                this.SequenceCandidates.add(tile2); // Add the sequence to candidates
                                this.SequenceCandidates.add(tile3); // Add the sequence to candidates
                                hand.remove(k); // Remove the third tile to avoid counting it again
                                hand.remove(j); // Remove the second tile to avoid counting it again
                                hand.remove(i); // Remove the first tile to avoid counting it again
                                break; // Exit the loop since we found a sequence
                            }
                        }
                    }
                }
            }
        }

        return sequences;
    }

    public int countSequences2(LinkedList<Tile> hand) {
        int sequences = 0;

        for (int i = 0; i < hand.size(); i++) {
            Tile tile1 = hand.get(i);
            if (tile1.getType() == tileType.BAMBOO || tile1.getType() == tileType.CHARACTER || tile1.getType() == tileType.PIN) {
                for (int j = i + 1; j < hand.size(); j++) {
                    Tile tile2 = hand.get(j);
                    if (tile2.getType() == tile1.getType() && Math.abs(tile2.getVal() - tile1.getVal()) == 1) {
                        // Found a sequence of two tiles
                        sequences++;
                        this.SequenceCandidates.add(tile1); // Add the first tile to candidates
                        this.SequenceCandidates.add(tile2); // Add the second tile to candidates
                        hand.remove(j); // Remove the second tile to avoid counting it again
                        hand.remove(i); // Remove the first tile to avoid counting it again
                        break; // Exit the loop since we found a sequence of two tiles
                    }
                }
            }
        }

        return sequences;
    }

}
