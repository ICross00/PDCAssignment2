package javasudoku.model;
import java.util.ArrayList;

/**
 * Defines how classes should implement database access for the SudokuGame object by
 * providing add, get, remove, and update operations
 * @author Ishaiah Cross
 */
public interface SudokuGameDAO {
    public SudokuGame getGame(int id);
    public void addGame(SudokuGame game);
    public void removeGame(int id);
    public void updateGame(int id, SudokuGame newBoard);
    public ArrayList<SudokuGame> getGamesByName(String name);
}
