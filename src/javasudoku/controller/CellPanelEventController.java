package javasudoku.controller;

import java.awt.AWTEvent;
import java.awt.Component;
import java.awt.Toolkit;
import java.awt.event.AWTEventListener;
import java.awt.event.MouseEvent;
import javasudoku.model.SudokuBoard;
import javasudoku.model.SudokuCoordinate;
import javasudoku.model.SudokuModel;
import javasudoku.view.CellPanel;
import javasudoku.view.SudokuView;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;

/**
 * An auxiliary controller class to handle events related to the selection of cells in a 
 * SudokuView, and the corresponding model updates.
 * 
 * This class should be encapsulated inside a SudokuController instance if it is used,
 * and thus is only visible within the controller package
 * 
 * @author Ishaiah Cross
 */
class CellPanelEventController implements AWTEventListener, SimpleDocumentListener {
    private final SudokuView view;
    private final SudokuModel model;
    
    private CellPanel activePanel;
    private JTextField activeField;
    private final BoardSolvedListener solveCallback;
    
    public CellPanelEventController(SudokuView view, SudokuModel model, BoardSolvedListener solveCallback) {
        this.view = view;
        this.model = model;
        
        this.activePanel = null;
        this.activeField = null;
        
        //Begin listening to mouse events, so that we can determine what cells were clicked
        Toolkit.getDefaultToolkit().addAWTEventListener(this, AWTEvent.MOUSE_EVENT_MASK);
        //Register the solved callback
        this.solveCallback = solveCallback;
    }
    
    /**
     * @return The cell panel in the view that is currently active
     */
    public CellPanel getActivePanel() {
        return activePanel;
    }

    /**
     * @return The text field in the view that is currently active
     */
    public JTextField getActiveField() {
        return activeField;
    }
    
    /**
     * Marks a JTextField as active, meaning the event listeners in this class will respond to changes
     * in the field's value. This should be called on the field that the user has clicked on.
     * 
     * @param field The field to activate
     */
    private void attachTextFieldListener(JTextField field) {
        if(activeField != null) { //Stop listening to the previous field
            activeField.getDocument().removeDocumentListener(this);
        } 
        
        //Set the currently selected field to active
        activeField = field;
        activeField.getDocument().addDocumentListener(this);
    }
    
    /**
     * Event listener to handle the selection of text fields, as well as to keep track
     * of which text field the user has selected by listening to mouse events. 
     * 
     * @param e 
     */
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
                activePanel = (CellPanel)textInput.getParent();

                System.out.println("Selected cell at " + activePanel.getCoordinate());
            }
        }
    }
    
    /**
     * Event handler to handle user input via text fields, and the corresponding
     * updates needed to be done on the underlying model. This event is fired
     * any time the value inside a cell is changed.
     * 
     * @param e
     */
    @Override
    public void update(DocumentEvent e) {
        SudokuCoordinate selectedCoord = activePanel.getCoordinate();
        String cellText = activeField.getText();

        SudokuBoard modelBoard = model.getBoard();
        //If the text was removed, remove the cell and do nothing
        if(cellText.isEmpty()) {    
            modelBoard.setCell(selectedCoord, 0);
        } else {
            try {
                int userValue = activePanel.getValue(); //get the value the user entered
                //Update the underlying model
                boolean couldPlace = modelBoard.setCell(selectedCoord, userValue);
                
                //Display the number as red if it was not a valid placement
                activePanel.setValid(couldPlace);
                
                //If the board is solved, trigger the solved callback
                if(modelBoard.isSolved())
                    solveCallback.onBoardSolved();
                
            } catch (NumberFormatException ex) {
                System.out.println("Text field at " + selectedCoord + " contains an unexpected value");
                activeField.setText(""); //Clear the text if invalid input somehow occurs
            }
        }
    }
}
