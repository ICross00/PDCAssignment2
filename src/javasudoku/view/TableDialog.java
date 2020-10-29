package javasudoku.view;

import java.awt.GridLayout;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.AbstractTableModel;

/**
 * A simple dialog prompt for the user to select an option from a table
 * using a supplied model
 * 
 * @author Ishaiah Cross
 */
public class TableDialog {
    
    /**
     * Displays a prompt for the user to select an option from a table
     * @param title The title of the prompt window
     * @param tableModel The model for the table to display
     * @return The row index the user selected, or null if the user selected cancel
     */
    public static Integer prompt(String title, AbstractTableModel tableModel) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        
        JTable selectionTable = new JTable();
        selectionTable.setModel(tableModel);
        selectionTable.setCellSelectionEnabled(false);
        selectionTable.setRowSelectionAllowed(true);
        selectionTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        selectionTable.getTableHeader().setResizingAllowed(false);
        selectionTable.getTableHeader().setReorderingAllowed(false);
        
        panel.add(new JScrollPane(selectionTable));
        
        int selection = JOptionPane.showConfirmDialog(null, panel, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if(selection == JOptionPane.OK_OPTION)
            return selectionTable.getSelectedRow();
        
        return null;
    }
}
