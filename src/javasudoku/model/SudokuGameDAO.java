package javasudoku.model;
import java.util.ArrayList;

/**
 * Defines how classes should implement database access for the SudokuGame object by
 * providing add, get, remove, and update operations
 * @author Ishaiah Cross
 */
public interface SudokuGameDAO {
    /**
     * This method should retrieve a SudokuGame object from data storage, given a unique ID used
     * to identify the object
     * @param id The ID of the SudokuGame
     * @return The SudokuGame object
     */
    public SudokuGame getGame(int id);
    
    /**
     * Add a SudokuGame object to the data storage
     * @param game The SudokuGame object to store
     */
    public void addGame(SudokuGame game);
    
    /**
     * Remove a game from the data storage given its unique ID
     * @param id  The ID of the SudokuGame to remove
     */
    public void removeGame(int id);
    
    /**
     * Return all of the SudokuGame objects whose player name
     * matches the provided parameter
     * @param name The name to search for in data storage
     * @return An ArrayList containing all of the SudokuGame objects with a matching
     * player name
     */
    public ArrayList<SudokuGame> getGamesByName(String name);
    
    /**
     * Gets all of the unique player names stored by SudokuGame objects
     * in data storage
     * @return An ArrayList containing all of the unique player names in data
     * storage
     */
    public ArrayList<String> getPlayerNames();
}
