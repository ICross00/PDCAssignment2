package javasudoku.view;

import java.awt.GridLayout;
import java.util.ArrayList;
import javax.swing.*;

/**
 * A simple dialog prompt for the user to select a string option from a list, or
 * enter their own option
 * 
 * @author Ishaiah Cross
 */
public class ListOptionDialog {
    
    /**
     * Displays a prompt for the user to select an option from a list of options displayed
     * as strings.
     * @param text The text to display to the user in the dialog
     * @param title The title of the prompt window
     * @param options The options to display in the list. 
     * @return The index the user selected, or null if the user selected cancel
     */
    public static Integer prompt(String text, String title, ArrayList<String> options) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        
        
        //JList object to display existing options
        JLabel promptLabel = new JLabel(text);
        
        DefaultListModel<String> listModel = new DefaultListModel<>();
        JList selectionList = new JList(listModel);
        selectionList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        for(String option: options)
            listModel.addElement(option);
        
        panel.add(promptLabel);
        panel.add(selectionList);
        
        int selection = JOptionPane.showConfirmDialog(null, panel, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if(selection == JOptionPane.OK_OPTION)
            return selectionList.getSelectedIndex();
        
        return null;
    }
}
