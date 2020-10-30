package javasudoku;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javasudoku.controller.SudokuController;
import javasudoku.dbaccess.SudokuDBManager;
import javasudoku.model.SudokuModel;
import javasudoku.view.SudokuView;
import javax.swing.UIManager;
import org.junit.internal.TextListener;
import org.junit.runner.JUnitCore;


/**
 * Entry point for the JavaSudoku program
 * @author Ishaiah Cross
 */
public class SudokuApp {

    public static void main(String[] args) {
        //Run the unit tests
        runUnitTests();
        
        //Set the look and feel
        try { 
            UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel"); 
        } catch (Exception ex) { 
            ex.printStackTrace(); 
        }
        
        //Set up the model, view, and controller, and connect the model and view using the controller
       SudokuModel model = new SudokuModel();
       SudokuView view = new SudokuView();
       SudokuController controller = new SudokuController(model, view);
       controller.connectModelView();
       
       //Display the main form
       view.setVisible(true);
       
       //Set up a listener to close the database connection when the frame is closed
       view.addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent event) {
              SudokuDBManager.getInstance().closeConnection();
          }
       });
    }
    
    /**
     * Run the unit tests provided in javasudoku.tests.SudokuBoardTest
     */
    public static void runUnitTests() {
        JUnitCore junit = new JUnitCore();
        junit.addListener(new TextListener(System.out));
        junit.run(javasudoku.tests.SudokuBoardTest.class);
    }
}
