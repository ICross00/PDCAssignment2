package javasudoku.view;
import java.awt.GridLayout;
import javax.swing.*;

/**
 * A simple dialog prompt for the user to input their name
 * @author Ishaiah Cross
 */
public class NameDifficultyDialog {
    
    /**
     * Displays a prompt for the user to input their name
     * @return The name that the user entered, or null if 'cancel' was clicked
     */
    public static GameOptions prompt() {
        JLabel namePromptLabel = new JLabel("Enter your name (max 30 characters): ");
        JTextField nameInputField = new JTextField();
        JPanel panel = new JPanel(new GridLayout(0, 1));
        
        JLabel difficultyPromptDialog = new JLabel("Select a difficulty (0 is easiest, 100 is hardest)");
        JSlider difficultySlider = new JSlider(0, 100);
        difficultySlider.setMajorTickSpacing(10);
        difficultySlider.setPaintTicks(true);
        difficultySlider.setPaintLabels(true);
        
        panel.add(namePromptLabel);
        panel.add(nameInputField);
        panel.add(difficultyPromptDialog);
        panel.add(difficultySlider);
        
        int selection = JOptionPane.showConfirmDialog(null, panel, "Enter your name",
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if(selection == JOptionPane.OK_OPTION) {
            String resp = nameInputField.getText().trim();
            int difficulty = difficultySlider.getValue();
            
            if(resp.isEmpty())
                return null;
            
            return new GameOptions(resp, difficulty);
        }
        
        return null;
    }
}
