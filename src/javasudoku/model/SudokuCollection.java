package javasudoku.model;

import java.util.ArrayList;
import java.util.HashSet;

/**
 * An abstract collection of sudoku cells. This may store a row,
 * a grid, a square of cells referred to as a subgrid, or any custom collection of cells. 
 * 
 * This class also provides functionality to test if a value already exists in the collection,
 * and that all values are present.
 * 
 * Classes that extend this class must provide their own functionality for extracting cells from a SudokuBoard.
 * 
 * @author Ishaiah Cross
 */
public abstract class SudokuCollection {
     
    public final ArrayList<SudokuCell> collection;
    //This stores all the coordinates that the collection contains. This way it can quickly be checked if a cell from the board exists.
    public final HashSet<SudokuCoordinate> containedCoords;
    
    public SudokuCollection() {
        collection = new ArrayList<>();
        containedCoords = new HashSet<>();
    }
    
    /**
     * Adds a sudoku cell to the arraylist
     * Will not be added if the arraylist is full
     * Will not be added if the cell already exists
     * @param cell The cell to be added
     * @return true if the value was added, false if the value already existed or if the arraylist was full
     */
    public boolean addCell(SudokuCell cell) {
        if(collection.size() > SudokuBoard.CELL_MAX)
            return false;
        
        SudokuCoordinate cellPosition = cell.getPosition();
        if(containedCoords.contains(cellPosition))
            return false;
        
        collection.add(cell);
        containedCoords.add(cellPosition);
        return true;
    }
    
    /**
     * Gets the first sudoku cell with the desired value
     * @param cellValue the value of the cell to retrieve
     * @return The cell that was retrieved, or null if no cell had that value
     */
    public SudokuCell getCell(int cellValue) {
        for(SudokuCell cell: collection) {
            if(cell.getCellValue() == cellValue)
                return cell;
        }
        
        return null;
    }
           
    /**
     * Checks if a cell with a given value exists in the collection
     * @param value
     * @return True if a cell with the given value exists, false if not
     */
    public boolean testValueExists(int value) {
        for(SudokuCell cell: collection) {
            int cellValue = cell.getCellValue();
            if(value == cellValue)
                return true;
        }
        return false;
    }
    
    /**
     * Checks if the collection is complete, i.e. that it contains all of the
     * values 1 through 9
     * @return True if the collection contains all values 1-9, false if not
     */
    public boolean testCollectionComplete() {
        //if 0 exists in the collection, the collection is incomplete as the cell is unassigned
        if(testValueExists(0))
            return false;
        
        //check that all values 1-9 exist in the collection
        for(int value = SudokuBoard.CELL_MIN; value < SudokuBoard.CELL_MAX; value++) {
            //return false if the current value did not exist
            if(!testValueExists(value + 1))
                return false;
        }
        
        //if this line was reached, 0 is not present and all values 1-9 are present
        return true;
    }
    
    /**
     * Checks if any of the cells in the collection are at the given position
     * @param position The position to test
     * @return True if the collection contains a cell at that position, false otherwise
     */
    public boolean testContainsCoordinate(SudokuCoordinate position) {
        return containedCoords.contains(position);
    }
    
    /**
     * Defines behaviour to populate the sudoku collection with the appropriate
     * cells from a SudokuBoard
     * @param sourceBoard The board from which to add cells to the collection
     */
    public abstract void populateCollection(SudokuBoard sourceBoard);
}
