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
import javax.swing.JOptionPane;
/**
 * Connects a SudokuModel and SudokuView instance
 * @author Ishaiah Cross
 */
public class SudokuController {
    //TODO: Connect the model and the view using observer/observable interface
    //View is the observer, model is the observable
    private final SudokuModel model;
    private final SudokuView view;
    private SimpleDocumentListener boardInputListener;
    
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
            requestStartNewGame();
        });
        
        //Load Game button clicked
        view.loadGameButton.addActionListener((ActionEvent e) -> {
            requestLoadGame();
        });
        
        //Quit Game button clicked
        view.quitButton.addActionListener((ActionEvent e) -> {
            view.setVisible(false);
            view.dispose();
        });
        
        //Show Solution button clicked
        view.showSolButton.addActionListener((ActionEvent e) -> {
            requestShowSolution();
        });
        
        //Change User button clicked
        view.changeUserButton.addActionListener((ActionEvent e) -> {
            requestChangeUser();
        });
        
        
        //Set up listener for when cell values are changed or clicked on
        this.boardInputListener = new CellPanelEventController(view, model);
    }
    
    /**
     * Prompts the user with a dialog asking if they wish to view
     * the solution of the game
     */
    private void requestShowSolution() {
        //Prompt show solution
        boolean reveal = YesNoDialog.prompt("Reveal the solution?", "Show Solution");
        
        //Solve the board
        if(reveal)
            model.solveBoard();
    }
    
    private void requestChangeUser() {
        //Prompt the user to enter their desired name
        if(promptEnterName()) {
            //If there is a game in progress, prompt the user to save
            if(!model.isBoardEmpty()) {
                requestSaveGame();
                model.startEmptyBoard(); //Clear the board
            }
        }
    }
    
    /**
     * Prompts the user to save their game
     * A dialog with options 'yes' and 'no' will be displayed
     * If 'yes' is selected, the game will be written to the database
     * @return True if the user selected to save, false if not
     */
    private boolean requestSaveGame() {
        //Prompt user to save their game to the database
        boolean save = YesNoDialog.prompt("Save your game?", "Save Game");

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
    private boolean promptEnterName() {
        //Get user name and desired difficulty using dialogs
        String name = JOptionPane.showInputDialog(null, "Please enter your name (max 30 characters)", 
                "Enter Name", JOptionPane.INFORMATION_MESSAGE);
       
        if(name == null) //If cancel was clicked, return false
            return false;
        
        if(name.isEmpty()) { //If the name is empty, display an error
            JOptionPane.showMessageDialog(null, "Please enter a valid name.", 
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return false;
        }
        
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
        
        //Prompt the user to enter their name if no name has been entered
        if(!model.hasValidPlayer()) {
            boolean enteredValidName = promptEnterName();
            if(!enteredValidName)
                return; //Do nothing if the user canceled or did not enter a valid name
        }
        
        Integer difficulty = NumberSliderDialog.prompt("Please select a difficulty (0 easiest, 100 hardest)", 
                "Difficulty Select", 0, 100); 
        
        if(difficulty == null) //If the user clicked cancel, do nothing
            return;

        model.startPuzzle(difficulty);
        view.activateBoard(true);
    }
    
    /**
     * Prompts the user to enter their username if they have not already chosen a name, 
     * then select an existing game from the database
     */
    private void requestLoadGame() {
        
        //Prompt the user to enter their name if no name has been entered
        if(!model.hasValidPlayer()) {
            boolean enteredValidName = promptEnterName();
            if(!enteredValidName)
                return; //Do nothing if the user canceled or did not enter a valid name
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
                        
            //Valid selection received, convert it to an object and load it into the model
            SudokuGame selectedGame = tableModel.getGame(selectedRow);
            model.importGame(selectedGame);
            view.activateBoard(true);
        }
    }
}
