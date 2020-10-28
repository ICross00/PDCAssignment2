package javasudoku.view;

/**
 * A wrapper class to contain the user name and difficulty setting
 * as returned from a NameInputDialog instance
 * @author Ishaiah Cross
 */
public class GameOptions {
    public final String name;
    public final int difficulty;
    
    public GameOptions(String name, int difficulty) {
        this.name = name;
        this.difficulty = difficulty;
    }
}
