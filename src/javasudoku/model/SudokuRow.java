package javasudoku.model;

/**
 * A concrete extension of the SudokuCollection class that implements
 * behaviour to extract a row of SudokuCell instances from a SudokuBoard
 * @author Ishaiah Cross
 */
public class SudokuRow extends SudokuCollection {
    private final int rowIndex;
    
    /**
     * @param rowIndex The row in the SudokuBoard that this collection represents,
     * where the topmost row is 0
     */
    public SudokuRow(int rowIndex) {
        this.rowIndex = rowIndex;
    }
  
    @Override
    public void populateCollection(SudokuBoard board) {
        //Get the row from the board
        SudokuCell boardCells[][] = board.getBoard();
        SudokuCell row[] = boardCells[rowIndex];
        
        //Add all the cells in the row 
        for(SudokuCell cell : row) {
            addCell(cell);
        }
    }
}
