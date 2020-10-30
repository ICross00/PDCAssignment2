package javasudoku.tests;
import java.util.ArrayList;
import org.junit.Test;
import javasudoku.model.*;
import javasudoku.solver.SudokuSolver;
import org.junit.Assert;
import org.junit.Before;
import org.junit.After;

/**
 * Contains unit tests to validate the functionality of the following Sudoku features:
 * 
 * -Adding a cell to a board
 * -Adding a duplicate value to along a row should not modify the board state
 * -Adding a duplicate value to along a column should not modify the board state
 * -Adding a duplicate value to in a board should not modify the board state
 * -A cell should be intersected by three SudokuCollection objects corresponding to a row, column, and subgrid
 * -A board should be able to check if it is in a solved state using isSolved, which uses its SudokuCollection objects
 * -Attempting to find an empty cell on a board that is fully solved should return null
 * 
 * Each unit test will provide a setup and teardown procedure involving
 * initializing an empty board and generator, before modifications are done on the board
 * by the unit test. The teardown will then destroy the board and generator to be re-created for the next test.
 * 
 * @author Ishaiah Cross
 */
public class SudokuBoardTest {
    //These will be instantiated before each test, and removed after each test
    private SudokuBoard board;
    private SudokuGenerator generator;
    
    /**
     * Initialize a SudokuBoard and SudokuGenerator for puzzle generation
     */
    @Before
    public void setupBoardAndGenerator() {
        this.board = new SudokuBoard();
        this.generator = new SudokuGenerator();
    }
    
    /**
     * Destroy the initialized objects after each test to work with clean objects
     */
    @After
    public void destroyBoardAndGenerator() {
        this.board = null;
        this.generator = null;
    }
    
    /**
     * This test asserts that after the 'setCell' method has been called on the
     * SudokuBoard, the underlying SudokuCell object has had its value changed
     * and is the same cell as defined by the coordinates
     */
    @Test
    public void sudokuBoard_setCell_CellModified() {
        //Values that will be used to test
        int testX = 6;
        int testY = 4;
        int testValue = 9;
        
        //Test the top-left cell
        SudokuCoordinate testCoordinate = new SudokuCoordinate(testX, testY);
        this.board.setCell(testCoordinate, testValue);
        
        //Access the cell manually using the board's underlying cell array
        SudokuCell[][] rawCellData = this.board.getBoard();
        SudokuCell modifiedCell = rawCellData[testY][testX];
        
        int cellValue = modifiedCell.getCellValue();
        
        //Assert that the same cell has been modified
        Assert.assertEquals(testValue, cellValue);
    }
    
     /**
     * If setCell is used to set a cell value that cannot be placed there, the
     * underlying data of the SudokuBoard object should not be modified. 
     * 
     * This test will attempt to duplicate a value within a row, and asserts
     * that the corresponding cell in the underlying data of the
     * SudokuBoard has not changed.
     */
    @Test
    public void sudokuBoard_setCell_InvalidRowPlacementNoModification() {
        //Value that will be tested via duplication across a row
        int testValue = 9;
        //Access the cells manually using the board's underlying cell array
        SudokuCell[][] rawCellData = this.board.getBoard();
        
        //Set up a valid test cell at 0, 0
        SudokuCoordinate validCellCoordinate = new SudokuCoordinate(0, 0);
        this.board.setCell(validCellCoordinate, testValue);
        
        //Attempt to duplicate the value along the same row, but not in the same subgrid
        int testX = 7;
        SudokuCoordinate rowDuplicateCoordinate = new SudokuCoordinate(testX, 0);
        this.board.setCell(rowDuplicateCoordinate, testValue);
        
        //Assert that the cell has not been modified, i.e. that its value is still zero
        SudokuCell targetCell = rawCellData[testX][0];
        int cellValue = targetCell.getCellValue();
        
        Assert.assertEquals(0, cellValue);
    }
    
    /**
     * This test will attempt to duplicate a value along a column, and asserts
     * that the corresponding cell in the underlying data of the
     * SudokuBoard has not changed.
     */
    @Test
    public void sudokuBoard_setCell_InvalidColumnPlacementNoModification() {
        //Value that will be tested via duplication along a column
        int testValue = 9;
        //Access the cells manually using the board's underlying cell array
        SudokuCell[][] rawCellData = this.board.getBoard();
        
        //Set up a valid test cell at 0, 0
        SudokuCoordinate validCellCoordinate = new SudokuCoordinate(0, 0);
        this.board.setCell(validCellCoordinate, testValue);
        
        //Attempt to duplicate the value along the same column, but not in the same subgrid
        int testY = 7;
        SudokuCoordinate columnDuplicateCoordinate = new SudokuCoordinate(0, testY);
        this.board.setCell(columnDuplicateCoordinate, testValue);
        
        //Assert that the cell has not been modified, i.e. that its value is still zero
        SudokuCell targetCell = rawCellData[0][testY];
        int cellValue = targetCell.getCellValue();
        
        Assert.assertEquals(0, cellValue);
    }
    
