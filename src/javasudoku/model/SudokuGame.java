package javasudoku.model;

/**
 * A wrapper class that pairs a SudokuBoard instance with a player name and 
 * completion status, so that it can be written to the database.
 * @author Ishaiah Cross
 */
public final class SudokuGame {
    public final String playerName;
    public final SudokuBoard boardState;
    public final float completionPercentage;
    
    public SudokuGame(String playerName, SudokuBoard boardState) {
        this.playerName = playerName;
        this.boardState = boardState;
        
        int totalCells = SudokuBoard.GRID_SIZE * SudokuBoard.GRID_SIZE;
        this.completionPercentage = ((float)getNumFilledCells()) / totalCells;
    }
    
    /**
     * Counts how many cells in the SudokuBoard object are filled
     * @return The number of filled cells in the SudokuBoard object
     */
    private int getNumFilledCells() {
        if(boardState == null)
            return 0;
        
        int numCells = 0;
        
        SudokuCell[][] cellData = boardState.getBoard();
        for (SudokuCell[] cellRow : cellData) {
            for (SudokuCell cell : cellRow) {
                int cellValue = cell.getCellValue();
                if(cellValue != 0) //If the cell was not empty, increment the count
                    numCells++;
            }
        }

        return numCells;
    }
}

