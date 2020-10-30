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
 * Connects a SudokuModel and SudokuView instance
 * @author Ishaiah Cross
 */
public class SudokuController {
    private final SudokuModel model;
    private final SudokuView view;
    private CellPanelEventController boardInputListener;
    private boolean enableSaving;
    private Integer activeGameDB_ID; //The ID of the active game in the database
    
    public SudokuController(SudokuModel model, SudokuView view) {   
        this.model = model;
        this.view = view;
        
        //Disable the text inputs on the board until a game has been started
        view.sudokuBoardPanel.setActive(false);
        //Set the active game ID to null. When a game is loaded, this field is initialized
        this.activeGameDB_ID = null;
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
            requestStartNewGame();
        });
        
        //Load Game button clicked
        view.loadGameButton.addActionListener((ActionEvent e) -> {   
            requestLoadGame();
        });
        
        //Quit Game button clicked
        view.quitButton.addActionListener((ActionEvent e) -> {
             //If there is a game in progress, prompt the user to save
            if(!model.isBoardEmpty())
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
        
        view.helpButton.addActionListener((ActionEvent e) -> { 
            requestShowHelp();
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
        ArrayList<String> userOptions = SudokuDBManager.getInstance().getPlayerNames();
        
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
            
            //If the game's ID already exists in the database, remove it so it can be updated
            if(this.activeGameDB_ID != null)
                dbaccess.removeGame(this.activeGameDB_ID);
                
            //Add the game to the database
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
        view.setValid(); //Clear away any red (invalid) cells, as a new game will not have any
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
            
            //Track the ID so that the database record can be updated later
            this.activeGameDB_ID = selectedGame.getGameID();
            view.setValid(); //Clear away any red (invalid) cells, as a loaded game will not have any
        }
    }
    
    /**
     * Displays a dialog containing instructions for the user on how to
     * operate the interface
     */
    private void requestShowHelp() {
        JOptionPane.showMessageDialog(null, "The goal of Sudoku is to fill every row, column, and 3x3 subgrid on the board with"
                + "the numbers 1-9. No repeated numbers are allowed.\n To start a game, first choose a user from the 'Select User' menu "
                + "or create a new user using the 'New User' button.\n Then, either load an existing game using the 'Load Game' button or create a new game "
                + "using the 'New Game' button.\n\n"
                + ""
                + "Once started, simply click on a cell to enter a digit from 1-9. If the digit placement was not valid, the cell will be highlighted in red.\n"
                + "To undo a placement, simply remove the text from the cell.\n"
                + "You will be prompted to save your game if you quit, start a new game, load a new game, or change users.\n You can then "
                + "access your game later through the 'Load Game' option, and your progress will be saved.\n"
                + "This will overwrite your previously saved progress."
                + "\n"
                + "If the solution for a puzzle is revealed, the option to save the puzzle will not be given.\n"
                + "Warning: If the window is closed using the top-right X icon, the game will not be saved.", 
                    "Instructions", JOptionPane.INFORMATION_MESSAGE);
    }
   
}
