package javasudoku.model;
import java.util.ArrayList;

/**
 * Represents a Sudoku board and provides functionality for interacting with the board
 * 
 * Implements an ArrayList that maintains the SudokuCollections that 
 * manage the rows, columns, and subgrids. This class also stores a set of constants
 * that determine the dimensions of the board.
 * 
 * @author Ishaiah Cross
 */
public class SudokuBoard {
    //The number of subgrids along the X and Y axis
    public static final int SUBGRID_COUNT = 3;

    //The side length of a subgrid in number of cells.
    public static final int SUBGRID_SIZE = 3;
    
    //The side length of the entire board
    public static final int GRID_SIZE = SUBGRID_COUNT  * SUBGRID_SIZE;
    //The total number of cells in the board
    public static final int TOTAL_CELLS = GRID_SIZE * GRID_SIZE;
    
    //The minimum and maximum values that can be stored in a sudoku cell. These should never be changed
    //(0 represents unassigned)
    public static final int CELL_MIN = 0;
    public static final int CELL_MAX = 9;
    
    private final SudokuCell board[][];
    private final ArrayList<SudokuCollection> collections;
    
    public SudokuBoard() {
        collections = new ArrayList<>();
        board = new SudokuCell[SudokuBoard.GRID_SIZE][SudokuBoard.GRID_SIZE];
        
        //Initialize the board with all zeroes (representing unassigned cells)
        for(int y = 0; y < SudokuBoard.GRID_SIZE; y++) {
            for(int x = 0; x < SudokuBoard.GRID_SIZE; x++) {
                SudokuCoordinate position = new SudokuCoordinate(x, y);
                board[y][x] = new SudokuCell(position, 0);
            }
            
            //Initialize the collections
            collections.add(new SudokuRow(y));
            collections.add(new SudokuColumn(y));
            collections.add(new SudokuSubgrid(y));
        }
        
        
        //Populate the collections
        for(SudokuCollection c: collections)
            c.populateCollection(this);
    }
    
    /**
     * Sets the value of a cell at a given position if possible
     * @param position The position of the cell
     * @param value The new value to set
     * @return True if the value could be placed at that location, false if not
     */
    public boolean setCell(SudokuCoordinate position, int value)  {
        if(canPlaceAt(position, value)) {
            SudokuCell cell = board[position.getY()][position.getX()];
            cell.setCellValue(value);
            
            return true;
        }
        
        return false;
    }
    
    /**
     * Checks if the entire board is solved
     * @return True if the board is solved, false otherwise
     */
    public boolean isSolved() {
        //If any of the collections are incomplete, the board is not solved
        for(SudokuCollection c: collections)
            if(!c.testCollectionComplete())
                return false;
        
        return true;
    }
    
    /**
     * Checks if the entire board is empty
     * @return True if the board is empty, false otherwise
     */
    public boolean isEmpty() {
        //Iterate over only the first 3 collections in the arraylist, as these comprise every cell on the board
        for(SudokuCollection c: collections.subList(0, SUBGRID_COUNT))
            if(!c.testCollectionEmpty())
                return false;
        
        return true;
    }
    
    /**
     * Gets the cell at a given position
     * @param position The position of the desired cell
     * @return The cell at the given position
     */
    public SudokuCell getCell(SudokuCoordinate position) {
        SudokuCell cell = board[position.getY()][position.getX()];
        return cell;
    }
    
    /**
     * Gets the value of the cell at a given position
     * @param position The position of the desired cell
     * @return The value of the cell at the given position
     */
    public int getCellValue(SudokuCoordinate position) {
        SudokuCell cell = board[position.getY()][position.getX()];
        return cell.getCellValue();
    }

    /**
     * @return The board as a 2 dimensional array of SudokuCell objects
     */
    public SudokuCell[][] getBoard() {
        return board;
    }
    
    /**
     * Sets the board from a raw, non-jagged 2D array of SudokuCell objects.
     * 
     * If the board was not of the correct size, the board is not set.
     * @param rawBoard 
     */
    public void setBoard(SudokuCell[][] rawBoard) {
        int numRows = rawBoard.length;
        int numCols = rawBoard[0].length;
        
        //Check for incorrect dimensions
        if(numRows != GRID_SIZE || numCols != GRID_SIZE) {
            System.out.println("Warning: incorrect grid dimensions received - " + numRows + "x" + numCols);
            return;
        }
        
        //Copy reach row into the board
        for(int row = 0; row < numRows; row++)
            System.arraycopy(rawBoard[row], 0, this.board[row], 0, numCols);
    }
    
