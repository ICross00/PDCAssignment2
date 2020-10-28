package javasudoku;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javasudoku.controller.SudokuController;
import javasudoku.dbaccess.SudokuDBManager;
import javasudoku.model.SudokuModel;
import javasudoku.view.SudokuView;


/**
 * Entry point for the JavaSudoku program
 * @author Ishaiah Cross
 */
public class SudokuApp {

    public static void main(String[] args) {
       SudokuDBManager dbmanage = SudokuDBManager.getInstance();

       SudokuModel model = new SudokuModel("test");
       SudokuView view = new SudokuView();
       SudokuController controller = new SudokuController(model, view);
       controller.connectModelView();
       
       view.setVisible(true);
       
       //Set up a listener to close the database connection when the frame is closed
       view.addWindowListener(new WindowAdapter() {
          @Override
          public void windowClosing(WindowEvent event) {
              dbmanage.closeConnection();
          }
       });
    }
}
