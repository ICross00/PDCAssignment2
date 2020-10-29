package javasudoku.model;

import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;

/**
 * A model for storing SudokuGame objects as table entries to be displayed
 * in table format. This model is derived from the model specified in the 
 * AbstractTableModel class
 * 
 * The table implementation allows for SudokuGame objects to be presented with the following
 * columns in table form:
 * 
 * Player Name | Save Date | Completion Percentage
 * name1         date1       percentage1
 * name2         date2       percentage2
 * ...
 * 
 * @author Ishaiah Cross
 */
public class SavedGameModel extends AbstractTableModel {
    private final String[] columnHeaders = { "Player Name", "Save Date", "Completion Percentage" };
    private ArrayList<SudokuGame> games = new ArrayList<>();
    
    public SavedGameModel(ArrayList<SudokuGame> games) {
        this.games = games;
    }
    
    /**
     * Returns the object 
     * @param rowIndex
     * @return 
     */
    public SudokuGame getGame(int rowIndex) {
        return games.get(rowIndex);
    }
    
    @Override
    public String getColumnName(int columnIndex){
         return columnHeaders[columnIndex];
    }
    
    @Override
    public int getRowCount() {
        return games.size();
    }

    @Override
    public int getColumnCount() {
        return columnHeaders.length;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        SudokuGame selectedGame = games.get(rowIndex);

        switch(columnIndex) {
            case 0:
                return selectedGame.playerName;
            case 1:
                return selectedGame.lastPlayedDate;
            case 2:
                return String.format("%.1f%%", 100 * selectedGame.completionPercentage); //Calculate and format the percentage
        }
        
        return null;
    }
}
