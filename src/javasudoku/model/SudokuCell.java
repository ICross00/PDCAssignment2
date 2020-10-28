package javasudoku.model;

/**
 * Represents a single cell on a Sudoku board. Contains the value of the cell
 * as well as its position.
 * 
 * The value must be on the interval defined by the static variables
 * CELL_MIN and CELL_MAX in the JavaSudoku class.
 * 
 * A cell whose value is equal to CELL_MIN is assumed to be unassigned.
 * 
 * @author Ishaiah Cross
 */
public final class SudokuCell {
    private final SudokuCoordinate position;
    private int cellValue;
    
    /**
     * Initialize a cell given a position and an initial value.
     * @param position
     * @param cellValue 
     */
    public SudokuCell(SudokuCoordinate position, int cellValue) {
        this.position = position;
        setCellValue(cellValue);
    }
  
    /**
     * @return The cell value
     */
    public int getCellValue() {
        return cellValue;
    }

    /**
     * @param cellValue The cell value to set. The cell value will be clamped to lie within the interval
     * defined by SudokuBoard.CELL_MIN and SudokuBoard.CELL_MAX
     */
    public void setCellValue(int cellValue) {
        this.cellValue = Math.max(SudokuBoard.CELL_MIN, Math.min(SudokuBoard.CELL_MAX, cellValue));
    }
    
    /**
     * @return the position of the cell
     */
    public SudokuCoordinate getPosition() {
        return position;
    } 
}
