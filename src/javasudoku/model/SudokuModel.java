package javasudoku.model;

import java.util.Observable;

/**
 * Initializes all of the necessary classes to generate and play a game of Sudoku,
 * including a SudokuBoard, a SudokuGenerator, and a player name.
 * 
 * This class extends the Observable class in order to fulfill the role of the
 * model in the MVC design pattern.
 * 
 * Provides functionality to interact with these underlying classes
 * @author Ishaiah Cross
 */
public class SudokuModel extends Observable {

    SudokuBoard board;
    private final SudokuGenerator generator;
    private String playerName;
    
    //TODO: Instantiate all the necessary objects & provide all the functionality for the necessary objects
    public SudokuModel(String playerName) {
        this.generator = new SudokuGenerator();
        this.playerName = playerName;
    }
    
    /**
     * Returns whether or not a board has been initialized
     * @return True if a board has been initialized, false if not
     */
    public boolean isGameActive() {
        return this.board != null;
    }
    
    /**
     * Initializes the current board as an empty board.
     */
    public void startEmptyBoard() {
        this.board = new SudokuBoard();
        
        //Update observers, notify them about the new board state
        setChanged();
        notifyObservers(this.getBoard());
    }
    
    /**
     * Initializes the current board as a partially solved puzzle.
     * @param difficulty The desired difficulty of the puzzle
     */
    public void startPuzzle(int difficulty) {
        this.board = generator.generatePuzzle(difficulty);
        
        //Update observers, notify them about the new board state
        setChanged();
        notifyObservers(this.getBoard());
    }
    
    /**
     * Checks if the board is solved
     * @return True if the board is currently solved, false if not
     */
    public boolean checkIsSolved() {
        return getBoard().isSolved();
    }
        
    /**
     * Solves the current board. If no board has been initialized,
     * this function will not do anything.
     */
    public void solveBoard() {
        if (this.getBoard() != null) {
            generator.getSolver().solveBoard(getBoard());
            
            //Update observers, notify them about the new board state
            setChanged();
            notifyObservers(this.getBoard());
        }
    }
    
    /**
     * Try to put a value at a given Sudoku cell
     * @param coordinate The coordinate of the cell to modify
     * @param value The desired value
     * @return True if the value could be placed, false if not
     */
    public boolean tryPutValue(SudokuCoordinate coordinate, int value) {
        boolean wasPlaced = getBoard().setCell(coordinate, value);
        
        if(wasPlaced) {
            //Update observers, notify them about the new board state
            setChanged();
            notifyObservers(this.getBoard());
        }
        
        return wasPlaced;
    }
    
    /**
     * Removes the value at a given sudoku cell
     * @param coordinate The coordinate of the cell to modify
     */
    public void removeValue(SudokuCoordinate coordinate) {
        getBoard().setCell(coordinate, 0);
        
        //Update observers, notify them about the new board state
        setChanged();
        notifyObservers(this.getBoard());
    }
    
    /**
     * Imports an existing SudokuGame object as the current game being maintained
     * by the model
     * @param game The game to import
     */
    public void importGame(SudokuGame game) {
        setPlayerName(game.playerName);
        this.board = game.boardState;
    }
    
    /**
     * Returns the current state of this class as a SudokuGame object,
     * which contains the completion amount, player, date, and board object
     * @return A fully initialized SudokuGame object encapsulating the current
     * state of this class
     */
    public SudokuGame exportGame() {
        return new SudokuGame(getPlayerName(), getBoard());
    }
    
    /**
     * @return the board
     */
    public SudokuBoard getBoard() {
        return board;
    }

    /**
     * @return The current player's name
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * @param playerName The current player name to set
     */
    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
}
