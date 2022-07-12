//package uk.ac.cam.jpj36.oop.tick5;
public class ArrayWorld extends World{
	boolean[][] world;
	boolean[] deadRow;
    
    public ArrayWorld(String serial) throws Exception{
	    super(serial); //Calls the world constructor
	    world = new boolean[getHeight()][getWidth()];
	    deadRow = new boolean[getWidth()];
	    for (int x = 0; x < getWidth(); x++){
	    	deadRow[x] = false;
	    }

	    try {
	  		getPattern().initialise(this);
	  		this.makeDead();
	  	} catch (PatternFormatException e){
	  		throw new Exception(e.getMessage());
	  	}
	}
	
	public ArrayWorld(Pattern patOb) throws Exception{
	  	super(patOb);
	  	world = new boolean[patOb.getHeight()][patOb.getWidth()];
	  	deadRow = new boolean[patOb.getWidth()];
	  	for (int x = 0; x < getWidth(); x++){
	    	deadRow[x] = false;
	    }

	  	try {
	  		getPattern().initialise(this);
	  		this.makeDead();
	  	} catch (PatternFormatException e){
	  		throw new Exception(e.getMessage());
	  	}
	}
	//Copy Constructor
	public ArrayWorld(ArrayWorld ar){
		super(ar);
		int height = ar.getHeight();
		int width = ar.getWidth();
		world = new boolean[height][width];
		for (int y = 0; y < height; ++y) {
		    world[y]=ar.world[y].clone();
    	}
    	this.deadRow = ar.deadRow; //This is so all copies use the same chunk of memory
	}

	@Override
    public ArrayWorld clone() throws CloneNotSupportedException{
        ArrayWorld clonedWorld = (ArrayWorld)super.clone();
        clonedWorld.world = new boolean[this.world.length][this.world[0].length];
        clonedWorld.deadRow = this.deadRow.clone();
	  	int heightVar = this.world.length;
	  	int widthVar = this.world[0].length;
	   	for (int y = 0; y < heightVar; ++y) {
			for (int x=0; x < widthVar; ++x){
			    clonedWorld.world[y][x] = world[y][x];
			}
	   	}
        clonedWorld.makeDead();
    	return clonedWorld;
    }

    public void makeDead (){
	  	int counterV=0;
	  	int heightVar = this.world.length;
	  	int widthVar = this.world[0].length;
	  	for (int y = 0; y < heightVar; ++y) {
	  	    counterV = 0;
			for (int x = 0; x < widthVar; ++x) {
			    counterV += this.world[y][x] ? 1 : 0;
			}
			if (counterV == 0) {
			    this.world[y] = this.deadRow;
			}
	   	}
    }

	@Override
	public boolean getCell(int col, int row){
	    try {
	      	return world[row][col];
	    } catch (Exception e){
	      	return false;
	    }
	}

  	@Override
  	public void setCell(int col, int row, boolean val){
	    try {
	      	world[row][col] = val;
	    } catch (Exception e){
	      	;
	    }
  	}

  public void nextGenerationImpl() {
    boolean[][] nextGeneration = new boolean[getHeight()][getWidth()];
    for (int x = 0; x < getWidth(); ++x) {
      for (int y = 0; y < getHeight(); ++y) {
          nextGeneration[y][x]=computeCell(x, y);
      }
    }
    world = nextGeneration;
  }
}