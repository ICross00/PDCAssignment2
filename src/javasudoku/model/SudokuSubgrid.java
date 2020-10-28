package javasudoku.model;

/**
 * A concrete extension of the SudokuCollection class that implements
 * behaviour to extract a subgrid of SudokuCell instances from a SudokuBoard
 * @author Ishaiah Cross
 */
public class SudokuSubgrid extends SudokuCollection {
    private final int gridIndex;
    
    /**
     * @param gridIndex The index of the subgrid that this collection represents,
     * where the top-left subgrid is indexed as 0, increasing going right and then down
     * such that the top-middle subgrid is 1, top-right is 2, etc.
     */
    public SudokuSubgrid(int gridIndex) {
        this.gridIndex = gridIndex;
    }
    
    
    @Override
    public void populateCollection(SudokuBoard board) {
        //Get the row from the board
        SudokuCell boardCells[][] = board.getBoard();
        
        //Get the starting X and Y coordinates of the subgrid.
        //These are found by getting the coordinate of the top-left cell of the subgrid
        
        //The X coordinate increases by SudokuBoard.SUBGRID_SIZE with each subgrid going across, and resets to 0 on the next row
        int startX = SudokuBoard.SUBGRID_SIZE * (gridIndex % SudokuBoard.SUBGRID_SIZE); 
        
        //The Y coordinate increases by 3 every SudokuBoard.SUBGRID_SIZE cells, so it is the result of the gridIndex undergoing integer division by  SudokuBoard.SUBGRID_SIZE
        int startY = SudokuBoard.SUBGRID_SIZE * (gridIndex /  SudokuBoard.SUBGRID_SIZE); 
       
        //Perform a double nested loop to iterate over the subgrid starting from the two coordinates
        for(int offsY = 0; offsY < SudokuBoard.SUBGRID_SIZE; offsY++) {
            for(int offsX = 0; offsX < SudokuBoard.SUBGRID_SIZE; offsX++) {
                //Add the cell to the collection
                SudokuCell cell = boardCells[startY + offsY][startX + offsX];
                addCell(cell);
            }
        }
    }
}
