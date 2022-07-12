//package uk.ac.cam.jpj36.oop.tick5;
public class Pattern implements Comparable<Pattern> {

  private String name;
  private String author;
  private int width;
  private int height;
  private int startCol;
  private int startRow;
  private String cells;
  private String input;

  @Override
  public int compareTo(Pattern o) {
    return name.compareTo(o.getName());
  }

  @Override
  public String toString(){
    return name + " (" + author + ")";
  }
    
  public String getName() {return name;}
  public String getAuthor() {return author;}
  public int getWidth() { return width;}
  public int getHeight() {return height;}
  public int getStartCol() { return startCol;}
  public int getStartRow() {return startRow;}
  public String getCells() {return cells;}

  private void validate_input () throws PatternFormatException{
    if (input.isEmpty()) {
      throw new PatternFormatException("Please specify a pattern.");
    }
    String[] parts = input.split(":");
    if (parts.length != 7) {
      throw new PatternFormatException("Invalid pattern format: Incorrect number of fields in pattern (found " + parts.length + ").");
    }
    try {
      Integer.parseInt(parts[2]);
    } catch (NumberFormatException e) {
      throw new PatternFormatException("Invalid pattern format: Could not interpret the width field as a number ('" + parts[2] + "' given).");
    }
    try {
      Integer.parseInt(parts[3]);
    } catch (NumberFormatException e) {
      throw new PatternFormatException("Invalid pattern format: Could not interpret the height field as a number ('" + parts[3] + "' given).");
    }
    try {
      Integer.parseInt(parts[4]);
    } catch (NumberFormatException e) {
      throw new PatternFormatException("Invalid pattern format: Could not interpret the startX field as a number ('" + parts[4] + "' given).");
    }
    try {
      Integer.parseInt(parts[5]);
    } catch (NumberFormatException e) {
      throw new PatternFormatException("Invalid pattern format: Could not interpret the startY field as a number ('" + parts[5] + "' given).");
    }
    String[] rows = parts[6].split(" ");
    PatternFormatException cellException = new PatternFormatException("Invalid pattern format: Malformed pattern '" + parts[6] + "'.");
    if (rows.length > Integer.parseInt(parts[3])) {
      throw cellException;
    }
    for(int row = 0; row < rows.length; ++row) {
        char[] cols = rows[row].toCharArray();
        if (cols.length > Integer.parseInt(parts[2])) {
          throw cellException;
        }
        for(int col = 0; col < cols.length; ++col) {
            if (cols[col] != '1' && cols[col] != '0') {
              throw cellException;
            }
        }
    }
  }

  public Pattern(String format) throws PatternFormatException {
    input = format;
    validate_input();
    String[] parts = format.split(":");
    name = parts[0];
    author = parts[1];
    width = Integer.parseInt(parts[2]);
    height = Integer.parseInt(parts[3]);
    startCol = Integer.parseInt(parts[4]);
    startRow = Integer.parseInt(parts[5]);
    cells = parts[6];
  }

  public void initialise(World world) throws PatternFormatException {
    validate_input();
    String[] rows = cells.split(" ");
    for(int row = 0; row < rows.length; ++row) {
        char[] cols = rows[row].toCharArray();
        for(int col = 0; col < cols.length; ++col) {
            world.setCell(startCol+col, startRow+row, cols[col] == '1');
        }
    }
  }
}