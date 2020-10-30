package javasudoku.controller;
import java.awt.event.ActionEvent;
import java.util.ArrayList;
import javasudoku.dbaccess.SudokuDBManager;
import javasudoku.model.SavedGameModel;
import javasudoku.model.SudokuGame;
import javasudoku.model.SudokuModel;
import javasudoku.view.NumberSliderDialog;
import javasudoku.view.YesNoDialog;
import javasudoku.view.SudokuView;
import javasudoku.view.TableDialog;
import javasudoku.view.ListOptionDialog;
import javax.swing.JOptionPane;

/**
 * TODO: Implement a dialog that allows the user to either enter a new user name,
 * or select from a list of existing players using the get player names method in 
 * SudokuDBManager
 * 
 * TODO:
 * Make the request change user button display this list and get the option from the user's selection
 * Add a 'new user' button that prompts the user to enter their name
 * Add a help button
 * Describe MVC implementation in a document
 * Create unit tests
 */

/**
 * Connects a SudokuModel and SudokuView instance
 * @author Ishaiah Cross
 */
public class SudokuController {
    private final SudokuModel model;
    private final SudokuView view;
    private CellPanelEventController boardInputListener;
    private boolean enableSaving;
    
    public SudokuController(SudokuModel model, SudokuView view) {   
        this.model = model;
        this.view = view;
        
        //Disable the text inputs on the board until a game has been started
        view.sudokuBoardPanel.setActive(false);
    }
    
    /**
     * Adds the SudokuView as an Observer of the Observable SudokuModel
     * Sets up action listeners for the SudokuView UI elements
     * to provide interactivity with the SudokuModel
     */
    public void connectModelView() {
        model.addObserver(view);
        
        //New Game button clicked
        view.newGameButton.addActionListener((ActionEvent e) -> {
            view.setValid(); //Clear away any red (invalid) cells, as a new game will not have any
            requestStartNewGame();
        });
        
        //Load Game button clicked
        view.loadGameButton.addActionListener((ActionEvent e) -> {
            view.setValid(); //Clear away any red (invalid) cells, as a loaded game will not have any
            requestLoadGame();
        });
        
        //Quit Game button clicked
        view.quitButton.addActionListener((ActionEvent e) -> {
            requestSaveGame();
            view.setVisible(false);
            view.dispose();
        });
        
        //Show Solution button clicked
        view.showSolButton.addActionListener((ActionEvent e) -> {
            view.setValid(); //Clear away any red (invalid) cells, as a solved game will not have any
            requestShowSolution();
        });
        
        //Change User button clicked
        view.changeUserButton.addActionListener((ActionEvent e) -> {
            requestChangeUser();
        });
        
        //New User button clicked
        view.newUserButton.addActionListener((ActionEvent e) -> { 
            requestNewUser();
        });
        
        //Set up listener for when cell values are changed or clicked on
        this.boardInputListener = new CellPanelEventController(view, model, () -> {
            onBoardSolved();
        });
    }
    
    /**
     * Called whenever the active board is fully solved
     */
    private void onBoardSolved() {
        //Disable the board to prevent future input
        view.activateBoard(false);
        
        //Display a solved notification
        JOptionPane.showMessageDialog(null, "To play again, start a new game or load an existing game using the options on the right.", 
                    "Puzzle Completed", JOptionPane.INFORMATION_MESSAGE);
        
        //Disable saving of a solved board
        this.enableSaving = false;
    }
    
    /**
     * Prompts the user with a dialog asking if they wish to view
     * the solution of the game
     */
    private void requestShowSolution() {
        //If no game has been started, display an error
        if(model.isBoardEmpty() || !model.hasValidPlayer()) {
            JOptionPane.showMessageDialog(null, "Please start a game to view the solution.", 
            "Error", JOptionPane.INFORMATION_MESSAGE);
            
            return;
        }
        
        //Prompt show solution
        boolean reveal = YesNoDialog.prompt("Reveal the solution? You will not be able to edit or save the board, and will need to start a new game.", 
                "Show Solution");
        
        //Solve the board
        if(reveal) {
            model.solveBoard();
            //Disable the board and disable saving
            view.activateBoard(false);
            this.enableSaving = false;
        }
    }
    
    /**
     * Prompts the user to select a user from a list of existing users
     */
    private void requestChangeUser() {
        //Get the unique user names from the database
        ArrayList<String> userOptions = SudokuDBManager.getInstance().getUserNames();
        
        if(userOptions.isEmpty()) {
            //If there were no users, prompt the user to create a new one and exit
            JOptionPane.showMessageDialog(null, "No users found. Please create a new user using the 'New User' button.", 
                    "No existing users", JOptionPane.INFORMATION_MESSAGE);
            return;
        }
        
        //Display a prompt and get the user's selection from the list
        Integer selectedRow = ListOptionDialog.prompt("Select a user", "User selection", userOptions);
        
        if(selectedRow == null) //If the user clicked cancel, do nothing
            return;

        if(selectedRow == -1) { //If the user did not select a row, display an error
            JOptionPane.showMessageDialog(null, "Please select a row.", 
                "Error", JOptionPane.INFORMATION_MESSAGE);
            
            return;
        }
        
        //If there is a game in progress, prompt the user to save
        if(!model.isBoardEmpty()) {
            requestSaveGame();
            model.startEmptyBoard(); //Clear the board
        }
        
        //Get the selection and update the model
        String name = userOptions.get(selectedRow);
        model.setPlayerName(name);
    }
    
