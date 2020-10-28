package javasudoku.view;

import java.util.Observable;
import java.util.Observer;
import javax.swing.JButton;
import javasudoku.model.SudokuCell;
import javasudoku.model.SudokuBoard;

/**
 *
 * @author Ishaiah Cross
 */
public class SudokuView extends javax.swing.JFrame implements Observer {

    /**
     * Creates new form GUIJavaSudokuView
     */
    public SudokuView() {
        initComponents();
        newGameButton.requestFocusInWindow();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        sudokuBoardPanel = new javasudoku.view.BoardPanel();
        optionsPanel = new javax.swing.JPanel();
        showSolButton = new javax.swing.JButton();
        changeUserButton = new javax.swing.JButton();
        quitButton = new javax.swing.JButton();
        newGameButton = new javax.swing.JButton();
        loadGameButton = new javax.swing.JButton();
        playerName = new javax.swing.JLabel();
        errorLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setBackground(javax.swing.UIManager.getDefaults().getColor("Actions.Grey"));
        setForeground(java.awt.Color.white);
        setMinimumSize(new java.awt.Dimension(630, 535));
        setName("sudokuFrame"); // NOI18N
        setResizable(false);
        setSize(new java.awt.Dimension(630, 535));

        optionsPanel.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Options", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Segoe UI", 0, 14), new java.awt.Color(102, 102, 102))); // NOI18N

        showSolButton.setText("Show Solution");

        changeUserButton.setText("Change User");

        quitButton.setBackground(new java.awt.Color(255, 153, 153));
        quitButton.setForeground(new java.awt.Color(0, 0, 0));
        quitButton.setText("Quit Game");
        quitButton.setFocusTraversalPolicyProvider(true);
        quitButton.setNextFocusableComponent(newGameButton);

        newGameButton.setText("New Game");

        loadGameButton.setText("Load Game");

        javax.swing.GroupLayout optionsPanelLayout = new javax.swing.GroupLayout(optionsPanel);
        optionsPanel.setLayout(optionsPanelLayout);
        optionsPanelLayout.setHorizontalGroup(
            optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionsPanelLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(loadGameButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(changeUserButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(showSolButton, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(newGameButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(quitButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(6, 6, 6))
        );
        optionsPanelLayout.setVerticalGroup(
            optionsPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(optionsPanelLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(newGameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(loadGameButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(showSolButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(changeUserButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 214, Short.MAX_VALUE)
                .addComponent(quitButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        playerName.setFont(new java.awt.Font("Segoe UI", 0, 18)); // NOI18N
        playerName.setText("Player:");

        errorLabel.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        errorLabel.setForeground(new java.awt.Color(255, 51, 51));

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addContainerGap(13, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(playerName)
                        .addGap(253, 253, 253)
                        .addComponent(errorLabel))
                    .addGroup(layout.createSequentialGroup()
                        .addComponent(sudokuBoardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(optionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap(11, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(playerName)
                    .addComponent(errorLabel))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(sudokuBoardPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(optionsPanel, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(25, 25, 25))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents


    // Variables declaration - do not modify//GEN-BEGIN:variables
    public javax.swing.JButton changeUserButton;
    public javax.swing.JLabel errorLabel;
    public javax.swing.JButton loadGameButton;
    public javax.swing.JButton newGameButton;
    private javax.swing.JPanel optionsPanel;
    public javax.swing.JLabel playerName;
    public javax.swing.JButton quitButton;
    public javax.swing.JButton showSolButton;
    public javasudoku.view.BoardPanel sudokuBoardPanel;
    // End of variables declaration//GEN-END:variables

    @Override
    public void update(Observable o, Object arg) {
        //TODO: Make the SudokuBoardPanel represent the model
        if(arg instanceof javasudoku.model.SudokuBoard) {
            //Use the received SudokuBoard object to extract the board data
            SudokuBoard boardObj = (SudokuBoard)arg;
            SudokuCell[][] boardArray = boardObj.getBoard();
            
            //Update the BoardPanel to match
            int x;
            int y = 0;
            
            for(SudokuCell[] row: boardArray) {
                x = 0;
                for(SudokuCell cell: row) {
                    sudokuBoardPanel.setCellValue(x, y, cell.getCellValue());
                    x++;
                }
                y++;
            }
        }
    }
    
    public JButton getQuitButton() {
        return quitButton;
    }
}
