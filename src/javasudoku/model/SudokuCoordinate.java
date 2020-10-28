package javasudoku.model;

/**
 * Represents a x, y coordinate pair that encodes the position of a cell
 * on a sudoku board. 
 * 
 * The values of the individual coordinates are 
 * constrained on the interval defined by [0, GRID_SIZE -1]. GRID_SIZE is located
 * within the JavaSudoku class.
 * 
 * The coordinate (0, 0) represents the top-left corner of the board.
 * @author Ishaiah Cross
 */
public final class SudokuCoordinate {
    private int x;
    private int y;
    
    /**
     * 
     * @param x The X coordinate 
     * @param y The Y coordinate
     */
    public SudokuCoordinate(int x, int y)  {
        setX(x);
        setY(y);
    }
    
    /**
     * @return The X coordinate 
     */
    public int getX() {
        return x;
    }

    /**
     * @param x The new X coordinate 
     */
    public void setX(int x) {
        this.x = Math.max(0, Math.min(SudokuBoard.GRID_SIZE - 1, x));
    }

    /**
     * @return The Y coordinate 
     */
    public int getY() {
        return y;
    }

    /**
     * @param y The new Y coordinate 
     */
    public void setY(int y) {
        this.y = Math.max(0, Math.min(SudokuBoard.GRID_SIZE - 1, y));
    }
    
    @Override
    public String toString() {
        return x + " , " + y;
    }
    
    @Override
    public int hashCode() {
        int hash = 5;
        hash = 17 * hash + x;
        hash = 17 * hash + y;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SudokuCoordinate other = (SudokuCoordinate) obj;
        if (x != other.x) {
            return false;
        }
        if (y != other.y) {
            return false;
        }
        return true;
    }

   
}