    /**
     * This test will attempt to duplicate a value within a subgrid, and asserts
     * that the corresponding cell in the underlying data of the
     * SudokuBoard has not changed.
     */
    @Test
    public void sudokuBoard_setCell_InvalidSubgridPlacementNoModification() {
        //Value that will be tested via duplication within a subgrid
        int testValue = 9;
        //Access the cells manually using the board's underlying cell array
        SudokuCell[][] rawCellData = this.board.getBoard();
        
        //Set up a valid test cell at 0, 0
        SudokuCoordinate validCellCoordinate = new SudokuCoordinate(0, 0);
        this.board.setCell(validCellCoordinate, testValue);
        
        //Attempt to duplicate the value within the same subgrid, but not in the same row or column
        int testX = 2;
        int testY = 2;
        SudokuCoordinate subgridDuplicateCoordinate = new SudokuCoordinate(testX, testY);
        this.board.setCell(subgridDuplicateCoordinate, testValue);
        
        //Assert that the cell has not been modified, i.e. that its value is still zero
        SudokuCell targetCell = rawCellData[testX][testY];
        int cellValue = targetCell.getCellValue();
        
        Assert.assertEquals(0, cellValue);
    }
    
    /**
     * Asserts that a given cell belongs to exactly 3 SudokuCollection objects,
     * and that those collections are a subgrid, a row, and a column
     * using a SudokuSolver
     */
    @Test
    public void sudokuBoard_getCollectionsAt_ReturnsThreeCollections() {
        //Values that will be used to test
        int testX = 6;
        int testY = 4;
        int testValue = 9;
        
        //Test the top-left cell
        SudokuCoordinate testCoordinate = new SudokuCoordinate(testX, testY);
        ArrayList<SudokuCollection> intersectingCollections = board.getCollectionsAt(testCoordinate);
        
        //Assert that there are exactly three collections
        Assert.assertEquals(3, intersectingCollections.size());
        
        //Check that the three collections are a row, column, and subgrid
        boolean cellFoundInRow = false;
        boolean cellFoundInColumn = false;
        boolean cellFoundInSubgrid = false;
        
        for(SudokuCollection collection: intersectingCollections) {
            if(collection instanceof SudokuRow)
                cellFoundInRow = true; //The cell belongs to a row collection
            else if(collection instanceof SudokuColumn)
                cellFoundInColumn = true; //The cell belongs to a column collection
            else if(collection instanceof SudokuSubgrid)
                cellFoundInSubgrid = true; //The cell belongs to a subgrid collection
        }
        
        //Assert that all 3 colleciton types were present
        Assert.assertTrue(cellFoundInRow);
        Assert.assertTrue(cellFoundInColumn);
        Assert.assertTrue(cellFoundInSubgrid);
    }
    
    /**
     * Asserts that when the board is not solved, the isSolved method returns false,
     * and that when the board is known to be solved (using a SudokuSolver),
     * the isSolved method returns true
     */
    @Test
    public void sudokuBoard_isSolved_DetectsBoardSolved() {
        //Assert that isSolved returns false on a non-solved board
        boolean emptyBoardSolved = board.isSolved();
        Assert.assertFalse(emptyBoardSolved);
        
        //Create a SudokuSolver and solve the board
        SudokuSolver testSolver = new SudokuSolver();
        testSolver.solveBoard(board);
        
        //Assert that isSolved returns true on a solved board
        boolean completeBoardSolved = board.isSolved();
        Assert.assertTrue(completeBoardSolved);
    }
    
    /**
     * Asserts that on a fully solved board, null is returned
     * when the getEmptyCell method is called to indicate
     * there are no empty cells
     */
   @Test
   public void sudokuBoard_gewtEmptyCell_NoEmptyCellsReturnsNull() {
        //Create a SudokuSolver and solve the board
        SudokuSolver testSolver = new SudokuSolver();
        testSolver.solveBoard(board);
        
        //Attempt to find the first empty cell
        SudokuCell emptyCell = board.getEmptyCell();
        
        //Assert that the empty cell was null, i.e. that no cell was found
        Assert.assertNull(emptyCell);
   }
}
