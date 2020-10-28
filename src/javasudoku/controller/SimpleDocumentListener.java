package javasudoku.controller;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

/**
 * A functional interface for the DocumentListener class that combines the functionality
 * of all three of its overrideable functions into one 'update' method
 * 
 * Retrieved from https://stackoverflow.com/questions/28913312/change-listener-for-a-jtextfield
 * @author Andrey Megvinov
 */
@FunctionalInterface
public interface SimpleDocumentListener extends DocumentListener {
    void update(DocumentEvent e);

    @Override
    default void insertUpdate(DocumentEvent e) {
        update(e);
    }
    @Override
    default void removeUpdate(DocumentEvent e) {
        update(e);
    }
    @Override
    default void changedUpdate(DocumentEvent e) {
        update(e);
    }
}