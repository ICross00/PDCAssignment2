package javasudoku.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * A wrapper class that pairs a SudokuBoard instance with a player name and 
 * completion status, so that it can be written to the database.
 * @author Ishaiah Cross
 */
public final class SudokuGame {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");  
    
    public final String playerName;
    public final SudokuBoard boardState;
    public final float completionPercentage;
    public String lastPlayedDate;
    
    public SudokuGame(String playerName, SudokuBoard boardState) {
        this.playerName = playerName;
        this.boardState = boardState;
        
        int totalCells = SudokuBoard.GRID_SIZE * SudokuBoard.GRID_SIZE;
        this.completionPercentage = ((float)getNumFilledCells()) / totalCells;
        
    }
    
    /**
     * Saves the current date to the object
     */
    public void saveLastPlayedDate() {
        //Get the current date
        LocalDateTime curTime = LocalDateTime.now();  
        this.lastPlayedDate = formatter.format(curTime);
    }
    
    /**
     * Manually sets the last played date of the game
     * @param date The date to set as a string
     */
    public void setDate(String date) {
        this.lastPlayedDate = date;
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

