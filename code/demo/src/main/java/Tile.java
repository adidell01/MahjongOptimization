public class Tile implements Comparable<Tile>{
    private tileType type;
    private int val;
    private int uid;
    private static int uidGenerator = 0;

    private boolean isSequence2 = false;
    private boolean isSequence3 = false;
    private boolean isPair = false;
    private boolean isTriplet = false;

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

    public int getVal() {
        return this.val;
    }

    public tileType getType() {
        return this.type;
    }

    public int getUid() {
        return this.uid;
    }

    public void setSequence2(boolean isSequence2) {
        this.isSequence2 = isSequence2;
    }
    public boolean isSequence2() {
        return this.isSequence2;
    }

    public void setSequence3(boolean isSequence3) {
        this.isSequence3 = isSequence3;
    }
    public boolean isSequence3() {
        return this.isSequence3;
    }

    public void setPair(boolean isPair) {
        this.isPair = isPair;
    }
    public boolean isPair() {
        return this.isPair;
    }

    public void setTriplet(boolean isTriplet) {
        this.isTriplet = isTriplet;
    }
    public boolean isTriplet() {
        return this.isTriplet;
    }

    public void resetFlags() {
        this.isSequence2 = false;
        this.isSequence3 = false;
        this.isPair = false;
        this.isTriplet = false;
    }

    @Override
    public String toString(){
        if(this.type == tileType.BAMBOO || this.type == tileType.CHARACTER || this.type == tileType.PIN)
            return this.type.name() + " " + this.val;
        return this.type.name();
    }

    @Override
    public int compareTo(Tile other) {
        if(this.type == other.type){
            if(this.val == other.val)
                return 0;
            else if (this.val > other.val)
                return 1;
            else
                return -1;
        } else if (valueOf(this.type) > valueOf(other.type)){
            return 1;
        } else {
            return -1;
        }
    }

    public int valueOf(tileType type){
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
}
