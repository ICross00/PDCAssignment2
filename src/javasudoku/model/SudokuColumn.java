package javasudoku.model;

/**
 * A concrete extension of the SudokuCollection class that implements
 * behaviour to extract a column of SudokuCell instances from a SudokuBoard
 * @author Ishaiah Cross
 */
public class SudokuColumn extends SudokuCollection {
    private final int colIndex;
    
    /**
     * @param colIndex The column in the SudokuBoard that this collection represents,
     * where the leftmost column is 0
     */
    public SudokuColumn(int colIndex ) {
        this.colIndex = colIndex ;
    }
    
    @Override
    public void populateCollection(SudokuBoard board) {
        //Get the row from the board
        SudokuCell boardCells[][] = board.getBoard();
        
        for(int row = 0; row < SudokuBoard.GRID_SIZE; row++) {
            //Fix the column using colIndex and iterate down each row to get all the cells in the column
            SudokuCell cell = boardCells[row][colIndex];
            //Add the cell to the collection
            addCell(cell);
        }
    }
}
