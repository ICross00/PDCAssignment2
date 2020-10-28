package javasudoku.model;

/**
 * A class containing methods to convert Sudoku boards to and from
 * one-line strings that can easily be written to text files or databases
 * 
 * @author Ishaiah Cross
 */
public class SudokuStringifier {
    //Delimiters that define how the completion status, date, and board data are separated
    public static final String CELL_SEPARATOR = ",";
    public static final String ROW_SEPARATOR = "/";
    
    /**
     * Converts a SudokuBoard into a one-line string that can be written to a file.
     * The string is formatted as follows:
     * 
     * number of filled cells/number of cells=date=row1|row2|row3...
     * 
     * This representation is not the same as that provided by the
     * toString method of the SudokuBoard class.
     * 
     * @return The board represented as an encoded string
     * @param board The board to convert into a string
     */
    public static String boardToString(SudokuBoard board) {
        SudokuCell[][] cellData = board.getBoard();
        StringBuilder boardStr = new StringBuilder();
        
        //Iterate over every cell in the board to write the cell data
        for (int curRow = 0; curRow < cellData.length; curRow++) {
            SudokuCell[] cellRow = cellData[curRow];
            
            for(int curCell = 0; curCell < cellRow.length; curCell++) {
                int cellValue = cellRow[curCell].getCellValue();
                //Append the value to the string as well as a separator if this was not the last cell in the row
                boardStr.append(cellValue);
                if(curCell != cellRow.length - 1)
                    boardStr.append(CELL_SEPARATOR);  
            }
            
            //Append a row separator if the current row was not the final row in the board
            if(curRow != cellData.length - 1)
                boardStr.append(ROW_SEPARATOR);
        }
        
        //Prepend the completion status to the output
        return boardStr.toString();
    }
    
    /**
     * Uses raw board string data as generated by boardToString to create and initialize a SudokuBoard object
     * 
     * @param boardStr the raw board string data
     * @return The initialized SudokuBoard, or null if the board was formatted incorrectly
     */
    public static SudokuBoard parseBoard(String boardStr) {
        
        try {
            //Split the raw board data into tokens
            
            //Create a SudokuBoard that we will initialize using the retrieved data
            SudokuBoard board = new SudokuBoard();
            SudokuCoordinate curCoordinate = new SudokuCoordinate(0, 0);
            
            //Split the board into rows
            String[] rows = boardStr.split(ROW_SEPARATOR);
           
            
            for(int rowIndex = 0; rowIndex < rows.length; rowIndex++) {
                //Split the row into its individual numbers
                String[] curRow = rows[rowIndex].split(CELL_SEPARATOR);
                
                //Iterate over every number in this row, and use it to initialize the board
                for(int colIndex = 0; colIndex < curRow.length; colIndex++) {
                    curCoordinate.setX(colIndex);
                    curCoordinate.setY(rowIndex);
                    
                    //Convert the current  cell value into a number, and set it on the board
                    int curValue = Integer.parseInt(curRow[colIndex]);
                    board.setCell(curCoordinate, curValue);
                }
            }
            
            //At this point, the board is fully initialized
            return board;
             
        } catch(NumberFormatException e) {
            System.out.println("Board data contained invalid numbers.");
            System.out.println(e.getMessage());
        }
        
        return null;
    }
}