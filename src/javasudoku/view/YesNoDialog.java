package javasudoku.view;
import javax.swing.JOptionPane;

/**
 * A simple yes/no dialog
 * @author Ishaiah Cross
 */
public class YesNoDialog {
       /**
     * Displays a dialog prompt for the user to select yes or no
     * @param text The text to display to the user
     * @param title The title of the dialog box
     * @return True if the user selected 'yes', false if the user selected 'no'
     */
    public static boolean prompt(String text, String title) {
       int dialogType = JOptionPane.YES_NO_OPTION;
       int response = JOptionPane.showConfirmDialog (null, text, title, dialogType);
       
       return response == JOptionPane.YES_OPTION;
    }
}
