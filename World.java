//package uk.ac.cam.jpj36.oop.tick5;
public abstract class World implements Cloneable{
  private int generation;
  private Pattern pattern;

  public World(String patternS) throws PatternFormatException{
    generation = 0;
    try {
  		pattern = new Pattern(patternS);
  	} catch (Exception e){
  		throw new PatternFormatException(e.getMessage());
  	}
  }
  public World(Pattern patOb){
  	generation = 0;
  	pattern = patOb;
  }
  //Copy Constructor
  public World (World wor){
    this.generation = wor.getGenerationCount();
    this.pattern = wor.getPattern(); // THIS MIGHT BE WRONG
  }

  @Override
  public World clone() throws CloneNotSupportedException{
    return (World)super.clone();
  }

  public int getWidth(){return pattern.getWidth();}
  public int getHeight(){return pattern.getHeight();}
  public int getGenerationCount(){return generation;}
  protected void incrementGenerationCount(){++generation;}
  protected Pattern getPattern(){return pattern;}
  public void nextGeneration() {
    nextGenerationImpl();
    generation++;
  }
  protected abstract void nextGenerationImpl();
  public abstract boolean getCell(int col, int row);
  public abstract void setCell(int col, int row, boolean val);
  protected int countNeighbours(int x, int y){
    int countNeigh = 0;
    for (int rowC = -1; rowC < 2; rowC++) { 
      for (int colC = -1; colC < 2; colC++) {
          countNeigh += getCell(x+colC, y+rowC) ? 1 : 0;
      }
    }
    countNeigh -= getCell(x, y) ? 1 : 0;
    return countNeigh;
  }
  protected boolean computeCell( int x, int y){  
    int neighbours = countNeighbours(x,y);
    switch (neighbours) {
      case 2: return getCell(x,y); //Stays the same
      case 3: return true; //A dead cell with exactly three live neighbours comes alive
      default: return false; //Dies as too few or too many
    }
  }
}