    /**
     * Prompts the user to save their game
     * A dialog with options 'yes' and 'no' will be displayed
     * If 'yes' is selected, the game will be written to the database
     * @return True if the user selected to save, false if not
     */
    private boolean requestSaveGame() {
        //If saving is not permitted (The board is solved), do nothing
        if(!this.enableSaving)
            return false;
        
        //Prompt user to save their game to the database, confirming the current username
        boolean save = YesNoDialog.prompt("Save your game under '" + model.getPlayerName() 
                + "'? Any invalid cells will not be saved.", "Save Game");

        if(save) {
            //Use the database manager to save the game to the database
            SudokuDBManager dbaccess = SudokuDBManager.getInstance();
            dbaccess.addGame(model.exportGame());
        }
        
        return save;
    }
    
    /**
     * Prompts the user to enter their name, and updates the model accordingly 
     * if a valid name was entered
     * @return True if a valid name was entered, false if an invalid name was
     * entered or the user clicked 'cancel'
     */
    private boolean requestNewUser() {
        //Get user name and desired difficulty using dialogs
        String name = JOptionPane.showInputDialog(null, "Please enter your name (max 15 characters)", 
                "Enter Name", JOptionPane.INFORMATION_MESSAGE);
       
        if(name == null) //If cancel was clicked, return false
            return false;
        
        if(name.isEmpty()) { //If the name is empty, display an error
            JOptionPane.showMessageDialog(null, "Please enter a valid name.", 
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        
        //Ask if the user wishes to save their game before finalizing the update
        if(!model.isBoardEmpty()) {
            requestSaveGame();
            model.startEmptyBoard();
        }
                
        //Trim the name to a maximum of 15 characters
        name = name.substring(0, Math.min(name.length(), 15));
        model.setPlayerName(name);
        return true;
    }
    
    /**
     * Prompts the user to start a new Sudoku game
     * The user will be prompted to enter their name and the desired difficulty of the puzzle
     * If a game is already in progress, the user will be asked if they wish to save their game
     */
    private void requestStartNewGame() {
        //Check if a game is already in progress
        if(!model.isBoardEmpty()) {
            requestSaveGame();
        }
        
        //Prompt the user to select a user or enter their name if no user has been set
        if(!model.hasValidPlayer()) {
            JOptionPane.showMessageDialog(null, "Please select a user or create a new user before starting a new game.", 
                    "Select a User", JOptionPane.INFORMATION_MESSAGE);
            
            return;
        }
        
        Integer difficulty = NumberSliderDialog.prompt("Please select a difficulty (0 easiest, 100 hardest)", 
                "Difficulty Select", 0, 100); 
        
        if(difficulty == null) //If the user clicked cancel, do nothing
            return;

        model.startPuzzle(difficulty);
        view.activateBoard(true);
        
        //Re-enable saving
        this.enableSaving = true;
    }
    
    /**
     * Prompts the user to enter their username if they have not already chosen a name, 
     * then select an existing game from the database
     */
    private void requestLoadGame() {
        
        //Prompt the user to select a user or enter their name if no user has been set
        if(!model.hasValidPlayer()) {
            JOptionPane.showMessageDialog(null, "Please select a user or create a new user before starting a new game.", 
                    "Select a user", JOptionPane.INFORMATION_MESSAGE);
            
            return;
        }
        
        //Check if a game is already in progress
        if(!model.isBoardEmpty()) {
            requestSaveGame();
        }
        
        //Query the database for games the user has previously played
        SudokuDBManager db = SudokuDBManager.getInstance();
        ArrayList<SudokuGame> pastGames = db.getGamesByName(model.getPlayerName());
        
        if(pastGames.isEmpty()) { //If this user has not played any games previously, display an error
            JOptionPane.showMessageDialog(null, "You do not have any saved games. Please start a new game or select a new user.", 
                    "Error", JOptionPane.INFORMATION_MESSAGE);
        } else {    
            //Prompt the user to select a saved game from the database
            SavedGameModel tableModel = new SavedGameModel(pastGames);
            Integer selectedRow = TableDialog.prompt("Select a game: ", tableModel);
            
                        
            if(selectedRow == null) //If the user clicked cancel, do nothing
                return;
            
            if(selectedRow == -1) { //If the user did not select a row, display an error
                JOptionPane.showMessageDialog(null, "Please select a row.", 
                    "Error", JOptionPane.INFORMATION_MESSAGE);
                
                return;
            }
                        
            //Valid selection received, get the game object from the table model and load it into the game model
            SudokuGame selectedGame = tableModel.getGame(selectedRow);
            model.importGame(selectedGame);
            view.activateBoard(true);
            
            //Re-enable saving
            this.enableSaving = true;
        }
    }
}
