//package uk.ac.cam.jpj36.oop.tick5;
public class PackedWorld extends World {
    long world;

    public PackedWorld(String format) throws Exception {
        super(format);
        if (getPattern().getWidth() * getPattern().getHeight() > 64) {
            throw new Exception("Field too big!");
        }
        getPattern().initialise(this);
    }
    public PackedWorld(Pattern p) throws Exception {
        super(p);
        if (getPattern().getWidth() * getPattern().getHeight() > 64) {
            throw new Exception("Field too big!");
        }
        getPattern().initialise(this);
    }
    //Copy Constructor
    public PackedWorld(PackedWorld pac){
        super(pac);
        this.world = pac.world;
    }

    @Override
    public World clone() throws CloneNotSupportedException{
        return (World)super.clone();
    }

    private boolean getBit(int position) {
        // set "check" to equal 1 if the "position" bit in "packed" is set to 1
        // you should use bitwise operators (not % or similar)
        long check = (world >> position) & 1L;
        return (check == 1);
    }

    /*
    * Set the nth bit in the packed number to the value given
    * and return the new packed number
    */
    private void setBit(int position, boolean value) {
    if (value) {
        // update the value "packed" with the bit at "position" set to 1
        world |= 1L << position; 
    }
    else {
        // update the value "packed" with the bit a "position" set to 0
        world &= ~(1L << position);
    }
    }

    private int getIdx(int col, int row) {
        return col < 0 || col > 7 || row < 0 || row > 7 ? -1 : col + row*8;
    } 

    public boolean getCell(int col, int row) {
        int idx = getIdx(col, row);
        return idx == -1 ? false : getBit(getIdx(col, row));
    }

    public void setCell(int col, int row, boolean value) {
        setBit(getIdx(col, row), value);
    }

    public void nextGenerationImpl() {
        long next_gen = 0L;
        for (int row = 0; row < 8; row++) { 
            for (int col = 0; col < 8; col++) {
                next_gen |= (computeCell(col, row) ? 1L : 0L) << getIdx(col, row);
            }
        } 
        world = next_gen;
    }
}