    /**
     * Returns the SudokuCollection objects that contain cell at the given
     * position
     * @param position The position to retrieve SudokuCollections from
     * @return All of the SudokuCollection objects in the collections arraylist that contain this coordinate
     */
    public ArrayList<SudokuCollection> getCollectionsAt(SudokuCoordinate position) {
        
        ArrayList<SudokuCollection> contains = new ArrayList<>();
        for(SudokuCollection collection: collections) {
            //If any of the collections contain this coordinate, add it to the arraylist
            if(collection.testContainsCoordinate(position))
                contains.add(collection);
        }
        
        return contains;
    }
    
    
    /**
     * Gets the first unassigned sudoku cell on the board
     * @return The first unassigned cell, or null if none was found
     */
    public SudokuCell getEmptyCell() {
        SudokuCoordinate curPosition = new SudokuCoordinate(0,0);
        
        //Iterate over every cell on the board
        for(int y = 0; y < SudokuBoard.GRID_SIZE; y++) {
            for(int x = 0; x < SudokuBoard.GRID_SIZE; x++) {
                curPosition.setX(x);
                curPosition.setY(y);
                
                //If an empty cell was found, return it
                SudokuCell currentCell = getCell(curPosition);
                if(currentCell.getCellValue() == 0)
                    return currentCell;
               
            }
        }
        
        return null;
    }
    
    /**
     * Determines whether or not a value can be placed at the given position
     * @param position The position to check
     * @param value The value to test
     * @return True if the value could be placed at the position, otherwise false
     */
    public boolean canPlaceAt(SudokuCoordinate position, int value) {
        //Cells can always be unassigned
        if(value == 0)
            return true;
        
        //Out of range values can never be placed
        if(value < SudokuBoard.CELL_MIN || value > SudokuBoard.CELL_MAX)
            return false;
        
        //Get the collections that contain this position
        ArrayList<SudokuCollection> collectionsContaining = getCollectionsAt(position);

        //For each of the collections, check if the provided value exists in them
        for(SudokuCollection c: collectionsContaining) {
            //If the value existed in any of them, it cannot be placed here
            
            if(c.testValueExists(value))
                return false;
        }
        
        //If the value did not exist in any of the above collections, it can be placed
        return true;
    }
    
    /**
     * Gets a string representation of a single row, to be used in the toString implementation
     * @return The string representation of the row
     */
    private String rowToString(int rowNumber) {
        StringBuilder rowBuilder = new StringBuilder();
        
        //Pad with a vertical coordinate number and a vertical bar
        rowBuilder.append(rowNumber + 1);
        rowBuilder.append(" | ");

        //Iterate over each number in the row
        for(int i = 0; i < SudokuBoard.GRID_SIZE; i++) {
            int cellValue = board[rowNumber][i].getCellValue();
            rowBuilder.append(cellValue == 0 ? "." : ""+cellValue);
            rowBuilder.append(" ");

            //Every third number, append another vertical bar
            if(i % 3 == 2)
                rowBuilder.append("| ");
        }
        
        //Move to the next line
        rowBuilder.append("\n");
        return rowBuilder.toString();
    }
    
    @Override
    public String toString() {
        StringBuilder boardString = new StringBuilder();
        
        //Generate the horizontal coordinates (designated by letters)
        char startCoord = 'A';
        StringBuilder horizontalAxis = new StringBuilder(" ");
        for(char i = 0; i < SudokuBoard.GRID_SIZE; i++) {
            //Convert the x-value to a character. By adding to startCoord we get the characters B, C, D, E, and so on.
            String coordinateLabel = " " + Character.toString((char) (startCoord + i));
            //After each subgrid, move an extra 2 spaces due to the subgrid separators
            if(i % SudokuBoard.SUBGRID_SIZE == 0) 
                horizontalAxis.append("  ");
            
            horizontalAxis.append(coordinateLabel);
        }
        
        boardString.append(horizontalAxis);
        boardString.append("\n");
        
        //Generate the horizontal separators between subgrids
        StringBuilder separator = new StringBuilder("  ");
        for(int i = 0; i < SudokuBoard.SUBGRID_COUNT; i++)
            separator.append("+-------");
        
        separator.append("+\n");
        
        //Iterate over each row
        for(int i = 0; i < SudokuBoard.GRID_SIZE; i++) {
            //After every subgrid row append a separator
            if(i % SudokuBoard.SUBGRID_SIZE == 0) {
                boardString.append(separator);
            }
            
            //Append the string representation of the row
            boardString.append(rowToString(i));
        }
        
        //Append a final separator to the board
        boardString.append(separator);
           
        return boardString.toString();
    }
    
    
}
