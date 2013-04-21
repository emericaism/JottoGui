package ui;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.concurrent.ExecutionException;
import java.util.regex.Pattern;

import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.SwingWorker;
import javax.swing.table.DefaultTableModel;

import model.JottoModel;

/**
 * JottoGUI is designed to be viewed by the user. The GUI is composed of
 * textboxes, buttons, text, and a TableModel.
 * 
 * The user can enter input into the GUI and it will be passed to the server.
 * The user also sees the output from his guess in the GUI. JottoModel sends the
 * messages to the server, and shows the responses to the user.
 * 
 * The user can enter a puzzle number into the textbox next to puzzle Number.
 * The user can enter the guess in the textbox next to the words "Enter guess."
 * After entering text, the user's input is sent to the server. The
 * BackgroundSwing class allows that the responses from the server are received
 * in the background, and will appear in the GUI when they response appears.
 */

public class JottoGUI extends JFrame {

    private JButton newPuzzleButton;
    private JTextField newPuzzleNumber;
    private JLabel puzzleNumber;
    private JTextField guess;
    private JTable guessTable;
    private JLabel guessLabel;
    private DefaultTableModel mod;
    private java.awt.Container content;
    private GroupLayout layout;
    private JottoModel jottomod;
    private BackgroundSwing bswing;
    public int row_index = 0;
    public int col_index = 0;

    /*
     * JottoGUI is a constructor that designs the GUI. JottoGUI is responsible
     * for creating the window through which the user interacts with the game.
     */
    public JottoGUI() {
        this.jottomod = new JottoModel();
        this.bswing = new BackgroundSwing();
        this.content = this.getContentPane();
        this.layout = new GroupLayout(content);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //We do this so that the program closes when the GUI is closed. Otherwise we could close the GUI and the program would
        //continue to run.
        content.setLayout(layout);
        //Optional ScrollPane
        //content.add(new JScrollPane(), BorderLayout.CENTER);

        // New Puzzle
        newPuzzleButton = new JButton("Generate Puzzle");
        newPuzzleButton.setName("newPuzzleButton");
        newPuzzleButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateNewPuzzle();
            }
        });
        // The actionListener permits the user to interact with the button. If
        // the action is performed,
        // we want to generateNewPuzzle.
        newPuzzleNumber = new JTextField();
        newPuzzleNumber.setName("newPuzzleNumber");
        // we may have to deal with a situation where the user presses
        // the enter button while the cursor is active on the puzzleNumber Box
        newPuzzleNumber.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                generateNewPuzzle();
            }
        });

        String puzzNum = this.jottomod.getPuzzNum();
        puzzleNumber = new JLabel("Puzzle #" + puzzNum);
        puzzleNumber.setName("puzzleNumber");

        guessLabel = new JLabel("Enter guess:");

        guess = new JTextField();
        guess.setName("guess");
        guess.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sendGuess();
            }
        });
        // table where we will store the guesses and their outcomes.
        mod = new DefaultTableModel(new String[] { "", "", "" }, 0);

        guessTable = new JTable() {

            private static final long serialVersionUID = 1L;

            //Makes it such that user cannot click into cells and change them.
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            }
        };
        guessTable.setName("guessTable");

        makeLayout(this);
        this.setMinimumSize(new Dimension(700, 600));
        //this.pack();
        // Possible scrollpane or .pack()

    }

    /*
     * This functions makes the layout for the textboxes, buttons, table.
     * 
     * @param jottoGUI, the JottoGUI for whom we are making a layout.
     */
    private static void makeLayout(JottoGUI jottoGUI) {
        jottoGUI.layout.setAutoCreateGaps(true);
        jottoGUI.layout.setAutoCreateContainerGaps(true);
        jottoGUI.layout.setHorizontalGroup(jottoGUI.layout
                .createParallelGroup()
                .addGroup(
                        jottoGUI.layout.createSequentialGroup()
                                .addComponent(jottoGUI.puzzleNumber)
                                .addComponent(jottoGUI.newPuzzleButton)
                                .addComponent(jottoGUI.newPuzzleNumber))
                .addGroup(
                        jottoGUI.layout.createSequentialGroup()
                                .addComponent(jottoGUI.guessLabel)
                                .addComponent(jottoGUI.guess))
                .addComponent(jottoGUI.guessTable));
        jottoGUI.layout.setVerticalGroup(jottoGUI.layout
                .createSequentialGroup()
                .addGroup(
                        jottoGUI.layout
                                .createParallelGroup(
                                        GroupLayout.Alignment.BASELINE)
                                .addComponent(jottoGUI.puzzleNumber)
                                .addComponent(jottoGUI.newPuzzleButton)
                                .addComponent(jottoGUI.newPuzzleNumber))
                .addGroup(
                        jottoGUI.layout
                                .createParallelGroup(
                                        GroupLayout.Alignment.BASELINE)
                                .addComponent(jottoGUI.guessLabel)
                                .addComponent(jottoGUI.guess))
                .addComponent(jottoGUI.guessTable));

    }

    /*
     * Includes the user's guess in the textbox to the table below. Makes
     * BackgroundSwing to do the send/receive to the server in the background.
     * The response will appear as soon as it arrives.
     */

    private void sendGuess() {
        final String guessText = this.guess.getText();
        String[] full_row = new String[3];
        full_row[0] = guessText;
        full_row[1] = "";
        full_row[2] = "";
        this.mod.addRow(full_row);
        this.guessTable.setModel(mod);
        col_index++;
        this.bswing = new BackgroundSwing(jottomod, row_index, col_index, mod,
                guessText);
        bswing.execute();
        this.guess.setText("");
        row_index++;
        col_index = 0;

    }

    /*
     * This generates a new puzzle. If the textbox adjacent to the
     * "Generate Puzzle" button is empty, it randomly selects a puzzle number.
     * If the user has input a number into the textbox, then it will make a
     * puzzle of that number.
     */

    private void generateNewPuzzle() {
        row_index = 0;
        col_index = 0;
        String pn = this.newPuzzleNumber.getText();
        if (pn.isEmpty() | pn.matches("^[0-9]+$") == false) {
            this.jottomod = new JottoModel();
            this.puzzleNumber.setText("Puzzle #" + this.jottomod.getPuzzNum());
        } else {
            this.puzzleNumber.setText("Puzzle #" + pn);
            this.jottomod = new JottoModel(pn);
        }
        this.mod = new DefaultTableModel(new String[] { "", "", "" }, 0);
        this.guessTable.setModel(mod);
        this.newPuzzleNumber.setText("");

    }

    /*
     * 6.005 staff method for running the program.
     * 
     * @param args, arguments being passed in by 6.005 staff to run the game.
     */

    public static void main(final String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                JottoGUI main = new JottoGUI();

                main.setVisible(true);
            }
        });
    }
}
