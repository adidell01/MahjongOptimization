public class Tile {
    private tileType type;
    private int val;
    private int uid;
    private static int uidGenerator = 0;

    public Tile(int type, int val){
        switch (type) {
            case 0:
                this.type = tileType.BAMBOO;
                break;
            case 1:
                this.type = tileType.CHARACTER;
                break;
            case 2:
                this.type = tileType.PIN;
                break;
            case 3:
                this.type = tileType.NORTH;
                break;
            case 4:
                this.type = tileType.SOUTH;
                break;
            case 5:
                this.type = tileType.EAST;
                break;
            case 6:
                this.type = tileType.WEST;
                break;
            case 7:
                this.type = tileType.RED;
                break;
            case 8:
                this.type = tileType.GREEN;
                break;
            case 9:
                this.type = tileType.WHITE;
                break;
            default:
                throw new IllegalArgumentException("INVALID TYPE FOR TILECREATION");
        }
        this.val = val;
        this.uid = this.uidGenerator;
        this.uidGenerator++;
    }

    public int getUid() {
        return this.uid;
    }

    @Override
    public String toString(){
        if(this.type == tileType.BAMBOO || this.type == tileType.CHARACTER || this.type == tileType.PIN)
            return this.type.name() + " " + this.val;
        return this.type.name();
    }
}
