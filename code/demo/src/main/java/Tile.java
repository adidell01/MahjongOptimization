/**
 * Simulates a tile with a {@link tileType}, value and uid.
 */
public class Tile implements Comparable<Tile> {
    private tileType type;
    private int val;
    private int uid;
    private static int uidGenerator = 0;
    private static final Tile[] staticTiles = initializeStaticTiles();

    /**
     * Generates a new unique tile given a type and value.
     * 
     * @param type The type of tile as an integer value. For the exact type
     *             conversion, see also {@link Tile#valueOf(tileType)
     *             Tile.valueOf(...)}.
     * @param val  The value of the tile (as integer between and including 1 and 9
     *             for bamboo/character/pin and only 1 otherwise)
     * @throws IllegalArgumentException If !(0 <= type <= 9), (0 <= type <= 2 && !(0
     *                                  <= val <= 9)) or (3 <= type <= 9 && val !=
     *                                  1)
     */
    public Tile(int type, int val) {
        try {
            this.type = Tile.valueOf(type);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("INVALID TYPE FOR TILE CREATION: " + type);
        }

        if ((type < 3 && (val < 1 || val > 9)) || (type >= 3 && val != 1))
            throw new IllegalArgumentException("INVALID VALUE FOR TILE: " + val + ", TYPE: " + this.type.name());
        this.val = val;
        this.uid = this.uidGenerator;
        this.uidGenerator++;
    }

    public Tile(int index) {
        if (0 <= index && index < 27) {
            this.type = Tile.valueOf(index / 9);
            this.val = (index % 9) + 1;
        } else if (27 <= index && index < 34) {
            this.type = Tile.valueOf(index - 24);
            this.val = 1;
        } else {
            throw new IllegalArgumentException("Tile index is " + index + "\nShould be between 0 and 33");
        }
    }

    /**
     * Returns the value of the tile.
     * 
     * @return An integer value between 1 and 9 if the type of the tile is
     *         {@link tileType#CHARACTER}, {@link tileType#PIN}
     *         or {@link tileType#BAMBOO} and only 1 otherwise.
     */
    public int getVal() {
        return this.val;
    }

    /**
     * Returns the the type of the tile.
     * 
     * @return The type of the tile as a {@link tileType} object.
     */
    public tileType getType() {
        return this.type;
    }

    /**
     * Returns the unique ID of the tile.
     * 
     * @return The unique ID of this specific tile.
     */
    public int getUid() {
        return this.uid;
    }

    /**
     * Returns the String of a tile as {@link tileType#name()}. If the tile is of
     * type
     * {@link tileType#CHARACTER}, {@link tileType#PIN} or {@link tileType#BAMBOO},
     * it will also include a space followed by it's value.
     */
    @Override
    public String toString() {
        if (this.type == tileType.BAMBOO || this.type == tileType.CHARACTER || this.type == tileType.PIN)
            return this.type.name() + " " + this.val;
        return this.type.name();
    }

    /**
     * The ordering of tiles goes {@link tileType#CHARACTER}s 1 through 9,
     * {@link tileType#PIN}s 1 through 9, {@link tileType#BAMBOO}s 1 through 9,
     * {@link tileType#NORTH}, {@link tileType#SOUTH}, {@link tileType#EAST},
     * {@link tileType#WEST}, {@link tileType#RED}, {@link tileType#GREEN},
     * {@link tileType#WHITE}.
     */
    @Override
    public int compareTo(Tile other) {
        if (this.type == other.type) {
            if (this.val == other.val)
                return 0;
            else if (this.val > other.val)
                return 1;
            else
                return -1;
        } else if (Tile.valueOf(this.type) > Tile.valueOf(other.type)) {
            return 1;
        } else {
            return -1;
        }
    }

    /**
     * Maps each {@link tileType} to an integer value. Used for
     * {@link Tile#compareTo(Tile)} with
     * each type corresponding to the position of the type in the type order
     * starting with
     * {@link tileType#CHARACTER} at 0.
     * 
     * @param type The to-be-converted {@link tileType}.
     * @return The appropriate integer value of the type.
     */
    public static int valueOf(tileType type) {
        switch (type) {
            case CHARACTER:
                return 0;
            case PIN:
                return 1;
            case BAMBOO:
                return 2;
            case NORTH:
                return 3;
            case SOUTH:
                return 4;
            case EAST:
                return 5;
            case WEST:
                return 6;
            case RED:
                return 7;
            case GREEN:
                return 8;
            case WHITE:
                return 9;
            default:
                throw new IllegalArgumentException();
        }
    }

    /**
     * Maps each integer value from 0 to 9 to a {@link tileType}. The mapping
     * follows
     * the order used in {@link Tile#compareTo(Tile)}.
     * 
     * @param type The to-be-converted type as integer. Must be inside the interval
     *             [0, 9].
     * @return The converted {@link tileType}.
     * @throws IllegalArgumentException If the integer type is not inside the
     *                                  interval [0, 9].
     */
    public static tileType valueOf(int type) {
        switch (type) {
            case 0:
                return tileType.CHARACTER;
            case 1:
                return tileType.PIN;
            case 2:
                return tileType.BAMBOO;
            case 3:
                return tileType.NORTH;
            case 4:
                return tileType.SOUTH;
            case 5:
                return tileType.EAST;
            case 6:
                return tileType.WEST;
            case 7:
                return tileType.RED;
            case 8:
                return tileType.GREEN;
            case 9:
                return tileType.WHITE;
            default:
                throw new IllegalArgumentException();
        }
    }

    public Tile copyOf() {
        return new Tile(Tile.valueOf(this.type), this.val);
    }

    public int orderPos() {
        int typeVal = valueOf(this.getType());
        if (typeVal <= 2) {
            return typeVal * 9 + this.getVal() - 1;
        }
        return 24 + typeVal;
    }

    public static Tile tileAt(int i) {
        return staticTiles[i];
    }

    private static Tile[] initializeStaticTiles() {
        Tile[] res = new Tile[34];
        for (int i = 0; i < 34; i++) {
            res[i] = new Tile(i);
        }
        return res;
    }
}
