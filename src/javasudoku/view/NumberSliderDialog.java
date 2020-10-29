package javasudoku.view;
import java.awt.GridLayout;
import javax.swing.*;

/**
 * A simple dialog prompt for the user to select a number using a slider
 * @author Ishaiah Cross
 */
public class NumberSliderDialog {
    
    /**
     * Displays a prompt for the user to select a number using a JSlider
     * @param text The text to display on the prompt
     * @param title The title of the prompt window
     * @param min The minimum value of the slider
     * @param max The maximum value of the slider
     * @return The value of the slider if the user selected 'OK', or null if the user selected cancel
     */
    public static Integer prompt(String text, String title, int min, int max) {
        JPanel panel = new JPanel(new GridLayout(0, 1));
        
        JLabel sliderPromptText = new JLabel(text);
        JSlider inputSlider = new JSlider(min, max);
        inputSlider.setMajorTickSpacing(10);
        inputSlider.setPaintTicks(true);
        inputSlider.setPaintLabels(true);
        
        panel.add(sliderPromptText);
        panel.add(inputSlider);
        
        int selection = JOptionPane.showConfirmDialog(null, panel, title,
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
        
        if(selection == JOptionPane.OK_OPTION)
            return inputSlider.getValue();
        
        return null;
    }
}
