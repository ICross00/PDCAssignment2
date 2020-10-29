package javasudoku.controller;
import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import java.awt.event.ActionEvent;
import javasudoku.dbaccess.SudokuDBManager;
import javasudoku.model.SudokuBoard;
import javasudoku.model.SudokuCoordinate;
import javasudoku.model.SudokuModel;
import javasudoku.view.CellPanel;
import javasudoku.view.NumberSliderDialog;
import javasudoku.view.YesNoDialog;
import javasudoku.view.SudokuView;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

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
    private JTextField activeField;
    private CellPanel activeCellPanel;
    
    public SudokuController(SudokuModel model, SudokuView view) {
        activeField = null;
        activeCellPanel = null;
        
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
        
        //Quit Game button clicked
        view.quitButton.addActionListener((ActionEvent e) -> {
            view.setVisible(false);
            view.dispose();
        });
        
        //Show Solution button clicked
        view.showSolButton.addActionListener((ActionEvent e) -> {
            requestShowSolution();
        });
        
        view.changeUserButton.addActionListener((ActionEvent e) -> {
            requestChangeUser();
        });
        
        //Setup listener for when cells are clicked
        Toolkit.getDefaultToolkit().addAWTEventListener(new OnCellClicked(), 
                AWTEvent.MOUSE_EVENT_MASK);
        
        //Setup listener for when a cell value is changed
        this.boardInputListener = new OnCellValueUpdated();
    }
    
    /**
     * Prompts the user with a dialog asking if they wish to view
     * the solution of the game
     */
    private void requestShowSolution() {
        //Prompt show solution
        boolean reveal = YesNoDialog.prompt("Reveal the solution? The game will not be saved.", "Show Solution");
        
        //Solve the board
        if(reveal)
            model.solveBoard();
    }
    
    private void requestChangeUser() {
        //Prompt the user to enter their desired name
        String nextName = promptEnterName();
        if(nextName != null) {
            //If there is a game in progress, prompt the user to save
            if(!model.isBoardEmpty()) {
                requestSaveGame();
            }
        
            model.startEmptyBoard();
            model.setPlayerName(nextName);
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
        boolean save = YesNoDialog.prompt("Save your game?", "Unsaved Progress");

        if(save) {
            //Use the database manager to save the game to the database
            SudokuDBManager dbaccess = SudokuDBManager.getInstance();
            dbaccess.addGame(model.exportGame());
        }
        
        return save;
    }
    
    /**
     * Prompts the user to enter their name 
     * @return The name that the user entered, or null if 
     * the user either canceled the operation or entered an empty name
     */
    private String promptEnterName() {
        //Get user name and desired difficulty using dialogs
        String name = JOptionPane.showInputDialog(null, "Please enter your name (max 30 characters)", 
                "Enter Name", JOptionPane.INFORMATION_MESSAGE);
       
        if(name.isEmpty()) { //If the name is empty, display an error
            JOptionPane.showMessageDialog(null, "Please enter a valid name.", 
                    "Error", JOptionPane.INFORMATION_MESSAGE);
            return null;
        }
        
        return name;
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
            String name = promptEnterName();
            if(name == null)    //Do nothing if the user canceled or did not enter a valid name
                return;

            //Start the game
            view.playerName.setText("Player: " + name); //Update the display label
            model.setPlayerName(name);
        }
        
        Integer difficulty = NumberSliderDialog.prompt("Please select a difficulty (0 easiest, 100 hardest)", 
                "Difficulty Select", 0, 100); 
        
        if(difficulty == null) //If the user clicked cancel, do nothing
            return;

        model.startPuzzle(difficulty);
    }
    
    /**
     * Marks a JTextField as active, meaning the controller will respond to changes
     * in the field's value and update the model accordingly. This should be 
     * called on the field that the user has clicked on.
     * 
     * @param field The field to activate
     */
    private void attachTextFieldListener(JTextField field) {
        if(activeField != null) { //Stop listening to the previous field
            activeField.getDocument().removeDocumentListener(boardInputListener);
        } 
        
        //Set the currently selected field to active
        activeField = field;
        activeField.getDocument().addDocumentListener(boardInputListener);
    }
    
    /**
     * Class to handle the selection of text fields, as well as keeping track
     * of which text field the user has selected by listening to mouse events
     */
    private class OnCellClicked implements AWTEventListener {
        @Override
        public void eventDispatched(AWTEvent e) {
            Object source = e.getSource();
            
            //Check if the event corresponded to a mouse release on a text field
            if (e.getID() == MouseEvent.MOUSE_RELEASED && source instanceof JTextField) {
                JTextField textInput = (JTextField)e.getSource();
                
                Component parent = textInput.getParent(); //Get the parent cell of the text input
                if(parent instanceof CellPanel) {
                    //Begin listening to changes to this cell
                    attachTextFieldListener(textInput); 
                    CellPanel cell = (CellPanel)textInput.getParent();
                    activeCellPanel = cell;
                    
                    System.out.println("Selected cell at " + activeCellPanel.getCoordinate());
                }
            }
        }
    }
    
    /**
     * Class to handle user input via text fields, and the corresponding
     * updates needed to be done on the underlying model. The update function
     * of this class is run every time the value inside a cell is changed
     */
    private class OnCellValueUpdated implements SimpleDocumentListener {

        @Override
        public void update(DocumentEvent e) {
            SudokuCoordinate selectedCoord = activeCellPanel.getCoordinate();
            String cellText = activeField.getText();
            
            SudokuBoard modelBoard = model.getBoard();
            //If the text was removed, remove the cell and do nothing
            if(cellText.isEmpty()) {
                modelBoard.setCell(selectedCoord, 0);
            } else {
                try {
                    int userValue = activeCellPanel.getValue(); //get the value the user entered
                    //Update the underlying model
                    boolean couldPlace = modelBoard.setCell(selectedCoord, userValue);
                    //Display the number as red if it was not a valid placement
                    activeCellPanel.setValid(couldPlace);
                } catch (NumberFormatException ex) {
                    System.out.println("Text field at " + selectedCoord + " contains an unexpected value");
                    activeField.setText(""); //Clear the text if invalid input somehow occurs
                }
            }
        }
    }
}
